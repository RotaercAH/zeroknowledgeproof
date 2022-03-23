package org.fisco.bcos.asset.client.electrade;

import com.starkbank.ellipticcurve.PrivateKey;
import com.starkbank.ellipticcurve.PublicKey;
import org.fisco.bcos.asset.client.TradeCountStruct;
import org.fisco.bcos.asset.client.TradeStruct;
import org.fisco.bcos.asset.client.ValueStruct;
import org.fisco.bcos.asset.contractuse.EvidenceContract;
import org.fisco.bcos.asset.contractuse.TradeContract;
import org.fisco.bcos.asset.crypto.ecdsasign.EcdsaSign;
import org.fisco.bcos.asset.crypto.ecdsasign.HexUtil;
import org.fisco.bcos.asset.crypto.ecdsasign.SignatureTwoParty;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalCipher;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalKeyPair;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalPublicKey;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.FormatProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.LinearEquationProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.VerificationFailedException;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.MoudleElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.MoudleGroup;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.PeddersenBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProof;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Business {
    /*
    public static void Usage() {
        System.out.println(" Usage:");
        System.out.println(
                "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient deploy");
        System.out.println(
                "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient query account");
        System.out.println(
                "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient register account value");
        System.out.println(
                "\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient transfer from_account to_account amount");
        System.exit(0);
    }*/
    //电能生产企业和用电企业协商电价后，电能生产企业对电价加密，生成格式正确证明、范围证明，协商双方签名并提交上链申请
    public static byte[] consensusValue(String username , BigInteger value, ElGamalPublicKey pk, PrivateKey sigsk1, PrivateKey sigsk2, EvidenceContract evidence, ZeroKonwledgeProofGorV zkp) throws Exception {
        //电价加密
        ElGamal_Ciphertext m = ElGamalCipher.encrypt(value, pk);
        //生成格式正确证明
        FormatProof fp = zkp.FormatProof(value, pk, m.r);
        //生成范围证明
        BigInteger moudle = pk.getP();
        BigInteger generator = new BigInteger("13");
        MoudleGroup group = new MoudleGroup(moudle, generator);
        MoudleElement g = group.generator().from(pk.getG());
        MoudleElement h = group.generator().from(pk.getY());
        PeddersenBase base = new PeddersenBase(g, h, group);
        zkp.GenrateRangeProofParams(32, base);
        RangeProof rp = zkp.ProveRangeProof(value, m.r);
        //生成上述电价密文、零知识证明Hash值
        List<byte[]> hash = new ArrayList<>();
        Collections.addAll(hash, HexUtil.tobyte32(m.gmyr.toByteArray()), HexUtil.tobyte32(m.hr.toByteArray()),fp.getC(),fp.getZ1(),fp.getZ2());
        hash.addAll(zkp.EncodeRangeProof(rp));
        byte[] temp = HexUtil.byteCombine(hash);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash_ = digest.digest(temp);
        //对以上内容进行双方签名
        EcdsaSign sign = new EcdsaSign();
        SignatureTwoParty twoParty = sign.ecdsasign(HexUtil.encodeHexString(hash_), sigsk1, sigsk2);
        //对上述内容编码并申请上链
        List<byte[]> ext = new ArrayList<>();
        ValueStruct struct = new ValueStruct(m, twoParty, fp, rp);
        ext = struct.encodeValueStruct(struct, zkp);
        evidence.createSaveRequest(hash_, ext, username);
        return hash_;
    }
    //监管者对请求上链的格式正确证明、范围证明、双方签名进行验证，验证通过后数据上链
    public static boolean verifyConsensusValue(String username, byte[] hash_in, ElGamalPublicKey pk, PublicKey pk1, PublicKey pk2, EvidenceContract evidence, ZeroKonwledgeProofGorV zkp) throws Exception {
        List<byte[]> structbyte = evidence.getRequestData(hash_in, username);
        ValueStruct struct = ValueStruct.decodeValueStruct(structbyte, zkp);
        EcdsaSign sign = new EcdsaSign();
        //验证格式正确证明
        boolean fp = zkp.VerifyFormatProof(struct.getM(), pk, struct.getFp());
        if(fp == false){
            System.out.println("格式正确证明验证未通过");
            return false;
        }
        //验证范围证明
        BigInteger moudle = pk.getP();
        BigInteger generator = new BigInteger("13");
        MoudleGroup group = new MoudleGroup(moudle, generator);
        GroupElement v_ = group.generator().from(struct.getM().gmyr);
        boolean rp = zkp.VerifyRangeProof(v_ , struct.getRf());
        if(rp == false){
            System.out.println("范围证明验证未通过");
            return false;
        }
        //验证签名
        boolean sig = sign.ecdsaverify(HexUtil.encodeHexString(hash_in), struct.getTwoParty(), pk1, pk2);
        if(sig == false){
            System.out.println("签名验证未通过");
            return false;
        }
        //允许此哈希值对应的消息上链
        evidence.voteSaveRequest(hash_in, username);
        return true;
    }
    //1为交易发起方，2为交易接收方，实际使用中需要交易双方进行信息交互
    public static byte[] trade(BigInteger value, ElGamalKeyPair keyPair1, ElGamalKeyPair keyPair2, ZeroKonwledgeProofGorV zkp1, ZeroKonwledgeProofGorV zkp2, TradeCountStruct struct, String username, EvidenceContract evidence) throws VerificationFailedException {
        //生成格式正确证明
        FormatProof fp_1 = zkp1.FormatProof(value, keyPair1.pk, struct.getV_pk1().r);
        FormatProof fp_2 = zkp2.FormatProof(value, keyPair2.pk, struct.getV_pk2().r);
        //生成相等证明
        LinearEquationProof ep = zkp1.EqualityProof(keyPair1.pk, keyPair2.pk, struct.getV_pk1(), struct.getV_pk2(), value);
        //生成会计平衡证明
        LinearEquationProof bp_1 = zkp1.BalanceProof(keyPair1.pk, struct.getCipher().get(0), struct.getCipher().get(1), struct.getV().get(0), struct.getV().get(1));
        LinearEquationProof bp_2 = zkp2.BalanceProof(keyPair2.pk, struct.getCipher().get(2), struct.getCipher().get(3), struct.getV().get(2), struct.getV().get(3));
        //生成范围证明参数
        BigInteger moudle1 = keyPair1.pk.getP();
        BigInteger generator1 = new BigInteger("13");
        MoudleGroup group1 = new MoudleGroup(moudle1, generator1);
        MoudleElement g1 = group1.generator().from(keyPair1.pk.getG());
        MoudleElement h1 = group1.generator().from(keyPair1.pk.getY());
        PeddersenBase base1 = new PeddersenBase(g1, h1, group1);
        zkp1.GenrateRangeProofParams(32, base1);
        BigInteger moudle2 = keyPair2.pk.getP();
        BigInteger generator2 = new BigInteger("13");
        MoudleGroup group2 = new MoudleGroup(moudle2, generator2);
        MoudleElement g2 = group2.generator().from(keyPair2.pk.getG());
        MoudleElement h2 = group2.generator().from(keyPair2.pk.getY());
        PeddersenBase base2 = new PeddersenBase(g2, h2, group2);
        zkp2.GenrateRangeProofParams(32, base2);
        //生成范围证明
        //证明交易金额在规定范围内
        RangeProof rp1 = zkp1.ProveRangeProof(value, struct.getV_pk1().r);
        //证明交易发起方账户金额在规定范围内
        RangeProof rp2 = zkp1.ProveRangeProof(struct.getBalance_after1(), struct.getBalance_after_m1().r);
        //证明交易接受方账户金额在规定范围内
        RangeProof rp3 = zkp2.ProveRangeProof(struct.getBalance_after2(), struct.getBalance_after_m2().r);
        TradeStruct tradestruct = new TradeStruct(fp_1, fp_2, rp1, rp2, rp3, ep, bp_1, bp_2);
        //上链数据编码
        List<byte[]> ext = new ArrayList<>();
        Collections.addAll(ext, HexUtil.tobyte32(fp_1.getC()), HexUtil.tobyte32(fp_1.getZ1()), HexUtil.tobyte32(fp_1.getZ2()));
        Collections.addAll(ext, HexUtil.tobyte32(fp_2.getC()), HexUtil.tobyte32(fp_2.getZ1()), HexUtil.tobyte32(fp_2.getZ2()));
        ext.addAll(zkp1.EncodeRangeProof(rp1));
        ext.addAll(zkp1.EncodeRangeProof(rp2));
        ext.addAll(zkp2.EncodeRangeProof(rp3));
        Collections.addAll(ext, HexUtil.tobyte32(ep.s.get(0)), HexUtil.tobyte32(ep.s.get(1)), HexUtil.tobyte32(ep.s.get(2)), HexUtil.tobyte32(ep.s.get(3)), HexUtil.tobyte32(ep.t));
        Collections.addAll(ext, HexUtil.tobyte32(bp_1.s.get(0)), HexUtil.tobyte32(bp_1.s.get(1)), HexUtil.tobyte32(bp_1.t));
        Collections.addAll(ext, HexUtil.tobyte32(bp_2.s.get(0)), HexUtil.tobyte32(bp_2.s.get(1)), HexUtil.tobyte32(bp_2.t));
        byte[] temp = HexUtil.byteCombine(ext);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash_ = digest.digest(temp);
        evidence.createSaveRequest(hash_, ext, username);
        return hash_;
    }

    public static boolean verifyTrade(byte[] hash_in, ElGamalPublicKey pk1, ElGamalPublicKey pk2,String user1 ,String user2 ,ZeroKonwledgeProofGorV zkp1, ZeroKonwledgeProofGorV zkp2, TradeCountStruct countStruct, TradeContract tradeContract, String username, EvidenceContract evidence) throws VerificationFailedException {
        List<byte[]> list = evidence.getRequestData(hash_in, username);
        FormatProof fp1_ = new FormatProof(new BigInteger(list.get(0)).toByteArray(),new BigInteger(list.get(1)).toByteArray(), new BigInteger(list.get(2)).toByteArray());
        FormatProof fp2_ = new FormatProof(new BigInteger(list.get(3)).toByteArray(),new BigInteger(list.get(4)).toByteArray(), new BigInteger(list.get(5)).toByteArray());
        List<byte[]> temp1 = new ArrayList<>();
        for (int i = 6 ; i < 26 ; i++){
            temp1.add(list.get(i));
        }
        RangeProof rp1_ = zkp1.DecodeRangeProof(temp1);
        List<byte[]> temp2 = new ArrayList<>();
        for (int i = 26 ; i < 46 ; i++){
            temp2.add(list.get(i));
        }
        RangeProof rp2_ = zkp1.DecodeRangeProof(temp2);
        List<byte[]> temp3 = new ArrayList<>();
        for (int i = 46 ; i < 66 ; i++){
            temp3.add(list.get(i));
        }
        RangeProof rp3_ = zkp2.DecodeRangeProof(temp3);
        ArrayList<byte[]> s_ep = new ArrayList<>();
        Collections.addAll(s_ep, new BigInteger(list.get(66)).toByteArray(),new BigInteger(list.get(67)).toByteArray(), new BigInteger(list.get(68)).toByteArray(), new BigInteger(list.get(69)).toByteArray());
        LinearEquationProof ep_ = new LinearEquationProof(s_ep, new BigInteger(list.get(70)).toByteArray());

        ArrayList<byte[]> s_bp1 = new ArrayList<>();
        Collections.addAll(s_bp1, new BigInteger(list.get(71)).toByteArray(),new BigInteger(list.get(72)).toByteArray());
        LinearEquationProof bp1_ = new LinearEquationProof(s_bp1, new BigInteger(list.get(73)).toByteArray());

        ArrayList<byte[]> s_bp2 = new ArrayList<>();
        Collections.addAll(s_bp2, new BigInteger(list.get(74)).toByteArray(),new BigInteger(list.get(75)).toByteArray());
        LinearEquationProof bp2_ = new LinearEquationProof(s_bp2, new BigInteger(list.get(76)).toByteArray());

        //验证格式正确证明
        boolean fp1 = zkp1.VerifyFormatProof(countStruct.getV_pk1(), pk1, fp1_);
        boolean fp2 = zkp2.VerifyFormatProof(countStruct.getV_pk2(), pk2, fp2_);
        if(fp1&&fp2 == false){
            System.out.println("格式正确证明验证未通过");
            return false;
        }
        //验证范围证明
        BigInteger moudle1 = pk1.getP();
        BigInteger moudle2 = pk2.getP();
        BigInteger generator = new BigInteger("13");
        MoudleGroup group1 = new MoudleGroup(moudle1, generator);
        MoudleGroup group2 = new MoudleGroup(moudle2, generator);
        GroupElement v1 = group1.generator().from(countStruct.getV_pk1().gmyr);
        GroupElement v2 = group1.generator().from(countStruct.getBalance_after_m1().gmyr);
        GroupElement v3 = group2.generator().from(countStruct.getBalance_after_m2().gmyr);
        boolean rp1 = zkp1.VerifyRangeProof(v1 , rp1_);
        boolean rp2 = zkp1.VerifyRangeProof(v2,  rp2_);
        boolean rp3 = zkp2.VerifyRangeProof(v3,  rp3_);
        if(rp1&&rp2&&rp3 == false){
            System.out.println("范围证明验证未通过");
            return false;
        }
        //验证相等证明
        boolean ep = zkp1.VerifyEqualityProof(pk1, pk2, countStruct.getV_pk1() ,countStruct.getV_pk2() , ep_);
        if(ep == false){
            System.out.println("相等证明验证未通过");
            return false;
        }
        //验证会计平衡证明
        boolean bp1 = zkp1.VerifyBalanceProof(pk1, countStruct.getCipher().get(0) , countStruct.getCipher().get(1) , bp1_);
        boolean bp2 = zkp2.VerifyBalanceProof(pk2, countStruct.getCipher().get(2) , countStruct.getCipher().get(3) , bp2_);
        if(bp1&&bp2 == false){
            System.out.println("会计平衡证明验证未通过");
            return false;
        }
        //修改双方账户余额
        tradeContract.issue(user1, ElGamalCipher.encodeCiphertext(countStruct.getBalance_after_m1()));
        tradeContract.issue(user2, ElGamalCipher.encodeCiphertext(countStruct.getBalance_after_m2()));
        //验证签名
        /*
        boolean sig = sign.ecdsaverify(HexUtil.encodeHexString(hash_in), struct.getTwoParty(), pk1, pk2);
        if(sig == false){
            System.out.println("签名验证未通过");
            return false;
        }*/
        //允许此哈希值对应的消息上链
        evidence.voteSaveRequest(hash_in, username);
        return true;
    }

    public static TradeCountStruct tradeCount(BigInteger value, ElGamalKeyPair keyPair1, ElGamalKeyPair keyPair2, TradeContract tradeContract, String username1, String username2){
        //使用双方公钥加密交易金额
        ElGamal_Ciphertext m_pk1 = ElGamalCipher.encrypt(value, keyPair1.pk);
        ElGamal_Ciphertext m_pk2 = ElGamalCipher.encrypt(value, keyPair2.pk);
        //链上获得双方账户余额密文
        ElGamal_Ciphertext balance_before1 = ElGamalCipher.decodeCiphertext(tradeContract.balance(username1));
        ElGamal_Ciphertext balance_before2 = ElGamalCipher.decodeCiphertext(tradeContract.balance(username2));
        //计算双方交易后余额
        ElGamal_Ciphertext balance_after1 = ElGamalCipher.subtract(balance_before1, m_pk1, keyPair1.pk);
        ElGamal_Ciphertext balance_after2 = ElGamalCipher.add(balance_before2, m_pk2, keyPair2.pk);
        BigInteger balance_after1_ = ElGamalCipher.decrypt(balance_after1, keyPair1.sk);
        BigInteger balance_after2_ = ElGamalCipher.decrypt(balance_after2, keyPair2.sk);
        ElGamal_Ciphertext[] C_o1 = {balance_before1};
        ElGamal_Ciphertext[] C_s1 = {m_pk1, balance_after1};
        BigInteger[] V_o1 = {ElGamalCipher.decrypt(balance_before1, keyPair1.sk)};
        BigInteger[] V_s1 = {value, balance_after1_};
        ElGamal_Ciphertext[] C_o2 = {balance_before2, m_pk2};
        ElGamal_Ciphertext[] C_s2 = {balance_after2};
        BigInteger[] V_o2 = {ElGamalCipher.decrypt(balance_before2, keyPair2.sk), value};
        BigInteger[] V_s2 = {balance_after2_};
        List<ElGamal_Ciphertext[]> cipher = new ArrayList<>();
        cipher.add(C_o1);
        cipher.add(C_s1);
        cipher.add(C_o2);
        cipher.add(C_s2);
        List<BigInteger[]> v = new ArrayList<>();
        v.add(V_o1);
        v.add(V_s1);
        v.add(V_o2);
        v.add(V_s2);
        TradeCountStruct struct = new TradeCountStruct(m_pk1, m_pk2, balance_after1, balance_after2,balance_after1_,balance_after2_,cipher,v);
        return struct;
    }

    public static ElGamal_Ciphertext calculateValue(BigInteger[] electricity, ElGamal_Ciphertext[] m, ElGamalPublicKey pk){
        ElGamal_Ciphertext total_m = null;
        if(electricity.length != m.length){
            System.out.println("length not equal!");
            return total_m;
        }
        total_m = ElGamalCipher.multiply_scalar(m[0], electricity[0], pk);
        for(int i = 1; i< electricity.length; i++){
            ElGamal_Ciphertext temp = ElGamalCipher.multiply_scalar(m[i], electricity[i], pk);
            total_m = ElGamalCipher.add(temp, total_m, pk);
        }
        return total_m;
    }
}
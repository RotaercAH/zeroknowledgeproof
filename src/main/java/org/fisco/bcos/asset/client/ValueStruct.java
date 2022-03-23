package org.fisco.bcos.asset.client;

import org.fisco.bcos.asset.crypto.ecdsasign.EcdsaSign;
import org.fisco.bcos.asset.crypto.ecdsasign.HexUtil;
import org.fisco.bcos.asset.crypto.ecdsasign.SignatureTwoParty;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalCipher;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.FormatProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProof;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValueStruct {
    private final ElGamal_Ciphertext m;
    private final SignatureTwoParty twoParty;
    private final FormatProof fp;
    private final RangeProof rp;

    public ValueStruct(ElGamal_Ciphertext m, SignatureTwoParty twoParty, FormatProof fp, RangeProof rp) {
        this.m = m;
        this.twoParty = twoParty;
        this.fp = fp;
        this.rp = rp;
    }

    public static List<byte[]> encodeValueStruct(ValueStruct struct, ZeroKonwledgeProofGorV zkp){
        //按ElGamal密文：3bytes[32]，格式正确证明:3bytes[32]，范围证明:20bytes[32]，签名: 9bytes[32]顺序存储
        List<byte[]> result= new ArrayList<>();
        List<byte[]> m = ElGamalCipher.encodeCiphertext(struct.getM());
        result.addAll(m);
        Collections.addAll(result, HexUtil.tobyte32(struct.getFp().getC()), HexUtil.tobyte32(struct.getFp().getZ1()), HexUtil.tobyte32(struct.getFp().getZ2()));
        result.addAll(zkp.EncodeRangeProof(struct.rp));
        List<byte[]> sig_ = EcdsaSign.encodesig(struct.getTwoParty());
        result.addAll(sig_);
        if(result.size() != 35){
            System.out.println("debug encodeValueStruct m.size" + m.size());
            System.out.println("debug encodeValueStruct rp.size" + zkp.EncodeRangeProof(struct.rp).size());
            System.out.println("debug encodeValueStruct sig.size" + sig_.size());
        }
        for(int i = 0;i<35;i++){
            if(result.get(i).length != 32) {
                System.out.println("这个" + i + "数组长度不为32");
                System.out.println(HexUtil.encodeHexString(result.get(i)));
            }
        }
        return result;
    }

    public static ValueStruct decodeValueStruct(List<byte[]> list, ZeroKonwledgeProofGorV zkp){
        List<byte[]> temp = new ArrayList<>();
        System.out.println("debug valuestruct list.size" + list.size());
        ElGamal_Ciphertext m = new ElGamal_Ciphertext(new BigInteger(list.get(1)), new BigInteger(list.get(0)), new BigInteger(list.get(2)));
        FormatProof fp = new FormatProof(new BigInteger(list.get(3)).toByteArray(),new BigInteger(list.get(4)).toByteArray(), new BigInteger(list.get(5)).toByteArray());
        List<byte[]> temp1 = new ArrayList<>();
        for (int i = 6 ; i < 26 ; i++){
            temp1.add(list.get(i));
        }
        RangeProof rp = zkp.DecodeRangeProof(temp1);
        List<byte[]> temp2 = new ArrayList<>();
        for (int i = 26 ; i < 35 ; i++){temp2.add(list.get(i));}
        SignatureTwoParty twoParty = EcdsaSign.decodesig(temp2);
        ValueStruct struct = new ValueStruct(m, twoParty, fp, rp);
        return struct;
    }

    public FormatProof getFp() {
        return fp;
    }

    public RangeProof getRf() {
        return rp;
    }

    public SignatureTwoParty getTwoParty() {
        return twoParty;
    }

    public ElGamal_Ciphertext getM() {
        return m;
    }
}

package org.fisco.bcos.asset.crypto.elgamal;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fisco.bcos.asset.crypto.ecdsasign.HexUtil;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.LinearEquationProof;
import org.fisco.bcos.asset.crypto.misc.NTL;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.FormatProof;
public class ElgamalTest {
    private static final int SIZE = 1; // Stress-Test
    private static final int KEY_SIZE = 256;
    private static final int BILLION = BigInteger.TEN.pow(9).intValue();
    public static void main(String[] args) throws InvalidKeyException, SignatureException {
        ZeroKonwledgeProofGorV test = new ZeroKonwledgeProofGorV();
        System.out.println("-----------EL-GAMAL TEST x" + SIZE + "--------------KEY: " + KEY_SIZE + "-----------");
        //加解密验证
        ElGamalKeyPairGenerator key_pair = new ElGamalKeyPairGenerator();
        //key_pair.initialize(KEY_SIZE, new SecureRandom());//第二个参数空缺则为AH-ElGamal，第二个参数为随机数，则为ElGamal
        key_pair.initialize(KEY_SIZE, null);
        KeyPair key = key_pair.generateKeyPair();
        ElGamalPublicKey e_pk = (ElGamalPublicKey) key.getPublic();
        ElGamalPrivateKey e_sk = (ElGamalPrivateKey) key.getPrivate();
        System.out.println(e_pk.p);
        System.out.println(e_pk.p.toByteArray().length);
        System.out.println(HexUtil.encodeHexString(e_pk.p.toByteArray()));
        KeyPair key2 = key_pair.generateKeyPair();
        ElGamalPublicKey e_pk2  = (ElGamalPublicKey) key2.getPublic();
        //ElGamalPrivateKey e_sk2 = (ElGamalPrivateKey) key2.getPrivate();
        //BigInteger b = NTL.generateXBitRandom(15);
        //同态加解密验证
        System.out.println("同态加解密部分");
        byte[] r = e_pk.getP().toByteArray();
        //System.out.println(r[128]);
        BigInteger longth = BigInteger.valueOf(257);
        byte[] longth_byte = longth.toByteArray();
        System.out.println(longth_byte.length);
        System.out.println(longth_byte[1]);
        ElGamal_Ciphertext add_1 = ElGamalCipher.encrypt(new BigInteger("13"), e_pk);
        ElGamal_Ciphertext mulity_13_2 = ElGamalCipher.multiply_scalar(add_1,13, e_pk);
        System.out.println(ElGamalCipher.decrypt(mulity_13_2, e_sk));
        BigInteger add_1_decrypt = ElGamalCipher.decrypt(add_1, e_sk);
        System.out.println("add_1明文为" + add_1_decrypt);
        ElGamal_Ciphertext add_2 = ElGamalCipher.encrypt(new BigInteger("-12"), e_pk);
        ElGamal_Ciphertext add_result = ElGamalCipher.add(add_1, add_2 ,e_pk);
        BigInteger add_out = ElGamalCipher.decrypt(add_result ,e_sk);
        System.out.println("AH-Elgamal密文gmyr" + add_1.gmyr.toByteArray().length);
        System.out.println("同态加法解密输出" + add_out);
        //格式正确证明验证
        System.out.println("格式正确证明部分");
        FormatProof fp = test.FormatProof(new BigInteger("13"), e_pk, add_1.r);
        System.out.printf("c: %s\nz1: %s\nz2: %s\n" , HexUtil.encodeHexString(fp.getC()), HexUtil.encodeHexString(fp.getZ1()), HexUtil.encodeHexString(fp.getZ2()));
        List<byte[]> hash = new ArrayList<>();
        Collections.addAll(hash,fp.getC(),fp.getZ1(),fp.getZ2());
        byte[] hash_ = byteCombine(hash);
        System.out.println(HexUtil.encodeHexString(hash_));
        boolean output = test.VerifyFormatProof(add_1, e_pk, fp);
        System.out.printf("格式正确证明验证结果%s\n", output);
        //相等证明验证
        System.out.println("相等证明部分");
        ElGamal_Ciphertext test_1 = ElGamalCipher.encrypt(new BigInteger("10"),e_pk);
        ElGamal_Ciphertext test_3 = ElGamalCipher.encrypt(new BigInteger("6"),e_pk);
        ElGamal_Ciphertext test_4 = ElGamalCipher.add(test_1, test_3, e_pk);
        ElGamal_Ciphertext test_2 = ElGamalCipher.encrypt(new BigInteger("16"),e_pk2);
        LinearEquationProof ep = test.EqualityProof(e_pk, e_pk2, test_4, test_2, new BigInteger("16"));
        boolean output2 = test.VerifyEqualityProof(e_pk, e_pk2, test_4, test_2, ep);
        System.out.printf("相等证明验证结果%s\n", output2);
        //会计平衡证明验证
        System.out.println("会计平衡证明部分");
        //定义合法交易
        BigInteger[] v_o = {BigInteger.valueOf(2),BigInteger.valueOf(3),BigInteger.valueOf(5)};
        BigInteger[] v_s = {BigInteger.valueOf(5),BigInteger.valueOf(5)};
        ElGamal_Ciphertext two = ElGamalCipher.encrypt(new BigInteger("2"),e_pk);
        ElGamal_Ciphertext three = ElGamalCipher.encrypt(new BigInteger("3"),e_pk);
        ElGamal_Ciphertext five = ElGamalCipher.encrypt(new BigInteger("5"),e_pk);
        ElGamal_Ciphertext[] C_o = {two, three, five};
        ElGamal_Ciphertext[] C_s = {five, five};
        LinearEquationProof bp = test.BalanceProof(e_pk, C_o, C_s, v_o, v_s);
        boolean output3 = test.VerifyBalanceProof(e_pk, C_o, C_s, bp);
        System.out.printf("会计平衡证明验证结果%s\n", output3);



        ElGamalSignature sig = new ElGamalSignature();
        sig.initSign(e_sk);
        sig.update(new BigInteger("42").toByteArray());
        byte [] cert = sig.sign();
        long start;
        /*start = System.nanoTime();
        for(int i = 0; i < SIZE;i++)
        {
            sig.initVerify(e_pk);
            sig.update(BigInteger.valueOf(i).toByteArray());
            if(sig.verify(cert))
            {
                System.out.println("ElGamal VALID AT: " + i);
            }
        }
        System.out.println("Time to complete signature: " + ((System.nanoTime() - start)/BILLION) + " seconds");*/

        start = System.nanoTime();
        for(int i = 0; i < SIZE; i++)
        {
            ElGamalCipher.encrypt(BigInteger.TEN, e_pk);
        }
        System.out.println("Time to complete encryption: " + ((System.nanoTime() - start)/BILLION) + " seconds");

        start = System.nanoTime();
        for(int i = 0; i < SIZE; i++)
        {
            ElGamalCipher.decrypt(add_1, e_sk);
        }
        System.out.println("Time to complete decryption: " + ((System.nanoTime() - start)/BILLION) + " seconds");

        start = System.nanoTime();
        for(int i = 0; i < SIZE; i++)
        {

            ElGamal_Ciphertext add = ElGamalCipher.add(add_1, add_2 ,e_pk);
            BigInteger sub = ElGamalCipher.decrypt(ElGamalCipher.subtract(add_1, add_2, e_pk), e_sk);
            System.out.printf("\n同态减法解密输出%d\n", sub);
        }
        System.out.println("Time to complete addition: " + ((System.nanoTime() - start)/BILLION) + " seconds");

        start = System.nanoTime();
        for(int i = 0; i < SIZE; i++)
        {
            add_result = ElGamalCipher.multiply_scalar(add_1, 12, e_pk);
        }
        System.out.println("Time to complete multiplication: " + ((System.nanoTime() - start)/BILLION) + " seconds");
    }
    private static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] byteCombine(List<byte[]> list){
        byte[] result = byteMerger(list.get(0), list.get(1));
        for(int i = 2; i<list.size(); i++){
            result = byteMerger(result, list.get(i));
        }
        return result;
    }
}


package org.fisco.bcos.asset.crypto.ecdsasign;

import com.starkbank.ellipticcurve.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EcdsaSign {
    public EcdsaSign(){

    }
    public SignatureTwoParty ecdsasign(String message, PrivateKey sk1, PrivateKey sk2) {
        Signature signature1 = Ecdsa.sign(message, sk1);
        StringBuffer sb = new StringBuffer();
        sb.append(HexUtil.encodeHexString(signature1.r.toByteArray()));
        sb.append(HexUtil.encodeHexString(signature1.s.toByteArray()));
        Signature signature1and2 = Ecdsa.sign(sb.toString(), sk2);
        SignatureTwoParty twoParty = new SignatureTwoParty(signature1, signature1and2, sb.toString());
        return twoParty;
    }

    public boolean ecdsaverify(String message, SignatureTwoParty twoParty,PublicKey pk1, PublicKey pk2) {
        boolean verify1 = Ecdsa.verify(twoParty.getString1and2(), twoParty.getSignature1and2(), pk2);
        boolean verify2 = Ecdsa.verify(message, twoParty.getSignature1(), pk1);
        return verify1&&verify2;
    }

    public static List<byte[]> encodesig(SignatureTwoParty twoParty){
        List<byte[]> sig = new ArrayList<>();
        List<byte[]> result = new ArrayList<>();
        //char r_length = twoParty.getSignature1().r.toByteArray().length;
        int[] num = new int[]{twoParty.getSignature1().r.toByteArray().length, twoParty.getSignature1().s.toByteArray().length, twoParty.getSignature1and2().r.toByteArray().length, twoParty.getSignature1and2().s.toByteArray().length};
        Collections.addAll(sig, twoParty.getSignature1().r.toByteArray(), twoParty.getSignature1().s.toByteArray(),twoParty.getSignature1and2().r.toByteArray(),twoParty.getSignature1and2().s.toByteArray());
        for(int i = 0; i<sig.size(); i++){
            if(sig.get(i).length == 32){
                result.add(sig.get(i));
                result.add(new byte[32]);
            }
            else if(sig.get(i).length == 31){
                List<byte[]> temp  = HexUtil.bytetobyte32list(sig.get(i));
                result.add(temp.get(0));
                result.add(new byte[32]);
            }
            else {
                List<byte[]> temp  = HexUtil.bytetobyte32list(sig.get(i));
                result.add(temp.get(0));
                result.add(temp.get(1));
            }
        }
        result.add(HexUtil.intToByte(num));
        return result;
    }

    public static SignatureTwoParty decodesig(List<byte[]> in){
        int[] num = HexUtil.byteToInt(in.get(8));
        BigInteger[] list = new BigInteger[4];
        for(int i = 0; i<list.length; i++) {
            if (num[i] == 32) {
                list[i] = new BigInteger(in.get(2 * i));
            } else if (num[i] == 31) {
                byte[] bytes31 = new byte[31];
                System.arraycopy(in.get(2 * i), 0, bytes31, 0, 31);
                list[i] = new BigInteger(bytes31);
            } else {
                byte[] bytes33 = new byte[33];
                System.arraycopy(in.get(2 * i), 0, bytes33, 0, 32);
                System.arraycopy(in.get(2 * i+1), 0, bytes33, 32, 1);
                list[i] = new BigInteger(bytes33);
            }
        }
        Signature a = new Signature(list[0], list[1]);
        Signature aandb = new Signature(list[2], list[3]);
        StringBuffer sb = new StringBuffer();
        sb.append(HexUtil.encodeHexString(a.r.toByteArray()));
        sb.append(HexUtil.encodeHexString(a.s.toByteArray()));
        SignatureTwoParty twoParty = new SignatureTwoParty(a, aandb, sb.toString());
        return twoParty;

    }
}



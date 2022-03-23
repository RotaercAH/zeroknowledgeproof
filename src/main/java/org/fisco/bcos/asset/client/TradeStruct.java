package org.fisco.bcos.asset.client;

import org.fisco.bcos.asset.crypto.ecdsasign.EcdsaSign;
import org.fisco.bcos.asset.crypto.ecdsasign.SignatureTwoParty;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.FormatProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.LinearEquationProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProof;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeStruct {
    //private final SignatureTwoParty twoParty;
    private final FormatProof fp1;
    private final FormatProof fp2;
    private final RangeProof rp1;
    private final RangeProof rp2;
    private final RangeProof rp3;
    private final LinearEquationProof ep;
    private final LinearEquationProof bp1;
    private final LinearEquationProof bp2;

    public TradeStruct(FormatProof fp1, FormatProof fp2, RangeProof rp1, RangeProof rp2, RangeProof rp3, LinearEquationProof ep, LinearEquationProof bp1, LinearEquationProof bp2) {
        //this.twoParty = twoParty;
        this.fp1 = fp1;
        this.rp1 = rp1;
        this.fp2 = fp2;
        this.rp2 = rp2;
        this.rp3 = rp3;
        this.ep = ep;
        this.bp1 = bp1;
        this.bp2 = bp2;
    }
/*
    public static List<byte[]> encodeValueStruct(ValueStruct struct, ZeroKonwledgeProofGorV zkp){
        //按ElGamal密文：3bytes[32]，格式正确证明:3bytes[32]，范围证明:20bytes[32]，签名: 8bytes[32]顺序存储
        List<byte[]> result= new ArrayList<>();
        Collections.addAll(result, struct.getM().gmyr.toByteArray(), struct.getM().hr.toByteArray(), struct.getM().r.toByteArray());
        Collections.addAll(result, struct.getFp().getC(), struct.getFp().getZ1(), struct.getFp().getZ2());
        result.addAll(zkp.EncodeRangeProof(struct.rp));
        List<byte[]> sig_ = EcdsaSign.encodesig(struct.getTwoParty());
        result.addAll(sig_);
        return result;
    }

    public static ValueStruct decodeValueStruct(List<byte[]> list, ZeroKonwledgeProofGorV zkp){
        ElGamal_Ciphertext m = new ElGamal_Ciphertext(new BigInteger(list.get(1)), new BigInteger(list.get(0)), new BigInteger(list.get(2)));
        FormatProof fp = new FormatProof(list.get(3),list.get(4), list.get(5));
        List<byte[]> temp1 = new ArrayList<>();
        for (int i = 6 ; i < 26 ; i++){
            temp1.add(list.get(i));
        }
        RangeProof rp = zkp.DecodeRangeProof(temp1);
        List<byte[]> temp2 = new ArrayList<>();
        for (int i = 26 ; i < 34 ; i++){temp2.add(list.get(i));}
        SignatureTwoParty twoParty = EcdsaSign.decodesig(temp2);
        ValueStruct struct = new ValueStruct(m, twoParty, fp, rp);
        return struct;
    }*/

    public FormatProof getFp1() {
        return fp1;
    }
    /*public SignatureTwoParty getTwoParty() {
        return twoParty;
    }*/
    public FormatProof getFp2() {
        return fp2;
    }

    public LinearEquationProof getBp1() {
        return bp1;
    }

    public LinearEquationProof getBp2() {
        return bp2;
    }

    public LinearEquationProof getEp() {
        return ep;
    }

    public RangeProof getRp1() {
        return rp1;
    }

    public RangeProof getRp2() {
        return rp2;
    }

    public RangeProof getRp3() {
        return rp3;
    }

}

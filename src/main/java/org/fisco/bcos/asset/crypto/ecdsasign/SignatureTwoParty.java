package org.fisco.bcos.asset.crypto.ecdsasign;

import com.starkbank.ellipticcurve.Signature;

public class SignatureTwoParty {
    private final Signature signature1;
    private final Signature signature1and2;
    private final String string1and2;
    public SignatureTwoParty(Signature signature1, Signature signature1and2, String string1and2){
        this.signature1 = signature1;
        this.signature1and2 = signature1and2;
        this.string1and2 = string1and2;
    }



    public Signature getSignature1() {
        return signature1;
    }

    public Signature getSignature1and2() {
        return signature1and2;
    }

    public String getString1and2() {
        return string1and2;
    }
}

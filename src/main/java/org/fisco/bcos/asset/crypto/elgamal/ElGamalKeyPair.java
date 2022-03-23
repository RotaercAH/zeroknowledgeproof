package org.fisco.bcos.asset.crypto.elgamal;

import java.math.BigInteger;
import java.security.KeyPair;

public class ElGamalKeyPair {
    public ElGamalPublicKey pk;
    public ElGamalPrivateKey sk;

    public ElGamalKeyPair(ElGamalPublicKey pk, ElGamalPrivateKey sk)
    {
        this.pk = pk;
        this.sk = sk;
    }

    // used for signatures
    public ElGamalPublicKey getPK()
    {
        return this.pk;
    }

    public ElGamalPrivateKey getSK() { return this.sk; }

    //生成ElGamal公私钥对
    public static ElGamalKeyPair getElgamalKeyPair() {
        ElGamalKeyPairGenerator key_pair = new ElGamalKeyPairGenerator();
        key_pair.initialize(256, null);
        KeyPair key = key_pair.generateKeyPair();
        ElGamalKeyPair pair = new ElGamalKeyPair((ElGamalPublicKey) key.getPublic(), (ElGamalPrivateKey) key.getPrivate());
        return pair;
    }

}

package org.fisco.bcos.asset.client;

import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;

import java.math.BigInteger;
import java.util.List;

public class TradeCountStruct {
    private final ElGamal_Ciphertext balance_after_m1;
    private final ElGamal_Ciphertext balance_after_m2;
    private final BigInteger balance_after1;
    private final BigInteger balance_after2;
    private final ElGamal_Ciphertext v_pk1;
    private final ElGamal_Ciphertext v_pk2;
    private final List<ElGamal_Ciphertext[]> cipher;
    private final List<BigInteger[]> v;

    public TradeCountStruct(ElGamal_Ciphertext v_pk1, ElGamal_Ciphertext v_pk2, ElGamal_Ciphertext balance_after_m1, ElGamal_Ciphertext balance_after_m2, BigInteger balance_after1, BigInteger balance_after2, List<ElGamal_Ciphertext[]> cipher, List<BigInteger[]> v) {
        this.balance_after_m1 = balance_after_m1;
        this.balance_after_m2 = balance_after_m2;
        this.v_pk1 = v_pk1;
        this.v_pk2 = v_pk2;
        this.balance_after1 = balance_after1;
        this.balance_after2 = balance_after2;
        this.cipher = cipher;
        this.v = v;
    }

    public BigInteger getBalance_after1() {
        return balance_after1;
    }

    public BigInteger getBalance_after2() {
        return balance_after2;
    }

    public ElGamal_Ciphertext getBalance_after_m1() {
        return balance_after_m1;
    }

    public ElGamal_Ciphertext getBalance_after_m2() {
        return balance_after_m2;
    }

    public List<BigInteger[]> getV() {
        return v;
    }

    public List<ElGamal_Ciphertext[]> getCipher() {
        return cipher;
    }

    public ElGamal_Ciphertext getV_pk1() {
        return v_pk1;
    }

    public ElGamal_Ciphertext getV_pk2() {
        return v_pk2;
    }
}

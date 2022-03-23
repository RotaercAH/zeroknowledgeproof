package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.commitments;

import java.math.BigInteger;

import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.PeddersenBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.ProofUtils;

/**
 * Created by buenz on 7/6/17.
 */
public class PeddersenCommitment<T extends GroupElement<T>> implements HomomorphicCommitment<PeddersenCommitment<T>> {
    private final PeddersenBase<T> base;
    private final BigInteger x;
    private final BigInteger r;
    private T commitment;

    public PeddersenCommitment(PeddersenBase<T> base, BigInteger x, BigInteger r) {
        this.base = base;
        this.x = x.mod(base.getGroup().groupOrder());
        this.r = r.mod(base.getGroup().groupOrder());
    }

    public PeddersenCommitment(PeddersenBase<T> base, BigInteger x) {
        this(base, x, ProofUtils.randomNumber());
    }

    @Override
    public <C2 extends PeddersenCommitment<T>> PeddersenCommitment<T> add(C2 other) {
        return new PeddersenCommitment<>(base, x.add(other.getX()), r.add(other.getR()));
    }

    @Override
    public PeddersenCommitment<T> times(BigInteger exponent) {
        return new PeddersenCommitment<>(base, x.multiply(exponent), r.multiply(exponent));
    }

    @Override
    public PeddersenCommitment<T> addConstant(BigInteger constant) {
        return new PeddersenCommitment<>(base, x.add(constant), r);
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getR() {
        return r;
    }

    public T getCommitment() {
        if (commitment == null) {
            commitment = base.commit(x, r);
        }
        return commitment;
    }
}

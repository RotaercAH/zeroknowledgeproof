package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.commitments;

import cyclops.collections.immutable.VectorX;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.PeddersenBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.ProofUtils;

import java.math.BigInteger;

/**
 * Created by buenz on 7/6/17.
 */
public class PolyCommitment<T extends GroupElement<T>> {

    private final VectorX<PeddersenCommitment<T>> coefficientCommitments;

    public PolyCommitment(VectorX<PeddersenCommitment<T>> coefficientCommitments) {
        this.coefficientCommitments = coefficientCommitments;
    }


    public PeddersenCommitment<T> evaluate(BigInteger x) {

        return VectorX.iterate(coefficientCommitments.size(), BigInteger.ONE, x::multiply).zip(coefficientCommitments, (xi, ci) -> ci.times(xi)).reduce(PeddersenCommitment::add).get();
    }

    public VectorX<PeddersenCommitment<T>> getCoefficientCommitments() {
        return coefficientCommitments;
    }

    public VectorX<T> getCommitments() {
        return coefficientCommitments.filterNot(pc -> pc.getR().equals(BigInteger.ZERO)).map(PeddersenCommitment::getCommitment);
    }

    public static <T extends GroupElement<T>> PolyCommitment<T> from(PeddersenBase<T> base, BigInteger x0, VectorX<BigInteger> xs) {
        VectorX<PeddersenCommitment<T>> peddersenCommitments = xs.map(x -> new PeddersenCommitment<>(base, x, ProofUtils.randomNumber())).prepend(new PeddersenCommitment<>(base, x0, BigInteger.ZERO)).materialize();
        return new PolyCommitment<>(peddersenCommitments);
    }
}

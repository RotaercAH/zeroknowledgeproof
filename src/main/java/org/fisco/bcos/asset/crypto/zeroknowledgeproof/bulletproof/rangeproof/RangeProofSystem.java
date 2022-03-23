package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof;

import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.GeneratorParams;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.ProofSystem;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.commitments.PeddersenCommitment;

/**
 * Created by buenz on 7/1/17.
 */
public class RangeProofSystem<T extends GroupElement<T>> implements ProofSystem<GeneratorParams<T>, T, PeddersenCommitment<T>, RangeProof<T>, RangeProofProver<T>, RangeProofVerifier<T>> {
    @Override
    public RangeProofProver<T> getProver() {

        return new RangeProofProver<>();
    }

    @Override
    public RangeProofVerifier<T> getVerifier() {
        return new RangeProofVerifier<>();
    }


}

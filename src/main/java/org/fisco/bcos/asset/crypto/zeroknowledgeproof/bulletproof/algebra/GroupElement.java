package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra;

import java.math.BigInteger;

public interface GroupElement<T extends GroupElement<T>> {
    T add(T other);

    T multiply(BigInteger exp);

    T negate();

    default T subtract(T other) {
        return add(other.negate());
    }

    byte[] canonicalRepresentation();

    String stringRepresentation();
}

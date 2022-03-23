package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra;

import java.math.BigInteger;

public interface Group<T extends GroupElement<T>> {
    T mapInto(BigInteger seed);

    T generator();

    BigInteger groupOrder();

    T zero();
}

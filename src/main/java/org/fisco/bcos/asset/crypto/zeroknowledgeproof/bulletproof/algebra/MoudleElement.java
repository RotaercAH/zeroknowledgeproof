package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra;

import java.math.BigInteger;
import java.util.Objects;

public class MoudleElement implements GroupElement<MoudleElement> {

    protected final BigInteger element;
    protected final BigInteger moudle;

    public MoudleElement(BigInteger element, BigInteger moudle){
        this.element = element;
        this.moudle = moudle;
    }
    
    @Override
    public MoudleElement add(MoudleElement other) {
            return from(element.multiply(other.element).mod(moudle));
    }

    @Override
    public MoudleElement multiply(BigInteger exp) {
        return from(element.modPow(exp, moudle));
    }

    @Override
    public MoudleElement negate() {
        return from(element.modInverse(moudle));
    }

    @Override
    public byte[] canonicalRepresentation() {
        byte[] arr = new byte[32];
        int xLength = element.bitLength() / 8 + 1;
        System.arraycopy(element.toByteArray(), 0, arr, 32 - xLength, xLength);
        return arr;
    }

    @Override
    public String stringRepresentation() {
        // TODO Auto-generated method stub
        return element.toString();
    }

    @Override
    public String toString() {
        return element.toString(512);
    }

    public MoudleElement from(BigInteger element) {
        return new MoudleElement(element, moudle);
    }

    public BigInteger getPoint() {
        return element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoudleElement that = (MoudleElement) o;

        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return element != null ? element.hashCode() : 0;
    }
}

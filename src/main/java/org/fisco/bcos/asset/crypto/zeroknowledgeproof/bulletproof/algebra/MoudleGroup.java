package org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra;

import java.math.BigInteger;

public class MoudleGroup implements Group<MoudleElement>{
    protected final BigInteger moudle;
    private final MoudleElement generator;
    //public  BigInteger moudle;
    //public  MoudleElement generator;


    public MoudleGroup(BigInteger moudle, BigInteger generator) {
        this(moudle, new MoudleElement(generator, moudle));
    }

    public MoudleGroup(BigInteger moudle, MoudleElement generator) {
        this.moudle = moudle;
        this.generator = generator;
    }

    @Override
    public MoudleElement mapInto(BigInteger seed) {
        seed = seed.mod(groupOrder());

        BigInteger y;

        seed = seed.subtract(BigInteger.ONE);
        do {
            seed = seed.add(BigInteger.ONE);
            y = seed;
            if (y.modPow(groupOrder(), moudle).equals(BigInteger.ONE)) {
                break;
            }
        } while (true);
        return new MoudleElement(seed, moudle);
    }

    @Override
    public MoudleElement generator() {
        return generator;
    }

    @Override
    public BigInteger groupOrder() {
        BigInteger q = moudle.subtract(BigInteger.ONE);
        return q.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE) ?  q : q.divide(BigInteger.valueOf(2));
    }

    @Override
    public MoudleElement zero() {
        return new MoudleElement(BigInteger.ONE, moudle);
    }
    
}

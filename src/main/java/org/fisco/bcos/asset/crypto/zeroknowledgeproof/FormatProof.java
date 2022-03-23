package org.fisco.bcos.asset.crypto.zeroknowledgeproof;

import java.math.BigInteger;

public class FormatProof {
    protected final byte[] c;
    protected final byte[] z1;
    protected final byte[] z2;
    public FormatProof(byte[] c, byte[] z1, byte[] z2){
        this.c = c;
        this.z1 = z1;
        this.z2 = z2;
    }
    public byte[] getC()
    {
        return this.c;
    }
    public byte[] getZ1()
    {
        return this.z1;
    }
    public byte[] getZ2()
    {
        return this.z2;
    }

}

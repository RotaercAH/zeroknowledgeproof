package org.fisco.bcos.asset.crypto.zeroknowledgeproof;

import java.util.ArrayList;

public class LinearEquationProof {
    public  ArrayList<byte[]> s;
    public final byte [] t;
    public LinearEquationProof(ArrayList<byte[]> s, byte [] t){
        this.s = s;
        this.t =t;
    }
}


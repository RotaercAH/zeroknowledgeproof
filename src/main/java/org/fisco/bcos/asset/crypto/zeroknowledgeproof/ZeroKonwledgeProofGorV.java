package org.fisco.bcos.asset.crypto.zeroknowledgeproof;

import cyclops.collections.immutable.VectorX;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.GeneratorParams;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.VerificationFailedException;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.Group;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.GroupElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.MoudleElement;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.algebra.MoudleGroup;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.commitments.PeddersenCommitment;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.innerproduct.InnerProductProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.GeneratorVector;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.PeddersenBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.linearalgebra.VectorBase;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProof;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProofProver;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.rangeproof.RangeProofVerifier;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalPublicKey;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.HexUtil;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.ProofUtils;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalKeyPairGenerator;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.fisco.bcos.asset.crypto.misc.NTL;

public class ZeroKonwledgeProofGorV {
    public GeneratorParams parameters;
    public Group<MoudleElement> group;

    public FormatProof FormatProof(BigInteger v, ElGamalPublicKey pk, BigInteger r){
        BigInteger P_1 = pk.getP().subtract(BigInteger.ONE);
        //生成两个随机数a,b
        BigInteger a = NTL.RandomBnd(pk.getP()).subtract(new BigInteger("2"));//暂时这样写，后续再改进
        BigInteger b = NTL.RandomBnd(pk.getP()).subtract(new BigInteger("2"));

        BigInteger ga = pk.getG().modPow(a, pk.getP());
        BigInteger yb = pk.getY().modPow(b, pk.getP());

        BigInteger t1_p = ga.multiply(yb).mod(pk.getP());//(g^a * y^b)
        BigInteger t2_p = pk.getH().modPow(b, pk.getP());//(h^b)

        byte[] tp = byteMerger(t1_p.toByteArray(), t2_p.toByteArray());
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] c_ = digest.digest(tp);

        BigInteger c = new BigInteger(c_);
        c = c.mod(pk.getP());

        //Z1
        BigInteger vc = v.multiply(c).mod(P_1);
        BigInteger z1 = P_1.subtract(vc).add(a).mod(P_1);

        //Z2
        BigInteger rc = r.multiply(c).mod(P_1);
        BigInteger z2 = P_1.subtract(rc).add(b).mod(P_1);

        FormatProof fp = new FormatProof(c.toByteArray(),z1.toByteArray(),z2.toByteArray());
        return fp;
    }

    public boolean VerifyFormatProof(ElGamal_Ciphertext c, ElGamalPublicKey pk, FormatProof fp){
        BigInteger c0 = new BigInteger(fp.c);
        BigInteger z1 = new BigInteger(fp.z1);
        BigInteger z2 = new BigInteger(fp.z2);
        //t1_v
        BigInteger c1c = c.getB().modPow(c0, pk.getP());
        BigInteger gz1 = pk.getG().modPow(z1, pk.getP());
        BigInteger yz2 = pk.getY().modPow(z2, pk.getP());
        BigInteger t1_v = c1c.multiply(gz1).mod(pk.getP()).multiply(yz2).mod(pk.getP());
        //t2_v
        BigInteger c2c = c.getA().modPow(c0, pk.getP());
        BigInteger hz2 = pk.getH().modPow(z2, pk.getP());
        BigInteger t2_v = c2c.multiply(hz2).mod(pk.getP());
        //c_v
        byte[] tp = byteMerger(t1_v.toByteArray(), t2_v.toByteArray());
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] c_verify = digest.digest(tp);

        return c0.equals(new BigInteger(c_verify).mod(pk.getP()));
    }

    /*public LinearEquationProof BalanceProof(ElGamalPublicKey pk, ElGamal_Ciphertext[] C_o, ElGamal_Ciphertext[] C_s, int[] v_o, int[] v_s){
        if (C_o.length != v_o.length || C_s.length != v_s.length){
            System.out.println("\n明密文长度不匹配！\n");
            ArrayList<byte[]> s = new ArrayList<byte[]>();
            byte t[] = null;
            LinearEquationProof ep = new LinearEquationProof(s, t);
            return ep;
        }

        BigInteger P_1 = pk.getP().subtract(BigInteger.ONE);
        int vr = 0;
        BigInteger rr = BigInteger.ZERO;
        BigInteger cr = BigInteger.ONE;
        for(int i = 0; i<v_o.length; i++){
            vr += v_o[i];
            rr = rr.add(C_o[i].r).mod(P_1);
            cr = cr.multiply(C_o[i].gmyr).mod(pk.getP());
        }
        BigInteger csc = BigInteger.ONE;
        for(int i = 0; i<v_s.length; i++){
            vr -= v_s[i];
            rr = rr.subtract(C_s[i].r).mod(P_1);
            csc = csc.multiply(C_s[i].gmyr).mod(pk.getP());
        }
        BigInteger[] x = {BigInteger.valueOf(vr), rr};
        csc = csc.modInverse(pk.getP());
        BigInteger y = cr.multiply(csc).mod(pk.getP());
        int[] a = {1, 0};
        int b = 0;
        BigInteger[] g ={pk.getG(), pk.getY()};
        LinearEquationProof bp = LinearEquationProof(y, b, a, x, g, pk);
        return bp;
    }*/
    public LinearEquationProof BalanceProof(ElGamalPublicKey pk, ElGamal_Ciphertext[] C_o, ElGamal_Ciphertext[] C_s, BigInteger[] v_o,BigInteger[] v_s){
        if (C_o.length != v_o.length || C_s.length != v_s.length){
            System.out.println("\n明密文长度不匹配！\n");
            ArrayList<byte[]> s = new ArrayList<byte[]>();
            byte t[] = null;
            LinearEquationProof ep = new LinearEquationProof(s, t);
            return ep;
        }

        BigInteger P_1 = pk.getP().subtract(BigInteger.ONE);
        BigInteger vr = BigInteger.ZERO;
        BigInteger rr = BigInteger.ZERO;
        BigInteger cr = BigInteger.ONE;
        for(int i = 0; i<v_o.length; i++){
            vr.add(v_o[i]);
            rr = rr.add(C_o[i].r).mod(P_1);
            cr = cr.multiply(C_o[i].gmyr).mod(pk.getP());
        }
        BigInteger csc = BigInteger.ONE;
        for(int i = 0; i<v_s.length; i++){
            vr.subtract(v_s[i]);
            rr = rr.subtract(C_s[i].r).mod(P_1);
            csc = csc.multiply(C_s[i].gmyr).mod(pk.getP());
        }
        BigInteger[] x = {vr, rr};
        csc = csc.modInverse(pk.getP());
        BigInteger y = cr.multiply(csc).mod(pk.getP());
        int[] a = {1, 0};
        int b = 0;
        BigInteger[] g ={pk.getG(), pk.getY()};
        LinearEquationProof bp = LinearEquationProof(y, b, a, x, g, pk);
        return bp;
    }

    public boolean VerifyBalanceProof(ElGamalPublicKey pk, ElGamal_Ciphertext[] C_o, ElGamal_Ciphertext[] C_s, LinearEquationProof bp){
        BigInteger cr = BigInteger.ONE;
        for(int i = 0; i<C_o.length; i++){
            cr = cr.multiply(C_o[i].gmyr).mod(pk.getP());
        }
        BigInteger csc = BigInteger.ONE;
        for(int i = 0; i<C_s.length; i++){
            csc = csc.multiply(C_s[i].gmyr).mod(pk.getP());
        }
        csc = csc.modInverse(pk.getP());
        BigInteger y = cr.multiply(csc).mod(pk.getP());
        int[] a = {1, 0};
        int b = 0;
        BigInteger[] g = {pk.getG(), pk.getY()};
        return VerifyLinearEquationProof(bp, y, b, a, g, pk);
    }

    public LinearEquationProof EqualityProof(ElGamalPublicKey pk1, ElGamalPublicKey pk2, ElGamal_Ciphertext c1, ElGamal_Ciphertext c2, BigInteger m){
        if(pk1.getP().equals(pk2.getP()) != true){
            System.out.println("\n零知识证明的两个密码公钥必须在同一个循环群内\n");
            ArrayList<byte[]> s = new ArrayList<byte[]>();
            byte t[] = null;
            LinearEquationProof ep = new LinearEquationProof(s, t);
            return ep;
        }
        BigInteger y = c1.gmyr.multiply(c2.gmyr).mod(pk1.getP());
        BigInteger[] g = {pk1.getG(), pk2.getG(), pk1.getY(), pk2.getY()};
        BigInteger[] x = {m, m, c1.r, c2.r};
        int[] a = {1, -1, 0, 0};
        int b = 0;
        LinearEquationProof ep = LinearEquationProof(y, b, a, x, g, pk1);
        return ep;
    }

    public boolean VerifyEqualityProof(ElGamalPublicKey pk1, ElGamalPublicKey pk2, ElGamal_Ciphertext c1, ElGamal_Ciphertext c2, LinearEquationProof ep) {
        if(pk1.getP().equals(pk2.getP()) != true){
            System.out.println("\n零知识证明的两个密码公钥必须在同一个循环群内\n");
            return false;
        }
        BigInteger y = c1.gmyr.multiply(c2.gmyr).mod(pk1.getP());
        BigInteger[] g = {pk1.getG(), pk2.getG(), pk1.getY(), pk2.getY()};
        int[] a = {1, -1, 0, 0};
        int b = 0;
        return VerifyLinearEquationProof(ep, y, b, a, g, pk1);
    }

    private LinearEquationProof LinearEquationProof(BigInteger y,int b ,int a[], BigInteger[] x, BigInteger[] g, ElGamalPublicKey pk){
        BigInteger P_1 = pk.getP().subtract(BigInteger.ONE);
        //生成两个随机数a,b
        BigInteger[] v = new BigInteger[a.length];
        boolean[] ss = new boolean[a.length];
        int ssnum = 0;
        for(int i = 0; i<a.length; i++){
            if (a[i] == 0){
                ss[i] = false;
            }else{
                ss[i] = true;
                ssnum += 1;
            }
        }

        BigInteger pPrime = pk.getP().subtract(BigInteger.ONE).divide(ElGamalKeyPairGenerator.TWO);
        BigInteger[] rbi = null;
        if (ssnum == 0){
            rbi = new BigInteger[a.length];
            for(int i = 0; i<a.length; i++){
                rbi[i] = NTL.RandomBnd(pPrime);
            }
        }else {
            rbi = new BigInteger[a.length-1];
            for(int i = 0; i<a.length-1; i++){
                rbi[i] = NTL.RandomBnd(pPrime);
            }
        }

        int line = 0;
        BigInteger last = new BigInteger("0");
        for(int i = 0; i<a.length; i++){
            if (a[i] == 0){
                v[i] = rbi[line];
                line++;
            }else{
                if (ssnum == 1){
                    v[i] = BigInteger.valueOf(a[i]).modInverse(P_1).multiply(last).mod(P_1);
                    ssnum --;
                }else{
                    v[i] = rbi[line];
                    line++;
                    ssnum--;
                    BigInteger buf = BigInteger.valueOf(a[i]).multiply(v[i]);
                    last = last.subtract(buf).mod(P_1);
                }
            }
        }
        //验证
        BigInteger vrf = new BigInteger("0");
        for(int i = 0; i<v.length; i++){
            BigInteger buf = v[i].multiply(BigInteger.valueOf(a[i]));
            vrf = vrf.add(buf).mod(P_1);
        }
        //计算t
        BigInteger t = new BigInteger("1");
        byte[] c_mash = {};
        for(int i = 0; i<g.length; i++){
            BigInteger Gi = g[i];
            Gi = Gi.modPow(v[i], pk.getP());
            t = t.multiply(Gi).mod(pk.getP());
            c_mash = byteMerger(c_mash, g[i].toByteArray());
        }
        //计算c
        c_mash = byteMerger(c_mash, y.toByteArray());
        c_mash = byteMerger(c_mash, t.toByteArray());
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] c_mash_ = digest.digest(c_mash);
        BigInteger c_bi = new BigInteger(c_mash_);
        c_bi = c_bi.mod(pk.getP());

        //计算 s
        //byte s[][] = null;
        ArrayList<byte[]> s = new ArrayList<byte[]>();
        for(int i = 0; i<v.length; i++) {;
            x[i] = x[i].multiply(c_bi);
            v[i] = v[i].subtract(x[i]).mod(P_1);
            s.add(v[i].toByteArray());
        }

        LinearEquationProof lp = new LinearEquationProof(s, t.toByteArray());
        return lp;
    }

    private boolean VerifyLinearEquationProof(LinearEquationProof lp, BigInteger y, int b, int a[], BigInteger[] g, ElGamalPublicKey pk){
        BigInteger P_1 = pk.getP().subtract(BigInteger.ONE);
        byte[] c_mash = {};
        for(int i = 0; i<g.length; i++){;
            c_mash = byteMerger(c_mash, g[i].toByteArray());
        }
        //计算 c
        c_mash = byteMerger(c_mash, y.toByteArray());
        c_mash = byteMerger(c_mash, lp.t);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] c_mash_ = digest.digest(c_mash);
        BigInteger c_bi = new BigInteger(c_mash_);
        c_bi = c_bi.mod(pk.getP());
        //验证 t 值
        BigInteger t_verify = y.modPow(c_bi, pk.getP());
        for(int i = 0; i<g.length; i++){
            BigInteger buf = g[i];
            buf = buf.modPow(new BigInteger(lp.s.get(i)), pk.getP());
            t_verify = t_verify.multiply(buf).mod(pk.getP());
        }

        if(t_verify.equals(new BigInteger(lp.t))){
        }else{
            System.out.println("线性相等证明验证失败");
            return false;
        }
        //验证 s 值
        BigInteger cb = BigInteger.valueOf(b);
        cb = cb.multiply(c_bi);
        cb = cb.negate();
        BigInteger mix = BigInteger.ZERO;
        for(int i = 0; i<a.length; i++){
            BigInteger Ai = BigInteger.valueOf(a[i]);
            BigInteger Si = new BigInteger(lp.s.get(i));
            BigInteger aisi = Ai.multiply(Si).mod(P_1);
            mix.add(aisi).mod(P_1);
        }
        if(mix.equals(cb)){
        }else{
            System.out.println("线性相等证明验证失败");
            return false;
        }
        return true;
    }

    public RangeProof ProveRangeProof(BigInteger number, BigInteger randomness){
        GroupElement v = parameters.getBase().commit(number, randomness);
        PeddersenCommitment<?> witness = new PeddersenCommitment<>(parameters.getBase(),number, randomness);
        RangeProof proof = new RangeProofProver().generateProof(parameters, v, witness);
        return proof;
    }

    public void GenrateRangeProofParams(int size, MoudleGroup group){
        this.parameters= GeneratorParams.generateParams(size, group);
        this.group = group;
    }

    public void GenrateRangeProofParams(VectorBase<MoudleElement> vectorBase, PeddersenBase<MoudleElement> base){
        this.group = base.getGroup();
        this.parameters = new GeneratorParams<MoudleElement>(vectorBase, base, group);
    }

    public void GenrateRangeProofParams(int size, PeddersenBase<MoudleElement> base){
        this.group = base.getGroup();
        VectorX<MoudleElement> gs = VectorX.range(0, size).map(i -> ProofUtils.paddedHash("G", i)).map(group::mapInto);
        VectorX<MoudleElement> hs = VectorX.range(0, size).map(i -> ProofUtils.paddedHash("H", i)).map(group::mapInto);
        MoudleElement g = base.g;
        MoudleElement h = base.h;
        VectorBase<MoudleElement> vectorBase = new VectorBase<>(new GeneratorVector<>(gs, group), new GeneratorVector<>(hs, group), h);
        this.parameters = new GeneratorParams<MoudleElement>(vectorBase, base, group);
    }

    public boolean VerifyRangeProof(GroupElement<MoudleElement> v, RangeProof<MoudleElement> proof) throws VerificationFailedException{
        RangeProofVerifier verifier = new RangeProofVerifier();
        try{
            verifier.verify(parameters, v, proof);
        }catch(VerificationFailedException e)
        {
            return false;
        }
        return true;
    }
    public List<byte[]> EncodeRangeProof(RangeProof<MoudleElement> rangeproof){
        return HexUtil.bytetobyte32list(rangeproof.serialize());
    }
    public Group getGroup(){
        return this.group;
    }

    public RangeProof<MoudleElement> DecodeRangeProof(List<byte[]> bytes){
        Group<MoudleElement> group = this.getGroup();
        int len = (bytes.size()-10)/2;
        List<byte[]> Lbytes = bytes.subList(0, len);
        List<byte[]> Rbytes = bytes.subList(len, 2*len);
        List<MoudleElement> L = new ArrayList<>();
        List<MoudleElement> R = new ArrayList<>();
        BigInteger a = new BigInteger(bytes.get(2 * len));
        BigInteger b = new BigInteger(bytes.get(2 * len + 1));
        for(int i = 0; i < len; i++){
            MoudleElement l = group.generator().from(new BigInteger(Lbytes.get(i)));
            MoudleElement r = group.generator().from(new BigInteger(Rbytes.get(i)));
            L.add(l);
            R.add(r);
        }
        InnerProductProof<MoudleElement> proof = new InnerProductProof<MoudleElement>(L, R, a, b);
        MoudleElement aI = group.generator().from(new BigInteger(bytes.get(2 * len + 2)));
        MoudleElement s = group.generator().from(new BigInteger(bytes.get(2 * len + 3)));
        MoudleElement g = group.generator().from(new BigInteger(bytes.get(2 * len + 4)));
        MoudleElement h = group.generator().from(new BigInteger(bytes.get(2 * len + 5)));
        VectorX<MoudleElement> gs = VectorX.of(g,h);
        // System.out.println(gs.size());
        GeneratorVector<MoudleElement> tCommits = new GeneratorVector<>(gs, group);
        BigInteger tauX = new BigInteger(bytes.get(2 * len + 6));
        BigInteger mu = new BigInteger(bytes.get(2 * len + 7));
        BigInteger t = new BigInteger(bytes.get(2 * len + 8));

        return new RangeProof<MoudleElement>(aI, s, tCommits, tauX, mu, t, proof);
    }

    private static byte[] byteMerger(byte[] bt1, byte[] bt2){
       byte[] bt3 = new byte[bt1.length+bt2.length];
       System.arraycopy(bt1, 0, bt3, 0, bt1.length);
       System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
       return bt3;
   }
}
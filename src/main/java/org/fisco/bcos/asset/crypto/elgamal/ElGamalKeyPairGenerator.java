package org.fisco.bcos.asset.crypto.elgamal;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;
import java.util.Random;

import org.fisco.bcos.asset.crypto.ecdsasign.HexUtil;
import org.fisco.bcos.asset.crypto.misc.CipherConstants;
import org.fisco.bcos.asset.crypto.misc.NTL;

public class ElGamalKeyPairGenerator extends KeyPairGeneratorSpi implements CipherConstants
{
	//private int keysize = 1024;
	private int keysize = 256;
	private SecureRandom random = null;
	private boolean ADDITIVE = false;
	
	public void initialize(int keysize, SecureRandom random) 
	{
		this.keysize = keysize;
		this.random = random;
	}

	public KeyPair generateKeyPair() 
	{
		long start_time = -1;
		if(this.random == null)
		{
			random = new SecureRandom();
			this.ADDITIVE = true;
		}
		
		// (a) take a random prime p with getPrime() function. p = 2 * p' + 1 with prime(p') = true
		start_time = System.nanoTime();
		//BigInteger p = getPrime(keysize-2, random);
		BigInteger p = new BigInteger("52416403975283754096244170861174657548739645501393878802972411900588990589343");
		//System.out.println("Obtaining p and q time: " + (System.nanoTime() - start_time)/BILLION + " seconds.");
		
		// (b) take a random element in [Z/Z[p]]* (p' order)
		BigInteger g = NTL.RandomBnd(p);
		BigInteger q = p.subtract(BigInteger.ONE).divide(TWO);

		start_time = System.nanoTime();
		while (true) 
		{
			g = NTL.RandomBnd(p);
			g = g.modPow(TWO, p);
			
			if(g.equals(BigInteger.ONE))
			{
				continue;
			}
			
			if(g.equals(TWO))
			{
				continue;
			}
			
			// Discard g if it divides p-1 because of the attack described
		    // in Note 11.67 (iii) in HAC
			if(p.subtract(BigInteger.ONE).mod(g).equals(BigInteger.ZERO))
			{
				continue;
			}
			
			// g^{-1} must not divide p-1 because of Khadir's attack
			// described in "Conditions of the generator for forging ElGamal
			// signature", 2011
			if(!p.subtract(BigInteger.ONE).mod(g.modInverse(p)).equals(BigInteger.ZERO))
			{
				if(g.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)),p).equals(BigInteger.ONE))
				break;
			}
		}
		//System.out.println("Obtaining Generator g time: " + (System.nanoTime() - start_time)/BILLION + " seconds.");


		BigInteger h = NTL.RandomBnd(p);
		start_time = System.nanoTime();
		while (true)
		{
			h = NTL.RandomBnd(p);
			h = h.modPow(TWO, p);

			if(h.equals(BigInteger.ONE))
			{
				continue;
			}

			if(g.equals(TWO))
			{
				continue;
			}

			// Discard g if it divides p-1 because of the attack described
			// in Note 11.67 (iii) in HAC
			if(p.subtract(BigInteger.ONE).mod(h).equals(BigInteger.ZERO))
			{
				continue;
			}

			// g^{-1} must not divide p-1 because of Khadir's attack
			// described in "Conditions of the generator for forging ElGamal
			// signature", 2011
			if(!p.subtract(BigInteger.ONE).mod(h.modInverse(p)).equals(BigInteger.ZERO))
			{
				if(h.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)),p).equals(BigInteger.ONE))
				break;
			}
		}
		//System.out.println("Obtaining Generator h time: " + (System.nanoTime() - start_time)/BILLION + " seconds.");

		// (c) take x random in [0, p' - 1]
		BigInteger x = NTL.RandomBnd(q);
		BigInteger y = h.modPow(x, p);

		// secret key is (p, x) and public key is (p, g, h)
		ElGamalPrivateKey sk = new ElGamalPrivateKey(p, x, g, h, y, ADDITIVE);
		ElGamalPublicKey pk = new ElGamalPublicKey(p, g, h, y, ADDITIVE);
		if (ADDITIVE)
		{
			//System.out.println("El-Gamal Key pair generated! (Supports Addition over Ciphertext/Scalar Multiplication");
		}
		else
		{
			//System.out.println("El-Gamal Key pair generated! (Supports Multiplication over Ciphertext)");
		}
		return new KeyPair(pk, sk);
	}

	/**
	 * Return a prime p = 2 * p' + 1
	 *
	 * @param nb_bits   is the prime representation
	 * @param prg       random
	 * @return p
	 */
	public static BigInteger getPrime(int nb_bits, Random prg) 
	{
		BigInteger pPrime = new BigInteger(nb_bits, CERTAINTY, prg);
		// p = 2 * pPrime + 1
		BigInteger p = pPrime.multiply(TWO).add(BigInteger.ONE);

		while (!p.isProbablePrime(CERTAINTY)) 
		{
			pPrime = new BigInteger(nb_bits, CERTAINTY, prg);
			p = pPrime.multiply(TWO).add(BigInteger.ONE);
		}
		return p;
	}
}

package org.fisco.bcos.asset.crypto.elgamal;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ElGamal_Ciphertext implements Serializable
{
	private static final long serialVersionUID = -4168027417302369803L;
	public final BigInteger gmyr; //(g^m * y^r)
	public final BigInteger hr; //(h^r)
	public final BigInteger r;
	
	public ElGamal_Ciphertext(BigInteger hr, BigInteger gmyr, BigInteger r)
	{
		this.hr = hr;
		this.gmyr = gmyr;
		this.r = r;
	}
	
	// used for signatures
	public BigInteger getA()
	{
		return this.hr;
	}
	
	public BigInteger getB() { return this.gmyr; }

	public BigInteger getR() { return this.r; }
}

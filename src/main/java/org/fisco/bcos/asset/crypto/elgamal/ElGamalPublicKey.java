package org.fisco.bcos.asset.crypto.elgamal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;

public final class ElGamalPublicKey implements Serializable, PublicKey, ElGamal_Key
{
	private static final long serialVersionUID = -6796919675914392847L;
	protected final BigInteger p;
	protected final BigInteger g;
	protected final BigInteger h;//论文中的h
	protected final BigInteger y;//论文中的y

	public boolean ADDITIVE;

	public ElGamalPublicKey(BigInteger p, BigInteger g, BigInteger h, BigInteger y, boolean ADDITIVE)
	{
		this.p = p;
		this.g = g;
		this.h = h;
		this.y = y;
		this.ADDITIVE = ADDITIVE;
	}

	public String getAlgorithm() 
	{
		return "ElGamal";
	}

	public String getFormat() 
	{
		return "X.509";
	}

	public byte[] getEncoded() 
	{
		return null;
	}

	public BigInteger getP() 
	{
		return this.p;
	}

	public BigInteger getG()
	{
		return this.g;
	}

	public BigInteger getH()
	{
		return this.h;
	}

	public BigInteger getY()
	{
		return this.y;
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException,IOException
	{
		aInputStream.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream) throws IOException
	{
		aOutputStream.defaultWriteObject();
	}

	public String toString()
	{
		String answer = "";
		answer += "p=" + this.p + '\n';
		answer += "g=" + this.g + '\n';
		answer += "h=" + this.h + '\n';
		return answer;
	}
}

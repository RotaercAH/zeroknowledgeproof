## 基于Fisco Bcos平台搭建区块链底层平台，建链过程参见https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/introduction.html

## 密码学算法部分位于asset-app/src/main/java/org/fisco/bcos/asset/crypto/

## 实现了两方签名以及零知识证明部分，零知识证明使用方式如下

# zeroknowledgeproof库使用说明

> 零知识证明库，包含各类零知识证明的生成与验证

|          名称           | 类型 |                 用途                 |
| :---------------------: | :--: | :----------------------------------: |
|      `FormatProof`      |  类  |      作为格式正确证明结构体使用      |
|  `LinearEquationProof`  |  类  | 作为会计平衡证明、相等证明结构体使用 |
| `ZeroKonwledgeProofGorV |  类  |    实现各类零知识证明的生成与验证    |
|     **FormatProof**     | 函数 |           生成格式正确证明           |
|  **VerifyFormatProof**  | 函数 |           验证格式正确证明           |
|    **EqualityProof**    | 函数 |             生成相等证明             |
| **VerifyEqualityProof** | 函数 |             验证相等证明             |
|    **BalanceProof**     | 函数 |           生成会计平衡证明           |
| **VerifyBalanceProof**  | 函数 |           验证会计平衡证明           |

---

#### FormatProof

*生成格式正确证明*

```java
public FormatProof FormatProof(BigInteger v, ElGamalPublicKey pk, BigInteger r)
```

##### 输入：

v：待加密数值

pk：加密数值所用公钥

r：用于加密的随机数

##### 输出：

FormatProof：格式正确证明结构体

##### 示例：

```java
ElGamal_Ciphertext add_1 = ElGamalCipher.encrypt(new BigInteger("2345"), e_pk);
FormatProof fp = test.FormatProof(new BigInteger("2345"), e_pk, add_1.r);
```

---

#### VerifyFormatProof

*验证格式正确证明*

```java
public boolean VerifyFormatProof(ElGamal_Ciphertext c, ElGamalPublicKey pk, FormatProof fp)
```

##### 输入：

c：待验证的密文

pk：加密数值所用公钥

fp：生成者生成的格式正确证明

##### 输出：

格式正确证明是否验证通过

##### 示例：

```java
ElGamal_Ciphertext add_1 = ElGamalCipher.encrypt(new BigInteger("2345"), e_pk);
FormatProof fp = test.FormatProof(new BigInteger("2345"), e_pk, add_1.r);
boolean output = test.VerifyFormatProof(add_1, e_pk, fp);
System.out.printf("格式正确证明验证结果%s\n", output);
```

---

#### EqualityProof

*生成相等证明*

```java
public LinearEquationProof EqualityProof(ElGamalPublicKey pk1, ElGamalPublicKey pk2, ElGamal_Ciphertext c1, ElGamal_Ciphertext c2, BigInteger m)
```

输入：

pk1, pk2：生成承诺的两个公钥（两个公钥的P要相同）

c1, c2：生成的两个密文

m：用于生成密文的金额

##### 输出：

LinearEquationProof：相等证明结构体

##### 示例：

```java
ElGamal_Ciphertext test_1 = ElGamalCipher.encrypt(new BigInteger("16"),e_pk);
ElGamal_Ciphertext test_2 = ElGamalCipher.encrypt(new BigInteger("16"),e_pk2);
LinearEquationProof ep = test.EqualityProof(e_pk, e_pk2, test_1, test_2, new BigInteger("16"));
```

---

#### VerifyEqualityProof

*验证相等证明*

```java
public boolean VerifyEqualityProof(ElGamalPublicKey pk1, ElGamalPublicKey pk2, ElGamal_Ciphertext c1, ElGamal_Ciphertext c2, LinearEquationProof ep)
```

##### 输入：

pk1, pk2：生成承诺的两个公钥

c1, c2：生成的两个密文

eq：生成者生成的相等证明

##### 输出：

相等证明是否验证通过

##### 示例：

```java
LinearEquationProof ep = test.EqualityProof(e_pk, e_pk2, test_1, test_2, new BigInteger("16"));
boolean output2 = test.VerifyEqualityProof(e_pk, e_pk2, test_1, test_2, ep);
System.out.printf("相等证明验证结果%s\n", output2);
```

---

#### BalanceProof

*生成会计平衡证明（证明 ∑v_o = ∑v_s）*

```java
public LinearEquationProof BalanceProof(ElGamalPublicKey pk, ElGamal_Ciphertext[] C_o, ElGamal_Ciphertext[] C_s, int[] v_o, int[] v_s)
```

##### 输入：

pk：生成者公钥

C_o：原金额密文数组

C_s：开销金额密文数组

v_o：原金额数组

v_s：开销金额数组

##### 输出：

LinearEquationProof：会计平衡证明结构体

##### 示例：

```java
int[] v_o = {2,3,5};
int[] v_s = {5,5};
ElGamal_Ciphertext two = ElGamalCipher.encrypt(new BigInteger("2"),e_pk);
ElGamal_Ciphertext three =ElGamalCipher.encrypt(new BigInteger("3"),e_pk);
ElGamal_Ciphertext five = ElGamalCipher.encrypt(new BigInteger("5"),e_pk);
ElGamal_Ciphertext[] C_o = {two, three, five};
ElGamal_Ciphertext[] C_s = {five, five};
LinearEquationProof bp = test.BalanceProof(e_pk, C_o, C_s, v_o, v_s);
```

---

#### VefityBalanceProof

*验证会计平衡证明（证明 ∑v_o = ∑v_s）*

```java
public boolean VerifyBalanceProof(ElGamalPublicKey pk,ElGamal_Ciphertext[] C_o, ElGamal_Ciphertext[] C_s, LinearEquationProof bp)
```

##### 输入：

pk：生成者公钥

C_o：原金额密文数组

C_s：开销金额密文数组

LinearEquationProof：生成者生成的会计平衡证明

##### 输出：

会计平衡证明是否验证通过

##### 示例：

```java
LinearEquationProof bp = test.BalanceProof(e_pk, C_o, C_s, v_o, v_s);
boolean output3 = test.VerifyBalanceProof(e_pk, C_o, C_s, bp);
System.out.printf("会计平衡证明验证结果%s\n", output3);
```

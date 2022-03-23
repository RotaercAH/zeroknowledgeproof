# ElGamal库使用说明



> ElGamal库包括公私钥生成，加密，解密，签名，验签 算法
>


|             名称             |        用途        |
| :--------------------------: | :----------------: |
|   ElGamal_Ciphertext.java    | 作为密文结构体使用 |
|    ElGamalPrivateKey.java    | 作为私钥结构体使用 |
|    ElGamalPublicKey.java     | 作为公钥结构体使用 |
| ElGamalKeyPairGenerator.java |   初始化公私钥对   |
|        ElGamalCipher         |     加解密操作     |
|       ElGamalSignature       |     签名、验签     |
|         ElGamalTest          |      测试用例      |

### ElGamal_Ciphertext.java

#### getA

获取密文第一部分C1 = gr 

```java
public BigInteger getA(){ return this.gr; }
```

#### getB

获取密文第二部分C2 = hrgm

```java
public BigInteger getB(){ return this.hrgm;}
```

### ElGamalKeyPairGenerator.java

---

#### initialize

初始化操作，设置密钥大小，使用ElGamal还是AH-ElGamal进行加密

```java
public void initialize(int keysize, SecureRandom random) 
```

##### 输入：

keysize：密钥大小

random：此值为null则使用AH-ElGamal进行加密，此值为随机数则使用ElGamal进行加密

##### 示例：

```java
ElGamalKeyPairGenerator key_pair = new ElGamalKeyPairGenerator();
key_pair.initialize(KEY_SIZE, new SecureRandom());//使用ElGamal加密算法
//or
key_pair.initialize(KEY_SIZE, null);//使用AH-ElGamal加密算法
```
---

#### generateKeyPair

生成公私钥对

```java
public KeyPair generateKeyPair() 
```

##### 输出：

KeyPair： java提供的密钥管理对象

##### 示例：

```java
ElGamalKeyPairGenerator key_pair = new ElGamalKeyPairGenerator();
KeyPair key = key_pair.generateKeyPair();
```

---

### ElGamalCipher.java

---

#### encrypt

执行加密操作

```java
public static ElGamal_Ciphertext encrypt(BigInteger plaintext, ElGamalPublicKey pk)
```

##### 输入：

plaintext: 明文

pk： 用户公钥

##### 输出：

ElGamal_Ciphertext: 密文对象

##### 示例：

```java
ElGamalCipher.encrypt(15, e_pk);
```

---

#### decrypt

执行解密操作

```java
public static BigInteger decrypt(ElGamal_Ciphertext ciphertext, ElGamalPrivateKey sk)
```

##### 输入：

ciphertext: 密文对象

sk： 用户私钥

##### 输出：

BigInteger： 明文

##### 示例：

```java
ElGamal_Ciphertext temp = ElGamalCipher.encrypt(233, e_pk);
BigInteger out = ElGamalCipher.decrypt(temp ,e_sk);
```

、


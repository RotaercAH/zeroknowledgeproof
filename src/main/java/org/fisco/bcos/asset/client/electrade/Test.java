package org.fisco.bcos.asset.client.electrade;

import com.starkbank.ellipticcurve.PrivateKey;
import com.starkbank.ellipticcurve.PublicKey;
import org.fisco.bcos.asset.client.TradeCountStruct;
import org.fisco.bcos.asset.contractuse.EvidenceContract;
import org.fisco.bcos.asset.contractuse.TradeContract;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalCipher;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalKeyPair;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.HexUtil;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

import java.math.BigInteger;
import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        HashMap<String, ElGamalKeyPair> elgamal = new HashMap<>();
        HashMap<String, PrivateKey> ecdsaSign = new HashMap<>();
        HashSet<String> user = new HashSet<String>();
        //初始化fisco bcos客户端
        FiscoInit fisco = new FiscoInit();
        Client testclient = fisco.initialize();
        TradeContract trade = new TradeContract(testclient, fisco.getFiscoAccount());
        EvidenceContract evidence = new EvidenceContract(testclient, fisco.getFiscoAccount());
        //注册管理员账户
        fisco.getFiscoAccount("admin");
        //部署存证合约
        evidence.deployEvidenceRepositoryAndRecordAddr("admin");
        evidence.deployRequestRepositoryAndRecordAddr("admin");
        evidence.deployEvidenceControllerAndRecordAddr("admin");
        evidence.evidenceAllow("admin");
        evidence.requestAllow("admin");
        //部署交易合约
        trade.deployAdminAndRecordAddr("admin");
        trade.getControllerAddress("admin");
        trade.registerAccount("admin");
        trade.addIssuer("admin");
        //初始化管理员账户
        ElGamalKeyPair keyPair0 = ElGamalKeyPair.getElgamalKeyPair();
        elgamal.put("admin", keyPair0);
        trade.issueElGamalAccount("admin", new BigInteger("1000000"), elgamal.get("admin").pk);
        //System.out.println("用户" + "admin" + "账户余额为： " + trade.balanceElGamalAccount("admin", elgamal.get("admin").sk));
        while (true){
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            String []str2 = str.split(" ");
            switch (str2[0]){
                case "quit":
                    System.exit(0);
                case "register": 
                    if (str2.length < 2) {
                        Usage();
                        break;
                    }
                    fisco.getFiscoAccount(str2[1]);
                    trade.registerAccount(str2[1]);
                    ElGamalKeyPair keyPair = ElGamalKeyPair.getElgamalKeyPair();
                    PrivateKey privateKey = new PrivateKey();
                    ecdsaSign.put(str2[1], privateKey);
                    elgamal.put(str2[1], keyPair);
                    trade.issueElGamalAccount(str2[1], new BigInteger("0"), elgamal.get(str2[1]).pk);
                    user.add(str2[1]);
                    break;
                case "consensus":
                    if (str2.length < 4) {
                        Usage();
                    }
                    if (!user.contains(str2[1])){
                        System.out.println(str2[1] + "未注册！");
                        break;
                    }
                    if (!user.contains(str2[2])){
                        System.out.println(str2[2] + "未注册！");
                        break;
                    }
                    ZeroKonwledgeProofGorV zkp = new ZeroKonwledgeProofGorV();
                    Business bs = new Business();
                    byte[] hash = bs.consensusValue("admin", new BigInteger(str2[3]), elgamal.get(str2[1]).pk, ecdsaSign.get(str2[1]), ecdsaSign.get(str2[2]), evidence, zkp);
                    boolean verify1 = Business.verifyConsensusValue("admin", hash, elgamal.get(str2[1]).pk, ecdsaSign.get(str2[1]).publicKey(), ecdsaSign.get(str2[2]).publicKey(), evidence, zkp);
                    System.out.println(HexUtil.encodeHexString(hash));
                    System.out.println(verify1);
                    break;
                case "transfer":
                    if (str2.length < 4) {
                        Usage();
                        break;
                    }
                    if (!user.contains(str2[1])){
                        System.out.println(str2[1] + "未注册！");
                        break;
                    }
                    if (!user.contains(str2[2])){
                        System.out.println(str2[2] + "未注册！");
                        break;
                    }
                    System.out.println("in trade");
                    ZeroKonwledgeProofGorV zkp1 = new ZeroKonwledgeProofGorV();
                    ZeroKonwledgeProofGorV zkp2 = new ZeroKonwledgeProofGorV();
                    Business bs2 = new Business();
                    TradeCountStruct countStruct1 = bs2.tradeCount(new BigInteger(str2[3]), elgamal.get(str2[1]), elgamal.get(str2[2]), trade, str2[1], str2[2]);
                    byte[] hash2 = bs2.trade(new BigInteger(str2[3]), elgamal.get(str2[1]), elgamal.get(str2[2]), zkp1, zkp2, countStruct1, "admin", evidence);
                    boolean verify3 = bs2.verifyTrade(hash2, elgamal.get(str2[1]).pk, elgamal.get(str2[2]).pk, str2[1], str2[2], zkp1, zkp2, countStruct1, trade, "admin", evidence);
                    System.out.println("用户" + str2[1] + "账户余额为： " + trade.balanceElGamalAccount(str2[1], elgamal.get(str2[1]).sk));
                    System.out.println("用户" + str2[2] + "账户余额为： " + trade.balanceElGamalAccount(str2[2], elgamal.get(str2[2]).sk));
                    break;
                case "issue":
                    if (str2.length < 3) {
                        Usage();
                        break;
                    }
                    if (!user.contains(str2[1])){
                        System.out.println(str2[1] + "未注册！");
                        break;
                    }
                    ZeroKonwledgeProofGorV zkp_1 = new ZeroKonwledgeProofGorV();
                    ZeroKonwledgeProofGorV zkp_2 = new ZeroKonwledgeProofGorV();
                    Business bs1 = new Business();
                    System.out.println(elgamal.get("admin").pk.getP());
                    TradeCountStruct countStruct = bs1.tradeCount(new BigInteger(str2[2]), elgamal.get("admin"), elgamal.get(str2[1]), trade, "admin", str2[1]);
                    byte[] hash1 = bs1.trade(new BigInteger(str2[2]), elgamal.get("admin"), elgamal.get(str2[1]), zkp_1, zkp_2, countStruct, "admin", evidence);
                    boolean verify2 = bs1.verifyTrade(hash1, elgamal.get("admin").pk, elgamal.get(str2[1]).pk,"admin", str2[1], zkp_1, zkp_2, countStruct, trade, "admin", evidence);
                    System.out.println("用户" + str2[1] + "账户余额为： " + trade.balanceElGamalAccount(str2[1], elgamal.get(str2[1]).sk));
                    break;
                default:
                    Usage();
                    break;
            }
        }
        //for(int i = 0; i<100;i++) {
            //初始化fisco bcos客户端
            /*FiscoInit fisco = new FiscoInit();
            Client testclient = fisco.initialize();
            TradeContract trade = new TradeContract(testclient, fisco.getFiscoAccount());
            EvidenceContract evidence = new EvidenceContract(testclient, fisco.getFiscoAccount());
            //注册fisco bcos账户
            fisco.getFiscoAccount("admin");
            fisco.getFiscoAccount("user1");
            fisco.getFiscoAccount("user2");
            //部署存证合约
            evidence.deployEvidenceRepositoryAndRecordAddr("admin");
            evidence.deployRequestRepositoryAndRecordAddr("admin");
            evidence.deployEvidenceControllerAndRecordAddr("admin");
            evidence.evidenceAllow("admin");
            evidence.requestAllow("admin");
            //生成ElGamal公私钥对
            ElGamalKeyPair keyPair = ElGamalKeyPair.getElgamalKeyPair();
            System.out.println("pk" + keyPair.pk);
            ElGamalKeyPair keyPair2 = ElGamalKeyPair.getElgamalKeyPair();
            ElGamal_Ciphertext test5 = ElGamalCipher.encrypt(BigInteger.TEN, keyPair.pk);
            long startTime3 = System.nanoTime();
            //long startTime2 = System.currentTimeMillis();
            for (int q = 0 ;q<100;q++) {
                    ElGamalCipher.decrypt(test5, keyPair.sk);
                    //if(verify1 == false){count4++;}
            }
            //long endTime2 = System.currentTimeMillis();
            long endTime3 = System.nanoTime();
            System.out.println("解密time： " + (endTime3 - startTime3) + "ns");
            //生成ECDSA签名算法公私钥对
            PrivateKey privateKey = new PrivateKey();
            PublicKey publicKey = privateKey.publicKey();
            PrivateKey privateKey2 = new PrivateKey();
            PublicKey publicKey2 = privateKey2.publicKey();
            //调用电价协商函数
            ZeroKonwledgeProofGorV zkp = new ZeroKonwledgeProofGorV();
            Business bs = new Business();
            byte[] hash = bs.consensusValue("admin", BigInteger.valueOf(20), keyPair.pk, privateKey, privateKey2, evidence, zkp);
            //调用电价协商验证函数
            long startTime2 = System.currentTimeMillis();
            int count4 = 0;
            //for (int q = 0 ;q<100;q++) {
            boolean verify1 =   bs.verifyConsensusValue("admin", hash, keyPair.pk, publicKey, publicKey2, evidence, zkp);
            //if(verify1 == false){count4++;}
            //}
            long endTime2 = System.currentTimeMillis();
            System.out.println("电价协商验证time： " + (endTime2 - startTime2) + "ms");
            System.out.println("false: " + count4);
            //初始化交易双方账户
            trade.deployAdminAndRecordAddr("admin");
            trade.getControllerAddress("admin");
            trade.registerAccount("admin");
            trade.registerAccount("user1");
            trade.registerAccount("user2");
            trade.addIssuer("admin");
            trade.addIssuer("user1");
            trade.addIssuer("user2");
            trade.issueElGamalAccount("user1", BigInteger.valueOf(1000), keyPair.pk);
            trade.issueElGamalAccount("user2", BigInteger.valueOf(10), keyPair2.pk);
            System.out.println(trade.balanceElGamalAccount("user1", keyPair.sk));
            System.out.println(trade.balanceElGamalAccount("user2", keyPair2.sk));
            //调用
            ZeroKonwledgeProofGorV zkp_1 = new ZeroKonwledgeProofGorV();
            ZeroKonwledgeProofGorV zkp_2 = new ZeroKonwledgeProofGorV();
            long startTime4 = System.currentTimeMillis();
            for (int q = 0 ; q < 1; q++) {
                    TradeCountStruct countStruct = bs.tradeCount(BigInteger.valueOf(1), keyPair, keyPair2, trade, "user1", "user2");
                    byte[] hash1 = bs.trade(BigInteger.valueOf(1), keyPair, keyPair2, zkp_1, zkp_2, countStruct, "admin", evidence);
            }
            long endTime4 = System.currentTimeMillis();
            System.out.println("电费结算time：    " + (endTime4 - startTime4) + "ms");
            TradeCountStruct countStruct = bs.tradeCount(BigInteger.valueOf(1), keyPair, keyPair2, trade, "user1", "user2");
            byte[] hash1 = bs.trade(BigInteger.valueOf(1), keyPair, keyPair2, zkp_1, zkp_2, countStruct, "admin", evidence);
            long startTime5 = System.currentTimeMillis();
            boolean verify2 = bs.verifyTrade(hash1, keyPair.pk, keyPair2.pk, zkp_1, zkp_2, countStruct, trade, "admin", evidence);
            long endTime5 = System.currentTimeMillis();
            System.out.println("电费结算验证time：    " + (endTime5 - startTime5) + "ms");
            System.out.println("电价交易验证结果                                        " + verify2);
            System.out.println(trade.balanceElGamalAccount("user1", keyPair.sk));
            System.out.println(trade.balanceElGamalAccount("user2", keyPair2.sk));
            ElGamal_Ciphertext[] mlist = new ElGamal_Ciphertext[300];
            BigInteger[] blist = new BigInteger[300];
            for (int i = 0 ; i<300; i++){
                    mlist[i] = test5;
            }
            for (int i = 0 ; i<300; i++){
                    blist[i] = BigInteger.valueOf(2);
            }
            long startTime6 = System.nanoTime();
            for (int q = 0 ;q<100;q++) {
                    bs.calculateValue(blist, mlist, keyPair.pk);
            }
            long endTime6 = System.nanoTime();
            System.out.println("电价计算time： " + (endTime6 - startTime6) + "ns");
            System.out.println(ElGamalCipher.decrypt(bs.calculateValue(blist, mlist, keyPair.pk), keyPair.sk));*/
            /*if(verify1 == false){
                System.out.println("出错啦！！！");
                break;
            }*/
        //}
/*
    switch (args[0]) {
      case "deployAdmin":
        if (args.length < 2) {
          Usage();
        }
        client.deployAdminAndRecordAddr(args[1]);
        break;
      case "getAddress":
        if (args.length < 2) {
          Usage();
        }
        client.getFiscoAccount(args[1]);
        break;
      case "getControllerAddress":
        if (args.length < 3) {
          Usage();
        }
        client.getControllerAddress(args[1], args[2]);
        break;
      case "isIssuer":
        if (args.length < 2) {
          Usage();
        }
        client.isIssuer(args[1]);
        break;
      case "isRegistered":
        if (args.length < 2) {
          Usage();
        }
        client.isRegistered(args[1]);
        break;
      case "issue":
        if (args.length < 4) {
          Usage();
        }
        client.issue(args[1], args[2], args[3]);
        break;
      case "registerAccount":
      if (args.length < 2) {
        Usage();
      }
      client.registerAccount(args[1]);
      break;
      case "getOwnerAddress":
        client.getadminowner();
        break;

      case "deploy":
        client.deployAssetAndRecordAddr();
        break;
      case "query":
        if (args.length < 2) {
          Usage();
        }
        client.queryAssetAmount(args[1]);
        break;
      case "register":
        if (args.length < 3) {
          Usage();long endTime4 = System.currentTimeMillis();
            System.out.println("电费结算time：    " + (endTime4 - startTime4) + "ms");
        }
        client.registerAssetAccount(args[1], new BigInteger(args[2]));
        break;
      case "transfer":
        if (args.length < 4) {
          Usage();
        }
        client.transferAsset(args[1], args[2], new BigInteger(args[3]));
        break;
      default:
        {
          Usage();
        }
    }*/
        //System.exit(0);
    }
    
    public static void Usage(){
            System.out.println("Usage: ");
            System.out.println("register asset_account");
            System.out.println("issue asset_account asset_amount");
            System.out.println("transfer from_asset_account to_asset_account amount");
            System.out.println("");
            System.out.println("examples: ");
            System.out.println("register user1");
            System.out.println("register user2");
            System.out.println("issue user1 500000");
            System.out.println("transfer user1 user2 30000");
    }
}


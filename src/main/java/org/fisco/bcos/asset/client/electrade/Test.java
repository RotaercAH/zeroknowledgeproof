package org.fisco.bcos.asset.client.electrade;

import com.starkbank.ellipticcurve.PrivateKey;
import org.fisco.bcos.asset.contractuse.EvidenceContract;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalKeyPair;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.ZeroKonwledgeProofGorV;
import org.fisco.bcos.asset.crypto.zeroknowledgeproof.bulletproof.util.HexUtil;
import org.fisco.bcos.sdk.client.Client;
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
        EvidenceContract evidence = new EvidenceContract(testclient, fisco.getFiscoAccount());
        //注册管理员账户
        fisco.getFiscoAccount("admin");
        //部署存证合约
        evidence.deployEvidenceRepositoryAndRecordAddr("admin");
        evidence.deployRequestRepositoryAndRecordAddr("admin");
        evidence.deployEvidenceControllerAndRecordAddr("admin");
        evidence.evidenceAllow("admin");
        evidence.requestAllow("admin");
        //初始化管理员账户
        ElGamalKeyPair keyPair0 = ElGamalKeyPair.getElgamalKeyPair();
        elgamal.put("admin", keyPair0);
        //System.out.println("用户" + "admin" + "账户余额为： " + trade.balanceElGamalAccount("admin", elgamal.get("admin").sk));
        while (true) {
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            String[] str2 = str.split(" ");
            switch (str2[0]) {
                case "quit":
                    System.exit(0);
                case "register":
                    if (str2.length < 2) {
                        break;
                    }
                    fisco.getFiscoAccount(str2[1]);
                    ElGamalKeyPair keyPair = ElGamalKeyPair.getElgamalKeyPair();
                    PrivateKey privateKey = new PrivateKey();
                    ecdsaSign.put(str2[1], privateKey);
                    elgamal.put(str2[1], keyPair);
                    user.add(str2[1]);
                    break;
                case "consensus":
                    if (str2.length < 4) {
                    }
                    if (!user.contains(str2[1])) {
                        System.out.println(str2[1] + "未注册！");
                        break;
                    }
                    if (!user.contains(str2[2])) {
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
                default:
                    break;
            }
        }
    }
}


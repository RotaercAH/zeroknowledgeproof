package org.fisco.bcos.asset.contractuse;

import org.fisco.bcos.asset.client.electrade.FiscoInit;
import org.fisco.bcos.asset.contract.RewardPointController;
import org.fisco.bcos.asset.contract.Trade;
import org.fisco.bcos.asset.crypto.ecdsasign.HexUtil;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalCipher;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalPrivateKey;
import org.fisco.bcos.asset.crypto.elgamal.ElGamalPublicKey;
import org.fisco.bcos.asset.crypto.elgamal.ElGamal_Ciphertext;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;

public class TradeContract {
    org.fisco.bcos.sdk.client.Client client;
    HashMap<String, CryptoKeyPair> fiscoAccount;
    static Logger logger = LoggerFactory.getLogger(TradeContract.class);
    public TradeContract(org.fisco.bcos.sdk.client.Client client, HashMap<String, CryptoKeyPair> fiscoAccount){
        this.client = client;
        this.fiscoAccount = fiscoAccount;
    }

    public void deployAdminAndRecordAddr(String username) {
        try {
            Trade trade = Trade.deploy(client, fiscoAccount.get(username));
            System.out.println(
                    " deploy Admin success, contract address is " + trade.getContractAddress());
            FiscoInit.recordAddr("admin" , trade.getContractAddress());
            //contractAddress.put("Admin", admin.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy Admin contract failed, error message is  " + e.getMessage());
        }
    }

    public void getControllerAddress(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("admin");
            Trade trade = Trade.load(contractAddress, client, fiscoAccount.get(username));
            String controllerAddress = trade._controllerAddress();
            //Tuple2<BigInteger, BigInteger> result = asset.select(assetAccount);
            if (contractAddress.length() != 0) {
                System.out.printf(" controllerAddress %s\n", controllerAddress);
                FiscoInit.recordAddr("controllerAddress", controllerAddress);
            } else {
                System.out.printf("controllerAddress is not exist \n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
        }
    }

    public void isIssuer(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            String accountAddress = fiscoAccount.get(username).getAddress();
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            Boolean bool = controller.isIssuer(accountAddress);
            if (bool) {
                System.out.printf(" account %s is issuer\n", accountAddress);
            } else {
                System.out.printf("account $s is not issuer \n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
        }
    }

    public void addIssuer(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            String accountAddress = fiscoAccount.get(username).getAddress();
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            controller.addIssuer(accountAddress);
      /*if (true) {
        System.out.printf(" account %s is issuer\n", accountAddress);
      } else {
        System.out.printf("account $s is not issuer \n");
      }*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
        }
    }

    public void isRegistered(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            String accountAddress = fiscoAccount.get(username).getAddress();
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            Boolean bool = controller.isRegistered(accountAddress);
            if (bool) {
                System.out.printf(" account %s is registered\n", accountAddress);
            } else {
                System.out.printf("account $s is not registered \n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
        }
    }

    public void issue(String username, List<byte[]> value) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            String accountAddress = fiscoAccount.get(username).getAddress();
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            System.out.println(HexUtil.encodeHexString(value.get(0)));
            controller.issue(accountAddress, value);
            if (value.size() != 0) {
            } else {
                System.out.printf("ownerAddress is not exist \n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" issue value failed, error message is %s\n", e.getMessage());
        }
    }

    public List<byte[]> balance(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            String accountAddress = fiscoAccount.get(username).getAddress();
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            List<byte[]> value= controller.balance(accountAddress);
            return value;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
            return null;
        }
    }

    public void registerAccount(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("controllerAddress");
            RewardPointController controller = RewardPointController.load(contractAddress, client, fiscoAccount.get(username));
            //TransactionReceipt tr = controller.register();
            controller.register();
            //if (tr.getMessage().length() != 0) {
            //  System.out.printf(" ownerAddress %s\n", tr.getMessage());
            //} else {
            //  System.out.printf("ownerAddress is not exist \n");
            //}
            System.out.printf(" ownerAddress %s\n", fiscoAccount.get(username).getAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

            System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
        }
    }

    public void issueElGamalAccount(String username, BigInteger value, ElGamalPublicKey pk){
        ElGamal_Ciphertext elgamal = ElGamalCipher.encrypt(value, pk);
        issue(username, ElGamalCipher.encodeCiphertext(elgamal));
    }
    public BigInteger balanceElGamalAccount(String username, ElGamalPrivateKey sk){
        return ElGamalCipher.decrypt(ElGamalCipher.decodeCiphertext(balance(username)), sk);
    }

}

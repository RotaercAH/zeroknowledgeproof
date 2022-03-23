package org.fisco.bcos.asset.contractuse;

import org.fisco.bcos.asset.client.electrade.FiscoInit;
import org.fisco.bcos.asset.contract.EvidenceController;
import org.fisco.bcos.asset.contract.EvidenceRepository;
import org.fisco.bcos.asset.contract.RequestRepository;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvidenceContract {
    org.fisco.bcos.sdk.client.Client client;
    HashMap<String, CryptoKeyPair> fiscoAccount;
    static Logger logger = LoggerFactory.getLogger(TradeContract.class);

    public EvidenceContract(org.fisco.bcos.sdk.client.Client client, HashMap<String, CryptoKeyPair> fiscoAccount) {
        this.client = client;
        this.fiscoAccount = fiscoAccount;
    }

    public void deployEvidenceRepositoryAndRecordAddr(String username) {
        try {
            EvidenceRepository evidence = EvidenceRepository.deploy(client, fiscoAccount.get(username));
            System.out.println(
                    " deploy EvidenceRepository success, contract address is " + evidence.getContractAddress());
            FiscoInit.recordAddr("EvidenceRepository", evidence.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy EvidenceRepository contract failed, error message is  " + e.getMessage());
        }
    }

    public void deployRequestRepositoryAndRecordAddr(String username) {
        try {
            List<String> voterArray = new ArrayList<>();
            voterArray.add(fiscoAccount.get(username).getAddress());
            RequestRepository request = RequestRepository.deploy(client, fiscoAccount.get(username), BigInteger.ONE, voterArray);
            System.out.println(
                    " deploy RequestRepository success, contract address is " + request.getContractAddress());
            FiscoInit.recordAddr("RequestRepository", request.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy RequestRepository contract failed, error message is  " + e.getMessage());
        }
    }

    public void deployEvidenceControllerAndRecordAddr(String username) {
        try {
            List<String> voterArray = new ArrayList<>();
            voterArray.add(fiscoAccount.get(username).getAddress());
            EvidenceController controller = EvidenceController.deploy(client, fiscoAccount.get(username), BigInteger.ONE, voterArray);
            System.out.println(
                    " deploy EvidenceController success, contract address is " + controller.getContractAddress());
            FiscoInit.recordAddr("EvidenceController", controller.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy EvidenceController contract failed, error message is  " + e.getMessage());
        }
    }

    public void evidenceAllow(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("EvidenceRepository");
            EvidenceRepository evidenceRepository = EvidenceRepository.load(contractAddress, client, fiscoAccount.get(username));
            evidenceRepository.allow(FiscoInit.loadAddr("EvidenceController"));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(" evidenceAllow exception, error message is {}", e.getMessage());
            System.out.printf(" allow evidence failed, error message is %s\n", e.getMessage());
        }
    }

    public void requestAllow(String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("RequestRepository");
            EvidenceRepository evidenceRepository = EvidenceRepository.load(contractAddress, client, fiscoAccount.get(username));
            evidenceRepository.allow(FiscoInit.loadAddr("EvidenceController"));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(" requestAllow exception, error message is {}", e.getMessage());
            System.out.printf(" allow request failed, error message is %s\n", e.getMessage());
        }
    }
    //存证方 提交存证请求
    public void createSaveRequest(byte[] hash, List<byte[]> ext, String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("EvidenceController");
            EvidenceController controller = EvidenceController.load(contractAddress, client, fiscoAccount.get(username));
            controller.createSaveRequest(hash, ext);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(" createSaveRequest exception, error message is {}", e.getMessage());
            System.out.printf(" create save request failed, error message is %s\n", e.getMessage());
        }
    }
    //审核者 查看存证请求
    public List<byte[]> getRequestData(byte[] hash, String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("EvidenceController");
            EvidenceController controller = EvidenceController.load(contractAddress, client, fiscoAccount.get(username));
            //Tuple5<byte[] , String , List<byte[]>, BigInteger, BigInteger> = new Tuple5<>();
            Tuple5<byte[], String, List<byte[]>, BigInteger, BigInteger> result = controller.getRequestData(hash);
            return result.getValue3();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(" getRequestData exception, error message is {}", e.getMessage());
            System.out.printf("get request data failed, error message is %s\n", e.getMessage());
            return null;
        }
    }
    //审核者 批准审核请求
    public void voteSaveRequest(byte[] hash, String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("EvidenceController");
            EvidenceController controller = EvidenceController.load(contractAddress, client, fiscoAccount.get(username));
            controller.voteSaveRequest(hash);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" voteSaveRequest exception, error message is {}", e.getMessage());

            System.out.printf(" vote save request failed, error message is %s\n", e.getMessage());
        }
    }
    //取证方 查看存证数据
    public List<byte[]> getEvidence(byte[] hash, String username) {
        try {
            String contractAddress = FiscoInit.loadAddr("EvidenceController");
            EvidenceController controller = EvidenceController.load(contractAddress, client, fiscoAccount.get(username));
            Tuple4<byte[], String, BigInteger, List<byte[]>> result = controller.getEvidence(hash);
            return result.getValue4();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(" voteSaveRequest exception, error message is {}", e.getMessage());
            System.out.printf(" vote save request failed, error message is %s\n", e.getMessage());
            return null;
        }
    }
}

package org.fisco.bcos.asset.client.electrade;

import org.fisco.bcos.asset.client.AssetClient;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class FiscoInit {

    static Logger logger = LoggerFactory.getLogger(AssetClient.class);

    private BcosSDK bcosSDK;
    private Client client;
    private HashMap<String, CryptoKeyPair> fiscoAccount = new HashMap<String, CryptoKeyPair>();
    private static Properties prop = new Properties();

    public HashMap<String, CryptoKeyPair> getFiscoAccount() {
        return fiscoAccount;
    }

    public Properties getProp() {
        return prop;
    }

    public Client initialize() throws Exception {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        bcosSDK = context.getBean(BcosSDK.class);
        client = bcosSDK.getClient(1);
        return client;
    }

    public void getFiscoAccount(String username){
        CryptoKeyPair cryptoKeyPairTemp = client.getCryptoSuite().createKeyPair();
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPairTemp);
        fiscoAccount.put(username, cryptoKeyPairTemp);
        System.out.printf("%s address is: %s\n",username ,cryptoKeyPairTemp.getAddress());
    }

    public static void recordAddr(String address_name, String address) throws FileNotFoundException, IOException {
        //Properties prop = new Properties();
        prop.setProperty(address_name, address);

        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
    }

    public static String loadAddr(String address_name) throws Exception {
        // load Asset contact address from contract.properties
        //Properties prop = new Properties();
        final Resource contractResource = new ClassPathResource("contract.properties");
        prop.load(contractResource.getInputStream());

        String contractAddress = prop.getProperty(address_name);
        if (contractAddress == null || contractAddress.trim().equals("")) {
            throw new Exception(" load Asset contract address failed, please deploy it first. ");
        }
        logger.info(" load Asset address from contract.properties, address is {}", contractAddress);
        return contractAddress;
    }
}

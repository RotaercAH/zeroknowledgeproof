package org.fisco.bcos.asset.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class EvidenceRepository extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610903806100536000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806354f6127f146100725780639c52a7f114610141578063aa4bf6d214610184578063b2bdfa7b14610222578063ff9913e814610279575b600080fd5b34801561007e57600080fd5b506100a160048036038101908080356000191690602001909291905050506102bc565b6040518085600019166000191681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019060200280838360005b8381101561012a57808201518184015260208101905061010f565b505050509050019550505050505060405180910390f35b34801561014d57600080fd5b50610182600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103fe565b005b34801561019057600080fd5b506102206004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919291929050505061051d565b005b34801561022e57600080fd5b5061023761071c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561028557600080fd5b506102ba600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610741565b005b60008060006060600060026000876000191660001916815260200190815260200160002090508560001916816000015460001916141515610365576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f45766964656e6365206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b80600001548160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16826002015483600301808054806020026020016040519081016040528092919081815260200182805480156103e757602002820191906000526020600020905b815460001916815260200190600101908083116103cf575b505050505090509450945094509450509193509193565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104c2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806105c8575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b151561063c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b8360026000866000191660001916815260200190815260200160002060000181600019169055508260026000866000191660001916815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816002600086600019166000191681526020019081526020016000206002018190555080600260008660001916600019168152602001908152602001600020600301908051906020019061071592919061085f565b5050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610805576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b8280548282559060005260206000209081019282156108a1579160200282015b828111156108a057825182906000191690559160200191906001019061087f565b5b5090506108ae91906108b2565b5090565b6108d491905b808211156108d05760008160009055506001016108b8565b5090565b905600a165627a7a723058205d292f82170e1800244b3e411d6ccb2f30cbb266f76532903b2f1842a69e9cf90029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610903806100536000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806328e914891461007257806356bea000146100c9578063cad1a4691461010c578063d134eb03146101db578063da89dd3814610279575b600080fd5b34801561007e57600080fd5b506100876102bc565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100d557600080fd5b5061010a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102e1565b005b34801561011857600080fd5b5061013b6004803603810190808035600019169060200190929190505050610400565b6040518085600019166000191681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019060200280838360005b838110156101c45780820151818401526020810190506101a9565b505050509050019550505050505060405180910390f35b3480156101e757600080fd5b506102776004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610542565b005b34801561028557600080fd5b506102ba600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610741565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156103a5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b600080600060606000600260008760001916600019168152602001908152602001600020905085600019168160000154600019161415156104a9576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f45766964656e6365206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b80600001548160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168260020154836003018080548060200260200160405190810160405280929190818152602001828054801561052b57602002820191906000526020600020905b81546000191681526020019060010190808311610513575b505050505090509450945094509450509193509193565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806105ed575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b1515610661576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b8360026000866000191660001916815260200190815260200160002060000181600019169055508260026000866000191660001916815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816002600086600019166000191681526020019081526020016000206002018190555080600260008660001916600019168152602001908152602001600020600301908051906020019061073a92919061085f565b5050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610805576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b8280548282559060005260206000209081019282156108a1579160200282015b828111156108a057825182906000191690559160200191906001019061087f565b5b5090506108ae91906108b2565b5090565b6108d491905b808211156108d05760008160009055506001016108b8565b5090565b905600a165627a7a7230582067ce094c8250d089b29ef069bf4ece17a19eb70adc27d2f05d58d382e7f002cb0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"getData\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"bytes32[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"deny\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"timestamp\",\"type\":\"uint256\"},{\"name\":\"ext\",\"type\":\"bytes32[]\"}],\"name\":\"setData\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"allow\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETDATA = "getData";

    public static final String FUNC_DENY = "deny";

    public static final String FUNC_SETDATA = "setData";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_ALLOW = "allow";

    protected EvidenceRepository(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public Tuple4<byte[], String, BigInteger, List<byte[]>> getData(byte[] hash) throws ContractException {
        final Function function = new Function(FUNC_GETDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<byte[], String, BigInteger, List<byte[]>>(
                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                convertToNative((List<Bytes32>) results.get(3).getValue()));
    }

    public TransactionReceipt deny(String addr) {
        final Function function = new Function(
                FUNC_DENY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] deny(String addr, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DENY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForDeny(String addr) {
        final Function function = new Function(
                FUNC_DENY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getDenyInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_DENY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt setData(byte[] hash, String owner, BigInteger timestamp, List<byte[]> ext) {
        final Function function = new Function(
                FUNC_SETDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(timestamp), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] setData(byte[] hash, String owner, BigInteger timestamp, List<byte[]> ext, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(timestamp), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetData(byte[] hash, String owner, BigInteger timestamp, List<byte[]> ext) {
        final Function function = new Function(
                FUNC_SETDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(timestamp), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<byte[], String, BigInteger, List<byte[]>> getSetDataInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETDATA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<byte[], String, BigInteger, List<byte[]>>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                convertToNative((List<Bytes32>) results.get(3).getValue())
                );
    }

    public String _owner() throws ContractException {
        final Function function = new Function(FUNC__OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt allow(String addr) {
        final Function function = new Function(
                FUNC_ALLOW, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] allow(String addr, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ALLOW, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAllow(String addr) {
        final Function function = new Function(
                FUNC_ALLOW, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getAllowInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ALLOW, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public static EvidenceRepository load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new EvidenceRepository(contractAddress, client, credential);
    }

    public static EvidenceRepository deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(EvidenceRepository.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}

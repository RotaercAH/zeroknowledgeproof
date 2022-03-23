package org.fisco.bcos.asset.contract;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
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
public class Request extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040526000600260006101000a81548160ff021916908315150217905550336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610c3b8061006e6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631e95c2ce146100885780632a00a472146100b95780633b7711381461018c5780634fad18f1146101bd5780639c52a7f114610251578063b2bdfa7b14610294578063ff9913e8146102eb575b600080fd5b34801561009457600080fd5b506100b7600480360381019080803560001916906020019092919050505061032e565b005b3480156100c557600080fd5b506100e8600480360381019080803560001916906020019092919050505061059a565b604051808560001916600019168152602001806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183151515158152602001828103825285818151815260200191508051906020019060200280838360005b8381101561017557808201518184015260208101905061015a565b505050509050019550505050505060405180910390f35b34801561019857600080fd5b506101bb60048036038101908080356000191690602001909291905050506106ff565b005b3480156101c957600080fd5b5061024f600480360381019080803560001916906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610739565b005b34801561025d57600080fd5b50610292600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610914565b005b3480156102a057600080fd5b506102a9610a33565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156102f757600080fd5b5061032c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a58565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806103d9575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b151561044d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b806000191660036000836000191660001916815260200190815260200160002060000154600019161415156104ea576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b600060036000836000191660001916815260200190815260200160002060020160146101000a81548160ff021916908315150217905515610597576003600082600019166000191681526020019081526020016000206000808201600090556001820160006105599190610b76565b6002820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556002820160146101000a81549060ff021916905550505b50565b60006060600080600060036000876000191660001916815260200190815260200160002090508560001916600360008860001916600019168152602001908152602001600020600001546000191614151561065d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b85816001018260020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360020160149054906101000a900460ff16828054806020026020016040519081016040528092919081815260200182805480156106e857602002820191906000526020600020905b815460001916815260200190600101908083116106d0575b505050505092509450945094509450509193509193565b600160036000836000191660001916815260200190815260200160002060020160146101000a81548160ff02191690831515021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806107e4575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b1515610858576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b8260036000856000191660001916815260200190815260200160002060000181600019169055508160036000856000191660001916815260200190815260200160002060010190805190602001906108b1929190610b97565b508060036000856000191660001916815260200190815260200160002060020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156109d8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b1c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b5080546000825590600052602060002090810190610b949190610bea565b50565b828054828255906000526020600020908101928215610bd9579160200282015b82811115610bd8578251829060001916905591602001919060010190610bb7565b5b509050610be69190610bea565b5090565b610c0c91905b80821115610c08576000816000905550600101610bf0565b5090565b905600a165627a7a72305820b5e94f0d7d6ab559e6ae36ddb813c0f307617fc94ddd32b6a4215ef36932d4400029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040526000600260006101000a81548160ff021916908315150217905550336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610c3b8061006e6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806328e914891461008857806356bea000146100df578063766ecc04146101225780638f5c1106146101f557806393878eed14610226578063bb45f36f146102ba578063da89dd38146102eb575b600080fd5b34801561009457600080fd5b5061009d61032e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100eb57600080fd5b50610120600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610353565b005b34801561012e57600080fd5b506101516004803603810190808035600019169060200190929190505050610472565b604051808560001916600019168152602001806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183151515158152602001828103825285818151815260200191508051906020019060200280838360005b838110156101de5780820151818401526020810190506101c3565b505050509050019550505050505060405180910390f35b34801561020157600080fd5b5061022460048036038101908080356000191690602001909291905050506105d7565b005b34801561023257600080fd5b506102b8600480360381019080803560001916906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610843565b005b3480156102c657600080fd5b506102e96004803603810190808035600019169060200190929190505050610a1e565b005b3480156102f757600080fd5b5061032c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a58565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610417576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b600060606000806000600360008760001916600019168152602001908152602001600020905085600019166003600088600019166000191681526020019081526020016000206000015460001916141515610535576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b85816001018260020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360020160149054906101000a900460ff16828054806020026020016040519081016040528092919081815260200182805480156105c057602002820191906000526020600020905b815460001916815260200190600101908083116105a8575b505050505092509450945094509450509193509193565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610682575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b15156106f6576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b80600019166003600083600019166000191681526020019081526020016000206000015460001916141515610793576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b600060036000836000191660001916815260200190815260200160002060020160146101000a81548160ff021916908315150217905515610840576003600082600019166000191681526020019081526020016000206000808201600090556001820160006108029190610b76565b6002820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556002820160146101000a81549060ff021916905550505b50565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806108ee575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b1515610962576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b8260036000856000191660001916815260200190815260200160002060000181600019169055508160036000856000191660001916815260200190815260200160002060010190805190602001906109bb929190610b97565b508060036000856000191660001916815260200190815260200160002060020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b600160036000836000191660001916815260200190815260200160002060020160146101000a81548160ff02191690831515021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b1c576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b5080546000825590600052602060002090810190610b949190610bea565b50565b828054828255906000526020600020908101928215610bd9579160200282015b82811115610bd8578251829060001916905591602001919060010190610bb7565b5b509050610be69190610bea565b5090565b610c0c91905b80821115610c08576000816000905550600101610bf0565b5090565b905600a165627a7a7230582055f1cec5c3803e5a89a3e205f85da3bafb5f1b815a4294196e72b753a01cf5470029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"deleteSaveRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"getRequestData\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"},{\"name\":\"SIG\",\"type\":\"bytes32[]\"},{\"name\":\"creator\",\"type\":\"address\"},{\"name\":\"passed\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"validateSaveRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"},{\"name\":\"SIG\",\"type\":\"bytes32[]\"},{\"name\":\"creator\",\"type\":\"address\"}],\"name\":\"createSaveRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"deny\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"allow\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_DELETESAVEREQUEST = "deleteSaveRequest";

    public static final String FUNC_GETREQUESTDATA = "getRequestData";

    public static final String FUNC_VALIDATESAVEREQUEST = "validateSaveRequest";

    public static final String FUNC_CREATESAVEREQUEST = "createSaveRequest";

    public static final String FUNC_DENY = "deny";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_ALLOW = "allow";

    protected Request(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt deleteSaveRequest(byte[] hash) {
        final Function function = new Function(
                FUNC_DELETESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] deleteSaveRequest(byte[] hash, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DELETESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForDeleteSaveRequest(byte[] hash) {
        final Function function = new Function(
                FUNC_DELETESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getDeleteSaveRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_DELETESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public Tuple4<byte[], List<byte[]>, String, Boolean> getRequestData(byte[] hash) throws ContractException {
        final Function function = new Function(FUNC_GETREQUESTDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<byte[], List<byte[]>, String, Boolean>(
                (byte[]) results.get(0).getValue(), 
                convertToNative((List<Bytes32>) results.get(1).getValue()), 
                (String) results.get(2).getValue(), 
                (Boolean) results.get(3).getValue());
    }

    public TransactionReceipt validateSaveRequest(byte[] hash) {
        final Function function = new Function(
                FUNC_VALIDATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] validateSaveRequest(byte[] hash, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_VALIDATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForValidateSaveRequest(byte[] hash) {
        final Function function = new Function(
                FUNC_VALIDATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getValidateSaveRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VALIDATESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public TransactionReceipt createSaveRequest(byte[] hash, List<byte[]> SIG, String creator) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                SIG.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(SIG, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class)), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(creator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] createSaveRequest(byte[] hash, List<byte[]> SIG, String creator, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                SIG.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(SIG, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class)), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(creator)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreateSaveRequest(byte[] hash, List<byte[]> SIG, String creator) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                SIG.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(SIG, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class)), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(creator)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<byte[], List<byte[]>, String> getCreateSaveRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<byte[], List<byte[]>, String>(

                (byte[]) results.get(0).getValue(), 
                convertToNative((List<Bytes32>) results.get(1).getValue()), 
                (String) results.get(2).getValue()
                );
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

    public static Request load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Request(contractAddress, client, credential);
    }

    public static Request deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Request.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}

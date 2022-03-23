package org.fisco.bcos.asset.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class RequestRepository extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405161124a38038061124a83398101806040528101908080519060200190929190805182019291905050506000336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600260006101000a81548160ff021916908360ff160217905550600090505b81518110156101245760016004600084848151811015156100bc57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550808060010191505061009f565b505050611114806101366000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631e95c2ce146100935780632a00a472146100c45780636aa73a1d146101a65780637f3c81601461023a57806398ced6981461026b5780639c52a7f1146102d4578063b2bdfa7b14610317578063ff9913e81461036e575b600080fd5b34801561009f57600080fd5b506100c260048036038101908080356000191690602001909291905050506103b1565b005b3480156100d057600080fd5b506100f360048036038101908080356000191690602001909291905050506105e1565b6040518086600019166000191681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018460ff1660ff1681526020018360ff1660ff168152602001828103825285818151815260200191508051906020019060200280838360005b8381101561018e578082015181840152602081019050610173565b50505050905001965050505050505060405180910390f35b3480156101b257600080fd5b506102386004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919291929050505061075b565b005b34801561024657600080fd5b5061024f6109d3565b604051808260ff1660ff16815260200191505060405180910390f35b34801561027757600080fd5b506102ba6004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506109e6565b604051808215151515815260200191505060405180910390f35b3480156102e057600080fd5b50610315600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610ded565b005b34801561032357600080fd5b5061032c610f0c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561037a57600080fd5b506103af600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f31565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061045c575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b15156104d0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b8060001916600360008360001916600019168152602001908152602001600020600001546000191614151561056d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b6003600082600019166000191681526020019081526020016000206000808201600090556001820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556001820160146101000a81549060ff02191690556002820160006105dc919061104f565b505050565b600080606060008060006003600088600019166000191681526020019081526020016000209050866000191660036000896000191660001916815260200190815260200160002060000154600019161415156106a5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b868160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16826002018360010160149054906101000a900460ff16600260009054906101000a900460ff168280548060200260200160405190810160405280929190818152602001828054801561074057602002820191906000526020600020905b81546000191681526020019060010190808311610728575b50505050509250955095509550955095505091939590929450565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610806575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b151561087a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b60006001026003600085600019166000191681526020019081526020016000206000015460001916141515610917576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f7265717565737420616c7265616479206578697374656400000000000000000081525060200191505060405180910390fd5b8260036000856000191660001916815260200190815260200160002060000181600019169055508160036000856000191660001916815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508060036000856000191660001916815260200190815260200160002060020190805190602001906109cd929190611070565b50505050565b600260009054906101000a900460ff1681565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610a94575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b1515610b08576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b60011515600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515141515610bd0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f4e6f7420616c6c6f77656420746f20766f74650000000000000000000000000081525060200191505060405180910390fd5b83600019166003600086600019166000191681526020019081526020016000206000015460001916141515610c6d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b6003600085600019166000191681526020019081526020016000209050600015158160030160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515141515610d54576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f566f74657220616c726561647920766f7465640000000000000000000000000081525060200191505060405180910390fd5b60018160030160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555080600101601481819054906101000a900460ff168092919060010191906101000a81548160ff021916908360ff16021790555050600191505092915050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610eb1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffff","ffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610ff5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b508054600082559060005260206000209081019061106d91906110c3565b50565b8280548282559060005260206000209081019282156110b2579160200282015b828111156110b1578251829060001916905591602001919060010190611090565b5b5090506110bf91906110c3565b5090565b6110e591905b808211156110e15760008160009055506001016110c9565b5090565b905600a165627a7a72305820b97cd7138322f7f47bdde4b10d8d31e26df1b0434e8b81aa16d6985ec1e74fd20029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405161124a38038061124a83398101806040528101908080519060200190929190805182019291905050506000336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600260006101000a81548160ff021916908360ff160217905550600090505b81518110156101245760016004600084848151811015156100bc57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550808060010191505061009f565b505050611114806101366000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630ada1c941461009357806328e914891461012757806356bea0001461017e57806365425e2e146101c1578063766ecc041461022a5780638f2160971461030c5780638f5c11061461033d578063da89dd381461036e575b600080fd5b34801561009f57600080fd5b506101256004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506103b1565b005b34801561013357600080fd5b5061013c610629565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561018a57600080fd5b506101bf600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061064e565b005b3480156101cd57600080fd5b506102106004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061076d565b604051808215151515815260200191505060405180910390f35b34801561023657600080fd5b506102596004803603810190808035600019169060200190929190505050610b74565b6040518086600019166000191681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018460ff1660ff1681526020018360ff1660ff168152602001828103825285818151815260200191508051906020019060200280838360005b838110156102f45780820151818401526020810190506102d9565b50505050905001965050505050505060405180910390f35b34801561031857600080fd5b50610321610cee565b604051808260ff1660ff16815260200191505060405180910390f35b34801561034957600080fd5b5061036c6004803603810190808035600019169060200190929190505050610d01565b005b34801561037a57600080fd5b506103af600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f31565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061045c575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b15156104d0576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b6000600102600360008560001916600019168152602001908152602001600020600001546000191614151561056d576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f7265717565737420616c7265616479206578697374656400000000000000000081525060200191505060405180910390fd5b8260036000856000191660001916815260200190815260200160002060000181600019169055508160036000856000191660001916815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600360008560001916600019168152602001908152602001600020600201908051906020019061062392919061104f565b50505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610712576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061081b575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b151561088f576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b60011515600460008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515141515610957576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f4e6f7420616c6c6f77656420746f20766f74650000000000000000000000000081525060200191505060405180910390fd5b836000191660036000866000191660001916815260200190815260200160002060000154600019161415156109f4576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b6003600085600019166000191681526020019081526020016000209050600015158160030160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515141515610adb576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f566f74657220616c726561647920766f7465640000000000000000000000000081525060200191505060405180910390fd5b60018160030160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555080600101601481819054906101000a900460ff168092919060010191906101000a81548160ff021916908360ff16021790555050600191505092915050565b60008060606000806000600360008860001916600019168152602001908152602001600020905086600019166003600089600019166000191681526020019081526020016000206000015460001916141515610c38576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b868160010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16826002018360010160149054906101000a900460ff16600260009054906101000a900460ff1682805480602002602001604051908101604052809291908181526020018280548015610cd357602002820191906000526020600020905b81546000191681526020019060010190808311610cbb575b50505050509250955095509550955095505091939590929450565b600260009054906101000a900460ff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610dac575060011515600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515145b1515610e20576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f4e6f742061757468656e7469636174656400000000000000000000000000000081525060200191505060405180910390fd5b80600019166003600083600019166000191681526020019081526020016000206000015460001916141515610ebd576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f72657175657374206e6f7420666f756e6400000000000000000000000000000081525060200191505060405180910390fd5b600360008260001916600019","1681526020019081526020016000206000808201600090556001820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556001820160146101000a81549060ff0219169055600282016000610f2c91906110a2565b505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610ff5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f4e6f742061646d696e000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b828054828255906000526020600020908101928215611091579160200282015b8281111561109057825182906000191690559160200191906001019061106f565b5b50905061109e91906110c3565b5090565b50805460008255906000526020600020908101906110c091906110c3565b50565b6110e591905b808211156110e15760008160009055506001016110c9565b5090565b905600a165627a7a72305820d295e8a032131bf7a8cd94690bdf8a7afb9fd0a05f240e2f45fd779af38fcc5f0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"deleteSaveRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"}],\"name\":\"getRequestData\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"},{\"name\":\"creator\",\"type\":\"address\"},{\"name\":\"ext\",\"type\":\"bytes32[]\"},{\"name\":\"voted\",\"type\":\"uint8\"},{\"name\":\"threshold\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"ext\",\"type\":\"bytes32[]\"}],\"name\":\"createSaveRequest\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_threshold\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"bytes32\"},{\"name\":\"voter\",\"type\":\"address\"}],\"name\":\"voteSaveRequest\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"deny\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"allow\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"threshold\",\"type\":\"uint8\"},{\"name\":\"voterArray\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_DELETESAVEREQUEST = "deleteSaveRequest";

    public static final String FUNC_GETREQUESTDATA = "getRequestData";

    public static final String FUNC_CREATESAVEREQUEST = "createSaveRequest";

    public static final String FUNC__THRESHOLD = "_threshold";

    public static final String FUNC_VOTESAVEREQUEST = "voteSaveRequest";

    public static final String FUNC_DENY = "deny";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_ALLOW = "allow";

    protected RequestRepository(String contractAddress, Client client, CryptoKeyPair credential) {
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

    public Tuple5<byte[], String, List<byte[]>, BigInteger, BigInteger> getRequestData(byte[] hash) throws ContractException {
        final Function function = new Function(FUNC_GETREQUESTDATA, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<DynamicArray<Bytes32>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint8>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<byte[], String, List<byte[]>, BigInteger, BigInteger>(
                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                convertToNative((List<Bytes32>) results.get(2).getValue()), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue());
    }

    public TransactionReceipt createSaveRequest(byte[] hash, String owner, List<byte[]> ext) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] createSaveRequest(byte[] hash, String owner, List<byte[]> ext, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreateSaveRequest(byte[] hash, String owner, List<byte[]> ext) {
        final Function function = new Function(
                FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(owner), 
                ext.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(ext, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<byte[], String, List<byte[]>> getCreateSaveRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<byte[], String, List<byte[]>>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                convertToNative((List<Bytes32>) results.get(2).getValue())
                );
    }

    public BigInteger _threshold() throws ContractException {
        final Function function = new Function(FUNC__THRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt voteSaveRequest(byte[] hash, String voter) {
        final Function function = new Function(
                FUNC_VOTESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(voter)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] voteSaveRequest(byte[] hash, String voter, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_VOTESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(voter)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForVoteSaveRequest(byte[] hash, String voter) {
        final Function function = new Function(
                FUNC_VOTESAVEREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(voter)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<byte[], String> getVoteSaveRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VOTESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<byte[], String>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<Boolean> getVoteSaveRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_VOTESAVEREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
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

    public static RequestRepository load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new RequestRepository(contractAddress, client, credential);
    }

    public static RequestRepository deploy(Client client, CryptoKeyPair credential, BigInteger threshold, List<String> voterArray) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(threshold), 
                voterArray.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.Address>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(voterArray, org.fisco.bcos.sdk.abi.datatypes.Address.class))));
        return deploy(RequestRepository.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }
}

package org.fisco.bcos.asset.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class RewardPointController extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061196a83398101806040528101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506118a6806100c46000396000f3006080604052600436106100ba576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806313af4035146100bf5780631aa3a0081461010257806320694db01461015957806346871a31146101b45780635f9ca9ff1461021a578063877b9a67146102b8578063938050e114610313578063b2bdfa7b14610342578063c3c5a54714610399578063cd5d2118146103f4578063e3d670d71461044f578063e79a198f146104e7575b600080fd5b3480156100cb57600080fd5b50610100600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061053e565b005b34801561010e57600080fd5b506101176105fe565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561016557600080fd5b5061019a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a25565b604051808215151515815260200191505060405180910390f35b3480156101c057600080fd5b5061021860048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610b05565b005b34801561022657600080fd5b5061029e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610b1f565b604051808215151515815260200191505060405180910390f35b3480156102c457600080fd5b506102f9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610ec2565b604051808215151515815260200191505060405180910390f35b34801561031f57600080fd5b50610328610fc3565b604051808215151515815260200191505060405180910390f35b34801561034e57600080fd5b506103576110a1565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156103a557600080fd5b506103da600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110c6565b604051808215151515815260200191505060405180910390f35b34801561040057600080fd5b50610435600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506111c7565b604051808215151515815260200191505060405180910390f35b34801561045b57600080fd5b50610490600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061126e565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156104d35780820151818401526020810190506104b8565b505050509050019250505060405180910390f35b3480156104f357600080fd5b506104fc6113c5565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610547336111c7565b15156105bb576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4f6e6c79206f776e65722100000000000000000000000000000000000000000081525060200191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60003360001515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c8e40fbf836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156106c257600080fd5b505af11580156106d6573d6000803e3d6000fd5b505050506040513d60208110156106ec57600080fd5b81019080805190602001909291905050501515141515610774576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4163636f756e7420616c7265616479206578697374656421000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663e5c96aa43360016040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018215151515815260200192505050602060405180830381600087803b15801561083e57600080fd5b505af1158015610852573d6000803e3d6000fd5b505050506040513d602081101561086857600080fd5b810190808051906020019092919050505050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce321bc53360026040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818154815260200191508054801561096157602002820191906000526020600020905b81546000191681526020019060010190808311610949575b50509350505050602060405180830381600087803b15801561098257600080fd5b505af1158015610996573d6000803e3d6000fd5b505050506040513d60208110156109ac57600080fd5b8101908080519060200190929190505050507f68f0f1b8c9e91743a8bf9b77af08d152d1cb9ac31eb5b472bf6e16467254885533604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a15090565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166320694db0836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b158015610ae457600080fd5b505af1158015610af8573d6000803e3d6000fd5b5050505060019050919050565b8060029080519060200190610b1b929190611802565b5050565b60008260011515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c8e40fbf836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015610be357600080fd5b505af1158015610bf7573d6000803e3d6000fd5b505050506040513d6020811015610c0d57600080fd5b81019080805190602001909291905050501515148015610c5a5750600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614155b1515610cce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f4f6e6c792065786973746564206163636f756e7421000000000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce321bc585856040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019060200280838360005b83811015610dae578082015181840152602081019050610d93565b505050509050019350505050602060405180830381600087803b158015610dd457600080fd5b505af1158015610de8573d6000803e3d6000fd5b505050506040513d6020811015610dfe57600080fd5b8101908080519060200190929190505050508373ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f2af8b5101cfa1eab2e38376b76275c9f62be27e29df4107a9360ed0e3fa132d1856040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610ea4578082015181840152602081019050610e89565b505050509050019250505060405180910390a3600191505092915050565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663877b9a67836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffff","ffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015610f8157600080fd5b505af1158015610f95573d6000803e3d6000fd5b505050506040513d6020811015610fab57600080fd5b81019080805190602001909291905050509050919050565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c510a9a8336040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15801561108257600080fd5b505af1158015611096573d6000803e3d6000fd5b505050506001905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c8e40fbf836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561118557600080fd5b505af1158015611199573d6000803e3d6000fd5b505050506040513d60208110156111af57600080fd5b81019080805190602001909291905050509050919050565b60003073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156112065760019050611269565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156112645760019050611269565b600090505b919050565b6060600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f8b2cb4f836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15801561132d57600080fd5b505af1158015611341573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561136b57600080fd5b81019080805164010000000081111561138357600080fd5b8281019050602081018481111561139957600080fd5b81518560208202830111640100000000821117156113b657600080fd5b50509291905050509050919050565b60003360011515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c8e40fbf836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561148957600080fd5b505af115801561149d573d6000803e3d6000fd5b505050506040513d60208110156114b357600080fd5b8101908080519060200190929190505050151514801561162157506000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f8b2cb4f836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15801561158d57600080fd5b505af11580156115a1573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060208110156115cb57600080fd5b8101908080516401000000008111156115e357600080fd5b828101905060208101848111156115f957600080fd5b815185602082028301116401000000008211171561161657600080fd5b505092919050505051145b1515611695576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f43616e6e277420756e726567697374657221000000000000000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663e5c96aa43360006040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018215151515815260200192505050602060405180830381600087803b15801561175f57600080fd5b505af1158015611773573d6000803e3d6000fd5b505050506040513d602081101561178957600080fd5b8101908080519060200190929190505050507f11854d1b3c0aa24c7c879af700c0089a48a48e9280bac11f5370b90b7cca481c33604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a15090565b828054828255906000526020600020908101928215611844579160200282015b82811115611843578251829060001916905591602001919060010190611822565b5b5090506118519190611855565b5090565b61187791905b8082111561187357600081600090555060010161185b565b5090565b905600a165627a7a72305820b831cd86566afaab92e6dd0382803f2008937e79edd5b0f0bfc6927bb350fe370029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061196a83398101806040528101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506118a6806100c46000396000f3006080604052600436106100ba576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806302afb286146100bf57806305282c701461011a57806310aa94c11461015d5780632392c068146101b857806328e91489146102135780635413d2601461026a578063608f2d2d14610308578063669dfcbd1461036e5780636e0376d4146103c55780639d4834e514610420578063a94759d8146104b8578063b24457bc1461050f575b600080fd5b3480156100cb57600080fd5b50610100600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061053e565b604051808215151515815260200191505060405180910390f35b34801561012657600080fd5b5061015b600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061063f565b005b34801561016957600080fd5b5061019e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506106ff565b604051808215151515815260200191505060405180910390f35b3480156101c457600080fd5b506101f9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610800565b604051808215151515815260200191505060405180910390f35b34801561021f57600080fd5b506102286108e0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561027657600080fd5b506102ee600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610905565b604051808215151515815260200191505060405180910390f35b34801561031457600080fd5b5061036c60048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610ca8565b005b34801561037a57600080fd5b50610383610cc2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156103d157600080fd5b50610406600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110e9565b604051808215151515815260200191505060405180910390f35b34801561042c57600080fd5b50610461600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611190565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156104a4578082015181840152602081019050610489565b505050509050019250505060405180910390f35b3480156104c457600080fd5b506104cd6112e7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561051b57600080fd5b50610524611724565b604051808215151515815260200191505060405180910390f35b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302afb286836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156105fd57600080fd5b505af1158015610611573d6000803e3d6000fd5b505050506040513d602081101561062757600080fd5b81019080805190602001909291905050509050919050565b610648336110e9565b15156106bc576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4f6e6c79206f776e65722100000000000000000000000000000000000000000081525060200191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e4ad09b836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156107be57600080fd5b505af11580156107d2573d6000803e3d6000fd5b505050506040513d60208110156107e857600080fd5b81019080805190602001909291905050509050919050565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632392c068836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b1580156108bf57600080fd5b505af11580156108d3573d6000803e3d6000fd5b5050505060019050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008260011515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e4ad09b836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156109c957600080fd5b505af11580156109dd573d6000803e3d6000fd5b505050506040513d60208110156109f357600080fd5b81019080805190602001909291905050501515148015610a405750600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614155b1515610ab4576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f4f6e6c792065786973746564206163636f756e7421000000000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fdf71de285856040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019060200280838360005b83811015610b94578082015181840152602081019050610b79565b505050509050019350505050602060405180830381600087803b158015610bba57600080fd5b505af1158015610bce573d6000803e3d6000fd5b505050506040513d6020811015610be457600080fd5b8101908080519060200190929190505050508373ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fc80050d3d8fe3d8477bab584a90dbd7798be94afe5881ce2f83310bd05e52336856040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610c8a578082015181840152602081019050610c6f565b505050509050019250505060405180910390a3600191505092915050565b8060029080519060200190610cbe929190611802565b5050565b60003360001515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e4ad09b836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015610d8657600080fd5b505af1158015610d9a573d6000803e3d6000fd5b505050506040513d6020811015610db057600080fd5b81019080805190602001909291905050501515141515610e38576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f4163636f756e7420616c7265616479206578697374656421000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166354ca8d093360016040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018215151515815260200192505050602060405180830381600087803b158015610f0257600080fd5b505af1158015610f16573d6000803e3d6000fd5b505050506040513d6020811015610f2c57600080fd5b810190808051906020019092919050","505050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fdf71de23360026040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818154815260200191508054801561102557602002820191906000526020600020905b8154600019168152602001906001019080831161100d575b50509350505050602060405180830381600087803b15801561104657600080fd5b505af115801561105a573d6000803e3d6000fd5b505050506040513d602081101561107057600080fd5b8101908080519060200190929190505050507ff05d133a1414b420c163205e98293656fca18d03a4fec96bc01bfb66b55223c433604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a15090565b60003073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611128576001905061118b565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611186576001905061118b565b600090505b919050565b6060600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633f1baf84836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15801561124f57600080fd5b505af1158015611263573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561128d57600080fd5b8101908080516401000000008111156112a557600080fd5b828101905060208101848111156112bb57600080fd5b81518560208202830111640100000000821117156112d857600080fd5b50509291905050509050919050565b60003360011515600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e4ad09b836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1580156113ab57600080fd5b505af11580156113bf573d6000803e3d6000fd5b505050506040513d60208110156113d557600080fd5b8101908080519060200190929190505050151514801561154357506000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633f1baf84836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b1580156114af57600080fd5b505af11580156114c3573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060208110156114ed57600080fd5b81019080805164010000000081111561150557600080fd5b8281019050602081018481111561151b57600080fd5b815185602082028301116401000000008211171561153857600080fd5b505092919050505051145b15156115b7576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f43616e6e277420756e726567697374657221000000000000000000000000000081525060200191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166354ca8d093360006040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018215151515815260200192505050602060405180830381600087803b15801561168157600080fd5b505af1158015611695573d6000803e3d6000fd5b505050506040513d60208110156116ab57600080fd5b8101908080519060200190929190505050507f5c09faf59c132a0070456f24a4ef0783f381bc98d9151bc2797d8dfbb8f2d66633604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a15090565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663be4f734b336040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b1580156117e357600080fd5b505af11580156117f7573d6000803e3d6000fd5b505050506001905090565b828054828255906000526020600020908101928215611844579160200282015b82811115611843578251829060001916905591602001919060010190611822565b5b5090506118519190611855565b5090565b61187791905b8082111561187357600081600090555060010161185b565b5090565b905600a165627a7a723058202f75eafc864a2b3a78ae074660a2019be4ee8e9a861e76bae4e46d7fa3d3dca00029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"register\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"addIssuer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"test\",\"type\":\"bytes32[]\"}],\"name\":\"getbytes\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"bytes32[]\"}],\"name\":\"issue\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"isIssuer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"renounceIssuer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"isRegistered\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"src\",\"type\":\"address\"}],\"name\":\"auth\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"balance\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"unregister\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"dataAddress\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"LogRegister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"LogUnregister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"bytes32[]\"}],\"name\":\"LogSend\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SETOWNER = "setOwner";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_ADDISSUER = "addIssuer";

    public static final String FUNC_GETBYTES = "getbytes";

    public static final String FUNC_ISSUE = "issue";

    public static final String FUNC_ISISSUER = "isIssuer";

    public static final String FUNC_RENOUNCEISSUER = "renounceIssuer";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_ISREGISTERED = "isRegistered";

    public static final String FUNC_AUTH = "auth";

    public static final String FUNC_BALANCE = "balance";

    public static final String FUNC_UNREGISTER = "unregister";

    public static final Event LOGREGISTER_EVENT = new Event("LogRegister",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event LOGUNREGISTER_EVENT = new Event("LogUnregister",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event LOGSEND_EVENT = new Event("LogSend",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<DynamicArray<Bytes32>>() {}));
    ;

    protected RewardPointController(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt setOwner(String owner) {
        final Function function = new Function(
                FUNC_SETOWNER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] setOwner(String owner, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETOWNER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetOwner(String owner) {
        final Function function = new Function(
                FUNC_SETOWNER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
        );
    }

    public TransactionReceipt register() {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] register(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRegister() {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getRegisterOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REGISTER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
        );
    }

    public TransactionReceipt addIssuer(String account) {
        final Function function = new Function(
                FUNC_ADDISSUER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] addIssuer(String account, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDISSUER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddIssuer(String account) {
        final Function function = new Function(
                FUNC_ADDISSUER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getAddIssuerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDISSUER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
        );
    }

    public Tuple1<Boolean> getAddIssuerOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDISSUER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
        );
    }

    public TransactionReceipt getbytes(List<byte[]> test) {
        final Function function = new Function(
                FUNC_GETBYTES,
                Arrays.<Type>asList(test.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(test, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] getbytes(List<byte[]> test, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETBYTES,
                Arrays.<Type>asList(test.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(test, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetbytes(List<byte[]> test) {
        final Function function = new Function(
                FUNC_GETBYTES,
                Arrays.<Type>asList(test.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(test, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<List<byte[]>> getGetbytesInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETBYTES,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<List<byte[]>>(

                convertToNative((List<Bytes32>) results.get(0).getValue())
        );
    }

    public TransactionReceipt issue(String account, List<byte[]> value) {
        final Function function = new Function(
                FUNC_ISSUE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account),
                        value.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(value, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] issue(String account, List<byte[]> value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ISSUE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account),
                        value.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(value, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForIssue(String account, List<byte[]> value) {
        final Function function = new Function(
                FUNC_ISSUE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account),
                        value.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes32[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(value, org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, List<byte[]>> getIssueInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ISSUE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, List<byte[]>>(

                (String) results.get(0).getValue(),
                convertToNative((List<Bytes32>) results.get(1).getValue())
        );
    }

    public Tuple1<Boolean> getIssueOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ISSUE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
        );
    }

    public Boolean isIssuer(String account) throws ContractException {
        final Function function = new Function(FUNC_ISISSUER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public TransactionReceipt renounceIssuer() {
        final Function function = new Function(
                FUNC_RENOUNCEISSUER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] renounceIssuer(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RENOUNCEISSUER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRenounceIssuer() {
        final Function function = new Function(
                FUNC_RENOUNCEISSUER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getRenounceIssuerOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_RENOUNCEISSUER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
        );
    }

    public String _owner() throws ContractException {
        final Function function = new Function(FUNC__OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Boolean isRegistered(String addr) throws ContractException {
        final Function function = new Function(FUNC_ISREGISTERED,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Boolean auth(String src) throws ContractException {
        final Function function = new Function(FUNC_AUTH,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(src)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public List balance(String addr) throws ContractException {
        final Function function = new Function(FUNC_BALANCE,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public TransactionReceipt unregister() {
        final Function function = new Function(
                FUNC_UNREGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] unregister(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UNREGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUnregister() {
        final Function function = new Function(
                FUNC_UNREGISTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getUnregisterOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UNREGISTER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
        );
    }

    public List<LogRegisterEventResponse> getLogRegisterEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGREGISTER_EVENT, transactionReceipt);
        ArrayList<LogRegisterEventResponse> responses = new ArrayList<LogRegisterEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogRegisterEventResponse typedResponse = new LogRegisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogRegisterEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGREGISTER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeLogRegisterEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGREGISTER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<LogUnregisterEventResponse> getLogUnregisterEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGUNREGISTER_EVENT, transactionReceipt);
        ArrayList<LogUnregisterEventResponse> responses = new ArrayList<LogUnregisterEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogUnregisterEventResponse typedResponse = new LogUnregisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogUnregisterEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGUNREGISTER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeLogUnregisterEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGUNREGISTER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<LogSendEventResponse> getLogSendEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGSEND_EVENT, transactionReceipt);
        ArrayList<LogSendEventResponse> responses = new ArrayList<LogSendEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogSendEventResponse typedResponse = new LogSendEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (List<byte[]>) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogSendEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSEND_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeLogSendEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSEND_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static RewardPointController load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new RewardPointController(contractAddress, client, credential);
    }

    public static RewardPointController deploy(Client client, CryptoKeyPair credential, String dataAddress) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(dataAddress)));
        return deploy(RewardPointController.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class LogRegisterEventResponse {
        public TransactionReceipt.Logs log;

        public String account;
    }

    public static class LogUnregisterEventResponse {
        public TransactionReceipt.Logs log;

        public String account;
    }

    public static class LogSendEventResponse {
        public TransactionReceipt.Logs log;

        public String from;

        public String to;

        public List<byte[]> value;
    }
}
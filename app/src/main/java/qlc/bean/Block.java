package qlc.bean;

public class Block {

	public static enum Type {
		State,
		Send,
		Receive,
		Change,
		Open,
		ContractReward,
		ContractSend,
		ContractRefund,
		ContractError,
		SmartContract,
		Invalid;
		public static String getIndex(String type) {
			switch (type.toLowerCase()) {
			case "state":
				return "0";
			case "send":
				return "1";
			case "receive":
				return "2";
			case "change":
				return "3";
			case "open":
				return "4";
			case "contractreward":
				return "5";
			case "contractsend":
				return "6";
			case "contractrefund":
				return "7";
			case "contracterror":
				return "8";
			case "smartcontract":
				return "9";
			default:
				return "10";
			}
		}
	};
}

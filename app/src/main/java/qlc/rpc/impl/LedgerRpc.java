package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;

public class LedgerRpc extends QlcRpc {

	public LedgerRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return number of blocks for a specific account
	 * @param params string : the account address
	 * @return int: blocks number for the account
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountBlocksCount(JSONArray params) throws IOException {
		return client.call("ledger_accountBlocksCount", params);
	}

	/**
	 * Return blocks for the account, include each token of the account and order of blocks is forward from the last one
	 * @param params string : the account address, int: number of blocks to return, int: optional , offset, index of block where to start, default is 0
	 * @return []block: blocks for the account
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountHistoryTopn(JSONArray params) throws IOException {
		return client.call("ledger_accountHistoryTopn", params);
	}

	/**
	 * Return account detail info, include each token in the account
	 * @param params string : the account address
	 * @return account : the account address
		coinBalance : balance of main token of the account (default is QLC)
		vote,network,storage,oracle: benefit for the account, if account don't have token QLC, these are omit
		representative : representative address of the account
		[]token: each token info for the account,
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountInfo(JSONArray params) throws IOException {
		return client.call("ledger_accountInfo", params);
	}

	/**
	 * Return the representative address for account
	 * @param params string : the account address
	 * @return string: representative address for the account (if account not found, return error)
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountRepresentative(JSONArray params) throws IOException {
		return client.call("ledger_accountRepresentative", params);
	}

	/**
	 * Return the vote weight for account
	 * @param params string :  the vote weight for the account (if account not found, return error)
	 * @return string: the vote weight for the account (if account not found, return error)
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountVotingWeight(JSONArray params) throws IOException {
		return client.call("ledger_accountVotingWeight", params);
	}


	/**
	 * Return account list of chain
	 * @param params int: number of accounts to return
	int: optional , offset, index of account where to start, default is 0
	 * @return []address: addresses list of accounts
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accounts(JSONArray params) throws IOException {
		return client.call("ledger_accounts", params);
	}

	/**
	 * Returns balance and pending (amount that has not yet been received) for each account
	 * @param params []string: addresses list
	 * @return balance and pending amount of each token for each account
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountsBalance(JSONArray params) throws IOException {
		return client.call("ledger_accountsBalance", params);
	}

	/**
	 * Return pairs of token name and block hash (representing the head block ) of each token for each account
	 * @param params []string: addresses list
	 * @return token name and block hash for each account
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountsFrontiers(JSONArray params) throws IOException {
		return client.call("ledger_accountsFrontiers", params);
	}

	/**
	 * Return pending info for accounts
	 * @param params []string: addresses list for accounts
	int: get the maximum number of pending for each account, if set -1, return all pending
	 * @return pending info for each token of each account, means:
	tokenName : token name
	type : token type
	source : sender account of transaction
	amount : amount of transaction
	hash : hash of send block
	timestamp: timestamp
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountsPending(JSONArray params) throws IOException {
		return client.call("ledger_accountsPending", params);
	}

	/**
	 * Return total number of accounts of chain
	 * @param params null
	 * @return: total number of accounts
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject accountsCount(JSONArray params) throws IOException {
		return client.call("ledger_accountsCount", params);
	}

	/**
	 * Return the account that the block belong to
	 * @param params string: block hash
	 * @return: string: the account address (if block not found, return error)
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blockAccount(JSONArray params) throws IOException {
		return client.call("ledger_blockAccount", params);
	}


	/**
	 * Return hash for the block
	 * @param params block: block info
	 * @return: string: block hash
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blockHash(JSONArray params) throws IOException {
		return client.call("ledger_blockHash", params);
	}


	/**
	 * Return blocks list of chain
	 * @param params int: number of blocks to return
	int: optional, offset, index of block where to start, default is 0
	 * @return: []block: blocks list
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blocks(JSONArray params) throws IOException {
		return client.call("ledger_blocks", params);
	}

	/**
	 * Return the number of blocks (include smartcontrant block) and unchecked blocks of chain
	 * @param params int: number of blocks to return
	int: optional, offset, index of block where to start, default is 0
	 * @return: number of blocks, means:
	count: int, number of blocks , include smartcontrant block
	unchecked: int, number of unchecked blocks
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blocksCount(JSONArray params) throws IOException {
		return client.call("ledger_blocksCount", params);
	}

	/**
	 * Report number of blocks by type of chain
	 * @param params null
	 * @return: number of blocks for each type
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blocksCountByType(JSONArray params) throws IOException {
		return client.call("ledger_blocksCountByType", params);
	}

	/**
	 * Return blocks info for blocks hash
	 * @param params []string: blocks hash
	 * @return: []block: blocks info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject blocksInfo(JSONArray params) throws IOException {
		return client.call("ledger_blocksInfo", params);
	}


	/**
	 * Accept a specific block hash and return a consecutive blocks hash list， starting with this block, and traverse forward to the maximum number
	 * @param params string : block hash to start at
	int: get the maximum number of blocks, if set n to -1, will list blocks to open block
	 * @return: []string: block hash list (if block not found, return error)
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject chain(JSONArray params) throws IOException {
		return client.call("ledger_chain", params);
	}

	/**
	 * Return a list of pairs of delegator and it's balance for a specific representative account
	 * @param params string: representative account address
	 * @return: each delegator and it's balance
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject delegators(JSONArray params) throws IOException {
		return client.call("ledger_delegators", params);
	}

	/**
	 * Return number of delegators for a specific representative account
	 * @param params string: representative account address
	 * @return: int: number of delegators for the account
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject delegatorsCount(JSONArray params) throws IOException {
		return client.call("ledger_delegatorsCount", params);
	}

	/**
	 * Return send block by send parameter and private key
	 * @param params send parameter for the block
	from: send address for the transaction
	to: receive address for the transaction
	tokenName: token name
	amount: transaction amount
	sender: optional, sms sender
	receiver: optional, sms receiver
	message: optional, sms message hash
	string: private key
	 * @return: block: send block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject generateSendBlock(JSONArray params) throws IOException {
		return client.call("ledger_generateSendBlock", params);
	}

	/**
	 * Return receive block by send block and private key
	 * @param params block: send block
	string: private key
	 * @return: block: receive block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject generateReceiveBlock(JSONArray params) throws IOException {
		return client.call("ledger_generateReceiveBlock", params);
	}


	/**
	 * Return change block by account and private key
	 * @param params string: account address
	string: new representative account
	string: private key
	 * @return: block: change block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject generateChangeBlock(JSONArray params) throws IOException {
		return client.call("ledger_generateChangeBlock", params);
	}

	/**
	 * Check block base info, update chain info for the block, and broadcast block
	 * @param params block: block
	 * @return: string: hash of the block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject process(JSONArray params) throws IOException {
		return client.call("ledger_process", params);
	}

	/**
	 * Return pairs of representative and its voting weight
	 * @param params bool , optional, if not set or set false, will return representatives randomly, if set true, will sorting represetntative balance in descending order
	 * @return: each representative and its voting weight
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject representatives(JSONArray params) throws IOException {
		return client.call("ledger_representatives", params);
	}

	/**
	 * Return tokens of the chain
	 * @param params null
	 * @return: []token: the tokens info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject tokens(JSONArray params) throws IOException {
		return client.call("ledger_tokens", params);
	}

	/**
	 * Return token info by token id
	 * @param params string: token id
	 * @return: token: token info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject tokenInfoById(JSONArray params) throws IOException {
		return client.call("ledger_tokenInfoById", params);
	}

	/**
	 * Return token info by token name
	 * @param params string: token name
	 * @return: token: token info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject tokenInfoByName(JSONArray params) throws IOException {
		return client.call("ledger_tokenInfoByName", params);
	}

	/**
	 * Return the number of blocks (not include smartcontrant block) and unchecked blocks of chain
	 * @param params null
	 * @return: count: int, number of blocks , not include smartcontrant block
	unchecked: int, number of unchecked blocks
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject transactionsCount(JSONArray params) throws IOException {
		return client.call("ledger_transactionsCount", params);
	}

}


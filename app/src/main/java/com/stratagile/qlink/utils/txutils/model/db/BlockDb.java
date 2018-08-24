package com.stratagile.qlink.utils.txutils.model.db;

import com.stratagile.qlink.utils.txutils.model.bytes.Fixed8;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.core.Block;
import com.stratagile.qlink.utils.txutils.model.core.CoinReference;
import com.stratagile.qlink.utils.txutils.model.core.Transaction;
import com.stratagile.qlink.utils.txutils.model.core.TransactionOutput;

import java.util.List;
import java.util.Map;


/**
 * the block database interface.
 *
 * @author coranos
 *
 */
public interface BlockDb {

	/**
	 * block force synch interval.
	 */
	int BLOCK_FORCE_SYNCH_INTERVAL = 50;

	/**
	 * close the database.
	 */
	void close();

	/**
	 * return true if the hash is in the database.
	 *
	 * @param hash
	 *            the hash to use.
	 * @return true if the hash is in the database.
	 */
	boolean containsBlockWithHash(UInt256 hash);

	/**
	 * deletes the highest block in the database.
	 */
	void deleteHighestBlock();

	/**
	 * return a map of account, assetid, and value for all accounts.
	 *
	 * @return a map of account, assetid, and value for all accounts.
	 */
	Map<UInt160, Map<UInt256, Fixed8>> getAccountAssetValueMap();

	/**
	 * returns the number of accounts in the database.
	 *
	 * @return the number of accounts in the database.
	 */
	long getAccountCount();

	/**
	 * return a map of assetid and value for the given account.
	 *
	 * @param account
	 *            the account to use.
	 *
	 * @return a map of assetid and value for the given account.
	 */
	Map<UInt256, Fixed8> getAssetValueMap(UInt160 account);

	/**
	 * return the block count.
	 *
	 * @return the block count.
	 */
	long getBlockCount();

	/**
	 * returns the index of the block that contains the given transaction.
	 *
	 * @param hash
	 *            the transaction hash to use.
	 *
	 * @return the block height, or null if the transaction does not exist.
	 */
	Long getBlockIndexFromTransactionHash(UInt256 hash);

	/**
	 * return the filze size of the database.
	 *
	 * @return the filze size of the database.
	 */
	long getFileSize();

	/**
	 * return the block with the given hash, with transactions attached.
	 *
	 * @param hash
	 *            the hash to use.
	 * @return the block with the given hash.
	 */
	Block getFullBlockFromHash(UInt256 hash);

	/**
	 * return the block at the given height, with transactions attached.
	 *
	 * @param blockHeight
	 *            the block height to use.
	 * @return the block at the given height.
	 */
	Block getFullBlockFromHeight(long blockHeight);

	/**
	 * return the block with the given hash, withount transactions attached.
	 *
	 * @param hash
	 *            the hash to use.
	 * @return the block with the given hash.
	 */
	Block getHeaderOfBlockFromHash(UInt256 hash);

	/**
	 * return the block at the given height, withount transactions attached.
	 *
	 * @param blockHeight
	 *            the block height to use.
	 * @return the block at the given height.
	 */
	Block getHeaderOfBlockFromHeight(long blockHeight);

	/**
	 * return the block with the highest index.
	 *
	 * @return the block with the highest index.
	 */
	Block getHeaderOfBlockWithMaxIndex();

	/**
	 * returns the list of transactions that output to the given address.
	 *
	 * @param account
	 *            the account.
	 * @return the list of transaction.
	 */
	List<Transaction> getTransactionWithAccountList(UInt160 account);

	/**
	 * return the transaction with the given hash.
	 *
	 * @param hash
	 *            the transaction hash to use.
	 * @return the transaction with the given hash.
	 */
	Transaction getTransactionWithHash(UInt256 hash);

	/**
	 * return a list of TransactionOutputs for the given account that have not been
	 * spent, by assetid.
	 *
	 * @param account
	 *            the account to use.
	 *
	 * @return a list of CoinReferences and amounts for the given account that have
	 *         not been spent, by assetid.
	 */
	Map<UInt256, Map<TransactionOutput, CoinReference>> getUnspentTransactionOutputListMap(UInt160 account);

	/**
	 * puts the given block into the database.
	 *
	 * @param forceSynch
	 *            if true, force all blocks to be written to disk.
	 * @param blocks
	 *            the blocks to use.
	 */
	void put(boolean forceSynch, Block... blocks);

	/**
	 * validates the database.
	 */
	void validate();
}

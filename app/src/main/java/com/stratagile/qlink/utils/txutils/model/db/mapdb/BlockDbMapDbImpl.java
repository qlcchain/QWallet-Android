//package com.stratagile.qlink.utils.txutils.model.db.mapdb;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.sql.SQLException;
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.ArrayUtils;
//import org.json.JSONObject;
//import org.mapdb.BTreeMap;
//import org.mapdb.DB;
//import org.mapdb.DBMaker;
//import org.mapdb.Serializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import neo.model.bytes.Fixed8;
//import neo.model.bytes.UInt16;
//import neo.model.bytes.UInt160;
//import neo.model.bytes.UInt256;
//import neo.model.core.Block;
//import neo.model.core.CoinReference;
//import neo.model.core.Transaction;
//import neo.model.core.TransactionOutput;
//import neo.model.db.BlockDb;
//import neo.model.util.ConfigurationUtil;
//import neo.model.util.GenesisBlockUtil;
//import neo.model.util.ModelUtil;
//import neo.model.util.NetworkUtil;
//
///**
// * the block database.
// *
// * @author coranos
// *
// */
//public final class BlockDbMapDbImpl implements BlockDb {
//
//	/**
//	 * the allocation increment size.
//	 */
//	private static final int ALLOCATION_INCREMENT_SIZE = 1024 * 1024;
//
//	/**
//	 * the logger.
//	 */
//	private static final Logger LOG = LoggerFactory.getLogger(BlockDbMapDbImpl.class);
//
//	/**
//	 * the block header primary index.
//	 */
//	private static final String BLOCK_HEADER_BY_INDEX = "blockHeaderByIndex";
//
//	/**
//	 * the block header primary index.
//	 */
//	private static final String BLOCK_INDEX_BY_HASH = "blockIndexByHash";
//
//	/**
//	 * transaction keys by block index.
//	 */
//	private static final String TRANSACTION_KEYS_BY_BLOCK_INDEX = "transactionKeysByBlockIndex";
//
//	/**
//	 * transaction key by transaction hash.
//	 */
//	private static final String TRANSACTION_KEY_BY_HASH = "transactionKeyByHash";
//
//	/**
//	 * transaction by keys.
//	 */
//	private static final String TRANSACTION_BY_KEY = "transactionByKey";
//
//	/**
//	 * the max block index.
//	 */
//	private static final String ASSET_AND_VALUE_BY_ACCOUNT = "assetAndValueByAccount";
//
//	/**
//	 * the transaction output spent state.
//	 */
//	private static final String TRANSACTION_OUTPUT_SPENT_STATE = "transactionOutputSpentState";
//
//	/**
//	 * the transaction outputs by account and index.
//	 */
//	private static final String TRANSACTION_BY_ACCOUNT_AND_INDEX = "transactionOutputsByAccountAndIndex";
//
//	/**
//	 * the transaction outputs by account max index.
//	 */
//	private static final String TRANSACTION_BY_ACCOUNT_MAX_INDEX = "transactionOutputsByAccountMaxIndex";
//
//	/**
//	 * the max block index.
//	 */
//	private static final String MAX_BLOCK_INDEX = "maxBlockIndex";
//
//	/**
//	 * the database.
//	 */
//	private final DB db;
//
//	/**
//	 * the directory.
//	 */
//	private final File fileSizeDir;
//
//	/**
//	 * the closed flag.
//	 */
//	private boolean closed = false;
//
//	/**
//	 * the constructor.
//	 *
//	 * @param config
//	 *            the configuration to use.
//	 */
//	public BlockDbMapDbImpl(final JSONObject config) {
//		fileSizeDir = new File(config.getString(ConfigurationUtil.FILE_SIZE_DIR));
//		final String url = config.getString(ConfigurationUtil.URL);
//		final File dbFile = new File(url);
//		dbFile.getParentFile().mkdirs();
//		db = DBMaker.fileDB(dbFile).transactionEnable().closeOnJvmShutdown().fileMmapEnableIfSupported()
//				.fileMmapPreclearDisable().allocateIncrement(ALLOCATION_INCREMENT_SIZE).make();
//	}
//
//	/**
//	 * close the database.
//	 *
//	 * @throws SQLException
//	 *             if an error occurs.
//	 */
//	@Override
//	public void close() {
//		synchronized (this) {
//			if (closed) {
//				return;
//			}
//			closed = true;
//		}
//		LOG.debug("STARTED shutdown");
//		db.close();
//		LOG.debug("SUCCESS shutdown");
//	}
//
//	/**
//	 * commit the validation so far.
//	 *
//	 * @param lastGoodBlockIndex
//	 *            the last good block index.
//	 */
//	private void commitValidation(final long lastGoodBlockIndex) {
//		setBlockIndex(lastGoodBlockIndex);
//		db.commit();
//	}
//
//	/**
//	 * returns true if the hash is in the database. <br>
//	 * checks both the "hash to block index" and "block index to header" map, in
//	 * case the header was deleted but the hash wasn't.
//	 *
//	 * @param hash
//	 *            the hash to use.
//	 *
//	 * @return true if the hash is in the database.
//	 */
//	@Override
//	public boolean containsBlockWithHash(final UInt256 hash) {
//		synchronized (this) {
//			if (closed) {
//				return false;
//			}
//		}
//		final BTreeMap<byte[], Long> blockIndexByHashMap = getBlockIndexByHashMap();
//		final BTreeMap<Long, byte[]> blockHeaderByIndexMap = getBlockHeaderByIndexMap();
//		final byte[] hashBa = hash.toByteArray();
//		if (blockIndexByHashMap.containsKey(hashBa)) {
//			final long index = blockIndexByHashMap.get(hashBa);
//			return blockHeaderByIndexMap.containsKey(index);
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * used to get blocks unstuck, during debugging.
//	 *
//	 * @param blockHeight
//	 *            the block height to remove.
//	 */
//	private void deleteBlockAtHeight(final long blockHeight) {
//		final BTreeMap<Long, byte[]> map = getBlockHeaderByIndexMap();
//		map.remove(blockHeight);
//	}
//
//	@Override
//	public void deleteHighestBlock() {
//		LOG.info("STARTED deleteHighestBlock");
//		try {
//			long blockHeight = getHeaderOfBlockWithMaxIndex().getIndexAsLong();
//			Block blockHeader = getBlock(blockHeight, false);
//			while (blockHeader == null) {
//				LOG.error("INTERIM INFO deleteHighestBlock height:{} block is null, decrementing by 1 and retrying");
//				blockHeight--;
//				blockHeader = getBlock(blockHeight, false);
//			}
//			LOG.info("INTERIM INFO deleteHighestBlock height:{};hash:{};timestamp:{};", blockHeight, blockHeader.hash,
//					blockHeader.getTimestamp());
//			final Block fullBlock = getBlock(blockHeight, true);
//			deleteBlockAtHeight(blockHeight);
//			try {
//				updateAssetAndValueByAccountMap(fullBlock, true);
//			} catch (final Exception e) {
//				throw new RuntimeException("deleteHighestBlock: error updating assets for block " + blockHeader.hash,
//						e);
//			}
//			setBlockIndex(blockHeight - 1);
//			db.commit();
//		} catch (final Exception e) {
//			LOG.error("FAILURE deleteHighestBlock", e);
//			db.rollback();
//		}
//		LOG.info("SUCCESS deleteHighestBlock");
//	}
//
//	/**
//	 * makes sure that an account exists with zeroed neo and gas assets.
//	 *
//	 * @param assetAndValueByAccountMap
//	 *            the account asset value map.
//	 * @param accountBa
//	 *            the account to use. as a byte array.
//	 * @return the account map.
//	 */
//	private Map<UInt256, Fixed8> ensureAccountExists(final BTreeMap<byte[], byte[]> assetAndValueByAccountMap,
//			final byte[] accountBa) {
//		if (!assetAndValueByAccountMap.containsKey(accountBa)) {
//			final Map<UInt256, Fixed8> friendAssetValueMap = new TreeMap<>();
//			putAssetValueMap(assetAndValueByAccountMap, accountBa, friendAssetValueMap);
//		}
//		return getAssetValueMapFromByteArray(assetAndValueByAccountMap.get(accountBa));
//	}
//
//	@Override
//	public Map<UInt160, Map<UInt256, Fixed8>> getAccountAssetValueMap() {
//		LOG.error("getAccountAssetValueMap STARTED");
//		final Map<UInt160, Map<UInt256, Fixed8>> accountAssetValueMap = new TreeMap<>();
//
//		final BTreeMap<byte[], byte[]> assetAndValueByAccountMap = getAssetAndValueByAccountMap();
//		LOG.error("getAccountAssetValueMap INTERIM assetAndValueByAccountMap.size:{};",
//				assetAndValueByAccountMap.size());
//
//		for (final byte[] key : assetAndValueByAccountMap.getKeys()) {
//			final byte[] value = assetAndValueByAccountMap.get(key);
//			final UInt160 account = new UInt160(key);
//			final Map<UInt256, Fixed8> map = getAssetValueMapFromByteArray(value);
//			accountAssetValueMap.put(account, map);
//		}
//
//		LOG.error("getAccountAssetValueMap SUCCESS, count:{}", accountAssetValueMap.size());
//		return accountAssetValueMap;
//	}
//
//	@Override
//	public long getAccountCount() {
//		return getAssetAndValueByAccountMap().size();
//	}
//
//	/**
//	 * gets the account key for the given account and index.
//	 *
//	 * @param accountBa
//	 *            the account byte array.
//	 * @param index
//	 *            the index.
//	 * @return the account key.
//	 */
//	private byte[] getAccountKey(final byte[] accountBa, final long index) {
//		final ByteArrayOutputStream bout;
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//			out.write(accountBa);
//			NetworkUtil.writeLong(out, index);
//			bout = out;
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//		return bout.toByteArray();
//	}
//
//	/**
//	 * return the map of transactions by key.
//	 *
//	 * @return the map of transactions by key.
//	 */
//	private BTreeMap<byte[], byte[]> getAssetAndValueByAccountMap() {
//		final BTreeMap<byte[], byte[]> map = db
//				.treeMap(ASSET_AND_VALUE_BY_ACCOUNT, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).counterEnable()
//				.createOrOpen();
//		return map;
//	}
//
//	@Override
//	public Map<UInt256, Fixed8> getAssetValueMap(final UInt160 account) {
//		final BTreeMap<byte[], byte[]> assetAndValueByAccountMap = getAssetAndValueByAccountMap();
//		final byte[] value = assetAndValueByAccountMap.get(account.toByteArray());
//		final Map<UInt256, Fixed8> map = getAssetValueMapFromByteArray(value);
//		return map;
//	}
//
//	/**
//	 * converts a byte array into a map of assets and values.
//	 *
//	 * @param ba
//	 *            the byte array to use.
//	 * @return the map.
//	 */
//	private Map<UInt256, Fixed8> getAssetValueMapFromByteArray(final byte[] ba) {
//		final Map<UInt256, Fixed8> map = new TreeMap<>();
//		final ByteBuffer bb = ByteBuffer.wrap(ba);
//		final int size = ModelUtil.getBigInteger(bb).intValue();
//
//		while (map.size() < size) {
//			final UInt256 key = new UInt256(ModelUtil.getVariableLengthByteArray(bb));
//			final Fixed8 value = new Fixed8(ByteBuffer.wrap(ModelUtil.getVariableLengthByteArray(bb)));
//			map.put(key, value);
//		}
//		return map;
//	}
//
//	/**
//	 * return the block at the given height, with transactions attached.
//	 *
//	 * @param blockHeight
//	 *            the block height to use.
//	 * @param withTransactions
//	 *            if true, add transactions. If false, only return the block header.
//	 * @return the block at the given height.
//	 */
//	private Block getBlock(final long blockHeight, final boolean withTransactions) {
//		synchronized (this) {
//			if (closed) {
//				return null;
//			}
//		}
//
//		final BTreeMap<Long, byte[]> map = getBlockHeaderByIndexMap();
//		if (!map.containsKey(blockHeight)) {
//			return null;
//		}
//
//		final Block block = new Block(ByteBuffer.wrap(map.get(blockHeight)));
//		if (withTransactions) {
//			getTransactionsForBlock(block);
//		}
//		return block;
//	}
//
//	/**
//	 * returns the block with the given hash.
//	 *
//	 * @param hash
//	 *            the hash to use.
//	 * @param withTransactions
//	 *            if true, add transactions. If false, only return the block header.
//	 * @return the block with the given hash.
//	 */
//	private Block getBlock(final UInt256 hash, final boolean withTransactions) {
//		synchronized (this) {
//			if (closed) {
//				return null;
//			}
//		}
//
//		final BTreeMap<byte[], Long> map = getBlockIndexByHashMap();
//		final byte[] hashBa = hash.toByteArray();
//		if (!map.containsKey(hashBa)) {
//			return null;
//		}
//		final long index = map.get(hashBa);
//		return getBlock(index, withTransactions);
//
//	}
//
//	/**
//	 * return the block count.
//	 *
//	 * @return the block count.
//	 */
//	@Override
//	public long getBlockCount() {
//		synchronized (this) {
//			if (closed) {
//				return 0;
//			}
//		}
//		final BTreeMap<Long, byte[]> map = getBlockHeaderByIndexMap();
//		return map.sizeLong();
//	}
//
//	/**
//	 * return the map of block headers by block indexes.
//	 *
//	 * @return the map of block headers by block indexes.
//	 */
//	public BTreeMap<Long, byte[]> getBlockHeaderByIndexMap() {
//		final BTreeMap<Long, byte[]> map = db.treeMap(BLOCK_HEADER_BY_INDEX, Serializer.LONG, Serializer.BYTE_ARRAY)
//				.counterEnable().createOrOpen();
//		return map;
//	}
//
//	/**
//	 * return the map of block indexes by block hash.
//	 *
//	 * @return the map of block indexes by block hash.
//	 */
//	private BTreeMap<byte[], Long> getBlockIndexByHashMap() {
//		return db.treeMap(BLOCK_INDEX_BY_HASH, Serializer.BYTE_ARRAY, Serializer.LONG).createOrOpen();
//	}
//
//	@Override
//	public Long getBlockIndexFromTransactionHash(final UInt256 hash) {
//		final BTreeMap<byte[], byte[]> map = getTransactionKeyByTransactionHashMap();
//		final byte[] hashBa = hash.toByteArray();
//		if (!map.containsKey(hashBa)) {
//			return null;
//		}
//		final byte[] txKey = map.get(hashBa);
//		return getBlockIndexFromTransactionKey(txKey);
//	}
//
//	/**
//	 * gets the block index from the transaction key.
//	 *
//	 * @param txKeyBa
//	 *            the transaction key.
//	 * @return the block index.
//	 */
//	private long getBlockIndexFromTransactionKey(final byte[] txKeyBa) {
//		final ByteBuffer keyBb = ByteBuffer.wrap(txKeyBa);
//		return keyBb.getLong();
//	}
//
//	/**
//	 * return the block with the maximum value in the index column.
//	 *
//	 * @param withTransactions
//	 *            if true, add transactions. If false, only return the block header.
//	 * @return the block with the maximum value in the index column.
//	 */
//	private Block getBlockWithMaxIndex(final boolean withTransactions) {
//		synchronized (this) {
//			if (closed) {
//				return null;
//			}
//		}
//
//		final long blockHeight = getMaxBlockIndex();
//		return getBlock(blockHeight, withTransactions);
//	}
//
//	/**
//	 * return the map of byte arrays by block index.
//	 *
//	 * @param mapName
//	 *            the name of the map to get.
//	 *
//	 * @return the map of byte arrays keys by block index.
//	 */
//	private BTreeMap<Long, byte[]> getByteArrayByBlockIndexMap(final String mapName) {
//		final BTreeMap<Long, byte[]> map = db.treeMap(mapName, Serializer.LONG, Serializer.BYTE_ARRAY).counterEnable()
//				.createOrOpen();
//		return map;
//	}
//
//	/**
//	 * converts a map of assets and values into a byte array.
//	 *
//	 * @param friendAssetValueMap
//	 *            the map to use.
//	 * @return the byte array.
//	 */
//	private byte[] getByteArrayFromAssetValueMap(final Map<UInt256, Fixed8> friendAssetValueMap) {
//		final byte[] mapBa;
//		final ByteArrayOutputStream bout;
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//			NetworkUtil.writeVarInt(out, friendAssetValueMap.size());
//			for (final UInt256 key : friendAssetValueMap.keySet()) {
//				final Fixed8 value = friendAssetValueMap.get(key);
//				NetworkUtil.writeByteArray(out, key.toByteArray());
//				final byte[] valueBa = value.toByteArray();
//				ArrayUtils.reverse(valueBa);
//				NetworkUtil.writeByteArray(out, valueBa);
//			}
//			bout = out;
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//		mapBa = bout.toByteArray();
//		return mapBa;
//	}
//
//	/**
//	 * returns a list of byte arrays from the map.
//	 *
//	 * @param map
//	 *            the map to use.
//	 * @param key
//	 *            the key to use.
//	 * @param <K>
//	 *            the key type.
//	 * @return the list of byte arrays.
//	 */
//	private <K> List<byte[]> getByteArrayList(final BTreeMap<K, byte[]> map, final K key) {
//		if (map.containsKey(key)) {
//			final byte[] keyListBa = map.get(key);
//			final List<byte[]> keyBaList = ModelUtil.toByteArrayList(keyListBa);
//			return keyBaList;
//		}
//		return Collections.emptyList();
//	}
//
//	/**
//	 * return the file size.
//	 *
//	 * @return the file size.
//	 */
//	@Override
//	public long getFileSize() {
//		return FileUtils.sizeOfDirectory(fileSizeDir);
//	}
//
//	@Override
//	public Block getFullBlockFromHash(final UInt256 hash) {
//		return getBlock(hash, true);
//	}
//
//	@Override
//	public Block getFullBlockFromHeight(final long blockHeight) {
//		return getBlock(blockHeight, true);
//	}
//
//	@Override
//	public Block getHeaderOfBlockFromHash(final UInt256 hash) {
//		return getBlock(hash, false);
//	}
//
//	@Override
//	public Block getHeaderOfBlockFromHeight(final long blockHeight) {
//		return getBlock(blockHeight, false);
//	}
//
//	/**
//	 * return the block with the maximum value in the index column.
//	 *
//	 * @return the block with the maximum value in the index column.
//	 */
//	@Override
//	public Block getHeaderOfBlockWithMaxIndex() {
//		return getBlockWithMaxIndex(false);
//	}
//
//	/**
//	 * return the max blockindex as an atomic long.
//	 *
//	 * @return the max blockindex as an atomic long.
//	 */
//	private long getMaxBlockIndex() {
//		final long retval = db.atomicLong(MAX_BLOCK_INDEX, 0).createOrOpen().get();
//		return retval;
//	}
//
//	/**
//	 * return the map of transactions by account and index.
//	 *
//	 * @return the map of transactions by account and index.
//	 */
//	private BTreeMap<byte[], byte[]> getTransactionByAccountAndIndexMap() {
//		final BTreeMap<byte[], byte[]> map = db
//				.treeMap(TRANSACTION_BY_ACCOUNT_AND_INDEX, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).createOrOpen();
//		return map;
//	}
//
//	/**
//	 * return the map of max index of transactions by account.
//	 *
//	 * @return the map of max index of transactions by account.
//	 */
//	private BTreeMap<byte[], Long> getTransactionByAccountMaxIndexMap() {
//		final BTreeMap<byte[], Long> map = db
//				.treeMap(TRANSACTION_BY_ACCOUNT_MAX_INDEX, Serializer.BYTE_ARRAY, Serializer.LONG).createOrOpen();
//		return map;
//	}
//
//	/**
//	 * gets the block index from the transaction key.
//	 *
//	 * @param txKeyBa
//	 *            the transaction key.
//	 * @return the block index.
//	 */
//	private int getTransactionIndexInBlockFromTransactionKey(final byte[] txKeyBa) {
//		final ByteBuffer keyBb = ByteBuffer.wrap(txKeyBa);
//		// skip the block index.
//		keyBb.getLong();
//		// return the transaction index.
//		return (int) keyBb.getLong();
//	}
//
//	/**
//	 * gets the transaction key for the given block index and transaction index.
//	 *
//	 * @param blockIndex
//	 *            the block index.
//	 * @param transactionIndex
//	 *            the transaction index.
//	 * @return the transaction key.
//	 */
//	private byte[] getTransactionKey(final long blockIndex, final int transactionIndex) {
//		final ByteArrayOutputStream bout;
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//			NetworkUtil.writeLong(out, blockIndex);
//			NetworkUtil.writeLong(out, transactionIndex);
//			bout = out;
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//		return bout.toByteArray();
//	}
//
//	/**
//	 * return the map of transaction keys by transaction hash.
//	 *
//	 * @return the map of transaction keys by transaction hash.
//	 */
//	private BTreeMap<byte[], byte[]> getTransactionKeyByTransactionHashMap() {
//		final BTreeMap<byte[], byte[]> map = db
//				.treeMap(TRANSACTION_KEY_BY_HASH, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).counterEnable()
//				.createOrOpen();
//		return map;
//	}
//
//	/**
//	 * return the map of unspent transaction outputs.
//	 *
//	 * @return the set of unspent transaction outputs.
//	 */
//	private BTreeMap<byte[], Boolean> getTransactionOutputSpentStateMap() {
//		final BTreeMap<byte[], Boolean> map = db
//				.treeMap(TRANSACTION_OUTPUT_SPENT_STATE, Serializer.BYTE_ARRAY, Serializer.BOOLEAN).createOrOpen();
//		return map;
//	}
//
//	/**
//	 * return the map of transactions by key.
//	 *
//	 * @return the map of transactions by key.
//	 */
//	private BTreeMap<byte[], byte[]> getTransactionsByKeyMap() {
//		final BTreeMap<byte[], byte[]> map = db
//				.treeMap(TRANSACTION_BY_KEY, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).counterEnable()
//				.createOrOpen();
//		return map;
//	}
//
//	/**
//	 * return the block, with transactions added.
//	 *
//	 * @param block
//	 *            the block, to add transactions to.
//	 */
//	private void getTransactionsForBlock(final Block block) {
//		final long blockIndex = block.getIndexAsLong();
//
//		final BTreeMap<Long, byte[]> txKeyListMap = getByteArrayByBlockIndexMap(TRANSACTION_KEYS_BY_BLOCK_INDEX);
//		final List<byte[]> txKeyBaList = getByteArrayList(txKeyListMap, blockIndex);
//
//		final BTreeMap<byte[], byte[]> txMap = getTransactionsByKeyMap();
//		for (final byte[] txKey : txKeyBaList) {
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("getTransactionsForBlock {} txKey:{}", blockIndex, ModelUtil.toHexString(txKey));
//			}
//			final byte[] data = txMap.get(txKey);
//			final Transaction transaction = new Transaction(ByteBuffer.wrap(data));
//			transaction.recalculateHash();
//			block.getTransactionList().add(transaction);
//		}
//	}
//
//	@Override
//	public List<Transaction> getTransactionWithAccountList(final UInt160 account) {
//		final List<Transaction> transactionList = new ArrayList<>();
//		final BTreeMap<byte[], byte[]> transactionByAccountAndIndexMap = getTransactionByAccountAndIndexMap();
//		final BTreeMap<byte[], Long> transactionByAccountMaxIndexMap = getTransactionByAccountMaxIndexMap();
//		final byte[] accountBa = account.toByteArray();
//		final long maxIndex = transactionByAccountMaxIndexMap.get(accountBa);
//		for (long ix = 0; ix < maxIndex; ix++) {
//			final byte[] accountKeyBa = getAccountKey(accountBa, ix);
//			final byte[] ba = transactionByAccountAndIndexMap.get(accountKeyBa);
//			final ByteBuffer bb = ByteBuffer.wrap(ba);
//			final Transaction transaction = new Transaction(bb);
//			transactionList.add(transaction);
//		}
//		return transactionList;
//	}
//
//	@Override
//	public Transaction getTransactionWithHash(final UInt256 hash) {
//		final BTreeMap<byte[], byte[]> map = getTransactionKeyByTransactionHashMap();
//		final byte[] hashBa = hash.toByteArray();
//		if (!map.containsKey(hashBa)) {
//			return null;
//		}
//		final byte[] txKey = map.get(hashBa);
//		final BTreeMap<byte[], byte[]> txMap = getTransactionsByKeyMap();
//		final byte[] data = txMap.get(txKey);
//		final Transaction transaction = new Transaction(ByteBuffer.wrap(data));
//		transaction.recalculateHash();
//		return transaction;
//	}
//
//	@Override
//	public Map<UInt256, Map<TransactionOutput, CoinReference>> getUnspentTransactionOutputListMap(
//			final UInt160 account) {
//		final List<Transaction> transactionList = getTransactionWithAccountList(account);
//		final BTreeMap<byte[], Boolean> transactionOutputSpentStateMap = getTransactionOutputSpentStateMap();
//		final Map<UInt256, Map<TransactionOutput, CoinReference>> assetIdTxoMap = new TreeMap<>();
//		final BTreeMap<byte[], byte[]> txKeyByHashMap = getTransactionKeyByTransactionHashMap();
//
//		for (final Transaction transaction : transactionList) {
//			final byte[] hashBa = transaction.getHash().toByteArray();
//			final byte[] txKeyBa = txKeyByHashMap.get(hashBa);
//			for (final TransactionOutput to : transaction.outputs) {
//				if (to.scriptHash.equals(account)) {
//					final byte[] toBa = to.toByteArray();
//					if (transactionOutputSpentStateMap.containsKey(toBa)) {
//						if (transactionOutputSpentStateMap.get(toBa) == true) {
//							if (!assetIdTxoMap.containsKey(to.assetId)) {
//								assetIdTxoMap.put(to.assetId, new TreeMap<>());
//							}
//							final int txIx = getTransactionIndexInBlockFromTransactionKey(txKeyBa);
//							final CoinReference cr = new CoinReference(transaction.getHash(), new UInt16(txIx));
//							assetIdTxoMap.get(to.assetId).put(to, cr);
//						}
//					}
//				}
//			}
//		}
//		return assetIdTxoMap;
//	}
//
//	@Override
//	public void put(final boolean forceSynch, final Block... blocks) {
//		synchronized (this) {
//			if (closed) {
//				return;
//			}
//		}
//
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("STARTED put, {} blocks", NumberFormat.getIntegerInstance().format(blocks.length));
//		}
//		try {
//			final BTreeMap<byte[], Long> blockIndexByHashMap = getBlockIndexByHashMap();
//			final BTreeMap<Long, byte[]> blockHeaderByIndexMap = getBlockHeaderByIndexMap();
//
//			for (final Block block : blocks) {
//				synchronized (this) {
//					if (closed) {
//						db.rollback();
//						return;
//					}
//				}
//				final long blockIndex = block.getIndexAsLong();
//				final long maxBlockIndex = getMaxBlockIndex();
//				final boolean duplicateBlock;
//				if ((blockIndex <= maxBlockIndex) && (blockIndex != 0) && (maxBlockIndex != 0)) {
//					duplicateBlock = true;
//				} else {
//					duplicateBlock = false;
//				}
//
//				if (duplicateBlock) {
//					LOG.error("duplicate block,blockIndex:{};maxBlockIndex:{};hash:{};", blockIndex, maxBlockIndex,
//							block.hash);
//				} else {
//					final byte[] prevHashBa = block.prevHash.toByteArray();
//					ArrayUtils.reverse(prevHashBa);
//
//					blockIndexByHashMap.put(block.hash.toByteArray(), blockIndex);
//					blockHeaderByIndexMap.put(blockIndex, block.toHeaderByteArray());
//
//					int transactionIndex = 0;
//
//					final Map<Long, List<byte[]>> txKeyByBlockIxMap = new TreeMap<>();
//					final Map<ByteBuffer, byte[]> txByKeyMap = new TreeMap<>();
//					final Map<ByteBuffer, byte[]> txKeyByTxHashMap = new TreeMap<>();
//
//					txKeyByBlockIxMap.put(blockIndex, new ArrayList<>());
//
//					for (final Transaction transaction : block.getTransactionList()) {
//						final byte[] transactionBaseBa = transaction.toByteArray();
//						final byte[] transactionKeyBa = getTransactionKey(blockIndex, transactionIndex);
//
//						putList(txKeyByBlockIxMap, blockIndex, transactionKeyBa);
//
//						final ByteBuffer transactionKeyBb = ByteBuffer.wrap(transactionKeyBa);
//						txByKeyMap.put(transactionKeyBb, transactionBaseBa);
//
//						txKeyByTxHashMap.put(ByteBuffer.wrap(transaction.getHash().toByteArray()), transactionKeyBa);
//
//						transactionIndex++;
//					}
//
//					putWithByteBufferKey(TRANSACTION_KEY_BY_HASH, txKeyByTxHashMap);
//					putWithByteBufferKey(TRANSACTION_BY_KEY, txByKeyMap);
//
//					putWithLongKey(TRANSACTION_KEYS_BY_BLOCK_INDEX, toByteBufferValue(txKeyByBlockIxMap));
//
//					try {
//						updateAssetAndValueByAccountMap(block, false);
//					} catch (final Exception e) {
//						throw new RuntimeException("put: error updating assets for block " + block.hash, e);
//					}
//					updateMaxBlockIndex(blockIndex);
//				}
//			}
//
//			db.commit();
//		} catch (final Exception e) {
//			LOG.error("FAILURE put, {} blocks", NumberFormat.getIntegerInstance().format(blocks.length));
//			LOG.error("FAILURE put", e);
//			db.rollback();
//			throw new RuntimeException(e);
//		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("SUCCESS put, {} blocks", NumberFormat.getIntegerInstance().format(blocks.length));
//		}
//	}
//
//	/**
//	 * puts the asset value map into the account map.
//	 *
//	 * @param assetAndValueByAccountMap
//	 *            the account map to use.
//	 * @param accountBa
//	 *            the account key byte array.
//	 * @param friendAssetValueMap
//	 *            the asset value map to use.
//	 */
//	private void putAssetValueMap(final BTreeMap<byte[], byte[]> assetAndValueByAccountMap, final byte[] accountBa,
//			final Map<UInt256, Fixed8> friendAssetValueMap) {
//		final byte[] mapBa = getByteArrayFromAssetValueMap(friendAssetValueMap);
//		assetAndValueByAccountMap.put(accountBa, mapBa);
//	}
//
//	/**
//	 * adds the value to the map, using the key.
//	 *
//	 * @param map
//	 *            the map to use.
//	 * @param key
//	 *            the key.
//	 * @param value
//	 *            the value.
//	 * @param <K>
//	 *            the type of key.
//	 */
//	private <K> void putList(final Map<K, List<byte[]>> map, final K key, final byte[] value) {
//		map.get(key).add(value);
//	}
//
//	/**
//	 * put the data in the map into the named database map.
//	 *
//	 * @param destMapName
//	 *            the destination map name to use.
//	 * @param sourceMap
//	 *            the source map to use.
//	 */
//	private void putWithByteBufferKey(final String destMapName, final Map<ByteBuffer, byte[]> sourceMap) {
//		final BTreeMap<byte[], byte[]> map = db.treeMap(destMapName, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY)
//				.counterEnable().createOrOpen();
//		for (final ByteBuffer key : sourceMap.keySet()) {
//			final byte[] ba = sourceMap.get(key);
//
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("putWithByteBufferKey {} {} {}", destMapName, ModelUtil.toHexString(key.array()),
//						ModelUtil.toHexString(ba));
//			}
//
//			map.put(key.array(), ba);
//		}
//	}
//
//	/**
//	 * put the data in the map into the named database map.
//	 *
//	 * @param destMapName
//	 *            the destination map name to use.
//	 * @param sourceMap
//	 *            the source map to use.
//	 */
//	private void putWithLongKey(final String destMapName, final Map<Long, byte[]> sourceMap) {
//		final BTreeMap<Long, byte[]> map = db.treeMap(destMapName, Serializer.LONG, Serializer.BYTE_ARRAY)
//				.counterEnable().createOrOpen();
//		for (final Long key : sourceMap.keySet()) {
//			final byte[] ba = sourceMap.get(key);
//			map.put(key, ba);
//		}
//	}
//
//	/**
//	 * sets the blockindex to be the given block index.
//	 *
//	 * @param blockIndex
//	 *            the block index to use.
//	 */
//	private void setBlockIndex(final long blockIndex) {
//		db.atomicLong(MAX_BLOCK_INDEX, blockIndex).createOrOpen().set(blockIndex);
//	}
//
//	/**
//	 * serializes a list of byte array values into a single byte array.
//	 *
//	 * @param sourceMap
//	 *            the map to use.
//	 * @param <K>
//	 *            the type of key.
//	 * @return the map with byte array values.
//	 */
//	private <K> Map<K, byte[]> toByteBufferValue(final Map<K, List<byte[]>> sourceMap) {
//		final Map<K, byte[]> destMap = new TreeMap<>();
//		for (final K key : sourceMap.keySet()) {
//			final List<byte[]> baList = sourceMap.get(key);
//			destMap.put(key, ModelUtil.toByteArray(baList));
//		}
//		return destMap;
//	}
//
//	/**
//	 * updates the asset and value by account map.
//	 *
//	 * @param block
//	 *            the block to update.
//	 * @param reverse
//	 *            if true, reverse the update.
//	 */
//	private void updateAssetAndValueByAccountMap(final Block block, final boolean reverse) {
//		final BTreeMap<byte[], byte[]> assetAndValueByAccountMap = getAssetAndValueByAccountMap();
//		final BTreeMap<byte[], byte[]> transactionByAccountAndIndexMap = getTransactionByAccountAndIndexMap();
//		final BTreeMap<byte[], Long> transactionByAccountMaxIndexMap = getTransactionByAccountMaxIndexMap();
//		final BTreeMap<byte[], Boolean> transactionOutputSpentStateMap = getTransactionOutputSpentStateMap();
//		LOG.debug("updateAssetAndValueByAccountMap STARTED block;{};reverse;{};numberOfAccounts:{}",
//				block.getIndexAsLong(), reverse, assetAndValueByAccountMap.size());
//
//		for (final Transaction t : block.getTransactionList()) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("updateAssetAndValueByAccountMap INTERIM tx:{}", t.getHash());
//			}
//			for (final CoinReference cr : t.inputs) {
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("updateAssetAndValueByAccountMap INTERIM cr:{}", cr.toJSONObject());
//				}
//
//				final byte[] crBa = cr.toByteArray();
//				if (!transactionOutputSpentStateMap.containsKey(crBa)) {
//					throw new RuntimeException("referenced transaction output was never a transaction input:" + cr);
//				}
//
//				final boolean oldSpendState;
//				final boolean newSpendState;
//				if (reverse) {
//					oldSpendState = true;
//					newSpendState = false;
//				} else {
//					oldSpendState = false;
//					newSpendState = true;
//				}
//
//				if (transactionOutputSpentStateMap.get(crBa) == oldSpendState) {
//					transactionOutputSpentStateMap.put(crBa, newSpendState);
//
//					final UInt256 prevHashReversed = cr.prevHash.reverse();
//					final Transaction tiTx = getTransactionWithHash(prevHashReversed);
//
//					if (tiTx == null) {
//						throw new RuntimeException("no transaction with prevHash:" + prevHashReversed + " in block[1] "
//								+ block.hash + " index[1] " + block.getIndexAsLong());
//					}
//
//					final int prevIndex = cr.prevIndex.asInt();
//					if (prevIndex >= tiTx.outputs.size()) {
//						throw new RuntimeException(
//								"prevIndex:" + prevIndex + " exceeds output size:" + tiTx.outputs.size()
//										+ "; in block[2] " + block.hash + " index[2] " + block.getIndexAsLong());
//					}
//					final TransactionOutput ti = tiTx.outputs.get(prevIndex);
//					final UInt160 input = ti.scriptHash;
//					final byte[] inputBa = input.toByteArray();
//					final Map<UInt256, Fixed8> accountAssetValueMap = ensureAccountExists(assetAndValueByAccountMap,
//							inputBa);
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("TI beforeMap {}", accountAssetValueMap);
//					}
//
//					if (!accountAssetValueMap.containsKey(ti.assetId)) {
//						accountAssetValueMap.put(ti.assetId, ModelUtil.FIXED8_ZERO);
//					}
//
//					final Fixed8 oldValue = accountAssetValueMap.get(ti.assetId);
//					final Fixed8 newValue;
//					if (reverse) {
//						newValue = ModelUtil.add(ti.value, oldValue);
//					} else {
//						newValue = ModelUtil.subtract(ti.value, oldValue);
//					}
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM input;{};",
//								ModelUtil.scriptHashToAddress(input));
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM ti.assetId:{} oldValue:{};", ti.assetId,
//								oldValue);
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM ti.assetId:{} to.value:{};", ti.assetId,
//								ti.value);
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM ti.assetId:{} newValue:{};", ti.assetId,
//								newValue);
//					}
//					if (newValue.equals(ModelUtil.FIXED8_ZERO)) {
//						accountAssetValueMap.remove(ti.assetId);
//					} else {
//						accountAssetValueMap.put(ti.assetId, newValue);
//					}
//					if (accountAssetValueMap.isEmpty()) {
//						assetAndValueByAccountMap.remove(inputBa);
//					} else {
//						putAssetValueMap(assetAndValueByAccountMap, inputBa, accountAssetValueMap);
//					}
//
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("TI afterMap {}", ensureAccountExists(assetAndValueByAccountMap, inputBa));
//					}
//				} else {
//					if (reverse) {
//						throw new RuntimeException("referenced transaction output is not already spent:" + cr);
//					} else {
//						throw new RuntimeException("referenced transaction output is already spent:" + cr);
//					}
//				}
//
//			}
//
//			try {
//				for (int outputIx = 0; outputIx < t.outputs.size(); outputIx++) {
//					final TransactionOutput to = t.outputs.get(outputIx);
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM to:{}", to.toJSONObject());
//					}
//					final UInt160 output = to.scriptHash;
//					final byte[] outputBa = output.toByteArray();
//					final Map<UInt256, Fixed8> accountAssetValueMap = ensureAccountExists(assetAndValueByAccountMap,
//							outputBa);
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("TO beforeMap {}", accountAssetValueMap);
//					}
//
//					if (!accountAssetValueMap.containsKey(to.assetId)) {
//						accountAssetValueMap.put(to.assetId, ModelUtil.FIXED8_ZERO);
//					}
//
//					final Fixed8 oldValue = accountAssetValueMap.get(to.assetId);
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM output;{};",
//								ModelUtil.scriptHashToAddress(output));
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM to.assetId:{} oldValue:{};", to.assetId,
//								oldValue);
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM to.assetId:{} to.value:{};", to.assetId,
//								to.value);
//					}
//
//					final Fixed8 newValue;
//					if (reverse) {
//						newValue = ModelUtil.subtract(to.value, oldValue);
//					} else {
//						newValue = ModelUtil.add(oldValue, to.value);
//					}
//					accountAssetValueMap.put(to.assetId, newValue);
//
//					if (accountAssetValueMap.isEmpty()) {
//						assetAndValueByAccountMap.remove(outputBa);
//					} else {
//						putAssetValueMap(assetAndValueByAccountMap, outputBa, accountAssetValueMap);
//					}
//
//					if (LOG.isDebugEnabled()) {
//						LOG.debug("updateAssetAndValueByAccountMap INTERIM to.assetId:{} newValue:{};", to.assetId,
//								newValue);
//						LOG.debug("TO afterMap {}", ensureAccountExists(assetAndValueByAccountMap, outputBa));
//					}
//
//					final CoinReference cr = new CoinReference(t.getHash().reverse(), new UInt16(outputIx));
//					if (reverse) {
//						transactionOutputSpentStateMap.remove(cr.toByteArray());
//					} else {
//						transactionOutputSpentStateMap.put(cr.toByteArray(), false);
//					}
//				}
//
//				final byte[] tBa = t.toByteArray();
//
//				final long index;
//				if (transactionByAccountMaxIndexMap.containsKey(tBa)) {
//					index = transactionByAccountMaxIndexMap.get(tBa);
//				} else {
//					index = 0;
//				}
//				transactionByAccountMaxIndexMap.put(tBa, index + 1);
//
//				final byte[] accountKeyBa = getAccountKey(tBa, index);
//
//				transactionByAccountAndIndexMap.put(accountKeyBa, tBa);
//			} catch (final RuntimeException e) {
//				final String msg = "error processing transaction type " + t.type + " hash " + t.getHash();
//				throw new RuntimeException(msg, e);
//			}
//		}
//		LOG.debug("updateAssetAndValueByAccountMap SUCCESS block;{};numberOfAccounts:{}", block.getIndexAsLong(),
//				assetAndValueByAccountMap.size());
//	}
//
//	/**
//	 * updates the block index, if the new block index is greater than the existing
//	 * block index.
//	 *
//	 * @param blockIndex
//	 *            the new block index.
//	 */
//	private void updateMaxBlockIndex(final long blockIndex) {
//		if (blockIndex > getMaxBlockIndex()) {
//			setBlockIndex(blockIndex);
//		}
//	}
//
//	@Override
//	public void validate() {
//		LOG.info("STARTED validate");
//		try {
//			final Block block0 = getBlock(0, false);
//			if (!block0.hash.equals(GenesisBlockUtil.GENESIS_HASH)) {
//				throw new RuntimeException(
//						"height 0 block hash \"" + block0.hash.toHexString() + "\" does not match genesis block hash \""
//								+ GenesisBlockUtil.GENESIS_HASH.toHexString() + "\".");
//			}
//
//			long lastInfoMs = System.currentTimeMillis();
//
//			long blockHeight = 0;
//			long lastGoodBlockIndex = -1;
//			final long maxBlockCount = getBlockCount();
//
//			boolean blockHeightNoLongerValid = false;
//
//			final String maxBlockCountStr;
//			if (LOG.isDebugEnabled() || LOG.isErrorEnabled()) {
//				maxBlockCountStr = NumberFormat.getIntegerInstance().format(maxBlockCount);
//			} else {
//				maxBlockCountStr = null;
//			}
//
//			LOG.info("INTERIM validate, clear account list STARTED");
//			final BTreeMap<byte[], byte[]> assetAndValueByAccountMap = getAssetAndValueByAccountMap();
//			assetAndValueByAccountMap.clear();
//			LOG.info("INTERIM validate, clear account list SUCCESS");
//
//			LOG.info("INTERIM validate, clear transaction output state STARTED");
//			getTransactionByAccountAndIndexMap().clear();
//			getTransactionByAccountMaxIndexMap().clear();
//			LOG.info("INTERIM validate, clear  transaction output state SUCCESS");
//
//			while (blockHeight < maxBlockCount) {
//				final String blockHeightStr;
//				if (LOG.isDebugEnabled() || LOG.isErrorEnabled()) {
//					blockHeightStr = NumberFormat.getIntegerInstance().format(blockHeight);
//				} else {
//					blockHeightStr = null;
//				}
//
//				LOG.debug("INTERIM DEBUG validate {} of {} STARTED ", blockHeightStr, maxBlockCountStr);
//				final Block block = getBlock(blockHeight, true);
//				if (block == null) {
//					LOG.error("INTERIM validate {} of {} FAILURE, block not found in blockchain.", blockHeightStr,
//							maxBlockCountStr);
//					blockHeightNoLongerValid = true;
//				} else if ((blockHeight != 0) && (!containsBlockWithHash(block.prevHash))) {
//					LOG.error("INTERIM validate {} of {} FAILURE, prevHash {} not found in blockchain.", blockHeightStr,
//							maxBlockCountStr, block.prevHash.toHexString());
//					deleteBlockAtHeight(blockHeight);
//					blockHeightNoLongerValid = true;
//				} else if (block.getIndexAsLong() != blockHeight) {
//					LOG.error("INTERIM validate {} of {} FAILURE, indexAsLong {} does not match blockchain.",
//							blockHeightStr, maxBlockCountStr, block.getIndexAsLong());
//					deleteBlockAtHeight(blockHeight);
//					blockHeightNoLongerValid = true;
//				} else if (blockHeightNoLongerValid) {
//					LOG.error("INTERIM validate {} of {} FAILURE, block height tainted.", blockHeightStr,
//							maxBlockCountStr, block.getIndexAsLong());
//					deleteBlockAtHeight(blockHeight);
//				} else {
//					if (System.currentTimeMillis() > (lastInfoMs + 30000)) {
//						final String numberOfAccountsStr = NumberFormat.getIntegerInstance()
//								.format(assetAndValueByAccountMap.size());
//						LOG.info("INTERIM INFO  validate {} of {} SUCCESS, number of accounts:{}; date:{}",
//								blockHeightStr, maxBlockCountStr, numberOfAccountsStr, block.getTimestamp());
//						lastInfoMs = System.currentTimeMillis();
//					} else {
//						LOG.debug("INTERIM DEBUG validate {} of {} SUCCESS.", blockHeightStr, maxBlockCountStr);
//					}
//
//					final long blockIndex = block.getIndexAsLong();
//					int transactionIndex = 0;
//					final Map<ByteBuffer, byte[]> txKeyByTxHashMap = new TreeMap<>();
//					for (final Transaction transaction : block.getTransactionList()) {
//						final byte[] transactionKeyBa = getTransactionKey(blockIndex, transactionIndex);
//						txKeyByTxHashMap.put(ByteBuffer.wrap(transaction.getHash().toByteArray()), transactionKeyBa);
//						transactionIndex++;
//					}
//					putWithByteBufferKey(TRANSACTION_KEY_BY_HASH, txKeyByTxHashMap);
//
//					try {
//						updateAssetAndValueByAccountMap(block, false);
//					} catch (final Exception e) {
//						throw new RuntimeException("validate: error updating assets for block ["
//								+ block.getIndexAsLong() + "]" + block.hash, e);
//					}
//
//					lastGoodBlockIndex = block.getIndexAsLong();
//				}
//
//				final boolean forceSynch = (lastGoodBlockIndex % BLOCK_FORCE_SYNCH_INTERVAL) == 0;
//				if (forceSynch) {
//					LOG.info("INTERIM validate, partial commit STARTED index {}", lastGoodBlockIndex);
//					commitValidation(lastGoodBlockIndex);
//					LOG.info("INTERIM validate, partial commit SUCCESS index {}", lastGoodBlockIndex);
//				}
//
//				blockHeight++;
//			}
//
//			LOG.info("INTERIM validate, commit STARTED index {}", lastGoodBlockIndex);
//			commitValidation(lastGoodBlockIndex);
//			LOG.info("INTERIM validate, commit SUCCESS index {}", lastGoodBlockIndex);
//
//			LOG.info("SUCCESS validate");
//		} catch (
//
//		final Exception e) {
//			LOG.error("FAILURE validate", e);
//			db.rollback();
//			throw new RuntimeException(e);
//		}
//	}
//}

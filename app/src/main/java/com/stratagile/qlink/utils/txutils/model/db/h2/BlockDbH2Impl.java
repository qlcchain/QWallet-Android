//package com.stratagile.qlink.utils.txutils.model.db.h2;
//
//import com.github.mikephil.charting.utils.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.sql.SQLException;
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.commons.lang3.ArrayUtils;
////import org.h2.jdbcx.JdbcDataSource;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.XML;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcOperations;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.TransactionCallbackWithoutResult;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import neo.model.bytes.Fixed8;
//import neo.model.bytes.UInt16;
//import neo.model.bytes.UInt160;
//import neo.model.bytes.UInt256;
//import neo.model.bytes.UInt32;
//import neo.model.core.Block;
//import neo.model.core.CoinReference;
//import neo.model.core.Transaction;
//import neo.model.core.TransactionOutput;
//import neo.model.core.Witness;
//import neo.model.db.BlockDb;
//import neo.model.util.BlockUtil;
//import neo.model.util.ConfigurationUtil;
//import neo.model.util.GenesisBlockUtil;
//
///**
// * the block database.
// *
// * @author coranos
// *
// */
//public final class BlockDbH2Impl implements BlockDb {
//
//	/**
//	 * the transaction index.
//	 */
//	private static final String TRANSACTION_INDEX = "transaction_index";
//
//	/**
//	 * the JSON key "sql".
//	 */
//	private static final String SQL = "sql";
//
//	/**
//	 * the logger.
//	 */
//	private static final Logger LOG = LoggerFactory.getLogger(BlockDbH2Impl.class);
//
//	/**
//	 * the SQL cache XML file name.
//	 */
//	private static final String SQL_CACHE_XML = "BlockDbH2Impl.xml";
//
//	/**
//	 * the data source.
//	 */
//	private final JdbcDataSource ds;
//
//	/**
//	 * the SQL cache.
//	 */
//	private final JSONObject sqlCache;
//
//	/**
//	 * the closed flag.
//	 */
//	private boolean closed = false;
//
//	/**
//	 * the directory to read to get the db file size.
//	 */
//	private final File fileSizeDir;
//
//	/**
//	 * the constructor.
//	 *
//	 * @param config
//	 *            the configuration to use.
//	 */
//	public BlockDbH2Impl(final JSONObject config) {
//		try (InputStream resourceAsStream = BlockDbH2Impl.class.getResourceAsStream(SQL_CACHE_XML);) {
//			final String jsonStr = IOUtils.toString(resourceAsStream, "UTF-8");
//			sqlCache = XML.toJSONObject(jsonStr, true).getJSONObject("BlockDbImpl");
//		} catch (final IOException | NullPointerException e) {
//			throw new RuntimeException("error reading resource\"" + SQL_CACHE_XML + "\" ", e);
//		}
//
//		fileSizeDir = new File(config.getString(ConfigurationUtil.FILE_SIZE_DIR));
//
//		ds = new JdbcDataSource();
//		ds.setUrl(config.getString(ConfigurationUtil.URL));
//
//		final JdbcTemplate t = new JdbcTemplate(ds);
//
//		executeSqlGroup(t, "create");
//	}
//
//	/**
//	 * add the parameters to the list.
//	 *
//	 * @param list
//	 *            the list to use.
//	 * @param parms
//	 *            the parameters to add to the list.
//	 */
//	private void add(final List<Object[]> list, final Object... parms) {
//		list.add(parms);
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
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		executeSqlGroup(t, "close");
//		LOG.debug("SUCCESS shutdown");
//	}
//
//	/**
//	 * returns true if the hash is in the database.
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
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("containsHash");
//		final List<Integer> data = t.queryForList(sql, Integer.class, hash.toByteArray());
//		return !data.isEmpty();
//	}
//
//	/**
//	 * used to get blocks unstuck, during debugging.
//	 *
//	 * @param blockHeight
//	 *            the block height to remove.
//	 */
//	public void deleteBlockAtHeight(final long blockHeight) {
//
//		final byte[] blockHeightBa = BlockUtil.getBlockHeightBa(blockHeight);
//		final DataSourceTransactionManager tsMan = new DataSourceTransactionManager(ds);
//
//		final TransactionTemplate txTemplate = new TransactionTemplate(tsMan);
//		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		try {
//			txTemplate.execute(new TransactionCallbackWithoutResult() {
//				@Override
//				protected void doInTransactionWithoutResult(final TransactionStatus ts) {
//					try {
//						final JdbcTemplate t = new JdbcTemplate(ds);
//						executeSqlGroup(t, "deleteBlockAtHeight", blockHeightBa);
//					} catch (final Exception e) {
//						if (LOG.isErrorEnabled()) {
//							LOG.error("deleteBlockAtHeight sql exception", e);
//						}
//					}
//				}
//			});
//		} catch (final DataAccessException e) {
//			if (LOG.isErrorEnabled()) {
//				LOG.error("deleteBlockAtHeight data access exception", e);
//			}
//		}
//	}
//
//	@Override
//	public void deleteHighestBlock() {
//		final long blockHeight = getHeaderOfBlockWithMaxIndex().getIndexAsLong();
//		deleteBlockAtHeight(blockHeight);
//	}
//
//	/**
//	 * executes the group of SQL in the SQL Cache.
//	 *
//	 * @param jdbc
//	 *            the jdbcoperations to use.
//	 *
//	 * @param sqlGroup
//	 *            the group of SQL to pull out of the sqlcache to execute.
//	 * @param parms
//	 *            the parameters to use.
//	 */
//	private void executeSqlGroup(final JdbcOperations jdbc, final String sqlGroup, final Object... parms) {
//		final JSONObject sqlGroupJo = sqlCache.getJSONObject(sqlGroup);
//		if (!sqlGroupJo.has(SQL)) {
//			throw new RuntimeException("no key \"" + SQL + "\" in " + sqlGroupJo.keySet());
//		}
//
//		if (sqlGroupJo.get(SQL) instanceof JSONArray) {
//			final JSONArray createSqls = sqlGroupJo.getJSONArray(SQL);
//			for (int createSqlIx = 0; createSqlIx < createSqls.length(); createSqlIx++) {
//				final String sql = createSqls.getString(createSqlIx);
//				if (LOG.isTraceEnabled()) {
//					LOG.trace("[1] sql:{} parms:{};", sql, parms);
//				}
//				jdbc.update(sql, parms);
//			}
//		} else if (sqlGroupJo.get(SQL) instanceof String) {
//			final String sql = sqlGroupJo.getString(SQL);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("[2] sql:{} parms:{};", sql, parms);
//			}
//			jdbc.update(sql, parms);
//		} else {
//			throw new RuntimeException(
//					"no key of type String or JSONArray in \"" + SQL + "\" found in " + sqlGroupJo.keySet());
//		}
//	}
//
//	@Override
//	public Map<UInt160, Map<UInt256, Fixed8>> getAccountAssetValueMap() {
//		final JdbcTemplate jdbcOperations = new JdbcTemplate(ds);
//		final String sql = getSql("getAccountAssetValueMap");
//
//		final List<Map<String, Object>> mapList = jdbcOperations.queryForList(sql);
//
//		final Map<UInt160, Map<UInt256, Fixed8>> accountAssetValueMap = new TreeMap<>();
//
//		final TransactionOutputMapToObject mapToObject = new TransactionOutputMapToObject();
//
//		for (final Map<String, Object> map : mapList) {
//			final TransactionOutput output = mapToObject.toObject(map);
//
//			if (!accountAssetValueMap.containsKey(output.scriptHash)) {
//				accountAssetValueMap.put(output.scriptHash, new TreeMap<>());
//			}
//			final Map<UInt256, Fixed8> assetValueMap = accountAssetValueMap.get(output.scriptHash);
//			assetValueMap.put(output.assetId, output.value);
//		}
//
//		return accountAssetValueMap;
//	}
//
//	@Override
//	public long getAccountCount() {
//		final JdbcTemplate jdbcOperations = new JdbcTemplate(ds);
//		final String sql = getSql("getAccountCount");
//		return jdbcOperations.queryForObject(sql, Long.class);
//	}
//
//	@Override
//	public Map<UInt256, Fixed8> getAssetValueMap(final UInt160 account) {
//		final JdbcTemplate jdbcOperations = new JdbcTemplate(ds);
//		final String sql = getSql("getAssetValueMap");
//
//		final List<Map<String, Object>> mapList = jdbcOperations.queryForList(sql, account);
//
//		final Map<UInt256, Fixed8> assetValueMap = new TreeMap<>();
//
//		final TransactionOutputMapToObject mapToObject = new TransactionOutputMapToObject();
//
//		for (final Map<String, Object> map : mapList) {
//			final TransactionOutput output = mapToObject.toObject(map);
//			assetValueMap.put(output.assetId, output.value);
//		}
//
//		return assetValueMap;
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
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final byte[] indexBa = BlockUtil.getBlockHeightBa(blockHeight);
//		final String sql = getSql("getBlockWithIndex");
//		final List<byte[]> data = t.queryForList(sql, byte[].class, indexBa);
//		if (data.isEmpty()) {
//			return null;
//		}
//
//		final Block block = new Block(ByteBuffer.wrap(data.get(0)));
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
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getBlockWithHash");
//		final List<byte[]> data = t.queryForList(sql, byte[].class, hash.toByteArray());
//		if (data.isEmpty()) {
//			return null;
//		}
//
//		final Block block = new Block(ByteBuffer.wrap(data.get(0)));
//		if (withTransactions) {
//			getTransactionsForBlock(block);
//		}
//		return block;
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
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getBlockCount");
//		return t.queryForObject(sql, Integer.class);
//	}
//
//	@Override
//	public Long getBlockIndexFromTransactionHash(final UInt256 hash) {
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getBlockIndexFromTransactionHash");
//		return t.queryForObject(sql, new Object[] { hash.toByteArray() }, Long.class);
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
//		synchronized (this) {
//			if (closed) {
//				return null;
//			}
//		}
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getBlockWithMaxIndex");
//		final List<byte[]> data = t.queryForList(sql, byte[].class);
//		if (data.isEmpty()) {
//			return null;
//		}
//
//		final Block block = new Block(ByteBuffer.wrap(data.get(0)));
//		return block;
//	}
//
//	/**
//	 * return a map of the objects, divided into their transactions indexes.
//	 *
//	 * @param jdbcOperations
//	 *            the jdbc operations to use.
//	 * @param sqlKey
//	 *            the sql key to use.
//	 * @param sqlArgsBaList
//	 *            the byte arrays to use for SQL arguments.
//	 * @param mapToObject
//	 *            the mapToObject to use.
//	 * @param <T>
//	 *            the object type to use.
//	 * @return a map of the objects, divided into their transactions indexes.
//	 */
//	private <T> Map<Integer, List<T>> getMapList(final JdbcOperations jdbcOperations, final String sqlKey,
//			final AbstractMapToObject<T> mapToObject, final byte[]... sqlArgsBaList) {
//		final String sql = getSql(sqlKey);
//
//		final List<Map<String, Object>> mapList = jdbcOperations.queryForList(sql, (Object[]) sqlArgsBaList);
//
//		final Map<Integer, List<T>> tMapList = new TreeMap<>();
//		for (final Map<String, Object> map : mapList) {
//			final byte[] transactionIndexBa = (byte[]) map.get(TRANSACTION_INDEX);
//			final T t = mapToObject.toObject(map);
//			final int transactionIndex = getTransactionIndex(transactionIndexBa);
//			if (!tMapList.containsKey(transactionIndex)) {
//				tMapList.put(transactionIndex, new ArrayList<>());
//			}
//			tMapList.get(transactionIndex).add(t);
//		}
//
//		return tMapList;
//	}
//
//	/**
//	 * returns the SQL in the sqlcache (must be a singleton SQL in the sqlGroup).
//	 *
//	 * @param sqlGroup
//	 *            the group of SQL to pull out of the sqlcache to execute.
//	 * @return the SQL in the sqlcache (must be a singleton SQL in the sqlGroup).
//	 */
//	private String getSql(final String sqlGroup) {
//		return sqlCache.getJSONObject(sqlGroup).getString(SQL);
//	}
//
//	/**
//	 * converts a transaction index byte array into an integer.
//	 *
//	 * @param transactionIndexBa
//	 *            the byte array to use.
//	 * @return the integer index.
//	 */
//	private int getTransactionIndex(final byte[] transactionIndexBa) {
//		final UInt16 transactionIndexObj = new UInt16(transactionIndexBa);
//		final int transactionIndex = transactionIndexObj.asInt();
//		return transactionIndex;
//	}
//
//	/**
//	 * gets the inputs for each transaction in the block, and adds them to the
//	 * transaction.
//	 *
//	 * @param block
//	 *            the block to use
//	 * @param jdbcOperations
//	 *            the jdbc operations to use.
//	 * @param blockIndexBa
//	 *            the block index to use.
//	 */
//	private void getTransactionInputsWithIndex(final Block block, final JdbcOperations jdbcOperations,
//			final byte[] blockIndexBa) {
//		final Map<Integer, List<CoinReference>> inputsMap = getMapList(jdbcOperations,
//				"getTransactionInputsWithBlockIndex", new CoinReferenceMapToObject(), blockIndexBa);
//		for (final int txIx : inputsMap.keySet()) {
//			final List<CoinReference> inputs = inputsMap.get(txIx);
//
//			if (txIx >= block.getTransactionList().size()) {
//				throw new RuntimeException(
//						"txIx \"" + txIx + "\" exceeds txList.size \"" + block.getTransactionList().size()
//								+ "\" for block index \"" + block.getIndexAsLong() + "\" hash \"" + block.hash + "\"");
//			} else {
//				block.getTransactionList().get(txIx).inputs.addAll(inputs);
//			}
//		}
//	}
//
//	/**
//	 * gets the outputs for each transaction in the block, and adds them to the
//	 * transaction.
//	 *
//	 * @param block
//	 *            the block to use
//	 * @param jdbcOperations
//	 *            the jdbc operations to use.
//	 * @param blockIndexBa
//	 *            the block index to use.
//	 */
//	private void getTransactionOutputsWithIndex(final Block block, final JdbcOperations jdbcOperations,
//			final byte[] blockIndexBa) {
//		final Map<Integer, List<TransactionOutput>> outputsMap = getMapList(jdbcOperations,
//				"getTransactionOutputsWithBlockIndex", new TransactionOutputMapToObject(), blockIndexBa);
//		for (final int txIx : outputsMap.keySet()) {
//			final List<TransactionOutput> outputs = outputsMap.get(txIx);
//			block.getTransactionList().get(txIx).outputs.addAll(outputs);
//		}
//	}
//
//	/**
//	 * gets the scripts for each transaction in the block, and adds them to the
//	 * transaction.
//	 *
//	 * @param block
//	 *            the block to use
//	 * @param jdbcOperations
//	 *            the jdbc operations to use.
//	 * @param blockIndexBa
//	 *            the block index to use.
//	 */
//	private void getTransactionScriptsWithIndex(final Block block, final JdbcOperations jdbcOperations,
//			final byte[] blockIndexBa) {
//		final Map<Integer, List<Witness>> scriptsMap = getMapList(jdbcOperations, "getTransactionScriptsWithBlockIndex",
//				new WitnessMapToObject(), blockIndexBa);
//		for (final int txIx : scriptsMap.keySet()) {
//			final List<Witness> scripts = scriptsMap.get(txIx);
//			block.getTransactionList().get(txIx).scripts.addAll(scripts);
//		}
//	}
//
//	/**
//	 * return the block, with transactions added.
//	 *
//	 * @param block
//	 *            the block, to add transactions to.
//	 */
//	private void getTransactionsForBlock(final Block block) {
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getTransactionsWithIndex");
//		final byte[] blockIndexBa = block.index.toByteArray();
//		final List<byte[]> dataList = t.queryForList(sql, byte[].class, blockIndexBa);
//
//		for (final byte[] data : dataList) {
//			final Transaction transaction = new Transaction(ByteBuffer.wrap(data));
//			block.getTransactionList().add(transaction);
//		}
//
//		getTransactionOutputsWithIndex(block, t, blockIndexBa);
//
//		getTransactionInputsWithIndex(block, t, blockIndexBa);
//
//		getTransactionScriptsWithIndex(block, t, blockIndexBa);
//	}
//
//	@Override
//	public List<Transaction> getTransactionWithAccountList(final UInt160 account) {
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getTransactionWithAccountList");
//		final byte[] accountBa = account.toByteArray();
//		final List<byte[]> dataList = t.queryForList(sql, byte[].class, accountBa);
//
//		final List<Transaction> transactionList = new ArrayList<>();
//		for (final byte[] data : dataList) {
//			final Transaction transaction = new Transaction(ByteBuffer.wrap(data));
//			transactionList.add(transaction);
//		}
//		return transactionList;
//	}
//
//	@Override
//	public Transaction getTransactionWithHash(final UInt256 hash) {
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getTransactionWithHash");
//		final List<Map<String, Object>> dataList = t.queryForList(sql, hash.toByteArray());
//
//		if (dataList.isEmpty()) {
//			return null;
//		}
//
//		final Map<String, Object> data = dataList.get(0);
//
//		final byte[] blockIndexBa = (byte[]) data.get("block_index");
//		final byte[] transactionIndexBa = (byte[]) data.get(TRANSACTION_INDEX);
//		final byte[] transactionBa = (byte[]) data.get("transaction");
//		final int transactionIndex = getTransactionIndex(transactionIndexBa);
//
//		final Transaction transaction = new Transaction(ByteBuffer.wrap(transactionBa));
//
//		final Map<Integer, List<TransactionOutput>> outputsMap = getMapList(t,
//				"getTransactionOutputsWithBlockAndTransactionIndex", new TransactionOutputMapToObject(), blockIndexBa,
//				transactionIndexBa);
//		transaction.outputs.addAll(outputsMap.get(transactionIndex));
//
//		final Map<Integer, List<CoinReference>> inputsMap = getMapList(t,
//				"getTransactionInputsWithBlockAndTransactionIndex", new CoinReferenceMapToObject(), blockIndexBa,
//				transactionIndexBa);
//		transaction.inputs.addAll(inputsMap.get(transactionIndex));
//
//		final Map<Integer, List<Witness>> scriptsMap = getMapList(t,
//				"getTransactionScriptsWithBlockAndTransactionIndex", new WitnessMapToObject(), blockIndexBa,
//				transactionIndexBa);
//		transaction.scripts.addAll(scriptsMap.get(transactionIndex));
//
//		return transaction;
//	}
//
//	@Override
//	public Map<UInt256, Map<TransactionOutput, CoinReference>> getUnspentTransactionOutputListMap(
//			final UInt160 account) {
//		synchronized (this) {
//			if (closed) {
//				return null;
//			}
//		}
//		final JdbcTemplate t = new JdbcTemplate(ds);
//		final String sql = getSql("getUnspentTransactionOutputListMap");
//		final List<Map<String, Object>> mapList = t.queryForList(sql, account.toByteArray());
//
//		final AbstractMapToObject<TransactionOutput> toMapToObject = new TransactionOutputMapToObject();
//		final AbstractMapToObject<CoinReference> crMapToObject = new CoinReferenceMapToObject();
//
//		final Map<UInt256, Map<TransactionOutput, CoinReference>> assetIdTxoMap = new TreeMap<>();
//		for (final Map<String, Object> map : mapList) {
//			final TransactionOutput to = toMapToObject.toObject(map);
//			final CoinReference cr = crMapToObject.toObject(map);
//			if (!assetIdTxoMap.containsKey(to.assetId)) {
//				assetIdTxoMap.put(to.assetId, new TreeMap<>());
//			}
//			assetIdTxoMap.get(to.assetId).put(to, cr);
//		}
//
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
//		if (LOG.isInfoEnabled()) {
//			LOG.info("STARTED put, {} blocks", NumberFormat.getIntegerInstance().format(blocks.length));
//		}
//		final DataSourceTransactionManager tsMan = new DataSourceTransactionManager(ds);
//
//		final TransactionTemplate txTemplate = new TransactionTemplate(tsMan);
//		// set behavior
//		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		try {
//			txTemplate.execute(new TransactionCallback(blocks));
//		} catch (final DataAccessException e) {
//			if (LOG.isErrorEnabled()) {
//				LOG.error("data access exception", e);
//			}
//		}
//		if (LOG.isInfoEnabled()) {
//			LOG.info("SUCCESS put, {} blocks", NumberFormat.getIntegerInstance().format(blocks.length));
//		}
//	}
//
//	@Override
//	public void validate() {
//		LOG.info("STARTED validate");
//
//		final Block block0 = getBlock(0, false);
//		if (!block0.hash.equals(GenesisBlockUtil.GENESIS_HASH)) {
//			throw new RuntimeException("height 0 block hash \"" + block0.hash.toHexString()
//					+ "\" does not match genesis block hash \"" + GenesisBlockUtil.GENESIS_HASH.toHexString() + "\".");
//		}
//
//		long lastInfoMs = System.currentTimeMillis();
//
//		long validBlockHeight = 1;
//		final long maxBlockCount = getBlockCount();
//		while (validBlockHeight < maxBlockCount) {
//			LOG.debug("INTERIM DEBUG validate {} of {} STARTED ", validBlockHeight, maxBlockCount);
//			final Block block = getBlock(validBlockHeight, false);
//			if (block == null) {
//				LOG.error("INTERIM validate {} of {} FAILURE, block not found in blockchain.", validBlockHeight,
//						maxBlockCount);
//			} else if (!containsBlockWithHash(block.prevHash)) {
//				LOG.error("INTERIM validate {} of {} FAILURE, prevHash {} not found in blockchain.", validBlockHeight,
//						maxBlockCount, block.prevHash.toHexString());
//				deleteBlockAtHeight(validBlockHeight);
//			} else if (block.getIndexAsLong() != validBlockHeight) {
//				LOG.error("INTERIM validate {} of {} FAILURE, indexAsLong {} does not match blockchain.",
//						validBlockHeight, maxBlockCount, block.getIndexAsLong());
//				deleteBlockAtHeight(validBlockHeight);
//			} else {
//				if (System.currentTimeMillis() > (lastInfoMs + 1000)) {
//					LOG.info("INTERIM INFO  validate {} of {} SUCCESS ", validBlockHeight, maxBlockCount);
//					lastInfoMs = System.currentTimeMillis();
//				} else {
//					LOG.debug("INTERIM DEBUG validate {} of {} SUCCESS ", validBlockHeight, maxBlockCount);
//				}
//			}
//			validBlockHeight++;
//		}
//
//		LOG.info("SUCCESS validate");
//	}
//
//	/**
//	 * the transaction callback class.
//	 *
//	 * @author coranos
//	 *
//	 */
//	private final class TransactionCallback extends TransactionCallbackWithoutResult {
//
//		/**
//		 * the list of blocks to update.
//		 */
//		private final Block[] blocks;
//
//		/**
//		 * the constructor.
//		 *
//		 * @param blocks
//		 *            the list of blocks.
//		 */
//		private TransactionCallback(final Block... blocks) {
//			this.blocks = blocks;
//		}
//
//		@Override
//		public void doInTransactionWithoutResult(final TransactionStatus status) {
//
//			final JdbcTemplate t = new JdbcTemplate(ds);
//
//			for (final Block block : blocks) {
//				final byte[] prevHashBa = block.prevHash.toByteArray();
//				ArrayUtils.reverse(prevHashBa);
//
//				final String putBlockSql = getSql("putBlock");
//				final byte[] blockIndexBa = block.index.toByteArray();
//				t.update(putBlockSql, block.hash.toByteArray(), prevHashBa, blockIndexBa, block.toHeaderByteArray());
//
//				final String putTransactionSql = getSql("putTransaction");
//				final String putTransactionInputSql = getSql("putTransactionInput");
//				final String putTransactionOutputSql = getSql("putTransactionOutput");
//				final String putTransactionScriptSql = getSql("putTransactionScript");
//				int transactionIndex = 0;
//
//				final List<Object[]> putTransactionList = new ArrayList<>();
//				final List<Object[]> putTransactionInputList = new ArrayList<>();
//				final List<Object[]> putTransactionOutputList = new ArrayList<>();
//				final List<Object[]> putTransactionScriptList = new ArrayList<>();
//
//				for (final Transaction transaction : block.getTransactionList()) {
//					final byte[] txIxByte = new UInt16(transactionIndex).toByteArray();
//					final byte[] transactionBaseBa = transaction.toBaseByteArray();
//					add(putTransactionList, blockIndexBa, txIxByte, transaction.getHash().toByteArray(),
//							transactionBaseBa);
//
//					for (int inputIx = 0; inputIx < transaction.inputs.size(); inputIx++) {
//						final byte[] txInputIxByte = new UInt32(inputIx).toByteArray();
//						final CoinReference input = transaction.inputs.get(inputIx);
//						add(putTransactionInputList, blockIndexBa, txIxByte, txInputIxByte,
//								input.prevHash.toByteArray(), input.prevIndex.toByteArray());
//					}
//
//					for (int outputIx = 0; outputIx < transaction.outputs.size(); outputIx++) {
//						final byte[] txOutputIxByte = new UInt16(outputIx).toByteArray();
//						final TransactionOutput output = transaction.outputs.get(outputIx);
//						add(putTransactionOutputList, blockIndexBa, txIxByte, txOutputIxByte,
//								output.assetId.toByteArray(), output.value.toByteArray(),
//								output.scriptHash.toByteArray());
//					}
//
//					for (int scriptIx = 0; scriptIx < transaction.scripts.size(); scriptIx++) {
//						final byte[] txScriptIxByte = new UInt32(scriptIx).toByteArray();
//						final Witness script = transaction.scripts.get(scriptIx);
//						add(putTransactionScriptList, blockIndexBa, txIxByte, txScriptIxByte,
//								script.getCopyOfInvocationScript(), script.getCopyOfVerificationScript());
//					}
//
//					transactionIndex++;
//				}
//
//				t.batchUpdate(putTransactionSql, putTransactionList);
//				t.batchUpdate(putTransactionInputSql, putTransactionInputList);
//				t.batchUpdate(putTransactionOutputSql, putTransactionOutputList);
//				t.batchUpdate(putTransactionScriptSql, putTransactionScriptList);
//			}
//		}
//	}
//}

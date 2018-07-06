//package com.stratagile.qlink.utils.txutils.model.db;
//
//import com.stratagile.qlink.utils.txutils.model.bytes.Fixed8;
//import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
//import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
//import com.stratagile.qlink.utils.txutils.model.core.Block;
//import com.stratagile.qlink.utils.txutils.model.core.CoinReference;
//import com.stratagile.qlink.utils.txutils.model.core.Transaction;
//import com.stratagile.qlink.utils.txutils.model.core.TransactionOutput;
//
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//
///**
// * the performance monitoring class.
// *
// * @author coranos
// *
// */
//public final class PerformanceMonitoringBlockDb implements BlockDb {
//
//	/**
//	 * the delegate.
//	 */
//	private final BlockDb delegate;
//
//	/**
//	 * the constructor.
//	 *
//	 * @param config
//	 *            the configuration to use.
//	 */
//	public PerformanceMonitoringBlockDb(final JSONObject config) {
//		delegate = new ReadCacheBlockDBImpl(config);
//	}
//
//	@Override
//	public void close() {
//		delegate.close();
//	}
//
//	@Override
//	public boolean containsBlockWithHash(final UInt256 hash) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.containsBlockWithHash")) {
//			return delegate.containsBlockWithHash(hash);
//		}
//	}
//
//	@Override
//	public void deleteHighestBlock() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.deleteHighestBlock")) {
//			delegate.deleteHighestBlock();
//		}
//	}
//
//	@Override
//	public Map<UInt160, Map<UInt256, Fixed8>> getAccountAssetValueMap() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getAccountAssetValueMap")) {
//			return delegate.getAccountAssetValueMap();
//		}
//	}
//
//	@Override
//	public long getAccountCount() {
//		return delegate.getAccountCount();
//	}
//
//	@Override
//	public Map<UInt256, Fixed8> getAssetValueMap(final UInt160 account) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getAssetValueMap")) {
//			return delegate.getAssetValueMap(account);
//		}
//	}
//
//	@Override
//	public long getBlockCount() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getBlockCount")) {
//			return delegate.getBlockCount();
//		}
//	}
//
//	@Override
//	public Long getBlockIndexFromTransactionHash(final UInt256 hash) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getBlockIndexFromTransactionHash")) {
//			return delegate.getBlockIndexFromTransactionHash(hash);
//		}
//	}
//
//	@Override
//	public long getFileSize() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getFileSize")) {
//			return delegate.getFileSize();
//		}
//	}
//
//	@Override
//	public Block getFullBlockFromHash(final UInt256 hash) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getFullBlockFromHash")) {
//			return delegate.getFullBlockFromHash(hash);
//		}
//	}
//
//	@Override
//	public Block getFullBlockFromHeight(final long blockHeight) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getFullBlockFromHeight")) {
//			return delegate.getFullBlockFromHeight(blockHeight);
//		}
//	}
//
//	@Override
//	public Block getHeaderOfBlockFromHash(final UInt256 hash) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getHeaderOfBlockFromHash")) {
//			return delegate.getHeaderOfBlockFromHash(hash);
//		}
//	}
//
//	@Override
//	public Block getHeaderOfBlockFromHeight(final long blockHeight) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getHeaderOfBlockFromHeight")) {
//			return delegate.getHeaderOfBlockFromHeight(blockHeight);
//		}
//	}
//
//	@Override
//	public Block getHeaderOfBlockWithMaxIndex() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getHeaderOfBlockWithMaxIndex")) {
//			return delegate.getHeaderOfBlockWithMaxIndex();
//		}
//	}
//
//	@Override
//	public List<Transaction> getTransactionWithAccountList(final UInt160 account) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getTransactionWithAccountList")) {
//			return delegate.getTransactionWithAccountList(account);
//		}
//	}
//
//	@Override
//	public Transaction getTransactionWithHash(final UInt256 hash) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getTransactionWithHash")) {
//			return delegate.getTransactionWithHash(hash);
//		}
//	}
//
//	@Override
//	public Map<UInt256, Map<TransactionOutput, CoinReference>> getUnspentTransactionOutputListMap(
//			final UInt160 account) {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.getUnspentTransactionOutputListMap")) {
//			return delegate.getUnspentTransactionOutputListMap(account);
//		}
//	}
//
//	@Override
//	public void put(final boolean forceSynch, final Block... blocks) {
//		try (PerformanceMonitor m1 = new PerformanceMonitor("BlockDb.put")) {
//			try (PerformanceMonitor m2 = new PerformanceMonitor("BlockDb.put[PerBlock]", blocks.length)) {
//				delegate.put(forceSynch, blocks);
//			}
//		}
//	}
//
//	@Override
//	public void validate() {
//		try (PerformanceMonitor m = new PerformanceMonitor("BlockDb.validate")) {
//			delegate.validate();
//		}
//	}
//}

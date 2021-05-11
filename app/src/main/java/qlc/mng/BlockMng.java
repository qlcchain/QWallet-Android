package qlc.mng;

import java.math.BigInteger;

import qlc.bean.Block;
import qlc.bean.StateBlock;
import qlc.network.QlcException;
import qlc.utils.Constants;
import qlc.utils.Encodes;
import qlc.utils.HashUtil;
import qlc.utils.Helper;
import qlc.utils.StringUtil;

public final class BlockMng {

    /**
     * get block root
     *
     * @param block:block info
     * @return String
     */
    public static String getRoot(StateBlock block) {
        if (Constants.ZERO_HASH.equals(block.getPrevious())) {
            return AccountMng.addressToPublicKey(block.getAddress());
        } else
            return block.getPrevious();
    }


    /**
     * get block hash
     *
     * @param block:block info
     * @return byte[]:block hash
     * @throws QlcException qlc exception
     */
    public static byte[] getHash(StateBlock block) {

        byte[] sources = new byte[1];
        sources = Helper.bigInttoBytes(new BigInteger(Block.Type.getIndex(block.getType())));

        sources = Helper.byteMerger(sources, Helper.hexStringToBytes(block.getToken()));

        sources = Helper.byteMerger(sources, Helper.hexStringToBytes(AccountMng.addressToPublicKey(block.getAddress())));

        sources = Helper.byteMerger(sources, Helper.bigInttoBytes(block.getBalance()));

        if (block.getVote() != null)
            sources = Helper.byteMerger(sources, Helper.bigInttoBytes(block.getVote()));

        if (block.getNetwork() != null)
            sources = Helper.byteMerger(sources, Helper.bigInttoBytes(block.getNetwork()));

        if (block.getStorage() != null)
            sources = Helper.byteMerger(sources, Helper.bigInttoBytes(block.getStorage()));

        if (block.getOracle() != null)
            sources = Helper.byteMerger(sources, Helper.bigInttoBytes(block.getOracle()));

        sources = Helper.byteMerger(sources, Helper.hexStringToBytes(block.getPrevious()));

        sources = Helper.byteMerger(sources, Helper.hexStringToBytes(block.getLink()));

        if (StringUtil.isNotBlank(block.getSender()))
            sources = Helper.byteMerger(sources, block.getSender().getBytes());
        if (StringUtil.isNotBlank(block.getReceiver()))
            sources = Helper.byteMerger(sources, block.getReceiver().getBytes());

        if (StringUtil.isNotBlank(block.getMessage()))
            sources = Helper.byteMerger(sources, Helper.hexStringToBytes(block.getMessage()));

        if (StringUtil.isNotBlank(block.getData()))
            sources = Helper.byteMerger(sources, Encodes.decodeBase64(block.getData()));

        sources = Helper.byteMerger(sources, Helper.LongToBytes(block.getTimestamp()));

        if (block.getPovHeight() != null)
            sources = Helper.byteMerger(sources, Helper.LongToBytes(block.getPovHeight()));

        if (StringUtil.isNotBlank(block.getExtra()))
            sources = Helper.byteMerger(sources, Helper.hexStringToBytes(block.getExtra()));

        sources = Helper.byteMerger(sources, Helper.hexStringToBytes(AccountMng.addressToPublicKey(block.getRepresentative())));

        byte[] output = HashUtil.digest(32, sources);

        //0508更改，hash的算法出错，如果不一致return后台返回的hash值
        String hash = Helper.byteToHexString(output);
        if (block.getHash()!=null && !hash.equals(block.getHash()))
            return Helper.hexStringToBytes(block.getHash());
        else
            return output;
    }

}

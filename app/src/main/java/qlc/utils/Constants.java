package qlc.utils;

import java.math.BigInteger;

public class Constants {

	// exception code
	public static final int EXCEPTION_CODE_1000 = 1000;
	public static final String EXCEPTION_MSG_1000 = "Not enought balance";
	
	public static final int EXCEPTION_CODE_1001 = 1001;
	public static final String EXCEPTION_MSG_1001 = "Seed can`t be empty";
	
	public static final int EXCEPTION_CODE_1002 = 1002;
	public static final String EXCEPTION_MSG_1002 = "Public key can`t be empty";
	
	public static final int EXCEPTION_CODE_1003 = 1003;	
	public static final String EXCEPTION_MSG_1003 = "Address can`t be empty";
	
	public static final int EXCEPTION_CODE_1004 = 1004;	
	public static final String EXCEPTION_MSG_1004 = "Address format error";
	
	public static final int EXCEPTION_CODE_1005 = 1005;	
	public static final String EXCEPTION_MSG_1005 = "Signature verification failed";
	
	public static final int EXCEPTION_CODE_1006 = 1006;	
	public static final String EXCEPTION_MSG_1006 = "Seed generation not supported";
	
	public static final int EXCEPTION_CODE_1007 = 1007;
	public static final String EXCEPTION_MSG_1007 = "Mnemonics can`t be empty";
	
	
	public static final int EXCEPTION_BLOCK_CODE_2000 = 2000;	
	public static final String EXCEPTION_BLOCK_MSG_2000 = "Parameter error for send block";
	
	public static final int EXCEPTION_BLOCK_CODE_2001 = 2001;	
	public static final String EXCEPTION_BLOCK_MSG_2001 = "Parameter error for receive block";
	
	public static final int EXCEPTION_BLOCK_CODE_2002 = 2002;	
	public static final String EXCEPTION_BLOCK_MSG_2002 = "The block is not send block";
	
	public static final int EXCEPTION_BLOCK_CODE_2003 = 2003;	
	public static final String EXCEPTION_BLOCK_MSG_2003 = "Send block does not exist";
	
	public static final int EXCEPTION_BLOCK_CODE_2004 = 2004;	
	public static final String EXCEPTION_BLOCK_MSG_2004 = "Address is mismatch private key";
	
	public static final int EXCEPTION_BLOCK_CODE_2005 = 2005;	
	public static final String EXCEPTION_BLOCK_MSG_2005 = "Pending not found";
	
	public static final int EXCEPTION_BLOCK_CODE_2006 = 2006;	
	public static final String EXCEPTION_BLOCK_MSG_2006 = "Invalid representative";
	
	public static final int EXCEPTION_BLOCK_CODE_2007 = 2007;	
	public static final String EXCEPTION_BLOCK_MSG_2007 = "Account is not exist";
	
	public static final int EXCEPTION_BLOCK_CODE_2008 = 2008;	
	public static final String EXCEPTION_BLOCK_MSG_2008 = "Account has no chain token";
	
	public static final int EXCEPTION_BLOCK_CODE_2009 = 2009;	
	public static final String EXCEPTION_BLOCK_MSG_2009 = "Token header block not found";
	
	public static final int EXCEPTION_BLOCK_CODE_2010 = 2010;	
	public static final String EXCEPTION_BLOCK_MSG_2010 = "Parameter error for change block";
	
	// system code
	public static final int EXCEPTION_SYS_CODE_3000 = 3000;	
	public static final String EXCEPTION_SYS_MSG_3000 = "Need initialize qlc client";
	
	public static final int EXCEPTION_SYS_CODE_3001 = 3001;	
	public static final String EXCEPTION_SYS_MSG_3001 = "Node url can`t be empty";
	
	public static final int EXCEPTION_SYS_CODE_3002 = 3002;	
	public static final String EXCEPTION_SYS_MSG_3002 = "Error node url";
	
	public static final int EXCEPTION_SYS_CODE_3003 = 3003;	
	public static final String EXCEPTION_SYS_MSG_3003 = "Invalid node url";
	
	public static final int EXCEPTION_PUBLISH_BLOCK_CODE_4001 = 4001;	
	public static final String EXCEPTION_PUBLISH_BLOCK_MSG_4001 = "Parameter error for publish block";
	
	// block type
	public static final String BLOCK_TYPE_OPEN = "Open";
	public static final String BLOCK_TYPE_SEND = "Send";
	public static final String BLOCK_TYPE_RECEIVE = "Receive";
	public static final String BLOCK_TYPE_CHANGE = "Change";
	public static final String BLOCK_TYPE_CONTRACTSEND = "ContractSend";
	public static final String BLOCK_TYPE_CONTRACTREWARD = "ContractReward";
	
	// block parameter default value
	public static final String ZERO_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
	public static final BigInteger ZERO_BIG_INTEGER = new BigInteger("0");
	public static final Long ZERO_LONG = 0l;

	// websocket close state
	public static final int NORMAL_CLOSURE_STATUS = 1000;
	
	// url start
	public static final String URL_START_HTTP = "http";
	public static final String URL_START_WEB_SOCKET = "ws";
	
	// link type
	public static final String LINNK_TYPE_AIRDORP = "d614bb9d5e20ad063316ce091148e77c99136c6194d55c7ecc7ffa9620dbcaeb";
	
	
}

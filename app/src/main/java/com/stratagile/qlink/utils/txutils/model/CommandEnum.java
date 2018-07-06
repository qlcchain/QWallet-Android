package com.stratagile.qlink.utils.txutils.model;

/**
 * an enumeration of all the commands that can be sent over the network.
 *
 * @author coranos
 *
 */
public enum CommandEnum {
	/** version. */
	VERSION("version"),
	/** block response. */
	BLOCK("block"),
	/** address response. */
	ADDR("addr"),
	/** inventory. */
	INV("inv"),
	/** version acknowledgement. */
	VERACK("verack"),
	/** get blocks. */
	GETBLOCKS("getblocks"),
	/** get blocks. */
	GETHEADERS("getheaders"),
	/** get addresses. */
	HEADERS("headers"),
	/** get addresses. */
	GETADDR("getaddr"),
	/** get mempool. */
	MEMPOOL("mempool"),
	/** get getdata. */
	GETDATA("getdata"),
	/** tx response. */
	TX("tx"),
	/** consensus response. */
	CONSENSUS("consensus"),

	/** trailing semicolon */
	;

	/**
	 * returns a command with th given name.
	 *
	 * @param name
	 *            the name to look up.
	 *
	 * @return the command with the given name, or null if no command exists.
	 */
	public static CommandEnum fromName(final String name) {
		for (final CommandEnum command : values()) {
			if (command.name.equals(name)) {
				return command;
			}
		}
		return null;
	}

	/**
	 * the command name.
	 */
	private final String name;

	/**
	 * the constructor.
	 *
	 * @param name
	 *            the name to use.
	 */
	CommandEnum(final String name) {
		this.name = name;
	}

	/**
	 * returns the name.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
}

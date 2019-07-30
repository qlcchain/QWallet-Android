package com.stratagile.qlink.data

/**
 * Created by drei on 11/24/17.
 */
data class TransactionHistoryEntry(val GAS: Double,
                                   val NEO: Double,
                                   val block_index: Int,
                                   val gas_sent: Boolean,
                                   val neo_sent: Boolean,
                                   val txid: String
                                   )

data class TransactionHistory(val address: String,
                              val history: Array<TransactionHistoryEntry>,
                              val name: String,
                              val net: String)

data class Claim(val claim: Int,
                 val end: Int,
                 val index: Int,
                 val start: Int,
                 val sysfee: Int,
                 val txid: String,
                 val value: Int)

data class Claims(val address: String,
                  val claims: Array<Claim>,
                  val net: String,
                  val total_claim: Int,
                  val total_unspent_claim: Int)



data class Assets(
		val GAS: GAS,
		val NEO: NEO,
		val address: String, //AKcm7eABuW1Pjb5HsTwiq7iARSatim9tQ6
		val net: String //MainNet
)

data class UTXOS(var data: Array<UTXO>)

data class UTXO(val asset: String, val index: Int, val txid: String, val value: String,  val createdAtBlock: Int)

data class GAS(
		val balance: Double, //1.7666314800000003
		val unspent: List<Unspent>
)

data class Unspent(
		val index: Int, //0
		val txid: String, //3f1e7ddd63eee2f4836aa7ce8505be41af1cdc02e93fdcea8150c3f64c140f68
		val value: Double //0.02024064
)

data class NEO(
		val balance: Int, //235
		val unspent: List<Unspent>
)


package com.stratagile.qlink.utils

import com.stratagile.qlink.entity.AllWallet

object OtcUtils {
    fun parseChain(chain : String): AllWallet.WalletType{
        when(chain) {
            "QLC_CHAIN" -> {
                return AllWallet.WalletType.QlcWallet
            }
            "ETH_CHAIN" -> {
                return AllWallet.WalletType.EthWallet
            }
            "NEO_CHAIN" -> {
                return AllWallet.WalletType.NeoWallet
            }
            "EOS_CHAIN" -> {
                return AllWallet.WalletType.EosWallet
            }
        }
        return AllWallet.WalletType.NullWallet
    }
    fun parseChain(chain : Int): AllWallet.WalletType{
        when(chain) {
            1 -> {
                return AllWallet.WalletType.QlcWallet
            }
            2 -> {
                return AllWallet.WalletType.EthWallet
            }
            3 -> {
                return AllWallet.WalletType.NeoWallet
            }
            4 -> {
                return AllWallet.WalletType.EosWallet
            }
        }
        return AllWallet.WalletType.NullWallet
    }
}
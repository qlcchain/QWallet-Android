package com.stratagile.qlink.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.stratagile.qlink.constant.ConstantValue
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

    fun gotoBlockBrowser(context: Context, chain: String, hash : String) {
        var urlPre = "https://explorer.qlcchain.org/transaction/"
        when(chain) {
            "QLC_CHAIN" -> {
                urlPre = "https://explorer.qlcchain.org/transaction/"
            }
            "ETH_CHAIN" -> {
                if (SpUtil.getBoolean(context, ConstantValue.isMainNet, true)) {
                    urlPre = "https://etherscan.io/tx/"
                } else {
                    urlPre = "https://rinkeby.etherscan.io/tx/"
                }
            }
            "NEO_CHAIN" -> {
                if (SpUtil.getBoolean(context, ConstantValue.isMainNet, true)) {
                    urlPre = "https://neoscan.io/transaction/"
                } else {
                    urlPre = "https://neoscan-testnet.io/transaction/"
                }
            }
            "EOS_CHAIN" -> {
                urlPre = "https://eosflare.io/tx/"
            }
        }
        val intent1 = Intent()
        intent1.action = "android.intent.action.VIEW"
        intent1.data = Uri.parse(urlPre + hash)
        context.startActivity(intent1)
    }
}
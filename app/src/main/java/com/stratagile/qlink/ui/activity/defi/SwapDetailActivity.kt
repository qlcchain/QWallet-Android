package com.stratagile.qlink.ui.activity.defi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.ui.activity.defi.component.DaggerSwapDetailComponent
import com.stratagile.qlink.ui.activity.defi.contract.SwapDetailContract
import com.stratagile.qlink.ui.activity.defi.module.SwapDetailModule
import com.stratagile.qlink.ui.activity.defi.presenter.SwapDetailPresenter
import com.stratagile.qlink.utils.OtcUtils
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_swap_detail.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/08/25 11:39:23
 */

class SwapDetailActivity : BaseActivity(), SwapDetailContract.View {

    @Inject
    internal lateinit var mPresenter: SwapDetailPresenter
    lateinit var swapRecord: SwapRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_swap_detail)
    }
    override fun initData() {
        title.text = getString(R.string.record_swap)
        swapRecord = intent.getParcelableExtra("swapRecord")
        tvSwapAmount.text = swapRecord.amount.toString()
        tvFrom.text = swapRecord.fromAddress
        tvTo.text = swapRecord.toAddress

        tvFrom.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvFrom.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }

        tvTo.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvTo.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvHash.text = swapRecord.rHash
        tvHash.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvHash.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }

        tvTime.text = TimeUtil.getOrderTime(swapRecord.lockTime)
        if (swapRecord.type == 1) {
            //nep5 -> erc20
            /**
             * // deposit
            DepositInit LockerState = iota
            DepositNeoLockedDone
            DepositEthLockedPending
            DepositEthLockedDone
            DepositEthUnLockedDone
            DepositNeoUnLockedPending
            //6
            DepositNeoUnLockedDone
            //7抵押超时，wrapper正在销毁eth
            DepositEthFetchPending
            //8抵押超时，wrapper销毁eth完成
            DepositEthFetchDone
            //9抵押超时，neo退回完成
            DepositNeoFetchDone

            Failed
            Invalid
             */
            ivTxid1.setImageResource(R.mipmap.icons_neo_wallet)
            txId1.text = swapRecord.txHash
            txId1.setOnClickListener {
                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.txHash)
            }
            tvSwapType.text = getString(R.string.nep5_token_erc20_token)
            when(swapRecord.state) {
                0 -> {
                    if (swapRecord.fail) {
                        tvSwapState.text = getString(R.string.failed)
                    } else {
                        tvSwapState.text = getString(R.string.waiting_for_confirm)
                    }
                }
                1 -> {
                    tvSwapState.text = getString(R.string.pending)
                }
                2 -> {
                    tvSwapState.text = getString(R.string.pending)
                }
                3 -> {
                    tvSwapState.text = getString(R.string.waiting_for_claim)
                }
                4 -> {
                    if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                        tvSwapState.text = getString(R.string.waiting_for_claim)
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                        txId2.text = swapRecord.swaptxHash
                        ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                        ivTxid2.setOnClickListener {
                            OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                        }
                    }
                }
                5 -> {
                    tvSwapState.text = getString(R.string.pending)
                    txId2.text = swapRecord.swaptxHash
                    ivTxid2.setImageResource(R.mipmap.icons_eth_wallet)
                    txId2.setOnClickListener {
                        OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.swaptxHash)
                    }
                }
                6 -> {
                    tvSwapState.text = getString(R.string.completed)
                    txId2.text = swapRecord.swaptxHash
                    ivTxid2.setImageResource(R.mipmap.icons_eth_wallet)
                    ivTxid2.setOnClickListener {
                        OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.swaptxHash)
                    }
                }
                7 -> {
                    if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                        tvSwapState.text = getString(R.string.expired)
                    } else {
                        tvSwapState.text = getString(R.string.revoked)
                        txId2.text = swapRecord.swaptxHash
                        ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                        ivTxid2.setOnClickListener {
                            OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                        }

                    }
                }
                8 -> {
                    if (swapRecord.neoTimeout) {
                        if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                            tvSwapState.text = getString(R.string.expired)
                        } else {
                            tvSwapState.text = getString(R.string.revoked)
                            txId2.text = swapRecord.swaptxHash
                            ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                            ivTxid2.setOnClickListener {
                                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                            }

                        }
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                    }
                }
                9 -> {
                    if (swapRecord.neoTimeout) {
                        if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                            tvSwapState.text = getString(R.string.expired)
                        } else {
                            tvSwapState.text = getString(R.string.revoked)
                            txId2.text = swapRecord.swaptxHash
                            ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                            ivTxid2.setOnClickListener {
                                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                            }

                        }
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                    }
                }
            }
        } else {
            /**
             *  // withdraw
            //10 eth交易确认
            WithDrawEthLockedDone
            11 locknep5中
            WithDrawNeoLockedPending
            12 nep5lock成功，可以开始赎回
            WithDrawNeoLockedDone
            13 nep5赎回成功，app端作为成功状态
            WithDrawNeoUnLockedDone
            14 wrapper 开始拿走erc20
            WithDrawEthUnlockPending
            15 wrapper 成功拿走erc20
            WithDrawEthUnlockDone
            16 赎回超时，wrapper开始取回nep5
            WithDrawNeoFetchPending
            17 赎回超时，wrapper成功取回nep5
            WithDrawNeoFetchDone
            18 赎回超时，用户取回erc20成功
            WithDrawEthFetchDone

            Failed
            Invalid
             */
            ivTxid1.setImageResource(R.mipmap.icons_eth_wallet)
            txId1.text = swapRecord.txHash
            txId1.setOnClickListener {
                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.txHash)
            }
            tvSwapType.text = getString(R.string.qlc_erc20_token_nep5_token)
            when(swapRecord.state) {
                -2 -> {
                    tvSwapState.text = getString(R.string.failed)
                }
                -1 -> {
                    tvSwapState.text = getString(R.string.waiting_for_confirm)
                }
                10 -> {
                    tvSwapState.text = getString(R.string.pending)
                }
                11 -> {
                    tvSwapState.text = getString(R.string.pending)
                }
                12 -> {
                    tvSwapState.text = getString(R.string.claim)
                }
                13 -> {
                    tvSwapState.text = getString(R.string.completed)
                    txId2.text = swapRecord.swaptxHash
                    ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                    txId2.setOnClickListener {
                        OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                    }
                }
                14 -> {
                    tvSwapState.text = getString(R.string.completed)
                    txId2.text = swapRecord.swaptxHash
                    ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                    txId2.setOnClickListener {
                        OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                    }
                }
                15 -> {
                    tvSwapState.text = getString(R.string.completed)
                    txId2.text = swapRecord.swaptxHash
                    ivTxid2.setImageResource(R.mipmap.icons_neo_wallet)
                    txId2.setOnClickListener {
                        OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "NEO_CHAIN", swapRecord.swaptxHash)
                    }
                }
                16 -> {
                    if (swapRecord.ethTimeout) {
                        if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                            tvSwapState.text = getString(R.string.expired)
                        } else {
                            tvSwapState.text = getString(R.string.revoked)
                            txId2.text = swapRecord.swaptxHash
                            ivTxid2.setImageResource(R.mipmap.icons_eth_wallet)
                            txId2.setOnClickListener {
                                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.swaptxHash)
                            }
                        }
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                    }
                }
                17 -> {
                    if (swapRecord.ethTimeout) {
                        if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                            tvSwapState.text = getString(R.string.expired)
                        } else {
                            tvSwapState.text = getString(R.string.revoked)
                            txId2.text = swapRecord.swaptxHash
                            ivTxid2.setImageResource(R.mipmap.icons_eth_wallet)
                            txId2.setOnClickListener {
                                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.swaptxHash)
                            }
                        }
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                    }
                }
                18 -> {
                    if (swapRecord.ethTimeout) {
                        if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                            tvSwapState.text = getString(R.string.expired)
                        } else {
                            tvSwapState.text = getString(R.string.revoked)
                            txId2.text = swapRecord.swaptxHash
                            ivTxid2.setImageResource(R.mipmap.icons_eth_wallet)
                            txId2.setOnClickListener {
                                OtcUtils.gotoBlockBrowser(SwapDetailActivity@this, "ETH_CHAIN", swapRecord.swaptxHash)
                            }
                        }
                    } else {
                        tvSwapState.text = getString(R.string.pending)
                    }
                }
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerSwapDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .swapDetailModule(SwapDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SwapDetailContract.SwapDetailContractPresenter) {
            mPresenter = presenter as SwapDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
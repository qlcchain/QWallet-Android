package com.stratagile.qlink.ui.adapter.defi

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_swap_detail.*

class SwapListAdapter(array: ArrayList<SwapRecord>) : BaseQuickAdapter<SwapRecord, BaseViewHolder>(R.layout.item_swap_list, array) {
    override fun convert(helper: BaseViewHolder, item: SwapRecord) {
        helper.setText(R.id.tvLockTime, TimeUtil.getOrderTime(item.lockTime))
        helper.setText(R.id.qlcAmount, item.amount.toString())
        helper.setVisible(R.id.tvOperator, false)
        // 1为nep5 --> erc20, 2为erc20 --> nep5
        if (item.type == 1) {
            //nep5 -> erc20
            /**
             * // deposit
            DepositInit LockerState = iota
            DepositNeoLockedDone
            DepositEthLockedPending
            //3 可以开始领取
            DepositEthLockedDone
            //
            DepositEthUnLockedDone
            // 5
            DepositNeoUnLockedPending
            //6
            DepositNeoUnLockedDone
            //7抵押超时，wrapper正在销毁eth
            DepositEthFetchPending
            //8抵押超时，wrapper销毁eth完成
            DepositEthFetchDone
            //9抵押超时，nep5退回完成
            DepositNeoFetchDone

            Failed
            Invalid
             */
            helper.setText(R.id.tvSwapType, mContext.resources.getString(R.string.nep5_token_erc20_token))
            when(item.state) {
                -1 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_confirm))
                    helper.setVisible(R.id.tvOperator, true)
                    helper.setText(R.id.tvOperator, mContext.getString(R.string.confirm))
                }
                SwapRecord.SwapState.DepositInit.ordinal -> {
                    if (item.fail) {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.failed))
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
                SwapRecord.SwapState.DepositNeoLockedDone.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                SwapRecord.SwapState.DepositEthLockedPending.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                SwapRecord.SwapState.DepositEthLockedDone.ordinal -> {
                    if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_claim))
                        helper.setVisible(R.id.tvOperator, true)
                        helper.setText(R.id.tvOperator, mContext.getString(R.string.claim_swap))
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.claiming))
                    }
                }

                SwapRecord.SwapState.DepositEthUnLockedDone.ordinal -> {
                    if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_claim))
                        helper.setVisible(R.id.tvOperator, true)
                        helper.setText(R.id.tvOperator, mContext.getString(R.string.claim_swap))
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.claiming))
                    }
                }
                //5
                5 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                6 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.completed))
                }
                7 -> {
                    if (item.neoTimeout && item.ethTimeout) {
                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        } else {
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoked))
                        }
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
                8 -> {
                    if (item.neoTimeout && item.ethTimeout) {
                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        } else {
//                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoked))
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        }
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
                9 -> {
                    if (item.neoTimeout && item.ethTimeout) {
                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        } else {
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoked))
                        }
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
                10 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoked))
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
            helper.setText(R.id.tvSwapType, mContext.resources.getString(R.string.qlc_erc20_token_nep5_token))
            when(item.state) {
                -2 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.failed))
                }
                -1 -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_confirm))
                }
                //11
                SwapRecord.SwapState.WithDrawInit.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                SwapRecord.SwapState.WithDrawEthLockedDone.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                //13
                SwapRecord.SwapState.WithDrawNeoLockedPending.ordinal -> {
                    helper.setVisible(R.id.tvOperator, true)
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_claim))
                    helper.setText(R.id.tvOperator, mContext.getString(R.string.claim_swap))
                }
                //14
                SwapRecord.SwapState.WithDrawNeoLockedDone.ordinal -> {
                    helper.setVisible(R.id.tvOperator, true)
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.waiting_for_claim))
                    helper.setText(R.id.tvOperator, mContext.getString(R.string.claim_swap))
                }
                //15
                SwapRecord.SwapState.WithDrawNeoUnLockedPending.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.completed))
                }
                //16
                SwapRecord.SwapState.WithDrawNeoUnLockedDone.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.completed))
                }
                //17
                SwapRecord.SwapState.WithDrawEthUnlockPending.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.completed))
                }
                //18
                SwapRecord.SwapState.WithDrawEthUnlockDone.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.completed))
//                    if (item.ethTimeout) {
//                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
//                            helper.setVisible(R.id.tvOperator, true)
//                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
//                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
//                        } else {
//                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoking))
//
//                        }
//                    } else {
//                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
//                    }
                }
                //19
                SwapRecord.SwapState.WithDrawNeoFetchPending.ordinal -> {
                    helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                }
                //20
                SwapRecord.SwapState.WithDrawNeoFetchDone.ordinal -> {
                    if (item.ethTimeout && item.neoTimeout) {
                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        } else {
                            if (!item.swaptxHash.startsWith("0x")) {
                                helper.setVisible(R.id.tvOperator, true)
                                helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                                helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                            } else {
                                helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoking))
                            }

                        }
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
                //21
                SwapRecord.SwapState.WithDrawEthFetchDone.ordinal -> {
                    if (item.ethTimeout && item.neoTimeout) {
                        if (item.swaptxHash == null || "".equals(item.swaptxHash)) {
                            helper.setVisible(R.id.tvOperator, true)
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.expired))
                            helper.setText(R.id.tvOperator, mContext.getString(R.string.revoke))
                        } else {
                            helper.setText(R.id.tvSwapState, mContext.getString(R.string.revoked))
                        }
                    } else {
                        helper.setText(R.id.tvSwapState, mContext.getString(R.string.pending))
                    }
                }
            }
        }
        helper.addOnClickListener(R.id.tvOperator)
    }
}
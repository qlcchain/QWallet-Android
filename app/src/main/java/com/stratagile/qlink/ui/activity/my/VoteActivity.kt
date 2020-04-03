package com.stratagile.qlink.ui.activity.my

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.VoteResult
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.my.component.DaggerVoteComponent
import com.stratagile.qlink.ui.activity.my.contract.VoteContract
import com.stratagile.qlink.ui.activity.my.module.VoteModule
import com.stratagile.qlink.ui.activity.my.presenter.VotePresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_eos_buy_ram.*
import kotlinx.android.synthetic.main.activity_vote.*
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/02/26 10:34:02
 */

class VoteActivity : BaseActivity(), VoteContract.View {

    val INVOTEING = 1
    val VOTEEND = 2

    val SELFNOVOTE = 3
    val SELFVOTED = 4

    var currentVoteStatus = -1
    var myVoteStatus = -1
    var currentSelected = -1

    @Inject
    internal lateinit var mPresenter: VotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.mainColor
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_vote)
        title.text = getString(R.string.vote_1)
        getVoteTime()
    }
    override fun initData() {
        llVote1.setOnClickListener {
            if (currentVoteStatus == INVOTEING) {
                if (myVoteStatus == SELFNOVOTE) {
                    currentSelected = 1
                    ivSelectVote1.setImageResource(R.mipmap.selected_h)
                    ivSelectVote2.setImageResource(R.mipmap.icon_default)
                    ivSelectVote3.setImageResource(R.mipmap.icon_default)
                    ivSelectVote4.setImageResource(R.mipmap.icon_default)
                } else if (myVoteStatus == SELFVOTED) {

                }
            }
            if (currentVoteStatus == VOTEEND) {

            }
        }
        llVote2.setOnClickListener {
            if (currentVoteStatus == INVOTEING) {
                if (myVoteStatus == SELFNOVOTE) {
                    currentSelected = 2
                    ivSelectVote1.setImageResource(R.mipmap.icon_default)
                    ivSelectVote2.setImageResource(R.mipmap.selected_h)
                    ivSelectVote3.setImageResource(R.mipmap.icon_default)
                    ivSelectVote4.setImageResource(R.mipmap.icon_default)
                } else if (myVoteStatus == SELFVOTED) {

                }
            }
            if (currentVoteStatus == VOTEEND) {

            }
        }
        llVote3.setOnClickListener {
            if (currentVoteStatus == INVOTEING) {
                if (myVoteStatus == SELFNOVOTE) {
                    currentSelected = 3
                    ivSelectVote1.setImageResource(R.mipmap.icon_default)
                    ivSelectVote2.setImageResource(R.mipmap.icon_default)
                    ivSelectVote3.setImageResource(R.mipmap.selected_h)
                    ivSelectVote4.setImageResource(R.mipmap.icon_default)
                } else if (myVoteStatus == SELFVOTED) {

                }
            }
            if (currentVoteStatus == VOTEEND) {

            }
        }
        llVote4.setOnClickListener {
            if (currentVoteStatus == INVOTEING) {
                if (myVoteStatus == SELFNOVOTE) {
                    currentSelected = 4
                    ivSelectVote1.setImageResource(R.mipmap.icon_default)
                    ivSelectVote2.setImageResource(R.mipmap.icon_default)
                    ivSelectVote3.setImageResource(R.mipmap.icon_default)
                    ivSelectVote4.setImageResource(R.mipmap.selected_h)
                } else if (myVoteStatus == SELFVOTED) {

                }
            }
            if (currentVoteStatus == VOTEEND) {

            }
        }

        tvSubmitVote.setOnClickListener {
            if (currentSelected == -1) {
                return@setOnClickListener
            }
            vote()
        }


        if (currentVoteStatus == VOTEEND) {
            //投票已结束
            tvTips.text = getString(R.string.the_voting_has_been_closed)
            etOpinion.visibility = View.GONE

            tvSubmitVote.visibility = View.GONE

            tvVote1Rate.visibility = View.VISIBLE
            tvVote2Rate.visibility = View.VISIBLE
            tvVote3Rate.visibility = View.VISIBLE
            tvVote4Rate.visibility = View.VISIBLE

            if (this::voteResult1.isInitialized) {
                var allVote = voteResult1.result.`_$1` + voteResult1.result.`_$2` + voteResult1.result.`_$3` + voteResult1.result.`_$4`

                tvVote1Rate.text = voteResult1.result.`_$1`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                tvVote2Rate.text = voteResult1.result.`_$2`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                tvVote3Rate.text = voteResult1.result.`_$3`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                tvVote4Rate.text = voteResult1.result.`_$4`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
            }

            ivSelectVote1.visibility = View.GONE
            ivSelectVote2.visibility = View.GONE
            ivSelectVote3.visibility = View.GONE
            ivSelectVote4.visibility = View.GONE

            ivVoteSelected1.visibility = View.INVISIBLE
            ivVoteSelected2.visibility = View.INVISIBLE
            ivVoteSelected3.visibility = View.INVISIBLE
            ivVoteSelected4.visibility = View.INVISIBLE

            if (myVoteStatus == SELFVOTED) {
                //自己投了票
                when(currentSelected) {
                    1 -> {
                        ivVoteSelected1.visibility = View.VISIBLE
                    }
                    2 -> {
                        ivVoteSelected2.visibility = View.VISIBLE
                    }
                    3 -> {
                        ivVoteSelected3.visibility = View.VISIBLE
                    }
                    4 -> {
                        ivVoteSelected4.visibility = View.VISIBLE
                    }
                }

            } else if (myVoteStatus == SELFNOVOTE) {
                //自己未投票
            }
        } else if (currentVoteStatus == INVOTEING) {
            //投票未结束
            if (myVoteStatus == SELFVOTED) {
                //自己投了票
                tvTips.text = getString(R.string.you_have_voted_successfully_thanks_for_your)
                etOpinion.visibility = View.GONE
                tvSubmitVote.visibility = View.GONE

                tvVote1Rate.visibility = View.VISIBLE
                tvVote2Rate.visibility = View.VISIBLE
                tvVote3Rate.visibility = View.VISIBLE
                tvVote4Rate.visibility = View.VISIBLE

                if (this::voteResult1.isInitialized) {
                    var allVote = voteResult1.result.`_$1` + voteResult1.result.`_$2` + voteResult1.result.`_$3` + voteResult1.result.`_$4`

                    tvVote1Rate.text = voteResult1.result.`_$1`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                    tvVote2Rate.text = voteResult1.result.`_$2`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                    tvVote3Rate.text = voteResult1.result.`_$3`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                    tvVote4Rate.text = voteResult1.result.`_$4`.toBigDecimal().divide(allVote.toBigDecimal(), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).toPlainString() + "%"
                }

                ivSelectVote1.visibility = View.GONE
                ivSelectVote2.visibility = View.GONE
                ivSelectVote3.visibility = View.GONE
                ivSelectVote4.visibility = View.GONE

                ivVoteSelected1.visibility = View.INVISIBLE
                ivVoteSelected2.visibility = View.INVISIBLE
                ivVoteSelected3.visibility = View.INVISIBLE
                ivVoteSelected4.visibility = View.INVISIBLE

                when(currentSelected) {
                    1 -> {
                        ivVoteSelected1.visibility = View.VISIBLE
                    }
                    2 -> {
                        ivVoteSelected2.visibility = View.VISIBLE
                    }
                    3 -> {
                        ivVoteSelected3.visibility = View.VISIBLE
                    }
                    4 -> {
                        ivVoteSelected4.visibility = View.VISIBLE
                    }
                }

            } else if (myVoteStatus == SELFNOVOTE){
                //自己未投票
                tvTips.text = getString(R.string.if_you_got_more_to_share_with_us_leave_your_opinions_and_comments_below)
                etOpinion.visibility = View.VISIBLE
                tvSubmitVote.visibility = View.VISIBLE

                tvVote1Rate.visibility = View.GONE
                tvVote2Rate.visibility = View.GONE
                tvVote3Rate.visibility = View.GONE
                tvVote4Rate.visibility = View.GONE



                ivSelectVote1.visibility = View.VISIBLE
                ivSelectVote2.visibility = View.VISIBLE
                ivSelectVote3.visibility = View.VISIBLE
                ivSelectVote4.visibility = View.VISIBLE

                ivVoteSelected1.visibility = View.INVISIBLE
                ivVoteSelected2.visibility = View.INVISIBLE
                ivVoteSelected3.visibility = View.INVISIBLE
                ivVoteSelected4.visibility = View.INVISIBLE
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerVoteComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .voteModule(VoteModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: VoteContract.VoteContractPresenter) {
            mPresenter = presenter as VotePresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    fun getVoteResult() {
        var infoMap = hashMapOf<String, String>()
        if (ConstantValue.currentUser != null) {
            infoMap["userAccount"] = ConstantValue.currentUser.account
        }
        mPresenter.sysVoteResult(infoMap)
    }

    lateinit var voteResult1: VoteResult
    override fun setVoteResult(voteResult: VoteResult) {
        voteResult1 = voteResult
        if (voteResult.choose == null) {
            myVoteStatus = SELFNOVOTE
        } else {
            myVoteStatus = SELFVOTED
            currentSelected = voteResult.choose.toInt()
        }
        initData()
    }

    override fun voteBack() {
        closeProgressDialog()
        finish()
    }

    fun getVoteTime() {
        var infoMap = hashMapOf<String, String>()
        infoMap["dictType"] = "app_dict"
        mPresenter.getAppDict(infoMap)
    }

    lateinit var appDict1: Dict

    override fun setAppDict(dict: Dict) {
        appDict1 = dict
        if ((TimeUtil.timeStamp(appDict1.data.burnQgasVoteStartDate) <= appDict1.currentTimeMillis) && (appDict1.currentTimeMillis <= TimeUtil.timeStamp(appDict1.data.burnQgasVoteEndDate))) {
            currentVoteStatus = INVOTEING
        } else {
            currentVoteStatus = VOTEEND
        }
        getVoteResult()
    }


    fun vote() {
        if (ConstantValue.currentUser == null) {
            startActivityForResult(Intent(this, AccountActivity::class.java), 1)
            return
        }
        showProgressDialog()
        var infoMap = hashMapOf<String, String>()
        infoMap["account"] = ConstantValue.currentUser.account
        infoMap["token"] = AccountUtil.getUserToken()
        infoMap["choose"] = currentSelected.toString()
        infoMap["opinion"] = etOpinion.text.toString().trim()
        mPresenter.sysVote(infoMap)
    }

}
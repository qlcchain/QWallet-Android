package com.stratagile.qlink.ui.activity.recommend

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.runDelayedOnUiThread
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.ui.activity.finance.InviteActivity
import com.stratagile.qlink.ui.activity.recommend.component.DaggerTopupProductDetailComponent
import com.stratagile.qlink.ui.activity.recommend.contract.TopupProductDetailContract
import com.stratagile.qlink.ui.activity.recommend.module.TopupProductDetailModule
import com.stratagile.qlink.ui.activity.recommend.presenter.TopupProductDetailPresenter
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.ui.activity.topup.TopupConfirmGroupOrderActivity
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupDeductionQlcChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupSelectDeductionTokenActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.topup.TopupGroupKindListAdapter
import com.stratagile.qlink.ui.adapter.topup.TopupGroupListAdapter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_topup_product_detail.*
import kotlinx.android.synthetic.main.activity_topup_product_detail.recyclerView
import kotlinx.android.synthetic.main.fragment_topup.*
import java.io.File
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/13 15:36:22
 */

class TopupProductDetailActivity : BaseActivity(), TopupProductDetailContract.View {

    @Inject
    internal lateinit var mPresenter: TopupProductDetailPresenter

    protected var openInterpolator: Interpolator = CubicBezierInterpolator.EASE_OUT_QUINT
    protected var backDrawable = ColorDrawable(0x4c000000)
    protected var backDrawableNew = ColorDrawable(0x4c000000)
    lateinit var topupBean: TopupProduct.ProductListBean
    lateinit var selectToken: PayToken.PayTokenListBean
    lateinit var selectedGroup: TopupGroupList.GroupListBean

    var isShowTuan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_product_detail)
        rlPartTuan.setBackgroundDrawable(backDrawable)

        rlNewPartTuan.setBackgroundDrawable(backDrawableNew)
        topupBean = intent.getParcelableExtra("productBean")
        selectToken = intent.getParcelableExtra("selectedPayToken")
    }

    fun getGroupList() {
        var infoMap = mutableMapOf<String, String>()
        infoMap["productId"] = topupBean.id
        infoMap["localFiatMoney"] = topupBean.localFiatAmount
        infoMap["deductionTokenId"] = selectToken.id
        infoMap["status"] = "PROCESSING"
        mPresenter.getTopupGroupList(infoMap)
    }

    fun joinGroup() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
        map["groupId"] = selectedGroup.id
        map["phoneNumber"] = intent.getStringExtra("phoneNumber")
        mPresenter.topupJoinGroup(map)
    }

    fun getGroupKindList() {
        var infoMap = mutableMapOf<String, String>()
        mPresenter.getTopupGroupKindList(infoMap)
    }

    override fun initData() {
        var deductionTokenPrice = 0.toDouble()
        if ("CNY".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.price
        } else if ("USD".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.usdPrice
        }
        tvPrice.text = topupBean.localFiatAmount.toString() + topupBean.localFiat
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            KLog.i("设置为英文")
            tvIsp.text = topupBean.ispEn
            TextUtil.setGroupInfo(topupBean.countryEn, topupBean.ispEn + " " + topupBean.localFiatAmount + topupBean.localFiat + " " + topupBean.explainEn, tvOperator)
            //tvOperator.text = topupBean.countryEn + topupBean.ispEn + topupBean.localFiatAmount + topupBean.localFiat + topupBean.explainEn
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 1) {
            KLog.i("设置为中文")
            TextUtil.setGroupInfo(topupBean.country, topupBean.isp + " " + topupBean.localFiatAmount + topupBean.localFiat + " " + topupBean.explain, tvOperator)
            //tvOperator.text = topupBean.country + topupBean.isp + topupBean.localFiatAmount + topupBean.localFiat + topupBean.explain
            tvIsp.text = topupBean.isp
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 2) {
            KLog.i("设置为印度尼西亚")
            TextUtil.setGroupInfo(topupBean.countryEn, topupBean.ispEn + " " + topupBean.localFiatAmount + topupBean.localFiat + " " + topupBean.explainEn, tvOperator)
            //tvOperator.text = topupBean.countryEn + topupBean.ispEn + topupBean.localFiatAmount + topupBean.localFiat + topupBean.explainEn
            tvIsp.text = topupBean.ispEn
        }

        when(topupBean.ispEn.trim()) {
            "Starhub" -> {
                ivIsp.setImageResource(R.mipmap.starhub)
            }
            "M1" -> {
                ivIsp.setImageResource(R.mipmap.m1)
            }
            "Singtel" -> {
                ivIsp.setImageResource(R.mipmap.singtel)
            }
            "All Operators in Indonesia" -> {
                ivIsp.setImageResource(R.mipmap.telkom)
            }
            "All Operators in China" -> {
                ivIsp.setImageResource(R.mipmap.quanguo)
            }
        }


        var dikoubijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.qgasDiscount.toBigDecimal())
        var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

        var zhifufabijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.discount.toBigDecimal())
        var zhifudaibijine = zhifufabijine - dikoubijine
        var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupBean.payFiat)) {
            topupBean.payTokenCnyPrice.toBigDecimal()
        } else {
            topupBean.payTokenUsdPrice.toBigDecimal()
        }, 3, BigDecimal.ROUND_HALF_UP)

        tvTokenCount.text = zhifubishuliang.stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol + "+" + dikoubishuliang.stripTrailingZeros().toPlainString() + selectToken.symbol


        var orginZhifufabishuliang = zhifufabijine.divide(if ("CNY".equals(topupBean.payFiat)){topupBean.payTokenCnyPrice.toBigDecimal()} else {topupBean.payTokenUsdPrice.toBigDecimal()}, 3, BigDecimal.ROUND_HALF_UP)
        tvPriceOrgin.text = orginZhifufabishuliang.stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol
        tvPriceOrgin.paint.isAntiAlias = true
        tvPriceOrgin.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG

        if (topupBean.orderTimes > 100) {
            tvOrderTimes.text = getString(R.string.xxx_open, topupBean.orderTimes.toString().trim())
        } else {
            tvOrderTimes.text = getString(R.string.xxx_open, "100+")
        }
        Glide.with(this)
                .load(AppConfig.instance.baseUrl + topupBean.imgPath.replace("/dapp", ""))
                .apply(AppConfig.getInstance().optionsTopup)
                .into(ivProduct1New)
        Glide.with(this)
                .load(AppConfig.instance.baseUrl + topupBean.imgPath.replace("/dapp", ""))
                .apply(AppConfig.getInstance().optionsTopup)
                .into(ivProduct1)

        title.text = getString(R.string.group_plan_details)
        tvBuyTuan.setOnClickListener {
            startAnimationNew()
        }
        rlPartTuan.setOnClickListener { dismiss() }

        rlNewPartTuan.setOnClickListener { dismissNew() }
        tvBuyTuan1.setOnClickListener {
            startAnimationNew()
        }
        ivClose.setOnClickListener {
            dismiss()
        }

        ivCloseNew.setOnClickListener {
            dismissNew()
        }
        runDelayedOnUiThread(50) {
            dismissInit()
            dismissInitNew()
        }
        getGroupList()

        tvBuySelf.setOnClickListener {
            var confirmIntent = Intent(this@TopupProductDetailActivity, TopupConfirmGroupOrderActivity::class.java)
            confirmIntent.putExtra("buySelf", true)
            confirmIntent.putExtra("productBean", topupBean)
            confirmIntent.putExtra("selectedPayToken", selectToken)
            if (intent.hasExtra("phoneNumber")) {
                confirmIntent.putExtra("phoneNumber", intent.getStringExtra("phoneNumber"))
            }
            startActivityForResult(confirmIntent, 10)
            //buySelf()
        }
    }

    fun buySelf() {
        var deductionTokenPrice = 0.toDouble()
        if ("CNY".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.price
        } else if ("USD".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.usdPrice
        }

        var dikoubijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.qgasDiscount.toBigDecimal())
        var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)
        var zhifufabijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.discount.toBigDecimal())
        var zhifudaibijine = zhifufabijine - dikoubijine
        var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupBean.payFiat)) {
            topupBean.payTokenCnyPrice.toBigDecimal()
        } else {
            topupBean.payTokenUsdPrice.toBigDecimal()
        }, 3, BigDecimal.ROUND_HALF_UP)

        alert(getString(R.string.a_cahrge_of_will_cost_paytoken_and_deduction_token, topupBean.localFiatAmount.toString(), zhifubishuliang.stripTrailingZeros().toPlainString(), topupBean.payTokenSymbol, dikoubishuliang.stripTrailingZeros().toPlainString(), selectToken.symbol, topupBean.localFiat)) {
            negativeButton(getString(R.string.cancel)) { dismiss() }
            positiveButton(getString(R.string.buy_topup)) {
                if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                    generateTopupOrder(topupBean)
                } else {
                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                        negativeButton(getString(R.string.cancel)) { dismiss() }
                        positiveButton(getString(R.string.create)) { startActivity(Intent(this@TopupProductDetailActivity, SelectWalletTypeActivity::class.java)) }
                    }.show()
                }
            }
        }.show()
    }

    fun generateTopupOrder(product: TopupProduct.ProductListBean) {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        var topUpP2pId = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
        if ("".equals(topUpP2pId)) {
            var saveP2pId = FileUtil.readData("/Qwallet/p2pId.txt")
            if ("".equals(saveP2pId)) {
                val uuid = UUID.randomUUID()
                var p2pId = ""
                p2pId += uuid.toString().replace("-", "")
                topUpP2pId = p2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, p2pId)

                val file = File(Environment.getExternalStorageDirectory().toString() + "/Qwallet/p2pId.txt")
                if (file.exists()) {
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                } else {
                    file.createNewFile()
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                }

            } else {
                topUpP2pId = saveP2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, topUpP2pId)
            }
        }
        KLog.i("p2pId为：" + topUpP2pId)
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
            map["p2pId"] = topUpP2pId
        } else {
            map["p2pId"] = topUpP2pId
        }
        map["productId"] = product.id
        map["localFiatAmount"] = product.localFiatAmount
        map["phoneNumber"] = intent.getStringExtra("phoneNumber")
        map["deductionTokenId"] = selectToken.id
        mPresenter.createTopupOrder(map)
    }

    override fun createTopupOrderError() {
        closeProgressDialog()
    }

    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {
        closeProgressDialog()
        when (OtcUtils.parseChain(selectToken.chain)) {
            AllWallet.WalletType.QlcWallet -> {
                val intent1 = Intent(this@TopupProductDetailActivity, TopupDeductionQlcChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            }
            AllWallet.WalletType.EthWallet -> {
                val intent1 = Intent(this@TopupProductDetailActivity, TopupDeductionEthChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            }
        }
    }


    override fun onBackPressed() {
        if (isShowTuan) {
            dismiss()
            dismissNew()
            return
        }
        super.onBackPressed()
    }

    fun startAnimationNew() {
        if (isShowTuan) {
            return
        }
        getGroupKindList()
        rlNewPartTuan.isClickable = true
        isShowTuan = true
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scvNew, View.TRANSLATION_Y, 0f),
                ObjectAnimator.ofInt(backDrawableNew, AnimationProperties.COLOR_DRAWABLE_ALPHA, 100))
        currentSheetAnimation.duration = 400
        currentSheetAnimation.startDelay = 20
        currentSheetAnimation.interpolator = openInterpolator
        rlNewPartTuan.visibility = View.VISIBLE
        currentSheetAnimation.start()
        tvConfirmNew.setOnClickListener {
            createNewGroup()
        }
    }

    fun startAnimation() {
        if (isShowTuan) {
            return
        }
        rlPartTuan.isClickable = true
        isShowTuan = true
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scv, View.TRANSLATION_Y, 0f),
                ObjectAnimator.ofInt(backDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, 100))
        currentSheetAnimation.duration = 400
        currentSheetAnimation.startDelay = 20
        currentSheetAnimation.interpolator = openInterpolator
        rlPartTuan.visibility = View.VISIBLE
        currentSheetAnimation.start()
        tvConfirm.setOnClickListener {

            alert(getString(R.string.a_cahrge_of_will_cost_paytoken_and_deduction_token, topupBean.localFiatAmount.toString(), selectedGroup.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString(), selectedGroup.payToken, selectedGroup.deductionTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString(), selectedGroup.deductionToken, topupBean.localFiat)) {
                negativeButton(getString(R.string.cancel)) { dismiss() }
                positiveButton(getString(R.string.buy_topup)) {
                    joinGroup()
                }
            }.show()
        }
        tvJoinTuanPrice.text = selectedGroup.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString() + selectedGroup.payToken + "+" + selectedGroup.deductionTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString() + selectedGroup.deductionToken
    }

    fun createNewGroup() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
        map["groupKindId"] = selectedGroupKind.id
        map["productId"] = topupBean.id
        map["localFiatMoney"] = topupBean.localFiatAmount
        map["deductionTokenId"] = selectToken.id
        mPresenter.createGroup(map)
    }

    fun dismissNew() {
        rlNewPartTuan.isClickable = false
        isShowTuan = false
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scvNew, "translationY", scv.getMeasuredHeight() + 10f),
                ObjectAnimator.ofInt(backDrawableNew, "alpha", 0)
        )
        currentSheetAnimation.duration = 180
        currentSheetAnimation.interpolator = CubicBezierInterpolator.EASE_OUT
        currentSheetAnimation.start()
    }

    fun dismiss() {
        rlPartTuan.isClickable = false
        isShowTuan = false
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scv, "translationY", scv.getMeasuredHeight() + 10f),
                ObjectAnimator.ofInt(backDrawable, "alpha", 0)
        )
        currentSheetAnimation.duration = 180
        currentSheetAnimation.interpolator = CubicBezierInterpolator.EASE_OUT
        currentSheetAnimation.start()
    }

    fun dismissInit() {
        rlPartTuan.isClickable = false
        isShowTuan = false
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scv, "translationY", scv.getMeasuredHeight() + 10f),
                ObjectAnimator.ofInt(backDrawable, "alpha", 0)
        )
        currentSheetAnimation.duration = 10
        currentSheetAnimation.interpolator = CubicBezierInterpolator.EASE_OUT
        currentSheetAnimation.start()
    }

    fun dismissInitNew() {
        rlNewPartTuan.isClickable = false
        isShowTuan = false
        var currentSheetAnimation = AnimatorSet()
        currentSheetAnimation.playTogether(
                ObjectAnimator.ofFloat(scvNew, "translationY", scv.getMeasuredHeight() + 10f),
                ObjectAnimator.ofInt(backDrawableNew, "alpha", 0)
        )
        currentSheetAnimation.duration = 10
        currentSheetAnimation.interpolator = CubicBezierInterpolator.EASE_OUT
        currentSheetAnimation.start()
    }

    override fun setupActivityComponent() {
        DaggerTopupProductDetailComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupProductDetailModule(TopupProductDetailModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupProductDetailContract.TopupProductDetailContractPresenter) {
        mPresenter = presenter as TopupProductDetailPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    lateinit var topupGroupKindListAdapter: TopupGroupKindListAdapter

    lateinit var topupGroupListAdapter: TopupGroupListAdapter

    lateinit var selectedGroupKind: TopupGroupKindList.GroupKindListBean
    override fun setGroupKindList(topupGroupKindList: TopupGroupKindList) {
        topupGroupKindList.groupKindList.forEachIndexed { index, groupKindListBean ->
            groupKindListBean.isSelected = index == 0
            if (groupKindListBean.isSelected) {
                selectedGroupKind = groupKindListBean
            }
        }
        topupGroupKindListAdapter = TopupGroupKindListAdapter(topupGroupKindList.groupKindList)
        recyclerViewGroupKind.adapter = topupGroupKindListAdapter
        topupGroupKindListAdapter.setOnItemClickListener { adapter, view, position ->
            topupGroupKindListAdapter.data.forEachIndexed { index, groupKindListBean ->
                groupKindListBean.isSelected = index == position
                if (groupKindListBean.isSelected) {
                    selectedGroupKind = groupKindListBean
                }
            }

            var deductionTokenPrice = 0.toDouble()
            if ("CNY".equals(topupBean.payFiat)) {
                deductionTokenPrice = selectToken.price
            } else if ("USD".equals(topupBean.payFiat)) {
                deductionTokenPrice = selectToken.usdPrice
            }

            var dikoubijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.qgasDiscount.toBigDecimal().multiply(topupGroupKindListAdapter.data[position].discount.toBigDecimal()))
            var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

            var zhifufabijine = topupBean.payFiatAmount.toBigDecimal()
            var zhifudaibijine = zhifufabijine.multiply(topupGroupKindListAdapter.data[position].discount.toBigDecimal()) - dikoubijine
            var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupBean.payFiat)) {
                topupBean.payTokenCnyPrice.toBigDecimal()
            } else {
                topupBean.payTokenUsdPrice.toBigDecimal()
            }, 3, BigDecimal.ROUND_HALF_UP)

            tvAfterAmount.text = zhifubishuliang.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol + " + " + dikoubishuliang.stripTrailingZeros().toPlainString() + selectToken.symbol
            topupGroupKindListAdapter.notifyDataSetChanged()
        }

        var deductionTokenPrice = 0.toDouble()
        if ("CNY".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.price
        } else if ("USD".equals(topupBean.payFiat)) {
            deductionTokenPrice = selectToken.usdPrice
        }

        var dikoubijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.qgasDiscount.toBigDecimal()).multiply(topupGroupKindListAdapter.data[0].discount.toBigDecimal())
        var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

        var zhifufabijine = topupBean.payFiatAmount.toBigDecimal()
        var zhifudaibijine = zhifufabijine.multiply(topupGroupKindListAdapter.data[0].discount.toBigDecimal()) - dikoubijine
        var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupBean.payFiat)) {
            topupBean.payTokenCnyPrice.toBigDecimal()
        } else {
            topupBean.payTokenUsdPrice.toBigDecimal()
        }, 3, BigDecimal.ROUND_HALF_UP)

        tvAfterAmount.text = zhifubishuliang.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol + " + " + dikoubishuliang.stripTrailingZeros().toPlainString() + selectToken.symbol

    }

    override fun showProxyDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.icon_liaojie_daili))
        tvContent.text = getString(R.string.a_recharge_agent_partner_can_start_the_group_recharge)
        val sweetAlertDialog = SweetAlertDialog(this)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvOk = view.findViewById<TextView>(R.id.tvOpreate)
        tvOk.text = getString(R.string.agent_partner_details)
        tvOk.setOnClickListener {
            startActivity(Intent(this@TopupProductDetailActivity, InviteActivity::class.java))
            sweetAlertDialog.cancel()
        }
        ivClose.setOnClickListener { sweetAlertDialog.cancel() }
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
    }

    override fun showStakeDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.icon_liaojie_daili))
        tvContent.text = getString(R.string.you_havenot_staked_1500_qlc_in_this_wallet)
        val sweetAlertDialog = SweetAlertDialog(this)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvOk = view.findViewById<TextView>(R.id.tvOpreate)
        tvOk.text = getString(R.string.stake_now)
        tvOk.setOnClickListener {
            startActivity(Intent(this@TopupProductDetailActivity, MyStakeActivity::class.java))
            sweetAlertDialog.cancel()
        }
        ivClose.setOnClickListener { sweetAlertDialog.cancel() }
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
    }

    override fun createGroupBack(createGroup: CreateGroup) {
        closeProgressDialog()
        dismiss()
        initData()
    }

    override fun setGroupList(topupGroupList: TopupGroupList) {
        if (topupGroupList.groupList != null && topupGroupList.groupList.size != 0) {
            llNoTuan.visibility = View.GONE
        } else {
            llNoTuan.visibility = View.VISIBLE
        }
        topupGroupListAdapter = TopupGroupListAdapter(topupGroupList.groupList)
        recyclerView.adapter = topupGroupListAdapter
        tvGroupInfo.text = ""
        tvNeedPartners.text = ""
        tvRemainTime.text = ""
        topupGroupListAdapter.setOnItemChildClickListener { adapter, view, position ->
            selectedGroup = topupGroupListAdapter.data[position]
            var confirmIntent = Intent(this@TopupProductDetailActivity, TopupConfirmGroupOrderActivity::class.java)
            confirmIntent.putExtra("productBean", topupBean)
            confirmIntent.putExtra("selectedPayToken", selectToken)
            if (intent.hasExtra("phoneNumber")) {
                confirmIntent.putExtra("phoneNumber", intent.getStringExtra("phoneNumber"))
            }
            confirmIntent.putExtra("group", selectedGroup)
            startActivityForResult(confirmIntent, 10)
//            runDelayedOnUiThread(430) {
//                var isCn = true
//                isCn = SpUtil.getInt(this, ConstantValue.Language, -1) == 1
//                tvNeedPartners.text = getString(R.string._more_partner_needed, (topupGroupListAdapter.data[position].numberOfPeople - topupGroupListAdapter.data[position].joined).toString())
//                tvGroupInfo.text = getString(R.string._off_discount_partners, if (isCn){topupGroupListAdapter.data[position].discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()} else {(1.toBigDecimal() - topupGroupListAdapter.data[position].discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString()}, topupGroupListAdapter.data[position].numberOfPeople.toString())
//                tvRemainTime.setText(getString(R.string.valid_till) + TimeUtil.getOrderTime(TimeUtil.timeStamp(topupGroupListAdapter.data[position].createDate) + (topupGroupListAdapter.data[position].duration * 60 * 1000)))
//
//                var headList = arrayListOf<String>()
//                topupGroupListAdapter.data[position].items?.forEach {
//                    headList.add(it.head)
//                }
//                headList.add(topupGroupListAdapter.data[position].head)
//                var allMargin = UIUtils.dip2px(20f, this@TopupProductDetailActivity)
//
//                var leftMargin = 0
//                if (headList.size > 1) {
//                    leftMargin = allMargin / (headList.size - 1)
//                }
//                headList.forEachIndexed { index, s ->
//                    var imageView = ImageView(this@TopupProductDetailActivity)
//                    var lp = RelativeLayout.LayoutParams(UIUtils.dip2px(32f, this@TopupProductDetailActivity), UIUtils.dip2px(32f, this@TopupProductDetailActivity))
//                    lp.leftMargin = index * leftMargin
//                    imageView.layoutParams = lp
//                    KLog.i(AppConfig.instance.baseUrl + s)
//                    Glide.with(this@TopupProductDetailActivity)
//                            .load(MainAPI.MainBASE_URL + s)
//                            .apply(AppConfig.getInstance().optionsWhiteColor)
//                            .into(imageView)
//                    rlAvatar.addView(imageView)
//
//
//                    if (index == headList.size - 1) {
//                        var imageView1 = ImageView(this@TopupProductDetailActivity)
//                        var lp1 = RelativeLayout.LayoutParams(UIUtils.dip2px(12f, this@TopupProductDetailActivity), UIUtils.dip2px(12f, this@TopupProductDetailActivity))
//                        lp1.leftMargin = index * leftMargin + (UIUtils.dip2px(32f, this@TopupProductDetailActivity) - UIUtils.dip2px(12f, this@TopupProductDetailActivity))
//                        lp1.topMargin = UIUtils.dip2px(32f, this@TopupProductDetailActivity) - UIUtils.dip2px(12f, this@TopupProductDetailActivity)
//                        imageView1.layoutParams = lp1
//                        Glide.with(this@TopupProductDetailActivity)
//                                .load(R.mipmap.label_regimental)
//                                .apply(RequestOptions().centerCrop())
//                                .into(imageView1)
//                        rlAvatar.addView(imageView1)
//                    }
//                }
//            }
//            startAnimation()
        }
    }

    override fun joinGroupBack(topupJoinGroup: TopupJoinGroup) {
        closeProgressDialog()
        dismiss()
        initData()

        when(OtcUtils.parseChain(topupJoinGroup.item.deductionTokenChain)) {
            AllWallet.WalletType.QlcWallet -> {
                if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                    var payIntent = Intent(this@TopupProductDetailActivity, TopupDeductionQlcChainActivity::class.java)
                    payIntent.putExtra("groupBean", topupJoinGroup.item)
                    payIntent.putExtra("isGroup", true)
                    startActivityForResult(payIntent, 1)
                } else {
                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                        negativeButton(getString(R.string.cancel)) { dismiss() }
                        positiveButton(getString(R.string.create)) { startActivity(Intent(this@TopupProductDetailActivity, SelectWalletTypeActivity::class.java)) }
                    }.show()
                }
            }
            AllWallet.WalletType.EthWallet -> {
                var payIntent = Intent(this@TopupProductDetailActivity, TopupDeductionEthChainActivity::class.java)
                payIntent.putExtra("groupBean", topupJoinGroup.item)
                payIntent.putExtra("isGroup", true)
                startActivityForResult(payIntent, 1)
            }
        }
    }

    lateinit var menu: Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.deduction_menu, menu)
        this.menu = menu
        val simpleTarget: SimpleTarget<Drawable> = object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                menu.findItem(R.id.deduction).icon = resource
            }
        }

        if ("".equals(selectToken.logo_png)) {
            Glide.with(this)
                    .load(resources.getIdentifier(selectToken.symbol.toLowerCase(), "mipmap", packageName))
                    .apply(AppConfig.instance.optionsTopup)
                    .into(simpleTarget)
        } else {
            Glide.with(this)
                    .load(selectToken.logo_png)
                    .apply(AppConfig.instance.optionsTopup)
                    .into(simpleTarget)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deduction -> {
                startActivityForResult(Intent(this, TopupSelectDeductionTokenActivity::class.java),11)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val simpleTarget: SimpleTarget<Drawable> = object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    menu.findItem(R.id.deduction).icon = resource
                }
            }

            selectToken = data!!.getParcelableExtra("selectPayToken")
            if ("".equals(selectToken.logo_png)) {
                Glide.with(this)
                        .load(this.resources.getIdentifier(selectToken.symbol.toLowerCase(), "mipmap", packageName))
                        .apply(AppConfig.instance.optionsTopup)
                        .into(simpleTarget)
            } else {
                Glide.with(this)
                        .load(selectToken.logo_png)
                        .apply(AppConfig.instance.optionsTopup)
                        .into(simpleTarget)
            }
            initData()
        } else {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

}
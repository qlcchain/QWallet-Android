package com.stratagile.qlink.ui.activity.defi

import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pawegio.kandroid.runDelayedOnUiThread
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DefiRating
import com.stratagile.qlink.entity.defi.RatingInfo
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiRateComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiRateContract
import com.stratagile.qlink.ui.activity.defi.module.DefiRateModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiRatePresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.view.DefiPageTransformer
import com.stratagile.qlink.view.DefiPagerAdapter
import com.stratagile.qlink.view.SweetAlertDialog
import com.stratagile.qlink.views.ProgressDrawable
import kotlinx.android.synthetic.main.activity_defi_rate.*
import java.math.BigDecimal
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/06/03 14:17:18
 */

class DefiRateActivity : BaseActivity(), DefiRateContract.View {

    @Inject
    internal lateinit var mPresenter: DefiRatePresenter
    lateinit var defiEntity: DefiList.ProjectListBean


    var allAngle = 225

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_defi_rate)
    }

    override fun initData() {
        title.text = getString(R.string.rate_this_defi)
        defiEntity = intent.getParcelableExtra("defiEntity")
        getRatingInfo()
        Glide.with(this)
                .load(resources.getIdentifier(defiEntity.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", packageName))
                .into(ivAvatar)
        tvName.text = defiEntity.name
        tvRating.text = getString(R.string.rating_) + DefiUtil.parseDefiRating(defiEntity.rating.toInt())
        var defiPageTransformer = DefiPageTransformer()
        var defiPagerAdapter = DefiPagerAdapter(this, arrayListOf(0, 1, 2, 3, 4, 5, 6, 7))
        viewPager.setAdapter(defiPagerAdapter)
//        viewPager.setPageMargin(20)
        viewPager.setOffscreenPageLimit(8)//设置缓存页面数量
        viewPager.setPageTransformer(false, defiPageTransformer)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                var seelectText = DefiUtil.parseLocalDefiRating(p0)
                KLog.i("选中：" + p0)
                when (p0) {
                    0, 1, 2 -> {
                        scrollToA()
                    }
                    3, 4, 5 -> {
                        scrollToB()
                    }
                    6 -> {
                        scrollToC()
                    }
                    7 -> {
                        scrollToD()
                    }
                }
            }

        })
        defiArrow.postDelayed({
            viewPager.setCurrentItem(0)
            scrollToA()
        }, 200)

        segmentControlView.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.ratinga -> {
                    viewPager.setCurrentItem(0)
                    scrollToA()
                }
                R.id.ratingb -> {
                    viewPager.setCurrentItem(3)
                    scrollToB()
                }
                R.id.ratingc -> {
                    viewPager.setCurrentItem(6)
                    scrollToC()
                }
                R.id.ratingd -> {
                    viewPager.setCurrentItem(7)
                    scrollToD()
                }
            }
        }
        neoWalletList = AppConfig.instance.daoSession.walletDao.loadAll()
        getSelfQlc()
        tvSubmit.setOnClickListener {
            ratingDefi()
        }
//        var items = arrayOf("A++", "A+", "A", "B++", "B+", "B", "C", "D")
//        wheelView.setDividerConfig(WheelView.DividerConfig().setVisible(false))
//        wheelView.setTextSize(30f)
//        wheelView.setItems(items, 0)
//        wheelView.setOnItemSelectListener { index ->
//            KLog.i("选中的为：" + index)
//        }
    }

    var imageDrawable : ProgressDrawable? = null

    lateinit var neoWalletList : MutableList<Wallet>
    var overWalletList = arrayListOf<String>()
    fun getSelfQlc() {
        if (overWalletList.size == 0) {
            imageDrawable = ProgressDrawable()
            imageDrawable!!.start()
            ivLoad.setImageDrawable(imageDrawable)
            var map = hashMapOf<String, String>()
            map["address"] = neoWalletList[0].address
            mPresenter.getNeoWalletDetail(neoWalletList[0].address, map)
        } else {
            var map = hashMapOf<String, String>()
            map["address"] = neoWalletList[overWalletList.size].address
            mPresenter.getNeoWalletDetail(neoWalletList[overWalletList.size].address, map)
        }
    }

    override fun setNeoQlcAmount(amount: Double, address: String) {
        overWalletList.add(address)
        neoQlcAmount += amount.toFloat()
        tvNeoChainQlc.text = neoQlcAmount.toString() + " QLC"
        if (overWalletList.size < neoWalletList.size) {
            getSelfQlc()
        } else {
            KLog.i("开始查qlc链的qlc")
            getQlcChainQlc()
        }
    }

    var neoQlcAmount = 0.toFloat()
    var neoQlcOver = false
    override fun setNeoQLcAmount(amount: Double, over : Boolean) {
        neoQlcOver = over
        neoQlcAmount += amount.toFloat()
        tvNeoChainQlc.text = neoQlcAmount.toString() + " QLC"
        if (over) {
            KLog.i("开始查qlc链的qlc")
            getQlcChainQlc()
        }
    }
    var sweetAlertDialog: SweetAlertDialog? = null
    fun showWaitDialog() {
        val view = layoutInflater.inflate(R.layout.layout_wait, null, false)
        sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog!!.setView(view)
        sweetAlertDialog!!.show()
    }

    var qlcQlcAmount = 0.toFloat()
    var qlcQlcOver = false
    fun getQlcChainQlc() {
        mPresenter.getQlcChainQlcCount(AppConfig.instance.daoSession.qlcAccountDao.loadAll())
    }

    override fun setQLCQLCAmount(amount: Double, over: Boolean) {
        qlcQlcOver = over
        qlcQlcAmount += amount.toFloat()
        runOnUiThread {
            tvQlcChainQlc.text = qlcQlcAmount.toBigDecimal().divide(BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + " QLC"
        }
        if (over) {
            runOnUiThread {
                tvSubmit.isEnabled = true
                imageDrawable!!.stop()
                ivLoad.visibility = View.GONE
                setUnRating()
            }
        }
    }

    fun setUnRating() {
        if (this::mRatingInfo.isInitialized) {
            if (mRatingInfo.score == 0) {
                var weight = (qlcQlcAmount.toBigDecimal().divide(BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toFloat() + neoQlcAmount) / ((qlcQlcAmount.toBigDecimal().divide(BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toFloat() + neoQlcAmount) + mRatingInfo.qlcAmount.toFloat())
                var myAngle = allAngle * weight
                defiOvalView.setSweepAngle(-myAngle)
            }
        }
    }

    fun getRatingInfo() {
        var infoMap = hashMapOf<String, String>()
        infoMap["projectId"] = defiEntity.id
        infoMap["account"] = ConstantValue.currentUser.account
        mPresenter.getRatingInfo(infoMap)
    }

    fun scrollToA() {
        var translationx = ObjectAnimator.ofFloat(defiArrow, "translationX", defiArrow.translationX, UIUtils.dip2px(60f, this).toFloat() - defiArrow.width / 2)
        translationx.start()
        ratinga.toggle()
    }

    fun scrollToB() {
        var translationx = ObjectAnimator.ofFloat(defiArrow, "translationX", defiArrow.translationX, UIUtils.dip2px(140f, this).toFloat() - defiArrow.width / 2)
        translationx.start()
        ratingb.toggle()
    }

    fun scrollToC() {
        var translationx = ObjectAnimator.ofFloat(defiArrow, "translationX", defiArrow.translationX, UIUtils.dip2px(220f, this).toFloat() - defiArrow.width / 2)
        translationx.start()
        ratingc.toggle()
    }

    fun ratingDefi() {
        showWaitDialog()
        var infoMap = hashMapOf<String, String>()
        infoMap["account"] = ConstantValue.currentUser.account
        infoMap["token"] = AccountUtil.getUserToken()
        infoMap["projectId"] = defiEntity.id
        infoMap["score"] = DefiUtil.parseViewepagerScore(viewPager.currentItem).toString()
        infoMap["qlcAmount"] = (qlcQlcAmount.toBigDecimal().divide(BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toFloat() + neoQlcAmount).toString()
        mPresenter.ratingDefi(infoMap)
    }

    override fun defiRatingBack(defiRating: DefiRating) {
        runDelayedOnUiThread(2000, {
            sweetAlertDialog!!.dismissWithAnimation()
            getRatingInfo()
            showRatingSuccessDialog()
        })
    }

    override fun ratingError() {
        sweetAlertDialog!!.dismissWithAnimation()
    }

    fun showRatingSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog_tip, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.op_success))
        tvContent.text = getString(R.string.success_you_rating_weight_is_counted)
        val sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvSubmit.postDelayed(Runnable {
            sweetAlertDialog.cancel()
        }, 2000)
    }

    fun scrollToD() {
        var translationx = ObjectAnimator.ofFloat(defiArrow, "translationX", defiArrow.translationX, UIUtils.dip2px(300f, this).toFloat() - defiArrow.width / 2)
        translationx.start()
        ratingd.toggle()
    }

    override fun setupActivityComponent() {
        DaggerDefiRateComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .defiRateModule(DefiRateModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiRateContract.DefiRateContractPresenter) {
        mPresenter = presenter as DefiRatePresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    lateinit var mRatingInfo: RatingInfo
    override fun setRatingInfo(ratingInfo: RatingInfo) {
        mRatingInfo = ratingInfo
        if (ratingInfo.score == 0) {
            llShowRating.visibility = View.GONE
            llOpreateRating.visibility = View.VISIBLE
        } else {
            llShowRating.visibility = View.VISIBLE
            llOpreateRating.visibility = View.GONE
            var myAngle = allAngle * ratingInfo.weight.toFloat()
            defiOvalView.setSweepAngle(-myAngle)
            tvMyRating.text = DefiUtil.parseDefiRating(ratingInfo.score)
            var fillColor = 0x000000
            if (view.text.toString().contains("A")) {
                fillColor = R.color.color_7ED321
            } else if (view.text.toString().contains("B")) {
                fillColor = R.color.color_108EE9
            } else if (view.text.toString().contains("C")) {
                fillColor = R.color.color_F5A623
            } else {
                fillColor = R.color.color_FF3669
            }
            tvMyRating.setTextColor(resources.getColor(fillColor))
            tvMyRatingDesc.text = getString(R.string.my_rating_weight_18, BigDecimal(ratingInfo.weight).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString())
        }
    }

}
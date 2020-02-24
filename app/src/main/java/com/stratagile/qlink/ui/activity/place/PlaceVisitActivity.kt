package com.stratagile.qlink.ui.activity.place

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.hardware.input.InputManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.PhoneNumber
import com.stratagile.qlink.entity.QLcSms
import com.stratagile.qlink.entity.SmsReport
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.place.component.DaggerPlaceVisitComponent
import com.stratagile.qlink.ui.activity.place.contract.PlaceVisitContract
import com.stratagile.qlink.ui.activity.place.module.PlaceVisitModule
import com.stratagile.qlink.ui.activity.place.presenter.PlaceVisitPresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.MD5Util
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.utils.SendBack
import com.stratagile.qlink.utils.SmsObserver
import com.stratagile.qlink.view.SweetAlertDialog
import com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import kotlinx.android.synthetic.main.activity_place_visit.*
import kotlinx.android.synthetic.main.activity_place_visit.llSelectQlcWallet
import kotlinx.android.synthetic.main.activity_place_visit.tvQGASBalance
import kotlinx.android.synthetic.main.activity_topup_qlc_pay.*
import kotlinx.android.synthetic.main.alert_mining.*
import kotlinx.android.synthetic.main.item_select_country.*
import org.apache.commons.collections.MultiHashMap
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Error
import java.math.BigDecimal

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: $description
 * @date 2020/02/20 10:07:00
 */

class PlaceVisitActivity : BaseActivity(), PlaceVisitContract.View {

    @Inject
    internal lateinit var mPresenter: PlaceVisitPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                var qlcSms = msg.obj as QLcSms
                if ((System.currentTimeMillis() - qlcSms.timestamp < 1000 * 3)) {
                    if (qlcSms.body.contains("重新输入") || qlcSms.body.contains("证件数字有误")) {
                        if (qlcSms1 == null) {
                            qlcSms1 = qlcSms
                            sweetAlertDialog.dismissWithAnimation()
                            showError(qlcSms.body)
                        }
                    } else {
                        if (qlcSms1 != null) {
                            if (qlcSms1!!.id == qlcSms.id) {

                            } else {
                                if (qlcSms2 != null) {

                                } else {
                                    if (qlcSms.number.contains("10086")) {
                                        //中国移动
                                        qlcSms2 = qlcSms
                                        analyseSms(qlcSms2!!)
                                    } else if (qlcSms.number.equals("10010")) {
                                        //中国联通
//                                            qlcSms1 = qlcSms
                                    } else if (qlcSms.number.contains("10001")) {
                                        //中国电信
                                        qlcSms2 = qlcSms
                                        analyseSms(qlcSms2!!)
                                    }
                                }
                            }
                        } else {
                            if (qlcSms.number.contains("10086")) {
                                //中国移动
                                if (qlcSms.body.contains("二次确认")) {
                                    qlcSms1 = qlcSms
                                    sendYiDongSms2(qlcSms.number)
                                } else {
                                    if (qlcSms.body.contains("根据您的授权查询")) {
                                        qlcSms1 = qlcSms
                                        analyseSms(qlcSms1!!)
                                    } else {
                                        sweetAlertDialog.dismissWithAnimation()
                                        showError(qlcSms.body)
                                    }
                                }
                            } else if (qlcSms.number.equals("10010")) {
                                //中国联通
                                qlcSms1 = qlcSms
                                analyseSms(qlcSms1!!)
                            } else if (qlcSms.number.contains("10001")) {
                                //中国电信
                                qlcSms1 = qlcSms
                                analyseSms(qlcSms1!!)
//                                    sendDianXinSms2()
                            }
                        }
                    }
                    KLog.i(qlcSms)
                }
            }
        }
    }

    fun showError(string: String) {
        alert(string) {
            positiveButton(getString(R.string.confirm)) {
                qlcSms1 = null
                qlcSms2 = null
                dismiss()
            }
        }.show()
    }

    fun analyseSms(qLcSms: QLcSms) {
        if (qLcSms.body.contains("根据您的授权查询")) {
            showViewAnimation(view1Main, { toAnimation2() })
            tvWalletAddess.postDelayed({
//                payQgas(qLcSms)
                reportSms(qLcSms)
            }, 4000)
        } else {
            sweetAlertDialog.dismissWithAnimation()
            showError(qLcSms.body)
            qlcSms1 = null
            qlcSms2 = null
        }
    }

    fun payQgas(qLcSms: QLcSms) {
        showViewAnimation(view2Main, { toAnimation3() })
        thread {
            QlcReceiveUtils.sendQGas(sendQlcWallet, ConstantValue.blackHoldAddress, "1", "", true, object : SendBack {
                override fun send(suceess: String) {
                    if ("".equals(suceess)) {
                        runOnUiThread {
                            toast(getString(R.string.send_qgas_error, "QGAS"))
                            sweetAlertDialog.dismissWithAnimation()
                        }
                    } else {
                        runOnUiThread {
//                            reportSms(suceess, qLcSms)
                        }
                    }
                }

            })
        }
    }

    fun reportSms(qLcSms: QLcSms) {
        showViewAnimation(view2Main, { toAnimation3() })
        var infoMap = hashMapOf<String, String>()
//        infoMap["qgasAmount"] = "1"
//        infoMap["qgasHash"] = txid
        infoMap["isp"] = getIsp()
        infoMap["phoneHash"] = MD5Util.getStringMD5("+86" + phoneNumber.replace("+86", ""))
        infoMap["sms"] = qLcSms.body
        infoMap["smsHash"] = MD5Util.getStringMD5(qLcSms.body)
        infoMap["userAccount"] = if (ConstantValue.currentUser != null) {
            ConstantValue.currentUser.account
        } else {
            ""
        }
        mPresenter.smsReport(infoMap)
    }


    override fun reportBack(smsReport: SmsReport) {
        completeAnimation()
        startActivityForResult(Intent(this, SmsVourchActivity::class.java).putExtra("report", smsReport.report), 2)

    }

    fun getIsp(): String {
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            //中国移动
            return "中国移动"
        } else if (imsi.startsWith("46001")) {
            //中国联通
            return "中国联通"
        } else if (imsi.startsWith("46003")) {
            //中国电信
            return "中国电信"
        }
        return ""
    }

    override fun initView() {
        setContentView(R.layout.activity_place_visit)
        animationView = layoutInflater.inflate(R.layout.alert_myd_check_animation, null, false)
        ivLoad1 = animationView.findViewById<ImageView>(R.id.ivLoad1)
        ivLoad2 = animationView.findViewById<ImageView>(R.id.ivLoad2)
        ivLoad3 = animationView.findViewById<ImageView>(R.id.ivLoad3)
        ivLoad4 = animationView.findViewById<ImageView>(R.id.ivLoad4)
        view1 = animationView.findViewById(R.id.view1)
        view2 = animationView.findViewById(R.id.view2)
        view3 = animationView.findViewById(R.id.view3)
        view1Main = animationView.findViewById(R.id.view1Main)
        view2Main = animationView.findViewById(R.id.view2Main)
        view3Main = animationView.findViewById(R.id.view3Main)
//        tvWalletName = animationView.findViewById(R.id.tvWalletName)
//        tvWalletAddress = animationView.findViewById(R.id.tvWalletAddress)
        sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog.setView(animationView)
        sweetAlertDialog.setOnBackListener {
            sweetAlertDialog.dismissWithAnimation()
        }
        saHalf = ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        saHalf.setDuration(400)

        sa1 = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(1000)
        etVCode.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                KLog.i("失去焦点")
                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(tvPhone1.getWindowToken(), 0)
            }
        }
    }


    fun showViewAnimation(view1: View, animationFun: () -> Unit) {
        var viewSa = ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        viewSa.setDuration(1000)
        viewSa.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                view1.visibility = View.VISIBLE
                animationFun()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        view1.startAnimation(viewSa)
    }

    fun scaleAnimationTo1(imageView: ImageView) {
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(sa1)
    }

    fun scaleAnimationToHalf(imageView: ImageView) {
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(saHalf)
    }

    override fun initData() {
        title.text = getString(R.string.itinerary_check)
        title.postDelayed({
            getPermission()
        }, 500)
//        llSelectQlcWallet.setOnClickListener {
//            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            if (imm != null) {
//                imm.hideSoftInputFromWindow(view.getWindowToken(),0)
//            }
//
//            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
//            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
//            intent1.putExtra("select", true)
//            startActivityForResult(intent1, 1)
//            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
//        }
        checkBlockChain.setOnClickListener {
            if ("".equals(imsi)) {
                toast(getString(R.string.no_valid_mobile_number_was_found))
                return@setOnClickListener
            }
            if (etVCode.text.toString().length != 4) {
                toast(getString(R.string.please_input_the_last_4_digits_on_the_id_card))
                return@setOnClickListener
            }
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(tvPhone1.getWindowToken(), 0)
            startCheckBlockChain()
        }

        var uri = Uri.parse("content://sms")
        var observer = SmsObserver(this, handler)
        getContentResolver().registerContentObserver(uri, true, observer)

        sendPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0)
        deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0)
        sendBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> {

                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {

                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {

                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {

                    }
                }
            }
        }
        registerReceiver(sendBroadcastReceiver, IntentFilter(SEND_SMS_ACTION))
        recevieBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //toast("收件人接收短信成功")
            }

        }
        registerReceiver(recevieBroadcastReceiver, IntentFilter(DELIVERED_SMS_ACTION))
    }

    lateinit var sendBroadcastReceiver: BroadcastReceiver
    lateinit var recevieBroadcastReceiver: BroadcastReceiver
    var SEND_SMS_ACTION = "SEND_SMS_ACTION"
    var sentIntent = Intent(SEND_SMS_ACTION)
    var DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"
    var deliverIntent = Intent(DELIVERED_SMS_ACTION)
    lateinit var sendPI: PendingIntent
    lateinit var deliverPI: PendingIntent

    lateinit var animationView: View
    lateinit var ivLoad1: ImageView
    lateinit var ivLoad2: ImageView
    lateinit var ivLoad3: ImageView
    lateinit var ivLoad4: ImageView
    lateinit var view1: View
    lateinit var view2: View
    lateinit var view3: View
    lateinit var view1Main: View
    lateinit var view2Main: View
    lateinit var view3Main: View
//    lateinit var tvWalletName: TextView
//    lateinit var tvWalletAddress: TextView

    lateinit var saHalf: ScaleAnimation
    lateinit var sa1: ScaleAnimation

    lateinit var selectedPhone: PhoneNumber

    var phoneNumber = ""
    var imsi = ""
    fun startCheckBlockChain() {
        if ("".equals(phoneNumber)) {
            phoneNumber = etPhone.text.toString()
        }
        if ("".equals(phoneNumber)) {
            toast(getString(R.string.please_input_the_mobile_number))
            return
        }
        sweetAlertDialog.show()
        startAnimation()
//        ivLoad1.postDelayed({
//            showViewAnimation(view1Main, {toAnimation2()})
//        }, 5000)
//        ivLoad1.postDelayed({
//            showViewAnimation(view2Main, {toAnimation3()})
//        }, 10000)
//        ivLoad1.postDelayed({
//            showViewAnimation(view3Main, {toAnimation4()})
//        }, 15000)
//        ivLoad1.postDelayed({
//            completeAnimation()
//        }, 20000)
        sendSms()
    }

    override fun onBackPressed() {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(tvPhone1.getWindowToken(), 0)
        super.onBackPressed()
    }


    fun sendSms() {
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            //中国移动
            sendYiDongSms1()
        } else if (imsi.startsWith("46001")) {
            //中国联通
            sendLianTongSms()
        } else if (imsi.startsWith("46003")) {
            //中国电信
            sendDianXinSms1()
        }
    }

    fun sendLianTongSms() {
        var smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage("10010", null, "CXMYD#${etVCode.text}", sendPI, deliverPI)
    }

    fun sendYiDongSms1() {
        var smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage("10086", null, "CXMYD", sendPI, deliverPI)
    }

    fun sendYiDongSms2(number: String) {
        var smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, etVCode.text.toString(), sendPI, deliverPI)
    }

    fun sendDianXinSms1() {
        var smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage("10001", null, "CXMYD#${etVCode.text}", sendPI, deliverPI)
    }

    fun sendDianXinSms2() {
        var smsManager = android.telephony.SmsManager.getDefault()
        smsManager.sendTextMessage("10001", null, "Y", sendPI, deliverPI)
    }

    var qlcSms1: QLcSms? = null
    var qlcSms2: QLcSms? = null
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handlerSms(qLcSms: QLcSms) {
        KLog.i(qLcSms.toString())
//        if (selectedPhone.imsi.startsWith("46000") || selectedPhone.imsi.startsWith("46002")) {
//            //中国移动
//            sendYiDongSms1()
//        } else if (selectedPhone.imsi.startsWith("46001")) {
//            //中国联通
//            if (!this::qlcSms1.isInitialized) {
//                qlcSms1 = qLcSms
//                showViewAnimation(view1Main, {toAnimation2()})
//            }
//        } else if (selectedPhone.imsi.startsWith("46003")) {
//            //中国电信
//            sendDianXinSms1()
//        }
    }

    fun startAnimation() {
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(ivLoad1)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(ivLoad1)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        ivLoad1.startAnimation(sa1)
    }

    fun toAnimation2() {
        ivLoad1.setImageResource(R.mipmap.background_success)
        ivLoad2.setImageResource(R.mipmap.background_load)
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(ivLoad2)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(ivLoad2)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        ivLoad2.startAnimation(sa1)
    }

    fun toAnimation3() {
        ivLoad2.setImageResource(R.mipmap.background_success)
        ivLoad3.setImageResource(R.mipmap.background_load)
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(ivLoad3)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(ivLoad3)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        ivLoad3.startAnimation(sa1)
    }

    fun toAnimation4() {
        ivLoad3.setImageResource(R.mipmap.background_success)
        ivLoad4.setImageResource(R.mipmap.background_load)
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(ivLoad4)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(ivLoad4)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        ivLoad4.startAnimation(sa1)
    }

    fun completeAnimation() {
        ivLoad3.setImageResource(R.mipmap.background_success)
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        ivLoad1.postDelayed({
            sweetAlertDialog.dismissWithAnimation()
        }, 1000)
    }

    lateinit var sweetAlertDialog: SweetAlertDialog

    lateinit var sendQlcWallet: QLCAccount
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            sendQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
            tvWalletAddess.text = sendQlcWallet.address
            getWalletBalance()
        }
        if (requestCode == 2) {
            onBackPressed()
        }
    }

    var qgasCount = 0.toBigDecimal()
    fun getWalletBalance() {
        QLCAPI().walletGetBalance(sendQlcWallet.address, "", object : QLCAPI.BalanceInter {
            override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                if (error == null) {
                    KLog.i("发射2")
                    if (baseResult!!.filter { it.symbol.equals("QGAS") }.size > 0) {
                        qgasCount = baseResult!!.filter { it.symbol.equals("QGAS") }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN)
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": ${qgasCount.stripTrailingZeros().toPlainString()} ${"QGAS"}"
                        }
                    } else {
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": -/- ${"QGAS"}"
                        }
                        qgasCount = BigDecimal.ZERO
                        toast(getString(R.string.not_enough) + " ${"QGAS"}")
                    }
                } else {
                    runOnUiThread {
                        tvQGASBalance.text = getString(R.string.balance) + ": -/- ${"QGAS"}"
                    }
                    qgasCount = BigDecimal.ZERO
                    toast(getString(R.string.not_enough) + " ${"QGAS"}")
                }
            }
        })
    }

    override fun setupActivityComponent() {
        DaggerPlaceVisitComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .placeVisitModule(PlaceVisitModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: PlaceVisitContract.PlaceVisitContractPresenter) {
        mPresenter = presenter as PlaceVisitPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    val permission = object : PermissionListener {
        override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
            KLog.i("权限成功")
            getSimInfo()

        }

        override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
            KLog.i("权限失败")
        }

    }

    fun getPermission() {
        AndPermission.with(this)
                .requestCode(101)
                .permission(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE
                )
                .rationale { requestCode, rationale ->

                }
                .callback(permission)
                .start()
    }

    var phoneNumberList = mutableListOf<PhoneNumber>()
    fun getSimInfo() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager // 取得相关系统服务
        phoneNumber = telephonyManager.line1Number
        if ("".equals(phoneNumber)) {
            llPhone.visibility = View.GONE
            etPhone.visibility = View.VISIBLE
        } else {
            llPhone.visibility = View.VISIBLE
            etPhone.visibility = View.GONE
            tvPhone1.text = phoneNumber.replace("+86", "")
        }

        imsi = telephonyManager.subscriberId
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            //中国移动
            tvIsp1.text = getString(R.string.china_mobile)
        } else if (imsi.startsWith("46001")) {
            //中国联通
            tvIsp1.text = getString(R.string.china_unicom)
        } else if (imsi.startsWith("46003")) {
            //中国电信
            tvIsp1.text = getString(R.string.china_telecom)
        }

//        val uri: Uri
//        uri = Uri.parse("content://telephony/siminfo")
//        var cursor: Cursor? = null
//        val contentResolver = applicationContext.contentResolver
//        cursor = contentResolver.query(
//                uri,
//                arrayOf("_id", "sim_id", "icc_id", "display_name", "number"),
//                "0=0",
//                arrayOf(),
//                null
//        )
//        if (null != cursor) {
//            while (cursor.moveToNext()) {
//                val icc_id = cursor.getString(cursor.getColumnIndex("icc_id"))
//                val display_name = cursor.getString(cursor.getColumnIndex("display_name"))
//                val sim_id = cursor.getInt(cursor.getColumnIndex("sim_id"))
//                val _id = cursor.getInt(cursor.getColumnIndex("_id"))
//                val simNumber = cursor.getString(cursor.getColumnIndex("number"))
//                val number = getLine1Number(_id)
//                val imsi = getSubscriberId(_id)
//                KLog.i("icc_id-->$icc_id")
//                KLog.i("sim_id-->$sim_id")
//                KLog.i("display_name-->$display_name")
//                KLog.i("subId或者说是_id->$_id")
//                KLog.i("imsi-->${imsi}")
//                KLog.i("手机号码-->${number}")
//                KLog.i("---------------------------------")
//                if (sim_id >= 0) {
//                    phoneNumberList.add(PhoneNumber(_id, number, imsi, sim_id, simNumber, phoneNumberList.size))
//                }
//            }
//            setPhoneNumber()
//        }

    }

    fun setPhoneNumber() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager // 取得相关系统服务
        KLog.i(telephonyManager.subscriberId)
        KLog.i(telephonyManager.line1Number)
//        toast("默认手机号码：" + telephonyManager.line1Number)
        if (phoneNumberList.size == 1) {
            llPhone2.visibility = View.INVISIBLE
            llKa2.visibility = View.INVISIBLE
            if ("".equals(phoneNumberList[0].phoneNumber)) {
                llPhone.visibility = View.GONE
                etPhone.visibility = View.VISIBLE
            } else {
                llPhone.visibility = View.VISIBLE
                etPhone.visibility = View.GONE
            }
        }
        if (phoneNumberList.size == 2 && ("".equals(phoneNumberList[0].phoneNumber) || "".equals(phoneNumberList[1].phoneNumber))) {
            llPhone.visibility = View.GONE
            etPhone.visibility = View.VISIBLE
        } else {
            llPhone.visibility = View.VISIBLE
            etPhone.visibility = View.GONE
        }

        phoneNumberList.forEachIndexed { index, phoneNumber ->
            KLog.i(phoneNumber.toString())
            if (index == 0) {
                selectedPhone = phoneNumber
                tvPhone1.setText(phoneNumber.phoneNumber.replace("+86", ""))
                tvPhone1.setTextColor(resources.getColor(R.color.color_37343c))
                if (phoneNumber.imsi.startsWith("46000") || phoneNumber.imsi.startsWith("46002")) {
                    //中国移动
                    tvIsp1.text = "移动"
                } else if (phoneNumber.imsi.startsWith("46001")) {
                    //中国联通
                    tvIsp1.text = "联通"
                } else if (phoneNumber.imsi.startsWith("46003")) {
                    //中国电信
                    tvIsp1.text = "电信"
                }
            }
            if (index == 1) {
                tvPhone2.setText(phoneNumber.phoneNumber.replace("+86", ""))
                tvPhone2.setTextColor(resources.getColor(R.color.color_cccccc))
                if (phoneNumber.imsi.startsWith("46000") || phoneNumber.imsi.startsWith("46002")) {
                    //中国移动
                    tvIsp2.text = "移动"
                } else if (phoneNumber.imsi.startsWith("46001")) {
                    //中国联通
                    tvIsp2.text = "联通"
                } else if (phoneNumber.imsi.startsWith("46003")) {
                    //中国电信
                    tvIsp2.text = "电信"
                }
            }
        }
        llKa1.setOnClickListener {
            selectedPhone = phoneNumberList[0]
            tvPhone1.setTextColor(resources.getColor(R.color.color_37343c))
            tvPhone2.setTextColor(resources.getColor(R.color.color_cccccc))
            ivKa1.setImageResource(R.mipmap.selected_h)
            ivKa2.setImageResource(R.mipmap.icon_default)
        }
        llKa2.setOnClickListener {
            selectedPhone = phoneNumberList[1]
            tvPhone1.setTextColor(resources.getColor(R.color.color_cccccc))
            tvPhone2.setTextColor(resources.getColor(R.color.color_37343c))
            ivKa1.setImageResource(R.mipmap.icon_default)
            ivKa2.setImageResource(R.mipmap.selected_h)
        }
    }

    fun getSubscriberId(subId: Int): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager // 取得相关系统服务
        var telephonyManagerClass: Class<*>? = null
        var imsi: String = ""
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getMethod("getSubscriberId", Int::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId) as String
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getMethod("getSubscriberId", Long::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId.toLong()) as String
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("Q_M", "IMSI--$imsi")
        return imsi
    }

    fun getLine1Number(subId: Int): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager // 取得相关系统服务
        var telephonyManagerClass: Class<*>? = null
        var imsi = ""
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getDeclaredMethod("getLine1Number", Int::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId) as String
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getDeclaredMethod("getLine1Number", Long::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId.toLong()) as String
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if ("".equals(imsi)) {
            imsi = getLine1NumberForSubscriber(subId)
        }
        Log.d("Q_M", "IMSI--$imsi")
        return imsi
    }

    fun getLine1NumberForSubscriber(subId: Int): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager // 取得相关系统服务
        var telephonyManagerClass: Class<*>? = null
        var imsi = ""
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getDeclaredMethod("getLine1NumberForSubscriber", Int::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId) as String
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                val method = telephonyManagerClass.getDeclaredMethod("getLine1NumberForSubscriber", Long::class.javaPrimitiveType)
                imsi = method.invoke(telephonyManager, subId.toLong()) as String
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("Q_M", "IMSI--$imsi")
        return imsi
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        unregisterReceiver(sendBroadcastReceiver)
        unregisterReceiver(recevieBroadcastReceiver)
        super.onDestroy()
    }

}
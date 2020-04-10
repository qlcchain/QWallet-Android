package com.stratagile.qlink.ui.activity.topup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.runDelayedOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupConfirmGroupOrderComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupConfirmGroupOrderContract
import com.stratagile.qlink.ui.activity.topup.module.TopupConfirmGroupOrderModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupConfirmGroupOrderPresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.*
import com.yanzhenjie.alertdialog.AlertDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RationaleListener
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.*
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.ivProduct1
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.rlAvatar
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvConfirm
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvGroupInfo
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvIsp
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvNeedPartners
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvOperator
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvPrice
import kotlinx.android.synthetic.main.activity_topup_confirm_group_order.tvRemainTime
import kotlinx.android.synthetic.main.activity_topup_product_detail.*
import java.io.File
import java.math.BigDecimal
import java.util.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2020/02/13 16:15:05
 */

class TopupConfirmGroupOrderActivity : BaseActivity(), TopupConfirmGroupOrderContract.View {

    @Inject
    internal lateinit var mPresenter: TopupConfirmGroupOrderPresenter

    lateinit var topupBean: TopupProduct.ProductListBean
    lateinit var selectToken: PayToken.PayTokenListBean
    lateinit var selectedGroup: TopupGroupList.GroupListBean

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_confirm_group_order)
        ivContact.setOnClickListener {
            getContactPermission()
        }
    }

    fun getContactPermission() {
        AndPermission.with(this)
                .requestCode(101)
                .permission(
                        Manifest.permission.READ_CONTACTS
                )
                .rationale(object : RationaleListener {
                    override fun showRequestPermissionRationale(requestCode: Int, rationale: Rationale) {
                        AlertDialog.newBuilder(this@TopupConfirmGroupOrderActivity)
                                .setTitle(AppConfig.getInstance().resources.getString(R.string.Permission_Requeset))
                                .setMessage(AppConfig.getInstance().resources.getString(R.string.We_Need_Some_Permission_to_continue))
                                .setPositiveButton(AppConfig.getInstance().resources.getString(R.string.Agree)) { dialog, which -> rationale.resume() }
                                .setNegativeButton(AppConfig.getInstance().resources.getString(R.string.Reject)) { dialog, which -> rationale.cancel() }.show()
                    }
                })
                .callback(permission)
                .start()
    }

    private val permission = object : PermissionListener {
        override fun onSucceed(requestCode: Int, grantedPermissions: List<String>) {
            //            LocalAssetsUtils.updateGreanDaoFromLocal();
            // 权限申请成功回调。
            if (requestCode == 101) {
//                var phoneList = PhoneUtil(this@QurryMobileActivity).phone
////                phoneList.forEach {
////                    KLog.i(it.toString())
////                }
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = ContactsContract.Contacts.CONTENT_TYPE
                startActivityForResult(intent, 20)
            }
        }

        override fun onFailed(requestCode: Int, deniedPermissions: List<String>) {
            // 权限申请失败回调。
            if (requestCode == 101) {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20 && resultCode == Activity.RESULT_OK) {
            var contactData = data!!.getData()
            var c = managedQuery(contactData, null, null, null, null)
            if (c.moveToFirst()) {
                var name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                var contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                var phoneNumber = ""
                if (hasPhone.equals("1")) {
                    hasPhone = "true"
                } else {
                    hasPhone = "false"
                }
                if (hasPhone.toBoolean()) {
                    var phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                    + contactId,
                            null,
                            null)
                    if (phones != null) {
                        while (phones.moveToNext()) {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                        phones.close()
                    }
                }
                KLog.i(name)
                KLog.i(phoneNumber)
                val area = Gson().fromJson<ArrayList<Area>>(FileUtil.getJson(this, "area.json"), object : TypeToken<ArrayList<Area?>?>() {}.type)
                var phone = ""
                area.forEach {
                    if (phoneNumber.contains(it.code + " ")) {
                        etContact.setText(phoneNumber.replace(it.code + " ", "").replace(" ", ""))
                        etContact.setSelection(etContact.text.toString().length)
                        phone = phoneNumber.replace(it.code + " ", "").replace(" ", "")
                        return@forEach
                    }
                }
                if ("".equals(phone)) {
                    etContact.setText(phoneNumber.replace(" ", ""))
                    etContact.setSelection(etContact.text.toString().length)
                }
            }
        }
    }


    override fun initData() {
        title.text = getString(R.string.confirm_order)

        topupBean = intent.getParcelableExtra("productBean")
        selectToken = intent.getParcelableExtra("selectedPayToken")
        if (intent.hasExtra("phoneNumber")) {
            etContact.setText(intent.getStringExtra("phoneNumber"))
            etContact.setSelection(etContact.text.toString().length)
        }
        if (intent.hasExtra("buySelf")) {
            llGroup.visibility = View.GONE
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
            tvPrice.text = zhifubishuliang.stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol + "+" + dikoubishuliang.stripTrailingZeros().toPlainString() + selectToken.symbol
        } else {
            selectedGroup = intent.getParcelableExtra("group")
            var deductionTokenPrice = 0.toDouble()
            if ("CNY".equals(topupBean.payFiat)) {
                deductionTokenPrice = selectToken.price
            } else if ("USD".equals(topupBean.payFiat)) {
                deductionTokenPrice = selectToken.usdPrice
            }
            var dikoubijine = topupBean.payFiatAmount.toBigDecimal().multiply(topupBean.qgasDiscount.toBigDecimal().multiply(selectedGroup.discount.toBigDecimal()))
            var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

            var zhifufabijine = topupBean.payFiatAmount.toBigDecimal().multiply(selectedGroup.discount.toBigDecimal())
            var zhifudaibijine = zhifufabijine - dikoubijine
            var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupBean.payFiat)) {
                topupBean.payTokenCnyPrice.toBigDecimal()
            } else {
                topupBean.payTokenUsdPrice.toBigDecimal()
            }, 3, BigDecimal.ROUND_HALF_UP)
            tvPrice.text = zhifubishuliang.stripTrailingZeros().toPlainString() + topupBean.payTokenSymbol + "+" + dikoubishuliang.stripTrailingZeros().toPlainString() + selectToken.symbol
        }

        Glide.with(this)
                .load(AppConfig.instance.baseUrl + topupBean.imgPath.replace("/dapp", ""))
                .apply(AppConfig.getInstance().optionsTopup)
                .into(ivProduct1)
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            KLog.i("设置为英文")
            tvIsp.text = getString(R.string.operator) + ": " + topupBean.ispEn
            tvArea.text = getString(R.string.region) + ": " + topupBean.countryEn
            TextUtil.setGroupInfo(topupBean.countryEn, topupBean.ispEn + " " + topupBean.localFiatAmount + topupBean.localFiat + "\n" + topupBean.explainEn, tvOperator)
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 1) {
            KLog.i("设置为中文")
            TextUtil.setGroupInfo(topupBean.country, topupBean.isp + " " + topupBean.localFiatAmount + topupBean.localFiat + "\n" + topupBean.explain, tvOperator)
            tvIsp.text = getString(R.string.operator) + ": " + topupBean.isp
            tvArea.text = getString(R.string.region) + ": " + topupBean.country
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 2) {
            KLog.i("设置为印度尼西亚")
            TextUtil.setGroupInfo(topupBean.countryEn, topupBean.ispEn + " " + topupBean.localFiatAmount + topupBean.localFiat + "\n" + topupBean.explainEn, tvOperator)
            tvIsp.text = getString(R.string.operator) + ": " + topupBean.ispEn
            tvArea.text = getString(R.string.region) + ": " + topupBean.countryEn
        }
        var isCn = true
        isCn = SpUtil.getInt(this, ConstantValue.Language, -1) == 1

//        if (isCn) {
//            etContact.setHint(getString(R.string.only_available_for_xxx_subscribers, topupBean.isp))
//        } else {
//            etContact.setHint(getString(R.string.only_available_for_xxx_subscribers, topupBean.ispEn))
//        }

        if (intent.hasExtra("buySelf")) {
            llGroup.visibility = View.GONE

        } else {
            selectedGroup = intent.getParcelableExtra("group")

            tvGroupType.text = getString(R.string._off_discount_partners, if (isCn) {
                selectedGroup.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()
            } else {
                (1.toBigDecimal() - selectedGroup.discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString()
            }, selectedGroup.numberOfPeople.toString())
            tvNeedPartners.text = getString(R.string._more_partner_needed, (selectedGroup.numberOfPeople - selectedGroup.joined).toString())
            tvGroupInfo.text = getString(R.string._off_discount_partners, if (isCn) {
                selectedGroup.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()
            } else {
                (1.toBigDecimal() - selectedGroup.discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString()
            }, selectedGroup.numberOfPeople.toString())
            tvRemainTime.setText(getString(R.string.valid_till) + TimeUtil.getOrderTime(TimeUtil.timeStamp(selectedGroup.createDate) + (selectedGroup.duration * 60 * 1000)))

            var headList = arrayListOf<String>()
            selectedGroup.items?.forEach {
                headList.add(it.head)
            }
            headList.add(selectedGroup.head)
            var allMargin = UIUtils.dip2px(20f, this@TopupConfirmGroupOrderActivity)

            var leftMargin = 0
            if (headList.size > 1) {
                leftMargin = allMargin / (headList.size - 1)
            }
            headList.forEachIndexed { index, s ->
                var imageView = ImageView(this@TopupConfirmGroupOrderActivity)
                var lp = RelativeLayout.LayoutParams(UIUtils.dip2px(32f, this@TopupConfirmGroupOrderActivity), UIUtils.dip2px(32f, this@TopupConfirmGroupOrderActivity))
                lp.leftMargin = index * leftMargin
                imageView.layoutParams = lp
                KLog.i(AppConfig.instance.baseUrl + s)
                Glide.with(this@TopupConfirmGroupOrderActivity)
                        .load(MainAPI.MainBASE_URL + s)
                        .apply(AppConfig.getInstance().optionsWhiteColor)
                        .into(imageView)
                rlAvatar.addView(imageView)


                if (index == headList.size - 1) {
                    var imageView1 = ImageView(this@TopupConfirmGroupOrderActivity)
                    var lp1 = RelativeLayout.LayoutParams(UIUtils.dip2px(12f, this@TopupConfirmGroupOrderActivity), UIUtils.dip2px(12f, this@TopupConfirmGroupOrderActivity))
                    lp1.leftMargin = index * leftMargin + (UIUtils.dip2px(32f, this@TopupConfirmGroupOrderActivity) - UIUtils.dip2px(12f, this@TopupConfirmGroupOrderActivity))
                    lp1.topMargin = UIUtils.dip2px(32f, this@TopupConfirmGroupOrderActivity) - UIUtils.dip2px(12f, this@TopupConfirmGroupOrderActivity)
                    imageView1.layoutParams = lp1
                    Glide.with(this@TopupConfirmGroupOrderActivity)
                            .load(R.mipmap.label_regimental)
                            .apply(RequestOptions().centerCrop())
                            .into(imageView1)
                    rlAvatar.addView(imageView1)
                }
            }
        }

        tvConfirm.setOnClickListener {
            if ("".equals(etContact.text.toString().trim())) {
                toast(getString(R.string.please_input_phone_number))
            } else {
                if (intent.hasExtra("buySelf")) {
                    buySelf()
                } else {
                    alert(getString(R.string.a_cahrge_of_will_cost_paytoken_and_deduction_token, topupBean.localFiatAmount.toString(), selectedGroup.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString(), selectedGroup.payToken, selectedGroup.deductionTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString(), selectedGroup.deductionToken, topupBean.localFiat)) {
                        negativeButton(getString(R.string.cancel)) { dismiss() }
                        positiveButton(getString(R.string.buy_topup)) {
                            joinGroup()
                        }
                    }.show()
                }
            }
        }
    }

    fun joinGroup() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
        map["groupId"] = selectedGroup.id
        map["phoneNumber"] = etContact.text.toString().trim()
        mPresenter.topupJoinGroup(map)
    }

    override fun joinGroupBack(topupJoinGroup: TopupJoinGroup) {
        closeProgressDialog()
        initData()

        when (OtcUtils.parseChain(topupJoinGroup.item.deductionTokenChain)) {
            AllWallet.WalletType.QlcWallet -> {
                if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                    var payIntent = Intent(this@TopupConfirmGroupOrderActivity, TopupDeductionQlcChainActivity::class.java)
                    payIntent.putExtra("groupBean", topupJoinGroup.item)
                    payIntent.putExtra("isGroup", true)
                    startActivityForResult(payIntent, 1)
                    finish()
                } else {
                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                        negativeButton(getString(R.string.cancel)) { dismiss() }
                        positiveButton(getString(R.string.create)) { startActivity(Intent(this@TopupConfirmGroupOrderActivity, SelectWalletTypeActivity::class.java)) }
                    }.show()
                }
            }
            AllWallet.WalletType.EthWallet -> {
                var payIntent = Intent(this@TopupConfirmGroupOrderActivity, TopupDeductionEthChainActivity::class.java)
                payIntent.putExtra("groupBean", topupJoinGroup.item)
                payIntent.putExtra("isGroup", true)
                startActivityForResult(payIntent, 1)
                finish()
            }
        }
    }

    override fun setupActivityComponent() {
        DaggerTopupConfirmGroupOrderComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupConfirmGroupOrderModule(TopupConfirmGroupOrderModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupConfirmGroupOrderContract.TopupConfirmGroupOrderContractPresenter) {
        mPresenter = presenter as TopupConfirmGroupOrderPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    fun buySelf() {
        FireBaseUtils.logEvent(this, FireBaseUtils.Topup_order_confirm)
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
            negativeButton(getString(R.string.cancel)) {
                FireBaseUtils.logEvent(this@TopupConfirmGroupOrderActivity, FireBaseUtils.Topup_Confirm_Cancel)
                dismiss()
            }
            positiveButton(getString(R.string.buy_topup)) {
                if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                    FireBaseUtils.logEvent(this@TopupConfirmGroupOrderActivity, FireBaseUtils.Topup_Confirm_buy)
                    generateTopupOrder(topupBean)
                } else {
                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                        negativeButton(getString(R.string.cancel)) {
                            dismiss()
                        }
                        positiveButton(getString(R.string.create)) {
                            startActivity(Intent(this@TopupConfirmGroupOrderActivity, SelectWalletTypeActivity::class.java))
                        }
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
        map["phoneNumber"] = etContact.text.toString().trim()
        map["deductionTokenId"] = selectToken.id
        mPresenter.createTopupOrder(map)
    }

    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {
        closeProgressDialog()
        when (OtcUtils.parseChain(selectToken.chain)) {
            AllWallet.WalletType.QlcWallet -> {
                val intent1 = Intent(this, TopupDeductionQlcChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
                setResult(Activity.RESULT_OK)
                finish()
            }
            AllWallet.WalletType.EthWallet -> {
                val intent1 = Intent(this, TopupDeductionEthChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

}
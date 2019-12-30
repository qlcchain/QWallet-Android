package com.stratagile.qlink.ui.activity.topup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.inputManager
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.qlinkcom
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerQurryMobileComponent
import com.stratagile.qlink.ui.activity.topup.contract.QurryMobileContract
import com.stratagile.qlink.ui.activity.topup.module.QurryMobileModule
import com.stratagile.qlink.ui.activity.topup.presenter.QurryMobilePresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.PayTokenAdapter
import com.stratagile.qlink.ui.adapter.topup.PayTokenDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupAbleAdapter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.view.CustomPopWindow
import com.vondear.rxtools.RxKeyboardTool
import com.yanzhenjie.alertdialog.AlertDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RationaleListener
import kotlinx.android.synthetic.main.activity_qurry_mobile.*
import java.io.File
import java.math.BigDecimal
import java.util.*

import javax.inject.Inject;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/24 14:50:33
 */

class QurryMobileActivity : BaseActivity(), QurryMobileContract.View {
    override fun setPayTokenAdapter(payToken: PayToken) {
        payTokenRecyclerView.visibility = View.GONE
        payToken.payTokenList[0].isSelected = true
        selectedPayToken = payToken.payTokenList[0]
        payTokenAdapter.setNewData(payToken.payTokenList)
        topupAbleAdapter!!.payToken = selectedPayToken
    }

    override fun createTopupOrderError() {
        closeProgressDialog()
    }

    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {
        closeProgressDialog()
        when (OtcUtils.parseChain(selectedPayToken.chain)) {
            AllWallet.WalletType.QlcWallet -> {
                val intent1 = Intent(this@QurryMobileActivity, TopupDeductionQlcChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            }
            AllWallet.WalletType.EthWallet -> {
                val intent1 = Intent(this@QurryMobileActivity, TopupDeductionEthChainActivity::class.java)
                intent1.putExtra("order", topupOrder.order)
                startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            }
        }
    }

    override fun setProductList(topupProduct: TopupProduct) {
        mPresenter.getCountryList(hashMapOf())
        var productList = arrayListOf<TopupProduct.ProductListBean>()
        if (topupProduct.productList.size == 0) {
            noProduct.visibility = View.VISIBLE
            tvFound.visibility = View.GONE
            payTokenRecyclerView.visibility = View.GONE
        } else {
            payTokenRecyclerView.visibility = View.VISIBLE
            noProduct.visibility = View.GONE
            tvFound.visibility = View.VISIBLE
        }
        topupProduct.productList.forEach { itss ->
            var payFiatAmountList = itss.payFiatAmount.split(",")
            itss.amountOfMoney.split(",").forEachIndexed { index, it ->
                var bean = TopupProduct.ProductListBean()
                bean.amountOfMoney = it
                bean.payFiatAmount = payFiatAmountList[index]
                bean.country = itss.country
                bean.province = itss.province
                bean.isp = itss.isp
                bean.name = itss.name
                bean.nameEn = itss.nameEn
                bean.discount = itss.discount
                bean.id = itss.id
                bean.qgasDiscount = itss.qgasDiscount
                bean.ispEn = itss.ispEn
                bean.provinceEn = itss.provinceEn
                bean.countryEn = itss.countryEn
                bean.descriptionEn = itss.descriptionEn
                bean.description = itss.description
                bean.explainEn = itss.explainEn
                bean.explain = itss.explain

                bean.localFiat = itss.localFiat
                bean.payWay = itss.payWay
                bean.payTokenSymbol = itss.payTokenSymbol
                bean.payFiat = itss.payFiat
                bean.payTokenCnyPrice = itss.payTokenCnyPrice


                productList.add(bean)
            }
        }
        topupAbleAdapter!!.setNewData(productList)
    }

    @Inject
    internal lateinit var mPresenter: QurryMobilePresenter
    var topupAbleAdapter: TopupAbleAdapter? = null
    lateinit var selectedPayToken: PayToken.PayTokenListBean
    lateinit var payTokenAdapter: PayTokenAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_qurry_mobile)
        llSelectCountry.setOnClickListener {
            val inputmanger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(etContact.getWindowToken(), 0)
            startActivityForResult(Intent(this, SelectAreaActivity::class.java), 10)
        }

        topupAbleAdapter = TopupAbleAdapter(arrayListOf())
        recyclerView.adapter = topupAbleAdapter
        topupAbleAdapter!!.setOnItemClickListener { adapter, view, position ->
            if ("".equals(etContact.text.toString().trim())) {
                toast(getString(R.string.please_enter_correct_mobbile_number))
                return@setOnItemClickListener
            }
            if ("FIAT".equals(topupAbleAdapter!!.data[position].payWay)) {
                var price = topupAbleAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupAbleAdapter!!.data[position].discount.toBigDecimal()).stripTrailingZeros().toPlainString()
                var dsicountPrice = topupAbleAdapter!!.data[position].payTokenCnyPrice.toBigDecimal().multiply(topupAbleAdapter!!.data[position].discount.toBigDecimal()).stripTrailingZeros().toPlainString()
                var deductionTokenAmount = topupAbleAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupAbleAdapter!!.data[position].qgasDiscount.toBigDecimal()).divide(selectedPayToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                alert(getString(R.string.a_cahrge_of_will_cost_qgas_and_rmb, topupAbleAdapter!!.data[position].amountOfMoney.toString(), deductionTokenAmount, selectedPayToken.symbol, price)) {
                    negativeButton(getString(R.string.cancel)) { dismiss() }
                    positiveButton(getString(R.string.buy_topup)) {
                        when (OtcUtils.parseChain(selectedPayToken.chain)) {
                            AllWallet.WalletType.QlcWallet -> {
                                if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                                    generateTopupOrder(topupAbleAdapter!!.data[position])

//                                    val intent1 = Intent(this@QurryMobileActivity, TopupQlcPayActivity::class.java)
//                                    intent1.putExtra("product", topupAbleAdapter!!.data[position])
//                                    intent1.putExtra("payToken", selectedPayToken)
//                                    intent1.putExtra("areaCode", tvArea.text.toString())
//                                    intent1.putExtra("phoneNumber", etContact.text.toString())
//                                    startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
                                } else {
                                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                                        negativeButton(getString(R.string.cancel)) { dismiss() }
                                        positiveButton(getString(R.string.create)) { startActivity(Intent(this@QurryMobileActivity, SelectWalletTypeActivity::class.java)) }
                                    }.show()
                                }
                            }
                            AllWallet.WalletType.EthWallet -> {
                                if (AppConfig.instance.daoSession.ethWalletDao.loadAll().size != 0) {
                                    generateTopupOrder(topupAbleAdapter!!.data[position])

//                                    val intent1 = Intent(this@QurryMobileActivity, TopupEthPayActivity::class.java)
//                                    intent1.putExtra("product", topupAbleAdapter!!.data[position])
//                                    intent1.putExtra("payToken", selectedPayToken)
//                                    intent1.putExtra("areaCode", tvArea.text.toString())
//                                    intent1.putExtra("phoneNumber", etContact.text.toString())
//                                    startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
                                } else {
                                    alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "ETH Chain")) {
                                        negativeButton(getString(R.string.cancel)) { dismiss() }
                                        positiveButton(getString(R.string.create)) { startActivity(Intent(this@QurryMobileActivity, SelectWalletTypeActivity::class.java)) }
                                    }.show()
                                }
                            }
                            AllWallet.WalletType.NeoWallet -> {
                                val intent1 = Intent(this@QurryMobileActivity, TopupQlcPayActivity::class.java)
                                intent1.putExtra("product", topupAbleAdapter!!.data[position])
                                intent1.putExtra("payToken", selectedPayToken)
                                intent1.putExtra("areaCode", tvArea.text.toString())
                                intent1.putExtra("phoneNumber", etContact.text.toString())
                                startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
                            }
                        }
                    }
                }.show()
            } else {

                var deductionTokenPrice = 0.toDouble()
                if ("CNY".equals(topupAbleAdapter!!.data[position].payFiat)) {
                    deductionTokenPrice = selectedPayToken.price
                } else if ("USD".equals(topupAbleAdapter!!.data[position].payFiat)){
                    deductionTokenPrice = selectedPayToken.usdPrice
                }

                var dikoubijine = topupAbleAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupAbleAdapter!!.data[position].qgasDiscount.toBigDecimal())
                var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)
                var zhifufabijine = topupAbleAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupAbleAdapter!!.data[position].discount.toBigDecimal())
                var zhifudaibijine = zhifufabijine - dikoubijine
                var zhifubishuliang = zhifudaibijine.divide(topupAbleAdapter!!.data[position].payTokenCnyPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

                alert(getString(R.string.a_cahrge_of_will_cost_paytoken_and_deduction_token, topupAbleAdapter!!.data[position].amountOfMoney.toString(), zhifubishuliang.stripTrailingZeros().toPlainString(), topupAbleAdapter!!.data[position].payTokenSymbol, dikoubishuliang.stripTrailingZeros().toPlainString(), selectedPayToken.symbol)) {
                    negativeButton(getString(R.string.cancel)) { dismiss() }
                    positiveButton(getString(R.string.buy_topup)) {
                        if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
                            generateTopupOrder(topupAbleAdapter!!.data[position])
                        } else {
                            alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
                                negativeButton(getString(R.string.cancel)) { dismiss() }
                                positiveButton(getString(R.string.create)) { startActivity(Intent(this@QurryMobileActivity, SelectWalletTypeActivity::class.java)) }
                            }.show()
                        }
                    }
                }.show()

            }
        }
        payTokenAdapter = PayTokenAdapter(arrayListOf())
        payTokenRecyclerView.adapter = payTokenAdapter
        payTokenRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        payTokenRecyclerView.addItemDecoration(PayTokenDecoration(UIUtils.dip2px(12f, this)))
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(5f, this)))
        ivContact.setOnClickListener {
            getContactPermission()
        }
        qurryMobile.setOnClickListener {
//            if ("".equals(countryName)) {
//                toast(getString(R.string.please_select_country))
//                return@setOnClickListener
//            }
//            if ("".equals(etContact.text.toString().trim())) {
//                toast(getString(R.string.please_input_phone_number))
//                return@setOnClickListener
//            }
//            if ("+86".equals(country)) {
//                if (!AccountUtil.isTelephone(etContact.text.toString().trim())) {
//                    toast(getString(R.string.please_input_phone_number))
//                }
//                return@setOnClickListener
//            }
//            if ("".equals(isp)) {
//                toast(getString(R.string.please_select_operator))
//                return@setOnClickListener
//            }
            getProductList()
        }
        llSelectOperator.setOnClickListener {
            if ("".equals(countryName)) {
                toast(getString(R.string.please_select_country))
                return@setOnClickListener
            }
            getOperator(country)
//            var ispIntent = Intent(this, SelectOperatorActivity::class.java)
//            ispIntent.putExtra("area", country)
//            startActivityForResult(ispIntent, 11)
        }
        llRegion.setOnClickListener {
            if ("".equals(countryName)) {
                toast(getString(R.string.please_select_country))
                return@setOnClickListener
            }
            if ("".equals(isp)) {
                toast(getString(R.string.please_select_operator))
                return@setOnClickListener
            }
            provinceList()
//            var regionIntent = Intent(this, SelectRegionActivity::class.java)
//            regionIntent.putExtra("area", country)
//            regionIntent.putExtra("isp", isp)
//            startActivityForResult(regionIntent, 12)
        }
        payTokenAdapter.setOnItemClickListener { adapter, view, position ->
            payTokenAdapter.data.forEach {
                it.isSelected = false
            }
            payTokenAdapter.data[position].isSelected = true
            selectedPayToken = payTokenAdapter.data[position]
            payTokenAdapter.notifyDataSetChanged()
            reSetProduct()
        }
    }

    fun provinceList() {
        var map = mutableMapOf<String, String>()
        map.put("globalRoaming", country)
        map.put("isp", isp)
        mPresenter.provinceList(map)
    }

    fun getOperator(globalRoaming : String) {
        var map = mutableMapOf<String, String>()
        map.put("globalRoaming", globalRoaming)
        mPresenter.getIspList(map)
    }

    override fun setIsp(ispList: IspList) {
        PopWindowUtil.showSharePopWindow(this, tvArea, ispList.ispList, object : PopWindowUtil.OnItemSelectListener{
            override fun onSelect(content: String) {
                if (!"".equals(content)) {
                    tvIsp.text = content
                    isp = content
                }
            }

        })
    }
    override fun setProvinceList(ispList: IspList) {
        PopWindowUtil.showSharePopWindow(this, tvArea, ispList.ispList, object : PopWindowUtil.OnItemSelectListener{
            override fun onSelect(content: String) {
                if (!"".equals(content)) {
                    tvRegion.text = content
                    region = content
                }
            }

        })
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
        map["localFiatAmount"] = product.amountOfMoney
        map["phoneNumber"] = etContact.text.toString()
        map["deductionTokenId"] = selectedPayToken.id
        mPresenter.createTopupOrder(map)
    }

    fun reSetProduct() {
        topupAbleAdapter!!.payToken = selectedPayToken
        topupAbleAdapter!!.notifyDataSetChanged()
    }

    @Override

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            val inputmanger = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(etContact.getWindowToken(), 0)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }

    fun getProductList() {
        noProduct.visibility = View.GONE
        tvFound.visibility = View.GONE
        payTokenRecyclerView.visibility = View.GONE
        topupAbleAdapter!!.setNewData(arrayListOf())
//        if ("+86".equals(tvArea.text.toString().trim())) {
//            if ("".equals(etContact.text.toString().trim())) {
//                return
//            }
//            if (!AccountUtil.isTelephone(etContact.text.toString().trim())) {
//                toast(getString(R.string.please_enter_correct_mobbile_number))
//                return
//            }
//        } else {
//            if ("".equals(etContact.text.toString().trim())) {
//                toast(getString(R.string.please_enter_correct_mobbile_number))
//                return
//            }
//        }
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(tvArea.getWindowToken(), 0)
        var map = mutableMapOf<String, String>()
        map.put("phoneNumber", etContact.text.toString().trim())

        map.put("globalRoaming", country)
        map.put("isp", isp)
        map.put("province", region)

        map.put("page", "1")
        map.put("size", "20")
        mPresenter.getProductList(map)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    var country = ""
    var countryName = ""
    var isp = ""
    var region = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            tvArea.text = data!!.getStringExtra("area")
            country = data!!.getStringExtra("area")
            countryName = data!!.getStringExtra("country")
            tvCountry.text = countryName
            isp = ""
            region = ""
            tvIsp.text = getString(R.string.please_choose)
            tvRegion.text = getString(R.string.optional)
            topupAbleAdapter!!.data.clear()
        }
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            tvIsp.text = data!!.getStringExtra("isp")
            isp = data!!.getStringExtra("isp")
        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            tvRegion.text = data!!.getStringExtra("region")
            region = data!!.getStringExtra("region")
        }
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
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
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
                        getProductList()
                        return@forEach
                    }
                }
                if ("".equals(phone)) {
                    etContact.setText(phoneNumber.replace(" ", ""))
                    etContact.setSelection(etContact.text.toString().length)
                    getProductList()
                }
            }
        }
        if (requestCode == AllWallet.WalletType.QlcWallet.ordinal && resultCode == Activity.RESULT_OK) {
            finish()
        }
        if (requestCode == AllWallet.WalletType.EthWallet.ordinal && resultCode == Activity.RESULT_OK) {
            finish()
        }
        if (requestCode == AllWallet.WalletType.NeoWallet.ordinal && resultCode == Activity.RESULT_OK) {
            finish()
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
                        AlertDialog.newBuilder(this@QurryMobileActivity)
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


    override fun initData() {
        title.text = getString(R.string.enter_mobile_phone_number)
        country = intent.getStringExtra("area")
        countryName =intent.getStringExtra("country")
        isp = intent.getStringExtra("isp")
        if (!"".equals(country)) {
            tvArea.text = country
        }
        if (!"".equals(countryName)) {
            tvCountry.text = countryName
        }
        if (!"".equals(isp)) {
            tvIsp.text = isp
        }
        mPresenter.getPayToken()
        etContact.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                getProductList()
            }
        }
    }

    override fun setupActivityComponent() {
        DaggerQurryMobileComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .qurryMobileModule(QurryMobileModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: QurryMobileContract.QurryMobileContractPresenter) {
        mPresenter = presenter as QurryMobilePresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
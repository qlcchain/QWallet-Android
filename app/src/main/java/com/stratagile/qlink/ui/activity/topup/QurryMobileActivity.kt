package com.stratagile.qlink.ui.activity.topup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.qlinkcom
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerQurryMobileComponent
import com.stratagile.qlink.ui.activity.topup.contract.QurryMobileContract
import com.stratagile.qlink.ui.activity.topup.module.QurryMobileModule
import com.stratagile.qlink.ui.activity.topup.presenter.QurryMobilePresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupAbleAdapter
import com.stratagile.qlink.utils.*
import com.yanzhenjie.alertdialog.AlertDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RationaleListener
import kotlinx.android.synthetic.main.activity_qurry_mobile.*
import kotlinx.android.synthetic.main.activity_qurry_mobile.recyclerView
import kotlinx.android.synthetic.main.fragment_trade_list.*
import java.util.*

import javax.inject.Inject;





/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/24 14:50:33
 */

class QurryMobileActivity : BaseActivity(), QurryMobileContract.View {
    override fun setProductList(topupProduct: TopupProduct) {
        var productList = arrayListOf<TopupProduct.ProductListBean>()
        if (topupProduct.productList.size == 0) {
            noProduct.visibility = View.VISIBLE
        }
        topupProduct.productList.forEach { itss ->
            itss.amountOfMoney.split(",").forEach {
                var bean = TopupProduct.ProductListBean()
                bean.price = it.toInt()
                bean.country = itss.country
                bean.province = itss.province
                bean.isp = itss.isp
                bean.name = itss.name
                bean.discount = itss.discount
                bean.amountOfMoney = itss.amountOfMoney
                bean.id = itss.id
                bean.qgasDiscount = itss.qgasDiscount
                bean.ispEn = itss.ispEn
                bean.provinceEn = itss.provinceEn
                bean.countryEn = itss.countryEn
                bean.descriptionEn = itss.descriptionEn
                bean.description = itss.description
                bean.explainEn = itss.explainEn
                bean.explain = itss.explain
                productList.add(bean)
            }
        }
        topupAbleAdapter!!.setNewData(productList)
    }

    @Inject
    internal lateinit var mPresenter: QurryMobilePresenter
    var topupAbleAdapter : TopupAbleAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_qurry_mobile)
        tvArea.setOnClickListener {
            startActivityForResult(Intent(this, SelectAreaActivity::class.java), 1)
        }
        topupAbleAdapter = TopupAbleAdapter(arrayListOf())
        recyclerView.adapter = topupAbleAdapter
        topupAbleAdapter!!.setOnItemClickListener { adapter, view, position ->
            var price = topupAbleAdapter!!.data[position].price.toString()
            var dsicountPrice = topupAbleAdapter!!.data[position].price.toBigDecimal().multiply(topupAbleAdapter!!.data[position].discount.toBigDecimal()).stripTrailingZeros().toPlainString()
            var qgasCount = topupAbleAdapter!!.data[position].price.toBigDecimal().multiply(topupAbleAdapter!!.data[position].qgasDiscount.toBigDecimal()).stripTrailingZeros().toPlainString()
            alert(getString(R.string.a_cahrge_of_will_cost_qgas_and_rmb, price, qgasCount, dsicountPrice)) {
                negativeButton (getString(R.string.cancel)) { dismiss() }
                positiveButton(getString(R.string.buy_topup)) {
                    val intent1 = Intent(this@QurryMobileActivity, TopupQlcPayActivity::class.java)
                    intent1.putExtra("product", topupAbleAdapter!!.data[position])
                    intent1.putExtra("areaCode", tvArea.text.toString())
                    intent1.putExtra("phoneNumber", etContact.text.toString())
                    startActivityForResult(intent1, 3)
                }
            }.show()
        }
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, this)))
        ivContact.setOnClickListener {
            getContactPermission()
        }
        qurryMobile.setOnClickListener {
            getProductList()
        }
    }

    fun getProductList() {
        noProduct.visibility = View.GONE
        topupAbleAdapter!!.setNewData(arrayListOf())
        if ("+86".equals(tvArea.text.toString().trim())) {
            if (!AccountUtil.isTelephone(etContact.text.toString().trim())) {
                toast(getString(R.string.please_enter_correct_mobbile_number))
                return
            }
        } else {
            if ("".equals(etContact.text.toString().trim())) {
                toast(getString(R.string.please_enter_correct_mobbile_number))
                return
            }
        }
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(tvArea.getWindowToken(),0)
        var map = mutableMapOf<String, String>()
        map.put("phoneNumber", etContact.text.toString().trim())
        map.put("page", "1")
        map.put("size", "20")
        mPresenter.getProductList(map)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            tvArea.text = "+" + data!!.getStringExtra("area")
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
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
                etContact.setText(phoneNumber.replace("+86", "").replace(" ", ""))
                etContact.setSelection(etContact.text.toString().length)
                getProductList()
            }
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    fun getContactPermission() {
        AndPermission.with(this)
                .requestCode(101)
                .permission(
                        Manifest.permission.READ_CONTACTS
                )
                .rationale(object : RationaleListener{
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
                startActivityForResult(intent, 2)
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
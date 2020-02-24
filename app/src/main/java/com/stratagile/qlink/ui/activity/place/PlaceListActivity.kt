package com.stratagile.qlink.ui.activity.place

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.QLcSms
import com.stratagile.qlink.entity.ReportList
import com.stratagile.qlink.ui.activity.place.component.DaggerPlaceListComponent
import com.stratagile.qlink.ui.activity.place.contract.PlaceListContract
import com.stratagile.qlink.ui.activity.place.module.PlaceListModule
import com.stratagile.qlink.ui.activity.place.presenter.PlaceListPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration
import com.stratagile.qlink.utils.MD5Util
import kotlinx.android.synthetic.main.activity_place_list.*
import kotlinx.android.synthetic.main.activity_place_list.etPhone
import kotlinx.android.synthetic.main.activity_place_visit.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: $description
 * @date 2020/02/21 21:39:44
 */

class PlaceListActivity : BaseActivity(), PlaceListContract.View {

    @Inject
    internal lateinit var mPresenter: PlaceListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_place_list)
    }
    override fun initData() {
        title.text = getString(R.string.query_history)
        placeListAdapter = PlaceListAdapter(arrayListOf())
        recyclerView.adapter = placeListAdapter
        recyclerView.addItemDecoration(SpaceItemDecoration(20))
        placeListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this, SmsVourchActivity::class.java).putExtra("report", placeListAdapter.data[position]))
        }

        tvChaxun.setOnClickListener {
            if (etPhone.text.toString().length != 11) {
                toast(getString(R.string.please_input_phone_number))
                return@setOnClickListener
            }
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
//                imm.hideSoftInputFromWindow(view.getWindowToken(),0)
            }
            getPlaceList()
        }
    }

    override fun onBackPressed() {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(tvChaxun.getWindowToken(), 0)
        super.onBackPressed()
    }

    fun getPlaceList() {
        var infoMap = hashMapOf<String, String>()
        infoMap["phoneHash"] = MD5Util.getStringMD5("+86" + etPhone.text.toString().replace("+86", ""))
        mPresenter.getPlaceList(infoMap)
    }

    lateinit var placeListAdapter: PlaceListAdapter
    override fun setReportList(reportList: ReportList) {
        placeListAdapter.phoneNumber = etPhone.text.toString().replace("+86", "")
        placeListAdapter.setNewData(reportList.smsList)
    }

    override fun setupActivityComponent() {
       DaggerPlaceListComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .placeListModule(PlaceListModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: PlaceListContract.PlaceListContractPresenter) {
            mPresenter = presenter as PlaceListPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
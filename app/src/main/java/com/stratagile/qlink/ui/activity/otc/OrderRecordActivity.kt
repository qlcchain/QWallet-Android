package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderRecordComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderRecordContract
import com.stratagile.qlink.ui.activity.otc.module.OrderRecordModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderRecordPresenter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import rx.schedulers.Schedulers
import java.util.*

import javax.inject.Inject;
import kotlin.collections.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/10 14:40:52
 */

class OrderRecordActivity : BaseActivity(), OrderRecordContract.View {

    @Inject
    internal lateinit var mPresenter: OrderRecordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_order_record)
    }
    override fun initData() {

    }

    override fun setupActivityComponent() {
       DaggerOrderRecordComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .orderRecordModule(OrderRecordModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OrderRecordContract.OrderRecordContractPresenter) {
            mPresenter = presenter as OrderRecordPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}

data class Student(var name: String, var math : Int, var english : Int)

fun main(args : Array<String>) {
    var txid = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
    try {
        var addStr = ""
        var addCount = 64 - txid.length
        if (addCount > 0) {
            for (i in 0..addCount) {
                addStr+= "0"
            }
        }
        txid = addStr + txid
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(txid)
}
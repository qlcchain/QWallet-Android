package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerConfidantComponent
import com.stratagile.qlink.ui.activity.stake.contract.ConfidantContract
import com.stratagile.qlink.ui.activity.stake.module.ConfidantModule
import com.stratagile.qlink.ui.activity.stake.presenter.ConfidantPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.alibaba.fastjson.JSONObject
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.stake.CheckSecurityResult
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_confidant.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:38:01
 */

class ConfidantFragment : BaseFragment(), ConfidantContract.View {

    @Inject
    lateinit internal var mPresenter: ConfidantPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_confidant, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVerification.setOnClickListener {
            startVCodeCountDown()
            sendSecurityCode()
        }
        invoke.setOnClickListener {

        }
    }

    fun sendSecurityCode() {
        thread {
            val dataJson = jsonObject(
                    "action" to "sendSecurityCode",
                    "email_address" to "dzerix@gmail.com"
            )
            KLog.i(dataJson.toString())
            var request = "https://myconfidant.io/api/explorer".httpPost().body(dataJson.toString())
            request.headers["Content-Type"] = "application/json"
            request.responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    try {
                        KLog.i(data)
                        val jsonObject = JSONObject.parseObject(data)
                        val resultCode = jsonObject.getIntValue("status")
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }
        }
    }

    fun checkSecurityCode() {
        thread {
            val dataJson = jsonObject(
                    "action" to "checkSecurityCode",
                    "address" to "dzerix@gmail.com",
                    "security_code" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8"
            )
            KLog.i(dataJson.toString())
            var request = "https://myconfidant.io/api/explorer".httpPost().body(dataJson.toString())
            request.headers["Content-Type"] = "application/json"
            request.responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    try {
                        KLog.i(data)
                        var checkSecurityResult = Gson().fromJson<CheckSecurityResult>(data, CheckSecurityResult::class.java)

                        val jsonObject = JSONObject.parseObject(data)
                        val resultCode = jsonObject.getIntValue("valid")
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }
        }
    }

    fun confirmMacAddresses() {
        thread {
            val macAddressJsonArrayData = jsonArray()

            val dataJson = jsonObject(
                    "action" to "confirmMacAddresses",
                    "address" to "dzerix@gmail.com",
                    "security_code" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8",
                    "qlc_address" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8",
                    "neo_address" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8",
                    "txid" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8",
                    "mac_addresses" to "9r4mGjEzbMMbcuPRNAFuDu9e0rIjGjY8"
            )
            KLog.i(dataJson.toString())
            var request = "https://myconfidant.io/api/explorer".httpPost().body(dataJson.toString())
            request.headers["Content-Type"] = "application/json"
            request.responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    try {
                        KLog.i(data)
                        var checkSecurityResult = Gson().fromJson<CheckSecurityResult>(data, CheckSecurityResult::class.java)

                        val jsonObject = JSONObject.parseObject(data)
                        val resultCode = jsonObject.getIntValue("valid")
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }
        }
    }


    override fun setupFragmentComponent() {
        DaggerConfidantComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .confidantModule(ConfidantModule(this))
                .build()
                .inject(this)
    }

    private var mdDisposable: Disposable? = null

    private fun startVCodeCountDown() {
        tvVerification.setEnabled(false)
        tvVerification.setBackground(resources.getDrawable(R.drawable.vcode_count_down_bg))
        mdDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { aLong -> tvVerification.setText("" + (60 - aLong!!) + "") }
                .doOnComplete {
                    //倒计时完毕置为可点击状态
                    tvVerification.setEnabled(true)
                    tvVerification.setText(getString(R.string.get_the_code))
                    tvVerification.setBackgroundColor(resources.getColor(R.color.white))
                }
                .subscribe()
    }

    override fun onDestroy() {
        mdDisposable?.dispose()
        super.onDestroy()
    }

    override fun setPresenter(presenter: ConfidantContract.ConfidantContractPresenter) {
        mPresenter = presenter as ConfidantPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
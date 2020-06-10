package com.stratagile.qlink.ui.activity.defi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiKeyStateComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiKeyStateContract
import com.stratagile.qlink.ui.activity.defi.module.DefiKeyStateModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiKeyStatePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.google.gson.Gson
import com.pawegio.kandroid.runOnUiThread
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiJson
import com.stratagile.qlink.entity.defi.KeyStateBean
import com.stratagile.qlink.ui.adapter.defi.DefiKeyStateAdapter
import kotlinx.android.synthetic.main.fragment_defi_key_state.*

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/06/02 10:25:39
 */

class DefiKeyStateFragment : BaseFragment(), DefiKeyStateContract.View {

    @Inject
    lateinit internal var mPresenter: DefiKeyStatePresenter
    lateinit var viewModel: DefiViewModel
    lateinit var mDefiDetail : DefiDetail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi_key_state, null)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(DefiViewModel::class.java)
        viewModel.defiDetailLiveData.observe(this, Observer {
            mDefiDetail = it!!
           runOnUiThread {
               KLog.i("数据改变")
               var jsonValue = Gson().fromJson<DefiJson>(it!!.project.jsonValue, DefiJson::class.java)
               var list = arrayListOf<KeyStateBean>()
               list.add(KeyStateBean(jsonValue.tvl.btc.relative_1d, jsonValue.tvl.btc.value, "BTC"))
               list.add(KeyStateBean(jsonValue.tvl.eth.relative_1d, jsonValue.tvl.eth.value, "ETH"))
               list.add(KeyStateBean(jsonValue.tvl.usd.relative_1d, jsonValue.tvl.usd.value.toDouble(), "USDT"))
               var keyStateAdapter = DefiKeyStateAdapter(list)
               KLog.i("数据个数为：" + list.size)
               recyclerView.adapter = keyStateAdapter
           }
        })
    }

    override fun initDataFromNet() {

    }


    override fun setupFragmentComponent() {
        DaggerDefiKeyStateComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiKeyStateModule(DefiKeyStateModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiKeyStateContract.DefiKeyStateContractPresenter) {
        mPresenter = presenter as DefiKeyStatePresenter
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
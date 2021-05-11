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
import java.math.BigDecimal

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
    lateinit var mDefiDetail: DefiDetail

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
                KLog.e("数据改变  ： ${mDefiDetail.project.jsonValue}")
                var jsonValue = Gson().fromJson<DefiJson>(it!!.project.jsonValue, DefiJson::class.java)
                KLog.e("数据改变完了")
                var list = arrayListOf<KeyStateBean>()
                list.add(KeyStateBean(jsonValue.tvl.btc.relative_1d, jsonValue.tvl.btc.value, "BTC"))
                list.add(KeyStateBean(jsonValue.tvl.eth.relative_1d, jsonValue.tvl.eth.value, "ETH"))
                list.add(KeyStateBean(jsonValue.tvl.usd.relative_1d, jsonValue.tvl.usd.value.toDouble(), "USDT"))
                var keyStateAdapter = DefiKeyStateAdapter(list)
                KLog.i("数据个数为：" + list.size)
                recyclerView.adapter = keyStateAdapter

                /**
                 * symbol : COMP
                 * marketCap : 477284653.8452589
                 * chain : Ethereum
                 * percentChange24h : -4.05578
                 * percentChange7d : -14.0927
                 * totalSupply : 10000000
                 * price : 186.346217591
                 * id : 188e0766426b4af1bd581dba24a2406d
                 * hash : 0xc00e94cb662c3520282e6f5717214004a7f26888
                 * circulatingSupply : 2561279
                 */
                if (mDefiDetail.project.token != null) {
                    tvTokenInfo.text = mDefiDetail.project.token.symbol
                    tvTokenPercent.text = mDefiDetail.project.token.percentChange24h + "% (24h)"
                    tvTokenPrice.text =  "$" + mDefiDetail.project.token.price.toBigDecimal().setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                    tvMarketCap.text = "$ " + mDefiDetail.project.token.marketCap.toBigDecimal().setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                    tvCirculatingSupply.text = mDefiDetail.project.token.circulatingSupply.toString() + " " + mDefiDetail.project.token.symbol

                    if (mDefiDetail.project.token.percentChange24h.toFloat() > 0) {
                        tvTokenPercent.setTextColor(resources.getColor(R.color.color_7ED321))
                        ivArrow.setImageResource(R.mipmap.icon_arrow_up)
                    } else {
                        tvTokenPercent.setTextColor(resources.getColor(R.color.color_f50f60))
                        ivArrow.setImageResource(R.mipmap.icon_arrow_down)
                    }
                } else {
                    tvTokenInfo.text = ""
                    tvMarketCap.text = ""
                    tvCirculatingSupply.text = ""
                    llTokenInfo.visibility = View.GONE
                    tvToken.text = ""
                }
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
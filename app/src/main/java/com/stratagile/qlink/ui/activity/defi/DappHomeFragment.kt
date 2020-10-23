package com.stratagile.qlink.ui.activity.defi

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.defi.component.DaggerDappHomeComponent
import com.stratagile.qlink.ui.activity.defi.contract.DappHomeContract
import com.stratagile.qlink.ui.activity.defi.module.DappHomeModule
import com.stratagile.qlink.ui.activity.defi.presenter.DappHomePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R
import kotlinx.android.synthetic.main.fragment_dapp_home.*

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/10/15 16:00:43
 */

class DappHomeFragment : BaseFragment(), DappHomeContract.View {

    @Inject
    lateinit internal var mPresenter: DappHomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dapp_home, null)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterSake.setOnClickListener {
            var dappIntent = Intent(activity, DappBrowserActivity::class.java)
            dappIntent.putExtra("url", "https://sakeswap.finance/#/swap")
            startActivity(dappIntent)
        }
        enterSake1.setOnClickListener {
            var dappIntent = Intent(activity, DappBrowserActivity::class.java)
            dappIntent.putExtra("url", "https://app.sakeswap.finance/#/")
            startActivity(dappIntent)
        }
        walletconnect.setOnClickListener {
            var dappIntent = Intent(activity, DappBrowserActivity::class.java)
            dappIntent.putExtra("url", "https://example.walletconnect.org/")
            startActivity(dappIntent)
        }
    }

    override fun initDataFromNet() {

    }


    override fun setupFragmentComponent() {
        DaggerDappHomeComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .dappHomeModule(DappHomeModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DappHomeContract.DappHomeContractPresenter) {
        mPresenter = presenter as DappHomePresenter
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
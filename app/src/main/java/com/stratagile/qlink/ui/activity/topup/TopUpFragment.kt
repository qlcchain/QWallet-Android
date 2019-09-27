package com.stratagile.qlink.ui.activity.topup

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopUpComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.module.TopUpModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopUpPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.pawegio.kandroid.alert
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupShowProductAdapter
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.fragment_topup.*
import java.util.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */

class TopUpFragment : BaseFragment(), TopUpContract.View {

    override fun setProductList(topupProduct: TopupProduct) {
        topupShowProductAdapter.setNewData(topupProduct.productList)
    }

    @Inject
    lateinit internal var mPresenter: TopUpPresenter
    internal var viewList: MutableList<View> = ArrayList()
    lateinit var topupShowProductAdapter: TopupShowProductAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topup, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList.add(layoutInflater.inflate(R.layout.layout_finance_share, null, false))
        topupShowProductAdapter = TopupShowProductAdapter(arrayListOf())
        recyclerView.adapter = topupShowProductAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, activity!!)))
        topupShowProductAdapter.setOnItemClickListener { adapter, view, position ->
            if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size == 0) {
                activity!!.alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately)) {
                    negativeButton (getString(R.string.cancel)) { dismiss() }
                    positiveButton(getString(R.string.create)) { startActivity(Intent(context, SelectWalletTypeActivity::class.java)) }
                }.show()
            } else {
                startActivity(Intent(activity!!, QurryMobileActivity::class.java))
            }
        }
        val viewAdapter = ViewAdapter()
        viewPager.setAdapter(viewAdapter)
        etMobile.setOnClickListener {
            if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size == 0) {
                activity!!.alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately)) {
                    negativeButton (getString(R.string.cancel)) { dismiss() }
                    positiveButton(getString(R.string.create)) { startActivity(Intent(context, SelectWalletTypeActivity::class.java)) }
                }.show()
                return@setOnClickListener
            }
            startActivity(Intent(activity!!, QurryMobileActivity::class.java))
        }
        var map = mutableMapOf<String, String>()
        map.put("page", "1")
        map.put("size", "20")
        mPresenter.getProductList(map)
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            var map = mutableMapOf<String, String>()
            map.put("page", "1")
            map.put("size", "20")
            mPresenter.getProductList(map)
        }
    }

    internal inner class ViewAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return 1
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(viewList[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewList[position])
            viewList[position].setOnClickListener(View.OnClickListener {
                startActivity(Intent(activity, MyStakeActivity::class.java))
            })
            return viewList[position]
        }
    }


    override fun setupFragmentComponent() {
        DaggerTopUpComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .topUpModule(TopUpModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopUpContract.TopUpContractPresenter) {
        mPresenter = presenter as TopUpPresenter
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
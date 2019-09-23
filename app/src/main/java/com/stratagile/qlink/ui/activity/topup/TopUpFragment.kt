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
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.ui.activity.finance.EarnRankActivity
import com.stratagile.qlink.ui.activity.finance.InviteActivity
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import kotlinx.android.synthetic.main.fragment_topup.*
import java.util.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */

class TopUpFragment : BaseFragment(), TopUpContract.View {

    @Inject
    lateinit internal var mPresenter: TopUpPresenter
    internal var viewList: MutableList<View> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topup, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList.add(layoutInflater.inflate(R.layout.layout_finance_share, null, false))
        val viewAdapter = ViewAdapter()
        viewPager.setAdapter(viewAdapter)
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
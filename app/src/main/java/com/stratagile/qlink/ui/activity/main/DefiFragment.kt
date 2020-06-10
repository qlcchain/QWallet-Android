package com.stratagile.qlink.ui.activity.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.entity.eventbus.DefiLoadData
import com.stratagile.qlink.ui.activity.defi.DefiListFragment
import com.stratagile.qlink.ui.activity.defi.DefiNewsFragment
import com.stratagile.qlink.ui.activity.main.component.DaggerDefiComponent
import com.stratagile.qlink.ui.activity.main.contract.DefiContract
import com.stratagile.qlink.ui.activity.main.module.DefiModule
import com.stratagile.qlink.ui.activity.main.presenter.DefiPresenter
import com.stratagile.qlink.utils.FireBaseUtils
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.fragment_defi.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2020/05/25 11:29:00
 */

class DefiFragment : BaseFragment(), DefiContract.View {
    override fun initDataFromNet() {
        KLog.i("加载数据》。。。")
        EventBus.getDefault().post(DefiLoadData())
    }
    @Inject
    lateinit internal var mPresenter: DefiPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi, null)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val llp = LinearLayout.LayoutParams(UIUtils.getDisplayWidth(activity), UIUtils.getStatusBarHeight(activity))
        status_bar.setLayoutParams(llp)

        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) {
                    DefiListFragment()
                } else if (position == 1) {
                    DefiNewsFragment()
                } else {
                    Fragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }
        button21.toggle()

        segmentControlView.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.button21) {
                viewPager.setCurrentItem(0)
                FireBaseUtils.logEvent(activity, FireBaseUtils.Defi_Home_Top_Defi)
            } else {
                viewPager.setCurrentItem(1)
                FireBaseUtils.logEvent(activity, FireBaseUtils.Defi_Home_Top_Hot)
            }
        }
    }


    override fun setupFragmentComponent() {
        DaggerDefiComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiModule(DefiModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiContract.DefiContractPresenter) {
        mPresenter = presenter as DefiPresenter
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
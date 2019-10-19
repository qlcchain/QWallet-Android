package com.stratagile.qlink.ui.activity.topup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.InviteList
import com.stratagile.qlink.entity.eventbus.Logout
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity
import com.stratagile.qlink.ui.activity.main.MainViewModel
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter
import com.stratagile.qlink.ui.adapter.topup.TopupShowProductAdapter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.UIUtils
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_person.*
import kotlinx.android.synthetic.main.fragment_topup.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */

class TopUpFragment : BaseFragment(), TopUpContract.View {
    override fun setOneFriendReward(dict: Dict) {
        getInviteRank()
        oneFirendClaimQgas = dict.data.value.toFloat()
    }

    override fun setInviteRank(inviteList: InviteList) {

        var map = mutableMapOf<String, String>()
        map.put("page", "1")
        map.put("size", "20")
        mPresenter.getProductList(map)

        val invitedAdapter = InvitedAdapter(inviteList.top5, oneFirendClaimQgas)
        recyclerViewInvite.adapter = invitedAdapter
    }

    override fun setProductList(topupProduct: TopupProduct) {
        topupShowProductAdapter.setNewData(topupProduct.productList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun logOut(logout: Logout) {
        llReferralCode.visibility = View.GONE
    }

    @Inject
    lateinit internal var mPresenter: TopUpPresenter
    private var oneFirendClaimQgas = 0f
    private var viewModel: MainViewModel? = null
    internal var viewList: MutableList<View> = ArrayList()
    lateinit var topupShowProductAdapter: TopupShowProductAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topup, null)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        EventBus.getDefault().register(this)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList.add(layoutInflater.inflate(R.layout.layout_finance_share, null, false))
        viewModel?.currentUserAccount?.observe(this, Observer {
            if (it != null) {
                tvIniviteCode.text = ConstantValue.currentUser.inviteCode
                llReferralCode.visibility = View.VISIBLE
                getOneFriendReward()
                tvInivteNow.setOnClickListener {
                    startActivity(Intent(activity, InviteNowActivity::class.java))
                }
                llCopy.setOnClickListener {
                    //获取剪贴板管理器：
                    val cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", tvIniviteCode.text.toString().trim { it <= ' ' })
                    // 将ClipData内容放到系统剪贴板里。
                    cm!!.primaryClip = mClipData
                    ToastUtil.displayShortToast(getString(R.string.copy_success))
                }
            } else {
                llReferralCode.visibility = View.GONE
            }
        })
        topupShowProductAdapter = TopupShowProductAdapter(arrayListOf())
        recyclerView.adapter = topupShowProductAdapter
        rlInvite.setOnClickListener {
            startActivity(Intent(activity, MyStakeActivity::class.java))
        }
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, activity!!)))
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false

        recyclerViewInvite.setHasFixedSize(true)
        recyclerViewInvite.isNestedScrollingEnabled = false
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
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            getOneFriendReward()
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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

    fun getInviteRank() {
        val infoMap = HashMap<String, String>()
        infoMap["account"] = ConstantValue.currentUser.account
        infoMap["token"] = AccountUtil.getUserToken()
        mPresenter.getInivteRank(infoMap)
    }

    /**
     * 获取邀请到一个好友的奖励数
     */
    private fun getOneFriendReward() {
        val infoMap = HashMap<String, String>()
        infoMap["dictType"] = "winq_invite_reward_amount"
        mPresenter.getOneFriendReward(infoMap)
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
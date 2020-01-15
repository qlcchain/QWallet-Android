package com.stratagile.qlink.ui.activity.recommend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.ui.activity.recommend.component.DaggerAgencyExcellenceComponent
import com.stratagile.qlink.ui.activity.recommend.contract.AgencyExcellenceContract
import com.stratagile.qlink.ui.activity.recommend.module.AgencyExcellenceModule
import com.stratagile.qlink.ui.activity.recommend.presenter.AgencyExcellencePresenter
import com.stratagile.qlink.utils.SpUtil
import kotlinx.android.synthetic.main.activity_agency_excellence.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/09 13:58:26
 */

class AgencyExcellenceActivity : BaseActivity(), AgencyExcellenceContract.View {

    @Inject
    internal lateinit var mPresenter: AgencyExcellencePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_agency_excellence)
    }
    override fun initData() {
        title.text = getString(R.string.exclusive_offers)
        tvNickName.text = ConstantValue.currentUser.account
        tvToOpen.setOnClickListener {
            startActivityForResult(Intent(this, OpenAgentActivity::class.java), 1)
        }
        if (ConstantValue.currentUser.qlcAddress != null && !"".equals(ConstantValue.currentUser.qlcAddress)) {
            tvKaitong.text = getString(R.string.already_opened)
        } else {
            tvKaitong.text = getString(R.string.not_opened)
        }
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 1) { //中文
            agentDetails.setBackground(resources.getDrawable(R.mipmap.agent_details_ch))
            agentDetailsB.setBackground(resources.getDrawable(R.mipmap.agent_details_b_ch))
        } else {
            agentDetails.setBackground(resources.getDrawable(R.mipmap.agent_details_en))
            agentDetailsB.setBackground(resources.getDrawable(R.mipmap.agent_details_b_en))
        }
        if (ConstantValue.currentUser.avatar != null && "" != ConstantValue.currentUser.avatar) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.avatar)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar)
        } else {
            Glide.with(this)
                    .load(R.mipmap.icon_user_default)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            initData()
        }
    }

    override fun setupActivityComponent() {
       DaggerAgencyExcellenceComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .agencyExcellenceModule(AgencyExcellenceModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: AgencyExcellenceContract.AgencyExcellenceContractPresenter) {
            mPresenter = presenter as AgencyExcellencePresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.otc.TradeOrderDetail
import com.stratagile.qlink.ui.activity.otc.component.DaggerAppealSubmittedComponent
import com.stratagile.qlink.ui.activity.otc.contract.AppealSubmittedContract
import com.stratagile.qlink.ui.activity.otc.module.AppealSubmittedModule
import com.stratagile.qlink.ui.activity.otc.presenter.AppealSubmittedPresenter
import com.stratagile.qlink.ui.adapter.AppealUploadImgItemDecoration
import com.stratagile.qlink.ui.adapter.otc.AppealImgAdapter
import com.stratagile.qlink.ui.adapter.otc.ImageEntity
import kotlinx.android.synthetic.main.activity_appeal.*
import kotlinx.android.synthetic.main.activity_appeal_submitted.*
import kotlinx.android.synthetic.main.activity_appeal_submitted.recyclerView

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/22 15:34:25
 */

class AppealSubmittedActivity : BaseActivity(), AppealSubmittedContract.View {

    @Inject
    internal lateinit var mPresenter: AppealSubmittedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_appeal_submitted)
        title.text = "Submitted"
    }
    override fun initData() {
        var tradeOrderDetail = intent.getParcelableExtra<TradeOrderDetail.OrderBean>("orderDetail")
        tvContent.text = tradeOrderDetail.reason
        var list = arrayListOf<ImageEntity>()
        if (!"".equals(tradeOrderDetail.photo1)) {
            list.add(ImageEntity(1, tradeOrderDetail.photo1, true))

        }
        if (!"".equals(tradeOrderDetail.photo2)) {
            list.add(ImageEntity(2, tradeOrderDetail.photo2, true))
        }
        if (!"".equals(tradeOrderDetail.photo3)) {
            list.add(ImageEntity(3, tradeOrderDetail.photo3, true))
        }
        if (!"".equals(tradeOrderDetail.photo4)) {
            list.add(ImageEntity(4, tradeOrderDetail.photo4, true))
        }
        var appealImgAdapter = AppealImgAdapter(list)
        appealImgAdapter.isSee = true
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(AppealUploadImgItemDecoration(resources.getDimension(R.dimen.x6).toInt()))
        recyclerView.adapter = appealImgAdapter
    }

    override fun setupActivityComponent() {
       DaggerAppealSubmittedComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .appealSubmittedModule(AppealSubmittedModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: AppealSubmittedContract.AppealSubmittedContractPresenter) {
            mPresenter = presenter as AppealSubmittedPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
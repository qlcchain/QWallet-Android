package com.stratagile.qlink.ui.activity.defi

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.defi.DefiNewsDetail
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiNewsDetailComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsDetailContract
import com.stratagile.qlink.ui.activity.defi.module.DefiNewsDetailModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiNewsDetailPresenter
import kotlinx.android.synthetic.main.activity_defi_news_detail.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/06/05 14:09:12
 */

class DefiNewsDetailActivity : BaseActivity(), DefiNewsDetailContract.View {

    @Inject
    internal lateinit var mPresenter: DefiNewsDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_defi_news_detail)
        title.text = getString(R.string.news)
    }
    override fun initData() {
        getNewsDetail()
    }

    fun getNewsDetail() {
        showProgressDialog()
        var infoMap = hashMapOf<String, String>()
        infoMap["newsId"] = intent.getStringExtra("newsId")
        mPresenter.getDefiNewsDetail(infoMap)
    }

    override fun setNewsDetail(defiNewsDetail: DefiNewsDetail) {
        closeProgressDialog()
        tvAuthor.text = defiNewsDetail.news.authod
        tvTitle.text = defiNewsDetail.news.title
        tvTime.text = defiNewsDetail.news.createDate.substring(0, 10)
        tvViews.text = defiNewsDetail.news.views.toString() + " " + getString(R.string.views)
        webview.setBackgroundColor(resources.getColor(R.color.transparent))
        webview.loadDataWithBaseURL(null, defiNewsDetail.news.content, "text/html", "utf-8", null)
    }

    override fun setupActivityComponent() {
       DaggerDefiNewsDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .defiNewsDetailModule(DefiNewsDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: DefiNewsDetailContract.DefiNewsDetailContractPresenter) {
            mPresenter = presenter as DefiNewsDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.my

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.my.component.DaggerBurnIntroduceComponent
import com.stratagile.qlink.ui.activity.my.contract.BurnIntroduceContract
import com.stratagile.qlink.ui.activity.my.module.BurnIntroduceModule
import com.stratagile.qlink.ui.activity.my.presenter.BurnIntroducePresenter
import kotlinx.android.synthetic.main.activity_burn_introduce.*
import java.util.HashMap

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/02/29 17:33:46
 */

class BurnIntroduceActivity : BaseActivity(), BurnIntroduceContract.View {

    @Inject
    internal lateinit var mPresenter: BurnIntroducePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_burn_introduce)
    }
    override fun initData() {
        title.text = getString(R.string.details_burn)
        var qgasPrice = ConstantValue.qgasToQlcPrice.toBigDecimal().stripTrailingZeros().toPlainString()
        tvQgasPrice.text = "1QGas= ${qgasPrice} QLC"
        tvIntroduce.text = getString(R.string.program_implementation, qgasPrice)
    }


    override fun setupActivityComponent() {
       DaggerBurnIntroduceComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .burnIntroduceModule(BurnIntroduceModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: BurnIntroduceContract.BurnIntroduceContractPresenter) {
            mPresenter = presenter as BurnIntroducePresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
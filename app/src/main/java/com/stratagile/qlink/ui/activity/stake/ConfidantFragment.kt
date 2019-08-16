package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerConfidantComponent
import com.stratagile.qlink.ui.activity.stake.contract.ConfidantContract
import com.stratagile.qlink.ui.activity.stake.module.ConfidantModule
import com.stratagile.qlink.ui.activity.stake.presenter.ConfidantPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_confidant.*
import java.util.concurrent.TimeUnit

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:38:01
 */

class ConfidantFragment : BaseFragment(), ConfidantContract.View {

    @Inject
    lateinit internal var mPresenter: ConfidantPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_confidant, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVerification.setOnClickListener {
            startVCodeCountDown()
        }
    }


    override fun setupFragmentComponent() {
        DaggerConfidantComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .confidantModule(ConfidantModule(this))
                .build()
                .inject(this)
    }

    private var mdDisposable: Disposable? = null

    private fun startVCodeCountDown() {
        tvVerification.setEnabled(false)
        tvVerification.setBackground(resources.getDrawable(R.drawable.vcode_count_down_bg))
        mdDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { aLong -> tvVerification.setText("" + (60 - aLong!!) + "") }
                .doOnComplete {
                    //倒计时完毕置为可点击状态
                    tvVerification.setEnabled(true)
                    tvVerification.setText(getString(R.string.get_the_code))
                    tvVerification.setBackgroundColor(resources.getColor(R.color.white))
                }
                .subscribe()
    }

    override fun onDestroy() {
        mdDisposable?.dispose()
        super.onDestroy()
    }

    override fun setPresenter(presenter: ConfidantContract.ConfidantContractPresenter) {
        mPresenter = presenter as ConfidantPresenter
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
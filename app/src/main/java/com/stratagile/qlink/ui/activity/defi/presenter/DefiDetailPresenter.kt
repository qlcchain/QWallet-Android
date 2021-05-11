package com.stratagile.qlink.ui.activity.defi.presenter

import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiDetailContract
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiDetailActivity
 * @date 2020/05/29 09:13:19
 */
class DefiDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiDetailContract.View) : DefiDetailContract.DefiDetailContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getDefiDetail(map: HashMap<String, String>) {
        KLog.e("fun  getDefiDetail")
        val disposable = httpAPIWrapper.defiProject(map)
                .subscribe({ baseBack ->
                    KLog.e("getDefiDetail  :  baseBack  " + baseBack.project.jsonValue)
                    try {
                        mView.closeProgressDialog()
                        mView.setDefiDetail(baseBack)
                        KLog.e("getDefiDetail  :  isSuccesse  ")
                    } catch (e: Exception) {
                        KLog.e(e.message)
                        KLog.e(e.stackTrace)
                        KLog.e(e.toString())
                    }
                    //isSuccesse
                }, {
                    mView.closeProgressDialog()
                    KLog.i(it.message)
                    mView.setDetailError(it.message)
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun getDefiStateList(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiStatsList(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiStateList(baseBack)
                    //isSuccesse
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
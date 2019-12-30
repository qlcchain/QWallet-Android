package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.QurryMobileContract
import com.stratagile.qlink.ui.activity.topup.QurryMobileActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of QurryMobileActivity
 * @date 2019/09/24 14:50:33
 */
class QurryMobilePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: QurryMobileContract.View) : QurryMobileContract.QurryMobileContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }

    fun getProductList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupProductList(map).subscribe({
            mView.setProductList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getCountryList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getCountryList(map).subscribe({
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getPayToken() {
        mCompositeDisposable.add(httpAPIWrapper.payToken(hashMapOf<String, String>()).subscribe({
            mView.setPayTokenAdapter(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun createTopupOrder(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupCreateOrder(map).subscribe({
            mView.createTopupOrderSuccess(it)
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }))
    }

    fun getIspList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getIspList(map).subscribe({
            mView.setIsp(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun provinceList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.provinceList(map).subscribe({
            mView.setProvinceList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

}
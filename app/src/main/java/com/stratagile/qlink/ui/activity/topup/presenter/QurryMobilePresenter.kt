package com.stratagile.qlink.ui.activity.topup.presenter
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.topup.contract.QurryMobileContract
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

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
        mCompositeDisposable.add(httpAPIWrapper.getTopupProductListV2(map).subscribe({
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

    fun qurryDict(map: Map<*, *>?, position : Int) {
        httpAPIWrapper.qurryDict(map).subscribe(object : Observer<Dict?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(dict: Dict) {
                mView.setGroupDate(dict, position)
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }
}
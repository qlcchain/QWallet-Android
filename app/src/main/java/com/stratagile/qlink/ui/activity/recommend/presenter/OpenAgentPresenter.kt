package com.stratagile.qlink.ui.activity.recommend.presenter
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.recommend.contract.OpenAgentContract
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: presenter of OpenAgentActivity
 * @date 2020/01/09 13:59:03
 */
class OpenAgentPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OpenAgentContract.View) : OpenAgentContract.OpenAgentContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun bindQlcChainAddress(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.bindQlcChainAddress(map).subscribe({
            mView.bindAddressSuccess()
        }, {
            mView.closeProgressDialog()

        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getUserInfo(map: Map<*, *>?) {
        mCompositeDisposable.add(httpAPIWrapper.getUserInfo(map).subscribe({ user ->
            //isSuccesse
            mView.setUsrInfo(user)
        }, { throwable ->
            //onError
            throwable.printStackTrace()
            //mView.closeProgressDialog();
        }) {
            //onComplete
        })
    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
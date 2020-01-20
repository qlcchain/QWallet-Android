package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.InviteList
import com.stratagile.qlink.entity.KLine
import com.stratagile.qlink.entity.TokenInfo
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.TopUpFragment
import io.reactivex.Observer
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import org.web3j.abi.datatypes.Bool
import java.util.ArrayList
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopUpFragment
 * @date 2019/09/23 15:54:17
 */

class TopUpPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopUpContract.View) : TopUpContract.TopUpContractPresenter {

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

    fun getProductList(map: MutableMap<String, String>, next : Boolean) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupProductList(map).subscribe({
            mView.setProductList(it, next)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getCountryList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getCountryList(map).subscribe({
            mView.setCountryList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }


    fun getToeknPrice(map: HashMap<*, *>) {
        val disposable = httpAPIWrapper.getTokenPrice(map)
                .subscribe({ baseBack ->
                    mView.setQlcPrice(baseBack)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun getInivteRank(map: Map<*, *>) {
        val disposable = httpAPIWrapper.getInivteTop5(map)
                .subscribe({ user ->
                    //isSuccesse
                    //mView.closeProgressDialog();
                    mView.setInviteRank(user)
                }, { throwable ->
                    //onError
                    throwable.printStackTrace()
                    //mView.closeProgressDialog();
                    //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun getOneFriendReward(map: Map<String, String>) {
        val disposable = httpAPIWrapper.qurryDict(map)
                .subscribe(Consumer<Dict> { user ->
                    //isSuccesse
                    //mView.closeProgressDialog();
                    mView.setOneFriendReward(user)
                }, Consumer<Throwable> { throwable ->
                    //onError
                    throwable.printStackTrace()
                    //mView.closeProgressDialog();
                    //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                }, Action {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun getTokenKline(map: Map<*, *>) {
        val disposable = httpAPIWrapper.getTokenKLine(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setChartData(baseBack)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun queryDict(map: Map<*, *>?) {
        httpAPIWrapper.qurryDict(map).subscribe(object : Observer<Dict?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(dict: Dict) {
                mView.setProxyActivityBanner(dict)
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }
}
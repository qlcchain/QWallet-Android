package com.stratagile.qlink.ui.activity.recommend.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.recommend.contract.TopupProductDetailContract
import com.stratagile.qlink.ui.activity.recommend.TopupProductDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import qlc.network.QlcException
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: presenter of TopupProductDetailActivity
 * @date 2020/01/13 15:36:22
 */
class TopupProductDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupProductDetailContract.View) : TopupProductDetailContract.TopupProductDetailContractPresenter {

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

    fun getTopupGroupList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupGroupList(map).subscribe({
            mView.setGroupList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun createGroup(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.createGroup(map).subscribe({
            mView.createGroupBack(it)
        }, {
            mView.closeProgressDialog()
            if (it is QlcException) {
                if ("Please bind qlc chain address!".equals(it.message)) {
                    mView.showProxyDialog()
                } else if ("The amount of QLC mortgage must be greater than 1500!".equals(it.message)) {
                    mView.showStakeDialog()
                }
            }
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getTopupGroupKindList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupGroupKindList(map).subscribe({
            mView.setGroupKindList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
    fun topupJoinGroup(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupJoinGroup(map).subscribe({
            mView.joinGroupBack(it)
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

}
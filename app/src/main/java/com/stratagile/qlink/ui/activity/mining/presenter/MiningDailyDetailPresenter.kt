package com.stratagile.qlink.ui.activity.mining.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.newwinq.MiningAct
import com.stratagile.qlink.ui.activity.mining.contract.MiningDailyDetailContract
import com.stratagile.qlink.ui.activity.mining.MiningDailyDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: presenter of MiningDailyDetailActivity
 * @date 2019/11/14 18:13:10
 */
class MiningDailyDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MiningDailyDetailContract.View) : MiningDailyDetailContract.MiningDailyDetailContractPresenter {

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

    fun getMiningRewardList(map : Map<String, String>, page : Int) {
        val disposable = httpAPIWrapper.getMiningRewardList(map)
                .subscribe({ miningAct ->
                    //isSuccesse
                    mView.setRewardList(miningAct, page)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
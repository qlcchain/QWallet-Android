package com.stratagile.qlink.ui.activity.recommend.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.recommend.contract.MyTopupGroupContract
import com.stratagile.qlink.ui.activity.recommend.MyTopupGroupActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: presenter of MyTopupGroupActivity
 * @date 2020/01/15 16:21:51
 */
class MyTopupGroupPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MyTopupGroupContract.View) : MyTopupGroupContract.MyTopupGroupContractPresenter {

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

    fun getOderList(map: MutableMap<String, String>, page : Int) {
        mCompositeDisposable.add(httpAPIWrapper.getGroupItemList(map).subscribe({
            mView.setOrderList(it, page)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
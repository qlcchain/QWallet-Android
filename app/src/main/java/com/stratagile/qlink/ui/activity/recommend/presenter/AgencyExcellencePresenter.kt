package com.stratagile.qlink.ui.activity.recommend.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.recommend.contract.AgencyExcellenceContract
import com.stratagile.qlink.ui.activity.recommend.AgencyExcellenceActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: presenter of AgencyExcellenceActivity
 * @date 2020/01/09 13:58:26
 */
class AgencyExcellencePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: AgencyExcellenceContract.View) : AgencyExcellenceContract.AgencyExcellenceContractPresenter {

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
}
package com.stratagile.qlink.ui.activity.vpn.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnSuccessContract;
import com.stratagile.qlink.ui.activity.vpn.ConnectVpnSuccessActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of ConnectVpnSuccessActivity
 * @date 2018/02/09 10:28:44
 */
public class ConnectVpnSuccessPresenter implements ConnectVpnSuccessContract.ConnectVpnSuccessContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private ConnectVpnSuccessContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public ConnectVpnSuccessPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ConnectVpnSuccessContract.View view) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
        mView = null;
    }
}
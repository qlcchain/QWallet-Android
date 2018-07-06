package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisterVpnSuccessActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisterVpnSuccessContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisterVpnSuccessPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of RegisterVpnSuccessActivity, provide field for RegisterVpnSuccessActivity
 * @date 2018/02/11 16:34:20
 */
@Module
public class RegisterVpnSuccessModule {
    private final RegisterVpnSuccessContract.View mView;


    public RegisterVpnSuccessModule(RegisterVpnSuccessContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisterVpnSuccessPresenter provideRegisterVpnSuccessPresenter(HttpAPIWrapper httpAPIWrapper, RegisterVpnSuccessActivity mActivity) {
        return new RegisterVpnSuccessPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisterVpnSuccessActivity provideRegisterVpnSuccessActivity() {
        return (RegisterVpnSuccessActivity) mView;
    }
}
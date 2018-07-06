package com.stratagile.qlink.ui.activity.wordcup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.mainwallet.MainWalletActivity;
import com.stratagile.qlink.ui.activity.wordcup.component.DaggerOpenMainWalletComponent;
import com.stratagile.qlink.ui.activity.wordcup.contract.OpenMainWalletContract;
import com.stratagile.qlink.ui.activity.wordcup.module.OpenMainWalletModule;
import com.stratagile.qlink.ui.activity.wordcup.presenter.OpenMainWalletPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description
 * @date 2018/06/13 17:37:05
 */

public class OpenMainWalletFragment extends BaseFragment implements OpenMainWalletContract.View {

    @Inject
    OpenMainWalletPresenter mPresenter;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      /* View view = inflater.inflate(R.layout.fragment_openMainWallet, null);
       ButterKnife.bind(this, view);
       Bundle mBundle = getArguments();*/
       return null;
   }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            startActivity(new Intent(getActivity(), MainWalletActivity.class));
        }
//        KLog.i("Second","MyBaseActivityCreated");
    }

    @Override
    protected void setupFragmentComponent() {
       DaggerOpenMainWalletComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .openMainWalletModule(new OpenMainWalletModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(OpenMainWalletContract.OpenMainWalletContractPresenter presenter) {
        mPresenter = (OpenMainWalletPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }
    public static OpenMainWalletFragment newInstance(String param) {
        OpenMainWalletFragment fragment = new OpenMainWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}
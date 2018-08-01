package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.rank.component.DaggerRankListComponent;
import com.stratagile.qlink.ui.activity.rank.contract.RankListContract;
import com.stratagile.qlink.ui.activity.rank.module.RankListModule;
import com.stratagile.qlink.ui.activity.rank.presenter.RankListPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: $description
 * @date 2018/07/31 18:09:12
 */

public class RankListFragment extends BaseFragment implements RankListContract.View {

    @Inject
    RankListPresenter mPresenter;
    @BindView(R.id.tv_page)
    TextView tvPage;

    private RankViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_list, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(RankViewModel.class);
        setListener();
        return view;
    }

    private void setListener() {
        viewModel.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                tvPage.setText(integer.toString());
            }
        });
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerRankListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .rankListModule(new RankListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RankListContract.RankListContractPresenter presenter) {
        mPresenter = (RankListPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
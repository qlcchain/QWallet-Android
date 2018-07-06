package com.stratagile.qlink.ui.activity.wordcup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.wordcup.component.DaggerWordCupIntroduceComponent;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupIntroduceContract;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupIntroduceModule;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupIntroducePresenter;
import com.stratagile.qlink.utils.SpUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description
 * @date 2018/06/11 13:50:44
 */

public class WordCupIntroduceActivity extends BaseActivity implements WordCupIntroduceContract.View {

    @Inject
    WordCupIntroducePresenter mPresenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.t_tip)
    TextView tTip;
    @BindView(R.id.bt_play)
    Button btPlay;
    @BindView(R.id.bt_back)
    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_word_cup_introduce);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        if (SpUtil.getBoolean(this, "isInWorldCupIntroduce", false)) {
            startActivity(new Intent(this, WordCupGamesActivity.class));
            finish();
        } else {
            SpUtil.putBoolean(this, "isInWorldCupIntroduce", true);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerWordCupIntroduceComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .wordCupIntroduceModule(new WordCupIntroduceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WordCupIntroduceContract.WordCupIntroduceContractPresenter presenter) {
        mPresenter = (WordCupIntroducePresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_play, R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_play:
                startActivity(new Intent(this, WordCupGamesActivity.class));
                finish();
                break;
            case R.id.bt_back:
                finish();
                break;
            default:
                break;
        }
    }
}
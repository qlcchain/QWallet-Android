package com.stratagile.qlink.ui.activity.vpn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.rank.RankListFragment;
import com.stratagile.qlink.ui.activity.rank.RuleActivity;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerMyAssetsComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.MyAssetsContract;
import com.stratagile.qlink.ui.activity.vpn.module.MyAssetsModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.MyAssetsPresenter;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/11/11 21:42:25
 */

public class MyAssetsActivity extends BaseActivity implements MyAssetsContract.View {

    @Inject
    MyAssetsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_assets);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("My Assets");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fg_container, AssetListFragment.newInstance(""));
        transaction.commit();
    }

    @Override
    protected void setupActivityComponent() {
       DaggerMyAssetsComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .myAssetsModule(new MyAssetsModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(MyAssetsContract.MyAssetsContractPresenter presenter) {
        mPresenter = (MyAssetsPresenter) presenter;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, RegisteVpnActivity.class);
            intent.putExtra("flag", "");
            startActivityForResult(intent, 0);
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
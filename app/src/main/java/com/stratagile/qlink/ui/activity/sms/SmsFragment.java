package com.stratagile.qlink.ui.activity.sms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.SelectCountry;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.ui.activity.sms.component.DaggerSmsComponent;
import com.stratagile.qlink.ui.activity.sms.contract.SmsContract;
import com.stratagile.qlink.ui.activity.sms.module.SmsModule;
import com.stratagile.qlink.ui.activity.sms.presenter.SmsPresenter;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.VpnListFragment;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.stratagile.qlink.ui.activity.wifi.WifiFragment.START_CREATE_PASSWORD;
import static com.stratagile.qlink.ui.activity.wifi.WifiFragment.START_NO_WALLLET;
import static com.stratagile.qlink.ui.activity.wifi.WifiFragment.START_VERTIFY_PASSWORD;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.sms
 * @Description: $description
 * @date 2018/01/10 14:59:05
 */

public class SmsFragment extends BaseFragment implements SmsContract.View {

    @Inject
    SmsPresenter mPresenter;
    //    @BindView(R.id.tabLayout)
//    TextView tabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    public static final int SELECT_CONTINENT = 4;
    @BindView(R.id.registerVpn)
    TextView registerVpn;
//    @BindView(R.id.title)
//    AppCompatTextView title;
//    @BindView(R.id.iv_world)
//    ImageView ivWorld;
//    @BindView(R.id.ll_select_country_guide)
//    LinearLayout llSelectCountryGuide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        ArrayList<String> titles = new ArrayList<>();
        titles.add(ConstantValue.longcountry.toUpperCase());
        if (ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
//            registerVpn.setVisibility(View.GONE);
        } else {
            registerVpn.setVisibility(View.VISIBLE);
        }
//        titles.add("UNREGISTERED");
//        titles.add(getResources().getString(R.string.generalsettings));
//        titles.add(getResources().getString(R.string.faq));
//        titles.add(getResources().getString(R.string.about));
        RelativeLayout relativeLayout_root = (RelativeLayout) view.findViewById(R.id.root_rl);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new VpnListFragment();
//                    case 1:
//                        return new VpnListFragment();
//                    case 2:
//                        return new GraphFragment();
//                    case 2:
//                        return new VPNProfileList();
//                    case 4:
//                        return new AboutFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
//        tabLayout.setupWithViewPager(viewPager);

        /* Toolbar and slider should have the same elevation */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            disableToolbarElevation();
        }
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeTestUI(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        if (!SpUtil.getBoolean(getActivity(), ConstantValue.isMainNet, false)) {
            registerVpn.setVisibility(View.VISIBLE);
        } else {
//            registerVpn.setVisibility(View.GONE);
        }
    }

    private static final String FEATURE_TELEVISION = "android.hardware.type.television";
    private static final String FEATURE_LEANBACK = "android.software.leanback";

    private boolean isDirectToTV() {
        return (getActivity().getPackageManager().hasSystemFeature(FEATURE_TELEVISION)
                || getActivity().getPackageManager().hasSystemFeature(FEATURE_LEANBACK));
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void disableToolbarElevation() {
//        ActionBar toolbar = getActivity().getActionBar();
//        toolbar.setElevation(0);
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerSmsComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .smsModule(new SmsModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void setPresenter(SmsContract.SmsContractPresenter presenter) {
        mPresenter = (SmsPresenter) presenter;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CONTINENT && resultCode == RESULT_OK) {
            String country = data.getStringExtra("country");
//            tabLayout.setText(country.toUpperCase());
            String continent = data.getStringExtra("continent");
            EventBus.getDefault().post(new SelectCountry(country, continent));
        }
    }

    //    @OnClick(R.id.ll_select_country)
//    public void onViewClicked() {
//        Intent intent = new Intent(getActivity(), SelectContinentActivity.class);
//        intent.putExtra("country", tabLayout.getText().toString().toLowerCase());
//        startActivityForResult(intent, SELECT_CONTINENT);
//    }
    int jianjushijian = 500;
    long dangqianshijian = 0;

    @OnClick({R.id.registerVpn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerVpn:
                if (Calendar.getInstance().getTimeInMillis() - dangqianshijian <= jianjushijian) {
                    return;
                }
                dangqianshijian = Calendar.getInstance().getTimeInMillis();
                List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                if (SpUtil.getString(getContext(), ConstantValue.walletPassWord, "").equals("") && SpUtil.getString(getContext(), ConstantValue.fingerPassWord, "").equals("")) {
                    Intent intent = new Intent(getActivity(), CreateWalletPasswordActivity.class);
                    startActivityForResult(intent, START_CREATE_PASSWORD);
                    return;
                }
                if (walletList == null || walletList.size() == 0) {
                    Intent intent = new Intent(getActivity(), NoWalletActivity.class);
                    intent.putExtra("flag", "nowallet");
                    startActivityForResult(intent, START_NO_WALLLET);
                    return;
                }
                if (ConstantValue.isShouldShowVertifyPassword) {
                    Intent intent = new Intent(getActivity(), VerifyWalletPasswordActivity.class);
                    startActivityForResult(intent, START_VERTIFY_PASSWORD);
                    return;
                }
                Intent intent = new Intent(getActivity(), RegisteVpnActivity.class);
                intent.putExtra("flag", "");
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                break;
//            case R.id.iv_world:
//                startActivity(new Intent(getActivity(), WordCupIntroduceActivity.class));
//                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showChooseCountryGuide(ShowGuide showGuide) {
        if (showGuide.getNumber() == 2) {
            showGuideViewSelectCountry();
        }
    }

    private void showGuideViewSelectCountry() {
//        if (!GuideSpUtil.getBoolean(getActivity(), GuideConstantValue.isShowChooseCountryGuide, false)) {
//            GuideSpUtil.putBoolean(getActivity(), GuideConstantValue.isShowChooseCountryGuide, true);
//            GuideBuilder builder = new GuideBuilder();
//            builder.setTargetView(llSelectCountryGuide)
//                    .setAlpha(150)
//                    .setHighTargetCorner(20)
//                    .setOverlayTarget(false)
//                    .setOutsideTouchable(true);
//            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
//                @Override
//                public void onShown() {
//                }
//
//                @Override
//                public void onDismiss() {
//                    EventBus.getDefault().post(new ShowGuide(1));
//                }
//            });
//
//            builder.addComponent(new ChooseCountryComponent());
//            Guide guide = builder.createGuide();
//            guide.setShouldCheckLocInWindow(false);
//            guide.show(getActivity());
//        }
    }
}
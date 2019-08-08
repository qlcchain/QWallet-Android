package com.stratagile.qlink.ui.activity.finance;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.ui.activity.finance.component.DaggerInviteComponent;
import com.stratagile.qlink.ui.activity.finance.contract.InviteContract;
import com.stratagile.qlink.ui.activity.finance.module.InviteModule;
import com.stratagile.qlink.ui.activity.finance.presenter.InvitePresenter;
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.LanguageUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.ScaleCircleNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/23 15:34:34
 */

public class InviteActivity extends BaseActivity implements InviteContract.View {

    @Inject
    InvitePresenter mPresenter;
    @BindView(R.id.tvIniviteCode)
    TextView tvIniviteCode;
    @BindView(R.id.tvRank)
    TextView tvRank;
    @BindView(R.id.userAvatar)
    ImageView userAvatar;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.inivtePersons)
    TextView inivtePersons;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.tvInivteNow)
    TextView tvInivteNow;
    @BindView(R.id.llbottom)
    LinearLayout llbottom;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.llCopy)
    LinearLayout llCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tvIniviteCode.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
            }
        });
        tvInivteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteActivity.this, InviteNowActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.share_with_friends));
        tvIniviteCode.setText(ConstantValue.currentUser.getInviteCode());
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        mPresenter.getInivteRank(infoMap);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerInviteComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .inviteModule(new InviteModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(InviteContract.InviteContractPresenter presenter) {
        mPresenter = (InvitePresenter) presenter;
    }

    class ViewAdapter extends PagerAdapter {
        public ViewAdapter(List<InviteList.GuanggaoListBean> views) {
            this.guanggaoListBeans = views;
            viewList = new ArrayList<>();
        }

        private List<InviteList.GuanggaoListBean> guanggaoListBeans;

        private List<View> viewList;

        @Override
        public int getCount() {
            return guanggaoListBeans.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(InviteActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (LanguageUtil.isCN(InviteActivity.this)) {
                Glide.with(InviteActivity.this)
                        .load(MainAPI.MainBASE_URL + guanggaoListBeans.get(position).getImgPath())
                        .into(imageView);
            } else {
                Glide.with(InviteActivity.this)
                        .load(MainAPI.MainBASE_URL + guanggaoListBeans.get(position).getImgPathEn())
                        .into(imageView);
            }
            container.addView(imageView);
            viewList.add(imageView);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"".equals(guanggaoListBeans.get(position).getUrl())) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(guanggaoListBeans.get(position).getUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }
            });
            return imageView;
        }
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
    public void setData(InviteList inviteList) {
        userName.setText(inviteList.getMyInfo().getName());
        inivtePersons.setText(getString(R.string.invited_) + " " + inviteList.getMyInfo().getTotalInvite() + " " +  getString(R.string.friends));
        tvRank.setText(inviteList.getMyInfo().getMyRanking() + "");
        InvitedAdapter invitedAdapter = new InvitedAdapter(inviteList.getTop5());
        invitedAdapter.addFooterView(getLayoutInflater().inflate(R.layout.invite_list_footview_layout, null, false));
        recyclerView.setAdapter(invitedAdapter);
        invitedAdapter.getFooterLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteActivity.this, MyRankingActivity.class));
            }
        });
        Glide.with(this)
                .load(MainAPI.MainBASE_URL + inviteList.getMyInfo().getHead())
                .apply(AppConfig.getInstance().options)
                .into(userAvatar);
        viewPager.setAdapter(new ViewAdapter(inviteList.getGuanggaoList()));
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(this);
        scaleCircleNavigator.setCircleCount(inviteList.getGuanggaoList().size());
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(getResources().getColor(R.color.mainColor));
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(indicator, viewPager);
    }

}
package com.stratagile.qlink.ui.activity.my;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.eventbus.UpdateAvatar;
import com.stratagile.qlink.statusbar.StatusBarCompat;
import com.stratagile.qlink.ui.activity.main.EditInputActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerPersonComponent;
import com.stratagile.qlink.ui.activity.my.contract.PersonContract;
import com.stratagile.qlink.ui.activity.my.module.PersonModule;
import com.stratagile.qlink.ui.activity.my.presenter.PersonPresenter;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/22 14:28:46
 */

public class PersonActivity extends BaseActivity implements PersonContract.View {

    @Inject
    PersonPresenter mPresenter;
    @BindView(R.id.profile)
    LinearLayout profile;
    @BindView(R.id.username)
    LinearLayout username;
    @BindView(R.id.inviteCode)
    LinearLayout inviteCode;
    @BindView(R.id.email)
    LinearLayout email;
    @BindView(R.id.mobile)
    LinearLayout mobile;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvInviteCode)
    TextView tvInviteCode;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.tvVerification)
    TextView tvVerification;
    @BindView(R.id.verification)
    LinearLayout verification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        if (ConstantValue.currentUser == null) {
            startActivity(new Intent(this, AccountActivity.class));
            finish();
        }
        setTitle(getString(R.string.person_info));
        EventBus.getDefault().register(this);
        if (!"".equals(ConstantValue.currentUser.getEmail())) {
            tvEmail.setText(ConstantValue.currentUser.getEmail());
        } else {
            tvEmail.setText(R.string.unverified);
        }
        if (!"".equals(ConstantValue.currentUser.getPhone())) {
            tvMobile.setText(ConstantValue.currentUser.getPhone());
        } else {
            tvMobile.setText(R.string.unverified);
        }
        tvInviteCode.setText(ConstantValue.currentUser.getInviteCode());
        if ("".equals(ConstantValue.currentUser.getUserName())) {
            tvUserName.setText(ConstantValue.currentUser.getAccount());
        } else {
            tvUserName.setText(ConstantValue.currentUser.getUserName());
        }
        if (ConstantValue.currentUser.getAvatar() != null && !"".equals(ConstantValue.currentUser.getAvatar())) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar);
        } else {
            Glide.with(this)
                    .load(R.mipmap.icon_user_default)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar);
        }
        Map map = new HashMap<String, String>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        mPresenter.getUserInfo(map);
//        switch (ConstantValue.currentUser.getVstatus()) {
//            case "":
//                break;
//            default:
//                break;
//        }
        if (ConstantValue.currentUser.getVstatus() == null) {
            return;
        }
        switch (ConstantValue.currentUser.getVstatus()) {
            case "NOT_UPLOAD":
                tvVerification.setText(getString(R.string.unverified));
                break;
            case "UPLOADED":
                tvVerification.setText(getString(R.string.under_review));
                break;
            case "KYC_SUCCESS":
                tvVerification.setText(getString(R.string.verified));
                break;
            case "KYC_FAIL":
                tvVerification.setText(getString(R.string.not_approved));
                break;
            default:
                break;
        }
    }

    @Override
    public void setUsrInfo(UserInfo usrInfo) {
        KLog.i("更新呢用户信息");
        ConstantValue.currentUser.setHoldingPhoto(usrInfo.getData().getHoldingPhoto());
        ConstantValue.currentUser.setFacePhoto(usrInfo.getData().getFacePhoto());
        ConstantValue.currentUser.setVstatus(usrInfo.getData().getVStatus());
        ConstantValue.currentUser.setInviteCode(usrInfo.getData().getNumber());
        ConstantValue.currentUser.setAvatar(usrInfo.getData().getHead());
        ConstantValue.currentUser.setUserName(usrInfo.getData().getNickname());
        ConstantValue.currentUser.setUserId(usrInfo.getData().getId());
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
        switch (ConstantValue.currentUser.getVstatus()) {
            case "NOT_UPLOAD":
                tvVerification.setText(getString(R.string.unverified));
                break;
            case "UPLOADED":
                tvVerification.setText(getString(R.string.under_review));
                break;
            case "KYC_SUCCESS":
                tvVerification.setText(getString(R.string.verified));
                break;
            case "KYC_FAIL":
                tvVerification.setText(getString(R.string.not_approved));
                break;
            default:
                break;
        }


    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateImg(UpdateAvatar updateAvatar) {
        if (!"".equals(ConstantValue.currentUser.getAvatar())) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerPersonComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .personModule(new PersonModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(PersonContract.PersonContractPresenter presenter) {
        mPresenter = (PersonPresenter) presenter;
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
    public void changeNickNameBack(BaseBack baseBack) {
        closeProgressDialog();
        ConstantValue.currentUser.setUserName(nickName);
        tvUserName.setText(nickName);
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
        ToastUtil.displayShortToast(getString(R.string.success));
    }

    @OnClick({R.id.profile, R.id.username, R.id.inviteCode, R.id.email, R.id.mobile, R.id.verification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                Intent intent1 = new Intent(this, ProfilePictureActivity.class);
//                Intent intent1 = new Intent(this, SelectWalletTypeActivity.class);
                startActivityForResult(intent1, 2);
                break;
            case R.id.username:
                Intent intent = new Intent(this, EditInputActivity.class);
                intent.putExtra("title", getString(R.string.username));
                intent.putExtra("hint", getString(R.string.username));
                intent.putExtra("content", ConstantValue.currentUser.getUserName());
                startActivityForResult(intent, 1);
                break;
            case R.id.inviteCode:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", ConstantValue.currentUser.getInviteCode() + "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
                break;
            case R.id.email:
                break;
            case R.id.mobile:
                break;
            case R.id.verification:
                startActivityForResult(new Intent(this, VerificationActivity.class), 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            switch (ConstantValue.currentUser.getVstatus()) {
                case "NOT_UPLOAD":
                    tvVerification.setText(getString(R.string.unverified));
                    break;
                case "UPLOADED":
                    tvVerification.setText(getString(R.string.under_review));
                    break;
                case "KYC_SUCCESS":
                    tvVerification.setText(getString(R.string.verified));
                    break;
                case "KYC_FAIL":
                    tvVerification.setText(getString(R.string.not_approved));
                    break;
                default:
                    break;
            }
        }
        if (requestCode == 1 && resultCode == -1) {
            changeNickNmae(data.getStringExtra("result"));
        }
        if (requestCode == 2 && resultCode == -1) {

        }
    }

    String nickName = "";

    private void changeNickNmae(String nickName) {
        showProgressDialog();
        this.nickName = nickName;
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        infoMap.put("nickname", nickName);
        mPresenter.changeNickName(infoMap);
    }
}
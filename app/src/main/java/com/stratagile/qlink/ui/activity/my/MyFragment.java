package com.stratagile.qlink.ui.activity.my;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.IndexInterface;
import com.stratagile.qlink.entity.RedPoint;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.entity.eventbus.LoginSuccess;
import com.stratagile.qlink.entity.eventbus.ShowBind;
import com.stratagile.qlink.entity.eventbus.ShowDot;
import com.stratagile.qlink.entity.eventbus.ShowEpidemic;
import com.stratagile.qlink.entity.eventbus.ShowMiningAct;
import com.stratagile.qlink.entity.eventbus.UpdateAvatar;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.ui.activity.finance.InviteActivity;
import com.stratagile.qlink.ui.activity.finance.JoinCommunityActivity;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.main.TestActivity;
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerMyComponent;
import com.stratagile.qlink.ui.activity.my.contract.MyContract;
import com.stratagile.qlink.ui.activity.my.module.MyModule;
import com.stratagile.qlink.ui.activity.my.presenter.MyPresenter;
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.FireBaseUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.TimeUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.MyItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/09 10:02:03
 */

public class MyFragment extends BaseFragment implements MyContract.View {
    @Override
    protected void initDataFromNet() {

    }
    @Inject
    MyPresenter mPresenter;
    @BindView(R.id.userAvatar)
    ImageView userAvatar;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userId)
    TextView userId;
    @BindView(R.id.user)
    LinearLayout user;
    @BindView(R.id.cryptoWallet)
    MyItemView cryptoWallet;
    @BindView(R.id.shareFriend)
    MyItemView shareFriend;
    @BindView(R.id.joinCommunity)
    MyItemView joinCommunity;
    @BindView(R.id.contactUs)
    MyItemView contactUs;
    @BindView(R.id.settings)
    MyItemView settings;
    boolean isLogin = false;
    @BindView(R.id.testView)
    View testView;
    @BindView(R.id.claimQlc)
    MyItemView claimQlc;
    @BindView(R.id.mining)
    MyItemView mining;
    @BindView(R.id.rlVoteEnter)
    RelativeLayout rlVoteEnter;
    @BindView(R.id.covid19)
    MyItemView covid19;
    @BindView(R.id.status_bar)
    TextView statusBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(UIUtils.getDisplayWidth(getActivity()), UIUtils.getStatusBarHeight(getActivity()));
        statusBar.setLayoutParams(llp);

        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccess loginSuccess) {
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getIsLogin()) {
                    loginUser = userAccount;
                    ConstantValue.currentUser = userAccount;
                    userAccount.setStartApp(false);
                    viewModel.currentUserAccount.postValue(userAccount);

                    if (userAccount.getBindDate() == null || "".equals(userAccount.getBindDate())) {
                        viewModel.isBind.postValue(false);
                        EventBus.getDefault().post(new ShowBind());
                    } else {
                        viewModel.isBind.postValue(true);
                        getCanClaimTotal();
                    }

                    isLogin = true;
                    userName.setText(loginUser.getAccount());
                    if (ConstantValue.currentUser.getAvatar() != null && !"".equals(ConstantValue.currentUser.getAvatar())) {
                        Glide.with(this)
                                .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    } else {
                        Glide.with(this)
                                .load(R.mipmap.icon_user_default)
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    }
                }
            }
        }
    }

    private void controllerDot() {
        if (mining.getDotViewVisible() == View.VISIBLE || claimQlc.getDotViewVisible() == View.VISIBLE || shareFriend.getDotViewVisible() == View.VISIBLE || covid19.getDotViewVisible() == View.VISIBLE) {
            EventBus.getDefault().post(new ShowDot(true));
        } else {
            EventBus.getDefault().post(new ShowDot(false));
        }
    }

    @Override
    public void setRedPoint(RedPoint redPoint) {
        if (redPoint.getGzbdPoint() == 0) {
            covid19.setDotViewVisible(View.GONE);
        } else {
            covid19.setDotViewVisible(View.VISIBLE);
        }
        //0是未中断，1是中断
        if (redPoint.getGzbdFocusInterrupt() == 1) {
            EventBus.getDefault().post(new ShowEpidemic(true));
        } else {
            if (!SpUtil.getBoolean(getActivity(), ConstantValue.showedEpidemic, false)) {
                EventBus.getDefault().post(new ShowEpidemic(false));
            }
        }
        controllerDot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showMiningAct(ShowMiningAct showMiningAct) {
        mining.setVisibility(showMiningAct.isShow() ? View.VISIBLE : View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateImg(UpdateAvatar updateAvatar) {
        if (ConstantValue.currentUser.getAvatar() != null && !"".equals(ConstantValue.currentUser.getAvatar())) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                    .apply(AppConfig.getInstance().options)
                    .into(userAvatar);
        } else {
            Glide.with(this)
                    .load(R.mipmap.icon_user_default)
                    .apply(AppConfig.getInstance().options)
                    .into(userAvatar);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getIsLogin()) {
                    loginUser = userAccount;
                    ConstantValue.currentUser = userAccount;
                    userAccount.setStartApp(true);
                    viewModel.currentUserAccount.postValue(userAccount);
                    isLogin = true;
                    userName.setText(loginUser.getAccount());
                    if (ConstantValue.currentUser.getAvatar() != null && !"".equals(ConstantValue.currentUser.getAvatar())) {
                        Glide.with(this)
                                .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    } else {
                        Glide.with(this)
                                .load(R.mipmap.icon_user_default)
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    }
                    Map map = new HashMap<String, String>();
                    map.put("account", userAccount.getAccount());
                    map.put("token", AccountUtil.getUserToken());
                    mPresenter.getUserInfo(map);
                }
            }
//            if (ConstantValue.currentUser == null) {
//                viewModel.noUserLogin.postValue("xxx");
//            }
        }
        claimQlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    if (ConstantValue.currentUser.getBindDate() == null || "".equals(ConstantValue.currentUser.getBindDate())) {
                        viewModel.isBind.postValue(false);
                        EventBus.getDefault().post(new ShowBind());
                    } else {
                        viewModel.isBind.postValue(true);
                        claimQlc.setDotViewVisible(View.INVISIBLE);
                        startActivity(new Intent(getActivity(), MyClaimActivity.class));
                        controllerDot();
                    }
                } else {
                    startActivityForResult(new Intent(getActivity(), AccountActivity.class), 0);
                }
            }
        });
        viewModel.indexInterfaceMutableLiveData.observe(this, new Observer<IndexInterface>() {
            @Override
            public void onChanged(@Nullable IndexInterface indexInterface) {
                if (indexInterface.getDictList().getShow19().equals("1")) {
                    covid19.setVisibility(View.VISIBLE);
                } else {
                    covid19.setVisibility(View.GONE);
                }
                if (TimeUtil.timeStamp(indexInterface.getDictList().getBurnQgasVoteStartDate()) > indexInterface.getCurrentTimeMillis() && TimeUtil.timeStamp(indexInterface.getDictList().getBurnQgasVoteEndDate()) < indexInterface.getCurrentTimeMillis()) {
                    rlVoteEnter.setVisibility(View.VISIBLE);
                } else {
                    rlVoteEnter.setVisibility(View.GONE);
                }
            }
        });
    }

    private void bindPush(UserAccount userAccount) {
        if ("".equals(JPushInterface.getRegistrationID(getActivity()))) {
            return;
        }
        Map map = new HashMap<String, String>();
        map.put("account", userAccount.getAccount());
        map.put("token", AccountUtil.getUserToken());
        map.put("appOs", "Android");
        map.put("pushPlatform", "JIGUANG");
        map.put("pushId", JPushInterface.getRegistrationID(getActivity()));

        KLog.i("极光推送id" + JPushInterface.getRegistrationID(getActivity()));
        mPresenter.bindPush(map);
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerMyComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .myModule(new MyModule(this))
                .build()
                .inject(this);
    }

    private void getMiningRewardTotal() {
        Map map = new HashMap<String, String>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        map.put("status", "NO_AWARD");
        mPresenter.getMiningRewardTotal(map);
    }

    private void redPoint() {
        Map map = new HashMap<String, String>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        mPresenter.redPoint(map);
    }

    @Override
    public void setPresenter(MyContract.MyContractPresenter presenter) {
        mPresenter = (MyPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    UserAccount loginUser;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLogin = false;
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getIsLogin()) {
                    loginUser = userAccount;
                    ConstantValue.currentUser = userAccount;
                    isLogin = true;
                    userName.setText(loginUser.getAccount());
                    if (ConstantValue.currentUser.getAvatar() != null && !"".equals(ConstantValue.currentUser.getAvatar())) {
                        Glide.with(this)
                                .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    } else {
                        Glide.with(this)
                                .load(R.mipmap.icon_user_default)
                                .apply(AppConfig.getInstance().options)
                                .into(userAvatar);
                    }
                    Map map = new HashMap<String, String>();
                    map.put("account", userAccount.getAccount());
                    map.put("token", AccountUtil.getUserToken());
                    mPresenter.getUserInfo(map);
                }
            }
        }
        if (!isLogin) {
            userName.setText(R.string.login_register);
            Glide.with(this)
                    .load(R.mipmap.icon_user_default)
                    .apply(AppConfig.getInstance().options)
                    .into(userAvatar);
        }
    }

    private void getCanClaimTotal() {
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        infoMap.put("type", "REGISTER");
        infoMap.put("status", "NEW");
        mPresenter.getCanClaimTotal(infoMap);
    }

    private void getCanInviteClaimTotal() {
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        infoMap.put("status", "NO_AWARD");
        mPresenter.getCanInviteClaimTotal(infoMap);
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
    public void setUsrInfo(UserInfo usrInfo) {
        bindPush(ConstantValue.currentUser);

        if (usrInfo.getData().getBindDate() == null || "".equals(usrInfo.getData().getBindDate())) {
            viewModel.isBind.postValue(false);
        } else {
            viewModel.isBind.postValue(true);
            getCanClaimTotal();
        }
        KLog.i("更新呢用户信息");
        ConstantValue.currentUser.setHoldingPhoto(usrInfo.getData().getHoldingPhoto());
        ConstantValue.currentUser.setFacePhoto(usrInfo.getData().getFacePhoto());
        ConstantValue.currentUser.setTotalInvite(usrInfo.getData().getTotalInvite());
        ConstantValue.currentUser.setVstatus(usrInfo.getData().getVStatus());
        ConstantValue.currentUser.setBindDate(usrInfo.getData().getBindDate());
        ConstantValue.currentUser.setInviteCode(usrInfo.getData().getNumber());
        ConstantValue.currentUser.setAvatar(usrInfo.getData().getHead());
        ConstantValue.currentUser.setUserName(usrInfo.getData().getNickname());
        ConstantValue.currentUser.setUserId(usrInfo.getData().getId());
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
    }

    @Override
    public void setCanClaimTotal(RewardTotal rewardTotal) {
        getCanInviteClaimTotal();
        if (rewardTotal.getRewardTotal() != 0) {
            claimQlc.setDotViewVisible(View.VISIBLE);
        } else {
            claimQlc.setDotViewVisible(View.INVISIBLE);
        }
        controllerDot();
    }

    @Override
    public void setCanInviteClaimTotal(InviteTotal rewardTotal) {
        if (!"0".equals(rewardTotal.getInviteTotal())) {
            shareFriend.setDotViewVisible(View.VISIBLE);
        } else {
            shareFriend.setDotViewVisible(View.INVISIBLE);
        }
        getMiningRewardTotal();
        controllerDot();
    }

    @Override
    public void bindPushBack() {
        Set<String> tags = new HashSet<>();
        tags.add(ConstantValue.userAll);
        if (!"".equals(ConstantValue.currentUser.getBindDate())) {
            tags.add(ConstantValue.userLend);
        }
        if ("Meizu16th".equals(SystemUtil.getDeviceBrand() + SystemUtil.getSystemModel())) {
            KLog.i("添加测试tag");
            tags.add("qwallet_test");
        }
        ConstantValue.jpushOpreateCount++;
        JPushInterface.setTags(getActivity(), ConstantValue.jpushOpreateCount, tags);
    }

    @Override
    public void setMiningRewardCount(RewardTotal claimQgas) {
        redPoint();
        if (claimQgas.getRewardTotal() != 0) {
            mining.setDotViewVisible(View.VISIBLE);
        } else {
            mining.setDotViewVisible(View.INVISIBLE);
        }
        controllerDot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.user, R.id.cryptoWallet, R.id.shareFriend, R.id.joinCommunity, R.id.contactUs, R.id.settings, R.id.testView, R.id.mining, R.id.rlVoteEnter, R.id.covid19})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user:
                isLogin = false;
                List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
                if (userAccounts.size() > 0) {
                    for (UserAccount userAccount : userAccounts) {
//                        getCanClaimTotal();
                        if (userAccount.getIsLogin()) {
                            loginUser = userAccount;
                            ConstantValue.currentUser = userAccount;
                            isLogin = true;
                            userName.setText(loginUser.getAccount());
                            startActivity(new Intent(getActivity(), PersonActivity.class));
                        }
                    }
                    if (!isLogin) {
                        startActivityForResult(new Intent(getActivity(), AccountActivity.class), 0);
                    }
                } else {
                    startActivityForResult(new Intent(getActivity(), AccountActivity.class), 0);
                }
                break;
            case R.id.cryptoWallet:
                EventBus.getDefault().post(new ChangeViewpager(2));
                break;
            case R.id.shareFriend:
                if (isLogin && ConstantValue.currentUser != null) {
                    shareFriend.setDotViewVisible(View.INVISIBLE);
                    controllerDot();
                    startActivity(new Intent(getActivity(), InviteActivity.class));
                    FireBaseUtils.logEvent(getActivity(), FireBaseUtils.Me_Referral_Rewards);
                } else {
                    startActivityForResult(new Intent(getActivity(), AccountActivity.class), 0);
                }
                break;
            case R.id.joinCommunity:
                startActivity(new Intent(getActivity(), JoinCommunityActivity.class));
                FireBaseUtils.logEvent(getActivity(), FireBaseUtils.Me_Join_the_community);
                break;
            case R.id.contactUs:
                break;
            case R.id.settings:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), 0);
                break;
            case R.id.testView:
                startActivityForResult(new Intent(getActivity(), TestActivity.class), 0);
                break;
            case R.id.rlVoteEnter:
                startActivity(new Intent(getActivity(), VoteActivity.class));
                break;
            case R.id.mining:
                mining.setDotViewVisible(View.INVISIBLE);
                controllerDot();
                startActivityForResult(new Intent(getActivity(), MiningInviteActivity.class), 0);
                break;
            case R.id.covid19:
                if (ConstantValue.currentUser == null) {
                    startActivityForResult(new Intent(getActivity(), AccountActivity.class), 0);
                    return;
                }
                covid19.setDotViewVisible(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), EpidemicRedPaperActivity.class);
                startActivity(intent);
                controllerDot();
                break;
            default:
                break;
        }
    }

}
package com.stratagile.qlink.ui.activity.finance;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.reward.Dict;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.ui.activity.finance.component.DaggerInviteComponent;
import com.stratagile.qlink.ui.activity.finance.contract.InviteContract;
import com.stratagile.qlink.ui.activity.finance.module.InviteModule;
import com.stratagile.qlink.ui.activity.finance.presenter.InvitePresenter;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.recommend.AgencyExcellenceActivity;
import com.stratagile.qlink.ui.activity.recommend.OpenAgentActivity;
import com.stratagile.qlink.ui.activity.reward.ClaimRewardActivity;
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.HashMap;
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.llCopy)
    LinearLayout llCopy;
    @BindView(R.id.ivTitle)
    ImageView ivTitle;
    @BindView(R.id.dot1)
    ImageView dot1;
    @BindView(R.id.base1)
    View base1;
    @BindView(R.id.dot2)
    ImageView dot2;
    @BindView(R.id.base2)
    View base2;
    @BindView(R.id.dot3)
    ImageView dot3;
    @BindView(R.id.tvInivteNow)
    TextView tvInivteNow;
    @BindView(R.id.llbottom)
    LinearLayout llbottom;
    @BindView(R.id.toClaim)
    TextView toClaim;
    @BindView(R.id.tvCanClaimQGAS)
    TextView tvCanClaimQGAS;
    @BindView(R.id.tvClaimNow)
    TextView tvClaimNow;
    @BindView(R.id.rlInvited)
    RelativeLayout rlInvited;
    @BindView(R.id.tvInviteCount)
    TextView tvInviteCount;
    @BindView(R.id.tvClaimedQgas)
    TextView tvClaimedQgas;
    @BindView(R.id.tv1FreindQgas)
    TextView tv1FreindQgas;
    @BindView(R.id.tv2FreindQgas)
    TextView tv2FreindQgas;
    @BindView(R.id.tvAtlistFriend)
    TextView tvAtlistFriend;

    private int atLeastInviteFirend = 0;

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
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.share_with_friends));
        if (ConstantValue.currentUser != null) {
            tvIniviteCode.setText(ConstantValue.currentUser.getInviteCode());
            if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
                //英文
                ivTitle.setBackground(getResources().getDrawable(R.mipmap.ad_share_en));
            } else {
                ivTitle.setBackground(getResources().getDrawable(R.mipmap.ad_share_ch));
            }
            getOneFriendReward();
        } else {
            ToastUtil.displayShortToast(getString(R.string.user_data_error));
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        tvInivteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteActivity.this, InviteNowActivity.class));
            }
        });
        tvClaimNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClaimFriendTotal == 0) {
                    ToastUtil.displayShortToast(getString(R.string.no_qgas_can_claim));
                } else {
                    if (canClaimFriendTotal < atLeastInviteFirend) {
                        ToastUtil.displayShortToast(getString(R.string.not_enough_friends_were_invited));
                    } else {
                        claimQgas();
                    }
                }
            }
        });
        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteActivity.this, AgencyExcellenceActivity.class));
            }
        });
    }

    private void claimQgas() {
        if (ConstantValue.currentUser == null) {
            startActivity(new Intent(this, AccountActivity.class));
        } else {
            Intent claimIntent = new Intent(this, ClaimRewardActivity.class);
            claimIntent.putExtra("total", canClaimFriendTotal * oneFirendClaimQgas + "");
            claimIntent.putExtra("claimType", "invite");
            startActivityForResult(claimIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getCanClaimTotal();
        }
    }

    /**
     * 获取能获取奖励的最少邀请好友数
     */
    private void getAtlistInviteFriend() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("dictType", "winq_invite_user_amount");
        mPresenter.getAtlistInviteFriend(infoMap);
    }

    /**
     * 获取邀请到一个好友的奖励数
     */
    private void getOneFriendReward() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("dictType", "winq_invite_reward_amount");
        mPresenter.getOneFriendReward(infoMap);
    }

    private void getCanClaimTotal() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        infoMap.put("status", "NO_AWARD");
        mPresenter.getCanClaimTotal(infoMap);
    }

    private void getClaimedTotal() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        infoMap.put("status", "AWARDED");
        mPresenter.getClaimedTotal(infoMap);
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
        getAtlistInviteFriend();
        InvitedAdapter invitedAdapter = new InvitedAdapter(inviteList.getTop5(), oneFirendClaimQgas);
        recyclerView.setAdapter(invitedAdapter);
    }

    private float oneFirendClaimQgas = 0;

    private int canClaimFriendTotal = 0;

    private int allInvitedFriend = 0;

    @Override
    public void setCanClaimTotal(InviteTotal inviteTotal) {
        canClaimFriendTotal = Integer.parseInt(inviteTotal.getInviteTotal());
        tvCanClaimQGAS.setText(canClaimFriendTotal * oneFirendClaimQgas + "");
        getClaimedTotal();
    }

    @Override
    public void setClaimedTotal(InviteTotal inviteTotal) {
        allInvitedFriend = canClaimFriendTotal + Integer.parseInt(inviteTotal.getInviteTotal());
        tvInviteCount.setText(getString(R.string.friend_referred, allInvitedFriend + " "));
        tvClaimedQgas.setText(getString(R.string.i_have_claimed, Integer.parseInt(inviteTotal.getInviteTotal()) * oneFirendClaimQgas + ""));
    }

    @Override
    public void setAtlistInviteFriend(Dict dict) {
        atLeastInviteFirend = Integer.parseInt(dict.getData().getValue());
        tvAtlistFriend.setText(getString(R.string.you_can_claim_the_reward_when_successfully_invited_more_than_2_friends, dict.getData().getValue() + ""));
        getCanClaimTotal();
    }

    @Override
    public void setOneFriendReward(Dict dict) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        mPresenter.getInivteRank(infoMap);

        oneFirendClaimQgas = Float.parseFloat(dict.getData().getValue());

        tv1FreindQgas.setText(getString(R.string.get_x_qgas, dict.getData().getValue() + ""));
        tv2FreindQgas.setText(getString(R.string.get_x_qgas, Double.parseDouble(dict.getData().getValue())*2 + ""));
    }

}
package com.stratagile.qlink.ui.activity.im;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.eventbus.JoinNewGroup;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.im.component.DaggerConversationComponent;
import com.stratagile.qlink.ui.activity.im.contract.ConversationContract;
import com.stratagile.qlink.ui.activity.im.module.ConversationModule;
import com.stratagile.qlink.ui.activity.im.presenter.ConversationPresenter;
import com.stratagile.qlink.ui.adapter.im.ConversationListAdapter;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.view.SoftKeyBoardListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.im
 * @Description: $description
 * @date 2018/03/19 15:49:59
 */

public class ConversationActivity extends BaseActivity implements ConversationContract.View {

    @Inject
    ConversationPresenter mPresenter;
    @Inject
    ConversationListAdapter conversationListAdapter;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private VpnEntity vpnEntity;

    private WifiEntity wifiEntity;
    /**
     * 聊天所在的群组id
     */
    private int groupNum;

    private String p2pId;

    private ArrayList<Message> showMessageList = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });
        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
                ivSend.performClick();
//                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginUseIM");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginUseIM");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginUseIM");
        mFirebaseAnalytics.logEvent("beginUseIM", bundle);
        String currentTime = StringUitl.getNowDateShort();
        String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime+"_im","0");
        if(currentTimeFlag.equals("0"))
        {
            bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "useIMTotal");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "useIMTotal");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "useIMTotal");
            mFirebaseAnalytics.logEvent("useIMTotal", bundle);
            SpUtil.putString(AppConfig.getInstance(), currentTime+"_im","1");
        }
        if (getIntent().getStringExtra("assetType").equals("3")) {
            vpnEntity = getIntent().getParcelableExtra("vpnEntity");
            LogUtil.addLog("聊天的vpn为：" + vpnEntity.toString(), getClass().getSimpleName());
            EventBus.getDefault().post(vpnEntity);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            conversationListAdapter.setChatType(3);
            conversationListAdapter.setVpnEntity(vpnEntity);
            recyclerView.setAdapter(conversationListAdapter);
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
            p2pId = SpUtil.getString(this, ConstantValue.P2PID, "");
            KLog.i(vpnEntity.toString());
            setTitle(vpnEntity.getVpnName());
            if (vpnEntity.getP2pId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
                groupNum = vpnEntity.getGroupNum();
                if (ConstantValue.messageMap.get(groupNum) != null) {
                    showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                    conversationListAdapter.setNewData(showMessageList);
                    recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                } else {
                    ConstantValue.messageMap.put(groupNum, new ArrayList<>());
                    showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                    conversationListAdapter.setNewData(showMessageList);
                    recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                }
            } else {
                if (vpnEntity.getGroupNum() == -1) {
                    if (ConstantValue.messageMap.get(vpnEntity.getGroupNum()) != null) {
                        showMessageList.addAll(ConstantValue.messageMap.get(vpnEntity.getGroupNum()));
                    }
                    KLog.i("加入群组");
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("assetName", vpnEntity.getVpnName());
                    infoMap.put("assetType", 3);
                    QlinkUtil.parseMap2StringAndSend(vpnEntity.getFriendNum(), ConstantValue.joinGroupChatReq, infoMap);
                } else {
                    groupNum = vpnEntity.getGroupNum();
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("assetName", vpnEntity.getVpnName());
                    infoMap.put("assetType", 3);
                    QlinkUtil.parseMap2StringAndSend(vpnEntity.getFriendNum(), ConstantValue.joinGroupChatReq, infoMap);
                    if (ConstantValue.messageMap.get(groupNum) != null) {
                        showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                        conversationListAdapter.setNewData(showMessageList);
                        recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                    } else {
                        ConstantValue.messageMap.put(groupNum, new ArrayList<>());
                        showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                        conversationListAdapter.setNewData(showMessageList);
                        recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                    }
                    if (conversationListAdapter.getData().size() == 0) {
                        tvTip.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (getIntent().getStringExtra("assetType").equals("0")) {
            wifiEntity = getIntent().getParcelableExtra("wifiEntity");
            LogUtil.addLog("聊天的wifi为：" + wifiEntity.toString(), getClass().getSimpleName());
            EventBus.getDefault().post(wifiEntity);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            conversationListAdapter.setChatType(0);
            conversationListAdapter.setWifiEntity(wifiEntity);
            recyclerView.setAdapter(conversationListAdapter);
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
            p2pId = SpUtil.getString(this, ConstantValue.P2PID, "");
            KLog.i(wifiEntity.toString());
            setTitle(wifiEntity.getSsid());
            if (wifiEntity.getOwnerP2PId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
                groupNum = wifiEntity.getGroupNum();
                if (ConstantValue.messageMap.get(groupNum) != null) {
                    showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                    conversationListAdapter.setNewData(showMessageList);
                    recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                } else {
                    ConstantValue.messageMap.put(groupNum, new ArrayList<>());
                    showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                    conversationListAdapter.setNewData(showMessageList);
                    recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                }
            } else {
                if (wifiEntity.getGroupNum() == -1) {
                    if (ConstantValue.messageMap.get(wifiEntity.getGroupNum()) != null) {
                        showMessageList.addAll(ConstantValue.messageMap.get(wifiEntity.getGroupNum()));
                    }
                    KLog.i("重新加入群组");
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("assetName", wifiEntity.getSsid());
                    infoMap.put("assetType", 0);
                    QlinkUtil.parseMap2StringAndSend(wifiEntity.getFreindNum(), ConstantValue.joinGroupChatReq, infoMap);
                } else {
                    groupNum = wifiEntity.getGroupNum();
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("assetName", wifiEntity.getSsid());
                    infoMap.put("assetType", 0);
                    QlinkUtil.parseMap2StringAndSend(wifiEntity.getFreindNum(), ConstantValue.joinGroupChatReq, infoMap);
                    if (ConstantValue.messageMap.get(groupNum) != null) {
                        showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                        conversationListAdapter.setNewData(showMessageList);
                        recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                    } else {
                        ConstantValue.messageMap.put(groupNum, new ArrayList<>());
                        showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
                        conversationListAdapter.setNewData(showMessageList);
                        recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
                    }
                    if (conversationListAdapter.getData().size() == 0) {
                        tvTip.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTip.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerConversationComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .conversationModule(new ConversationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConversationContract.ConversationContractPresenter presenter) {
        mPresenter = (ConversationPresenter) presenter;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.iv_send)
    public void onViewClicked() {
        if (etContent.getText().toString().trim().equals("")) {
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
        } else {
            sendMessage(etContent.getText());
            etContent.setText("");
        }
    }

    private void sendMessage(CharSequence message) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("content", message.toString());
        messageMap.put("p2pId", p2pId);
        messageMap.put("nickName", ConstantValue.nickName);
        messageMap.put("avatar", SpUtil.getString(this, ConstantValue.myAvatarPath, ""));
        messageMap.put("avatarUpdateTime", SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, ""));
        Message message1 = new Message(message.toString());
        messageMap.put("messageTime", Calendar.getInstance().getTimeInMillis());
        if (getIntent().getStringExtra("assetType").equals("3")) {
            messageMap.put("assetName", vpnEntity.getVpnName());
            message1.setAssetName(vpnEntity.getVpnName());
            message1.setAssetType(3);
            messageMap.put("assetType", 3);
        } else if (getIntent().getStringExtra("assetType").equals("0")) {
            messageMap.put("assetName", wifiEntity.getSsid());
            message1.setAssetName(wifiEntity.getSsid());
            message1.setAssetType(0);
            messageMap.put("assetType", 0);
        }
        String result = JSONObject.toJSON(messageMap).toString();
        KLog.i("发送消息的结果为：" + qlinkcom.SendMessageToGroupChat(groupNum, result));
        message1.setDirection(1);
        message1.setP2pId(p2pId);
        message1.setGroupNum(groupNum);
        message1.setNickName(ConstantValue.nickName);
        message1.setAvatarUpdateTime(SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, ""));
        message1.setMessageTime(Calendar.getInstance().getTimeInMillis());
        if (ConstantValue.messageMap.get(groupNum) == null) {
            ConstantValue.messageMap.put(groupNum, new ArrayList<>());
        }
        ConstantValue.messageMap.get(groupNum).add(message1);
        recieved(message1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGroupNum(JoinNewGroup joinNewGroup) {
        KLog.i("加入群组成功");
        groupNum = joinNewGroup.getGroupNum();
        if (getIntent().getStringExtra("assetType").equals("3")) {
            vpnEntity.setGroupNum(groupNum);
            AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
            EventBus.getDefault().post(new ArrayList<VpnEntity>());
        } else if (getIntent().getStringExtra("assetType").equals("0")) {
            wifiEntity.setGroupNum(groupNum);
            AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
            EventBus.getDefault().post(new ArrayList<VpnEntity>());
        }
        if (ConstantValue.messageMap.get(groupNum) != null) {
            ConstantValue.messageMap.get(groupNum).clear();
            showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
            conversationListAdapter.setNewData(showMessageList);
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
        } else {
            ConstantValue.messageMap.put(groupNum, new ArrayList<>());
            showMessageList.addAll(ConstantValue.messageMap.get(groupNum));
            conversationListAdapter.setNewData(showMessageList);
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
        }
        if (conversationListAdapter.getData().size() == 0) {
            tvTip.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieved(Message message) {
        KLog.i("接收到消息了。" + message.toString());
        if (groupNum == message.getGroupNum()) {
            //发消息，清除未读标记
            if (getIntent().getStringExtra("assetType").equals("3")) {
                EventBus.getDefault().post(vpnEntity);
            } else if (getIntent().getStringExtra("assetType").equals("0")) {
                EventBus.getDefault().post(wifiEntity);
            }
//            KLog.i("适配器里的数据个数为：" + conversationListAdapter.getData().size());
            conversationListAdapter.addData(message);
            recyclerView.scrollToPosition(conversationListAdapter.getData().size() - 1);
        }
    }
}
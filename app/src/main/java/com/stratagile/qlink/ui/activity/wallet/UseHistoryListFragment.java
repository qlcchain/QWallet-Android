package com.stratagile.qlink.ui.activity.wallet;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerUseHistoryListComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.UseHistoryListContract;
import com.stratagile.qlink.ui.activity.wallet.module.UseHistoryListModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.UseHistoryListPresenter;
import com.stratagile.qlink.ui.adapter.FundDecoration;
import com.stratagile.qlink.ui.adapter.wallet.UseHistoryListAdapter;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.view.RxQRCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/19 11:44:00
 */

public class UseHistoryListFragment extends MyBaseFragment implements UseHistoryListContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    UseHistoryListPresenter mPresenter;
    @Inject
    UseHistoryListAdapter useHistoryListAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_content)
    LinearLayout rlContent;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    Balance mBalance;
    Raw mRaw;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 5;
    private int total;
    private static final String ARG_TYPE = "arg_type";
    private boolean isChanged = false;//申明一个flag 来判断是否已经改变

    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //这边判断,如果是back的按键被点击了   就自己拦截实现掉
                if (i == KeyEvent.KEYCODE_BACK) {
                    if (rlContent.getVisibility() == View.VISIBLE && rlContent.getChildCount() != 0) {
                        endSpringAnimation(rlContent.getChildAt(0));
//                        rlContent.setVisibility(View.GONE);
//                        rlContent.removeAllViews();
                        return true;//表示处理了
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
    };
    private TextView address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fund_layout, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        //监听back必须设置的
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //然后在写这个监听器
        view.setOnKeyListener(backlistener);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new FundDecoration((int) getResources().getDimension(R.dimen.x15)));
        useHistoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (mRaw == null || mBalance == null) {
                    ToastUtil.displayShortToast(getString(R.string.please_wait));
                    /**
                     * @see WalletFragment#preGetBalance(Wallet)
                     */
                    EventBus.getDefault().post(new NeoRefrash());
                    return;
                }
                rlContent.setVisibility(View.VISIBLE);
                rlContent.removeAllViews();
                switch (i) {
                    case 0:
                        setToReceiveFund();
                        break;
                    case 1:
                        setToSendFund();
                        break;
                    case 2:
                        setToViewHistory();
                        break;
                    case 3:
                        setToBuyQlc();
                        break;
                    default:
                        break;

                }
            }
        });
        recyclerView.setAdapter(useHistoryListAdapter);
        List<String> recordSaveList = new ArrayList<>();
        recordSaveList.add(getString(R.string.RECEIVE_FUNDS));
        recordSaveList.add(getString(R.string.SEND_FUNDS));
        recordSaveList.add(getString(R.string.VIEW_HISTORY));
        recordSaveList.add(getString(R.string.BUY_QLC));
        useHistoryListAdapter.setNewData(recordSaveList);
        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    private void setToViewHistory() {
        startActivity(TransactionRecordActivity.class);
        rlContent.setVisibility(View.GONE);
    }
    private void setToReceiveFund() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.recevice_fund_layout, null, false);
        Button btBack = view.findViewById(R.id.bt_back);
        Button btCopy = view.findViewById(R.id.bt_copy);
        CardView cardView = view.findViewById(R.id.cardView);
        TextView tvWalletAddress = view.findViewById(R.id.address);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        ImageView iv_address = view.findViewById(R.id.iv_address);
        Wallet wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
        tvWalletAddress.setText(wallet.getAddress());
        RxQRCode.builder(wallet.getAddress()).
                backColor(getResources().getColor(com.vondear.rxtools.R.color.white)).
                codeColor(getResources().getColor(com.vondear.rxtools.R.color.black)).
                codeSide(800).
                into(iv_address);
        iv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QrCodeDetailActivity.class);
                intent.putExtra("content", wallet.getAddress());
                intent.putExtra("title", "public address");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), Pair.create(iv_address, "qrcode"), Pair.create(cardView, "card"), Pair.create(tvTitle, "title")).toBundle());

                } else {
                    startActivity(intent);
                }
            }
        });
        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tvWalletAddress.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RxAnimationTool.popout(view, 400, new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        rlContent.removeAllViews();
//                        rlContent.setVisibility(View.GONE);
//                    }
//                }).start();
                endSpringAnimation(view);
            }
        });
        rlContent.addView(view);
        startSpringViewAnimation(view);
    }

    private void startSpringViewAnimation(View springView) {
        SpringForce spring = new SpringForce(1f)
                .setDampingRatio(0.6f)
                .setStiffness(200f);
        new SpringAnimation(springView, DynamicAnimation.SCALE_X)
                .setStartValue(0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .start();
        new SpringAnimation(springView, DynamicAnimation.SCALE_Y)
                .setStartValue(0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .start();
    }

    private void endSpringAnimation(View springView) {
        SpringAnimationUtil.endScaleSpringViewAnimation(springView, new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                rlContent.removeAllViews();
                rlContent.setVisibility(View.GONE);
            }
        });
    }
    public boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    /**
     * 设置为第二个选项。
     */
    private void setToSendFund() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.send_fund_layout, null, false);
        LinearLayout paste = view.findViewById(R.id.paste);
        LinearLayout scan = view.findViewById(R.id.scan);
        address = view.findViewById(R.id.address);
        TextView tvMaxQlc = view.findViewById(R.id.tv_max_qlc);
        Button back = view.findViewById(R.id.bt_back);
        Button btSendNow = view.findViewById(R.id.bt_send);
        CheckBox cbQlc = view.findViewById(R.id.cb_qlc);
        cbQlc.setChecked(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSpringAnimation(view);
            }
        });
        EditText etQlcCount = view.findViewById(R.id.et_qlc_count);
        tvMaxQlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQlcCount.setText(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
            }
        });
        etQlcCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    return;
                }
                if (".".equals(charSequence.toString())) {
                    etQlcCount.setText("");
                    return;
                }
                if ("0".equals(charSequence.toString())) {
                    etQlcCount.setText("");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (isChanged){//必须在修改内容前调用
                        return;
                    }
                    if (editable != null && !"".equals(editable.toString()) && !".".equals(editable.toString())) {
                        int selectionStart;
                        int selectionEnd;
                        selectionStart = etQlcCount.getSelectionStart();
                        selectionEnd = etQlcCount.getSelectionEnd();
                        if (!isOnlyPointNumber(etQlcCount.getText().toString())){
                            if(selectionStart >= 1 && selectionEnd >= selectionStart)
                            {
                                editable.delete(selectionStart - 1, selectionEnd);
                            }
                            isChanged=true;
                            etQlcCount.setText(editable);
                        }
                        float toSendQlcCount = Float.parseFloat(editable.toString());
                        KLog.i(toSendQlcCount);
                        if (toSendQlcCount > Float.parseFloat(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "")) {
                            KLog.i(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            etQlcCount.setText(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            isChanged=true;
                            etQlcCount.setSelection((RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "").length());
                        } else {
                            KLog.i("进入else");
                            etQlcCount.setSelection(editable.toString().length());
                        }
                        isChanged = false;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    isChanged=true;
                    etQlcCount.setText("");
                    isChanged = false;
                }
            }
        });
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将ClipData内容放到系统剪贴板里。
                    address.setText(cm.getPrimaryClip().getItemAt(0).getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getScanPermission();
            }
        });

        btSendNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText().toString().trim().equals("")) {
                    return;
                }
                if (etQlcCount.getText().toString().trim().equals("")) {
                    return;
                }
                if (Float.parseFloat(etQlcCount.getText().toString()) <= 0) {
                    return;
                }

                if (vertifyAddressIsSelf(address.getText().toString())) {
                    ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
                    return;
                }

                showSendConfirmDialog(etQlcCount.getText().toString().trim(), address.getText().toString());
            }
        });

        rlContent.addView(view);
        startSpringViewAnimation(view);
    }

    /**
     * 校验钱包地址是不是自己的
     */
    private boolean vertifyAddressIsSelf(String address) {
        if (AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress().equals(address)) {
            return true;
        }
        return false;
    }

    private void showSendConfirmDialog(String qlc, String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        btn_cancel.setText(R.string.cancel);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        btn_comfirm.setText(R.string.yes);
        String content = "Are you sure you want to send " + qlc + " QLC to \n" + address + " ?";
        tvContent.setText(content);
        title.setText(getString(R.string.confirm_withdrawal).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                excuteSend(qlc, address);
            }
        });
//        startSpringViewAnimation(view);
        dialog.show();
    }

    //Map map, String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack
    private void excuteSend(String qlc, String toaddress) {
        if (toaddress.length() == 34) {
            showProgressDialog();
            List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            Wallet wallet = walletList.get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
            Map<String, Object> infoMap = new HashMap<>();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String uuid1 = uuid.substring(0, 32);
            infoMap.put("recordId", uuid1);
            infoMap.put("type", 1);
            infoMap.put("addressFrom", wallet.getAddress());
            infoMap.put("qlc", qlc);
            infoMap.put("addressTo", toaddress);
            TransactionApi.getInstance().v2Transaction(infoMap, wallet.getAddress(), toaddress, qlc, new SendBackWithTxId() {
                @Override
                public void onSuccess(String txid) {
                    TransactionRecord recordSave = new TransactionRecord();
                    recordSave.setTxid(txid);
                    recordSave.setExChangeId(txid);
                    recordSave.setTransactiomType(1);
                    recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                    recordSave.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                    AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                    closeProgressDialog();
                    showSendDialog();
//                    setToRoot();
                }

                @Override
                public void onFailure() {
                    ToastUtil.displayShortToast(getString(R.string.send_failure));
                    closeProgressDialog();
                }
            });

        } else {
            ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
        }
    }



    private void setToRoot() {
        rlContent.removeAllViews();
        rlContent.setVisibility(View.GONE);
    }

    private void setToBuyQlc() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.buy_qlc_layout, null, false);
        TextView tvMaxNeo = view.findViewById(R.id.tv_max_neo);
        TextView tvRate = view.findViewById(R.id.tv_rate);
        EditText etNeoCount = view.findViewById(R.id.et_neo_count);
        EditText etQlcCount = view.findViewById(R.id.et_qlc_count);
        Button btBack = view.findViewById(R.id.bt_back);
        Button btBuyNow = view.findViewById(R.id.bt_buy);
        tvRate.setText("1 NEO = " + mRaw.getData().getRates().getNEO().getQLC() + " QLC");
        etNeoCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    etQlcCount.setText("");
                    return;
                }
//                etQlcCount.setSelection(charSequence.toString().length());
                if (Integer.parseInt(charSequence.toString()) == 0) {
                    etNeoCount.setText("");
                    return;
                }
                if (Integer.parseInt(charSequence.toString()) > mBalance.getData().getNEO()) {
                    etNeoCount.setText(mBalance.getData().getNEO() + "");
                    if (!"".equals(etNeoCount.getText().toString())) {
                        etNeoCount.setSelection((mBalance.getData().getNEO() + "").length());
                    }
                    etQlcCount.setText(mBalance.getData().getNEO()* mRaw.getData().getRates().getNEO().getQLC() + "");
                    return;
                }
                etQlcCount.setText(Float.parseFloat(charSequence.toString()) * mRaw.getData().getRates().getNEO().getQLC() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvMaxNeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNeoCount.setText(mBalance.getData().getNEO() + "");
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSpringAnimation(view);
            }
        });
        btBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNeoCount.getText().toString().equals("")) {
                    return;
                }
                if (Integer.parseInt(etNeoCount.getText().toString()) > mBalance.getData().getNEO()) {

                } else {
                    showBuyQlcConfirmDialog(etNeoCount.getText().toString(), etQlcCount.getText().toString());
                }
            }
        });
        rlContent.addView(view);
        startSpringViewAnimation(view);
    }

    private void showBuyQlcConfirmDialog(String neo, String qlc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        btn_cancel.setText(getString(R.string.cancel).toLowerCase());
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        btn_comfirm.setText(getString(R.string.yes).toLowerCase());
        String content = "Are you sure you want to buy " + qlc + " QLC\n" + "in exchange for " + neo + " NEO?";
        tvContent.setText(content);
        title.setText(getString(R.string.confirm_purchase).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                excuteBuyQlc(neo);
            }
        });
        dialog.show();
    }

    private void showBuyQlcDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.only_ok_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(getString(R.string.purchase_successful).toUpperCase());
        tvContent.setText(R.string.Your_withdrawal_will_arrive_soon);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setToRoot();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    private void showSendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.only_ok_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(getString(R.string.withdrawal_successful).toUpperCase());
        tvContent.setText(R.string.Take_a_rest_Your_transfer_request_is_being_processed);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setToRoot();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void excuteBuyQlc(String neo) {
        mPresenter.getMainAddress(neo);
    }

    @Override
    public void getMainAddressSuccess(String mainAddress, String neo) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet = walletList.get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
        Map<String, String> map = new HashMap<>();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        uuid = uuid.substring(0, 32);
        KLog.i(uuid);
        map.put("neo", neo);
        map.put("exchangeId", uuid);
        map.put("address", wallet.getAddress());
        map.put("wif", wallet.getWif());
        showProgressDialog();
        TransactionApi.getInstance().transactionNEO(mainAddress, neo, wallet.getAddress(), new SendCallBack() {
            @Override
            public void onSuccess() {
                closeProgressDialog();
                showBuyQlcDialog();
            }

            @Override
            public void onFailure() {
                closeProgressDialog();
                ToastUtil.displayShortToast(getString(R.string.error2));
            }
        });
    }

    /**
     * @see WalletFragment#onGetBalancelSuccess(Balance)
     * @param balance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refrashNeo(Balance balance) {
        mBalance = balance;
    }

    /**
     * @see WalletFragment#onGetRawSuccess(Raw)
     * @param raw
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refrashNeo(Raw raw) {
        mRaw = raw;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void fetchData() {
    }

    public void loadDataFromServer(boolean showRefresh) {
//        refreshLayout.setRefreshing(showRefresh);
        if (showRefresh) {
            mNextPage = 1;
        }
        mPresenter.getUseHistoryListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }

    public static UseHistoryListFragment newInstance(String param) {
        UseHistoryListFragment fragment = new UseHistoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mNextPage = 1;
        loadDataFromServer(true);
    }

    @Override
    public void onLoadMoreRequested() {
        if (useHistoryListAdapter.getData().size() < ONE_PAGE_SIZE || useHistoryListAdapter.getData().size() >= total) {
            useHistoryListAdapter.loadMoreEnd(false);
            return;
        }
        mNextPage++;
        mPresenter.getUseHistoryListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerUseHistoryListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .useHistoryListModule(new UseHistoryListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(UseHistoryListContract.UseHistoryListContractPresenter presenter) {
        mPresenter = (UseHistoryListPresenter) presenter;
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
    public void buyQlcBack(BuyQlc buyQlc) {
        if (buyQlc.getData().isResult()) {
            showBuyQlcDialog();
        } else {
            ToastUtil.displayShortToast(getString(R.string.error2));
        }
    }

    @Override
    public void sendFundBack(BuyQlc buyQlc) {
        if (buyQlc.getData().isResult()) {
            showSendDialog();
        } else {
            ToastUtil.displayShortToast(getString(R.string.error2));
        }
    }

    @Override
    public void getScanPermissionSuccess() {
        Intent intent1 = new Intent(getActivity(), ScanQrCodeActivity.class);
        startActivityForResult(intent1, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (address != null) {
                address.setText(data.getStringExtra("result"));
            }
        }
    }
}
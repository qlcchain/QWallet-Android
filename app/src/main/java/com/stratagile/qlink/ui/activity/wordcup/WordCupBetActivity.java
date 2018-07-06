package com.stratagile.qlink.ui.activity.wordcup;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.ui.activity.wordcup.component.DaggerWordCupBetComponent;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupBetContract;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupBetModule;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupBetPresenter;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import neoutils.Wallet;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description
 * @date 2018/06/12 11:36:18
 */

public class WordCupBetActivity extends BaseActivity implements WordCupBetContract.View {

    @Inject
    WordCupBetPresenter mPresenter;
    @BindView(R.id.bt_bet)
    Button btBet;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.tv_bet_home)
    TextView tvBetHome;
    @BindView(R.id.tv_bet_tie)
    TextView tvBetTie;
    @BindView(R.id.tv_bet_away)
    TextView tvBetAway;
    @BindView(R.id.tv_total_qlc)
    TextView tvTotalQlc;
    @BindView(R.id.tv_home_value)
    TextView tvHomeValue;
    @BindView(R.id.tv_tie_value)
    TextView tvTieValue;
    @BindView(R.id.tv_away_value)
    TextView tvAwayValue;
    @BindView(R.id.cardview_bet_status)
    CardView cardviewBetStatus;
    @BindView(R.id.tv_home_wins)
    TextView tvHomeWins;
    @BindView(R.id.tv_tie_game)
    TextView tvTieGame;
    @BindView(R.id.tv_away_wins)
    TextView tvAwayWins;
    @BindView(R.id.tv_game_status_tip)
    TextView tvGameStatusTip;
    @BindView(R.id.tv_you_win_qlc)
    TextView tvYouWinQlc;
    @BindView(R.id.ll_your_win)
    LinearLayout llYourWin;

    private int betIndex = -1;

    RaceTimes.DataBean dataBean;
    @BindView(R.id.tv_group)
    TextView tvGroup;
    @BindView(R.id.tv_race_time)
    TextView tvRaceTime;
    @BindView(R.id.iv_home_country)
    ImageView ivHomeCountry;
    @BindView(R.id.tv_home_country)
    TextView tvHomeCountry;
    @BindView(R.id.tv_home_amount)
    TextView tvHomeAmount;
    @BindView(R.id.tv_away_amount)
    TextView tvAwayAmount;
    @BindView(R.id.iv_away_country)
    ImageView ivAwayCountry;
    @BindView(R.id.tv_away_country)
    TextView tvAwayCountry;
    @BindView(R.id.iv_bet_home)
    ImageView ivBetHome;
    @BindView(R.id.iv_bet_tie)
    ImageView ivBetTie;
    @BindView(R.id.iv_bet_away)
    ImageView ivBetAway;

    Balance mBalance;
    Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_word_cup_bet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout_root.setBackground(getResources().getDrawable(R.mipmap.bg_header));
    }

    @Override
    protected void initData() {
        wallet = Account.INSTANCE.getWallet();
        Map<String, String> map = new HashMap<>();
        map.put("address", wallet.getAddress());
        mPresenter.getBalance(map);
        dataBean = getIntent().getParcelableExtra("dataBean");
        setTitle("my prediction");
        tvGroup.setText(dataBean.getRaceType());
        tvRaceTime.setText(parseTime(dataBean.getRaceTime()));
        tvHomeCountry.setText(dataBean.getHomeCountryName());
        tvAwayCountry.setText(dataBean.getAwayCountryName());
        Glide.with(this)
                .load(getResources().getIdentifier("icon_" + dataBean.getHomeCountryNumber().toLowerCase(Locale.ENGLISH), "mipmap", getPackageName()))
                .into(ivHomeCountry);
        Glide.with(this)
                .load(getResources().getIdentifier("icon_" + dataBean.getAwayCountryNumber().toLowerCase(Locale.ENGLISH), "mipmap", getPackageName()))
                .into(ivAwayCountry);
        if (dataBean.getIsBegin() == 1) {
            //比赛已经结束
            tvHomeAmount.setText(dataBean.getHomeGoalsAmount() + "");
            tvAwayAmount.setText(dataBean.getAwayGoalsAmount() + "");
            tvExplain.setVisibility(View.GONE);
            btBet.setVisibility(View.GONE);
            ivBetHome.setEnabled(false);
            ivBetTie.setEnabled(false);
            ivBetAway.setEnabled(false);
            cardviewBetStatus.setVisibility(View.VISIBLE);
            if (dataBean.getBetRecord() == null) {
                //我没有下注
                llYourWin.setVisibility(View.GONE);
            } else {
                //我已经下注
                switch (dataBean.getBetRecord().getBettingValue()) {
                    case 1:
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_333));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                        switch (dataBean.getRaceResult()) {
                            case 1:
                                tvGameStatusTip.setText("YOU WON!");
                                tvYouWinQlc.setText(dataBean.getBetRecord().getRewardAmount() + " QLC");
                                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_wins));
                                tvBetHome.setTextColor(getResources().getColor(R.color.white));
                                break;
                            case 2:
                                tvGameStatusTip.setText("YOU LOSE!");
                                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_away_wins));
                                tvBetHome.setTextColor(getResources().getColor(R.color.white));
                                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                tvYouWinQlc.setText("0 QLC");
                                break;
                            case 3:
                                tvGameStatusTip.setText("YOU LOSE!");
                                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_away_wins));
                                tvBetHome.setTextColor(getResources().getColor(R.color.white));
                                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                tvYouWinQlc.setText("0 QLC");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_333));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                        switch (dataBean.getRaceResult()) {
                            case 1:
                                tvGameStatusTip.setText("YOU LOSE!");
                                tvYouWinQlc.setText("0 QLC");
                                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_away_wins));
                                tvBetTie.setTextColor(getResources().getColor(R.color.white));
                                break;
                            case 2:
                                tvGameStatusTip.setText("YOU WON!");
                                tvYouWinQlc.setText(dataBean.getBetRecord().getRewardAmount() + " QLC");
                                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_wins));
                                tvBetTie.setTextColor(getResources().getColor(R.color.white));
                                break;
                            case 3:
                                tvGameStatusTip.setText("YOU LOSE!");
                                tvYouWinQlc.setText("0 QLC");
                                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_away_wins));
                                tvBetTie.setTextColor(getResources().getColor(R.color.white));
                                break;
                            default:
                                break;
                        }
                        break;
                    case 3:
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_333));
                        switch (dataBean.getRaceResult()) {
                            case 1:
                                tvGameStatusTip.setText("YOU LOSE!");
                                tvYouWinQlc.setText("0 QLC");
                                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_wins));
                                tvBetAway.setTextColor(getResources().getColor(R.color.white));
                                break;
                            case 2:
                                tvGameStatusTip.setText("YOU LOSE!");
                                tvYouWinQlc.setText("0 QLC");
                                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_lose));
                                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_away_wins));
                                tvBetAway.setTextColor(getResources().getColor(R.color.white));
                                break;
                            case 3:
                                tvGameStatusTip.setText("YOU WON!");
                                tvYouWinQlc.setText(dataBean.getBetRecord().getRewardAmount() + " QLC");
                                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_wins));
                                tvBetAway.setTextColor(getResources().getColor(R.color.white));
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            //比赛没有结束
            if (dataBean.getBetRecord() == null) {
                //我没有下注
                llYourWin.setVisibility(View.GONE);
            } else {
                //我已经下注
                tvYouWinQlc.setText("0 QLC");
                tvGameStatusTip.setText("YOUR BET IS ACTIVIE!");
                cardviewBetStatus.setVisibility(View.VISIBLE);
                ivBetHome.setEnabled(false);
                ivBetTie.setEnabled(false);
                ivBetAway.setEnabled(false);
                tvExplain.setVisibility(View.GONE);
                btBet.setVisibility(View.GONE);
                switch (dataBean.getBetRecord().getBettingValue()) {
                    case 1:
                        ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_333));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                        break;
                    case 2:
                        ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_333));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                        break;
                    case 3:
                        ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                        tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                        tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                        tvAwayWins.setTextColor(getResources().getColor(R.color.color_333));
                        break;
                    default:
                        break;
                }
            }
        }

        float totalQlc = dataBean.getHomeBetsAmount() + dataBean.getAwayBetsAmount() + dataBean.getTiedBetsAmount();
        tvTotalQlc.setText(totalQlc + " QLC");
        tvHomeValue.setText(dataBean.getHomeBetsAmount() + " QLC");
        tvTieValue.setText(dataBean.getTiedBetsAmount() + " QLC");
        tvAwayValue.setText(dataBean.getAwayBetsAmount() + " QLC");

    }

    private String parseTime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        String result = "";
        try {
            date = sdr.parse("2018-" + time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            result += parseWeek(week) + " ";
            result += (calendar.get(Calendar.DAY_OF_MONTH)) + " ";
            result += parseMonth(calendar.get(Calendar.MONTH)) + " ";
            result += time.substring(6, 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseTimeDialog(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        String result = "";
        try {
            date = sdr.parse("2018-" + time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            result += parseWeek(week) + " ";
            result += (calendar.get(Calendar.DAY_OF_MONTH)) + " ";
            result += parseMonth(calendar.get(Calendar.MONTH)) + " 2018 at ";
            result += time.substring(6, 11) + "?";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerWordCupBetComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .wordCupBetModule(new WordCupBetModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WordCupBetContract.WordCupBetContractPresenter presenter) {
        mPresenter = (WordCupBetPresenter) presenter;
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
    public void onBetSuccess(RaceTimes.DataBean betRecordBean) {
        dataBean = betRecordBean;
        initData();
        closeProgressDialog();
        btBet.setVisibility(View.GONE);
        tvExplain.setVisibility(View.GONE);
        cardviewBetStatus.setVisibility(View.VISIBLE);
        ivBetHome.setEnabled(false);
        ivBetTie.setEnabled(false);
        ivBetAway.setEnabled(false);
        EventBus.getDefault().post(betRecordBean);
        tvGameStatusTip.setText("YOUR BET IS ACTIVIE!");
        llYourWin.setVisibility(View.VISIBLE);
        tvYouWinQlc.setText("0 QLC");
    }

    @Override
    public void onGetBalancelSuccess(Balance balance) {
        mBalance = balance;
    }

    private String parseWeek(int index) {
        KLog.i(index);
        switch (index) {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            case 1:
                return "Sunday";
            default:
                return "";
        }
    }

    private String parseMonth(int index) {
        switch (index) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "";
        }
    }

    @OnClick({R.id.iv_bet_home, R.id.iv_bet_tie, R.id.iv_bet_away, R.id.bt_bet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bet_home:
                if (dataBean.getIsBegin() == 1) {
                    return;
                }
                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                tvHomeWins.setTextColor(getResources().getColor(R.color.color_333));
                tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                betIndex = 1;
                setBetButtonEnable();
                break;
            case R.id.iv_bet_tie:
                if (dataBean.getIsBegin() == 1) {
                    return;
                }
                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                tvTieGame.setTextColor(getResources().getColor(R.color.color_333));
                tvAwayWins.setTextColor(getResources().getColor(R.color.color_999));
                betIndex = 2;
                setBetButtonEnable();
                break;
            case R.id.iv_bet_away:
                if (dataBean.getIsBegin() == 1) {
                    return;
                }
                ivBetHome.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                ivBetTie.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse_two));
                ivBetAway.setImageDrawable(getResources().getDrawable(R.mipmap.icon_ellipse));
                tvHomeWins.setTextColor(getResources().getColor(R.color.color_999));
                tvTieGame.setTextColor(getResources().getColor(R.color.color_999));
                tvAwayWins.setTextColor(getResources().getColor(R.color.color_333));
                betIndex = 3;
                setBetButtonEnable();
                break;
            case R.id.bt_bet:
                if (betIndex == -1) {
                    return;
                }
                if (mBalance == null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("address", wallet.getAddress());
                    mPresenter.getBalance(map);
                    ToastUtil.displayShortToast(getString(R.string.please_wait));
                    return;
                }
                if (mBalance.getData().getGAS() < 0.01 || Double.parseDouble(mBalance.getData().getQLC() + "") < 10) {
                    ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                    return;
                }
                showBetDialog();
            default:
                break;
        }
    }

    private void setBetButtonEnable() {
        btBet.setBackground(getResources().getDrawable(R.drawable.main_color_bt_bg));
    }

    private void showBetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.bet_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        String betWin = "home wins";
        switch (betIndex) {
            case 1:
                betWin = "home wins";
                break;
            case 2:
                betWin = "tie game";
                break;
            case 3:
                betWin = "away wins";
            default:
                break;
        }
        String betContry = "France vs Australia";
        String betTime = parseTimeDialog(dataBean.getRaceTime());
        tvContent.setText(Html.fromHtml(getResources().getString(R.string.bet_confirm, betWin, betContry, betTime)));
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
                showProgressDialog();
                TransactionApi.getInstance().getTransactionHex(wallet.getAddress(), ConstantValue.mainAddress, "10", new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        Map<String, Object> infoMap = new HashMap<>();
                        infoMap.put("raceKey", dataBean.getRacekey());
                        infoMap.put("address", wallet.getAddress());
                        infoMap.put("qlcAmount", 10);
                        infoMap.put("bettingType", betIndex);
                        infoMap.put("hex", txid);
                        mPresenter.betRace(infoMap);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        dialog.show();
    }


}
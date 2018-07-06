package com.stratagile.qlink.ui.adapter.wordcup;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.utils.QlcCountUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description  adapter of GameFragment
 * @date 2018/06/11 16:31:05
 */

public class GameListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILDREN = 1;

    /**
     * 不能再下注的时间间隔
     */
    public long timeRate = 1000 * 60 * 30;

    public String getCurrentServerTime() {
        return currentServerTime;
    }

    public void setCurrentServerTime(String currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

    /**
     * 当前服务器的时间
     */
    private String currentServerTime = "";

    public GameListAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_PARENT, R.layout.item_game_list_parent);
        addItemType(TYPE_CHILDREN, R.layout.item_game_list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultiItemEntity item) {
        switch (baseViewHolder.getItemViewType()) {
            case TYPE_PARENT:
                RaceTimes.RaceBeginTime beginTime = (RaceTimes.RaceBeginTime) item;
                baseViewHolder.setText(R.id.tv_time, parseTime(beginTime.getBeginTime()));
                break;
            case TYPE_CHILDREN:
                RaceTimes.DataBean dataBean = (RaceTimes.DataBean) item;
                baseViewHolder.setText(R.id.tv_group, dataBean.getRaceType())
                        .setText(R.id.tv_time, dataBean.getRaceTime().substring(6, 11))
                        .setText(R.id.tv_home_country, dataBean.getHomeCountryNumber())
                        .setText(R.id.tv_away_country, dataBean.getAwayCountryNumber());
                baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.wordcup_back_bg)
                        .setBackgroundRes(R.id.tv_bet_away, R.drawable.wordcup_back_bg)
                        .setBackgroundRes(R.id.tv_bet_tie, R.drawable.wordcup_back_bg);
                ImageView homeCountry = baseViewHolder.getView(R.id.iv_home_country);
                ImageView awayCountry = baseViewHolder.getView(R.id.iv_away_country);
                Glide.with(mContext)
                        .load(mContext.getResources().getIdentifier("icon_" + dataBean.getHomeCountryNumber().toLowerCase(Locale.ENGLISH), "mipmap", mContext.getPackageName()))
                        .into(homeCountry);
                Glide.with(mContext)
                        .load(mContext.getResources().getIdentifier("icon_" + dataBean.getAwayCountryNumber().toLowerCase(Locale.ENGLISH), "mipmap", mContext.getPackageName()))
                        .into(awayCountry);
                CardView cardView = baseViewHolder.getView(R.id.cardView);
                if (dataBean.getIsBegin() == 1) {
                    //比赛结束
                    cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.cannotbet));
                    baseViewHolder.setGone(R.id.iv_win_or_lost, true);
                    if (dataBean.getBetRecord() == null) {
                        //我没有下过注
                        baseViewHolder.setVisible(R.id.ll_bet, false);
                    } else {
                        //我下过注
                        baseViewHolder.setVisible(R.id.ll_bet, true);
                        switch (dataBean.getRaceResult()) {
                            case 1:
                                if (dataBean.getBetRecord().getBettingValue() == 1) {
                                    //我赢了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.word_cup_green_bg);
                                    if (dataBean.getBetRecord().getRewardAmount() == 0) {
                                        baseViewHolder.setGone(R.id.iv_win_or_lost, false);
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Pool: ");
                                        float poolValue = dataBean.getHomeBetsAmount() + dataBean.getTiedBetsAmount() + dataBean.getAwayBetsAmount();
                                        baseViewHolder.setText(R.id.tv_qlc, QlcCountUtil.parseQlc(poolValue) + " QLC");
                                    } else {
                                        baseViewHolder.setText(R.id.tv_qlc, "+" + QlcCountUtil.parseQlc(dataBean.getBetRecord().getRewardAmount()) + " QLC");
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Won: ");
                                        baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_rise);
                                    }
                                } else {
                                    //我输了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.wordcup_lose_bg);
                                    baseViewHolder.setText(R.id.tv_qlc, "-10.0 QLC");
                                    baseViewHolder.setText(R.id.tv_win_or_lost, "Lost: ");
                                    baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_falling);
                                }
                                break;
                            case 2:
                                if (dataBean.getBetRecord().getBettingValue() == 2) {
                                    //我赢了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_tie, R.drawable.word_cup_green_bg);
                                    if (dataBean.getBetRecord().getRewardAmount() == 0) {
                                        baseViewHolder.setGone(R.id.iv_win_or_lost, false);
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Pool: ");
                                        float poolValue = dataBean.getHomeBetsAmount() + dataBean.getTiedBetsAmount() + dataBean.getAwayBetsAmount();
                                        baseViewHolder.setText(R.id.tv_qlc, QlcCountUtil.parseQlc(poolValue) + " QLC");
                                    } else {
                                        baseViewHolder.setText(R.id.tv_qlc, "+" + QlcCountUtil.parseQlc(dataBean.getBetRecord().getRewardAmount()) + " QLC");
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Won: ");
                                        baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_rise);
                                    }
                                } else {
                                    //我输了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_tie, R.drawable.wordcup_lose_bg);
                                    baseViewHolder.setText(R.id.tv_qlc, "-10.0 QLC");
                                    baseViewHolder.setText(R.id.tv_win_or_lost, "Lost: ");
                                    baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_falling);
                                }
                                break;
                            case 3:
                                if (dataBean.getBetRecord().getBettingValue() == 3) {
                                    //我赢了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_away, R.drawable.word_cup_green_bg);
                                    if (dataBean.getBetRecord().getRewardAmount() == 0) {
                                        baseViewHolder.setGone(R.id.iv_win_or_lost, false);
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Pool: ");
                                        float poolValue = dataBean.getHomeBetsAmount() + dataBean.getTiedBetsAmount() + dataBean.getAwayBetsAmount();
                                        baseViewHolder.setText(R.id.tv_qlc, QlcCountUtil.parseQlc(poolValue) + " QLC");
                                    } else {
                                        baseViewHolder.setText(R.id.tv_qlc, "+" + QlcCountUtil.parseQlc(dataBean.getBetRecord().getRewardAmount()) + " QLC");
                                        baseViewHolder.setText(R.id.tv_win_or_lost, "Won: ");
                                        baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_rise);
                                    }

                                } else {
                                    //我输了
                                    baseViewHolder.setBackgroundRes(R.id.tv_bet_away, R.drawable.wordcup_lose_bg);
                                    baseViewHolder.setText(R.id.tv_qlc, "-10.0 QLC");
                                    baseViewHolder.setText(R.id.tv_win_or_lost, "Lost: ");
                                    baseViewHolder.setImageResource(R.id.iv_win_or_lost, R.mipmap.bg_icon_falling);
                                }
                                break;
                            default:
                                break;
                        }

                    }
                } else {
                    //比赛没有结束
                    cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
                    baseViewHolder.setVisible(R.id.ll_bet, true);
                    baseViewHolder.setGone(R.id.iv_win_or_lost, false);
                    baseViewHolder.setText(R.id.tv_win_or_lost, "Pool: ");
                    float betAmount = dataBean.getTiedBetsAmount() + dataBean.getHomeBetsAmount() + dataBean.getAwayBetsAmount();
                    baseViewHolder.setText(R.id.tv_qlc, QlcCountUtil.parseQlc(betAmount) + " QLC");

                    if (dataBean.getBetRecord() == null) {
                        //我没有下过注
                    } else {
                        //我下过注
                        if (dataBean.getBetRecord().getBettingValue() == 1) {
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.word_cup_maincolor_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_tie, R.drawable.wordcup_back_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_away, R.drawable.wordcup_back_bg);
                        } else if (dataBean.getBetRecord().getBettingValue() == 2) {
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.wordcup_back_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_tie, R.drawable.word_cup_maincolor_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_away, R.drawable.wordcup_back_bg);
                        } else if (dataBean.getBetRecord().getBettingValue() == 3) {
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_home, R.drawable.wordcup_back_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_tie, R.drawable.wordcup_back_bg);
                            baseViewHolder.setBackgroundRes(R.id.tv_bet_away, R.drawable.word_cup_maincolor_bg);
                        }
                    }
                }
                break;
            default:
                break;
        }
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
            result += parseMonth(calendar.get(Calendar.MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isCanBet(String time) {
        SimpleDateFormat sysSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date sysDate;
        SimpleDateFormat raceSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date raceDate;
        try {
            sysDate = sysSdf.parse(currentServerTime);
            Calendar sysCalendar = Calendar.getInstance();
            sysCalendar.setTime(sysDate);
            raceDate = raceSdf.parse("2018-" + time);
            Calendar raceCalendar = Calendar.getInstance();
            raceCalendar.setTime(raceDate);
            if (sysCalendar.getTimeInMillis() - raceCalendar.getTimeInMillis() > timeRate) {
                //超过30分钟，不能再下注
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
}
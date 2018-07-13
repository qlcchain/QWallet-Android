package com.stratagile.qlink.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.entity.eventbus.SelectCountry;
import com.stratagile.qlink.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by huzhipeng on 2018/2/3.
 */

public class DownCheckView extends LinearLayout {

    private View mask;
    private RecyclerView recyclerView;
    private LinearLayout llController;
    private TextView tvSelect;
    private ImageView ivShow;
    private boolean isShow;
    private boolean animationEnd;
    private DownCheckAdapter downCheckAdapter;
    private long intervalTime = 250;
    private ImageView ivChooseCountry;

    public OnItemCheckListener getOnItemCheckListener() {
        return onItemCheckListener;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    private OnItemCheckListener onItemCheckListener;

    public DownCheckView(Context context) {
        super(context);
        init();
    }

    public DownCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownCheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.down_check_layout, this, true);
        mask = view.findViewById(R.id.mask);
        recyclerView = view.findViewById(R.id.recyclerView);
        llController = view.findViewById(R.id.ll_controller);
        tvSelect = view.findViewById(R.id.select);
        ivShow = view.findViewById(R.id.iv_show);
        ivChooseCountry = view.findViewById(R.id.iv_country);
        isShow = false;
        animationEnd = true;
        tvSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.i("点击");
                if (!animationEnd) {
                    return;
                }
                animationEnd = false;
                if (isShow) {
                    isShow = false;
                    mask.setVisibility(GONE);
                    recyclerView.setVisibility(INVISIBLE);
                    imageOff();
                    recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
                    mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
                    handler.sendEmptyMessageDelayed(0, intervalTime);
                } else {
                    isShow = true;
                    mask.setVisibility(VISIBLE);
                    recyclerView.setVisibility(VISIBLE);
                    imageOn();
                    recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_to_center));
                    mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_in));
                    handler.sendEmptyMessageDelayed(1, intervalTime);
                }
            }
        });
        mask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animationEnd) {
                    return;
                }
                if (!isShow) {
                    return;
                }
                animationEnd = false;
                isShow = false;
                mask.setVisibility(GONE);
                recyclerView.setVisibility(INVISIBLE);
                imageOff();
                recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
                mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
                handler.sendEmptyMessageDelayed(0, intervalTime);
            }
        });
    }

    public void close() {
        if (!isShow) {
            return;
        }
        animationEnd = false;
        isShow = false;
        mask.setVisibility(GONE);
        recyclerView.setVisibility(INVISIBLE);
        imageOff();
        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
        mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
        handler.sendEmptyMessageDelayed(0, intervalTime);
    }

    private void imageOn() {
        RotateAnimation animation = new RotateAnimation(0f, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        ivShow.startAnimation(animation);//开始动画
    }

    private void imageOff() {
        Animation animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        ivShow.startAnimation(animation);//开始动画
    }

    public void setData(ArrayList<ContinentAndCountry.ContinentBean.CountryBean> contentList) {
        downCheckAdapter = new DownCheckAdapter(contentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(downCheckAdapter);
        tvSelect.setText(contentList.get(0).getName());
        downCheckAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mask.performClick();
                downCheckAdapter.setCurrentSelectItem(position);
                downCheckAdapter.notifyDataSetChanged();
                if (onItemCheckListener != null) {
                    Message msg = new Message();
                    msg.arg1 = position;
                    msg.what = 2;
                    handler.sendMessageDelayed(msg, intervalTime);
                }
            }
        });
        downCheckAdapter.setOnItemChangeListener(new DownCheckAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int position) {
                close();
            }

            @Override
            public void onItemSelect(ContinentAndCountry.ContinentBean.CountryBean item) {
                setText(item);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    animationEnd = true;
                    break;
                case 1:
                    animationEnd = true;
                    break;
                case 2:
                    setText(downCheckAdapter.getItem(msg.arg1));
                    onItemCheckListener.onItemCheck(downCheckAdapter.getItem(msg.arg1));
                    mask.performClick();
                    break;
                case 3:
                    EventBus.getDefault().post(msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public interface OnItemCheckListener {
        void onItemCheck(ContinentAndCountry.ContinentBean.CountryBean item);
    }

    public void setText(ContinentAndCountry.ContinentBean.CountryBean item) {
        if (!item.getName().equals(getContext().getString(R.string.choose_location))) {
            Message msg = new Message();
            msg.obj = item;
            msg.what = 3;
            handler.sendMessageDelayed(msg, 600);
        }
        tvSelect.setText(item.getName());
        Glide.with(getContext())
                .load(getContext().getResources().getIdentifier(item.getCountryImage(), "mipmap", getContext().getPackageName()))
                .into(ivChooseCountry);
    }


}

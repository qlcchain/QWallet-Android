package com.stratagile.qlink.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.animation.DynamicAnimation;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.ui.activity.wallet.AllWalletTokenActivity;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.utils.SpringAnimationUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by huzhipeng on 2018/2/3.
 */

public class SelectWalletView extends LinearLayout {

    private View mask;
    private RecyclerView recyclerView;
    private LinearLayout llController;
    private TextView tvSelect;
    private ImageView ivShow;
    private boolean isShow;
    private boolean animationEnd;
    private SelectWalletAdapter downCheckAdapter;
    private LinearLayout frameLayout;
    private long intervalTime = 250;
    private TextView createWallet;

    public OnItemCheckListener getOnItemCheckListener() {
        return onItemCheckListener;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    private OnItemCheckListener onItemCheckListener;

    public SelectWalletView(Context context) {
        super(context);
        init();
    }

    public SelectWalletView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectWalletView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCurrentSelectItem(int position) {
        downCheckAdapter.setCurrentSelectItem(position);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_wallet_layout, this, true);
        mask = view.findViewById(R.id.mask);
        recyclerView = view.findViewById(R.id.recyclerView);
        llController = view.findViewById(R.id.ll_controller);
        tvSelect = view.findViewById(R.id.select);
        ivShow = view.findViewById(R.id.iv_show);
        frameLayout = view.findViewById(R.id.framelayout);
        createWallet = view.findViewById(R.id.tvCreateWallet);
        isShow = false;
        animationEnd = true;
        llController.setOnClickListener(new OnClickListener() {
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
                    frameLayout.setVisibility(INVISIBLE);
                    imageOff();
                    frameLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
                    mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
                    handler.sendEmptyMessageDelayed(0, intervalTime);
                } else {
                    isShow = true;
                    mask.setVisibility(VISIBLE);
                    frameLayout.setVisibility(VISIBLE);
                    imageOn();
                    frameLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_to_center));
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
                frameLayout.setVisibility(INVISIBLE);
                imageOff();
                frameLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
                mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
                handler.sendEmptyMessageDelayed(0, intervalTime);
            }
        });
        createWallet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).startActivityForResult(new Intent((getContext()), SelectWalletTypeActivity.class), AllWalletTokenActivity.CREATE_WALLLET);
                close();
            }
        });
    }

    public boolean isShow() {
        return isShow;
    }

    public void close() {
        if (!isShow) {
            return;
        }
        animationEnd = false;
        isShow = false;
        mask.setVisibility(GONE);
        frameLayout.setVisibility(INVISIBLE);
        imageOff();
        frameLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
        mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
        handler.sendEmptyMessageDelayed(0, intervalTime);
    }

    private void imageOn() {
        SpringAnimationUtil.startRotatoSpringViewAnimation(ivShow, new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });
//        RotateAnimation animation = new RotateAnimation(0f, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(200);
//        animation.setRepeatCount(0);//动画的重复次数
//        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
//        ivShow.startAnimation(animation);//开始动画
    }

    private void imageOff() {
        SpringAnimationUtil.endRotatoSpringViewAnimation(ivShow, new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });
//        Animation animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(200);
//        animation.setRepeatCount(0);//动画的重复次数
//        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
//        ivShow.startAnimation(animation);//开始动画
    }

    public void setData(ArrayList<AllWallet> contentList, AllWallet allWallet) {
        downCheckAdapter = new SelectWalletAdapter(contentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(downCheckAdapter);
        tvSelect.setText(contentList.get(0).getWalletName());
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
        downCheckAdapter.setOnItemChangeListener(new SelectWalletAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int position) {
                close();
            }

            @Override
            public void onItemSelect(AllWallet item) {
                setText(item);
            }
        });
        for (int i = 0; i < contentList.size(); i++) {
            if (contentList.get(i) == allWallet) {
                downCheckAdapter.setCurrentSelectItem(i);
                setText(contentList.get(i));
                onItemCheckListener.onItemCheck(contentList.get(i));
            }
        }
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
        void onItemCheck(AllWallet item);
    }

    public void setText(AllWallet item) {
        tvSelect.setText(item.getWalletName());
    }


}

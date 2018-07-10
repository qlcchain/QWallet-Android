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
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.utils.UIUtils;

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
                    ivShow.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_choose));
                    recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
                    mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
                    handler.sendEmptyMessageDelayed(0, intervalTime);
                } else {
                    isShow = true;
                    mask.setVisibility(VISIBLE);
                    recyclerView.setVisibility(VISIBLE);
                    ivShow.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_choice));
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
                ivShow.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_max));
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
        ivShow.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.icon_max));
        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_center_to_top));
        mask.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.activity_fade_out));
        handler.sendEmptyMessageDelayed(0, intervalTime);
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
                setText(item.getName());
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
                    tvSelect.setText(downCheckAdapter.getItem(msg.arg1).getName());
                    onItemCheckListener.OnItemCheck(msg.arg1);
                    mask.performClick();
                    break;
                default:
                    break;
            }
        }
    };

    public interface OnItemCheckListener {
        void OnItemCheck(int position);
    }

    public void setText(String country) {
        tvSelect.setText(country);
    }


}

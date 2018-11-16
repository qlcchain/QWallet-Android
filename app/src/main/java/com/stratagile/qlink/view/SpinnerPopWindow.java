package com.stratagile.qlink.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;

import java.util.List;

public class SpinnerPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    private List<T> list;
    private MyAdapter mAdapter;
    private CardView cardView;

    public SpinnerPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.list = list;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener) {
        View view = inflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x4c29282a));
        mListView = view.findViewById(R.id.listview);
        cardView = view.findViewById(R.id.cardView);
        mListView.setAdapter(mAdapter = new MyAdapter());
        mListView.setOnItemClickListener(clickListener);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        cardView.setAnimation(AnimationUtils.loadAnimation(cardView.getContext(), R.anim.anim_top_to_center));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        cardView.setAnimation(AnimationUtils.loadAnimation(cardView.getContext(), R.anim.anim_center_to_top));
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spiner_item_layout, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position).toString());
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvName;
    }
}

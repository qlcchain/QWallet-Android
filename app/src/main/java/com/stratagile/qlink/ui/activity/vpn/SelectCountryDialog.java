package com.stratagile.qlink.ui.activity.vpn;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.ui.adapter.vpn.ContactCityAdapter;
import com.stratagile.qlink.utils.FileUtil;

/**
 * Created by zl on 2018/7/9.
 */

public class SelectCountryDialog {

    public static SelectCountryDialog instance;

    private ContactCityAdapter mAdapterContactCity;
    private ContinentAndCountry.ContinentBean continentChose;
    private AlertDialog builderTips;
    private SelectCountryDialog.SelectDelegate mSelectDelegate;

    public static synchronized SelectCountryDialog getInstance() {
        if (null == instance) {
            instance = new SelectCountryDialog();
        }
        return instance;
    }
    /**
     * 创建UI
     * @param context 上下文
     * @return
     */
    public SelectCountryDialog createDialog(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.activity_select_country_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);//取消按钮
        btn_cancel.setText(R.string.back_btn);

        Button btn_comfirm = (Button) view.findViewById(R.id.bt_continue);//确定按钮
        btn_comfirm.setText(R.string.cancel_btn_dialog);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSelView);
        btn_cancel.setVisibility(View.GONE);
        btn_comfirm.setVisibility(View.GONE);
        builder.setCancelable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Gson gson = new Gson();
        ContinentAndCountry continentAndCountry = gson.fromJson(FileUtil.getJson(context, "ContinentAndCountryDialogBean.json"), ContinentAndCountry.class);
        for (int i = 0; i < continentAndCountry.getContinent().size(); i++) {
            KLog.i("设置适配器。。。");
            //Collections.sort(continentAndCountry.getContinent().get(i).getCountry());
            continentChose = continentAndCountry.getContinent().get(i);
            mAdapterContactCity = new ContactCityAdapter(continentAndCountry.getContinent().get(i).getCountry());
            mAdapterContactCity.setSelectItem(-1);
            recyclerView.setAdapter(mAdapterContactCity);
            mAdapterContactCity.setOnItemChangeListener(new ContactCityAdapter.OnItemChangeListener() {
                @Override
                public void onItemChange(int position) {
                    if (mSelectDelegate != null) {
                        mSelectDelegate.onSelected(mAdapterContactCity.getItem(position).getName());
                    }
                }
            });
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderTips.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderTips.dismiss();
            }
        });
        builderTips = builder.create();
        builderTips.setCanceledOnTouchOutside(true);
        builderTips.show();
        return this;
    }

    /**
     * 设置回调函数接口
     */
    public interface SelectDelegate {
        void onSelected(String country);
    }
    /**
     * 设置回调函数
     * @param pSelectDelegate 回调函数
     * @return
     */
    public SelectCountryDialog setSelectDelegate(SelectDelegate pSelectDelegate) {
        this.mSelectDelegate=pSelectDelegate;
        return this;
    }
}

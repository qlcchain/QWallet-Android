package com.stratagile.qlink.utils

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.eos.TransferEthToCreateEos
import com.stratagile.qlink.view.BottomSheet
import com.stratagile.qlink.view.CustomPopWindow

import java.util.ArrayList


/**
 * 作者：hu on 2017/6/8
 * 邮箱：365941593@qq.com
 * 描述：
 */

/**
 * 公共的popwindow弹出类。所有的popwindow都可以封装在这个类里边
 */
object PopWindowUtil {


    /**
     * @param activity 上下文
     * @param showView 从activity中传进来的view,用于让popWindow附着的
     */
    @JvmOverloads
    fun showPopWindow(activity: Activity, showView: View, clickListener: View.OnClickListener, tipContentStr: String, comfirmContent: String = "", cancalContent: String = "") {
        //        View maskView = LayoutInflater.from(activity).inflate(R.layout.confirm_cancal_layout, null);
        //        View contentView = maskView.findViewById(R.id.ll_popup);
        //        maskView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.open_fade));
        //        contentView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.pop_manage_product_in));
        //        //对具体的view的事件的处理
        //        TextView confirm = (TextView) maskView.findViewById(R.id.bt_confirm);
        //        TextView cancal = (TextView) maskView.findViewById(R.id.bt_cancal);
        //        TextView tipContent = (TextView) maskView.findViewById(R.id.tip_content);
        //        if (!"".equals(comfirmContent)) {
        //            confirm.setText(comfirmContent);
        //        }
        //        if (!"".equals(cancalContent)) {
        //            cancal.setText(cancalContent);
        //        }
        //        tipContent.setText(tipContentStr);
        //        maskView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                CustomPopWindow.onBackPressed();
        //            }
        //        });
        //        tipContent.setOnClickListener(clickListener);
        //        cancal.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                CustomPopWindow.onBackPressed();
        //            }
        //        });
        //
        //        confirm.setOnClickListener(clickListener);
        //
        //        new CustomPopWindow.PopupWindowBuilder(activity)
        //                .setView(maskView)
        //                .setClippingEnable(false)
        //                .setContenView(contentView)
        //                .setFocusable(false)
        //                .size(UIUtils.getDisplayWidth(activity), UIUtils.getDisplayHeigh(activity))
        //                .create()
        //                .showAtLocation(showView, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * @param activity 上下文
     * @param showView 从activity中传进来的view,用于让popWindow附着的
     */
    fun showSharePopWindow(activity: Activity, showView: View, arrayList: MutableList<String>, onItemSelectListener: OnItemSelectListener) {
        val maskView = LayoutInflater.from(activity).inflate(R.layout.share_pop_layout, null)
        val builder = BottomSheet.Builder(activity)
        builder.setApplyTopPadding(false)

        builder.setCustomView(maskView)
        builder.create().setOnDismissListener {
            onItemSelectListener.onSelect("")
        }

        val contentView = maskView.findViewById<View>(R.id.ll_popup)
        val bt_cancal = maskView.findViewById<View>(R.id.bt_cancal)
//        maskView.animation = AnimationUtils.loadAnimation(activity, R.anim.open_fade)
//        contentView.animation = AnimationUtils.loadAnimation(activity, R.anim.pop_manage_product_in)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        var sendTokenAdapter = SendTokenAdapter(arrayList)
        recyclerView.adapter = sendTokenAdapter
        //对具体的view的事件的处理
        sendTokenAdapter.setOnItemClickListener { adapter, view, position ->
            onItemSelectListener.onSelect(arrayList[position])
            builder.create().dismiss()
        }
//        CustomPopWindow.PopupWindowBuilder(activity)
//                .setView(maskView)
//                .setClippingEnable(false)
//                .setContenView(contentView)
//                .setFocusable(false)
//                .size(UIUtils.getDisplayWidth(activity), UIUtils.getDisplayHeigh(activity))
//                .create()
//                .showAtLocation(showView, Gravity.NO_GRAVITY, 0, 0)

        bt_cancal.setOnClickListener {
            builder.create().dismiss()
        }
        builder.create().show()
    }

    fun showTransferEthPopWindow(transferEthToCreateEos: TransferEthToCreateEos, activity: Activity, showView: View, onClickListener: View.OnClickListener) {
        val maskView = LayoutInflater.from(activity).inflate(R.layout.transfer_eth_to_create_eos_layout, null)
        val contentView = maskView.findViewById<View>(R.id.ll_popup)
        maskView.animation = AnimationUtils.loadAnimation(activity, R.anim.open_fade)
        contentView.animation = AnimationUtils.loadAnimation(activity, R.anim.pop_manage_product_in)

        var tvTo = maskView.findViewById<TextView>(R.id.tvTo)
        var tvFrom = maskView.findViewById<TextView>(R.id.tvFrom)
        var tvCost = maskView.findViewById<TextView>(R.id.tvCost)
        var tvEthValue = maskView.findViewById<TextView>(R.id.tvEthValue)
        var tvCostDetail = maskView.findViewById<TextView>(R.id.tvCostDetail)
        var tvNext = maskView.findViewById<TextView>(R.id.tvNext)
        val ivClose = maskView.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            CustomPopWindow.onBackPressed()
        }
        tvNext.setOnClickListener(onClickListener)
        tvTo.text = transferEthToCreateEos.to
        tvFrom.text = transferEthToCreateEos.from
        tvCost.text = "" + transferEthToCreateEos.cost + " ether"
        tvCostDetail.text = transferEthToCreateEos.costDetail
        tvEthValue.text = "" + transferEthToCreateEos.ethValue + " ETH"

        //对具体的view的事件的处理
        CustomPopWindow.PopupWindowBuilder(activity)
                .setView(maskView)
                .setClippingEnable(false)
                .setContenView(contentView)
                .setFocusable(false)
                .size(UIUtils.getDisplayWidth(activity), UIUtils.getDisplayHeigh(activity))
                .create()
                .showAtLocation(showView, Gravity.NO_GRAVITY, 0, 0)
        maskView.setOnClickListener {
            CustomPopWindow.onBackPressed()
        }
        contentView.setOnClickListener{

        }
    }

    interface OnItemSelectListener {
        fun onSelect(content : String)
    }
}

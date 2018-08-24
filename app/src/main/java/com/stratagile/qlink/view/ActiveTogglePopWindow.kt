package com.stratagile.qlink.view


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout

import com.stratagile.qlink.R


class ActiveTogglePopWindow @SuppressLint("InflateParams")
constructor(context: Activity) : PopupWindow() {
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.popupwindow_active, null)

        // 设置SelectPicPopupWindow的View
        contentView = content
        // 设置SelectPicPopupWindow弹出窗体的宽
        width = LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体的高
        height = LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体可点击
        isFocusable = true
        isOutsideTouchable = true
        // 刷新状态
        update()
        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(0)
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(dw)

        // 设置SelectPicPopupWindow弹出窗体动画效果
        animationStyle = R.style.AnimationPreview


        val rlAll = content.findViewById<View>(R.id.rl_detail) as LinearLayout
        val rlGain = content.findViewById<View>(R.id.rl_rank) as LinearLayout
        rlAll.setOnClickListener { v ->
            dismiss()
            mOnItemClickListener?.onItemClick(v.id)
        }
        rlGain.setOnClickListener { v ->
            dismiss()
            mOnItemClickListener?.onItemClick(v.id)
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    fun showPopupWindow(parent: View) {
        if (!isShowing) {
            // 以下拉方式显示popupwindow
            showAsDropDown(parent, -parent.context.resources.getDimension(R.dimen.x180).toInt(), parent.context.resources.getDimension(R.dimen.x25).toInt())
        } else {
            dismiss()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(@IdRes id: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }
}

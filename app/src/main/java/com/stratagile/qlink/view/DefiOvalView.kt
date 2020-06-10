package com.stratagile.qlink.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.stratagile.qlink.R
import com.stratagile.qlink.utils.CubicBezierInterpolator
import com.stratagile.qlink.utils.UIUtils

class DefiOvalView : View {
    private var mPaint: Paint? = null
    private var strokeWidth = 0f
    private var startAngle = 0f
    private var sweepAngle = 0f
    private var ovalColor = 0
    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width = widthSize - paddingLeft - paddingRight
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        /**模式 */
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        /**长度大小 */
        val height = heightSize - paddingTop - paddingBottom
        /**去掉上下的padding */

//        if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY){
//            height = (int) (width / ratio + 0.5f);/**修正一下 高度的值 让高度=宽度/比例,+0.5f是确保四舍五入*/
//        }else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
//            /**由于高度是精确的值 ,宽度随着高度的变化而变化*/
//            width = (int) ((height * ratio) + 0.5f);
//        }
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun init(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.DefiOvalView, 0, 0)
        strokeWidth = typedArray.getInteger(R.styleable.DefiOvalView_strokeWidthOval, 2).toFloat()
        startAngle = typedArray.getFloat(R.styleable.DefiOvalView_startAngle, 0.0f)
        sweepAngle = typedArray.getFloat(R.styleable.DefiOvalView_sweepAngle, 0.0f)
        ovalColor = typedArray.getColor(R.styleable.DefiOvalView_ovalColor, context.resources.getColor(R.color.color_f50f60))
        typedArray.recycle()
    }

    fun setSweepAngle(sweepAngle : Float) {
        this.sweepAngle = sweepAngle
        startAnimation(sweepAngle)
    }

    fun startAnimation(value : Float) {
        var valueAnimator = ValueAnimator.ofFloat(value)
        valueAnimator.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT
        valueAnimator.duration = 500
        valueAnimator.addUpdateListener {
            var value = it.animatedValue as Float
            sweepAngle = value
            invalidate()
        }
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        /**
         * 这里我是偷懒了，建议不要在onDraw()方法里初始化对象
         */
        mPaint = Paint()
        mPaint!!.isAntiAlias = true //取消锯齿
        mPaint!!.style = Paint.Style.STROKE //设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaint!!.strokeWidth = UIUtils.dip2px(strokeWidth, context).toFloat()
        mPaint!!.color = ovalColor
        /**
         * 这是一个居中的圆
         */
        val x = width / 2.toFloat()
        val y = height / 2.toFloat()
        val oval = RectF((0 + UIUtils.dip2px(strokeWidth, context) / 2).toFloat(), (0 + UIUtils.dip2px(strokeWidth, context) / 2).toFloat(), (width - UIUtils.dip2px(strokeWidth, context) / 2).toFloat(), (height - UIUtils.dip2px(strokeWidth, context) / 2).toFloat())
        canvas.drawArc(oval, startAngle, sweepAngle, false, mPaint) //画圆弧，这个时候，绘制没有经过圆心
        super.onDraw(canvas)
    }
}
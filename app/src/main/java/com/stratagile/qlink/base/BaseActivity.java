package com.stratagile.qlink.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.BuildConfig;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.statusbar.StatusBarCompat;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.ui.activity.setting.SelectLanguageActivityActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.FitRelativeLayout;
import com.stratagile.qlink.view.RxDialogLoading;
import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.ShowKeyRelativeLayout;

import java.util.Locale;

import timber.log.Timber;

/**
 * 作者：Android on 2017/8/1
 * 邮箱：365941593@qq.com
 * 描述：
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityDelegate {

    public Toolbar toolbar;
    public boolean needFront = false;   //toolBar 是否需要显示在最上层的标识
    public RelativeLayout rootLayout;
    public RelativeLayout relativeLayout_root;
    public TextView view;
    public TextView title;
    public int mainColor = 0;
    public int drawableBg = 0;
    public boolean isEditActivity = false;
    /**
     * 公共的加载进度弹窗
     */
    public RxDialogLoading progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setLanguage(false);
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.DebugTree());
        }
        //getWindow().setBackgroundDrawable(null);
        if (mainColor == 0) {
            mainColor = R.color.mainColor;
        }
        if (!isEditActivity) {
            StatusBarCompat.translucentStatusBar(this, true);
            StatusBarCompat.cancelLightStatusBar(this);
        }
        initToolbar();
        setupActivityComponent();
        initView();
        initData();
    }



    /**
     * 设置app的语言
     */
    public void setLanguage(boolean isUpdate) {

        int defaultLanguage = SpUtil.getInt(this, ConstantValue.Language, -1);
        if (defaultLanguage == -1) {
            Resources resources = getResources();
            // 获取应用内语言
            final Configuration configuration = resources.getConfiguration();
            if (configuration.locale == Locale.ENGLISH) {
                defaultLanguage = 0;
            } else if (configuration.locale == Locale.CHINESE){
                defaultLanguage = 1;
            } else {
                defaultLanguage = 0;
            }
            SpUtil.putInt(this, ConstantValue.Language, defaultLanguage);
        }

        //0，英文， 1中文
        Resources resources = getResources();
        // 获取应用内语言
        final Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            configuration.locale = Locale.ENGLISH;
            Locale.setDefault(Locale.ENGLISH);
            KLog.i("设置为英文");
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 1){
            configuration.locale = Locale.CHINESE;
            Locale.setDefault(Locale.CHINESE);
            KLog.i("设置为中文");
        } else if (SpUtil.getInt(this, ConstantValue.Language, -1) == 2) {
            configuration.setLocale(new Locale("in", "rID"));
            //Locale.setDefault(Locale.CHINESE);
            KLog.i("设置为印度尼西亚");
        }
        getResources().updateConfiguration(configuration, displayMetrics);
        if (isUpdate) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    /**
     * 启动Activity
     *
     * @param clazz
     */
    protected <T> void startActivity(Class<T> clazz) {
        Intent intent = new Intent(this, clazz);
        try {
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(this, getString(R.string.Coming_soon));
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        try {
//            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//                View v = getCurrentFocus();
//                if (isShouldHideInput(v, ev)) {
//
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    }
//                }
//                return super.dispatchTouchEvent(ev);
//            }
//            // 必不可少，否则所有的组件都不会有TouchEvent了
//            if (getWindow() != null && getWindow().superDispatchTouchEvent(ev)) {
//                return true;
//            }
//        }catch (Exception e)
//        {
//
//        }finally {
//            return onTouchEvent(ev);
//        }
//
//    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && ((v instanceof EditText) && !(v.getParent() instanceof ShowKeyRelativeLayout))) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        } else if (v != null && ((v instanceof EditText) && (v.getParent() instanceof ShowKeyRelativeLayout))) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right + (getResources().getDimension(R.dimen.x100)) && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        progressDialog = new RxDialogLoading(this);
        progressDialog.setmDialogColor(getResources().getColor(R.color.mainColor));
        progressDialog.setDialogText(getResources().getString(R.string.apploading));
        if (rootLayout == null) {
            return;
        }
        if (needFront) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_00000000));
            relativeLayout_root.setBackgroundColor(getResources().getColor(R.color.color_00000000));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rootLayout.addView(view, params);
            toolbar.bringToFront();
            toolbar.setVisibility(View.GONE);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.root_rl);
            rootLayout.addView(view, params);
        }
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        relativeLayout_root = (RelativeLayout) findViewById(R.id.root_rl);
        view = findViewById(R.id.view);
        view.setBackgroundColor(getResources().getColor(mainColor));
        if (drawableBg != 0) {
            view.setBackgroundResource(drawableBg);
        }
        toolbar.setBackgroundColor(getResources().getColor(mainColor));
        if (mainColor == R.color.white) {
            title.setTextColor(getResources().getColor(R.color.color_1F314A));
            toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_back_dark));
            StatusBarCompat.changeToLightStatusBar(this);
        } else {
            StatusBarCompat.cancelLightStatusBar(this);
        }
        if (drawableBg != 0) {
            toolbar.setBackgroundResource(drawableBg);
        }
        view.setLayoutParams(new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), (int) (UIUtils.getStatusBarHeight(this))));
//        if (!SpUtil.getBoolean(this, ConstantValue.isMainNet, false) && SpUtil.getBoolean(this, ConstantValue.showTestFlag, true)) {
//            view.setBackgroundColor(getResources().getColor(R.color.color_f51818));
//            view.setText(getString(R.string.testnet));
//        }
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.dip2px(getResources().getDimension(R.dimen.dp_69), this) - (UIUtils.getStatusBarHeight(this)));
//        toolbar.setLayoutParams(rlp);
        toolbar.setTitle("");
        relativeLayout_root.setLayoutParams(new FitRelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), (int) ((UIUtils.getStatusBarHeight(this)) + UIUtils.dip2px(42f, this))));
        if (toolbar != null && !needFront) {
            setSupportActionBar(toolbar);
        }
        if (isEditActivity) {
            relativeLayout_root.setLayoutParams(new FitRelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), (int) UIUtils.dip2px(42f, this)));
            view.setVisibility(View.GONE);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        }
    }

    @Override
    public void setTitle(CharSequence text) {
        title.setText(text.toString() + "  ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化dagger2
     */
    protected abstract void setupActivityComponent();


    @Override
    public void destoryContainer() {
        finish();
    }

    @Override
    public BaseActivity getContainerActivity() {
        return this;
    }


    @Override
    public boolean isContainerDead() {
        if (Build.VERSION.SDK_INT > 16) {
            return this.isDestroyed();
        } else {
            return this.isFinishing();
        }
    }

    public void setToorBar(boolean isVisitiy) {
        if (toolbar != null) {
            if (isVisitiy) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

}

package com.stratagile.qlink.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.stratagile.qlink.base.ActivityDelegate;

import java.util.Stack;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author zhaoyun
 * @desc 功能描述
 * @date 2016/7/26 18:01
 */
@Singleton
public class AppActivityManager {

    private final Application mApplication;

    private final Stack<ActivityDelegate> activityStack;

    @Inject
    public AppActivityManager(Application application){
        mApplication = application;
        activityStack = new Stack<>();
    }

    public Stack<ActivityDelegate> getAll() {
        return activityStack;
    }

    /**
     * 按照下标查找压入栈内的Activity实例
     *
     * @param index
     * @return
     */
    public ActivityDelegate findActivityByIndex(int index) {
        if (index < 0) {
            return null;
        }
        if (activityStack != null && activityStack.size() - 1 >= index) {
            return activityStack.get(index);
        } else {
            return null;
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(ActivityDelegate activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public ActivityDelegate currentActivity() {
        ActivityDelegate activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的，也就是我们在APP中现在所看到的）
     */
    public void finishActivity() {
        ActivityDelegate activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(ActivityDelegate activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isContainerDead()) {
                activity.destoryContainer();
            }
        }
    }

    /**
     * 当Activity被调用onDestory时从堆栈中去除
     *
     * @param activity
     */
    public void removeActivity(ActivityDelegate activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Stack<ActivityDelegate> tempActivityStack = new Stack<ActivityDelegate>();
        for (int i = (activityStack.size() - 1); i >= 0; i--) {
            if (activityStack.get(i).getClass().equals(cls)) {
                tempActivityStack.add(activityStack.get(i));
                break;
            }
        }
        if (!tempActivityStack.isEmpty()) {
            finishActivity(tempActivityStack.get(0));
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null && !activityStack.isEmpty()){
            for (int i = (activityStack.size() - 1); i >= 0; i--) {
                //栈内不为空，并没有在Activity栈中被销毁
                if (activityStack.get(i) != null && !activityStack.get(i).isContainerDead()) {
                    activityStack.get(i).destoryContainer();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 结束所有Activity除了指定Activity外
     */
    public void finishAllActivityWithoutSpeciality(Class<?> cls) {
        Stack<ActivityDelegate> tempActivityStack = new Stack<ActivityDelegate>();
        for (int i = (activityStack.size() - 1); i >= 0; i--) {
            //首先是要真是存在于Activity栈中的实例
            if (activityStack.get(i) != null && !activityStack.get(i).isContainerDead()) {
                //不同的，销毁
                if (!activityStack.get(i).getClass().equals(cls)) {
                    activityStack.get(i).destoryContainer();
                }
                //相同的，保留
                else {
                    tempActivityStack.add(activityStack.get(i));
                }
            }
        }
        activityStack.clear();
        activityStack.addAll(tempActivityStack);
    }

    /**
     * 结束所有Activity除了指定Activity外
     */
    public void finishAllActivityWithoutThis() {
        ActivityDelegate act = activityStack.lastElement();
        for (int i = (activityStack.size() - 1); i >= 0; i--) {
            if (activityStack.get(i) != null) {
                if (!act.equals(activityStack.get(i)) && !activityStack.get(i).isContainerDead()) {
                    activityStack.get(i).destoryContainer();
                }
            }
        }
        activityStack.clear();
        activityStack.add(act);
    }


    /**
     * 判断第index位置的Activity实例是不是cls类型的Activity
     *
     * @param cls
     * @param index
     * @return
     */
    public boolean isSpecialityActivity(int index, Class<?> cls) {
        boolean isActivity = false;
        if (index < 0 || activityStack.isEmpty() || index > (activityStack.size() - 1)) {
            return isActivity;
        }
        if (activityStack.get(index).getClass().equals(cls)) {
            isActivity = true;
        }
        return isActivity;
    }

    /**
     * 找出对应Activity实例在堆栈中的位置
     *
     * @param cls
     * @return
     */
    public int localeActivity(Class<?> cls) {
        int activityIndex = -1;
        if (activityStack == null || activityStack.isEmpty()) {
            return activityIndex;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                activityIndex = i;
                break;
            }
        }
        return activityIndex;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) mApplication
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(mApplication.getPackageName());
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

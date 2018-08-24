package com.stratagile.qlink.ui.activity.base;

/**
 * @author 基本控制器接口
 * @desc 功能描述
 * @date 2016/7/20 16:22
 */
public interface BasePresenter {

    void subscribe();

    //此方法不调用会造成内存泄漏
    void unsubscribe();

}

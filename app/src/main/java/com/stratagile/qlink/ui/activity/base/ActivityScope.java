package com.stratagile.qlink.ui.activity.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author 只有标明了@Scope ActivityScope注解的Component才能使用所dependency的Module中用@ActivityScope标注的方法
 * @desc 功能描述
 * 胡：这个注释相当于scope的作用，只是自己定义了一下，作用为在标注的范围内使用单例
 * @date 2016/7/21 9:33
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}

package com.release.wanandroid.event

import com.release.wanandroid.utils.SettingUtil

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())
package com.release.wanandroid.event

import com.release.wanandroid.utils.SettingUtil

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */

class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor())

class LoginEvent(var isLogin: Boolean)

class NetworkChangeEvent(var isConnected: Boolean)

class RefreshHomeEvent(var isRefresh: Boolean)

class RefreshTodoEvent(var isRefresh: Boolean, var type: Int)

class TodoEvent(var type: String, var curIndex: Int)

class TodoTypeEvent(var type: Int)


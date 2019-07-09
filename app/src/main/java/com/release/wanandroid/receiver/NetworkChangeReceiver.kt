package com.cxz.wanandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.event.NetworkChangeEvent
import com.release.wanandroid.utils.NetWorkUtil
import com.release.wanandroid.utils.Sp
import org.greenrobot.eventbus.EventBus

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by Sp(Constant.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}
package com.release.wanandroid.rx

import com.cxz.wanandroid.rx.scheduler.IoMainScheduler

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}
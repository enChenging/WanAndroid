package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface AddTodoContract {

    interface View : IView {

        fun showAddTodo(success: Boolean)

        fun showUpdateTodo(success: Boolean)

        fun getType(): Int
        fun getCurrentDate(): String
        fun getTitle(): String
        fun getContent(): String
        fun getStatus(): Int
        fun getItemId(): Int
        fun getPriority(): String

    }

    interface Presenter : IPresenter<View> {

        fun addTodo()

        fun updateTodo(id: Int)

    }

    interface Model : IModel {

        fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>>

        fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    }


}
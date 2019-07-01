package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.AllTodoResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.TodoResponseBody
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface TodoContract {

    interface View : IView {

        fun showNoTodoList(todoResponseBody: TodoResponseBody)

        fun showDeleteSuccess(success: Boolean)

        fun showUpdateSuccess(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun getAllTodoList(type: Int)

        fun getNoTodoList(page: Int, type: Int)

        fun getDoneList(page: Int, type: Int)

        fun deleteTodoById(id: Int)

        fun updateTodoById(id: Int, status: Int)

    }

    interface Model : IModel {

        fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>>

        fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>>

        fun deleteTodoById(id: Int): Observable<HttpResult<Any>>

        fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>>

    }

}
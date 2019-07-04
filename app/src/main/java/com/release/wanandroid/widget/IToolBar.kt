package com.release.wanandroid.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.release.wanandroid.R


/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */
class IToolBar constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    Toolbar(context, attrs, defStyleAttr) {

    private var toolBar: View? = null
    private var toolBar_layout: View? = null
    private var iv_back: ImageView? = null
    private var tv_title: TextView? = null
    private var tv_right: TextView? = null

    init {
        initView(context)
    }


    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.tool_bar_back, this, true)
        toolBar = view.findViewById(R.id.toolBar)
        toolBar_layout = view.findViewById(R.id.toolBar_layout)
        iv_back = view.findViewById(R.id.iv_back)
        tv_title = view.findViewById(R.id.tv_title)
        tv_right = view.findViewById(R.id.tv_right)
        setBackFinish()
    }

    fun setToolBarBackgroundColor(color: Int): IToolBar {
        toolBar?.background = resources.getDrawable(color)
        return this
    }

    fun setToolLayoutBackgroundColor(color: Int): IToolBar {
        toolBar_layout?.setBackgroundColor(resources.getColor(color))
        return this
    }

    fun setBackGone(): IToolBar {
        iv_back?.visibility = View.GONE
        return this
    }

    fun setBackDrawable(drawable: Drawable): IToolBar {
        iv_back?.setImageDrawable(drawable)
        return this
    }

    fun setBackDrawable(color: Int): IToolBar {
        iv_back?.setImageDrawable(resources.getDrawable(color))
        return this
    }

    fun setRight(right: String, clickListener: View.OnClickListener): IToolBar {
        tv_right?.visibility = View.VISIBLE
        tv_right?.text = right
        tv_right?.setOnClickListener(clickListener)
        return this
    }

    fun setRight(right: Int, clickListener: View.OnClickListener): IToolBar {
        tv_right?.visibility = View.VISIBLE
        tv_right?.setText(right)
        tv_right?.setOnClickListener(clickListener)
        return this
    }

    fun setRightGone(Visible: Int): IToolBar {
        tv_right?.visibility = Visible
        return this
    }


    fun setRightTextColor(color: Int): IToolBar {
        tv_right?.setTextColor(resources.getColor(color))
        return this
    }

    fun setRightSize(textSize: Int): IToolBar {
        tv_right?.textSize = textSize.toFloat()
        return this
    }

    fun setBackFinish(): IToolBar {
        iv_back?.setOnClickListener { (context as Activity).finish() }
        return this
    }


    fun setTitleText(title: String): IToolBar {
        tv_title?.text = title
        return this
    }

    fun setTitleText(textId: Int): IToolBar {
        tv_title?.setText(textId)
        return this
    }

    fun setTitleColor(color: Int): IToolBar {
        tv_title?.setTextColor(resources.getColor(color))
        return this
    }

    fun setTitleSize(textSize: Int): IToolBar {
        tv_title?.textSize = textSize.toFloat()
        return this
    }

}

package com.release.wanandroid.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.release.wanandroid.R;


/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */
public class IToolBar extends Toolbar {

    private View toolBar, toolBar_layout;
    private ImageView iv_back;
    private TextView tv_title, tv_right;

    public IToolBar(Context context) {
        this(context, null);
    }

    public IToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tool_bar_back, this, true);
        toolBar = view.findViewById(R.id.toolBar);
        toolBar_layout = view.findViewById(R.id.toolBar_layout);
        iv_back = view.findViewById(R.id.iv_back);
        tv_title = view.findViewById(R.id.tv_title);
        tv_right = view.findViewById(R.id.tv_right);

        setBackFinish();
    }

    public IToolBar setToolBarBackgroundColor(int color) {
        toolBar.setBackground(getResources().getDrawable(color));
        return this;
    }

    public IToolBar setToolLayoutBackgroundColor(int color) {
        toolBar_layout.setBackgroundColor(getResources().getColor(color));
        return this;
    }

    public IToolBar setBackGone() {
        iv_back.setVisibility(View.GONE);
        return this;
    }

    public IToolBar setBackDrawable(Drawable drawable) {
        iv_back.setImageDrawable(drawable);
        return this;
    }

    public IToolBar setBackDrawable(int color) {
        iv_back.setImageDrawable(getResources().getDrawable(color));
        return this;
    }

    public IToolBar setRight(String right, View.OnClickListener clickListener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(right);
        tv_right.setOnClickListener(clickListener);
        return this;
    }

    public IToolBar setRight(int right, View.OnClickListener clickListener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(right);
        tv_right.setOnClickListener(clickListener);
        return this;
    }

    public IToolBar setRightGone(int Visible) {
        tv_right.setVisibility(Visible);
        return this;
    }


    public IToolBar setRightTextColor(int color) {
        tv_right.setTextColor(getResources().getColor(color));
        return this;
    }

    public IToolBar setRightSize(int textSize) {
        tv_right.setTextSize((float) textSize);
        return this;
    }

    public IToolBar setBackFinish() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        return this;
    }


    public IToolBar setTitleText(String title) {
        tv_title.setText(title);
        return this;
    }

    public IToolBar setTitleText(int textId) {
        tv_title.setText(textId);
        return this;
    }

    public IToolBar setTitleColor(int color) {
        tv_title.setTextColor(getResources().getColor(color));
        return this;
    }

    public IToolBar setTitleSize(int textSize) {
        tv_title.setTextSize((float) textSize);
        return this;
    }

}

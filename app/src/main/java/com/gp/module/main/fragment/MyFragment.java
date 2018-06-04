package com.gp.module.main.fragment;

import android.os.Bundle;
import android.view.View;

import com.gp.R;
import com.gp.base.BaseFragment;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MyFragment extends BaseFragment {
    @Override
    protected int getContentView() {
        return R.layout._3frag;
    }

    public static MyFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}

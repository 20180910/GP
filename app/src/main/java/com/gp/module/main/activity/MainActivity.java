package com.gp.module.main.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.customview.MyRadioButton;
import com.gp.R;
import com.gp.base.BaseActivity;
import com.gp.module.main.fragment.HomeFragment;
import com.gp.module.main.fragment.MyFragment;
import com.gp.module.main.fragment.OrderTypeFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    HomeFragment homeFragment;
    OrderTypeFragment orderTypeFragment;
    MyFragment myFragment;

    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    @BindView(R.id.rb_home_tab1)
    MyRadioButton rb_home_tab1;

    @BindView(R.id.rb_home_tab2)
    MyRadioButton rb_home_tab2;

    @BindView(R.id.rb_home_tab3)
    MyRadioButton rb_home_tab3;
    private MyRadioButton selectView;


    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        homeFragment = new HomeFragment();
        addFragment(R.id.fl_content, homeFragment);

        setTabClickListener();

    }



    private void setTabClickListener() {
        selectView = rb_home_tab1;
        rb_home_tab1.setOnClickListener(getTabClickListener(1));
        rb_home_tab2.setOnClickListener(getTabClickListener(2));
        rb_home_tab3.setOnClickListener(getTabClickListener(3));

    }



    @NonNull
    private MyOnClickListener getTabClickListener(final int index) {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                switch (index) {
                    case 1:
                        selectHome();
                        break;
                    case 2:
                            selectOrder();
                        break;
                    case 3:
                            selectMy();
                        break;
                }
            }
        };
    }
    @Override
    protected void initRxBus() {
        super.initRxBus();


    }

    private void selectHome() {
        if (selectView == rb_home_tab1) {
            return;
        }
        selectView = rb_home_tab1;
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            addFragment(R.id.fl_content, homeFragment);
        } else {
            showFragment(homeFragment);
        }
        hideFragment(orderTypeFragment);
        hideFragment(myFragment);
    }

    private void selectOrder() {
        if (selectView == rb_home_tab2) {
            return;
        }
        selectView = rb_home_tab2;
        if (orderTypeFragment == null) {
            orderTypeFragment = new OrderTypeFragment();
            addFragment(R.id.fl_content, orderTypeFragment);
        } else {
            showFragment(orderTypeFragment);
        }
        hideFragment(homeFragment);
        hideFragment(myFragment);
    }

    private void selectMy() {
        if (selectView == rb_home_tab3) {
            return;
        }
        selectView = rb_home_tab3;
        if (myFragment == null) {
            myFragment = new MyFragment();
            addFragment(R.id.fl_content, myFragment);
        } else {
            showFragment(myFragment);
        }
        hideFragment(homeFragment);
        hideFragment(orderTypeFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    @Override
    protected void initData() {
    }









    protected void onViewClick(View v) {
        switch (v.getId()) {

        }
    }




    private long mExitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 1500) {
            showToastS("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}

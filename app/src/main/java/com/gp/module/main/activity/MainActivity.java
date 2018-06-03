package com.gp.module.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.customview.MyRadioButton;
import com.gp.Constant;
import com.gp.R;
import com.gp.base.BaseActivity;
import com.gp.module.main.fragment.HomeFragment;
import com.gp.module.main.fragment.MyFragment;
import com.gp.module.main.fragment.OrderTypeFragment;
import com.gp.tools.CopyFile;
import com.gp.tools.StreamUtils;

import java.util.ArrayList;
import java.util.List;

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
//        sz();
//        sh();
    }
    private void sz() {
        String code = StreamUtils.get(this, R.raw.sz);
        Log("==code="+code.indexOf(""));
        String sz = code.substring(code.indexOf("sz"), code.indexOf(".html"));
        Log("==code=="+sz);
        Log("==code==="+subString(code,"sz",".html"));
        List<String> list=new ArrayList<>();
        while (code.lastIndexOf(".html")-code.lastIndexOf("sz")>2){
            String string = subString(code, "sz", ".html");
            list.add(string);
            code=code.replace(string,"").replace(" ","").replace("sz.html","");
            Log("#==="+string);
        }
        Log("#==size="+list.size());
        Log("#==="+list.get(0));
        Log("#==="+list.get(list.size()-1));
    }
    private void sh() {
        String code = StreamUtils.get(this, R.raw.sh);
        Log("==code="+code.indexOf(""));
        String sh = code.substring(code.indexOf("sh"), code.indexOf(".html"));
        Log("==code=="+sh);
        Log("==code==="+subString(code,"sh",".html"));
        List<String> list=new ArrayList<>();
        while (code.lastIndexOf(".html")-code.lastIndexOf("sh")>2){
            String string = subString(code, "sh", ".html");
            list.add(string);
            code=code.replace(string,"").replace(" ","").replace("sh.html","");
            Log("#==="+string);
        }
        Log("#==size="+list.size());
        Log("#==="+list.get(0));
        Log("#==="+list.get(list.size()-1));
    }

    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex+2, strEndIndex);
        return result;
    }
    public String splitData(String str, String strStart, String strEnd) {
        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;
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
                            copeFile();
                        break;
                }
            }
            private void copeFile() {
                String pathDatabase=mContext.getDatabasePath("MyGP").getPath();
                System.out.println("path="+pathDatabase);
                String newPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ Constant.rootFileName+"/" + "MyGP"+System.currentTimeMillis();

                int copy = CopyFile.copySdcardFile(pathDatabase, newPath);
                System.out.println("path=="+copy);
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

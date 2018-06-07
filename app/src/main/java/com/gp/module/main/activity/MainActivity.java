package com.gp.module.main.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.customview.MyRadioButton;
import com.github.rxbus.RxBus;
import com.gp.Constant;
import com.gp.R;
import com.gp.base.BaseActivity;
import com.gp.base.BaseGP;
import com.gp.base.EventCallback;
import com.gp.base.IOCallBack;
import com.gp.database.DBConstant;
import com.gp.database.DBManager;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;
import com.gp.module.main.event.ProgressEvent;
import com.gp.module.main.fragment.HomeFragment;
import com.gp.module.main.fragment.MyFragment;
import com.gp.module.main.network.ApiRequest;
import com.gp.tools.CalendarUtil;
import com.gp.tools.CopyFile;
import com.gp.tools.StreamUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.FlowableEmitter;

public class MainActivity extends BaseActivity<HomeImp> {
    HomeFragment homeFragment;
    HomeFragment ziXuanFragment;
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

    Timer timer;
    private final int periodTime = 5;


    private LocalBroadcastManager localBroadcastManager;
    private TimerTask timerTask;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setAppRightTitle("复制文件");
//        addHomeFragment();
        hiddenBackIcon();
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startTimer() {
        if (timer != null) {
            stopTimer();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                int hour = CalendarUtil.get(Calendar.HOUR_OF_DAY);
                int minute = CalendarUtil.get(Calendar.MINUTE);
                if (((hour == 9 &&minute>=30)&&hour>9&& hour < 11) || (hour == 11 && minute <= 30) || (hour >= 13 && hour < 15)) {
                    refreshZiXuan();
                }else if(hour>=15){
                    stopTimer();
                }
            }
        };
        timer.schedule(timerTask, 1, periodTime * 1000);
        timerTask.run();
    }

    private void refreshZiXuan() {
        RXStart(new IOCallBack<Long>() {
            @Override
            public void call(FlowableEmitter<Long> emitter) {
                List<GpBean> list = mDaoImp.selectZiXuan();
                for (int i = 0; i < list.size(); i++) {
                    GpBean bean = requestForCode(list.get(i).code, list.get(i).type);
                    if (bean != null) {
                        bean.type = list.get(i).type;
                        bean.uid = System.nanoTime() + "";
                        bean.gp_uid = list.get(i)._id;
                        bean.update_time = new Date().getTime();
                        bean.create_time = new Date().getTime();

                        bean.gp_year = CalendarUtil.getYear();
                        bean.gp_month = CalendarUtil.getMonth();
                        bean.gp_day = CalendarUtil.getDay();
                    }
                    long l = mDaoImp.addDataToTable(bean, DBManager.T_EveryMinute);
                    emitter.onNext(l);
                }
                emitter.onComplete();
            }

            @Override
            public void onMyNext(Long obj) {
                Log("##==onMyNext=" + obj);
            }
        });
    }

    public void addHomeFragment() {
        homeFragment = HomeFragment.newInstance(Constant.ziXuan_0);
        ;
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

    private void copeFile() {
        showLoading();
        RXStart(new IOCallBack<Integer>() {
            @Override
            public void call(FlowableEmitter<Integer> emitter) {
                String pathDatabase = mContext.getDatabasePath("MyGP").getPath();
                System.out.println("path=" + pathDatabase);
                String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constant.rootFileName + "/" + "MyGP" + System.currentTimeMillis();

                int copy = CopyFile.copySdcardFile(pathDatabase, newPath);
                System.out.println("path==" + copy);
                emitter.onNext(copy);
                emitter.onComplete();
            }

            @Override
            public void onMyNext(Integer obj) {
                showMsg(obj == -1 ? "复制失败" : "复制成功");
            }

            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                dismissLoading();
            }

            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                dismissLoading();
            }
        });

    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(ProgressEvent.class, new EventCallback<ProgressEvent>() {
            @Override
            public void accept(ProgressEvent event) {
                tv_adddata_progress.setText(event.progress+"/"+event.count);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initData() {
        initDialog();
        RXStart(new IOCallBack() {
            @Override
            public void call(FlowableEmitter emitter) {
                if (isFirstIntoApp()) {//是否第一次进入app
                    IOFirstIntoApp();
                } else {
                    //收盘之后添加当天数据
                    if (!isSaveShouPanData()) {//没有添加当天收盘数据
                        if (isShouPan()) {//已经收盘收盘
                            IOShouPan();
                        }/* else {
                            //准备开盘
                            IOKaiPan();
                        }*/
                    }/* else {
                        //有添加当天收盘数据，已经收盘
                        IOOther();
                    }*/
                }
                emitter.onComplete();
            }

            @Override
            public void onMyNext(Object obj) {
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                startTimer();
                setNoFirstIntoApp();
                dialogDismiss();
                addHomeFragment();
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                dialogDismiss();
                showMsg("操作失败");
                addHomeFragment();
            }
        });
    }

    private void IOFirstIntoApp() {
        addGPData();
    }


    private void IOShouPan() {
        List<GpBean> list = mDaoImp.selectGpBean(1, true);
        Log("===size" + list.size());
        final int count = list.size();
        for (int i = 0; i < count; i++) {
            String obj = ApiRequest.getDataTongBu(list.get(i).code, list.get(i).type);
            GpBean gpBean = BaseGP.formatStr(obj);
            addShouPanData(gpBean,list.get(i).type);
            RxBus.getInstance().post(new ProgressEvent(i+1,count));
        }
        setSaveShouPanData();
    }


    public void dialogDismiss() {
        if (myDialog != null) {
            myDialog.dismiss();
        }
    }

    //添加当天数据
    private void addTodayData() {
        if (myDialog == null) {
            initDialog();
        }
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(final FlowableEmitter<String> emitter) {
                List<GpBean> list = mDaoImp.selectGpBean(1, true);
                Log("===size" + list.size());
                final int count = list.size();
                for (int i = 0; i < count; i++) {
                    String obj = ApiRequest.getDataTongBu(list.get(i).code, list.get(i).type);
                    if (obj != null && obj.indexOf("v_pv_none_match") == -1) {
                        GpBean bean = BaseGP.formatStr(obj);
                        //修改code表的name
                        bean.type = list.get(i).type;
                        bean.uid = System.nanoTime() + "";
                        bean.gp_uid = list.get(i)._id;
                        bean.update_time = new Date().getTime();
                        bean.create_time = new Date().getTime();

                        bean.gp_year = CalendarUtil.getYear();
                        bean.gp_month = CalendarUtil.getMonth();
                        bean.gp_day = CalendarUtil.getDay();


                        long todayDataCount = mDaoImp.selectTodayDataCount(bean.code, bean.gp_year, bean.gp_month, bean.gp_day);
                        if (todayDataCount == 0) {
                            long l = mDaoImp.addDataToTable(bean, DBManager.T_Everyday);
                            Log("==addDataToTable=" + l);
                        }
                    } else {
                        Log.i(TAG + "##===", "===code2:" + list.get(i).code);
                    }
                    int progress = i + 1;
                    emitter.onNext(progress + "/" + count);
                }
                emitter.onComplete();
            }

            @Override
            public void onMyNext(String msg) {
//                showMsg(msg);
                tv_updatedata_progress.setVisibility(View.VISIBLE);
                tv_updatedata_progress.setText(msg);
            }

            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                myDialog.dismiss();
                addHomeFragment();
            }

            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("修改失败");
                myDialog.dismiss();
            }
        });
    }

    //添加数据到code表
    public void addGPData() {
        List<String> sh = sh();
        List<String> sz = sz();
        int count = sh.size() + sz.size();
        int scaleNum = 0;
        int[] obj;
        for (int i = 0; i < sh.size(); i++) {
            scaleNum++;
            obj = new int[2];
            obj[0] = scaleNum;
            obj[1] = count;
            addCodeData(sh.get(i), DBConstant.type_6);
            RxBus.getInstance().post(new ProgressEvent(scaleNum,count));
        }
        for (int i = 0; i < sz.size(); i++) {
            scaleNum++;
            obj = new int[2];
            obj[0] = scaleNum;
            obj[1] = count;
            addCodeData(sz.get(i), DBConstant.type_0);
            RxBus.getInstance().post(new ProgressEvent(scaleNum,count));
        }
    }

    private void addCodeData(String code, int type) {
        String obj = ApiRequest.getDataTongBu(code, type);
        GpBean bean = BaseGP.formatStr(obj);
        if (bean != null) {
            //修改code表的name
            if (!mDaoImp.existCode(code, DBManager.T_Code)) {//不存在code才添加数据
                boolean result = mDaoImp.addGP(code, bean.gpstr, bean.name, type + "");
                if (result) {
                    addShouPanData(bean, type);
                }
            }
        } else {
            Log.i(TAG + "###===", "===code:" + code);
        }
    }

    public void addShouPanData(GpBean bean, int type) {
        if (isShouPan()) {
            String uid = mDaoImp.selectUid(bean.code, DBManager.T_Code);
            //如果收盘，第一次打开app就添加当天数据
            //修改code表的name
            bean.type = type;
            bean.uid = System.nanoTime() + "";
            bean.gp_uid = uid;
            bean.update_time = new Date().getTime();
            bean.create_time = new Date().getTime();

            bean.gp_year = CalendarUtil.getYear();
            bean.gp_month = CalendarUtil.getMonth();
            bean.gp_day = CalendarUtil.getDay();

            long todayDataCount = mDaoImp.selectTodayDataCount(bean.code, bean.gp_year, bean.gp_month, bean.gp_day);
            if (todayDataCount == 0) {
                long l = mDaoImp.addDataToTable(bean, DBManager.T_Everyday);
                Log("==addDataToTable=" + l);
            }
        }

    }

    private List sz() {
        String code = StreamUtils.get(mContext, R.raw.sz);
        String sz = code.substring(code.indexOf("sz"), code.indexOf(".html"));
        List<String> list = new ArrayList<>();
        while (code.lastIndexOf(".html") - code.lastIndexOf("sz") > 2) {
            String string = subString(code, "sz", ".html");
            list.add(string);
            code = code.replace(string, "").replace(" ", "").replace("sz.html", "");
        }
        Log("#==size=" + list.size());
        return list;
    }

    private List sh() {
        String code = StreamUtils.get(mContext, R.raw.sh);
        List<String> list = new ArrayList<>();
        while (code.lastIndexOf(".html") - code.lastIndexOf("sh") > 2) {
            String string = subString(code, "sh", ".html");
            list.add(string);
            code = code.replace(string, "").replace(" ", "").replace("sh.html", "");
        }
        Log("#==size=" + list.size());
        return list;
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
        hideFragment(ziXuanFragment);
        hideFragment(myFragment);
    }

    private void selectOrder() {
        if (selectView == rb_home_tab2) {
            return;
        }
        selectView = rb_home_tab2;
        if (ziXuanFragment == null) {
            ziXuanFragment = HomeFragment.newInstance(Constant.ziXuan_1);
            addFragment(R.id.fl_content, ziXuanFragment);
        } else {
            showFragment(ziXuanFragment);
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
            myFragment = MyFragment.newInstance();
            addFragment(R.id.fl_content, myFragment);
        } else {
            showFragment(myFragment);
        }
        hideFragment(homeFragment);
        hideFragment(ziXuanFragment);
    }


    @OnClick({R.id.app_right_tv})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.app_right_tv:
                copeFile();
                break;
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
        String result = str.substring(strStartIndex + 2, strEndIndex);
        return result;
    }
}

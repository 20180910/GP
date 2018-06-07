package com.gp.module.main.activity;

import android.graphics.Color;
import android.view.View;

import com.github.androidtools.AndroidUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gp.IntentParam;
import com.gp.R;
import com.gp.base.BaseActivity;
import com.gp.base.IOCallBack;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

/**
 * Created by windows10 on 2018/6/4.
 */

public class NowWaiNeiPanActivity extends BaseActivity<HomeImp> {
    @BindView(R.id.chart)
    LineChart chart;

    private String code;

    @Override
    protected int getContentView() {
        return R.layout.nowwaineipan_act;
    }

    @Override
    protected void initView() {
        code = getIntent().getStringExtra(IntentParam.code);
        String name = getIntent().getStringExtra(IntentParam.name);
        setAppTitle(name.replace(" ","")+code);
    }

    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(pl_load,new IOCallBack<List<GpBean>>() {
            @Override
            public void call(FlowableEmitter emitter) {
                List<GpBean> list = mDaoImp.selectZiXuanTodayDetail(code);
                emitter.onNext(list);
                emitter.onComplete();
            }
            @Override
            public void onMyNext(List<GpBean> list) {
                if(notEmpty(list)){
                    setList(list);
                }else{
                    Log("##===empty");
                }
            }
        });
    }
    private void setList(List<GpBean> list) {
        List<Entry> waiPan = new ArrayList<Entry>();
        List<Entry> neiPan = new ArrayList<Entry>();
        List<Entry> waiNeiBi = new ArrayList<Entry>();

        for (int i = 0; i < list.size(); i++) {
            waiPan.add(new Entry(i,Float.parseFloat(list.get(i).wai_num)));
            neiPan.add(new Entry(i,Float.parseFloat(list.get(i).nei_num)));
            double bi;
            if(Integer.parseInt(list.get(i).nei_num)==0){
                 bi = AndroidUtils.chuFa(Integer.parseInt(list.get(i).wai_num), 1, 4);
            }else{
                 bi = AndroidUtils.chuFa(Integer.parseInt(list.get(i).wai_num), Integer.parseInt(list.get(i).nei_num), 4);
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date(list.get(i).create_time));
                if(calendar.get(Calendar.MINUTE)>=31){
                    waiNeiBi.add(new Entry(i, (float) bi));
                }
            }
        }
        LineDataSet dataSet = new LineDataSet(waiPan, "外盘"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawCircles(false);




        LineDataSet dataSet2 = new LineDataSet(neiPan, "内盘"); // add entries to dataset
        dataSet2.setColor(Color.GREEN);
        dataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet2.setDrawCircles(false);
        dataSet2.setValueTextColor(Color.BLACK);


        LineDataSet dataSet3 = new LineDataSet(waiNeiBi, "外盘/内盘"); // add entries to dataset
        dataSet3.setColor(Color.BLUE);
        dataSet3.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet3.setDrawCircles(false);
        dataSet3.setValueTextColor(Color.BLACK);



        LineData lineData = new LineData(dataSet,dataSet2,dataSet3);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(10);
        xAxis.setLabelRotationAngle(45);
        xAxis.setAxisMaximum(3000);



        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);

        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    @Override
    protected void onViewClick(View v) {

    }
}

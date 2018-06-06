package com.gp.module.main.activity;

import android.graphics.Color;
import android.view.View;

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
        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i,Float.parseFloat(list.get(i).wai_num)));
        }
        LineDataSet dataSet = new LineDataSet(entries, "外盘"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawCircles(false);




        List<Entry> entries2 = new ArrayList<Entry>();

        for (int i = 0; i < list.size(); i++) {
            entries2.add(new Entry(i,Float.parseFloat(list.get(i).nei_num)));
        }
        LineDataSet dataSet2 = new LineDataSet(entries2, "内盘"); // add entries to dataset
        dataSet2.setColor(Color.GREEN);
        dataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet2.setDrawCircles(false);
        dataSet2.setValueTextColor(Color.BLACK);



        LineData lineData = new LineData(dataSet,dataSet2);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(10);
        xAxis.setLabelRotationAngle(45);



        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);

        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    @Override
    protected void onViewClick(View v) {

    }
}

package com.gp.module.main.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.androidtools.DateUtils;
import com.github.androidtools.SPUtils;
import com.gp.AppXml;
import com.gp.R;
import com.gp.base.BaseFragment;
import com.gp.base.BaseGP;
import com.gp.base.IOCallBack;
import com.gp.database.DBManager;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;
import com.gp.module.main.network.ApiRequest;
import com.gp.tools.CalendarUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/5/31.
 */

public class HomeFragment extends BaseFragment<HomeImp> {
    @BindView(R.id.rv_all_gp)
    RecyclerView rv_all_gp;
    @Override
    protected int getContentView() {
        return R.layout.home_frag;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        showProgress();
        showContent();
        //添加当天数据
        addTodayData();
//        getData(1,false);

    }

    private void addTodayData() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Log("==="+hour+":"+minute);
        String isSaveTodayData = SPUtils.getString(mContext, AppXml.isSaveTodayData,"");
        String dateToString = DateUtils.dateToString(new Date());
        if(!dateToString.equals(isSaveTodayData)){
            if(hour>15||(hour==15&&minute>2)){
                addDataToTable();
            }
        }

        addDataToTable();

    }

    private void addDataToTable() {
        selectData();
    }
    private void selectData() {
        initDialog();
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(final FlowableEmitter<String> emitter) {
                List<GpBean> list = mDaoImp.selectGpBean(1, true);
                Log("===size"+list.size());
                final int count=list.size();
                for (int i = 0; i < count; i++) {
                    String obj = ApiRequest.getDataTongBu(list.get(i).code, list.get(i).type);
                    if(obj!=null&&obj.indexOf("v_pv_none_match")==-1){
                        GpBean bean = BaseGP.formatStr(obj);
                        //修改code表的name
                        bean.type=list.get(i).type;
                        bean.uid=System.nanoTime()+"";
                        bean.gp_uid=list.get(i)._id;
                        bean.update_time=new Date().getTime();
                        bean.create_time=new Date().getTime();

                        bean.gp_year= CalendarUtil.getYear();
                        bean.gp_month=CalendarUtil.getMonth();
                        bean.gp_day=CalendarUtil.getDay();

                        long l = mDaoImp.addDataToTable(bean, DBManager.T_Everyday);
                        Log("==addDataToTable="+l);
                    }
                    int progress=i+1;
                    emitter.onNext(progress+"/"+count);
                }
                emitter.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
//                showMsg(msg);
                tv_updatedata_progress.setText(msg);
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                myDialog.dismiss();
            }

            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("修改失败");
                myDialog.dismiss();
            }
        });
    }
    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
    }

    @Override
    protected void onViewClick(View v) {

    }

}

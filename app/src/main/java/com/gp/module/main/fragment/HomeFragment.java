package com.gp.module.main.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.MyLoadMoreAdapter;
import com.github.baseclass.adapter.MyRecyclerViewHolder;
import com.github.baseclass.view.MyDialog;
import com.github.rxbus.RxBus;
import com.gp.Constant;
import com.gp.R;
import com.gp.base.BaseFragment;
import com.gp.base.EventCallback;
import com.gp.base.IOCallBack;
import com.gp.database.DBManager;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;
import com.gp.module.main.event.JoinEvent;
import com.library.base.tools.has.AndroidUtils;

import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/5/31.
 */

public class HomeFragment extends BaseFragment<HomeImp> {
    @BindView(R.id.ll_my)
    LinearLayout ll_my;
    @BindView(R.id.rv_all_gp)
    RecyclerView rv_all_gp;
    MyLoadMoreAdapter adapter;
    @Override
    protected int getContentView() {
        return R.layout.home_frag;
    }

    public static HomeFragment newInstance(int ziXuanFlag) {
        Bundle args = new Bundle();
        args.putInt(Constant.ziXuanFlag, ziXuanFlag);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(JoinEvent.class, new EventCallback<JoinEvent>() {
            @Override
            public void accept(JoinEvent event) {
                if(getArguments().getInt(Constant.ziXuanFlag,0)==1){
                    getData(1,false);
                }
            }
        });
    }

    @Override
    protected void initView() {
        clearFocus(rv_all_gp,ll_my);
        adapter=new MyLoadMoreAdapter<GpBean>(mContext,R.layout.every_day_item,pageSize) {
            @Override
            public void bindData(MyRecyclerViewHolder holder, int position, final GpBean bean) {
                holder.setText(R.id.tv_name,bean.name);
                holder.setText(R.id.tv_code,bean.code);

                TextView tv_price = holder.getTextView(R.id.tv_price);
                tv_price.setText(bean.now_price+"");

                TextView tv_wainei_pan = holder.getTextView(R.id.tv_wainei_pan);
                TextView tv_wainei_pan_num = holder.getTextView(R.id.tv_wainei_pan_num);
                tv_wainei_pan_num.setText(bean.wai_num+"\n"+bean.nei_num);
                double waiNeiPan;
                if(Double.parseDouble(bean.nei_num)==0){
                    waiNeiPan=0;
                }else{
                    waiNeiPan = AndroidUtils.chuFa(Double.parseDouble(bean.wai_num), Double.parseDouble(bean.nei_num), 5);
                }
                tv_wainei_pan.setText(waiNeiPan+"");

                TextView tv_price_change_percent = holder.getTextView(R.id.tv_price_change_percent);
                holder.setText(R.id.tv_price_change_percent,bean.change_price_percent+"%");

                TextView tv_price_change = holder.getTextView(R.id.tv_price_change);
                holder.setText(R.id.tv_price_change,bean.change_price+"");

                if(waiNeiPan>1){
                    tv_wainei_pan.setTextColor(ContextCompat.getColor(mContext,R.color.red_gp));
                }else if(waiNeiPan<1){
                    tv_wainei_pan.setTextColor(ContextCompat.getColor(mContext,R.color.green_gp));
                }else{
                    tv_wainei_pan.setTextColor(ContextCompat.getColor(mContext,R.color.gray_66));
                }

                if(bean.change_price_percent>0){
                    tv_price.setTextColor(ContextCompat.getColor(mContext,R.color.red_gp));
                    tv_price_change_percent.setTextColor(ContextCompat.getColor(mContext,R.color.red_gp));
                    tv_price_change.setTextColor(ContextCompat.getColor(mContext,R.color.red_gp));
                }else if(bean.change_price_percent<0){
                    tv_price.setTextColor(ContextCompat.getColor(mContext,R.color.green_gp));
                    tv_price_change_percent.setTextColor(ContextCompat.getColor(mContext,R.color.green_gp));
                    tv_price_change.setTextColor(ContextCompat.getColor(mContext,R.color.green_gp));
                }else{
                    tv_price.setTextColor(ContextCompat.getColor(mContext,R.color.gray_66));
                    tv_price_change_percent.setTextColor(ContextCompat.getColor(mContext,R.color.gray_66));
                    tv_price_change.setTextColor(ContextCompat.getColor(mContext,R.color.gray_66));
                }

                holder.getView(R.id.ll_join_zx).setOnClickListener(new MyOnClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        if(getArguments().getInt(Constant.ziXuanFlag,0)==0){
                            joinZiXuan(bean.code);
                        }
                    }
                });
                holder.getView(R.id.ll_join_zx).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(getArguments().getInt(Constant.ziXuanFlag,0)==1){
                            MyDialog.Builder mDialog=new MyDialog.Builder(mContext);
                            mDialog.setMessage("是否删除自选?");
                            mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    joinZiXuan(bean.code);
                                }
                            });
                            mDialog.create().show();
                        }
                        return true;
                    }
                });

            }
        };
        adapter.setOnLoadMoreListener(this);
        rv_all_gp.setAdapter(adapter);
    }

    private void joinZiXuan(final String code) {
        showLoading();
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> emitter) {
                boolean ziXuan = mDaoImp.isZiXuan(code);
                if(ziXuan){
                    boolean removeZiXuan = mDaoImp.removeZiXuan(code, DBManager.T_Code);
                    boolean removeZiXuanDay = mDaoImp.removeZiXuan(code, DBManager.T_Everyday);
                    if(removeZiXuan){
                        emitter.onNext("删除自选");
                    }else{
                        emitter.onNext("删除自选失败");
                    }
                    emitter.onComplete();
                }else{
                    boolean joinZiXuan = mDaoImp.joinZiXuan(code, DBManager.T_Code);
                    boolean joinZiXuanDay = mDaoImp.joinZiXuan(code, DBManager.T_Everyday);
                    if(joinZiXuan){
                        emitter.onNext("添加自选");
                        emitter.onComplete();
                    }else{
                        emitter.onError(new Throwable("0"));
                    }
                }

            }
            @Override
            public void onMyNext(String obj) {
                showMsg(obj);
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                RxBus.getInstance().post(new JoinEvent());
                dismissLoading();
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("添加失败");
                dismissLoading();
            }
        });
    }

    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }

    @Override
    protected void getData(final int page,final boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(pl_load, new IOCallBack<List<GpBean>>() {
            @Override
            public void call(FlowableEmitter<List<GpBean>> emitter) {
                int isZiXuan=getArguments().getInt(Constant.ziXuanFlag,0);
                List<GpBean> list = mDaoImp.selectEveryDay(page, null, true,isZiXuan==1?true:false);
                emitter.onNext(list);
                emitter.onComplete();
            }
            @Override
            public void onMyNext(List<GpBean> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    pageNum=2;
                    adapter.setList(list,true);
                }
            }
        });
    }

    @Override
    protected void onViewClick(View v) {

    }

}

package com.gp.module.main.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.MyLoadMoreAdapter;
import com.github.baseclass.adapter.MyRecyclerViewHolder;
import com.gp.R;
import com.gp.base.BaseFragment;
import com.gp.base.IOCallBack;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;
import com.library.base.tools.has.AndroidUtils;

import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/5/31.
 */

public class HomeFragment extends BaseFragment<HomeImp> {
    @BindView(R.id.rv_all_gp)
    RecyclerView rv_all_gp;
    MyLoadMoreAdapter adapter;
    @Override
    protected int getContentView() {
        return R.layout.home_frag;
    }

    @Override
    protected void initView() {
        adapter=new MyLoadMoreAdapter<GpBean>(mContext,R.layout.every_day_item,pageSize) {
            @Override
            public void bindData(MyRecyclerViewHolder holder, int position, final GpBean bean) {
                holder.setText(R.id.tv_name,bean.name);
                holder.setText(R.id.tv_code,bean.code);

                TextView tv_price = holder.getTextView(R.id.tv_price);
                tv_price.setText(bean.now_price+"");

                TextView tv_wainei_pan = holder.getTextView(R.id.tv_wainei_pan);
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
                        joinZiXuan(bean.code);
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
                    boolean removeZiXuan = mDaoImp.removeZiXuan(code);
                    if(removeZiXuan){
                        emitter.onNext("删除自选");
                    }else{
                        emitter.onNext("删除自选失败");
                    }
                    emitter.onComplete();
                }else{
                    boolean joinZiXuan = mDaoImp.joinZiXuan(code);
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
                List<GpBean> list = mDaoImp.selectEveryDay(page, null, true);
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

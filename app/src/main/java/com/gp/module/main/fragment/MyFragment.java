package com.gp.module.main.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.MyLoadMoreAdapter;
import com.github.baseclass.adapter.MyRecyclerViewHolder;
import com.github.rxbus.RxBus;
import com.gp.R;
import com.gp.base.BaseFragment;
import com.gp.base.IOCallBack;
import com.gp.database.DBManager;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.dao.HomeImp;
import com.gp.module.main.event.JoinEvent;

import java.util.List;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MyFragment extends BaseFragment<HomeImp> {
    @BindView(R.id.rv_all_code)
    RecyclerView rv_all_code;
    @BindView(R.id.cb_code_zixuan)
    CheckBox cb_code_zixuan;

    @BindView(R.id.et_code_search)
    EditText et_code_search;

    MyLoadMoreAdapter adapter;

    String searchInfo="";

    @Override
    protected int getContentView() {
        return R.layout.my_frag;
    }

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {
        adapter=new MyLoadMoreAdapter<GpBean>(mContext,R.layout.code_item,pageSize) {
            @Override
            public void bindData(MyRecyclerViewHolder holder, final int position, final GpBean bean) {
                TextView tv_all_code_name = holder.getTextView(R.id.tv_all_code_name);
                tv_all_code_name.setText(bean.code+"   "+bean.name);
                String status="加入自选";
                if(bean.status==1){
                    status="删除自选";
                    tv_all_code_name.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimaryAlpha1));
                }else{
                    tv_all_code_name.setTextColor(ContextCompat.getColor(mContext,R.color.gray_66));
                }
                holder.setText(R.id.tv_all_code_join,status);
                holder.getView(R.id.tv_all_code_join).setOnClickListener(new MyOnClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        joinZiXuan(bean.code,position);
                    }
                });
            }
        };
        adapter.setOnLoadMoreListener(this);
        rv_all_code.setAdapter(adapter);




        et_code_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchInfo=s.toString();
                getData(1,false);
            }
        });

        cb_code_zixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1,false);
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
            public void call(FlowableEmitter emitter) {
                List<GpBean> gpBeen = mDaoImp.selectGpBean(page, searchInfo,cb_code_zixuan.isChecked(),false,mDaoImp.getWritableDatabase());
                emitter.onNext(gpBeen);
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
    private void joinZiXuan(final String code,final int position) {
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
                GpBean bean = (GpBean) adapter.getList().get(position);
                bean.status=Math.abs(bean.status-1);
                adapter.notifyDataSetChanged();

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
}

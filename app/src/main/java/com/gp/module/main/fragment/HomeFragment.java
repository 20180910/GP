package com.gp.module.main.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.rxbus.rxjava.MyFlowableSubscriber;
import com.gp.R;
import com.gp.base.BaseFragment;

import butterknife.BindView;
import io.reactivex.FlowableEmitter;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2018/5/31.
 */

public class HomeFragment extends BaseFragment {
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
        showProgress();
        getData(1,false);
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(new MyFlowableSubscriber<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) {

            }

            @Override
            public void onNext(String obj) {

            }
        });

    }

    @Override
    protected void onViewClick(View v) {

    }
}

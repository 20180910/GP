package com.gp.module.main.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gp.R;
import com.gp.base.BaseFragment;
import com.gp.base.BaseGP;
import com.gp.base.HuZhaoCallBack;
import com.gp.base.IOCallBack;
import com.gp.database.DBConstant;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.bean.GpProgressObj;
import com.gp.module.main.dao.HomeImp;
import com.gp.module.main.network.ApiRequest;
import com.gp.tools.StreamUtils;

import java.util.ArrayList;
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
    private List sz() {
        String code = StreamUtils.get(mContext, R.raw.sz);
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
        return list;
    }
    private List sh() {
        String code = StreamUtils.get(mContext, R.raw.sh);
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
        return list;
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
    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
//        addGPData();
        setName();
    }

    @Override
    protected void onViewClick(View v) {

    }
    public void setName(){
      /*  ApiRequest.getSHData("600011", new HuZhaoCallBack<String>(mContext) {
            @Override
            public void onSuccess(String obj) {
                Log("=api=="+obj);
                GpBean bean = BaseGP.formatStr(obj);
                long l = mDaoImp.updateGpBean(bean);
                Log.i(TAG+"===","==="+l);
            }
        });
*/
//        addGPData();
        selectData();
    }

    private void updateData(String code) {
        ApiRequest.getSHData(code, new HuZhaoCallBack<String>(mContext) {
            @Override
            public void onSuccess(String obj) {
                GpBean bean = BaseGP.formatStr(obj);
                long l = mDaoImp.updateGpBean(bean);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.i(TAG+"##===onError","===");
            }
        });
    }

    private void selectData() {
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
                        long l = mDaoImp.updateGpBean(bean);
                    }
                    emitter.onNext(i+"/"+count);
                }
                emitter.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
                showMsg(msg);
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                Log.i(TAG+"===","##===onMyError");
            }
        });
    }

    public void addGPData(){
        RXStart(pl_load,new IOCallBack<GpProgressObj>() {
            @Override
            public void call(FlowableEmitter<GpProgressObj> emitter) {
                List<String> sh = sh();
                List<String> sz = sz();
                int count=sh.size()+sz.size();
                int scaleNum=0;
                GpProgressObj obj;
                for (int i = 0; i < sh.size(); i++) {
                    scaleNum++;
                    obj=new GpProgressObj();
                    obj.count=count;
                    obj.current=scaleNum;

                    mDaoImp.addGP(sh.get(i), DBConstant.type_6);

                    emitter.onNext(obj);
                }
                for (int i = 0; i < sz.size(); i++) {
                    scaleNum++;
                    obj=new GpProgressObj();
                    obj.count=count;
                    obj.current=scaleNum;

                    long result = mDaoImp.addGP(sz.get(i), DBConstant.type_0);
                    Log("==result="+result);

                    emitter.onNext(obj);
                }
                emitter.onComplete();
            }

            @Override
            public void onMyNext(GpProgressObj obj) {
//                showMsg(msg);
                showMsg(obj.current+"/"+obj.count);
            }
            @Override
            public void onMyCompleted() {
                super.onMyCompleted();
                showMsg("添加完成");
            }
            @Override
            public void onMyError(Throwable e) {
                super.onMyError(e);
                showMsg("添加失败");
            }
        });
    }
}

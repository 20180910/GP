package com.gp.base;

import android.text.TextUtils;

import com.gp.module.main.bean.GpBean;

/**
 * Created by Administrator on 2018/6/1.
 */

public class BaseGP {
   public String _id;
   public String uid;
   public String gp_uid;
   public long create_time;
   public long update_time;
   public int type;

    public static GpBean formatStr(String data){
        String[] str=data.replace("\"","").split(";")[0].split("~");
        GpBean bean=new GpBean();
        bean.gpstr        = str[0];
        bean.name         = str[1];
        bean.code         = str[2];
        bean.now_price    = Float.parseFloat(toFloat(str[3]));
        bean.zuo_price    = Float.parseFloat(toFloat(str[4]));
        bean.jin_price    = Float.parseFloat(toFloat(str[5]));
        bean.total_volume = str[6];
        bean.wai_num      = str[7];
        bean.nei_num      = str[8];
        bean.buy_price    = Float.parseFloat(toFloat(str[9]));
        bean.buy_volume   = str[10];
        bean.buy2_price   = Float.parseFloat(toFloat(str[11]));
        bean.buy2_volume  = str[12];
        bean.buy3_price   = Float.parseFloat(toFloat(str[13]));
        bean.buy3_volume  = str[14];
        bean.buy4_price   = Float.parseFloat(toFloat(str[15]));
        bean.buy4_volume  = str[16];
        bean.buy5_price   = Float.parseFloat(toFloat(str[17]));
        bean.buy5_volume  = str[18];
        bean.sell_price   = Float.parseFloat(toFloat(str[19]));
        bean.sell_volume  = str[20];
        bean.sell2_price  = Float.parseFloat(toFloat(str[21]));
        bean.sell2_volume = str[22];
        bean.sell3_price  = Float.parseFloat(toFloat(str[23]));
        bean.sell3_volume = str[24];
        bean.sell4_price  = Float.parseFloat(toFloat(str[25]));
        bean.sell4_volume = str[26];
        bean.sell5_price  = Float.parseFloat(toFloat(str[27]));
        bean.sell5_volume = str[28];
        bean.recent_trade           = str[29];
        bean.recent_time            = str[30];
        bean.change_price           = Float.parseFloat(toFloat(str[31]));
        bean.change_price_percent   = Float.parseFloat(toFloat(str[32]));
        bean.max_price              = Float.parseFloat(toFloat(str[33]));
        bean.min_price              = Float.parseFloat(toFloat(str[34]));
        bean.last_price             = str[35];
        bean.last_volume_num        = str[36];
        bean.last_volume_price      = str[37];
        bean.huan_shou_lv           = Float.parseFloat(toFloat(str[38]));
        bean.shi_ying_lv_ttm        =Float.parseFloat(toFloat( str[39]));
        bean.ziduan1                = str[40];
        bean.max_price2             = Float.parseFloat(toFloat(str[41]));
        bean.min_price2             = Float.parseFloat(toFloat(str[42]));
        bean.zhen_fu                = Float.parseFloat(toFloat(str[43]));
        bean.liutong_shizhi         =Float.parseFloat(toFloat( str[44]));
        bean.total_shizhi           = Float.parseFloat(toFloat(str[45]));
        bean.shi_jing_lv            = Float.parseFloat(toFloat(str[46]));
        bean.top_price              = Float.parseFloat(toFloat(str[47]));
        bean.bottom_price           = Float.parseFloat(toFloat(str[48]));
        bean.liang_bi               = Float.parseFloat(toFloat(str[49]));
        bean.ziduan2                = str[50];
        if(str.length>=52){
            bean.average_price          = Float.parseFloat(toFloat(str[51]));
        }
        if(str.length>=53){
            bean.shi_ying_lv_dong       = Float.parseFloat(toFloat(str[52]));
        }
        if(str.length>=54){
            bean.shi_ying_lv_jing       = Float.parseFloat(toFloat(str[53]));
        }

        return bean;
    }
    private static String toFloat(String data){
        if(TextUtils.isEmpty(data)){
            return "0";
        }else{
            return data;
        }
    }
}

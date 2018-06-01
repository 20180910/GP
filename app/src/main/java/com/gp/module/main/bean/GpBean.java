package com.gp.module.main.bean;

import com.gp.base.BaseGP;

/**
 * Created by Administrator on 2018/6/1.
 */

public class GpBean extends BaseGP {
    private String gp_year      ;  //INTEGER(255) not null,
    private String gp_month     ;  //INTEGER(255) not null,
    private String gp_day       ;  //INTEGER(255) not null,
      //-- ~ 分割字符串中内容，下标从0开始，依次为
    private String gpstr        ;  //varchar(255) not null,--  0: 未知                        v_sz000858=51
    private String name         ;  //varchar(255) not null,--  1: 名字                        五 粮 液
    private String code         ;  //varchar(255) not null,--  2: 代码                        000858
    private String now_price    ;  //float not null,--  3: 当前价格                    78.98
    private String zuo_price    ;  //float not null,--  4: 昨收                        76.80
    private String jin_price    ;  //float not null,--  5: 今开                        77.79
    private String total_volume ;  //varchar(255) not null,--  6: 成交量（手）                415294
    private String wai_num      ;  //    varchar(255) not null,--  7: 外盘                        217206
    private String nei_num      ;  //varchar(255) not null,--  8: 内盘                        197898
//private String
    private String buy_price    ;  //float not null,--  9: 买一                        78.98
    private String buy_volume   ;  //varchar(255) not null,--  10: 买一量（手）               882
//private String
    private String buy2_price   ;  //float not null,--  11-18: 买二 买二               78.97
    private String buy2_volume  ;  //varchar(255) not null,--  12: 买二 买二               882
//private String
    private String buy3_price   ;  //float not null,--  13: 买3 买3               78.97
    private String buy3_volume  ;  //varchar(255) not null,--  14: 买3 买3               882
//private String
    private String buy4_price   ;  //float not null,--  15: 买4 买4               78.97
    private String buy4_volume  ;  //varchar(255) not null,--  16: 买4 买4               882
//private String
    private String buy5_price   ;  //float not null,--  17: 买二 买五               78.97
    private String buy5_volume  ;  //varchar(255) not null,--  18: 买二 买五               882
//private String
    private String sell_price   ;  //float not null,--  19: 卖一                       78.99
    private String sell_volume  ;  //varchar(255) not null,--  20: 卖一量                      65
//private String
    private String sell2_price  ;  //float not null,--  21: 卖2                       78.99
    private String sell2_volume ;  //varchar(255) not null,--  22: 卖2量                      882
//private String
    private String sell3_price  ;  //float not null,--  23: 卖3                       78.99
    private String sell3_volume ;  //varchar(255) not null,--  24: 卖3量                      882
//private String
    private String sell4_price  ;  //float not null,--  25: 卖4                       78.99
    private String sell4_volume ;  //varchar(255) not null,--  26: 卖4量                      882
//private String
    private String sell5_price  ;  //float not null,--  27: 卖5                       78.99
    private String sell5_volume ;  //varchar(255) not null,--  28: 卖5量                      882
//private String
//private String
    private String recent_trade ;  //    varchar(255) not null,--  29: 最近逐笔成交               15:00:03/78.98/7062/S/55771095/9604|14:57:00/78.81/50/B/394042/9515|14:56:57/78.81/172/B/1355276/9513|14:56:54/78.79/275/S/2166827/9511|14:56:51/78.80/147/B/1158333/9509|14:56:48/78.80/384/S/3025949/9507
    private String recent_time  ;  //    varchar(255) not null,--  30: 时间                      20180531150136
    private String change_price ;          //float not null,--  31: 涨跌                      2.18
    private String change_price_percent ;  //float not null,--  32: 涨跌%                     2.84
    private String max_price            ;  //float not null,--  33: 最高                      79.59
    private String min_price            ;  //float not null,--  34: 最低                      76.85
    private String last_price           ;  //varchar(255) not null,--  35: 价格/成交量（手）/成交额   78.98/415294/3266151334
    private String last_volume_num      ;  //varchar(255) not null,--  36: 成交量（手）               415294
    private String last_volume_price    ;  //varchar(255) not null,--  37: 成交额（万）              326615
    private String huan_shou_lv         ;  //float not null,--  38: 换手率                    1.09
    private String shi_ying_lv_ttm      ;  //float not null,--  39: 市盈率                    27.74
    private String ziduan1              ;  //varchar(255) not null,--  40:
    private String max_price2           ;  //float not null,--  41: 最高                     79.59
    private String min_price2           ;  //float not null,--  42: 最低                     76.85
    private String zhen_fu              ;  //float not null,--  43: 振幅                     3.57
    private String liutong_shizhi       ;  //float not null,--  44: 流通市值                 2997.90
    private String total_shizhi         ;  //float not null,--  45: 总市值                   3065.69
    private String shi_jing_lv          ;  //float not null,--  46: 市净率                   5.14
    private String top_price            ;  //float not null,--  47: 涨停价                   84.48
    private String bottom_price         ;  //float not null,--  48: 跌停价                   69.12
    private String liang_bi             ;  //float not null,--  49: ?                        1.40
    private String ziduan2              ;  //varchar(255) not null,--  50: ?                        -147
    private String average_price        ;  //float not null,--  51: ?                        78.66
    private String shi_ying_lv_dong     ;  //float not null,--  52: ?                        15.42
    private String shi_ying_lv_jing     ;  //float not null--  53: ?                        31.69


    public String getGp_year() {
        return gp_year;
    }

    public void setGp_year(String gp_year) {
        this.gp_year = gp_year;
    }

    public String getGp_month() {
        return gp_month;
    }

    public void setGp_month(String gp_month) {
        this.gp_month = gp_month;
    }

    public String getGp_day() {
        return gp_day;
    }

    public void setGp_day(String gp_day) {
        this.gp_day = gp_day;
    }

    public String getGpstr() {
        return gpstr;
    }

    public void setGpstr(String gpstr) {
        this.gpstr = gpstr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNow_price() {
        return now_price;
    }

    public void setNow_price(String now_price) {
        this.now_price = now_price;
    }

    public String getZuo_price() {
        return zuo_price;
    }

    public void setZuo_price(String zuo_price) {
        this.zuo_price = zuo_price;
    }

    public String getJin_price() {
        return jin_price;
    }

    public void setJin_price(String jin_price) {
        this.jin_price = jin_price;
    }

    public String getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(String total_volume) {
        this.total_volume = total_volume;
    }

    public String getWai_num() {
        return wai_num;
    }

    public void setWai_num(String wai_num) {
        this.wai_num = wai_num;
    }

    public String getNei_num() {
        return nei_num;
    }

    public void setNei_num(String nei_num) {
        this.nei_num = nei_num;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getBuy_volume() {
        return buy_volume;
    }

    public void setBuy_volume(String buy_volume) {
        this.buy_volume = buy_volume;
    }

    public String getBuy2_price() {
        return buy2_price;
    }

    public void setBuy2_price(String buy2_price) {
        this.buy2_price = buy2_price;
    }

    public String getBuy2_volume() {
        return buy2_volume;
    }

    public void setBuy2_volume(String buy2_volume) {
        this.buy2_volume = buy2_volume;
    }

    public String getBuy3_price() {
        return buy3_price;
    }

    public void setBuy3_price(String buy3_price) {
        this.buy3_price = buy3_price;
    }

    public String getBuy3_volume() {
        return buy3_volume;
    }

    public void setBuy3_volume(String buy3_volume) {
        this.buy3_volume = buy3_volume;
    }

    public String getBuy4_price() {
        return buy4_price;
    }

    public void setBuy4_price(String buy4_price) {
        this.buy4_price = buy4_price;
    }

    public String getBuy4_volume() {
        return buy4_volume;
    }

    public void setBuy4_volume(String buy4_volume) {
        this.buy4_volume = buy4_volume;
    }

    public String getBuy5_price() {
        return buy5_price;
    }

    public void setBuy5_price(String buy5_price) {
        this.buy5_price = buy5_price;
    }

    public String getBuy5_volume() {
        return buy5_volume;
    }

    public void setBuy5_volume(String buy5_volume) {
        this.buy5_volume = buy5_volume;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getSell_volume() {
        return sell_volume;
    }

    public void setSell_volume(String sell_volume) {
        this.sell_volume = sell_volume;
    }

    public String getSell2_price() {
        return sell2_price;
    }

    public void setSell2_price(String sell2_price) {
        this.sell2_price = sell2_price;
    }

    public String getSell2_volume() {
        return sell2_volume;
    }

    public void setSell2_volume(String sell2_volume) {
        this.sell2_volume = sell2_volume;
    }

    public String getSell3_price() {
        return sell3_price;
    }

    public void setSell3_price(String sell3_price) {
        this.sell3_price = sell3_price;
    }

    public String getSell3_volume() {
        return sell3_volume;
    }

    public void setSell3_volume(String sell3_volume) {
        this.sell3_volume = sell3_volume;
    }

    public String getSell4_price() {
        return sell4_price;
    }

    public void setSell4_price(String sell4_price) {
        this.sell4_price = sell4_price;
    }

    public String getSell4_volume() {
        return sell4_volume;
    }

    public void setSell4_volume(String sell4_volume) {
        this.sell4_volume = sell4_volume;
    }

    public String getSell5_price() {
        return sell5_price;
    }

    public void setSell5_price(String sell5_price) {
        this.sell5_price = sell5_price;
    }

    public String getSell5_volume() {
        return sell5_volume;
    }

    public void setSell5_volume(String sell5_volume) {
        this.sell5_volume = sell5_volume;
    }

    public String getRecent_trade() {
        return recent_trade;
    }

    public void setRecent_trade(String recent_trade) {
        this.recent_trade = recent_trade;
    }

    public String getRecent_time() {
        return recent_time;
    }

    public void setRecent_time(String recent_time) {
        this.recent_time = recent_time;
    }

    public String getChange_price() {
        return change_price;
    }

    public void setChange_price(String change_price) {
        this.change_price = change_price;
    }

    public String getChange_price_percent() {
        return change_price_percent;
    }

    public void setChange_price_percent(String change_price_percent) {
        this.change_price_percent = change_price_percent;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getLast_price() {
        return last_price;
    }

    public void setLast_price(String last_price) {
        this.last_price = last_price;
    }

    public String getLast_volume_num() {
        return last_volume_num;
    }

    public void setLast_volume_num(String last_volume_num) {
        this.last_volume_num = last_volume_num;
    }

    public String getLast_volume_price() {
        return last_volume_price;
    }

    public void setLast_volume_price(String last_volume_price) {
        this.last_volume_price = last_volume_price;
    }

    public String getHuan_shou_lv() {
        return huan_shou_lv;
    }

    public void setHuan_shou_lv(String huan_shou_lv) {
        this.huan_shou_lv = huan_shou_lv;
    }

    public String getShi_ying_lv_ttm() {
        return shi_ying_lv_ttm;
    }

    public void setShi_ying_lv_ttm(String shi_ying_lv_ttm) {
        this.shi_ying_lv_ttm = shi_ying_lv_ttm;
    }

    public String getZiduan1() {
        return ziduan1;
    }

    public void setZiduan1(String ziduan1) {
        this.ziduan1 = ziduan1;
    }

    public String getMax_price2() {
        return max_price2;
    }

    public void setMax_price2(String max_price2) {
        this.max_price2 = max_price2;
    }

    public String getMin_price2() {
        return min_price2;
    }

    public void setMin_price2(String min_price2) {
        this.min_price2 = min_price2;
    }

    public String getZhen_fu() {
        return zhen_fu;
    }

    public void setZhen_fu(String zhen_fu) {
        this.zhen_fu = zhen_fu;
    }

    public String getLiutong_shizhi() {
        return liutong_shizhi;
    }

    public void setLiutong_shizhi(String liutong_shizhi) {
        this.liutong_shizhi = liutong_shizhi;
    }

    public String getTotal_shizhi() {
        return total_shizhi;
    }

    public void setTotal_shizhi(String total_shizhi) {
        this.total_shizhi = total_shizhi;
    }

    public String getShi_jing_lv() {
        return shi_jing_lv;
    }

    public void setShi_jing_lv(String shi_jing_lv) {
        this.shi_jing_lv = shi_jing_lv;
    }

    public String getTop_price() {
        return top_price;
    }

    public void setTop_price(String top_price) {
        this.top_price = top_price;
    }

    public String getBottom_price() {
        return bottom_price;
    }

    public void setBottom_price(String bottom_price) {
        this.bottom_price = bottom_price;
    }

    public String getLiang_bi() {
        return liang_bi;
    }

    public void setLiang_bi(String liang_bi) {
        this.liang_bi = liang_bi;
    }

    public String getZiduan2() {
        return ziduan2;
    }

    public void setZiduan2(String ziduan2) {
        this.ziduan2 = ziduan2;
    }

    public String getAverage_price() {
        return average_price;
    }

    public void setAverage_price(String average_price) {
        this.average_price = average_price;
    }

    public String getShi_ying_lv_dong() {
        return shi_ying_lv_dong;
    }

    public void setShi_ying_lv_dong(String shi_ying_lv_dong) {
        this.shi_ying_lv_dong = shi_ying_lv_dong;
    }

    public String getShi_ying_lv_jing() {
        return shi_ying_lv_jing;
    }

    public void setShi_ying_lv_jing(String shi_ying_lv_jing) {
        this.shi_ying_lv_jing = shi_ying_lv_jing;
    }
}

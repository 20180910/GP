package com.gp.module.main.bean;

import com.gp.base.BaseGP;

/**
 * Created by Administrator on 2018/6/1.
 */

public class GpBean extends BaseGP {
    public int gp_year          ;  //INTEGER(255) not null,
    public int gp_month         ;  //INTEGER(255) not null,
    public int gp_day           ;  //INTEGER(255) not null,
                                //-- ~ 分割字符串中内容，下标从0开始，依次为
    public String gpstr        ;  //varchar(255) not null,--  0: 未知                        v_sz000858=51
    public String name         ;  //varchar(255) not null,--  1: 名字                        五 粮 液
    public String code         ;  //varchar(255) not null,--  2: 代码                        000858
    public float now_price    ;  //float not null,--  3: 当前价格                    78.98
    public float zuo_price    ;  //float not null,--  4: 昨收                        76.80
    public float jin_price    ;  //float not null,--  5: 今开                        77.79
    public String total_volume ;  //varchar(255) not null,--  6: 成交量（手）                415294
    public String wai_num      ;  //    varchar(255) not null,--  7: 外盘                        217206
    public String nei_num      ;  //varchar(255) not null,--  8: 内盘                        197898

    public float buy_price    ;  //float not null,--  9: 买一                        78.98
    public String buy_volume   ;  //varchar(255) not null,--  10: 买一量（手）               882

    public float buy2_price   ;  //float not null,--  11-18: 买二 买二               78.97
    public String buy2_volume  ;  //varchar(255) not null,--  12: 买二 买二               882

    public float buy3_price   ;  //float not null,--  13: 买3 买3               78.97
    public String buy3_volume  ;  //varchar(255) not null,--  14: 买3 买3               882

    public float buy4_price   ;  //float not null,--  15: 买4 买4               78.97
    public String buy4_volume  ;  //varchar(255) not null,--  16: 买4 买4               882

    public float buy5_price   ;  //float not null,--  17: 买二 买五               78.97
    public String buy5_volume  ;  //varchar(255) not null,--  18: 买二 买五               882

    public float sell_price   ;  //float not null,--  19: 卖一                       78.99
    public String sell_volume  ;  //varchar(255) not null,--  20: 卖一量                      65

    public float sell2_price  ;  //float not null,--  21: 卖2                       78.99
    public String sell2_volume ;  //varchar(255) not null,--  22: 卖2量                      882

    public float sell3_price  ;  //float not null,--  23: 卖3                       78.99
    public String sell3_volume ;  //varchar(255) not null,--  24: 卖3量                      882

    public float sell4_price  ;  //float not null,--  25: 卖4                       78.99
    public String sell4_volume ;  //varchar(255) not null,--  26: 卖4量                      882

    public float sell5_price  ;  //float not null,--  27: 卖5                       78.99
    public String sell5_volume              ;  //varchar(255) not null,--  28: 卖5量                      882


    public String recent_trade          ;  //    varchar(255) not null,--  29: 最近逐笔成交               15:00:03/78.98/7062/S/55771095/9604|14:57:00/78.81/50/B/394042/9515|14:56:57/78.81/172/B/1355276/9513|14:56:54/78.79/275/S/2166827/9511|14:56:51/78.80/147/B/1158333/9509|14:56:48/78.80/384/S/3025949/9507
    public String recent_time           ;  //    varchar(255) not null,--  30: 时间                      20180531150136
    public float change_price           ;          //float not null,--  31: 涨跌                      2.18
    public float change_price_percent ;  //float not null,--  32: 涨跌%                     2.84
    public float max_price            ;  //float not null,--  33: 最高                      79.59
    public float min_price            ;  //float not null,--  34: 最低                      76.85
    public String last_price           ;  //varchar(255) not null,--  35: 价格/成交量（手）/成交额   78.98/415294/3266151334
    public String last_volume_num      ;  //varchar(255) not null,--  36: 成交量（手）               415294
    public String last_volume_price    ;  //varchar(255) not null,--  37: 成交额（万）              326615
    public float huan_shou_lv         ;  //float not null,--  38: 换手率                    1.09
    public float shi_ying_lv_ttm      ;  //float not null,--  39: 市盈率                    27.74
    public String ziduan1              ;  //varchar(255) not null,--  40:
    public float max_price2           ;  //float not null,--  41: 最高                     79.59
    public float min_price2           ;  //float not null,--  42: 最低                     76.85
    public float zhen_fu              ;  //float not null,--  43: 振幅                     3.57
    public float liutong_shizhi       ;  //float not null,--  44: 流通市值                 2997.90
    public float total_shizhi         ;  //float not null,--  45: 总市值                   3065.69
    public float shi_jing_lv          ;  //float not null,--  46: 市净率                   5.14
    public float top_price            ;  //float not null,--  47: 涨停价                   84.48
    public float bottom_price         ;  //float not null,--  48: 跌停价                   69.12
    public float liang_bi             ;  //float not null,--  49: ?                        1.40
    public String ziduan2              ;  //varchar(255) not null,--  50: ?                        -147
    public float average_price        ;  //float not null,--  51: ?                        78.66
    public float shi_ying_lv_dong     ;  //float not null,--  52: ?                        15.42
    public float shi_ying_lv_jing     ;  //float not null--  53: ?                        31.69



}

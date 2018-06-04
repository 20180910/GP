package com.gp.module.main.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gp.base.BaseDaoImp;
import com.gp.database.DBConstant;
import com.gp.database.DBManager;
import com.gp.module.main.bean.GpBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class HomeImp extends BaseDaoImp {


    private boolean deleteAccount(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Code, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete > 0 ? true : false;
    }

    private boolean deleteAccount(List<String> idList) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < idList.size(); i++) {
                db.delete(DBManager.T_Code, DBConstant._id + "=?", new String[]{idList.get(i)});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    public long addGP(String code, String str, String name, String type) {
//        Calendar instance = Calendar.getInstance();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBConstant.uid, System.nanoTime() + "");
        values.put(DBConstant.code, code);


        values.put(DBConstant.gpstr, str);
        values.put(DBConstant.name, name);

//        values.put(DBConstant.gp_year,instance.get(Calendar.YEAR));
//        values.put(DBConstant.gp_month,instance.get(Calendar.MONTH)+1);
//        values.put(DBConstant.gp_day,instance.get(Calendar.DAY_OF_MONTH));

        values.put(DBConstant.update_time, new Date().getTime());
        values.put(DBConstant.create_time, new Date().getTime());
        values.put(DBConstant.type, type);
        values.put(DBConstant.status, "0");

        long insert = db.insert(DBManager.T_Code, null, values);
        db.close();
        return insert;
    }

    public List<GpBean> selectGpBean(int page, boolean selectAll) {
        return selectGpBean(page, null, false, getWritableDatabase(), selectAll);
    }

    public List<GpBean> selectGpBean(int page, String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db, boolean selectAll) {
        String orderBy = DBConstant.update_time + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.create_time + " desc";
        }
        StringBuffer searchSql = null;
        String[] searchStr = new String[4];
        if (!TextUtils.isEmpty(searchInfo)) {
           /* searchSql=new StringBuffer();
            searchSql.append(DBConstant.dataSource+" like ? or ");
            searchSql.append(DBConstant.dataAccount+" like ? or ");
            searchSql.append(DBConstant.dataPassword+" like ? or ");
            searchSql.append(DBConstant.dataRemark+" like ? ");
            searchStr[0]="%"+searchInfo+"%";
            searchStr[1]="%"+searchInfo+"%";
            searchStr[2]="%"+searchInfo+"%";
            searchStr[3]="%"+searchInfo+"%";*/
        }
        String limit = getLimit(page);
        Cursor query = db.query(DBManager.T_Code,
                new String[]{
                        DBConstant._id,
                        DBConstant.uid,
                        DBConstant.create_time,
                        DBConstant.update_time,
                        DBConstant.gpstr,
                        DBConstant.name,
                        DBConstant.code,
                        DBConstant.type,
                        DBConstant.status
                }, searchSql != null ? searchSql.toString() : null, searchSql != null ? searchStr : null, null, null, orderBy, selectAll ? null : limit);
        List<GpBean> list = new ArrayList<GpBean>();
        GpBean bean;
        while (query.moveToNext()) {
            bean = new GpBean();
            String id = query.getString(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            long create_time = query.getLong(query.getColumnIndex(DBConstant.create_time));
            long update_time = query.getLong(query.getColumnIndex(DBConstant.update_time));
            String gpstr = query.getString(query.getColumnIndex(DBConstant.gpstr));
            String name = query.getString(query.getColumnIndex(DBConstant.name));
            String code = query.getString(query.getColumnIndex(DBConstant.code));
            int type = query.getInt(query.getColumnIndex(DBConstant.type));
            int status = query.getInt(query.getColumnIndex(DBConstant.status));

//            long updateTime = string2Date(query.getString(query.getColumnIndex(DBConstant.updateTime)));
//            long creatTime = string2Date(query.getString(query.getColumnIndex(DBConstant.createTime)));
            bean._id = id;
            bean.uid = uid;
            bean.create_time = create_time;
            bean.update_time = update_time;
            bean.gpstr = gpstr;
            bean.name = name;
            bean.code = code;
            bean.type = type;
            bean.status = status;

            list.add(bean);
        }
        db.close();
        return list;
    }

    public long updateGpBean(GpBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(DBConstant._id,  bean._id) ;
//        values.put(DBConstant.uid,  bean.uid) ;
//        values.put(DBConstant.create_time,  bean.create_time );
//        values.put(DBConstant.update_time,  bean.update_time );
        values.put(DBConstant.gpstr, bean.gpstr);
        values.put(DBConstant.name, bean.name);
//        values.put(DBConstant.code,  bean.code);
//        values.put(DBConstant.type,  bean.type);
        long insert = db.update(DBManager.T_Code, values, DBConstant.code + " =? ", new String[]{bean.code + ""});
        db.close();
        return insert;
    }

    public int deleteTableCode() {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Code, null, null);
        db.close();
        Log("==delete==" + delete);
        return delete;
    }

    public long addDataToTable(GpBean bean, String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(DBConstant._id             ,bean._id             );
        values.put(DBConstant.uid, bean.uid + "");
        values.put(DBConstant.gp_uid, bean.gp_uid + "");
        values.put(DBConstant.create_time, bean.create_time + "");
        values.put(DBConstant.update_time, bean.update_time + "");
        values.put(DBConstant.type, bean.type + "");
        values.put(DBConstant.gp_year, bean.gp_year + "");
        values.put(DBConstant.gp_month, bean.gp_month + "");
        values.put(DBConstant.gp_day, bean.gp_day + "");
        values.put(DBConstant.gpstr, bean.gpstr + "");
        values.put(DBConstant.name, bean.name + "");
        values.put(DBConstant.code, bean.code + "");
        values.put(DBConstant.now_price, bean.now_price + "");
        values.put(DBConstant.zuo_price, bean.zuo_price + "");
        values.put(DBConstant.jin_price, bean.jin_price + "");
        values.put(DBConstant.total_volume, bean.total_volume + "");
        values.put(DBConstant.wai_num, bean.wai_num + "");
        values.put(DBConstant.nei_num, bean.nei_num + "");
        values.put(DBConstant.buy_price, bean.buy_price + "");
        values.put(DBConstant.buy_volume, bean.buy_volume + "");
        values.put(DBConstant.buy2_price, bean.buy2_price + "");
        values.put(DBConstant.buy2_volume, bean.buy2_volume + "");
        values.put(DBConstant.buy3_price, bean.buy3_price + "");
        values.put(DBConstant.buy3_volume, bean.buy3_volume + "");
        values.put(DBConstant.buy4_price, bean.buy4_price + "");
        values.put(DBConstant.buy4_volume, bean.buy4_volume + "");
        values.put(DBConstant.buy5_price, bean.buy5_price + "");
        values.put(DBConstant.buy5_volume, bean.buy5_volume + "");
        values.put(DBConstant.sell_price, bean.sell_price + "");
        values.put(DBConstant.sell_volume, bean.sell_volume + "");
        values.put(DBConstant.sell2_price, bean.sell2_price + "");
        values.put(DBConstant.sell2_volume, bean.sell2_volume + "" + "");
        values.put(DBConstant.sell3_price, bean.sell3_price + "");
        values.put(DBConstant.sell3_volume, bean.sell3_volume + "");
        values.put(DBConstant.sell4_price, bean.sell4_price + "");
        values.put(DBConstant.sell4_volume, bean.sell4_volume + "");
        values.put(DBConstant.sell5_price, bean.sell5_price + "");
        values.put(DBConstant.sell5_volume, bean.sell5_volume + "");
        values.put(DBConstant.recent_trade, bean.recent_trade + "");
        values.put(DBConstant.recent_time, bean.recent_time + "");
        values.put(DBConstant.change_price, bean.change_price + "");
        values.put(DBConstant.change_price_percent, bean.change_price_percent + "");
        values.put(DBConstant.max_price, bean.max_price + "");
        values.put(DBConstant.min_price, bean.min_price + "");
        values.put(DBConstant.last_price, bean.last_price + "");
        values.put(DBConstant.last_volume_num, bean.last_volume_num + "");
        values.put(DBConstant.last_volume_price, bean.last_volume_price + "");
        values.put(DBConstant.huan_shou_lv, bean.huan_shou_lv + "");
        values.put(DBConstant.shi_ying_lv_ttm, bean.shi_ying_lv_ttm + "");
        values.put(DBConstant.ziduan1, bean.ziduan1 + "");
        values.put(DBConstant.max_price2, bean.max_price2 + "");
        values.put(DBConstant.min_price2, bean.min_price2 + "");
        values.put(DBConstant.zhen_fu, bean.zhen_fu + "");
        values.put(DBConstant.liutong_shizhi, bean.liutong_shizhi + "");
        values.put(DBConstant.total_shizhi, bean.total_shizhi + "");
        values.put(DBConstant.shi_jing_lv, bean.shi_jing_lv + "");
        values.put(DBConstant.top_price, bean.top_price + "");
        values.put(DBConstant.bottom_price, bean.bottom_price + "");
        values.put(DBConstant.liang_bi, bean.liang_bi + "");
        values.put(DBConstant.ziduan2, bean.ziduan2 + "");
        values.put(DBConstant.average_price, bean.average_price + "");
        values.put(DBConstant.shi_ying_lv_dong, bean.shi_ying_lv_dong + "");
        values.put(DBConstant.shi_ying_lv_jing, bean.shi_ying_lv_jing + "");


        long insert = db.insert(tableName, null, values);
        db.close();
        return insert;
    }

    public List<GpBean> selectEveryDay(int page, String searchInfo, boolean isUp) {
        String orderBy = DBConstant.change_price_percent + " asc";
        if (isUp) {
            orderBy = DBConstant.change_price_percent + " desc";
        }
        StringBuffer searchSql = null;
        String[] searchStr = new String[2];
        if (!TextUtils.isEmpty(searchInfo)) {
            searchSql = new StringBuffer();
            searchSql.append(DBConstant.name + " like ? or ");
            searchSql.append(DBConstant.code + " like ? ");
            searchStr[0] = "%" + searchInfo + "%";
            searchStr[1] = "%" + searchInfo + "%";
        }
        String limit = getLimit(page);
        SQLiteDatabase db = getWritableDatabase();
        Cursor query = db.query(DBManager.T_Everyday,
                new String[]{
                        DBConstant._id,
                        DBConstant.uid,
                        DBConstant.gp_uid,
                        DBConstant.create_time,
                        DBConstant.update_time,
                        DBConstant.type,
                        DBConstant.status,
                        DBConstant.gp_year,
                        DBConstant.gp_month,
                        DBConstant.gp_day,
                        DBConstant.gpstr,
                        DBConstant.name,
                        DBConstant.code,
                        DBConstant.now_price,
                        DBConstant.zuo_price,
                        DBConstant.jin_price,
                        DBConstant.total_volume,
                        DBConstant.wai_num,
                        DBConstant.nei_num,
                        DBConstant.buy_price,
                        DBConstant.buy_volume,
                        DBConstant.buy2_price,
                        DBConstant.buy2_volume,
                        DBConstant.buy3_price,
                        DBConstant.buy3_volume,
                        DBConstant.buy4_price,
                        DBConstant.buy4_volume,
                        DBConstant.buy5_price,
                        DBConstant.buy5_volume,
                        DBConstant.sell_price,
                        DBConstant.sell_volume,
                        DBConstant.sell2_price,
                        DBConstant.sell2_volume,
                        DBConstant.sell3_price,
                        DBConstant.sell3_volume,
                        DBConstant.sell4_price,
                        DBConstant.sell4_volume,
                        DBConstant.sell5_price,
                        DBConstant.sell5_volume,
                        DBConstant.recent_trade,
                        DBConstant.recent_time,
                        DBConstant.change_price,
                        DBConstant.change_price_percent,
                        DBConstant.max_price,
                        DBConstant.min_price,
                        DBConstant.last_price,
                        DBConstant.last_volume_num,
                        DBConstant.last_volume_price,
                        DBConstant.huan_shou_lv,
                        DBConstant.shi_ying_lv_ttm,
                        DBConstant.ziduan1,
                        DBConstant.max_price2,
                        DBConstant.min_price2,
                        DBConstant.zhen_fu,
                        DBConstant.liutong_shizhi,
                        DBConstant.total_shizhi,
                        DBConstant.shi_jing_lv,
                        DBConstant.top_price,
                        DBConstant.bottom_price,
                        DBConstant.liang_bi,
                        DBConstant.ziduan2,
                        DBConstant.average_price,
                        DBConstant.shi_ying_lv_dong,
                        DBConstant.shi_ying_lv_jing
                }, searchSql != null ? searchSql.toString() : null, searchSql != null ? searchStr : null, null, null, orderBy, limit);
        List<GpBean> list = new ArrayList<GpBean>();
        GpBean bean;
        while (query.moveToNext()) {
            bean = new GpBean();
            String _id = query.getString(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            String gp_uid = query.getString(query.getColumnIndex(DBConstant.gp_uid));
            long create_time = query.getLong(query.getColumnIndex(DBConstant.create_time));
            long update_time = query.getLong(query.getColumnIndex(DBConstant.update_time));
            int type = query.getInt(query.getColumnIndex(DBConstant.type));
            int status = query.getInt(query.getColumnIndex(DBConstant.status));
            int gp_year = query.getInt(query.getColumnIndex(DBConstant.gp_year));
            int gp_month = query.getInt(query.getColumnIndex(DBConstant.gp_month));
            int gp_day = query.getInt(query.getColumnIndex(DBConstant.gp_day));
            String gpstr = query.getString(query.getColumnIndex(DBConstant.gpstr));
            String name = query.getString(query.getColumnIndex(DBConstant.name));
            String code = query.getString(query.getColumnIndex(DBConstant.code));
            float now_price = query.getFloat(query.getColumnIndex(DBConstant.now_price));
            float zuo_price = query.getFloat(query.getColumnIndex(DBConstant.zuo_price));
            float jin_price = query.getFloat(query.getColumnIndex(DBConstant.jin_price));
            String total_volume = query.getString(query.getColumnIndex(DBConstant.total_volume));
            String wai_num = query.getString(query.getColumnIndex(DBConstant.wai_num));
            String nei_num = query.getString(query.getColumnIndex(DBConstant.nei_num));
            float buy_price = query.getFloat(query.getColumnIndex(DBConstant.buy_price));
            String buy_volume = query.getString(query.getColumnIndex(DBConstant.buy_volume));
            float buy2_price = query.getFloat(query.getColumnIndex(DBConstant.buy2_price));
            String buy2_volume = query.getString(query.getColumnIndex(DBConstant.buy2_volume));
            float buy3_price = query.getFloat(query.getColumnIndex(DBConstant.buy3_price));
            String buy3_volume = query.getString(query.getColumnIndex(DBConstant.buy3_volume));
            float buy4_price = query.getFloat(query.getColumnIndex(DBConstant.buy4_price));
            String buy4_volume = query.getString(query.getColumnIndex(DBConstant.buy4_volume));
            float buy5_price = query.getFloat(query.getColumnIndex(DBConstant.buy5_price));
            String buy5_volume = query.getString(query.getColumnIndex(DBConstant.buy5_volume));
            float sell_price = query.getFloat(query.getColumnIndex(DBConstant.sell_price));
            String sell_volume = query.getString(query.getColumnIndex(DBConstant.sell_volume));
            float sell2_price = query.getFloat(query.getColumnIndex(DBConstant.sell2_price));
            String sell2_volume = query.getString(query.getColumnIndex(DBConstant.sell2_volume));
            float sell3_price = query.getFloat(query.getColumnIndex(DBConstant.sell3_price));
            String sell3_volume = query.getString(query.getColumnIndex(DBConstant.sell3_volume));
            float sell4_price = query.getFloat(query.getColumnIndex(DBConstant.sell4_price));
            String sell4_volume = query.getString(query.getColumnIndex(DBConstant.sell4_volume));
            float sell5_price = query.getFloat(query.getColumnIndex(DBConstant.sell5_price));
            String sell5_volume = query.getString(query.getColumnIndex(DBConstant.sell5_volume));
            String recent_trade = query.getString(query.getColumnIndex(DBConstant.recent_trade));
            String recent_time = query.getString(query.getColumnIndex(DBConstant.recent_time));
            float change_price = query.getFloat(query.getColumnIndex(DBConstant.change_price));
            float change_price_percent = query.getFloat(query.getColumnIndex(DBConstant.change_price_percent));
            float max_price = query.getFloat(query.getColumnIndex(DBConstant.max_price));
            float min_price = query.getFloat(query.getColumnIndex(DBConstant.min_price));
            String last_price = query.getString(query.getColumnIndex(DBConstant.last_price));
            String last_volume_num = query.getString(query.getColumnIndex(DBConstant.last_volume_num));
            String last_volume_price = query.getString(query.getColumnIndex(DBConstant.last_volume_price));
            float huan_shou_lv = query.getFloat(query.getColumnIndex(DBConstant.huan_shou_lv));
            float shi_ying_lv_ttm = query.getFloat(query.getColumnIndex(DBConstant.shi_ying_lv_ttm));
            String ziduan1 = query.getString(query.getColumnIndex(DBConstant.ziduan1));
            float max_price2 = query.getFloat(query.getColumnIndex(DBConstant.max_price2));
            float min_price2 = query.getFloat(query.getColumnIndex(DBConstant.min_price2));
            float zhen_fu = query.getFloat(query.getColumnIndex(DBConstant.zhen_fu));
            float liutong_shizhi = query.getFloat(query.getColumnIndex(DBConstant.liutong_shizhi));
            float total_shizhi = query.getFloat(query.getColumnIndex(DBConstant.total_shizhi));
            float shi_jing_lv = query.getFloat(query.getColumnIndex(DBConstant.shi_jing_lv));
            float top_price = query.getFloat(query.getColumnIndex(DBConstant.top_price));
            float bottom_price = query.getFloat(query.getColumnIndex(DBConstant.bottom_price));
            float liang_bi = query.getFloat(query.getColumnIndex(DBConstant.liang_bi));
            String ziduan2 = query.getString(query.getColumnIndex(DBConstant.ziduan2));
            float average_price = query.getFloat(query.getColumnIndex(DBConstant.average_price));
            float shi_ying_lv_dong = query.getFloat(query.getColumnIndex(DBConstant.shi_ying_lv_dong));
            float shi_ying_lv_jing = query.getFloat(query.getColumnIndex(DBConstant.shi_ying_lv_jing));

//            long updateTime = string2Date(query.getString(query.getColumnIndex(DBConstant.updateTime)));
//            long creatTime = string2Date(query.getString(query.getColumnIndex(DBConstant.createTime)));


            bean._id = _id;
            bean.uid = uid;
            bean.gp_uid = gp_uid;
            bean.create_time = create_time;
            bean.update_time = update_time;
            bean.type = type;
            bean.status = status;
            bean.gp_year = gp_year;
            bean.gp_month = gp_month;
            bean.gp_day = gp_day;
            bean.gpstr = gpstr;
            bean.name = name;
            bean.code = code;
            bean.now_price = now_price;
            bean.zuo_price = zuo_price;
            bean.jin_price = jin_price;
            bean.total_volume = total_volume;
            bean.wai_num = wai_num;
            bean.nei_num = nei_num;
            bean.buy_price = buy_price;
            bean.buy_volume = buy_volume;
            bean.buy2_price = buy2_price;
            bean.buy2_volume = buy2_volume;
            bean.buy3_price = buy3_price;
            bean.buy3_volume = buy3_volume;
            bean.buy4_price = buy4_price;
            bean.buy4_volume = buy4_volume;
            bean.buy5_price = buy5_price;
            bean.buy5_volume = buy5_volume;
            bean.sell_price = sell_price;
            bean.sell_volume = sell_volume;
            bean.sell2_price = sell2_price;
            bean.sell2_volume = sell2_volume;
            bean.sell3_price = sell3_price;
            bean.sell3_volume = sell3_volume;
            bean.sell4_price = sell4_price;
            bean.sell4_volume = sell4_volume;
            bean.sell5_price = sell5_price;
            bean.sell5_volume = sell5_volume;
            bean.recent_trade = recent_trade;
            bean.recent_time = recent_time;
            bean.change_price = change_price;
            bean.change_price_percent = change_price_percent;
            bean.max_price = max_price;
            bean.min_price = min_price;
            bean.last_price = last_price;
            bean.last_volume_num = last_volume_num;
            bean.last_volume_price = last_volume_price;
            bean.huan_shou_lv = huan_shou_lv;
            bean.shi_ying_lv_ttm = shi_ying_lv_ttm;
            bean.ziduan1 = ziduan1;
            bean.max_price2 = max_price2;
            bean.min_price2 = min_price2;
            bean.zhen_fu = zhen_fu;
            bean.liutong_shizhi = liutong_shizhi;
            bean.total_shizhi = total_shizhi;
            bean.shi_jing_lv = shi_jing_lv;
            bean.top_price = top_price;
            bean.bottom_price = bottom_price;
            bean.liang_bi = liang_bi;
            bean.ziduan2 = ziduan2;
            bean.average_price = average_price;
            bean.shi_ying_lv_dong = shi_ying_lv_dong;
            bean.shi_ying_lv_jing = shi_ying_lv_jing;

            list.add(bean);
        }
        db.close();
        return list;
    }

    public long joinZiXuan(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.status, "1");
        long insert = db.update(DBManager.T_Code, values, DBConstant.uid + " =? ", new String[]{uid});
        db.close();
        return insert;
    }
    public long removeZiXuan(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.status, "0");
        long insert = db.update(DBManager.T_Code, values, DBConstant.uid + " =? ", new String[]{uid});
        db.close();
        return insert;
    }
    public boolean isZiXuan(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        boolean scale=false;
        Cursor query = db.query(DBManager.T_Code,
                new String[]{
                        DBConstant.status
                }, " uid=? ", new String[]{uid}, null, null, null, null );
        while (query.moveToNext()) {
            int status = query.getInt(query.getColumnIndex(DBConstant.status));
            if(status==1){
                scale=true;
            }
        }
        db.close();
        return scale;
    }
}

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

    public long addGP(String code,String type) {
//        Calendar instance = Calendar.getInstance();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBConstant.uid,System.nanoTime()+"");
        values.put(DBConstant.code,code);

//        values.put(DBConstant.gp_year,instance.get(Calendar.YEAR));
//        values.put(DBConstant.gp_month,instance.get(Calendar.MONTH)+1);
//        values.put(DBConstant.gp_day,instance.get(Calendar.DAY_OF_MONTH));

        values.put(DBConstant.update_time,new Date().getTime());
        values.put(DBConstant.create_time,new Date().getTime());
        values.put(DBConstant.type,type);
        values.put(DBConstant.status,"0");

        long insert = db.insert(DBManager.T_Code, null, values);
        db.close();
        return insert;
    }

    public List<GpBean> selectGpBean(int page,boolean selectAll) {
        return selectGpBean(page,null, false,getWritableDatabase(),selectAll);
    }
    public List<GpBean> selectGpBean(int page, String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db,boolean selectAll) {
        String orderBy = DBConstant.update_time + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.create_time + " desc";
        }
        StringBuffer searchSql=null;
        String[]searchStr=new String[4];
        if(!TextUtils.isEmpty(searchInfo)){
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
        String limit=getLimit(page);
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
                        }, searchSql!=null?searchSql.toString():null,searchSql!=null?searchStr:null, null, null, orderBy,selectAll?null:limit);
        List<GpBean> list = new ArrayList<GpBean>();
        GpBean bean;
        while (query.moveToNext()) {
            bean = new GpBean();
            String id = query.getString(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            long create_time = query.getLong(query.getColumnIndex(DBConstant.create_time));
            long update_time = query.getLong(query.getColumnIndex(DBConstant.update_time));
            String gpstr = query.getString(query.getColumnIndex(DBConstant.gpstr));
            String  name= query.getString(query.getColumnIndex(DBConstant.name));
            String code = query.getString(query.getColumnIndex(DBConstant.code));
            int type = query.getInt(query.getColumnIndex(DBConstant.type));
            int status = query.getInt(query.getColumnIndex(DBConstant.status));

//            long updateTime = string2Date(query.getString(query.getColumnIndex(DBConstant.updateTime)));
//            long creatTime = string2Date(query.getString(query.getColumnIndex(DBConstant.createTime)));
            bean._id=id;
            bean.uid=uid;
            bean.create_time=create_time;
            bean.update_time=update_time;
            bean.gpstr=gpstr;
            bean.name=name;
            bean.code=code;
            bean.type=type;
            bean.status=status;

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
        values.put(DBConstant.gpstr,  bean.gpstr );
        values.put(DBConstant.name,  bean.name );
//        values.put(DBConstant.code,  bean.code);
//        values.put(DBConstant.type,  bean.type);
        long insert = db.update(DBManager.T_Code, values, DBConstant.code + " =? ", new String[]{bean.code + ""});
        db.close();
        return insert;
    }

    public int deleteTableCode() {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Code,null,null);
        db.close();
        Log("==delete=="+delete);
        return delete;
    }

    public long addDataToTable(GpBean bean,String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(DBConstant._id             ,bean._id             );
        values.put(DBConstant.uid              ,bean.uid             +"");
        values.put(DBConstant.gp_uid           ,bean.gp_uid          +"");
        values.put(DBConstant.create_time     ,bean.create_time     +"");
        values.put(DBConstant.update_time     ,bean.update_time     +"");
        values.put(DBConstant.type             ,bean.type            +"");
        values.put(DBConstant.gp_year          ,bean.gp_year         +"");
        values.put(DBConstant.gp_month        ,bean.gp_month        +"");
        values.put(DBConstant.gp_day          ,bean.gp_day          +"");
        values.put(DBConstant.gpstr          ,bean.gpstr          +"");
        values.put(DBConstant.name           ,bean.name           +"");
        values.put(DBConstant.code           ,bean.code           +"");
        values.put(DBConstant.now_price      ,bean.now_price      +"");
        values.put(DBConstant.zuo_price      ,bean.zuo_price      +"");
        values.put(DBConstant.jin_price      ,bean.jin_price      +"");
        values.put(DBConstant.total_volume   ,bean.total_volume   +"");
        values.put(DBConstant.wai_num        ,bean.wai_num        +"");
        values.put(DBConstant.nei_num        ,bean.nei_num        +"");
        values.put(DBConstant.buy_price      ,bean.buy_price      +"");
        values.put(DBConstant.buy_volume     ,bean.buy_volume     +"");
        values.put(DBConstant.buy2_price     ,bean.buy2_price     +"");
        values.put(DBConstant.buy2_volume    ,bean.buy2_volume    +"");
        values.put(DBConstant.buy3_price     ,bean.buy3_price     +"");
        values.put(DBConstant.buy3_volume    ,bean.buy3_volume    +"");
        values.put(DBConstant.buy4_price     ,bean.buy4_price     +"");
        values.put(DBConstant.buy4_volume    ,bean.buy4_volume    +"");
        values.put(DBConstant.buy5_price     ,bean.buy5_price     +"");
        values.put(DBConstant.buy5_volume    ,bean.buy5_volume    +"");
        values.put(DBConstant.sell_price     ,bean.sell_price     +"");
        values.put(DBConstant.sell_volume    ,bean.sell_volume    +"");
        values.put(DBConstant.sell2_price    ,bean.sell2_price    +"");
        values.put(DBConstant.sell2_volume   ,bean.sell2_volume+""   +"");
        values.put(DBConstant.sell3_price    ,bean.sell3_price    +"");
        values.put(DBConstant.sell3_volume   ,bean.sell3_volume   +"");
        values.put(DBConstant.sell4_price    ,bean.sell4_price    +"");
        values.put(DBConstant.sell4_volume   ,bean.sell4_volume   +"");
        values.put(DBConstant.sell5_price    ,bean.sell5_price    +"");
        values.put(DBConstant.sell5_volume   ,bean.sell5_volume   +"");
        values.put(DBConstant.recent_trade   ,bean.recent_trade   +"");
        values.put(DBConstant.recent_time    ,bean.recent_time    +"");
        values.put(DBConstant.change_price   ,bean.change_price   +"");
        values.put(DBConstant.change_price_percent ,bean.change_price_percent +"");
        values.put(DBConstant.max_price            ,bean.max_price            +"");
        values.put(DBConstant.min_price            ,bean.min_price            +"");
        values.put(DBConstant.last_price           ,bean.last_price           +"");
        values.put(DBConstant.last_volume_num      ,bean.last_volume_num      +"");
        values.put(DBConstant.last_volume_price    ,bean.last_volume_price    +"");
        values.put(DBConstant.huan_shou_lv         ,bean.huan_shou_lv         +"");
        values.put(DBConstant.shi_ying_lv_ttm      ,bean.shi_ying_lv_ttm      +"");
        values.put(DBConstant.ziduan1              ,bean.ziduan1              +"");
        values.put(DBConstant.max_price2           ,bean.max_price2           +"");
        values.put(DBConstant.min_price2           ,bean.min_price2           +"");
        values.put(DBConstant.zhen_fu              ,bean.zhen_fu              +"");
        values.put(DBConstant.liutong_shizhi       ,bean.liutong_shizhi       +"");
        values.put(DBConstant.total_shizhi         ,bean.total_shizhi         +"");
        values.put(DBConstant.shi_jing_lv          ,bean.shi_jing_lv          +"");
        values.put(DBConstant.top_price            ,bean.top_price            +"");
        values.put(DBConstant.bottom_price         ,bean.bottom_price         +"");
        values.put(DBConstant.liang_bi             ,bean.liang_bi             +"");
        values.put(DBConstant.ziduan2              ,bean.ziduan2              +"");
        values.put(DBConstant.average_price        ,bean.average_price        +"");
        values.put(DBConstant.shi_ying_lv_dong     ,bean.shi_ying_lv_dong     +"");
        values.put(DBConstant.shi_ying_lv_jing     ,bean.shi_ying_lv_jing     +"");


        long insert = db.insert(tableName, null, values);
        db.close();
        return insert;
    }
}

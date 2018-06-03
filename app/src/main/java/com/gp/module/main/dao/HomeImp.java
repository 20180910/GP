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
    public boolean deleteAccount(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Code, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete > 0 ? true : false;
    }

    public boolean deleteAccount(List<String> idList) {
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
                        DBConstant.type
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
}

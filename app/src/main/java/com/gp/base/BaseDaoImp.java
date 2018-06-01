package com.gp.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.github.androidtools.DateUtils;
import com.gp.database.DBConstant;
import com.gp.database.DBManager;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/25.
 */

public abstract class BaseDaoImp {
    protected String TAG=this.getClass().getSimpleName();
    protected Context mContext;
    public void setContext(Context context){
        mContext=context;
    }
    public String getLimitSql(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return "";
        }
        String limit=String.format(" limit "+ DBManager.pageSize+" offset %d ",DBManager.pageSize*(page-1));
        return limit;
    }
    public String getLimit(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return null;
        }
        return DBManager.pageSize*(page-1)+","+DBManager.pageSize;
    }

    public SQLiteDatabase getWritableDatabase(){
        return DBManager.getNewInstance(mContext).getWritableDatabase();
    }


    public long string2Date(String s){
        Date date = DateUtils.stringToDate(s,"yyy-MM-dd HH:mm:ss");
        return date.getTime();
    }

    public int selectTableCount(String tableName,String uid) {
        String sql="select count(0) as num from " + tableName;
        if(!TextUtils.isEmpty(uid)&&uid.trim().length()>0){
            sql="select count(0) as num from " + tableName+" where "+ DBConstant.uid+"=?";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,uid==null?null:new String[]{uid});
        int count = 0;
        try {
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("num"));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            cursor.close();
            db.close();
            return -1;
        }
        return count;
    }
    public int selectTableCount(String tableName) {
        return selectTableCount(tableName,null);
    }
}

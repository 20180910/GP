package com.gp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gp.Constant;
import com.gp.R;
import com.gp.tools.StreamUtils;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBManager extends SQLiteOpenHelper {
    private static final String dbName = "MyGP";

    public String getDBName() {
        return dbName;
    }
    private Context mContext;
    private static final int version = 1;
    public static final String T_Code = "T_Code";
    public static final String T_Everyday = "T_Everyday";
    public static final String T_EveryMinute = "T_EveryMinute";
    public static final int pageSize= Constant.pageSize;
    private String getLimitSql(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return "";
        }
        String limit=String.format(" limit "+pageSize+" offset %d ",pageSize*(page-1));
        return limit;
    }
    private String getLimit(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return null;
        }
        return pageSize*(page-1)+","+pageSize;
    }
    private DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    public static DBManager getNewInstance(Context context) {
        return new DBManager(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addDataTable(db);
    }

    private void addDataTable(SQLiteDatabase db, String table) {
        if (noExistTable(db, table)) {
            db.execSQL(table);
        }
    }
    private void addDataTable(SQLiteDatabase db) {
        if (noExistTable(db, T_Code)) {
            db.execSQL(getSql(T_Code));
        }
        if (noExistTable(db, T_Everyday)) {
            db.execSQL(getSql(T_Everyday));
        }
        if (noExistTable(db, T_EveryMinute)) {
            db.execSQL(getSql(T_EveryMinute));
        }
    }
    private String getSql(String table){
        String sql="";
        switch (table){
            case T_Code:
                sql = StreamUtils.get(mContext, R.raw.t_code);
            break;
            case T_Everyday:
                sql = StreamUtils.get(mContext, R.raw.t_everyday);
            break;
            case T_EveryMinute:
                sql = StreamUtils.get(mContext, R.raw.t_everyminute);
            break;
        }
        return sql;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean dropTable(SQLiteDatabase db, String table) {
        try {
            db.execSQL("drop table " + table);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean existTable(SQLiteDatabase db, String table) {
        boolean exits = false;
        String sql = "select * from sqlite_master where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{table});
        if (cursor.getCount() != 0) {
            exits = true;
        }
        cursor.close();
        return exits;
    }

    private boolean noExistTable(SQLiteDatabase db, String table) {
        boolean exits = true;
        String sql = "select * from sqlite_master where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{table});
        if (cursor.getCount() != 0) {
            exits = false;
        }
        cursor.close();
        return exits;
    }


}

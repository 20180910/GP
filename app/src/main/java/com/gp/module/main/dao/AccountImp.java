package com.gp.module.main.dao;

import android.database.sqlite.SQLiteDatabase;

import com.gp.base.BaseDaoImp;
import com.gp.database.DBConstant;
import com.gp.database.DBManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class AccountImp extends BaseDaoImp {
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
}

package com.gp.base;

/**
 * Created by Administrator on 2018/6/1.
 */

public class BaseGP {
    protected String _id;
    protected String uid;
    protected String gp_uid;
    protected String create_time;
    protected String update_time;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGp_uid() {
        return gp_uid;
    }

    public void setGp_uid(String gp_uid) {
        this.gp_uid = gp_uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}

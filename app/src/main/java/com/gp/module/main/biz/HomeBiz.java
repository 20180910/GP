package com.gp.module.main.biz;

import com.github.androidtools.DateUtils;
import com.github.androidtools.SPUtils;
import com.gp.AppXml;
import com.gp.base.BaseBiz;
import com.gp.tools.CalendarUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by windows10 on 2018/6/5.
 */

public class HomeBiz extends BaseBiz{
    public void a(HomeInter.FirstAddDataInter inter){
        boolean isFirstIntoApp = SPUtils.getBoolean(mContext, AppXml.isFirstIntoApp, true);
        if (isFirstIntoApp) {
            inter.firstIntoApp();
            SPUtils.setPrefBoolean(mContext, AppXml.isFirstIntoApp, false);
        } else {
            //收盘之后添加当天数据
            int hour = CalendarUtil.get(Calendar.HOUR_OF_DAY);
            int minute = CalendarUtil.get(Calendar.MINUTE);
            String isSaveTodayData = SPUtils.getString(mContext, AppXml.isSaveTodayData, "");
            String dateToString = DateUtils.dateToString(new Date());
            if (!dateToString.equals(isSaveTodayData)) {
                if (hour > 15 || (hour == 15 && minute > 2)) {
                    inter.kaiPan();
                } else {
                    inter.shouPan();
                }
            } else {
                inter.other();
            }
        }
    }
}

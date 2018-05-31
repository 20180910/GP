package com.gp.base;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.androidtools.PhoneUtils;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ImageSizeUtils {
    public static final int imgWidth=750;
    public static final int imgHeight=400;

    public static LinearLayout.LayoutParams getImageSizeLayoutParams(Context mContext,int width,int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width= PhoneUtils.getScreenWidth(mContext);
        layoutParams.height= PhoneUtils.getScreenWidth(mContext)*height/width;
        return layoutParams;
    }
    public static LinearLayout.LayoutParams getImageSizeLayoutParams(Context mContext){
        return getImageSizeLayoutParams(mContext,imgWidth,imgHeight);
    }

    public static FrameLayout.LayoutParams getImageSizeFrameLayoutParams(Context mContext, int width, int height){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width= PhoneUtils.getScreenWidth(mContext);
        layoutParams.height= PhoneUtils.getScreenWidth(mContext)*height/width;
        return layoutParams;
    }
    public static FrameLayout.LayoutParams getImageSizeFrameLayoutParams(Context mContext){
        return getImageSizeFrameLayoutParams(mContext,imgWidth,imgHeight);
    }
}

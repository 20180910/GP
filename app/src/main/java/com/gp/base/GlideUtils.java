package com.gp.base;

/**
 * Created by Administrator on 2018/3/21.
 */

public class GlideUtils {
   /* public static void intoSimple(Context mContext, String url, ImageView imageView){
        if(isGifUrl(url)){
            Glide.with(mContext).load(url).into(imageView);
        }else{
            Glide.with(mContext).load(url).asBitmap().encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,90)).into(imageView);
        }
    }
    public static void into(Context mContext,String url, ImageView imageView){
        if(isGifUrl(url)){
            Glide.with(mContext).load(url).error(R.color.c_press).into(imageView);
        }else{
            Glide.with(mContext).load(url).asBitmap().encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,90)).error(R.color.c_press).into(imageView);
        }
    }
    public static void into(Context mContext,String url, ImageView imageView,@ColorRes int color){
        if(isGifUrl(url)){
            Glide.with(mContext).load(url).error(color).into(imageView);
        }else{
            Glide.with(mContext).load(url).asBitmap().encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,90)).error(color).into(imageView);
        }
    }
    public static void intoD(Context mContext,String url, ImageView imageView,@DrawableRes int drawable){
        if(isGifUrl(url)){
            Glide.with(mContext).load(url).error(drawable).into(imageView);
        }else{
            Glide.with(mContext).load(url).asBitmap().encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,90)).error(drawable).into(imageView);
        }
    }
    public static boolean isGifUrl(String url){
        if(TextUtils.isEmpty(url)){
            return false;
        }
        String imgUrl=url.toLowerCase();
        int index = imgUrl.lastIndexOf(".gif");
        if(index==-1){
            return false;
        }
        index+=4;
        return (index==url.length());
    }*/
}

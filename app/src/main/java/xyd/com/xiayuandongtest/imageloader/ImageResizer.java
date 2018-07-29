package xyd.com.xiayuandongtest.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResizer {

    public static final String TAG = "ImageResizer";
    public ImageResizer() {
    }


    /**
     * 压缩资源文件的图片
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSammleBitmapFromResource(Resources res, int resId, int reqWidth , int reqHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);

        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);

    }
    public Bitmap decodeSammleBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth , int reqHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);

        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);

    }

    /**
     * 计算出一个缩放比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if(reqWidth == 0 || reqHeight == 0){
            return 1;
        }
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int inSampleSize = 1;
        if(outHeight > reqHeight || outWidth > reqWidth ){
            int halfHeight = outHeight / 2;
            int halfWigth = outWidth / 2;

            while (((halfHeight / inSampleSize) >= reqHeight && (halfWigth / inSampleSize) >= reqWidth)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }





}

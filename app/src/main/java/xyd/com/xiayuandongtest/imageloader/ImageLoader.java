package xyd.com.xiayuandongtest.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import xyd.com.xiayuandongtest.R;

public class ImageLoader {
    public static final String TAG = "ImageLoader";

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private final Context mContext;

    /**
     * 内存缓存
     */
    private  LruCache<String, Bitmap> memoryLruCache;
    /**
     * 磁盘缓存
     */
    private  DiskLruCache mDiskLruCache;

    //缓存是否创建
    private  boolean mDiskLruCacheCreated = false;

    private int DISK_CACHE_INDEX = 0;

    final ImageResizer imageResizer = new ImageResizer();

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final long KEEP_ALIVE = 10L;

    public static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private  final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"ImageLoader#"+ mCount.getAndIncrement());
        }
    };

    /**
     * 利用线程池做异步操作
     */
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(),sThreadFactory
    );

    /**
     * 必须是主线程
     */
    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            LoadResult loadResult = (LoadResult) msg.obj;
            ImageView imageView = loadResult.imageView;
            imageView.setImageBitmap(loadResult.bitmap);
            String tagUri = (String) imageView.getTag(TAG_KEY_URI);
            if (tagUri.equals(loadResult.uri)) {
                imageView.setImageBitmap(loadResult.bitmap);
            }else {
                Log.w(TAG, "set image bitmap,but url has changed, ignored!");
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;

        int cacheSize = maxMemory / 8;
        memoryLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if(getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加缓存 (如果key下面有bitmap 则不保存)
     */
    public void addBitmapToMemoryCache(String key , Bitmap bitmap){
        if(getBitmapFromCache(key) == null){
            memoryLruCache.put(key,bitmap);
        }
    }

    /**
     * 查看缓存里边是否存在
     * @param key
     * @return
     */
    private Bitmap getBitmapFromCache(String key) {
        return memoryLruCache.get(key);
    }
    /**
     * load bitmap from memory cache or disk cache or network async, then bind imageView and bitmap.
     * NOTE THAT: should run in UI Thread
     * @param uri http url
     * @param imageView bitmap's bind object
     */
    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    /**
     * load bitmap from memory cache or disk cache or network async, then bind imageView and bitmap.
     * NOTE THAT: should run in UI Thread
     * @param uri http url
     * @param imageView bitmap's bind object
     */
    public void bindBitmap(final String uri, final ImageView imageView,int reqWidth,int reqHeight) {
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
       Runnable loadBitmapTask =  new Runnable(){

            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri,reqWidth,reqHeight);
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDisk,url:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromHttp,url:" + uri);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null && mDiskLruCacheCreated) {
             bitmap = downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap downloadBitmapFromUrl(String uri) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream =null;
        try {
            URL url = new URL(uri);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            bufferedInputStream= new BufferedInputStream(httpURLConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in downloadBitmap: " + e);
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            MyUtils.close(bufferedInputStream);
        }


        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String uri) {
        String key = hashKeyFromUrl(uri);
        Bitmap bitmapFromCache = getBitmapFromCache(key);
        return bitmapFromCache;
    }


    /**
     * 加载网络图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(url);

        DiskLruCache.Editor edit = mDiskLruCache.edit(key);
        if(edit != null){
            OutputStream outputStream = edit.newOutputStream(DISK_CACHE_INDEX);
            if(downLoadUrlToStream(url,outputStream)){
                edit.commit();
            }else {
                edit.abort();
            }
        }
        mDiskLruCache.flush();
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);

    }

    /**
     * 从磁盘缓存中取出图片放入内存缓存
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fd = inputStream.getFD();
            Bitmap bitmap = imageResizer.decodeSammleBitmapFromFileDescriptor(fd, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key,bitmap);
            }

        }


        return null;
    }

    /**
     * 将流写成文件
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downLoadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "downloadBitmap failed." + e);
        }finally {
            if (urlConnection != null) {
              urlConnection.disconnect();
            }
            MyUtils.close(out);
            MyUtils.close(in);
        }
        return false;
    }

    private String hashKeyFromUrl(String url) {
        String  cacheKey = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] digest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                stringBuilder.append(hex);
            }
        }
        return stringBuilder.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        StatFs statFs = new StatFs(path.getPath());

        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    private File getDiskCacheDir(Context mContext, String bitmap) {
        boolean ea = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (ea) {
            cachePath = mContext.getExternalCacheDir().getPath();
        }else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + bitmap);
    }


    private  static class LoadResult {
        public ImageView imageView;
        public String uri;
        private Bitmap bitmap;

        public LoadResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}

package xyd.com.xiayuandongtest.utils;

import android.os.Environment;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/18 21:35
 */

public class Constants {
    public static final String PATH = Environment
            .getExternalStorageDirectory().getPath();

    public static final String TEST_FILE_1 = PATH + "/xyd/chapter_2/";
    public static final String CACHE_FILE_PATH = TEST_FILE_1 + "usercache";

    public static final int MSG_FROM_CLIEN = 0X10010;
    public static final int MSG_FROM_SERVICE = 0X10011;



}

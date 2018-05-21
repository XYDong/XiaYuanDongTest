package com.sinosoft.xlib.impl;

import android.app.Activity;
import android.widget.Toast;

import com.sinosoft.xlib.interfaces.IDynamic;


/**
 * @author xyd
 * @version 1.0
 * @description: 实现接口
 * @program XiaYuanDongTest
 * @emil 2028318192@qq.com
 * @create at 2018/5/14 18:13
 */
public class Dynamic implements IDynamic {

    private Activity mActivity;


    @Override
    public void init(Activity var1) {
        this.mActivity = var1;
    }

    @Override
    public void showSomething() {
        Toast.makeText(mActivity, "something is here!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void destory() {

    }
}

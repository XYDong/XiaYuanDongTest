package xyd.com.xiayuandongtest.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import xyd.com.xiayuandongtest.R;

public class ProviderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri parse = Uri.parse("content://xyd.com.xiayuandongtest.providers");
       getContentResolver().query(parse, null, null, null,null);
       getContentResolver().query(parse, null, null, null,null);
       getContentResolver().query(parse, null, null, null,null);
    }
}

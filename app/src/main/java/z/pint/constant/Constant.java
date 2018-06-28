package z.pint.constant;

import android.Manifest;
import android.os.Environment;

import z.pint.R;

/**
 * Created by DN on 2018/6/22.
 */

public class Constant {
    //内存卡的读写权限
    public static final String[] per = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final String environment = Environment.getExternalStorageDirectory().getPath();

}



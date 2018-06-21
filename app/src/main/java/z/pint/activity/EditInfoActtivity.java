package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseActivity;
import z.pint.R;

/**
 * Created by DN on 2018/6/21.
 */

public class EditInfoActtivity extends BaseActivity {
    @ViewInject(value = R.id.edit_toBack)
    private ImageView edit_toBack;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_editinfo_activity;
    }

    @Override
    public void initView(View view) {
        x.view().inject(mContextView);
    }

    @Override
    public void initListener() {
        edit_toBack.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {

    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.edit_toBack:
                finish();
                break;
        }
    }
}

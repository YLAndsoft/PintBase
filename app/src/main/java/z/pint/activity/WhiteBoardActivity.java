package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yinghe.whiteboardlib.fragment.WhiteBoardFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseFragmentActivity;
import z.pint.R;

/**
 * Created by DN on 2018/6/19.
 */

public class WhiteBoardActivity extends BaseFragmentActivity {
    @ViewInject(value = R.id.whiteboard)
    private FrameLayout whiteboard;
    @ViewInject(value = R.id.toBack)
    private ImageView toBack;

    private WhiteBoardFragment whiteBoardFragment;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_white_board;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
    }
    @Override
    public void setListener() {
        toBack.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        //获取Fragment管理器
        FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
        //获取WhiteBoardFragment实例
        if(whiteBoardFragment==null){
            whiteBoardFragment = WhiteBoardFragment.newInstance();
        }
        //添加到界面中
        ts.add(R.id.whiteboard, whiteBoardFragment, "wb").commit();
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.toBack:
                finish();
                break;
        }
    }
}

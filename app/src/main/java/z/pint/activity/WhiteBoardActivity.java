package z.pint.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yinghe.whiteboardlib.Utils.TimeUtils;
import com.yinghe.whiteboardlib.fragment.WhiteBoardFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import z.pint.R;
import z.pint.constant.Constant;
import z.pint.fragment.HomeFragment;

/**
 * Created by DN on 2018/6/19.
 */

public class WhiteBoardActivity extends BaseFragmentActivity implements  WhiteBoardFragment.SendBtnCallback{
    @ViewInject(value = R.id.whiteboard)
    private FrameLayout whiteboard;
    @ViewInject(value = R.id.toBack)
    private ImageView toBack;
    @ViewInject(value = R.id.send_submit)
    private ImageView send_submit;//发布

    private WhiteBoardFragment whiteBoardFragment;
    private static  final  int SEND_CODE=1;
    private BroadcastReceiverWork mBroadcastReceiver;
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
        mBroadcastReceiver = new BroadcastReceiverWork();
        //注册发布作品广播,用于关闭界面
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.RELASER_WORKS_ACTION);
        intentFilter.setPriority(99);//设置接收级别为100
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void initListener() {
        toBack.setOnClickListener(this);
        send_submit.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        //获取Fragment管理器
        FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
        //获取WhiteBoardFragment实例
        if(whiteBoardFragment==null){
            whiteBoardFragment = WhiteBoardFragment.newInstance(this);
        }
        //添加到界面中
        ts.add(R.id.whiteboard, whiteBoardFragment, "wb").commit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBroadcastReceiver!=null){
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.toBack:
                finish();
                break;
            case R.id.send_submit:
                Bitmap resultBitmap = whiteBoardFragment.getResultBitmap();
                if(resultBitmap!=null){
                    //跳转到发布界面
                    String photoName = WhiteBoardFragment.TEMP_FILE_NAME + TimeUtils.getNowTimeString();
                    File file = whiteBoardFragment.saveInOI(WhiteBoardFragment.TEMP_FILE_PATH, photoName, 100);
                    Intent send = new Intent(mContext,ReleaseWorksActivity.class);
                    send.putExtra("filePath",file.getAbsolutePath()+"");
                    startActivityForResult(send,SEND_CODE);
                }else{
                    showToast("画点儿什么吧！");
                }
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);

    }

    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String s) {

    }


    @Override
    public void onSendBtnClick(File filePath) {
        //保存成功后，返回图片文件路径
    }

    class BroadcastReceiverWork extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("works",intent.getSerializableExtra("works"));
            setResult(0, "", bundle);
            //广播通知关闭
            finish();
        }
    }

}

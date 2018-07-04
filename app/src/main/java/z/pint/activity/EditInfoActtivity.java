package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysq.album.activity.AlbumActivity;
import com.ysq.album.bean.ImageBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import f.base.BaseActivity;
import f.base.BaseDialog;
import f.base.BaseDialog.OnDialogClickListener;
import f.base.bean.Params;
import f.base.view.ProvincePopupWindow;
import z.pint.R;
import z.pint.bean.User;
import z.pint.utils.PhotoUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.CuttingImageView;
import z.pint.view.ButtonPopupWindow;

/**
 * 编辑个人信息界面
 * Created by DN on 2018/6/21.
 */

public class EditInfoActtivity extends BaseActivity implements ProvincePopupWindow.OnResultClickListener,OnDialogClickListener,ButtonPopupWindow.OnSelectItemListener{
    private User user;
    private BaseDialog nameDialog;
    private BaseDialog sexDialog;
    private BaseDialog signDialog;
    private ProvincePopupWindow ppw;
    private ButtonPopupWindow photoPW;

    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
        user = (User) intent.getSerializableExtra("userInfo");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_editinfo_activity;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this);
    }

    @Override
    public void initListener() {
        edit_toBack.setOnClickListener(this);
        edit_info_userHead.setOnClickListener(this);
        edit_info_userSex.setOnClickListener(this);
        edit_info_userAddress.setOnClickListener(this);
        edit_info_userSign.setOnClickListener(this);
        edit_info_userName.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        ViewUtils.setTextView(edit_submit,getResources().getString(R.string.edit_submit),"");
        ViewUtils.setTextView(tv_userSign,getResources().getString(R.string.tv_userSign),"");
        ViewUtils.setTextView(tv_userAddress,getResources().getString(R.string.tv_userAddress),"");
        ViewUtils.setTextView(tv_userSex,getResources().getString(R.string.tv_userSex),"");
        ViewUtils.setTextView(tv_userName,getResources().getString(R.string.tv_userName),"");
        ViewUtils.setTextView(tv_userHead,getResources().getString(R.string.tv_userHead),"");
        ViewUtils.setTextView(userInfo,getResources().getString(R.string.userInfo),"");
        if(user==null)return;
        ViewUtils.setImageUrl(mContext,edit_info_userHead,user.getUserHead(),R.mipmap.default_head_image);
        ViewUtils.setTextView(edit_info_userName,user.getUserName(),getResources().getString(R.string.defult_userName));
        ViewUtils.setTextView(edit_info_userSex,user.getUserSex()==0?getResources().getString(R.string.man):getResources().getString(R.string.woman),getResources().getString(R.string.man));
        ViewUtils.setTextView(edit_info_userAddress,user.getUserAddress(),getResources().getString(R.string.defult_address));
        ViewUtils.setTextView(edit_info_userSign,user.getUserSign(),getResources().getString(R.string.defult_sign));
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.edit_toBack:
                finish();
                break;
            case R.id.edit_info_userHead:
                //showToast("编辑头像");
                if(null==photoPW) photoPW = new ButtonPopupWindow(mContext,EditInfoActtivity.this);
                if(!photoPW.isShowing()){
                    photoPW.showPopupWindow(edit_rootView, Gravity.BOTTOM,0,0);
                    return;
                }
                photoPW.dismiss();
                break;
            case R.id.edit_info_userName:
                //showToast("编辑昵称");
                if(null==nameDialog)nameDialog = new BaseDialog(mContext,BaseDialog.DIALOG_EDIT_STATE,getResources().getString(R.string.edit_info_nameTitle),"","取消","完成" ,false,EditInfoActtivity.this,1);
                if(!nameDialog.isShowing()){
                    nameDialog.show();
                    return;
                }
                nameDialog.dismiss();
                break;
            case R.id.edit_info_userSex:
                //showToast("编辑性别");
                if(null==sexDialog)sexDialog = new BaseDialog(mContext,BaseDialog.DIALOG_CHECK_STATE,getResources().getString(R.string.edit_info_sexTitle),"","取消","完成" ,false,EditInfoActtivity.this,2);
                if(!sexDialog.isShowing()){
                    sexDialog.show();
                    return;
                }
                sexDialog.dismiss();
                break;
            case R.id.edit_info_userAddress:
                //showToast("编辑地区");
                if(null==ppw)ppw = new ProvincePopupWindow(mContext,"province_data.xml");//
                ppw.setOutsideTouchable(false);
                ppw.setListener(EditInfoActtivity.this);
                if(!ppw.isShowing()){
                    ppw.showPopupWindow(edit_rootView, Gravity.BOTTOM,0,0);
                    return;
                }
                ppw.dismiss();
                break;
            case R.id.edit_info_userSign:
                //showToast("编辑签名");
                if(null==signDialog)signDialog = new BaseDialog(mContext,BaseDialog.DIALOG_EDIT_STATE,getResources().getString(R.string.edit_info_signTitle),"","取消","完成" ,false,EditInfoActtivity.this,3);
                if(!signDialog.isShowing()){
                    signDialog.show();
                    return;
                }
                signDialog.dismiss();
                break;
        }
    }

    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String s) {

    }

    /**
     * 地区回调
     * @param s
     */
    @Override
    public void onResultClick(String s) {
        ViewUtils.setTextView(edit_info_userAddress,s,getResources().getString(R.string.defult_address));
    }

    /**
     * dialog左边按钮回调
     */
    @Override
    public void onLeftClick(int state) {
        if(state==1){
            nameDialog.dismiss();
        }else if(state==2){
            sexDialog.dismiss();
        }else if(state==2){
            signDialog.dismiss();
        }
    }
    /**
     * dialog右边按钮回调
     */
    @Override
    public void onRigthClick(String content,int state) {
        switch (state){
            case 1:
                ViewUtils.setTextView(edit_info_userName,content,getResources().getString(R.string.defult_userName));
                nameDialog.dismiss();
                break;
            case 2:
                ViewUtils.setTextView(edit_info_userSex,content,getResources().getString(R.string.man));
                sexDialog.dismiss();
                break;
            case 3:
                ViewUtils.setTextView(edit_info_userSign,content,getResources().getString(R.string.defult_sign));
                signDialog.dismiss();
                break;
        }
    }

    /**
     * 修改头像底部弹窗回调
     * @param position
     */
    @Override
    public void onSelectItemOnclick(int position,String tabName) {
        if(position==1){
            showToast("点击了相册");
            /*PermissionGen.with(EditInfoActtivity.this)
                    .addRequestCode(PER_CODE)
                    .permissions(Constant.per)
                    .request();*/
            //PhotoUtils.startGallery(EditInfoActtivity.this,GALLERY_OPEN_REQUEST_CODE);
        }else if(position==2){
            showToast("点击了拍照");
        }
        //关闭底部弹窗
        if(null!=photoPW){
            if(photoPW.isShowing()){
                photoPW.dismiss();
            }
        }
    }

    /**
     * 申请权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    /**
     * 打开相册，拍照返回回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                /*case CAMERA_OPEN_REQUEST_CODE://拍照完成回调

                    break;
                case INTENT_CODE://访问相册完成回调

                    break;
                case CROP_IMAGE_REQUEST_CODE://裁剪完成回调

                    break;*/
            }
        }


    }



    @ViewInject(value = R.id.edit_toBack)
    private ImageView edit_toBack;//返回
    @ViewInject(value = R.id.edit_submit)
    private TextView edit_submit;//保存
    @ViewInject(value = R.id.edit_info_userHead)
    private CuttingImageView edit_info_userHead;//用户头像
    @ViewInject(value = R.id.edit_info_userName)
    private TextView edit_info_userName;//用户昵称
    @ViewInject(value = R.id.edit_info_userSex)
    private TextView edit_info_userSex;//用户性别
    @ViewInject(value = R.id.edit_info_userAddress)
    private TextView edit_info_userAddress;//用户地区
    @ViewInject(value = R.id.edit_info_userSign)
    private TextView edit_info_userSign;//用户签名
    @ViewInject(value = R.id.edit_rootView)
    private LinearLayout edit_rootView;//父布局
    @ViewInject(value = R.id.tv_userSign)
    private TextView tv_userSign;
    @ViewInject(value = R.id.tv_userAddress)
    private TextView tv_userAddress;
    @ViewInject(value = R.id.tv_userSex)
    private TextView tv_userSex;
    @ViewInject(value = R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(value = R.id.tv_userHead)
    private TextView tv_userHead;
    @ViewInject(value = R.id.userInfo)
    private TextView userInfo;

}

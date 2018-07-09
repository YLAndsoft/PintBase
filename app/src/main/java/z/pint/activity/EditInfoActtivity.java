package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseActivity;
import f.base.BaseDialog;
import f.base.BaseDialog.OnDialogClickListener;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import f.base.view.ProvincePopupWindow;
import z.pint.R;
import z.pint.bean.EventBusEvent;
import z.pint.bean.User;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.PhotoUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.CuttingImageView;

/**
 * 编辑个人信息界面
 * Created by DN on 2018/6/21.
 */

public class EditInfoActtivity extends BaseActivity implements ProvincePopupWindow.OnResultClickListener,OnDialogClickListener,User.OnUpdateValueListener{
    private User user;
    private BaseDialog nameDialog;
    private BaseDialog sexDialog;
    private BaseDialog signDialog;
    private BaseDialog upDialog;
    private ProvincePopupWindow ppw;

    private static  final int REQUEST_CODE_CHOOSE = 0;

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
        edit_submit.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        edit_toBack.setOnClickListener(this);
        edit_info_userHead.setOnClickListener(this);
        edit_info_userSex.setOnClickListener(this);
        edit_info_userAddress.setOnClickListener(this);
        edit_info_userSign.setOnClickListener(this);
        edit_info_userName.setOnClickListener(this);
        edit_submit.setOnClickListener(this);
        user.setUpdateValueListener(this);//监听用户的属性值是否发生改变
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
                if(isUpdate){
                    showUpdateDialog();
                }else{
                    finish();
                }
                break;
            case R.id.edit_submit://保存
                //showToast("点击保存");
                //保存数据
                SPUtils.getInstance(mContext).setParam("userHead",user.getUserHead());
                SPUtils.getInstance(mContext).setParam("userName",user.getUserName());
                boolean b = DBHelper.saveUser(user);
                if(b){
                    EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.A,user));
                    //修改服务器上面的数据
                    upUserInfo(user);
                    finish();
                }else{
                    showToast("修改失败！");
                    isUpdate=false;
                }
                break;
            case R.id.edit_info_userHead:
                //showToast("编辑头像");
                showPhoto();
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

    /**
     * 返回提醒窗口
     */
    private void showUpdateDialog() {
            if(null==upDialog)upDialog = new BaseDialog(mContext,
                    BaseDialog.DIALOG_DEFAULT_STATE,
                    "提示",
                    "有编辑的内容没保存,是否保存?",
                    "取消",
                    "保存" ,
                    false,
                    EditInfoActtivity.this,
                    4);
            if(!upDialog.isShowing()){
                upDialog.show();
                return;
            }
            upDialog.dismiss();
            return;
    }

    /**
     * 显示相册
     */
    private void showPhoto() {
        Matisse.from(EditInfoActtivity.this)
                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG)) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(1) // 图片选择的最多数量
                .gridExpectedSize(0)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
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
        if(!StringUtils.isBlank(s)){
            ViewUtils.setTextView(edit_info_userAddress,s,getResources().getString(R.string.defult_address));
            user.setUserAddress(s);//保存修改的地区
            isUpdate = true;
        }
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
        }else if(state==3){
            signDialog.dismiss();
        }else if(state==4){
            upDialog.dismiss();
        }
    }
    /**
     * dialog右边按钮回调
     */
    @Override
    public void onRigthClick(String content,int state) {
        switch (state){
            case 1:
                if(!StringUtils.isBlank(content)){
                    ViewUtils.setTextView(edit_info_userName,content,getResources().getString(R.string.defult_userName));
                    user.setUserName(content);//保存用户昵称
                }
                nameDialog.dismiss();
                break;
            case 2:
                if(!StringUtils.isBlank(content)){
                    ViewUtils.setTextView(edit_info_userSex,content,getResources().getString(R.string.man));
                    if(content.equals("女")){ //保存性别
                        user.setUserSex(1);
                    }else{
                        user.setUserSex(0);
                    }
                }
                sexDialog.dismiss();
                break;
            case 3:
                if(!StringUtils.isBlank(content)){
                    ViewUtils.setTextView(edit_info_userSign,content,getResources().getString(R.string.defult_sign));
                    user.setUserSign(content);//保存修改的签名
                }
                signDialog.dismiss();
                break;
            case 4:
                //保存数据
                SPUtils.getInstance(mContext).setParam("userHead",user.getUserHead());
                SPUtils.getInstance(mContext).setParam("userName",user.getUserName());
                boolean b = DBHelper.saveUser(user);
                if(b){
                    //通知个人信息界面更改信息
                    EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.A,user));
                    //修改服务器上面的数据
                    upUserInfo(user);
                    finish();
                }else{
                    showToast("修改失败！");
                    isUpdate=false;
                }
                break;
        }
    }

    /**
     * 修改服务器上的数据
     */
    private void upUserInfo(User us) {
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID,user.getUserID()+"");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.UP_STATE+"");
        try{
            String toJson = new Gson().toJson(us);
            map.put(HttpConfig.USER_INFO,toJson);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        XutilsHttp.xUtilsRequest(HttpConfig.getUserInfoData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                showLog(3,"修改信息结果："+result+"");
            }
            @Override
            public void onFail(String result) {
                showLog(3,"修改信息结果："+result+"");
            }
        });
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
                case REQUEST_CODE_CHOOSE://访问相册完成回调
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if(null!=mSelected&&mSelected.size()>0){
                        Uri uri = mSelected.get(0);
                        if(!StringUtils.isBlank(uri+"")){
                            showLog(3,"mSelected: " + uri);
                            String path = PhotoUtils.getPath(mContext, uri);
                            ViewUtils.setImageUrl(mContext,edit_info_userHead,path,R.mipmap.default_head_image);
                            user.setUserHead(path);//保存用户头像
                        }
                    }
                    break;
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

    private boolean isUpdate = false; //是否改修改过当前值
    /**
     * 用户属性值是否有改变的监听回调
     */
    @Override
    public void onSuccess(String content) {
        isUpdate = true;
        edit_submit.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isUpdate){
                showUpdateDialog();
                return true;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}

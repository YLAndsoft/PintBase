package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import f.base.utils.RandomUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import f.base.view.ProvincePopupWindow;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.bean.Comment;
import z.pint.bean.EventBusEvent;
import z.pint.bean.Likes;
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.Constant;
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

public class EditInfoActtivity extends BaseActivity implements ProvincePopupWindow.OnResultClickListener, OnDialogClickListener, User.OnUpdateValueListener {
    private User user;
    private BaseDialog nameDialog;
    private BaseDialog sexDialog;
    private BaseDialog signDialog;
    private BaseDialog addressDialog;
    private BaseDialog upDialog;
    private ProvincePopupWindow ppw;

    private static final int REQUEST_CODE_CHOOSE = 0;

    @Override
    public void initParms(Intent intent) {
        setSetActionBarColor(true, R.color.maintab_topbar_bg_color);
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
        rl_user_sign.setOnClickListener(this);
        rl_user_address.setOnClickListener(this);
        edit_right1.setOnClickListener(this);
        rl_user_name.setOnClickListener(this);
        rl_user_sex.setOnClickListener(this);
    }

    @Override
    public void initData(Context context) {
        ViewUtils.setTextView(edit_submit, getResources().getString(R.string.edit_submit));
        ViewUtils.setTextView(tv_userSign, getResources().getString(R.string.tv_userSign));
        ViewUtils.setTextView(tv_userAddress, getResources().getString(R.string.tv_userAddress));
        ViewUtils.setTextView(tv_userSex, getResources().getString(R.string.tv_userSex));
        ViewUtils.setTextView(tv_userName, getResources().getString(R.string.tv_userName));
        ViewUtils.setTextView(tv_userHead, getResources().getString(R.string.tv_userHead));
        ViewUtils.setTextView(userInfo, getResources().getString(R.string.userInfo));
        if (user == null) return;
        user.setUpdateValueListener(this);//监听用户的属性值是否发生改变
        ViewUtils.setImageUrl(mContext, edit_info_userHead, user.getUserHead(), R.mipmap.default_head_image);
        ViewUtils.setTextView(edit_info_userName, user.getUserName());
        ViewUtils.setTextView(edit_info_userSex, user.getUserSex() == 0 ? getResources().getString(R.string.man) : getResources().getString(R.string.woman));
        ViewUtils.setTextView(edit_info_userAddress, user.getUserAddress());
        ViewUtils.setTextView(edit_info_userSign, user.getUserSign());
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.edit_toBack:
                if (isUpdate) {
                    showUpdateDialog();
                } else {
                    finish();
                }
                break;
            case R.id.edit_submit://保存
                //showToast("点击保存");
                commitData();
                break;
            case R.id.edit_right1:
                //showToast("随机头像");
                SVP.showWithStatus(mContext, "随机中...");
                mHandler.postDelayed(runnable, 2000);
                break;
            case R.id.edit_info_userHead:
                //showToast("编辑头像");
                showPhoto();
                break;
            case R.id.rl_user_name:
            case R.id.edit_info_userName:
                //showToast("编辑昵称");
                if (null == nameDialog)
                    nameDialog = new BaseDialog(mContext, BaseDialog.DIALOG_EDIT_STATE, getResources().getString(R.string.edit_info_nameTitle), "", "取消", "完成", false, EditInfoActtivity.this, 1);
                if (!nameDialog.isShowing()) {
                    nameDialog.show();
                    return;
                }
                nameDialog.dismiss();
                break;
            case R.id.rl_user_sex:
            case R.id.edit_info_userSex:
                //showToast("编辑性别");
                if (null == sexDialog)
                    sexDialog = new BaseDialog(mContext, BaseDialog.DIALOG_CHECK_STATE, getResources().getString(R.string.edit_info_sexTitle), "", "取消", "完成", false, EditInfoActtivity.this, 2);
                if (!sexDialog.isShowing()) {
                    sexDialog.show();
                    return;
                }
                sexDialog.dismiss();
                break;
            case R.id.rl_user_address:
            case R.id.edit_info_userAddress:
                //showToast("编辑地区");
                /*if(null==ppw)ppw = new ProvincePopupWindow(mContext,"province_data.xml");//
                ppw.setOutsideTouchable(false);
                ppw.setListener(EditInfoActtivity.this);
                if(!ppw.isShowing()){
                    ppw.showPopupWindow(edit_rootView, Gravity.BOTTOM,0,0);
                    return;
                }
                ppw.dismiss();*/
                if (null == addressDialog)
                    addressDialog = new BaseDialog(mContext, BaseDialog.DIALOG_EDIT_STATE, getResources().getString(R.string.edit_info_addressTitle), "", "取消", "完成", false, EditInfoActtivity.this, 5);
                if (!addressDialog.isShowing()) {
                    addressDialog.show();
                    return;
                }
                sexDialog.dismiss();
                break;
            case R.id.rl_user_sign:
            case R.id.edit_info_userSign:
                //showToast("编辑签名");
                if (null == signDialog)
                    signDialog = new BaseDialog(mContext, BaseDialog.DIALOG_EDIT_STATE, getResources().getString(R.string.edit_info_signTitle), "", "取消", "完成", false, EditInfoActtivity.this, 3);
                if (!signDialog.isShowing()) {
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
        if (null == upDialog) upDialog = new BaseDialog(mContext,
                BaseDialog.DIALOG_DEFAULT_STATE,
                "提示",
                "有编辑的内容没保存,是否保存?",
                "取消",
                "保存",
                false,
                EditInfoActtivity.this,
                4);
        if (!upDialog.isShowing()) {
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
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG)) // 选择 mime 的类型
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
    protected void onSuccess(Params params) {
    }

    @Override
    protected void onErrors(Params params) {
    }

    /**
     * 创建定时器
     */
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (SVP.isShowing(mContext)) SVP.dismiss(mContext);
            String r_head = Constant.USER_HEAD[RandomUtils.getRandom(0, Constant.USER_HEAD.length)];
            ViewUtils.setImageUrl(mContext, edit_info_userHead, r_head, R.mipmap.default_head_image);
            user.setUserHead(r_head);//保存用户头像
        }
    };

    /**
     * 地区回调
     *
     * @param s
     */
    @Override
    public void onResultClick(String s) {
        if (!StringUtils.isBlank(s)) {
            ViewUtils.setTextView(edit_info_userAddress, s);
            user.setUserAddress(s);//保存修改的地区
            isUpdate = true;
        }
    }

    /**
     * dialog左边按钮回调
     */
    @Override
    public void onLeftClick(int state) {
        if (state == 1) {
            nameDialog.dismiss();
        } else if (state == 2) {
            sexDialog.dismiss();
        } else if (state == 3) {
            signDialog.dismiss();
        } else if (state == 4) {
            upDialog.dismiss();
            finish();
        } else if (state == 5) {
            addressDialog.dismiss();
        }
    }

    /**
     * dialog右边按钮回调
     */
    @Override
    public void onRigthClick(String content, int state) {
        switch (state) {
            case 1:
                if (!StringUtils.isBlank(content)) {
                    ViewUtils.setTextView(edit_info_userName, content);
                    user.setUserName(content);//保存用户昵称
                }
                nameDialog.dismiss();
                break;
            case 2:
                if (!StringUtils.isBlank(content)) {
                    ViewUtils.setTextView(edit_info_userSex, content);
                    if (content.equals("女")) { //保存性别
                        user.setUserSex(1);
                    } else {
                        user.setUserSex(0);
                    }
                }
                sexDialog.dismiss();
                break;
            case 3:
                if (!StringUtils.isBlank(content)) {
                    ViewUtils.setTextView(edit_info_userSign, content);
                    user.setUserSign(content);//保存修改的签名
                }
                signDialog.dismiss();
                break;
            case 5:
                if (!StringUtils.isBlank(content)) {
                    ViewUtils.setTextView(edit_info_userAddress, content);
                    user.setUserAddress(content);//保存修改的签名
                }
                addressDialog.dismiss();
                break;
            case 4:
                commitData();
                break;
        }
    }

    /**
     * 保存修改后的数据
     */
    private void commitData() {
        //保存修改后的数据
        SPUtils.saveUserHead(mContext, user.getUserHead());
        SPUtils.saveUserName(mContext, user.getUserName());
        boolean b = DBHelper.saveUser(user);
        if (b) {
            //通知个人信息界面更改信息
            EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.A, user));
            //修改相关数据库里面的值
            DBHelper.updateUser(user);
            //考虑到作品多的情况，会产生耗时。
            new Thread() {
                @Override
                public void run() {
                    //修改作品表
                    final List<Works> worksList = DBHelper.selectWorksAll();
                    if (worksList != null && worksList.size() > 0) {
                        for (Works work : worksList) {
                            work.setUserName(user.getUserName());
                            work.setUserHead(user.getUserHead());
                            DBHelper.updateWorksHead(work);
                            DBHelper.updateWorksUserName(work);
                        }
                    }
                    //修改评论表
                    List<Comment> comments = DBHelper.queryCommentAll();
                    if(null!=comments&&comments.size()>0){
                        for (Comment comm : comments) {
                            comm.setUserName(user.getUserName());
                            comm.setUserHead(user.getUserHead());
                            DBHelper.updateCommentHead(comm);
                            DBHelper.updateCommentUserName(comm);
                        }
                    }
                    //修改点赞表
                    List<Likes> likes = DBHelper.queryLikesAll();
                    if(null!=likes&&likes.size()>0){
                        for (Likes like : likes) {
                            like.setUserName(user.getUserName());
                            like.setUserHead(user.getUserHead());
                            DBHelper.updateLikesHead(like);
                            DBHelper.updateLikesUserName(like);
                        }
                    }
                }
            }.start();
            //修改服务器上面的数据
            upUserInfo(user);
            finish();
        } else {
            showToast("修改失败！");
            isUpdate = false;
        }
    }

    /**
     * 修改服务器上的数据
     */
    private void upUserInfo(User us) {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID, user.getUserID() + "");
        map.put(HttpConfig.ACTION_STATE, HttpConfig.UP_STATE + "");
        try {
            String toJson = new Gson().toJson(us);
            map.put(HttpConfig.USER_INFO, toJson);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        XutilsHttp.xUtilsRequest(HttpConfig.getUserInfoData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                showLog(3, "修改信息结果：" + result + "");
            }

            @Override
            public void onFail(String result) {
                showLog(3, "修改信息结果：" + result + "");
            }
        });
    }

    /**
     * 申请权限回调
     *
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE://访问相册完成回调
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (null != mSelected && mSelected.size() > 0) {
                        Uri uri = mSelected.get(0);
                        if (!StringUtils.isBlank(uri + "")) {
                            showLog(3, "mSelected: " + uri);
                            String path = PhotoUtils.getPath(mContext, uri);
                            ViewUtils.setImageUrl(mContext, edit_info_userHead, path, R.mipmap.default_head_image);
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
    @ViewInject(value = R.id.rl_user_sign)
    private RelativeLayout rl_user_sign;
    @ViewInject(value = R.id.edit_right1)
    private ImageView edit_right1;
    @ViewInject(value = R.id.rl_user_name)
    private RelativeLayout rl_user_name;
    @ViewInject(value = R.id.rl_user_address)
    private RelativeLayout rl_user_address;
    @ViewInject(value = R.id.rl_user_sex)
    private RelativeLayout rl_user_sex;

    private boolean isUpdate = false; //是否改修改过当前值

    /**
     * 用户属性值是否有改变的监听回调
     */
    @Override
    public void onUpSucces(String content) {
        isUpdate = true;
        edit_submit.setVisibility(View.VISIBLE);
    }

}

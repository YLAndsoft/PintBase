package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.util.Date;
import java.util.List;

import f.base.BaseActivity;
import f.base.bean.Params;
import f.base.utils.StringUtils;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.bean.EventBusEvent;
import z.pint.bean.Works;
import z.pint.constant.Constant;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.TimeUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/7/5.
 */

public class ReleaseWorksActivity extends BaseActivity {
    @ViewInject(value = R.id.release_submit)
    private ImageView release_submit;
    @ViewInject(value = R.id.release_img)
    private ImageView release_img;
    @ViewInject(value = R.id.release_des)
    private EditText release_des;
    @ViewInject(value = R.id.des_length)
    private TextView des_length;
    @ViewInject(value = R.id.relsease_ll_tag)
    private LinearLayout relsease_ll_tag;
    @ViewInject(value = R.id.release_title)
    private TextView release_title;
    @ViewInject(value = R.id.relsease_tag)
    private TextView relsease_tag;
    private String filePath;
    private Works works;//待发布的作品
    private static final int TAG_CAODE = 1;//标签选择回调码
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
        filePath = intent.getStringExtra("filePath");
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_release_works;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this);
        works = new Works();
    }
    @Override
    public void initListener() {
        release_submit.setOnClickListener(this);
        relsease_ll_tag.setOnClickListener(this);
        release_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()<=1000){
                    des_length.setText((1000-(editable.length()))+"/1000");
                }
                works.setWorksDescribe(editable.toString());//保存描述
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAG_CAODE) {
                String tag = data.getStringExtra("tag");
                if(StringUtils.isBlank(tag)){return;}
                works.setWorksLabel(tag);//设置作品的标签
                String[] split = tag.split("\\|");//截取标签
                relsease_ll_tag.removeAllViews();//移除所有布局
                ImageView imageView = new ImageView(mContext);//创建Imageview布局
                imageView.setImageResource(R.mipmap.tag); //设置image资源
                relsease_ll_tag.addView(imageView);//添加标签图标布局
                for(int i=0;i<split.length;i++){
                    if(StringUtils.isBlank(split[i])) return;
                    View inflate = View.inflate(mContext, R.layout.custom_tag_item_layout, null);
                    TextView txt = inflate.findViewById(R.id.custom_tag_txt);
                    txt.setText("#  "+split[i]+"");
                    relsease_ll_tag.addView(inflate);
                }
            }
        }

    }

    @Override
    public void initData(Context mContext) {
        ViewUtils.setImageUrl(mContext,release_img,filePath,R.mipmap.data_error);
        ViewUtils.setTextView(release_title,getResources().getString(R.string.release_title));
        release_des.setHint(getResources().getString(R.string.release_des));
        ViewUtils.setTextView(des_length,getResources().getString(R.string.des_length));
        ViewUtils.setTextView(relsease_tag,getResources().getString(R.string.relsease_tag));
        works.setWorksReleaseTime(TimeUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));//保存发布时间
        String userName = SPUtils.getUserName(mContext);
        String userHead = SPUtils.getUserHead(mContext);
        int userID = SPUtils.getUserID(mContext);
        works.setUserHead(userHead);//保存发布者头像
        works.setUserName(userName);//保存发布者昵称
        works.setUserID(userID);////保存发布者用户ID
        works.setWorksImage(filePath+""); //保存发布图片
        List<Works> workses = DBHelper.selectWorksAll();
        if(null!=workses&&workses.size()>0){
            //设置作品ID
            works.setWorksID(workses.size()+1);
        }else{
            works.setWorksID(1);
        }
        works.setWorksStrokes(0);//笔画数
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.relsease_ll_tag:
                //跳转至选择标签界面
                Intent intent = new Intent(mContext,SelectTagActivity.class);
                startActivityForResult(intent,TAG_CAODE);
                break;
            case R.id.release_submit:
                SVP.showWithStatus(mContext,"发布中..");
                //验证,//发布作品
                verification(works);
                break;
        }
    }

    /**
     * 验证作品格式是否正确
     */
    private void verification(Works works) {
        if(works==null){
            showToast("发布失败！");
            if(SVP.isShowing(mContext)) SVP.dismiss(mContext);
            return;
        }
        if(StringUtils.isBlank(works.getWorksLabel())){
            showToast("作品标签不能为空！");
            if(SVP.isShowing(mContext))SVP.dismiss(mContext);
            return;
        }
        if(StringUtils.isBlank(works.getWorksDescribe())){
            showToast("作品描述不能为空！");
            if(SVP.isShowing(mContext))SVP.dismiss(mContext);
            return;
        }
        showToast("发布成功！");
        if(SVP.isShowing(mContext))SVP.dismiss(mContext);
        //保存至数据库
        boolean b = DBHelper.saveWorks(works);
        showLog(3,"保存发布作品结果："+b);
        //通知主界面刷新数据
        //通知个人信息界面更改信息
        EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.B,works));
        finish();
    }

    @Override
    public Params getParams() {
        return null;
    }
    @Override
    protected void setData(String result) {
    }
}

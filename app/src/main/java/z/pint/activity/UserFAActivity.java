package z.pint.activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseActivity;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.RandomUtils;
import z.pint.R;
import z.pint.bean.User;
import z.pint.constant.Constant;
import z.pint.constant.HttpConfig;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewDivider;

/**
 * 用户关注/粉丝界面
 * Created by DN on 2018/7/2.
 */

public class UserFAActivity extends BaseActivity {

    @ViewInject(value = R.id.fansAndattention_recycler)
    private RecyclerView mRecyclerView;
    @ViewInject(value = R.id.fansAndattention_toBlack)
    private ImageView toBlack;
    @ViewInject(value = R.id.fansAndattention_title)
    private TextView fansAndattention_title;

    private int view_tag;//显示数据的标识
    private int userID;//用户ID
    private int start;
    private int num;
    @Override
    public void initParms(Intent intent) {
        //此属性设置与状态栏相关
        setAllowFullScreen(true);//是否允许全屏
        setScreenRoate(false);//是否允许屏幕旋转
        setSteepStatusBar(false);//是否设置沉浸状态栏
        setSetActionBarColor(true, R.color.maintab_topbar_bg_color);//设置状态栏主题颜色
        view_tag = intent.getIntExtra("VIEW_TAG", 0);
        userID = intent.getIntExtra("userID", 0);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_fans;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this);
    }

    @Override
    public void initListener() {
        toBlack.setOnClickListener(this);
    }

    @Override
    public void initData(final Context mContext) {
        if(view_tag== Constant.VIEW_FANS){
            ViewUtils.setTextView(fansAndattention_title,getResources().getString(R.string.my_fans),"");
        }else if(view_tag== Constant.VIEW_ATTENTION){
            ViewUtils.setTextView(fansAndattention_title,getResources().getString(R.string.my_attention),"");
        }

        List<User> list = new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(new User("测试"+i,Constant.USER_HEAD[RandomUtils.getRandom(0,Constant.USER_HEAD.length)]));
        }
        mRecyclerView.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary5));
        BaseRecyclerAdapter<User> adapter = new BaseRecyclerAdapter<User>(mContext,list,R.layout.fans_attention_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, User item, int position) {
                holder.setText(R.id.fansAndAttention_userName,item.getUserName()+"");
                ImageView view = holder.getView(R.id.fansAndAttention_userHead);
                ViewUtils.setImageUrl(mContext,view,item.getUserHead(),R.mipmap.default_head_image);
            }
        };
        //设置布局管理器
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.fansAndattention_toBlack:
                finish();
                break;
        }
    }

    @Override
    public Params getParams() {
        //userID=1&converAttentionID=1&actionState=0 关注
        if(userID==0){
            userID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
        }
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID+"");
        if(view_tag==Constant.VIEW_FANS){
            return new Params(HttpConfig.getFansData,map);
        }else if(view_tag==Constant.VIEW_ATTENTION){
            return new Params(HttpConfig.getAttentionData,map);
        }
        return null;
    }

    @Override
    protected void setData(String result) {

    }
}

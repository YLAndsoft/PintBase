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
import f.base.BaseDialog;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.RandomUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.bean.Attention;
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
    @ViewInject(value = R.id.data_error)
    private ImageView data_error;

    private int view_tag;//显示数据的标识
    private int userID;//用户ID
    private int dbUserID;
    private BaseDialog dialog;
    private BaseRecyclerAdapter<Attention> attentionAdapter;
    private BaseRecyclerAdapter adapter = null;
    @Override
    public void initParms(Intent intent) {
        setSetActionBarColor(true, R.color.maintab_topbar_bg_color);//设置状态栏主题颜色
        view_tag = intent.getIntExtra("VIEW_TAG", 0);
        userID = intent.getIntExtra("userID", 0);
        dbUserID = SPUtils.getUserID(mContext);
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
            ViewUtils.setTextView(fansAndattention_title,getResources().getString(R.string.my_fans));
        }else if(view_tag== Constant.VIEW_ATTENTION){
            ViewUtils.setTextView(fansAndattention_title,getResources().getString(R.string.my_attention));
        }
        mRecyclerView.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary5));
        //设置布局管理器
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
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
        if(userID==0)userID=dbUserID;
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID+"");
        if(view_tag==Constant.VIEW_FANS){
            return new Params(HttpConfig.getFansData,map,User.class,true);
        }else if(view_tag==Constant.VIEW_ATTENTION){
            return new Params(HttpConfig.getAttentionData,map,Attention.class,true);
        }
        return null;
    }

    @Override
    protected void onSuccess(Params params) {
        if(view_tag==Constant.VIEW_FANS){
            List<User> gsonList = (List<User>) params.getObj();
            adapter = setFansAdapter(gsonList);
        }else if(view_tag==Constant.VIEW_ATTENTION){
            List<Attention> gsonList = (List<Attention>) params.getObj();
            adapter = setAttentionAdapter(gsonList);
        }
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onErrors(Params params) {
        showLog(3,params.getObj()+"");
        if(!params.isLoad()&&!params.isRefresh()){
            mRecyclerView.setVisibility(View.GONE);
            data_error.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 绑定关注适配器
     * @param gsonList
     * @return
     */
    private BaseRecyclerAdapter setAttentionAdapter(List<Attention> gsonList) {
        attentionAdapter  = new BaseRecyclerAdapter<Attention>(mContext,gsonList,R.layout.fans_attention_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, final Attention item, int position) {
                holder.setText(R.id.fansAndAttention_userName,item.getUserName()+"");
                ImageView view = holder.getView(R.id.fansAndAttention_userHead);
                ViewUtils.setImageUrl(mContext,view,item.getUserHead(),R.mipmap.default_head_image);
                ImageView view1 = holder.getView(R.id.fansAndAttention_attention);
                view1.setImageResource(R.mipmap.unfollow);
                holder.setOnViewClick(R.id.fansAndAttention_attention,item,position, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view, Object object, final int position) {
                        dialog  = new BaseDialog(mContext,
                                BaseDialog.DIALOG_DEFAULT_STATE,
                                "提示",
                                "是否取消关注？",
                                "否",
                                "是",
                                false,
                                new BaseDialog.OnDialogClickListener() {
                                    @Override
                                    public void onLeftClick(int listenerCode) {
                                        dialog.dismiss();
                                    }
                                    @Override
                                    public void onRigthClick(String content, int listenerCode) {
                                        dialog.dismiss();
                                        attentionAdapter.delete(position);
                                        //attentionAdapter.notifyItemRemoved(position);
                                        deleteFA(view_tag,item);
                                    }
                        },1);
                        dialog.show();
                    }
                });
            }
        };
        return attentionAdapter;
    }
    /**
     * 绑定粉丝适配器
     * @param list
     * @return
     */
    private BaseRecyclerAdapter setFansAdapter(List<User> list){
        BaseRecyclerAdapter<User> adapter = new BaseRecyclerAdapter<User>(mContext,list,R.layout.fans_attention_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, User item, int position) {
                holder.setText(R.id.fansAndAttention_userName,item.getUserName()+"");
                ImageView view = holder.getView(R.id.fansAndAttention_userHead);
                ViewUtils.setImageUrl(mContext,view,item.getUserHead(),R.mipmap.default_head_image);
            }
        };
        return adapter;
    }

    private void deleteFA(int view_tag,Attention attention){
        int userID = SPUtils.getUserID(mContext);
        if(view_tag==Constant.VIEW_FANS){
        }else{
            //取消粉丝
            deleteAttention(HttpConfig.DELETE_STATE,attention.getConverAttentionID(),userID);
        }
    }

    /**
     * 取消关注
     * @param action
     * @param converAttentionID
     * @param userID
     */
    private void deleteAttention(int action,int converAttentionID,int userID) {
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,action+"");
        map.put(HttpConfig.CONVER_ATTENTION_ID,converAttentionID+"");
        map.put(HttpConfig.USER_ID,userID+"");
        XutilsHttp.xUtilsPost(HttpConfig.getAttentionData, map, new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                showLog(3,"关注结果："+result);
            }
            @Override
            public void onFail(String result) {
                showLog(3,"关注结果："+result);
            }
        });
    }


}

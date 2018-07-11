package z.pint.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.bean.Likes;
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.Constant;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;

/**
 * 用户信息里面作品页面
 * Created by DN on 2018/6/21.
 */

public class UserWorksFragment extends BaseFragment {

    @ViewInject(value = R.id.userinfo_works_recycler)
    private RecyclerView userinfo_works_recycler;
    @ViewInject(value = R.id.userWorks_refreshLayout)
    private SmartRefreshLayout userWorks_refreshLayout;
    @ViewInject(value = R.id.data_error)
    private ImageView data_error;
    @ViewInject(value = R.id.userWoorks_loding)
    private ProgressBar userWoorks_loding;

    private String userID;
    private int start=0;
    private int num=10;
    private BaseRecyclerAdapter<Works> adapter;
    private int dbuserID;
    @Override
    public int bindLayout() {
        return R.layout.fragment_userinfo_works;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        dbuserID = (int) SPUtils.getInstance(mContext).getParam("userID",0);
    }

    @Override
    protected void initData() {
        userinfo_works_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
    }
    @Override
    public void widgetClick(View view) {
    }

    @Override
    public Params getParams() {
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        if(userID.equals(dbuserID+"")){ //查询本地作品
            List<Works> worksList = DBHelper.selectWorksAll();
            if(null==worksList||worksList.size()<=0){return null;}
            //自己的作品，直接返回，不去请求服务器
            setData(worksList,false);
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID);
        map.put(HttpConfig.NUM,num+"");
        map.put(HttpConfig.START,start+"");
        return new Params(HttpConfig.getWorksAllData, map,Works.class,true);
    }

    @Override
    protected void showError(String result) {
        userWoorks_loding.setVisibility(View.GONE);
    }


    @Override
    protected void setData(Object result,boolean isRefresh) {
        userWoorks_loding.setVisibility(View.GONE);
        List<Works> worksList = (List<Works>) result;
        if(null==worksList||worksList.size()<=0){
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        userinfo_works_recycler.setVisibility(View.VISIBLE);
        data_error.setVisibility(View.GONE);
        adapter = new BaseRecyclerAdapter<Works>(mContext,worksList,R.layout.userinfo_works_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, Works item, int i) {
                //baseRecyclerHolder.setText(R.id.comment_content,works+"");
                holder.setText(R.id.userWorks_item_userName,item.getUserName()+"");
                ImageView home_item_works_img = holder.getView(R.id.userWorks_item_img);
                Glide.with(mContext).load(item.getWorksImage()).placeholder(R.mipmap.img_placeholder).thumbnail(0.1f).into(home_item_works_img);
                ImageView coll_userHead = holder.getView(R.id.userWorks_item_userHead);
                Glide.with(mContext).load(item.getUserHead()).centerCrop().into(coll_userHead);
                holder.setText(R.id.userWorks_item_releaseTime,item.getWorksReleaseTime());
                holder.setText(R.id.userWorks_item_des,item.getWorksDescribe());
                holder.setText(R.id.userWorks_commentNumber,item.getWorksCommentNumber()+"");
                holder.setText(R.id.userWorks_likesNumber,item.getWorksLikeNumber()+"");
            }
        };
        userinfo_works_recycler.setAdapter(adapter);
        start = start+worksList.size();
    }

}

package z.pint.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import z.pint.constant.HttpConfig;
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

    @Override
    public int bindLayout() {
        return R.layout.fragment_userinfo_works;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    protected void initData() {
        userinfo_works_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        userWorks_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData();//加载更多
            }
        });

    }

    private void loadData() {
        /*Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(),params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                //成功
                if(null==adapter){
                    setData(result);
                    return;
                }
                if (StringUtils.isBlank(result)) {
                    userWorks_refreshLayout.finishLoadMore(false);//数据加载失败
                    userWorks_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                List<Works> likesList = GsonUtils.getGsonList(result, Works.class);
                if (null == likesList || likesList.size() <= 0) {
                    userWorks_refreshLayout.finishLoadMore(false);//数据加载失败
                    userWorks_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                adapter.insertAll(likesList);
                start = start + likesList.size();
                userWorks_refreshLayout.finishLoadMore(true);//数据加载成功
                userWorks_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }

            @Override
            public void onFail(String result) {
                //失败
                userWorks_refreshLayout.finishLoadMore(false);//数据加载失败
            }
        });*/

    }

    @Override
    public void widgetClick(View view) {

    }

    @Override
    public Params getParams() {
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        if(userID==null){return null;}
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID);
        map.put(HttpConfig.NUM,num+"");
        map.put(HttpConfig.START,start+"");
        //new Params(HttpConfig.getWorksAllData, map)
        return null;
    }


    @Override
    protected void showError(String result) {

    }

    @Override
    protected void showLoadError(String result) {

    }

    @Override
    protected void setLoadData(Object result) {

    }

    @Override
    protected void setData(Object result,boolean isRefresh) {
        /*userWoorks_loding.setVisibility(View.GONE);
        if(StringUtils.isBlank(result)){data_error.setVisibility(View.VISIBLE);return;}
        List<Works> worksList = GsonUtils.getGsonList(result, Works.class);
        if(null==worksList||worksList.size()<=0){
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        userinfo_works_recycler.setVisibility(View.VISIBLE);
        data_error.setVisibility(View.GONE);
        adapter = new BaseRecyclerAdapter<Works>(mContext,worksList,R.layout.userinfo_works_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, Works works, int i) {
                //baseRecyclerHolder.setText(R.id.comment_content,works+"");

            }
        };
        userinfo_works_recycler.setAdapter(adapter);
        start = start+worksList.size();
        userWorks_refreshLayout.setEnableLoadMore(true);*/
    }

}

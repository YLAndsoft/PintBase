package z.pint.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import f.base.BaseLazyLoadFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.bean.Likes;
import z.pint.constant.HttpConfig;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewDivider;

/**
 * 详情点赞页面
 * Created by DN on 2018/6/26.
 */

public class DetialsLaudFragment extends BaseLazyLoadFragment {

    @ViewInject(value = R.id.details_laud_recycler)
    private RecyclerView details_laud_recycler;
    @ViewInject(value = R.id.details_likes_refreshLayout)
    private SmartRefreshLayout details_likes_refreshLayout;
    @ViewInject(value = R.id.data_error)
    private LinearLayout data_error;
    @ViewInject(value = R.id.comment_loding)
    private ProgressBar comment_loding;

    private BaseRecyclerAdapter<Likes> adapter;
    private int start=0;
    private int num=10;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private String worksID;

    @Override
    public int bindLayout() {
        return R.layout.details_laud_fragment;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        isPrepared = true;//标识已经初始化完成
        Bundle arguments = getArguments();
        worksID = arguments.getString("worksID");
        String works = worksID;
    }

    @Override
    public Params getParams() { //设置网络请求参数及地址
        Map<String, String> map = new HashMap<>();
        //map.put(HttpConfig.USER_ID, 1 + "");
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.WORKS_ID, worksID + "");
        map.put(HttpConfig.ACTION_STATE, HttpConfig.SELECT_STATE + "");
        //new Params(HttpConfig.getHomeData,map);
        return new Params(HttpConfig.getHomeData, map,Likes.class,true);
    }


    @Override
    protected void initData() {
        //List<Likes> list = DataUtils.getLikesData();//测试数据
        if(!isVisible || !isPrepared || mHasLoadedOnce){
            return;
        }
        mHasLoadedOnce = true;//标识已经加载过
        details_laud_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        details_laud_recycler.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary));
        details_likes_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //loadData();//加载更多
                Params params = getParams();
                //loadData(params);
            }
        });
        getData(getParams());
    }

    @Override
    public void widgetClick(View view) {
    }

    /**
     * 网络请求数据回调
     * @param result
     * @param isRefresh
     */
    @Override
    protected void setData(Object result,boolean isRefresh) {
        //绑定网络数据
        comment_loding.setVisibility(View.GONE);//关闭正在加载控件
        List<Likes> likesList = (List<Likes>) result;
        if(null==likesList||likesList.size()<=0){
            details_likes_refreshLayout.setEnableLoadMore(false);//没有数据了，禁止上拉
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        setLikeAdapter(likesList);
    }

    /**
     * 绑定适配器
     * @param likesList
     */
    private void setLikeAdapter(List<Likes> likesList) {
        adapter = new BaseRecyclerAdapter<Likes>(mContext,likesList,R.layout.details_works_likes_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, Likes likes, int i) {
                ImageView details_likes_userHead = baseRecyclerHolder.getView(R.id.details_likes_userHead);
                ViewUtils.setImageUrl(mContext,details_likes_userHead,likes.getUserHead(),R.mipmap.default_head_image);
                baseRecyclerHolder.setText(R.id.details_likes_userName,likes.getUserName()+"");
                //关注图标，需做特殊处理
            }
        };
        details_laud_recycler.setAdapter(adapter);
        adapter.updateAll(likesList.size());
        details_laud_recycler.setVisibility(View.VISIBLE);
        data_error.setVisibility(View.GONE);
        details_likes_refreshLayout.setEnableLoadMore(likesList.size()>=num);//打开加载更多
        start = start + likesList.size();
    }

    /**
     * 加载更多错误或者没有更多数据回调
     * @param result
     */
    /*@Override
    protected void showLoadError(String result) {
        details_likes_refreshLayout.finishLoadMore(false);//数据加载失败
        details_likes_refreshLayout.setEnableLoadMore(false);//关闭加载更多
    }*/
    @Override
    protected void showError(String result) {
        //服务器返回空数据
        data_error.setVisibility(View.VISIBLE);
        comment_loding.setVisibility(View.GONE);//关闭正在加载控件
    }
    /**
     * 加载更多数据回调
     * @param result
     */
    /*@Override
    protected void setLoadData(Object result) {
        if(null==adapter){
            setData(result,false);
            return;
        }
        List<Likes> likesList = (List<Likes>) result;
        if (null == likesList || likesList.size() <= 0) {
            details_likes_refreshLayout.finishLoadMore(false);//数据加载失败
            details_likes_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            return;
        }
        adapter.insertAll(likesList);
        start = start + likesList.size();
        details_likes_refreshLayout.finishLoadMore(true);//数据加载成功
        details_likes_refreshLayout.setEnableLoadMore(likesList.size()>=num);//打开加载更多
    }*/


}

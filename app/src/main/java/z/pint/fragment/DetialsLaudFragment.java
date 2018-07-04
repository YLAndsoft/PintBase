package z.pint.fragment;

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
 * Created by DN on 2018/6/26.
 */

public class DetialsLaudFragment extends BaseLazyLoadFragment {

    @ViewInject(value = R.id.details_laud_recycler)
    private RecyclerView details_laud_recycler;
    @ViewInject(value = R.id.details_likes_refreshLayout)
    private SmartRefreshLayout details_likes_refreshLayout;
    @ViewInject(value = R.id.data_error)
    private ImageView data_error;
    @ViewInject(value = R.id.comment_loding)
    private ProgressBar comment_loding;

    private BaseRecyclerAdapter<Likes> adapter;
    private int start=0;
    private int num=10;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    @Override
    public int bindLayout() {
        return R.layout.details_laud_fragment;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        isPrepared = true;//标识已经初始化完成
    }

    @Override
    public Params getParams() { //设置网络请求参数及地址
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID, 1 + "");
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.WORKS_ID, 1 + "");
        map.put(HttpConfig.ACTION_STATE, HttpConfig.SELECT_STATE + "");
        //new Params(HttpConfig.getHomeData,map);
        return new Params(HttpConfig.getHomeData, map);
    }

    @Override
    protected void initData() {
        //List<Likes> list = DataUtils.getLikesData();
        if(!isVisible || !isPrepared || mHasLoadedOnce){
            return;
        }
        mHasLoadedOnce = true;//标识已经加载过
        details_laud_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        details_laud_recycler.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary));
        details_likes_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData();//加载更多
            }
        });
        Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                setData( result);
            }
            @Override
            public void onFail(String result) {
                setData( result);
            }
        });

    }

    @Override
    public void widgetClick(View view) {
    }

    @Override
    protected void setData(String result) {
        //绑定网络数据
        comment_loding.setVisibility(View.GONE);//关闭正在加载控件
        if(StringUtils.isBlank(result)){
            //服务器返回空数据
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        List<Likes> likesList = GsonUtils.getGsonList(result, Likes.class);
        if(null==likesList||likesList.size()<=0){
            details_likes_refreshLayout.setEnableLoadMore(false);//没有数据了，禁止上拉
            data_error.setVisibility(View.VISIBLE);
            return;
        }
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
        details_likes_refreshLayout.setEnableLoadMore(true);//打开加载更多
        start = start + likesList.size();
    }

    /**
     * 加载更多
     */
    private void loadData() {
        Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if(null==adapter){
                    setData(result);
                    return;
                }
                if (StringUtils.isBlank(result)) {
                    details_likes_refreshLayout.finishLoadMore(false);//数据加载失败
                    details_likes_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                List<Likes> likesList = GsonUtils.getGsonList(result, Likes.class);
                if (null == likesList || likesList.size() <= 0) {
                    details_likes_refreshLayout.finishLoadMore(false);//数据加载失败
                    details_likes_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                adapter.insertAll(likesList);
                start = start + likesList.size();
                details_likes_refreshLayout.finishLoadMore(true);//数据加载成功
                details_likes_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                details_likes_refreshLayout.finishLoadMore(false);//数据加载失败
            }
        });
    }


}

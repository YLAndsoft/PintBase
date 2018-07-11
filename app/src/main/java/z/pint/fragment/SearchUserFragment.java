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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.base.BaseFragment;
import f.base.BaseLazyLoadFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.bean.Likes;
import z.pint.bean.User;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewDivider;

/**
 * Created by DN on 2018/7/6.
 */

public class SearchUserFragment extends BaseFragment {

    @ViewInject(value = R.id.search_refreshLayout)
    private SmartRefreshLayout search_refreshLayout;
    @ViewInject(value = R.id.search_recycler)
    private RecyclerView search_recycler;
    @ViewInject(value = R.id.search_loding)
    private ProgressBar search_loding;
    @ViewInject(value = R.id.search_error)
    ImageView search_error;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private String searchContent;//搜索的内容
    private int userID;
    private int start=0,num = 10;

    private BaseRecyclerAdapter<User> adapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_user;
    }
    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        isPrepared = true;//标识已经初始化完成
        userID =  SPUtils.getUserID(mContext);
    }

    /**
     * 初始化配置
     */
    @Override
    protected void initData() {
        search_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        search_recycler.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL,1,R.color.gary));
        search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
        search_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Params params = getParams();
                getData(params);
            }
        });
    }

    @Override
    public void widgetClick(View v) {
    }

    /**
     * 提交搜索
     * @param searchContent
     */
    public  void submitSearch(String searchContent){
        this.searchContent = searchContent;
        search_loding.setVisibility(View.GONE);
        Params params = getParams();
        getData(params);
    }

    /**
     * 获取网络请求参数
     * @return
     */
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.SEARCH_CONTENT,searchContent+"");
        map.put(HttpConfig.APPNAME,getResources().getString(R.string.app_name)+"");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        map.put(HttpConfig.USER_ID,userID+"");
        map.put(HttpConfig.START,start+"");
        map.put(HttpConfig.NUM,num+"");
        return new Params(HttpConfig.getSearchData, map, User.class,true);
    }

    /**
     * 数据回调
     * @param result
     * @param isRefresh
     */
    @Override
    protected void setData(Object result, boolean isRefresh) {
        search_loding.setVisibility(View.GONE);
        List<User> userList = (List<User>) result;
        if(null==userList||userList.size()<=0){
            search_refreshLayout.setEnableLoadMore(false);//没有数据了，禁止上拉
            search_error.setVisibility(View.VISIBLE);
            return;
        }
        if(adapter==null){
            setUserAdapter(userList);
        }else{
            adapter.insertAll(userList);
            search_refreshLayout.finishLoadMore(true);//数据加载成功
        }
        search_recycler.setVisibility(View.VISIBLE);
        search_error.setVisibility(View.GONE);
        search_refreshLayout.setEnableLoadMore(userList.size()>=num);//打开加载更多
        if(userList.size()<num){
            isLoadData=true;//没有更多数据
        }
        start = start + userList.size();
    }


    /**
     * 绑定适配器
     * @param userList
     */
    private void setUserAdapter(List<User> userList) {
        adapter = new BaseRecyclerAdapter<User>(mContext,userList,R.layout.details_works_likes_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, User user, int i) {
                ImageView details_likes_userHead = baseRecyclerHolder.getView(R.id.details_likes_userHead);
                ViewUtils.setImageUrl(mContext,details_likes_userHead,user.getUserHead(),R.mipmap.default_head_image);
                baseRecyclerHolder.setText(R.id.details_likes_userName,user.getUserName()+"");
                //关注图标，需做特殊处理
            }
        };
        search_recycler.setAdapter(adapter);
    }

    /**
     * 加载更多数据回调
     * @param result
     */
   /* @Override
    protected void setLoadData(Object result) {
        if(adapter==null){
            setData(result,false);
            return;
        }
        List<User> user = (List<User>) result;
        if (null == user || user.size() <= 0) {
            search_refreshLayout.finishLoadMore(false);//数据加载失败
            search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            return;
        }
        adapter.insertAll(user);
        setEnableLoadData(user.size()<num);
        start = start + user.size();
        search_refreshLayout.finishLoadMore(true);//数据加载成功
        search_refreshLayout.setEnableLoadMore(user.size()>=num);//打开加载更多
    }*/

    /**
     * 错误回调
     * @param result
     */
    @Override
    protected void showError(String result) {
        search_loding.setVisibility(View.GONE);
        search_error.setVisibility(View.VISIBLE);
        showLog(3,"搜索结果："+result);
    }

    /**
     * 加载更多错误数据回调
     * @param result
     */
    /*@Override
    protected void showLoadError(String result) {
        search_loding.setVisibility(View.GONE);
        search_refreshLayout.finishLoadMore(false);//数据加载失败
        search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
    }*/

}

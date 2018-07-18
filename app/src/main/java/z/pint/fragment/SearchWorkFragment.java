package z.pint.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import f.base.BaseLazyLoadFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.activity.WorksDetailsActivity;
import z.pint.adapter.SearchWorkAdapter;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.SPUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewItemDecoration;

/**
 * 展示作品搜索内容
 * Created by DN on 2018/7/6.
 */

public class SearchWorkFragment extends BaseLazyLoadFragment implements BaseRecyclerHolder.OnViewClickListener{
    @ViewInject(value = R.id.search_refreshLayout)
    private SmartRefreshLayout search_refreshLayout;
    @ViewInject(value = R.id.search_recycler)
    private RecyclerView search_recycler;
    @ViewInject(value = R.id.search_error)
    private ImageView search_error;

    private String searchContent="";//搜索的内容
    private int userID;//用户ID
    private int start=0,num = 10;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    //private BaseRecyclerAdapter<Works> adapter;//适配器
    private SearchWorkAdapter searchWorkAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_work;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        isPrepared = true;//标识已经初始化完成
        userID = SPUtils.getUserID(mContext);
        initRecycler();
    }

    /**
     * 初始化recycler
     */
    private void initRecycler() {
        //设置管理器
        search_recycler.setLayoutManager(ViewUtils.getStaggeredGridManager(2));
        search_recycler.setHasFixedSize(true);
        search_recycler.setNestedScrollingEnabled(false);
        //添加分割线
        search_recycler.addItemDecoration(new RecyclerViewItemDecoration(32));
        //List<Works> worksList = getListData();
        search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
        search_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //Params params = getParams();
                //loadData(params);
                search_refreshLayout.finishLoadMore(true);
            }
        });
    }

    @Override
    protected void initData() {
        if(!isVisible || !isPrepared){
            return;
        }
        if(StringUtils.isBlank(searchContent)){
            mHasLoadedOnce=false;
            return;
        }
        if(!mHasLoadedOnce){
            mHasLoadedOnce=true;
            Params params = getParams();
            getData(params);
        }
    }

    @Override
    public void widgetClick(View v) {
    }

    /**
     * 配置参数
     * @return
     */
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.SEARCH_CONTENT,searchContent+"");
        map.put(HttpConfig.APPNAME,mContext.getResources().getString(R.string.app_name)+"");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID,userID+"");
        map.put(HttpConfig.START,start+"");
        map.put(HttpConfig.NUM,num+"");
        return new Params(HttpConfig.getSearchData, map, Works.class,true);
    }

    /**
     * 设置网络数据
     * @param result
     * @param isRefresh
     */
    @Override
    protected void setData(Object result, boolean isRefresh) {
        if(result==null){
            search_error.setVisibility(View.VISIBLE);
            search_refreshLayout.finishLoadMore(false);//数据加载失败
            search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            search_recycler.setVisibility(View.GONE);
            return;
        }
        List<Works> works = (List<Works>) result;
        if(null==works||works.size()<=0){
            search_error.setVisibility(View.VISIBLE);
            search_refreshLayout.finishLoadMore(false);//数据加载失败
            search_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            search_recycler.setVisibility(View.GONE);
            return;
        }
        search_error.setVisibility(View.GONE);
        if(searchWorkAdapter==null){
            setWorkAdapter(works);
        }else{
            searchWorkAdapter.refreshAll(works);
        }
        search_refreshLayout.setEnableLoadMore(works.size()>=num);//打开加载更多
        start = works.size();
        search_recycler.setVisibility(View.VISIBLE);
    }

    private void refreshData() {
       /* if(adapter==null)setWorkAdapter(worksList);//绑定适配器
        adapter.refreshAll(worksList);
        start = start + worksList.size();
        if(worksList.size()<num){
            isLoadData=true;//没有更多数据
            search_refreshLayout.setEnableLoadMore(false);//打开加载更多
        }
        search_refreshLayout.setEnableLoadMore(true);//打开加载更多
        search_refreshLayout.setEnableRefresh(true);//打开刷新
        search_refreshLayout.finishRefresh(false);//刷新成功
        search_error.setVisibility(View.GONE);
        search_recycler.setVisibility(View.VISIBLE);*/
    }

    /**
     * 绑定适配器
     * @param worksList
     */
    private void setWorkAdapter(List<Works> worksList) {
        searchWorkAdapter = new SearchWorkAdapter(mContext,worksList);
        //绑定适配器
        search_recycler.setAdapter(searchWorkAdapter);
    }

    /**
     * 显示网络数据错误信息
     * @param result
     */
    @Override
    protected void showError(String result) {
        search_error.setVisibility(View.VISIBLE);
        showLog(3,"搜索结果："+result);
    }

    /**
     * 提交搜索
     * @param content
     */
    public  void submitSearch(String content){
        start=0;
        if(!content.equals(searchContent)){
            this.searchContent = content;
            mHasLoadedOnce=false;//搜索的值改变，需要去获取新数据，
        }else{
            mHasLoadedOnce =true;
        }
        if(isVisible){
            mHasLoadedOnce =true;
            Params params = getParams();
            getData(params);
        }
    }

    /**
     * recyclerview item的点击事件
     * @param view
     * @param object
     * @param position
     */
    @Override
    public void onViewClick(View view, Object object, int position) {
        switch (view.getId()){
            case R.id.recommend_item_image:
            case R.id.recommend_item_des:
                //showToast("跳转到详情界面");
                Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                intent.putExtra("works",(Works)object);
                startActivity(intent);
                break;
        }
    }

}

package z.pint.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
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
import z.pint.activity.UserInfoActivity;
import z.pint.activity.WorksDetailsActivity;
import z.pint.bean.SearchData;
import z.pint.bean.Works;
import z.pint.bean.WorksClassify;
import z.pint.constant.HttpConfig;
import z.pint.utils.SPUtils;
import z.pint.utils.StatisticsUtils;
import z.pint.utils.ViewUtils;

/**
 * 首页
 * Created by DN on 2018/6/19.
 */

public class HomeFragment extends BaseFragment implements BaseRecyclerHolder.OnViewClickListener {
    @ViewInject(value = R.id.home_recycler)
    private RecyclerView home_recycler;
    @ViewInject(value = R.id.home_refreshLayout)
    private SmartRefreshLayout home_refreshLayout;
    @ViewInject(value = R.id.home_loding)
    private ProgressBar home_loding;
    @ViewInject(value = R.id.home_error)
    private ImageView home_error;

    private BaseRecyclerAdapter<Works> adapter;
    private int start = 0;
    private int num = 10;
    private boolean isData;
    private List<WorksClassify> worksClassifies;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this, mContextView);
        home_error.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //设置布局管理器
        home_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        //List<Works> worksList = getListData();
        home_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshData(true);
            }
        });
        home_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData(boolean isRefresh) {
        start = 0;//初始化参数
        num = 10;//初始化参数
        Params params = getParams();
        params.setRefresh(isRefresh);
        if(isRefresh){
            params.getMap().put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        }else{
            params.getMap().put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        }
        getData(params);
    }

    private void isLoadMore(boolean isMore){
        home_refreshLayout.finishLoadMore(isMore);//数据加载成功或者失败
        home_refreshLayout.setEnableLoadMore(isMore);//打开或者关闭加载更多
    }

    private void showErrorView(){
        home_loding.setVisibility(View.GONE);//关闭正在加载
        home_error.setVisibility(View.VISIBLE);
    }
    /**
     * 加载更多数据
     */
    private void loadData() {
        Params params = getParams();
        params.setRefresh(false);//不是刷新操作
        params.getMap().put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
        params.setClazz(null);//自己去解析
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if (StringUtils.isBlank(result)){
                    isLoadMore(false);//数据加载失败//关闭加载更多
                    return;
                }
                SearchData searchData = GsonUtils.getGsonObject(result, SearchData.class);
                if (null==searchData){
                    isLoadMore(false);//数据加载失败//关闭加载更多
                    return;
                }
                List<Works> works = searchData.getWorks();
                if (null == works || works.size() <= 0) {
                    isLoadMore(false);//数据加载失败//关闭加载更多
                    return;
                }
                adapter.insertAll(works);
                start = start + works.size();
                isLoadMore(true);//数据加载成功//打开加载更多
            }
            @Override
            public void onFail(String result) {
                isLoadMore(false);//数据加载失败//关闭加载更多
            }
        });

    }
    @Override
    public Params getParams() { //设置网络请求地址及参数
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.http_name));
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        int userID = (int) SPUtils.getInstance(mContext).getParam(HttpConfig.USER_ID, 0);
        map.put(HttpConfig.USER_ID, userID + "");
        return new Params(HttpConfig.getHomeData, map,SearchData.class,false);
    }
    private long mkeyTime;
    @Override
    protected void setData(Object result,boolean isRefresh) { //绑定网络数据
        SearchData gson = (SearchData) result;
        List<Works> worksList = gson.getWorks();//得到作品数据
        if(!isData){
            worksClassifies = gson.getWorksClassifies();//得到作品分类集合
            SPUtils.getInstance(mContext).setList("classifyName",worksClassifies);
        }
        if (null == worksList || worksList.size() <= 0) {
            if(isRefresh) { //是否是刷新操作
                if(isData){ //是否已经加载过数据
                    home_refreshLayout.finishRefresh(false);//数据刷新失败
                    home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                }else{
                    showErrorView();
                }
            }else{
                //显示错误布局
                showErrorView();
            }
            return;
        }
        if(adapter==null){
            adapter = new BaseRecyclerAdapter<Works>(mContext, worksList, R.layout.home_recycler_item_layout) {
                @Override
                public void convert(final BaseRecyclerHolder holder, final Works works, int position) {
                    ImageView home_item_works_img = holder.getView(R.id.home_item_works_img);
                    Glide.with(mContext).load(works.getWorksImage()).placeholder(R.mipmap.img_placeholder).thumbnail(0.1f).into(home_item_works_img);
                    ImageView home_item_userHead = holder.getView(R.id.home_item_userHead);
                    Glide.with(mContext).load(works.getUserHead()).centerCrop().into(home_item_userHead);
                    holder.setText(R.id.home_item_userName, works.getUserName() + "");
                    holder.setText(R.id.home_item_releaseTime, works.getWorksReleaseTime() + "");
                    holder.setText(R.id.home_item_des, works.getWorksDescribe() + "");
                    holder.setText(R.id.home_item_commentNumber, works.getWorksCommentNumber() + "");
                    final TextView home_item_likesNumber = holder.getView(R.id.home_item_likesNumber);
                    home_item_likesNumber.setText(works.getWorksLikeNumber() + "");//设置点赞数
                    final ImageView home_item_likes_img = holder.getView(R.id.home_item_likes_img);
                    ViewUtils.isLikes(works.isLikes(),home_item_likes_img);//设置是否点赞

                    holder.setOnViewClick(R.id.home_item_userInfo,works,position, HomeFragment.this);
                    holder.setOnViewClick(R.id.home_item_works_img, works,position,HomeFragment.this);
                    holder.setOnViewClick(R.id.home_item_des, works,position,HomeFragment.this);
                    holder.setOnViewClick(R.id.home_item_ll_comment, works,position,HomeFragment.this);
                    holder.setOnViewClick(R.id.home_item_ll_likes,works,position, new BaseRecyclerHolder.OnViewClickListener() {
                        @Override
                        public void onViewClick(View view, Object object,int position) {
                            if ((System.currentTimeMillis() - mkeyTime) > 3000) {
                                mkeyTime = System.currentTimeMillis();
                                //showToast("点赞成功");
                                boolean likes = ViewUtils.isLikes(!works.isLikes(), home_item_likes_img);
                                int likesNumber = ViewUtils.setLikesNumber(!works.isLikes(), home_item_likesNumber, works.getWorksLikeNumber());
                                works.setLikes(likes);
                                works.setWorksLikeNumber(likesNumber);
                                StatisticsUtils.isLikes(mContext,works.isLikes(),works);//作品点赞/取消点赞
                            } else {
                                showToast("点击不要这么快嘛~");
                            }
                        }
                    });
                }
            };
            //绑定适配器
            home_recycler.setAdapter(adapter);
        }
        if(isRefresh){
            adapter.refreshAll(worksList);
            home_refreshLayout.finishRefresh(true);//数据刷新成功
        }else{
            adapter.updateAll(worksList.size());
        }
        isData=true;//标识已经加载过数据
        start = start + worksList.size();
        home_refreshLayout.setEnableLoadMore(true);//打开加载更多
        home_refreshLayout.setEnableRefresh(true);//打开刷新
        home_error.setVisibility(View.GONE);
        home_loding.setVisibility(View.GONE);//关闭正在加载
        home_recycler.setVisibility(View.VISIBLE);
    }

    @Override
    protected void showError(String result) {
        showLog(3,result);
        home_loding.setVisibility(View.GONE);//关闭正在加载
        home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
        if(adapter==null){
            home_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.home_error:
                //点击重试
                home_loding.setVisibility(View.VISIBLE);
                home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                refreshData(false);
                break;
        }
    }

    @Override
    public void onViewClick(View view, Object object, int position) {
        switch (view.getId()) {
            case R.id.home_item_userInfo:
                //showToast("跳转到个人信息中心");
                Intent userinfo = new Intent(mContext, UserInfoActivity.class);
                Works works = (Works) object;
                userinfo.putExtra("userID",works.getUserID()+"");
                userinfo.putExtra("works",works);
                startActivity(userinfo);
                break;
            case R.id.home_item_works_img:
            case R.id.home_item_ll_comment:
            case R.id.home_item_des:
                //showToast("跳转到作品详情");
                Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                intent.putExtra("works",(Works)object);
                startActivity(intent);
                break;
        }
    }



}

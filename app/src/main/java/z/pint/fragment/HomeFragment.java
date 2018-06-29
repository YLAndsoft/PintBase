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
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.StatisticsUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/6/19.
 */

public class HomeFragment extends BaseFragment implements BaseRecyclerHolder.OnViewClickListener {
    @ViewInject(value = R.id.home_recycler)
    private RecyclerView home_recycler;
    @ViewInject(value = R.id.home_refreshLayout)
    private SmartRefreshLayout home_refreshLayout;
    @ViewInject(value = R.id.home_loding)
    private ProgressBar home_loding;

    private BaseRecyclerAdapter<Works> adapter;
    private List<Works> worksList = null;
    private int start = 0;
    private int num = 10;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this, mContextView);
    }

    @Override
    protected void initData() {
        //设置布局管理器
        home_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        //List<Works> worksList = getListData();
        home_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshData();
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
    private void refreshData() {
        //refreshAll
        start = 0;//初始化参数
        num = 10;//初始化参数
        Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if (StringUtils.isBlank(result)) return;
                List<Works> works = GsonUtils.getGsonList(result, Works.class);
                if (null == works || works.size() <= 0) {
                    home_refreshLayout.finishRefresh(false);//数据加载失败
                    home_refreshLayout.setEnableLoadMore(false);//打开加载更多
                    return;
                }
                adapter.refreshAll(works);
                start = start + worksList.size();
                home_refreshLayout.finishRefresh(true);//数据加载成功
                home_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                //setData(result);
                home_refreshLayout.finishRefresh(false);//数据加载失败
                home_refreshLayout.setEnableLoadMore(false);//打开加载更多
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadData() {
        Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if (StringUtils.isBlank(result)) return;
                List<Works> works = GsonUtils.getGsonList(result, Works.class);
                if (null == works || works.size() <= 0) {
                    home_refreshLayout.finishLoadMore(false);//数据加载失败
                    home_refreshLayout.setEnableLoadMore(false);//打开加载更多
                    return;
                }
                adapter.insertAll(works);
                start = start + worksList.size();
                home_refreshLayout.finishLoadMore(true);//数据加载成功
                home_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                //setData(result);
                home_refreshLayout.finishLoadMore(false);//数据加载失败
                home_refreshLayout.setEnableLoadMore(false);//打开加载更多
            }
        });
    }

    @Override
    public Params getParams() { //设置网络请求地址及参数
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.http_name));
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        //new Params(HttpConfig.getHomeData,map);
        return new Params(HttpConfig.getHomeData, map);
    }
    private long mkeyTime;
    @Override
    protected void setData(String result) { //绑定网络数据
        showLog("数据结果"+result);
        home_loding.setVisibility(View.GONE);
        home_refreshLayout.setEnableRefresh(true);//打开下拉刷新
        if (StringUtils.isBlank(result)) return;
        worksList = GsonUtils.getGsonList(result, Works.class);
        if (null == worksList || worksList.size() <= 0) {
            home_refreshLayout.setEnableLoadMore(false);//打开加载更多
            return;
        }
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
                            StatisticsUtils.isLikes(works.isLikes(),works);//作品点赞/取消点赞
                        } else {
                            showToast("点击不要这么快嘛~");
                        }
                    }
                });
            }
        };
        //绑定适配器
        home_recycler.setAdapter(adapter);
        adapter.updateAll();
        home_refreshLayout.setEnableLoadMore(true);//打开加载更多
        start = start + worksList.size();
        home_recycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void widgetClick(View view) {
    }

    @Override
    public void onViewClick(View view, Object object, int position) {
        switch (view.getId()) {
            case R.id.home_item_userInfo:
                //showToast("跳转到个人信息中心");
                startActivity(new Intent(mContext, UserInfoActivity.class));
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

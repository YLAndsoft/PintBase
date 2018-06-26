package z.pint.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import f.base.BaseFragment;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.activity.UserInfoActivity;
import z.pint.activity.WorksDetailsActivity;
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.GsonUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/6/19.
 */

public class HomeFragment extends BaseFragment implements BaseRecyclerHolder.OnViewClickListener {
    @ViewInject(value = R.id.home_recycler)
    private RecyclerView home_recycler;

    @ViewInject(value = R.id.home_refreshLayout)
    private SmartRefreshLayout home_refreshLayout;

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

    private boolean isLiskes = false;

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
                    home_refreshLayout.setEnableRefresh(false);//打开下拉刷新
                    home_refreshLayout.setEnableLoadMore(false);//打开加载更多
                    return;
                }
                adapter.refreshAll(works);
                start = start + worksList.size();
                home_refreshLayout.finishRefresh(true);//数据加载成功
                home_refreshLayout.setEnableRefresh(true);//打开下拉刷新
                home_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                //setData(result);
                home_refreshLayout.finishRefresh(false);//数据加载失败
                home_refreshLayout.setEnableRefresh(false);//打开下拉刷新
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
                    home_refreshLayout.setEnableRefresh(false);//打开下拉刷新
                    home_refreshLayout.setEnableLoadMore(false);//打开加载更多
                    return;
                }
                adapter.insertAll(works);
                start = start + worksList.size();
                home_refreshLayout.finishLoadMore(true);//数据加载成功
                home_refreshLayout.setEnableRefresh(true);//打开下拉刷新
                home_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                //setData(result);
                home_refreshLayout.finishLoadMore(false);//数据加载失败
                home_refreshLayout.setEnableRefresh(false);//打开下拉刷新
                home_refreshLayout.setEnableLoadMore(false);//打开加载更多
            }
        });
    }

    @Override
    public Params getParams() { //设置网络请求地址及参数
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.USER_ID, 1 + "");
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.app_name));
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        //new Params(HttpConfig.getHomeData,map);
        return new Params(HttpConfig.getHomeData, map);
    }

    @Override
    protected void setData(String result) { //绑定网络数据
        if (StringUtils.isBlank(result)) return;
        worksList = GsonUtils.getGsonList(result, Works.class);
        if (null == worksList || worksList.size() <= 0) {
            home_refreshLayout.setEnableLoadMore(false);//打开加载更多
            return;
        }
        adapter = new BaseRecyclerAdapter<Works>(mContext, worksList, R.layout.home_recycler_item_layout) {
            @Override
            public void convert(final BaseRecyclerHolder baseRecyclerHolder, Works works, int position) {
                ImageView home_item_works_img = baseRecyclerHolder.getView(R.id.home_item_works_img);
                if (position % 3 == 0) {
                    Glide.with(mContext).load(R.mipmap.ova).thumbnail(0.1f).into(home_item_works_img);
                } else {
                    Glide.with(mContext).load(works.getWorksImage()).thumbnail(0.1f).into(home_item_works_img);
                }
                ImageView home_item_userHead = baseRecyclerHolder.getView(R.id.home_item_userHead);
                Glide.with(mContext).load(works.getUserHead()).thumbnail(0.1f).centerCrop().into(home_item_userHead);
                baseRecyclerHolder.setText(R.id.home_item_userName, works.getUserName() + "");
                baseRecyclerHolder.setText(R.id.home_item_releaseTime, works.getWorksReleaseTime() + "");
                baseRecyclerHolder.setText(R.id.home_item_des, works.getWorksDescribe() + "");
                baseRecyclerHolder.setText(R.id.home_item_commentNumber, works.getWorksCommentNumber() + "");
                baseRecyclerHolder.setText(R.id.home_item_likesNumber, works.getWorksLikeNumber() + "");
                baseRecyclerHolder.setOnViewClick(R.id.home_item_userInfo, HomeFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.home_item_works_img, HomeFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.home_item_des, HomeFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.home_item_ll_comment, HomeFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.home_item_ll_likes, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view) {
                        if (!isLiskes) {
                            isLiskes = true;
                            baseRecyclerHolder.setImageResource(R.id.home_item_likes_img, R.mipmap.dynamic_love_hl);
                        } else {
                            isLiskes = false;
                            baseRecyclerHolder.setImageResource(R.id.home_item_likes_img, R.mipmap.dynamic_love);
                        }
                        showToast("点赞");
                        //发送网络请求dynamic_love_hl
                    }
                });
            }
        };
        //绑定适配器
        home_recycler.setAdapter(adapter);
        adapter.updateAll();
        home_refreshLayout.setEnableLoadMore(true);//打开加载更多
        start = start + worksList.size();
    }

    @Override
    public void widgetClick(View view) {

    }

    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.home_item_userInfo:
                showToast("跳转到个人信息中心");
                startActivity(new Intent(mContext, UserInfoActivity.class));
                break;
            case R.id.home_item_works_img:
            case R.id.home_item_ll_comment:
            case R.id.home_item_des:
                showToast("跳转到作品详情");
                startActivity(new Intent(mContext, WorksDetailsActivity.class));
                break;
        }
    }

    /**
     * 得到假数据
     *
     * @return
     */
    private List<Works> getListData() {
        List<Works> worksList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            worksList.add(new Works());
        }
        return worksList;
    }

}

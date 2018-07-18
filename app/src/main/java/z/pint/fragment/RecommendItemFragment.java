package z.pint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.DataUtils;
import z.pint.utils.ScreenUtils;
import z.pint.utils.UiUtils;
import z.pint.utils.ViewUtils;
import z.pint.view.RecyclerViewDivider;
import z.pint.view.RecyclerViewItemDecoration;

/**
 * 推荐页item界面
 * Created by DN on 2018/6/19.
 */

public class RecommendItemFragment extends BaseLazyLoadFragment implements BaseRecyclerHolder.OnViewClickListener{
    @ViewInject(value = R.id.recommend_refreshLayout)
    private SmartRefreshLayout recommend_refreshLayout;
    @ViewInject(value = R.id.recommend_recycler)
    private RecyclerView recommend_recycler;
    @ViewInject(value = R.id.recommend_loding)
    private ProgressBar recommend_loding;
    @ViewInject(value = R.id.data_error)
    private ImageView data_error;
    private String classifyID;
    private int start = 0;
    private int num=10;
    private BaseRecyclerAdapter<Works> adapter;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private List<Works> worksList = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_recommend_item_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        isPrepared=true;
    }
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.CLASSIFY_ID, classifyID + "");
        return new Params(HttpConfig.getRecommendItemData, map,Works.class,true);
    }

    @Override
    protected void showError(String result) {
        recommend_loding.setVisibility(View.GONE);
        if(!mHasLoadedOnce){
            data_error.setVisibility(View.VISIBLE);
        }
        recommend_refreshLayout.setEnableLoadMore(false);//关闭加载更多
    }

    @Override
    protected void initData() {
        if(!isVisible || !isPrepared || mHasLoadedOnce){
            return;
        }
        mHasLoadedOnce = true;//标识已经加载过
        Bundle bundle = getArguments();//从activity传过来的Bundle
        classifyID = bundle.getString("classifyID");
        //设置管理器
        recommend_recycler.setLayoutManager(ViewUtils.getStaggeredGridManager(2));
        recommend_recycler.setHasFixedSize(true);
        recommend_recycler.setNestedScrollingEnabled(false);
        //添加分割线
        recommend_recycler.addItemDecoration(new RecyclerViewItemDecoration(32));
        //List<Works> worksList = getListData();
        recommend_refreshLayout.setEnableLoadMore(false);//关闭加载更多
        recommend_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Params params = getParams();
                getData(params);
            }
        });
        Params params = getParams();
        getData(params);
    }


    @Override
    public void widgetClick(View view) {
    }

    @Override
    protected void setData( Object result, boolean isRefresh) {
        recommend_loding.setVisibility(View.GONE);
        if(null==result){
            if(!mHasLoadedOnce){
                data_error.setVisibility(View.VISIBLE);
            }
            isLoadData=true;
            recommend_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            return;
        }
        List<Works> workses = (List<Works>) result;
        if(null==workses||workses.size()<=0){
            if(!mHasLoadedOnce){
                data_error.setVisibility(View.VISIBLE);
            }
            recommend_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            return;
        }
        worksList.addAll(workses);
        if(adapter==null){
            bindAdapter();
        }else{
            //adapter.insertAll(workses);
            adapter.notifyDataSetChanged();
            recommend_refreshLayout.finishLoadMore(true);//数据加载成功或者失败
        }
        //adapter.refreshAll(worksList);
        //adapter.notifyDataSetChanged();
        data_error.setVisibility(View.GONE);
        recommend_refreshLayout.setEnableLoadMore(workses.size()>=num);//打开加载更多
        start = start + workses.size();
        recommend_recycler.setVisibility(View.VISIBLE);
    }

    private void bindAdapter() {
        adapter = new BaseRecyclerAdapter<Works>(mContext, worksList, R.layout.recommend_item_layout) {
            @Override
            public void convert(final BaseRecyclerHolder baseRecyclerHolder, Works works, int position) {
                ImageView recommend_item_image = baseRecyclerHolder.getView(R.id.recommend_item_image);
                Glide.with(mContext).load(works.getWorksImage()).thumbnail(0.1f).into(recommend_item_image);
                ImageView recommend_item_userhead = baseRecyclerHolder.getView(R.id.recommend_item_userhead);
                Glide.with(mContext).load(works.getUserHead()).centerCrop().into(recommend_item_userhead);
                baseRecyclerHolder.setText(R.id.recommend_item_username, works.getUserName() + "");
                baseRecyclerHolder.setText(R.id.recommend_item_des, works.getWorksDescribe() + "");
                baseRecyclerHolder.setOnViewClick(R.id.recommend_item_image, works,position,RecommendItemFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.recommend_item_des, works,position,RecommendItemFragment.this);
                baseRecyclerHolder.setOnViewClick(R.id.recommend_item_likes,works,position, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view, Object object,int position) {
                        showToast("点赞");
                        //发送网络请求dynamic_love_hl
                    }
                });
            }
        };
        //绑定适配器
        recommend_recycler.setAdapter(adapter);
    }

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

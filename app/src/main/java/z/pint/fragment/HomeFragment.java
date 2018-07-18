package z.pint.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import z.pint.R;
import z.pint.activity.UserInfoActivity;
import z.pint.activity.WorksDetailsActivity;
import z.pint.bean.EventBusEvent;
import z.pint.bean.SearchData;
import z.pint.bean.Works;
import z.pint.bean.WorksClassify;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.EventBusUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.SortUtils;
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
    @ViewInject(value = R.id.home_title)
    private TextView home_title;

    private int start=0,num=10;
    private int userID;//用户ID
    private boolean isClassifyData;//是否已经加载分类数据
    private boolean isLoad;//是否已经加载过数据
    private List<WorksClassify> worksClassifies;
    private List<Works> worksList = new ArrayList<>();//保存作品数据集合
    private BaseRecyclerAdapter<Works> homeAdapter; //适配器
    private long mkeyTime;//记录点击频率的时间
    private LinearLayoutManager layoutManager;//布局管理器

    /**
     * 绑定layout
     * @return
     */
    @Override
    public int bindLayout() {
        return R.layout.fragment_home_layout;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        //注册EventBus
        EventBusUtils.register(this);
        //获取登录用户ID
        userID = SPUtils.getUserID(mContext);
        ViewUtils.setTextView(home_title,getResources().getString(R.string.app_name));
    }

    /**
     * 初始化布局管理器，监听
     */
    @Override
    protected void initData() {
        //设置布局管理器
        layoutManager = ViewUtils.getLayoutManager(mContext);
        home_recycler.setLayoutManager(layoutManager);
        //List<Works> worksList = getListData();
        //刷新操作
        home_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                start=0;
                Params params = getParams();
                params.setRefresh(true);//刷新操作
                getData(params);
            }
        });
        //加载更多操作
        home_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Params params = getParams();
                params.setRefresh(false);//不是刷新操作
                params.getMap().put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
                getData(params);
            }
        });
        //获取本地作品
        List<Works> dbWorks = DBHelper.selectWorksAll();
        if(null!=dbWorks&&dbWorks.size()>0){
            worksList.addAll(dbWorks);//添加自己发布的作品
        }
        home_error.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.home_error:
                //点击重试
                home_loding.setVisibility(View.VISIBLE);
                home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                Params params = getParams();
                params.setRefresh(false);
                getData(params);
                break;
        }
    }

    /**
     * 初始化网络请求参数
     * @return
     */
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.http_name));
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.USER_ID, userID + "");
        return new Params(HttpConfig.getHomeData, map,SearchData.class,false);
    }

    /**
     * 设置网络数据
     * @param result
     * @param isRefresh
     */
    @Override
    protected void setData(Object result, boolean isRefresh) {
        home_loding.setVisibility(View.GONE);//关闭正在加载
        SearchData gson = (SearchData) result;
        if(null==gson)return;
        if(isRefresh){
            //刷新流程
            refreshData(result);
            return;
        }
        List<Works> works = gson.getWorks();//得到作品数据
        if(!isClassifyData){
            worksClassifies = gson.getWorksClassifies();//得到分类数据
            isClassifyData=true;//设置为true,再次刷新不再次获取。
            SPUtils.getInstance(mContext).setList("classifyName",worksClassifies);
        }
        if(null!=works&&works.size()>0){
            start = start + works.size();
            //添加数据
            worksList.addAll(works);
            SortUtils.sortList(worksList);//对作品进行排序
            if(homeAdapter==null){
                bindAdapter(worksList);//绑定适配器
            }else{
                homeAdapter.insertAll(works);
                home_refreshLayout.finishLoadMore(true);//数据加载成功或者失败
            }
            isLoad=true;//标识数据已经加载过
            home_refreshLayout.setEnableLoadMore(works.size()>=num);//打开加载更多
            home_refreshLayout.setEnableRefresh(true);//打开刷新
            home_error.setVisibility(View.GONE);
            home_recycler.setVisibility(View.VISIBLE);
        }else{
            if(!isLoad){ //没加载过数据的情况
                home_error.setVisibility(View.VISIBLE);
            }else{
                home_refreshLayout.finishLoadMore(false);//数据加载成功或者失败
            }
            home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
        }

    }

    /**
     * 绑定适配器
     * @param worksList
     */
    private void bindAdapter(List<Works> worksList) {
        homeAdapter = new BaseRecyclerAdapter<Works>(mContext, worksList, R.layout.home_recycler_item_layout) {
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
        home_recycler.setAdapter(homeAdapter);
    }

    /**
     * 刷新数据
     * @param result
     */
    private void refreshData(Object result) {
        SearchData gson = (SearchData) result;
        if(null!=gson){
            List<Works> works = gson.getWorks();
            if(!isClassifyData){
                worksClassifies = gson.getWorksClassifies();//得到分类数据
                isClassifyData=true;//设置为true,再次刷新不再次获取。
                SPUtils.getInstance(mContext).setList("classifyName",worksClassifies);
            }
            if(null!=works&&works.size()>0){
                if(null!=worksList&&worksList.size()>0){
                    worksList.clear();
                    worksList.addAll(works);
                }
                SortUtils.sortList(works);//对作品进行排序
                if(homeAdapter==null)bindAdapter(works);//绑定适配器
                homeAdapter.refreshAll(works);
                start = start + works.size();
                isLoad=true;//标识数据已经加载过
                home_refreshLayout.setEnableLoadMore(works.size()>=num);//打开加载更多
                home_refreshLayout.setEnableRefresh(true);//打开刷新
                home_refreshLayout.finishRefresh(true);//刷新成功
                home_error.setVisibility(View.GONE);
                home_recycler.setVisibility(View.VISIBLE);
            }else{
                if(!isLoad){ //没加载过数据的情况
                    home_error.setVisibility(View.VISIBLE);
                }
                home_refreshLayout.finishRefresh(true);//刷新失败
                home_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            }
        }else{
            if(!isLoad){ //没加载过数据的情况
                home_error.setVisibility(View.VISIBLE);
            }
            home_refreshLayout.finishRefresh(false);//数据刷新失败
        }
    }

    /**
     * 网络错误
     * @param result
     */
    @Override
    protected void showError(String result,boolean isRefresh) {
        showLog(3,result);
        if(!isLoad){ //没加载过数据的情况
            home_error.setVisibility(View.VISIBLE);
        }
        if(isRefresh){
            home_refreshLayout.finishRefresh(false);//数据刷新失败
        }
        home_loding.setVisibility(View.GONE);//关闭正在加载
    }

    /**
     * recyclerView item的点击事件
     * @param view
     * @param object
     * @param position
     */
    @Override
    public void onViewClick(View view, Object object, int position) {
        switch (view.getId()) {
            case R.id.home_item_userInfo:
                //showToast("跳转到个人信息中心");
                Intent userinfo = new Intent(mContext, UserInfoActivity.class);
                Works works = (Works) object;
                userinfo.putExtra("userID",works.getUserID()+"");
                startActivity(userinfo);
                break;
            case R.id.home_item_works_img:
            case R.id.home_item_ll_comment:
            case R.id.home_item_des:
                //showToast("跳转到作品详情");
                Intent intent = new Intent(mContext, WorksDetailsActivity.class);
                intent.putExtra("works",(Works)object);
                intent.putExtra("position",position);
                startActivity(intent);
                break;
        }
    }
    /**
     * 接收用户发布作品的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING,priority = 97)
    public void updateWorks(EventBusEvent event) {
        if(event.getCode()== EventBusUtils.EventCode.B){
            if(event!=null){
                Works works = (Works) event.getData();
                if(works!=null){
                    //showToast("发布成功！");
                    if(homeAdapter!=null){
                        homeAdapter.insert(works,0);
                        worksList.add(works);
                        homeAdapter.notifyDataSetChanged();
                    }
                }
            }
        }else if(event.getCode()== EventBusUtils.EventCode.C){
            int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (event.getPosition() - firstItemPosition >= 0) {
                //得到要更新的item的view
                View view = home_recycler.getChildAt(event.getPosition());
                if (null != home_recycler.getChildViewHolder(view)) {
                    //更新评论数量
                    View childAt = home_recycler.getChildAt(event.getPosition());
                    if(childAt==null){return;}
                    BaseRecyclerHolder holder  = (BaseRecyclerHolder) home_recycler.getChildViewHolder(childAt);
                    Works works = (Works) event.getData();
                    holder.setText(R.id.home_item_commentNumber,works.getWorksCommentNumber()+"");
                    holder.setText(R.id.home_item_likesNumber,works.getWorksLikeNumber()+"");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁EventBus
        EventBusUtils.unregister(this);
    }


}

package z.pint.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;
import z.pint.R;
import z.pint.bean.Comment;
import z.pint.constant.HttpConfig;
import z.pint.utils.ViewUtils;
import z.pint.view.ButtonPopupWindow;
import z.pint.view.RecyclerViewDivider;

/**
 * 作品详情里面的评论列表
 * Created by DN on 2018/6/26.
 */

public class DetailsCommentFragment extends BaseFragment implements BaseRecyclerHolder.OnViewClickListener{
    @ViewInject(value = R.id.details_comment_rootView)
    private LinearLayout details_comment_rootView;
    @ViewInject(value = R.id.details_comment_recycler)
    private RecyclerView details_comment_recycler;
    @ViewInject(value = R.id.comment_refreshLayout)
    private SmartRefreshLayout comment_refreshLayout;
    @ViewInject(value = R.id.data_error)
    private ImageView data_error;
    @ViewInject(value = R.id.comment_loding)
    private ProgressBar comment_loding;

    private BaseRecyclerAdapter<Comment> adapter;
    private ButtonPopupWindow buttonPopupWindow;
    private int start = 0;
    private int num = 10;
    @Override
    public int bindLayout() {
        return R.layout.details_comment_fragment;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
    }

    @Override
    public Params getParams() { //设置网络请求参数及地址
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.WORKS_ID,1+"");
        map.put(HttpConfig.ACTION_STATE,0+"");
        map.put(HttpConfig.START,start+"");
        map.put(HttpConfig.NUM,num+"");
        //new Params(HttpConfig.getCommentData,map)
        return new Params(HttpConfig.getCommentData,map);
    }
    @Override
    protected void initData() {
        //List<Comment> commentList = DataUtils.getCommentData();
        details_comment_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        details_comment_recycler.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary));
        comment_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData();//加载更多
            }
        });
    }

    @Override
    protected void setData(String result) {//绑定网络数据
        comment_loding.setVisibility(View.GONE);//关闭正在加载控件
        if(StringUtils.isBlank(result)){
            //服务器返回空数据
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        List<Comment> commentList = GsonUtils.getGsonList(result,Comment.class);
        if(null==commentList||commentList.size()<=0){
            comment_refreshLayout.setEnableLoadMore(false);//没有数据了，禁止上拉
            data_error.setVisibility(View.VISIBLE);
            return;
        }
        adapter = new BaseRecyclerAdapter<Comment>(mContext,commentList,R.layout.details_works_comment_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, Comment comment, int position) {
                baseRecyclerHolder.setText(R.id.comment_userName,comment.getUserName()+"");
                baseRecyclerHolder.setText(R.id.comment_content,comment.getCommentContent()+"");
                baseRecyclerHolder.setText(R.id.comment_tims,"2018-06-23 13:23:17");
                ImageView comment_userhead = baseRecyclerHolder.getView(R.id.comment_userhead);
                ViewUtils.setImageUrl(mContext,comment_userhead,comment.getUserHead(),R.mipmap.default_head_image);
                baseRecyclerHolder.setOnViewClick(R.id.comment_info,comment,position,DetailsCommentFragment.this);
            }
        };
        details_comment_recycler.setAdapter(adapter);
        adapter.updateAll();
        details_comment_recycler.setVisibility(View.VISIBLE);
        data_error.setVisibility(View.GONE);
        comment_refreshLayout.setEnableLoadMore(true);//打开加载更多
        start = start + commentList.size();
    }

    /**
     * 加载更多
     */
    private void loadData() {
        Params params = getParams();
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if (StringUtils.isBlank(result)) {
                    comment_refreshLayout.finishLoadMore(false);//数据加载失败
                    comment_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                List<Comment> commentList = GsonUtils.getGsonList(result, Comment.class);
                if (null == commentList || commentList.size() <= 0) {
                    comment_refreshLayout.finishLoadMore(false);//数据加载失败
                    comment_refreshLayout.setEnableLoadMore(false);//关闭加载更多
                    return;
                }
                adapter.insertAll(commentList);
                start = start + commentList.size();
                comment_refreshLayout.finishLoadMore(true);//数据加载成功
                comment_refreshLayout.setEnableLoadMore(true);//打开加载更多
            }
            @Override
            public void onFail(String result) {
                comment_refreshLayout.finishLoadMore(false);//数据加载失败
            }
        });
    }
    @Override
    public void widgetClick(View view) {
    }


    @Override
    public void onViewClick(View view, Object object, int position) {
        switch (view.getId()){
            case R.id.comment_info:
                //showToast("点击评论");
                if(buttonPopupWindow==null){
                    buttonPopupWindow = new ButtonPopupWindow(mContext, getBPWTabName(), new ButtonPopupWindow.OnSelectItemListener() {
                        @Override
                        public void onSelectItemOnclick(int position,String tabName) {
                            showToast("点击了"+tabName);
                            buttonPopupWindow.dismiss();
                        }
                    });
                }
                if(buttonPopupWindow.isShowing()){
                    buttonPopupWindow.dismiss();
                    return;
                }
                buttonPopupWindow.showPopupWindow(details_comment_rootView, Gravity.BOTTOM,0,0);
                break;
        }
    }

    private String[] getBPWTabName(){
        String [] bpw ={getResources().getString(R.string.details_comment_tabName1),
                getResources().getString(R.string.details_comment_tabName2),
                getResources().getString(R.string.details_comment_tabName3)};
        return bpw;
    }
}

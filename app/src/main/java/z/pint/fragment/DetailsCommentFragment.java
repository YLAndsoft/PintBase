package z.pint.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import z.pint.bean.Works;
import z.pint.constant.HttpConfig;
import z.pint.utils.DBHelper;
import z.pint.utils.SPUtils;
import z.pint.utils.TimeUtils;
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
    @ViewInject(value = R.id.comment_loding)
    private ProgressBar comment_loding;

    @ViewInject(value = R.id.error_comment)
    private TextView error_comment;

    private BaseRecyclerAdapter<Comment> adapter;
    private ButtonPopupWindow buttonPopupWindow;
    private int start = 0;
    private int num = 10;
    private Works works;
    private int dbUserID;
    private boolean isLoadData;//是否已经加载过数据
    private List<Comment> commentList = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.details_comment_fragment;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        works = (Works) getArguments().getSerializable("works");
        dbUserID = SPUtils.getUserID(mContext);
    }

    /**
     * 配置参数
     * @return
     */
    @Override
    public Params getParams() { //设置网络请求参数及地址
        if(works.getUserID()==dbUserID){
            //拿本地评论
            List<Comment> comments = DBHelper.queryComment(works.getWorksID());
            if(null==comments||comments.size()<=0){return null;}
            setData(comments,false);
            return null;
        }
        Map<String,String> map = new HashMap<>();
        map.put(HttpConfig.WORKS_ID,works.getWorksID()+"");
        map.put(HttpConfig.ACTION_STATE,HttpConfig.SELECT_STATE+"");
        map.put(HttpConfig.START,start+"");
        map.put(HttpConfig.NUM,num+"");
        return new Params(HttpConfig.getCommentData,map,Comment.class,true);
    }

    @Override
    protected void initData() {
        ViewUtils.setTextView(error_comment,getResources().getString(R.string.error_comment));
        //List<Comment> commentList = DataUtils.getCommentData();//假数据
        details_comment_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        details_comment_recycler.addItemDecoration(new RecyclerViewDivider(mContext,LinearLayoutManager.HORIZONTAL,1,R.color.gary));
        comment_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //loadData(getParams());//加载更多
            }
        });
    }

    /**
     * 绑定网络数据
     * @param result
     * @param isRefresh
     */
    @Override
    protected void setData(Object result,boolean isRefresh) {
        comment_loding.setVisibility(View.GONE);//关闭正在加载控件
        commentList = (List<Comment>) result;
        if(null==commentList||commentList.size()<=0){
            comment_refreshLayout.setEnableLoadMore(false);//没有数据了，禁止上拉
            error_comment.setVisibility(View.VISIBLE);
            return;
        }
        isLoadData=true;
        sortList(commentList);
        setCommentAdapter();
    }
    /**
     * 时间排序来显示
     * @param worksList
     */
    private void sortList(List<Comment> worksList) {
        Comparator<Comment> itemComparator = new Comparator<Comment>() {
            public int compare(Comment info1, Comment info2){
                Date data1 = TimeUtils.stringToDate(info1.getCommentTime(),"yyyy-MM-dd HH:mm:ss");
                Date data2 = TimeUtils.stringToDate(info2.getCommentTime(),"yyyy-MM-dd HH:mm:ss");
                return data2.compareTo(data1);
            }
        };
        Collections.sort(worksList, itemComparator);
    }
    /**
     * 绑定适配器
     * @param
     */
    private void setCommentAdapter() {
        adapter = new BaseRecyclerAdapter<Comment>(mContext,commentList,R.layout.details_works_comment_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder baseRecyclerHolder, Comment comment, int position) {
                baseRecyclerHolder.setText(R.id.comment_userName,comment.getUserName()+"");
                baseRecyclerHolder.setText(R.id.comment_content,comment.getCommentContent()+"");
                baseRecyclerHolder.setText(R.id.comment_tims,comment.getCommentTime()+"");
                ImageView comment_userhead = baseRecyclerHolder.getView(R.id.comment_userhead);
                ViewUtils.setImageUrl(mContext,comment_userhead,comment.getUserHead(),R.mipmap.default_head_image);
                baseRecyclerHolder.setOnViewClick(R.id.comment_info,comment,position,DetailsCommentFragment.this);
            }
        };
        details_comment_recycler.setAdapter(adapter);
        adapter.updateAll(commentList.size());
        details_comment_recycler.setVisibility(View.VISIBLE);
        error_comment.setVisibility(View.GONE);
        comment_refreshLayout.setEnableLoadMore(commentList.size()>=num);//打开加载更多
        start = start + commentList.size();
    }

    /**
     * 刷新评论
     * @param comment
     */
    public void refreshAdapter(Comment comment){
        if(comment==null){return;}
        if(adapter==null){
            commentList.add(comment);
            setCommentAdapter();
        }else{
            adapter.insert(comment,0);
        }
    }


    /**
     * 显示网络错误布局
     * @param result
     */
    @Override
    protected void showError(String result,boolean isRefresh) {
        //服务器返回空数据
        comment_loding.setVisibility(View.GONE);
        if(!isLoadData){
            error_comment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载更多错误回调
     * @param result
     */


    /**
     * 加载更多回调
     * @param
     */
   /* @Override
    protected void setLoadData(Object result) {
        List<Comment> commentList = (List<Comment>) result;
        if (null == commentList || commentList.size() <= 0) {
            comment_refreshLayout.finishLoadMore(false);//数据加载失败
            comment_refreshLayout.setEnableLoadMore(false);//关闭加载更多
            return;
        }
        adapter.insertAll(commentList);
        start = start + commentList.size();
        comment_refreshLayout.finishLoadMore(true);//数据加载成功
        comment_refreshLayout.setEnableLoadMore(commentList.size()>=num);//打开加载更多
    }*/


    @Override
    public void widgetClick(View view) {
    }

    /**
     * recyclerView item点击事件
     * @param view
     * @param object
     * @param position
     */
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

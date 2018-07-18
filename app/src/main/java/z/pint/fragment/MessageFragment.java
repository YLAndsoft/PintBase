package z.pint.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import f.base.bean.Params;
import z.pint.R;
import z.pint.adapter.MessageAdapter;
import z.pint.bean.Message;
import z.pint.constant.HttpConfig;
import z.pint.utils.DataUtils;
import z.pint.utils.SPUtils;
import z.pint.utils.SortUtils;
import z.pint.utils.ViewUtils;

/**消息界面
 * Created by DN on 2018/6/19.
 */

public class MessageFragment extends BaseFragment {
    @ViewInject(value = R.id.msg_refreshLayout)
    private SmartRefreshLayout msg_refreshLayout;
    @ViewInject(value = R.id.msg_recycler)
    private RecyclerView msg_recycler;
    @ViewInject(value = R.id.msg_loding)
    private ProgressBar msg_loding;
    @ViewInject(value = R.id.msg_error)
    private ImageView msg_error;
    @ViewInject(value = R.id.message_title)
    private TextView message_title;

    private int start=0,num=20,userID;
    private MessageAdapter adapter;
    private boolean isData;//是否加载过数据

    @Override
    public int bindLayout() {
        return R.layout.fragment_message_layout;
    }
    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        userID = SPUtils.getUserID(mContext);
        ViewUtils.setTextView(message_title,getResources().getString(R.string.message_title));
    }
    @Override
    public Params getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConfig.APPNAME, getResources().getString(R.string.http_name));
        map.put(HttpConfig.START, start + "");
        map.put(HttpConfig.NUM, num + "");
        map.put(HttpConfig.NOTIFY_USER_ID, userID + "");
        return new Params(HttpConfig.getMessageAll,map,Message.class,true);
    }

    @Override
    protected void setData(Object object,boolean isRefresh) {
        msg_loding.setVisibility(View.GONE);
        List<Message> msgs = (List<Message>) object;
        if(null==msgs||msgs.size()<=0){
            if(!isData){
                msg_error.setImageResource(View.VISIBLE);
            }
            if(isRefresh){
                msg_refreshLayout.finishRefresh(true);
            }
            msg_refreshLayout.setEnableLoadMore(true);//关闭下拉加载更多
            return;
        }
        //此处为测试数据
        /*List<Message> messageData = DataUtils.getMessageData();
        msgs.addAll(messageData);*/
        SortUtils.sortMessageList(msgs);
        if(adapter==null){
            //绑定适配器
            bindAdapter(msgs);
            if(isRefresh)msg_refreshLayout.finishRefresh(true);
        }else{
            if(isRefresh){
                adapter.refresh(msgs);
                msg_refreshLayout.finishRefresh(true);
            }else{
                adapter.addDataAll(msgs);
                msg_refreshLayout.finishLoadMore(true);
            }
        }
        start = start+msgs.size();
        msg_refreshLayout.setEnableLoadMore(msgs.size()>=num);
        msg_error.setImageResource(View.GONE);
        msg_recycler.setVisibility(View.VISIBLE);
    }

    /**
     * 绑定适配器
     * @param messages
     */
    private void bindAdapter(List<Message> messages) {
        adapter = new MessageAdapter(mContext,messages);
        msg_recycler.setAdapter(adapter);
    }

    @Override
    protected void showError(String result,boolean isRefresh) {
        if(!isData){
            msg_error.setImageResource(View.VISIBLE);
        }
        if(isRefresh){
            msg_refreshLayout.finishRefresh(true);
        }
        msg_loding.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        //设置布局管理器
        msg_recycler.setLayoutManager(ViewUtils.getLayoutManager(mContext));
        //List<Message> messageData = DataUtils.getMessageData();
        //bindAdapter(messageData);
        //刷新操作
        msg_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                start=0;
                Params params = getParams();
                params.setRefresh(true);//刷新操作
                getData(params);
            }
        });
        //加载更多操作
        msg_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Params params = getParams();
                params.setRefresh(false);//不是刷新操作
                //params.getMap().put(HttpConfig.ACTION_STATE,HttpConfig.ADD_STATE+"");
                getData(params);
            }
        });
    }

    @Override
    public void widgetClick(View view) {

    }

}

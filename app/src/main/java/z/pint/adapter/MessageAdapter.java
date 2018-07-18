package z.pint.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.activity.UserInfoActivity;
import z.pint.bean.Message;
import z.pint.bean.Works;
import z.pint.utils.TimeUtils;
import z.pint.utils.ViewUtils;

/**
 * Created by DN on 2018/7/12.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Message> messages;
    private LayoutInflater inflater;
    //定义三种常量  表示三种条目类型
    public static final int TYPE_SYSTEM = 1;
    public static final int TYPE_ATTENTION = 2;

    public MessageAdapter(Context mContext,List<Message> messages){
        this.mContext = mContext;
        this.messages = messages;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view;
        if(viewType==TYPE_ATTENTION){ //关注消息
            view = inflater.inflate(R.layout.attention_msg_item_layout,parent,false);
            return new AttentionHolder(view);
        }else if(viewType==TYPE_SYSTEM){ //系统消息
            view = inflater.inflate(R.layout.system_msg_item_layout,parent,false);
            return new SystemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if(holder instanceof AttentionHolder){
                AttentionHolder attentionHolder = (AttentionHolder) holder;
                ViewUtils.setTextView(attentionHolder.message_at_content,messages.get(position).getMessageContent());
                ViewUtils.setTextView(attentionHolder.message_at_useName,messages.get(position).getUserName());
                ViewUtils.setImageUrl(mContext,attentionHolder.message_at_userHead,messages.get(position).getUserHead(),R.mipmap.default_head_image);
                ViewUtils.setImageUrl(mContext,attentionHolder.message_at_icon,messages.get(position).getUserHead(),R.mipmap.default_head_image);
                String messageTime = messages.get(position).getMessageTime();
                if(!StringUtils.isBlank(messageTime)){
                    Date date = TimeUtils.stringToDate(messageTime, "yyyy-MM-dd HH:mm:ss");
                    String string = TimeUtils.dateToString(date, "yyyy-MM-dd");
                    ViewUtils.setTextView(attentionHolder.message_at_time,string);
                }
                attentionHolder.rl_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userinfo = new Intent(mContext, UserInfoActivity.class);
                        userinfo.putExtra("userID",messages.get(position).getAddUserID()+"");
                        mContext.startActivity(userinfo);
                    }
                });

            }else if(holder instanceof SystemHolder){
                SystemHolder systemHolder = (SystemHolder) holder;
                ViewUtils.setTextView(systemHolder.system_msg_content,messages.get(position).getMessageContent());
                String messageTime = messages.get(position).getMessageTime();
                if(!StringUtils.isBlank(messageTime)){
                    Date date = TimeUtils.stringToDate(messageTime, "yyyy-MM-dd HH:mm:ss");
                    String string = TimeUtils.dateToString(date, "yyyy-MM-dd");
                    ViewUtils.setTextView(systemHolder.system_msg_time,string);
                }
                ViewUtils.setTextView(systemHolder.system_msg_title,messages.get(position).getMessageTitle());
                String messageImage = messages.get(position).getMessageImage();
                ViewUtils.setImageUrl(mContext,systemHolder.system_msg_image,messageImage,R.mipmap.bg_woman);
            }
    }

    /**
     * 刷新数据
     * @param messages
     */
    public void refresh(List<Message> messages){
        if(null==messages||messages.size()<=0){return;}
        this.messages = null;
        this.messages = messages;
        notifyItemRangeChanged(this.messages.size(),0);//指定位置开始刷新
    }

    /**
     * 添加数据
     * @param messages
     */
    public void addDataAll(List<Message> messages){
        if(null==messages||messages.size()<=0){return;}
        this.messages.addAll(messages);
        notifyItemRangeChanged(((this.messages.size())-(messages.size())),0);//指定位置开始刷新
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(message.getMessageType()==TYPE_ATTENTION){
            return TYPE_ATTENTION;
        }else{
            return TYPE_SYSTEM;
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }


    private class AttentionHolder extends RecyclerView.ViewHolder {
        @ViewInject(value = R.id.message_at_time)
        private TextView message_at_time;
        @ViewInject(value = R.id.message_at_userHead)
        private ImageView message_at_userHead;
        @ViewInject(value = R.id.message_at_icon)
        private ImageView message_at_icon;
        @ViewInject(value = R.id.message_at_useName)
        private TextView message_at_useName;
        @ViewInject(value = R.id.message_at_content)
        private TextView message_at_content;
        @ViewInject(value = R.id.rl_info)
        private RelativeLayout rl_info;

        public AttentionHolder(View view) {
            super(view);
            x.view().inject(this,view);
        }
    }

    private class SystemHolder extends RecyclerView.ViewHolder {
        @ViewInject(value = R.id.system_msg_time)
        private TextView system_msg_time;
        @ViewInject(value = R.id.system_msg_image)
        private ImageView system_msg_image;
        @ViewInject(value = R.id.system_msg_title)
        private TextView system_msg_title;
        @ViewInject(value = R.id.system_msg_content)
        private TextView system_msg_content;

        public SystemHolder(View view) {
            super(view);
            x.view().inject(this,view);
        }
    }



}

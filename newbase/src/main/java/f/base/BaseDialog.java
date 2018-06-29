package f.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import f.base.utils.StringUtils;

/**
 * Created by DN on 2018/6/15.
 */

public class BaseDialog extends Dialog implements View.OnClickListener{

    private Button left;//取消按钮
    private Button right;//取消按钮
    private TextView title_message;//消息标题
    private TextView message;//消息提示文本
    private EditText edit_content;//编辑框内容
    private RadioGroup radiogroup;//单选
    private RadioButton radiobutton1;
    private RadioButton radiobutton2;

    private String title_text;
    private String msg_content;
    private String left_txt;
    private String rigth_txt;
    private int dialogState;
    private int listenerCode;
    private boolean isCanceled = true; //点击屏幕外是否关闭对话框
    private String checkText = "男";//单选框选择的内容，默认为男

    public static final int DIALOG_DEFAULT_STATE = 0;//默认对话框
    public static final int DIALOG_EDIT_STATE = 1;//对话框-编辑类型
    public static final int DIALOG_CHECK_STATE = 2;//对话框-单选类型
    private OnDialogClickListener listener; //点击回调监听

    public BaseDialog(@NonNull Context context,String message,boolean isCanceled,OnDialogClickListener listener) {
        super(context,R.style.MyDialog);//默认消息对话框
        this.msg_content = message;
        this.isCanceled = isCanceled;
        this.listener = listener;
    }


    public interface OnDialogClickListener{
        void onLeftClick(int listenerCode);//左边按钮回调
        void onRigthClick(String content,int listenerCode);//右边按钮回调
    }

    /**
     * @param context
     * @param title //标题
     * @param dialogState dialog类型
     * @param msg_content //消息内容
     * @param left_txt //左边按钮文字
     * @param rigth_txt //右边按钮文字
     */
    public BaseDialog(Context context,
                      int dialogState,
                      String title,
                      String msg_content,
                      String left_txt,
                      String rigth_txt,
                      boolean isCanceled,
                      OnDialogClickListener listener,
                      int listenerCode) {
        super(context,R.style.MyDialog);
        this.dialogState = dialogState;
        this.title_text = title;
        this.msg_content = msg_content;
        this.left_txt = left_txt;
        this.rigth_txt = rigth_txt;
        this.isCanceled = isCanceled;
        this.listener = listener;
        this.listenerCode = listenerCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_dialog_layout);

        setCanceledOnTouchOutside(isCanceled);//设置点击屏幕外关闭对话框
        //setCancelable(false);
        //初始化界面控件
        initView();
        initBtn();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        //单选组件的监听事件
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                checkText = rb.getText().toString();
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.left){
            listener.onLeftClick(listenerCode);
        }else if(view.getId()==R.id.right){
            if(dialogState==DIALOG_DEFAULT_STATE){
                listener.onRigthClick("",listenerCode);
            }else if(dialogState==DIALOG_EDIT_STATE){
                String content = edit_content.getText().toString().trim();
                listener.onRigthClick(content,listenerCode);
            }else if(dialogState==DIALOG_CHECK_STATE){
                listener.onRigthClick(checkText,listenerCode);
            }
        }
    }

    /**
     * 设置左右两边按钮
     */
    private void initBtn() {
        if(StringUtils.isBlank(left_txt)){ //设置左边按钮
            left.setText(R.string.left_text);
        }else{
            left.setText(left_txt);
        }
        if(StringUtils.isBlank(rigth_txt)){ //设置右边边按钮
            right.setText(R.string.right_text);
        }else{
            right.setText(rigth_txt);
        }
    }

    private void initData() {
        switch (dialogState){
            case DIALOG_DEFAULT_STATE:
                //默认
                title_message.setVisibility(View.GONE);//隐藏标题
                message.setText(StringUtils.isBlank(msg_content)?"":msg_content);
                message.setVisibility(View.VISIBLE);//显示消息内容
                break;
            case DIALOG_EDIT_STATE:
                //编辑
                title_message.setText(StringUtils.isBlank(title_text)?"":title_text);
                if(StringUtils.isBlank(title_text)){
                    title_message.setVisibility(View.GONE);//隐藏标题
                }else{
                    title_message.setVisibility(View.VISIBLE);//显示标题
                }
                edit_content.setVisibility(View.VISIBLE);//显示编辑框
                message.setVisibility(View.GONE);//隐藏消息内容
                radiogroup.setVisibility(View.GONE);//隐藏单选组件
                break;
            case DIALOG_CHECK_STATE:
                //单选
                title_message.setText(StringUtils.isBlank(title_text)?"":title_text);
                if(StringUtils.isBlank(title_text)){
                    title_message.setVisibility(View.GONE);//隐藏标题
                }else{
                    title_message.setVisibility(View.VISIBLE);//显示标题
                }
                radiogroup.setVisibility(View.VISIBLE);//显示单选组件
                edit_content.setVisibility(View.GONE);//隐藏编辑框
                message.setVisibility(View.GONE);//隐藏消息内容
                break;
        }
    }

    private void initView() {
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        title_message = (TextView)findViewById(R.id.title_message);
        edit_content = (EditText) findViewById(R.id.edit_content);
        message = (TextView)findViewById(R.id.message);
        radiogroup = (RadioGroup)findViewById(R.id.radiogroup);
        radiobutton1 = (RadioButton)findViewById(R.id.radiobutton1);
        radiobutton2 = (RadioButton)findViewById(R.id.radiobutton2);
    }


}

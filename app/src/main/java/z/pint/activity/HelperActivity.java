package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseActivity;
import f.base.bean.Params;
import f.base.utils.RandomUtils;
import f.base.utils.StringUtils;
import f.base.widget.SVP;
import z.pint.R;
import z.pint.utils.ViewUtils;

/**
 * 帮助反馈界面
 * Created by DN on 2018/7/16.
 */

public class HelperActivity extends BaseActivity {
    @ViewInject(value = R.id.default_titleName)
    private TextView default_titleName;
    @ViewInject(value = R.id.function_advise)
    private TextView function_advise; //功能建议
    @ViewInject(value = R.id.function_question)
    private TextView function_question;//性能问题
    @ViewInject(value = R.id.edit_content)
    private EditText edit_content; //改进内容
    @ViewInject(value = R.id.edit_contact)
    private EditText edit_contact;//联系方式
    @ViewInject(value = R.id.help_commit)
    private Button help_commit;
    @ViewInject(value = R.id.help_classifyTitie)
    private TextView help_classifyTitie;
    @ViewInject(value = R.id.help_content)
    private TextView help_content;
    @ViewInject(value = R.id.help_contact)
    private TextView  help_contact;

    private static final String FUNCTION_ADVISE = "功能建议";
    private static final String FUNCTION_QUESTION = "性能问题";
    private String function=FUNCTION_ADVISE;
    @Override
    public void initParms(Intent intent) {
        setSetActionBarColor(true, R.color.maintab_topbar_bg_color);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_helper;
    }

    @Override
    public void initView(View view) {
        x.view().inject(this);
    }

    @Override
    public void initListener() {
        function_advise.setOnClickListener(this);
        function_question.setOnClickListener(this);
        help_commit.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        ViewUtils .setTextView(default_titleName,getResources().getString(R.string.helper));
        ViewUtils .setTextView(help_classifyTitie,getResources().getString(R.string.help_classifyTitie));
        ViewUtils .setTextView(function_advise,getResources().getString(R.string.function_advise));
        ViewUtils .setTextView(function_question,getResources().getString(R.string.function_question));
        ViewUtils .setTextView(help_content,getResources().getString(R.string.help_content));
        ViewUtils .setTextView(help_contact,getResources().getString(R.string.help_contact));
        ViewUtils .setTextView(edit_contact,getResources().getString(R.string.edit_contact));
        ViewUtils .setTextView(edit_content,getResources().getString(R.string.edit_content));
        ViewUtils .setTextView(help_commit,getResources().getString(R.string.help_commit));
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.function_advise:
                function = FUNCTION_QUESTION;
                function_advise.setBackground(ContextCompat.getDrawable(mContext,R.drawable.helper_function_bg_shape));
                function_advise.setTextColor(ContextCompat.getColor(mContext,R.color.details_bg_label_color));
                function_question.setBackground(ContextCompat.getDrawable(mContext,R.drawable.helper_unfunction_bg_shape));
                function_question.setTextColor(ContextCompat.getColor(mContext,R.color.gary2));
                break;
            case R.id.function_question:
                function =FUNCTION_ADVISE;
                function_question.setBackground(ContextCompat.getDrawable(mContext,R.drawable.helper_function_bg_shape));
                function_question.setTextColor(ContextCompat.getColor(mContext,R.color.details_bg_label_color));
                function_advise.setBackground(ContextCompat.getDrawable(mContext,R.drawable.helper_unfunction_bg_shape));
                function_advise.setTextColor(ContextCompat.getColor(mContext,R.color.gary2));
                break;
            case R.id.help_commit:
                //提交
                String content = edit_content.getText().toString().trim();
                if(StringUtils.isBlank(content)){
                    showToast("写点什么意见在提交吧！");
                    return;
                }
                String contact = edit_contact.getText().toString().trim();
                StringBuffer sb = new StringBuffer();
                sb.append(function+":");//分类
                sb.append(content+"\n");//建议内容
                sb.append("联系方式:"+contact+"");//联系方式
                showLog(3,sb.toString()+"");
                SVP.showWithStatus(mContext,"提交中...");
                //启动定时器
                mHandler.postDelayed(runnable, 3000);
                break;

        }
    }
    /**
     * 创建定时器
     */
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(SVP.isShowing(mContext)){
                SVP.dismiss(mContext);
                showToast("提交成功!");
                edit_content.setText("");
                edit_contact.setText("");
            }
        }
    };
    @Override
    public Params getParams() {
        return null;
    }
    @Override
    protected void onSuccess(Params params) {
    }
    @Override
    protected void onErrors(Params params) {
    }


}

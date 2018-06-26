package z.pint.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.BaseFragment;
import f.base.bean.Params;
import z.pint.R;
import z.pint.activity.UserInfoActivity;

/**
 * Created by DN on 2018/6/19.
 */

public class UserFragment extends BaseFragment {
    @ViewInject(value = R.id.rl_user)
    private RelativeLayout rl_user;
    @ViewInject(value = R.id.ll_gz)
    private LinearLayout ll_gz;//关注
    @ViewInject(value = R.id.ll_fs)
    private LinearLayout ll_fs;//粉丝
    @ViewInject(value = R.id.ll_zp)
    private LinearLayout ll_zp;//作品
    @ViewInject(value = R.id.ll_collect)
    private LinearLayout ll_collect;//收藏
    @ViewInject(value = R.id.ll_advise)
    private LinearLayout ll_advise;//帮助
    @ViewInject(value = R.id.ll_setting)
    private LinearLayout ll_setting;//设置

    @Override
    public int bindLayout() {
        return R.layout.fragment_user_layout;
    }

    @Override
    protected void initView() {
        x.view().inject(this,mContextView);
        rl_user.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_advise.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_gz.setOnClickListener(this);
        ll_fs.setOnClickListener(this);
        ll_zp.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.rl_user:
                showToast("跳转到个人信息界面");
                startActivity(new Intent(mContext,UserInfoActivity.class));
                break;
            case R.id.ll_gz:
                showToast("跳转到关注界面");
                break;
            case R.id.ll_fs:
                showToast("跳转到粉丝界面");
                break;
            case R.id.ll_zp:
                showToast("跳转到作品界面");
                break;
            case R.id.ll_collect:
                showToast("跳转到收藏界面");
                break;
            case R.id.ll_advise:
                showToast("跳转到帮助界面");
                break;
            case R.id.ll_setting:
                showToast("跳转到设置界面");
                break;

        }
    }

    @Override
    public Params getParams() {
        return null;
    }

    @Override
    protected void setData(String s) {

    }

}

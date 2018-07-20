package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import f.base.MenuFragmentActivity;
import f.base.bean.Params;
import z.pint.R;
import z.pint.activity.WhiteBoardActivity;
import z.pint.fragment.HomeFragment;
import z.pint.fragment.MessageFragment;
import z.pint.fragment.RecommendFragment;
import z.pint.fragment.UserFragment;

public class MainActivity extends MenuFragmentActivity {

    private int flResId = R.id.fl_menu_container;
    private int[] tabResIds = { R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_3, R.id.iv_menu_4 };

    private int[] imageNormals = {R.mipmap.ic_home_actionbar0,
            R.mipmap.ic_home_actionbar1,
            R.mipmap.ic_home_actionbar2,
            R.mipmap.ic_home_actionbar3,
            R.mipmap.ic_home_actionbar4};
    private int[] imgsHovers = {
            R.mipmap.ic_home_select0,
            R.mipmap.ic_home_select1,
            R.mipmap.ic_home_actionbar2,
            R.mipmap.ic_home_select3,
            R.mipmap.ic_home_select4};
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private RecommendFragment searchFragment;
    private UserFragment userFragment;

    @ViewInject(value = R.id.main_menu_table)
    private LinearLayout main_menu_table;
    @ViewInject(value = R.id.iv_menu_0)
    private ImageView iv_menu_0;
    @ViewInject(value = R.id.iv_menu_1)
    private ImageView iv_menu_1;
    @ViewInject(value = R.id.iv_menu_2)
    private ImageView iv_menu_2;
    @ViewInject(value = R.id.iv_menu_3)
    private ImageView iv_menu_3;
    @ViewInject(value = R.id.iv_menu_4)
    private ImageView iv_menu_4;
    @Override
    public void initParms(Intent intent) {
        setSetActionBarColor(true,R.color.maintab_topbar_bg_color);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this,mContextView);
        super.initTab(tabResIds);
        main_menu_table.setBackgroundResource(R.color.white);
        //首次展现加载的fragment
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        switchFragment(flResId, homeFragment);
    }

    @Override
    public void initListener() {
        iv_menu_2.setOnClickListener(this);
    }
    @Override
    public void initData(Context context) {
    }
    @Override
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.iv_menu_2:
                startActivity(new Intent(mContext, WhiteBoardActivity.class));
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

    @Override
    protected boolean onTabClick(int tabId) {
        iv_menu_0.setImageResource(imageNormals[0]);
        iv_menu_1.setImageResource(imageNormals[1]);
        iv_menu_3.setImageResource(imageNormals[3]);
        iv_menu_4.setImageResource(imageNormals[4]);
        super.onTabClick(tabId);
        switch (tabId){
            case R.id.iv_menu_0:
                iv_menu_0.setImageResource(imgsHovers[0]);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                switchFragment(flResId, homeFragment);
                break;
            case R.id.iv_menu_1:
                iv_menu_1.setImageResource(imgsHovers[1]);
                if (searchFragment == null) {
                    searchFragment = new RecommendFragment();
                }
                switchFragment(flResId, searchFragment);
                break;
            case R.id.iv_menu_3:
                iv_menu_3.setImageResource(imgsHovers[3]);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                }
                switchFragment(flResId, messageFragment);
                break;
            case R.id.iv_menu_4:
                iv_menu_4.setImageResource(imgsHovers[4]);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                }
                switchFragment(flResId, userFragment);
                break;
        }
        return true;
    }

    private long mkeyTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                showToast("再按一次退出程序");
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

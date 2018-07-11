package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import f.base.BaseFragmentActivity;
import f.base.bean.Params;
import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.adapter.ClassifyViewPagerAdapter;
import z.pint.fragment.SearchUserFragment;
import z.pint.fragment.SearchWorkFragment;
import z.pint.view.PagerSlidingTabStrip;

/**
 * 搜索界面
 * Created by DN on 2018/7/6.
 */

public class SearchActivity extends BaseFragmentActivity {
    @ViewInject(value = R.id.search_edit)
    private EditText search_edit;
    @ViewInject(value = R.id.submit_search)
    private ImageView submit_search;
    @ViewInject(value = R.id.search_tabs)
    private PagerSlidingTabStrip search_tabs;
    @ViewInject(value = R.id.search_pager)
    private ViewPager search_pager;

    private ClassifyViewPagerAdapter classViewPagerAdapter;
    private SearchWorkFragment swf;
    private SearchUserFragment suf ;
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this);
        search_edit.setHint("输入搜索的作品或者用户");
    }
    @Override
    public void initListener() {
        submit_search.setOnClickListener(this);
        //监听软键盘上搜索按钮
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String content = search_edit.getText().toString().trim();
                    search(content);
                }
                return false;
            }
        });
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void search(String content) {
        if(StringUtils.isBlank(content)){
            showToast("搜素的内容不能为空!");
            return;
        }
        search_pager.setVisibility(View.VISIBLE);
        search_tabs.setVisibility(View.VISIBLE);
        swf.submitSearch(content);
        suf.submitSearch(content);
        showToast("搜索的内容为:"+content);
    }

    @Override
    public void initData(Context mContext) {
        classViewPagerAdapter = new ClassifyViewPagerAdapter(getSupportFragmentManager(), getTabName(), getFragments());
        search_pager.setAdapter(classViewPagerAdapter);
        search_pager.setOffscreenPageLimit(getTabName().size());//依据传过来的tab页的个数来设置缓存的页数
        //tabs.setFollowTabColor(true);//设置标题是否跟随
        search_tabs.setViewPager(search_pager);
       // search_pager.setVisibility(View.GONE);
       // search_tabs.setVisibility(View.GONE);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.submit_search:
                //提交搜索的内容
                String content = search_edit.getText().toString().trim();
                search(content);
                break;
        }
    }
    @Override
    public Params getParams() {
        return null;
    }
    @Override
    protected void setData(String result) {
    }

    private List<String> getTabName() {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.search_tabName1));
        list.add(getResources().getString(R.string.search_tabName2));
        return list;
    }
    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(getSWFFragment());
        list.add(getSUFFragment());
        return list;
    }
    private SearchWorkFragment getSWFFragment(){
        if(swf==null){
            swf = new SearchWorkFragment();
        }
        return swf;
    }
    private SearchUserFragment getSUFFragment(){
        if(suf==null){
            suf = new SearchUserFragment();
        }
        return suf;
    }
    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) return true;
        return onTouchEvent(ev);
    }
    /**
     * Return whether touch the view.判断点击是否是EditText区域
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],top = l[1],bottom = top + v.getHeight(),right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right&& event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}

package z.pint.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import f.base.BaseActivity;
import f.base.BaseRecyclerAdapter;
import f.base.BaseRecyclerHolder;
import f.base.bean.Params;
import f.base.utils.StringUtils;
import z.pint.R;
import z.pint.utils.ViewUtils;

/**
 * 选择标签
 * Created by DN on 2018/7/5.
 */

public class SelectTagActivity extends BaseActivity {
    @ViewInject(value = R.id.recycler_tag)
    private RecyclerView recycler_tag;
    @ViewInject(value = R.id.create_des)
    private EditText create_des;
    @ViewInject(value = R.id.submit_tag)
    private TextView submit_tag;
    @ViewInject(value = R.id.custom_tag)
    private LinearLayout custom_tag;

    private String tag = "";
    private String tmpTag = "";
    private Map<String,Boolean> selectMap = new HashMap<>();
    @Override
    public void initParms(Intent intent) {
        setAllowFullScreen(true);
        setScreenRoate(false);
        setSteepStatusBar(false);
        setSetActionBarColor(true, R.color.colorActionBar);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_select_tag;
    }
    @Override
    public void initView(View view) {
        x.view().inject(this);
    }


    @Override
    public void initListener() {
        submit_tag.setOnClickListener(this);

        create_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String[] tmp = tmpTag.split("//,");
                if(tmp.length>3){
                    create_des.setText("");
                    showToast("最多只能自定义三个标签！");
                    return;
                }
                String t = editable.toString();
                if(null!=t&&!t.equals("")){
                    boolean b1 = Pattern.compile("\\s+").matcher(t).find();
                    boolean b2 = !t.contains(",");
                    if(t.contains(",")){
                        addView(t);
                        return;
                    }
                    if(Pattern.compile("\\s+").matcher(t).find()){ //判断是否有空格存在
                        addView(t);
                        return;
                    }

                }
            }
        });
    }

    /**
     * 动态添加布局
     * @param t
     */
    private void addView(String t){
        //if(isAddView){return;}
        if(custom_tag.getChildCount()<3){
            String substring="";
            if(t.contains(",")){
                substring = t.substring(0, (t.length() - 1));
                tmpTag = tmpTag+t;
            }else if(t.contains(" ")){
                substring = t.trim();
                tmpTag = tmpTag+t+",";
            }
            View inflate = View.inflate(mContext, R.layout.custom_tag_item_layout, null);
            TextView txt = inflate.findViewById(R.id.custom_tag_txt);
            txt.setText("#  "+substring+"");
            custom_tag.addView(inflate);
            create_des.setText("");
        }else{
            showToast("最多只能自定义三个标签！");
        }
    }

    @Override
    public void initData(Context mContext) {
        recycler_tag.setLayoutManager(ViewUtils.getHorManager(mContext));
        final List<String> tags = createTag();
        for(int i = 0; i <tags.size();i++){
            selectMap.put(tags.get(i),false);
        }
        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(mContext,tags,R.layout.tag_item_layout) {
            @Override
            public void convert(BaseRecyclerHolder holder, final String item, int position) {
                final TextView itemTag = holder.getView(R.id.item_tag);
                itemTag.setText(item+"");
                Boolean aBoolean = selectMap.get(item);
                if(aBoolean){
                    itemTag.setBackgroundColor(getResources().getColor(R.color.details_bg_label_color));
                }else{
                    itemTag.setBackgroundColor(getResources().getColor(R.color.gary2));
                }
                holder.setOnViewClick(R.id.item_tag, item, position, new BaseRecyclerHolder.OnViewClickListener() {
                    @Override
                    public void onViewClick(View view, Object object, int position) {
                        for(int i = 0; i <tags.size();i++){
                            selectMap.put(tags.get(i),false);
                        }
                        selectMap.put(item,true);
                        tag = item+",";
                        notifyDataSetChanged();
                    }
                });
            }
        };
        recycler_tag.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case  R.id.submit_tag:
                tag = tag+tmpTag;
                //showToast("提交的标签是>>>"+tag);
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("tag", tag);
                //设置返回数据
                setResult(RESULT_OK, intent);
                //关闭Activity
                finish();
                break;
        }
    }

    private List<String> createTag(){
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.defult_tag1));
        list.add(getResources().getString(R.string.defult_tag2));
        list.add(getResources().getString(R.string.defult_tag3));
        list.add(getResources().getString(R.string.defult_tag4));
        list.add(getResources().getString(R.string.defult_tag5));
        list.add(getResources().getString(R.string.defult_tag6));
        list.add(getResources().getString(R.string.defult_tag7));
        return list;
    }



    @Override
    public Params getParams() {
        return null;
    }
    @Override
    protected void setData(String result) {
    }
}

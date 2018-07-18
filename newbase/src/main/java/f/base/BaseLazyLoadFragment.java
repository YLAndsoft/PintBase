package f.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import f.base.bean.Params;
import f.base.utils.GsonUtils;
import f.base.utils.NetworkUtils;
import f.base.utils.StringUtils;
import f.base.utils.XutilsHttp;

/**
 * Created by DN on 2018/1/10.
 */

public abstract class BaseLazyLoadFragment extends Fragment implements View.OnClickListener{
    protected View mContextView = null;
    protected Context mContext;
    protected boolean isVisible;//界面是否可见
    protected boolean isLoadData;//是否还有数据
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    /**
     * 此方法是在onCreateView之前调用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            initData_();
        }else{
            isVisible = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            mContextView = inflater.inflate(bindLayout(), null, false);
        }catch (Exception ex){
            showToast("绑定布局异常！");
            ex.printStackTrace();
            return null;
        }
        initView();//初始化控件
        if(isVisible){
            initData_();
        }
        return mContextView;
    }


    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int bindLayout();

    /**
     * [初始化控件]
     *
     */
    private void initData_() {
        initData();
    }
    /**
     * 控件的初始化
     */
    protected abstract void initView();

    /**
     * 数据显示
     */
    protected abstract void initData();

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    /** View点击 **/
    public abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * [吐出Toast]
     * @param msg
     */
    protected void showToast(String msg){
            if(null!=getActivity()){
                Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * [打印日志]
     * @param msg
     */
    protected final String TAG = "LazyLoadFragmentLog信息：";
    protected void showLog(int level,String msg){
        if(!BaseApplication.isLog){return;}
        switch (level){
            case Config.LEVEL_1:
                Log.v(TAG,msg);
                break;
            case Config.LEVEL_2:
                Log.d(TAG,msg);
                break;
            case Config.LEVEL_3:
                Log.e(TAG,msg);
                break;
            default:
                Log.i(TAG,msg);
                break;
        }
    }

    /**
     * 获取参数
     * @return
     */
    public abstract Params getParams();

    /**
     * 展示网络数据
     */
    protected abstract void setData(Object result,boolean isRefresh);
    /**
     * 显示错误视图
     */
    protected abstract void showError(String result);

    /**
     * 获取网络数据
     * @param params
     */
    public void getData(final Params params) {
        if(isLoadData)return; //没有更多数据，直接返回
        if(null==params){ //未配置参数
            showError(Config.PARAMS_ERROR);
            return;
        }
        if(!NetworkUtils.isConnected(mContext)){
            showError(Config.NETWORK_ERROR);
            return;
        }//网络未连接
        XutilsHttp.xUtilsPost(params.getURL(), params.getMap(), new XutilsHttp.XUilsCallBack() {
            @Override
            public void onResponse(String result) {
                if(StringUtils.isBlank(result)){
                    showError(result);
                    return;
                }
                if(params.getClazz()!=null){ //判断要解析的bean类是否存在
                    Object object;
                    if(params.isList()){  //得到解析数据是bean还是集合类型
                        object = GsonUtils.getGsonList(result, params.getClazz());
                    }else{
                        object = GsonUtils.getGsonObject(result, params.getClazz());
                    }
                    if(null!=object){
                        isLoadData = false;
                        setData(object,params.isRefresh());
                        return;
                    }
                    showError(result);
                }else{ //不存在，让用户自己去解析
                    setData(result,params.isRefresh());
                }
            }
            @Override
            public void onFail(String result) {
                showError(result);
            }
        });
    }

    public void setEnableLoadData(boolean isLoadData){
        this.isLoadData = isLoadData;
    }

}

package f.base;

/**
 * Created by DN on 2018/6/11.
 */

public class Config {

    private Config() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 日志级别：verbose
     */
    public static final int LEVEL_1=1;//正常输出级别verbose
    /**
     * 日志级别：debug
     */
    public static final int LEVEL_2=2;//debug输出日志级别
    /**
     * 日志级别：error
     */
    public static final int LEVEL_3=3;//错误级别error


    public static final String PARAMS_ERROR="未配置参数!";
    public static final String NETWORK_ERROR="网络未连接!";

}

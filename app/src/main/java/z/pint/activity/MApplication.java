package z.pint.activity;


import f.base.BaseApplication;
import f.base.db.DBManager;

/**
 * Created by DN on 2018/6/19.
 */

public class MApplication extends BaseApplication {
    @Override
    public void initDB() {
        //创建数据库
        DBManager.initDB();
    }

}

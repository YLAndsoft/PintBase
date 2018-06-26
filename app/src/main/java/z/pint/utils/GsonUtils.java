package z.pint.utils;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import f.base.utils.StringUtils;

/**
 * Created by DN on 2018/6/26.
 */

public class GsonUtils {
    private GsonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static <T> T getGsonObject(String data, Class<T> mClass) {
        if(StringUtils.isBlank(data))return null;
        try{
            T result = new Gson().fromJson(data, mClass);
            return result;
        }catch (Exception ex){
            return null;
        }
    }

    public static <T> List<T> getGsonList(String data, Class<T> klass) {
        if(StringUtils.isBlank(data))return null;
        try{
            return new Gson().fromJson(data, new ListOfSomething<T>(klass));
        }catch (Exception ex){
            return null;
        }
    }

    public static class ListOfSomething<X> implements ParameterizedType {
        private Class<?> wrapped;
        public ListOfSomething(Class<X> wrapped) {
            this.wrapped = wrapped;
        }
        public Type[] getActualTypeArguments() {
            return new Type[] {wrapped};
        }
        public Type getRawType() {
            return List.class;
        }
        public Type getOwnerType() {
            return null;
        }
    }
}

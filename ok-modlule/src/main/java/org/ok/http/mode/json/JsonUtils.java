package org.ok.http.mode.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/28.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class JsonUtils {

    private static JsonParse sJsonParse;

    public static void initJsonParse(JsonParse jsonParse) {
        sJsonParse = jsonParse;
    }


    /**
     * 获取接口泛型的实体类型
     */
    public static Class<?> analysisInterfaceInfo(Object object, String callbakName) {
        if (sJsonParse == null) {
            throw new NullPointerException("JsonParse is null ,please call method JsonUtils.initJsonParse().");
        }

        // 获取接口定义相关泛型
        Type[] genType = object.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = null;//(ParameterizedType) genType[0];
        // 找到对应的interface
        for (int i = 0; i < genType.length; i++) {
            try {
                parameterizedType = (ParameterizedType) genType[i];
                // 类名相匹配
                if (parameterizedType.toString().contains(callbakName)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // 普通泛型类型
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception e) {
            // list类型泛型
            ParameterizedType type = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
            // list对象中的泛型 List<String> -> clazz.getName() =  String
            Class clazz = (Class<?>) type.getActualTypeArguments()[0];
            // 拿list (Class<?>) type.getRawType()  -> (Class<?>) type.getRawType().getName() = List
            return clazz;
//            return (Class<?>) type.getRawType();
        }
    }


    /**
     * 解析成list
     *
     * @param data
     * @param tClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> gsonToArr(String data, Class<T> tClass) throws Exception {
        return sJsonParse.parseArray(data, tClass);
    }

    /**
     * 解析成list
     *
     * @param data
     * @param tClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T gsonToObj(String data, Class<T> tClass) throws RuntimeException {
        return sJsonParse.parseObj(data, tClass);
    }
}

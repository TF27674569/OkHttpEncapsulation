package org.ok.http.mode.json;

import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/28.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface JsonParse {

    /**
     * 解析成list
     *
     * @param jsonArr 正确的jsonArr对象
     * @param clazz   需要解析成的对象class
     * @return
     */
    <T> List<T> parseArray(String jsonArr, Class<T> clazz) throws RuntimeException;


    /**
     * 解析成普通对象
     *
     * @param json  正确的jsonArr对象
     * @param clazz 需要解析成的对象class
     * @return
     */
    <T> T parseObj(String json, Class<T> clazz) throws RuntimeException;
}

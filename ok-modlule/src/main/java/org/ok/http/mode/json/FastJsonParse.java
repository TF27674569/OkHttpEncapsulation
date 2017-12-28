package org.ok.http.mode.json;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/28.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class FastJsonParse implements JsonParse {
    @Override
    public <T> List<T> parseArray(String jsonArr, Class<T> clazz) throws RuntimeException {
        return JSON.parseArray(jsonArr, clazz);
    }

    @Override
    public <T> T parseObj(String json, Class<T> clazz) throws RuntimeException {
        return JSON.parseObject(json, clazz);
    }
}

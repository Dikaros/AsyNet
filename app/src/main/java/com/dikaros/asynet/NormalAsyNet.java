package com.dikaros.asynet;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 基础的AsyNet进行简单的Http信息传输
 * @author dikaros
 */
public class NormalAsyNet extends AsyNet<String> {





    /**
     * 无参方法
     * @param url
     * @param method
     */
    public NormalAsyNet(String url,NetMethod method){
        super(url);
        this.method = method;
    }

    /**
     * 基本构造方法传入json或xml文本
     * @param url 请求地址
     * @param key json或xml 名称
     * @param jsonOrXmlFile json或xml文本
     * @param method
     */
    public NormalAsyNet(String url, String key, String jsonOrXmlFile,
                        NetMethod method) {
        super(url, key, jsonOrXmlFile);
        this.method = method;
    }



    public NormalAsyNet(String url, HashMap keyValuePair,NetMethod method) {
        super(url, keyValuePair);
        this.method = method;
    }








    @Override
    protected String doInBackground(String... params) {

        try {
            Response response = accessNet();
            //这里直接用同步的方法获取响应
            return response.body().string();
        } catch (Exception e) {
            cancel(true);
        }
        return  null;
    }


}

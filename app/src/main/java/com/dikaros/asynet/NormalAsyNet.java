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
     * 请求方法POST和get
     */
    NetMethod method;



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
        //响应
        Response response = null;
        //call
        Call call = null;
        //判断请求的类型是post还是get
        switch (method){
            case POST:
                FormEncodingBuilder postParam = new FormEncodingBuilder();
                switch (type){
                    //无参数的不操作
                    case NO_PARAM:
                        break;
                    //单一参数的增加一个键值对
                    case JSON_OR_XML_FILE:
                        postParam.add(key,jsonOrXmlFile);
                        break;
                    //增加一堆键值对
                    case KEY_VALUE_PAIR:
                        for (String pk:keyValuePair.keySet()
                             ) {
                            postParam.add(pk,keyValuePair.get(pk).toString());
                        }
                        break;
                }
                Request.Builder postBuilder = new Request.Builder();
                //循环添加http报文头
                for (String headerKey:header.keySet()
                        ) {
                    //添加报文头
                    postBuilder.addHeader(headerKey,header.get(headerKey));
                }
                //构建请求
                Request postRequest = postBuilder.url(url).post(postParam.build()).build();
                call = client.newCall(postRequest);
                break;
            case GET:
                String param="?";
                switch (type){
                    case NO_PARAM:
                        param="";
                        break;
                    case JSON_OR_XML_FILE:
                        param+=key+"="+jsonOrXmlFile;
                        break;
                    case KEY_VALUE_PAIR:
                        for (String k:keyValuePair.keySet()
                             ) {
                            //补充param
                            param+=k+"="+keyValuePair.get(k).toString()+"&";
                        }
                        //去掉最后的&
                        if (param.endsWith("&")) {
                            param = param.substring(0, param.length() - 1);

                        }
                        break;
                }

                //创建请求的构造器并添加http报文头
                Request.Builder getBuilder = new Request.Builder();
                //循环添加http报文头
                for (String headerKey:header.keySet()
                     ) {
                    //添加报文头
                    getBuilder.addHeader(headerKey,header.get(headerKey));
                }
                //请求
                Request request = getBuilder.url(url+param).build();
                call = client.newCall(request);
        }


        try {
            //这里直接用同步的方法获取响应
            response = call.execute();
            return response.body().string();
        } catch (Exception e) {
            cancel(true);
        }
        return  null;
    }


}

package com.dikaros.asynet;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 异步http工具
 * 由于Google在Android 6.0中取消了HttpClient,所以现在使用OkHttp替换HttpClient
 *
 * @param <T>
 * @author dikaros
 * @version 0.3
 */
public abstract class AsyNet<T> extends AsyncTask<String, Integer, T> {


    public enum NetMethod{
        GET,POST
    }

    //参数类型
    public enum ParamType{
        JSON_OR_XML_FILE,KEY_VALUE_PAIR,NO_PARAM
    }

    //http报文头
    HashMap<String,String> header;


    //设置监听器回调

    public OnNetStateChangedListener<T> getOnNetStateChangedListener() {
        return onNetStateChangedListener;
    }

    public void setOnNetStateChangedListener(OnNetStateChangedListener<T> onNetStateChangedListener) {
        this.onNetStateChangedListener = onNetStateChangedListener;
    }

    OnNetStateChangedListener<T> onNetStateChangedListener;


    @Override
    protected void onCancelled() {
        if (onNetStateChangedListener!=null){
            onNetStateChangedListener.whenException();
        }
    }

    @Override
    protected void onPostExecute(T t) {
        if (onNetStateChangedListener!=null){
            onNetStateChangedListener.afterAccessNet(t);
        }
    }

    @Override
    protected void onPreExecute() {
        if (onNetStateChangedListener!=null){
            onNetStateChangedListener.beforeAccessNet();
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        if (onNetStateChangedListener!=null){
            if (values.length>1) {
                onNetStateChangedListener.onProgress(values[0]);
            }
        }
    }


    /**
     * 网络状态改变时候的监听器
     * @param <T>
     */
    public interface OnNetStateChangedListener<T> {
        /**
         * 访问网络之前
         */
        public void beforeAccessNet();

        /**
         * 访问网络成功获取数据之后
         *
         * @param result
         */
        public void afterAccessNet(T result);

        /**
         * 出现了错误的时候
         */
        public void whenException();

        /**
         * 执行进度
         * @param progress
         */
        public void onProgress(Integer progress);
    }

    //OkHttp;
    OkHttpClient client;


    //请求地址
    String url;

    //数据
    String jsonOrXmlFile;
    String key;

    ParamType type;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsonOrXmlFile() {
        return jsonOrXmlFile;
    }

    public void setJsonOrXmlFile(String jsonOrXmlFile) {
        this.jsonOrXmlFile = jsonOrXmlFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, Object> getKeyValuePair() {
        return keyValuePair;
    }

    public void setKeyValuePair(HashMap<String, Object> keyValuePair) {
        this.keyValuePair = keyValuePair;
    }

    HashMap<String, Object> keyValuePair;

    /**
     * 无参构造方法,初始化OkHttpClient
     */
    protected AsyNet(){
        client = new OkHttpClient();
        header = new HashMap<>();
        client.setConnectTimeout(connectTimeOut, TimeUnit.SECONDS);
    }

    long connectTimeOut = 5;

    public long getConnectTimeOut() {
        return connectTimeOut;
    }

    /**
     * 设置连接超时时间
     * @param connectTimeOut
     */
    public void setConnectTimeOut(long connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        client.setConnectTimeout(connectTimeOut, TimeUnit.SECONDS);
    }

    /**
     * 使用json或xml为参数的构造方法
     * @param url
     * @param key
     * @param jsonOrXmlFile
     */
    protected AsyNet(String url, String key, String jsonOrXmlFile) {
        this();
        this.url = url;
        this.key = key;
        this.jsonOrXmlFile = jsonOrXmlFile;
        this.keyValuePair = null;
        type = ParamType.JSON_OR_XML_FILE;
    }

    /**
     * 没有参数的方法
     * @param url
     */
    protected AsyNet(String url){
        this();
        this.url = url;
        type = ParamType.NO_PARAM;
    }

    /**
     * 使用键值对的构造方法
     * @param url
     * @param keyValuePair
     */
    protected AsyNet(String url, HashMap keyValuePair) {
        this();
        this.url  = url;
        this.keyValuePair = keyValuePair;
        this.key =null;
        type = ParamType.KEY_VALUE_PAIR;
    }

    /**
     * 执行
     */
    public void execute(){
        this.execute(new String[]{});
    }

    public void addHeader(String key,String value){
        header.put(key,value);

    }
}

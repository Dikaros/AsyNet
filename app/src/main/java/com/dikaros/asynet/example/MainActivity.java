package com.dikaros.asynet.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dikaros.asynet.AsyNet;
import com.dikaros.asynet.NormalAsyNet;
import com.dikaros.asynet.R;

import java.util.HashMap;

/**
 * 示例Activity
 */
public class MainActivity extends AppCompatActivity {

    AsyNet net;
    TextView tvMsg;
    public static final String API_KEY = "db642b2fac4fafe26849179ad8883592";
    public String path = "http://apis.baidu.com/apistore/idservice/id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        initNetMethodGet();

    }

    /**
     * 网络改变监听器,后面的泛型String表示返回的类型为String
     */
    class NetChangedListener implements AsyNet.OnNetStateChangedListener<String> {

        /**
         * 在请求网络之前调用,比如在这里弹出菊花显示加载
         */
        @Override
        public void beforeAccessNet() {

        }

        /**
         * 获取的结果,可以在这里进行UI的操作
         *
         * @param result
         */
        @Override
        public void afterAccessNet(String result) {
            tvMsg.append(result + "\n\n");
        }

        /**
         * 当网络连接出现问题的时候调用,这里不区分问题的类型
         */
        @Override
        public void whenException() {
            Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();

        }

        /**
         * 这个方法还没写
         *
         * @param progress
         */
        @Override
        public void onProgress(Integer progress) {

        }
    }

    /**
     * 初始化网络,使用get方法
     */
    private void initNetMethodGet() {
        //使用普通文本的AsyNet
        HashMap<String, String> keyValuePair = new HashMap<>();
        keyValuePair.put("id","41030319950221001X");
        net = new NormalAsyNet(path,keyValuePair,AsyNet.NetMethod.GET);
        //增加http报文头
        net.addHeader("apikey", API_KEY);
        //设置网络的监听器
        net.setOnNetStateChangedListener(new NetChangedListener());
        //执行(这个只能执行一次)
        net.execute();
    }

    /**
     * 使用post方法
     */
    private void initNetMethodPost() {
        String url = "http://172.30.18.211:8080";
        /*
        使用两个参数的构造方法说明不需要进行参数的传递,所以在使用post方式时尽量使用其他构造方法
         */
//        AsyNet an = new NormalAsyNet(url, AsyNet.NetMethod.POST);

        //这个是键值对,可以通过这个将信息传递到服务器中(支持get和Post方法)
        HashMap<String, String> keyValuePair = new HashMap<>();
        AsyNet am = new NormalAsyNet(url, keyValuePair, AsyNet.NetMethod.POST);
        //可以直接执行
        am.execute();

    }


}

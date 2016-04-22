package com.dikaros.asynet.example;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dikaros.asynet.AsyNet;
import com.dikaros.asynet.ImageAsyNet;
import com.dikaros.asynet.NormalAsyNet;
import com.dikaros.asynet.R;

import java.util.HashMap;

/**
 * 示例Activity
 */
public class MainActivity extends AppCompatActivity {

    AsyNet netImage,netText;
    TextView tvMsg,tvImageProgress;
    //api
    public static final String API_KEY = "db642b2fac4fafe26849179ad8883592";
    //获取身份证地址
    public static final String PATH = "http://apis.baidu.com/apistore/idservice/id";
    //图片地址

    Button btnGetImage,btnGetText;
    EditText etImageUrl;
    ImageView ivMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载控件
        initViews();






    }

    /**
     * 初始化Views
     */
    private void initViews() {
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        btnGetImage = (Button) findViewById(R.id.btn_get_image);
        btnGetText = (Button) findViewById(R.id.btn_get_text);
        etImageUrl = (EditText) findViewById(R.id.et_url);
        tvImageProgress  = (TextView) findViewById(R.id.tv_image_progress);
        ivMain = (ImageView) findViewById(R.id.iv_main);
        btnGetText.setOnClickListener(btnOnClickListener);
        btnGetImage.setOnClickListener(btnOnClickListener);
    }

    View.OnClickListener btnOnClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.btn_get_image){
                //初始化获取图片方法
                initImageMethod();
                netImage.execute();
            }else if (v.getId()==R.id.btn_get_text){
                //初始化获取数据方法
                initTextMethodGet();
                netText.execute();
            }
            v.setEnabled(false);
        }
    };


    /**
     * 初始化获取图片网络
     */
    private void initImageMethod() {
        netImage = new ImageAsyNet(etImageUrl.getText().toString(), AsyNet.NetMethod.GET);
        netImage.setOnNetStateChangedListener(new AsyNet.OnNetStateChangedListener<Bitmap>() {
            @Override
            public void beforeAccessNet() {
                tvImageProgress.setText("图片加载0%");
            }

            @Override
            public void afterAccessNet(Bitmap result) {
                tvImageProgress.setText("图片加载完成");
                if (result!=null) {
                    ivMain.setImageBitmap(result);
                }
                btnGetImage.setEnabled(true);

            }

            @Override
            public void whenException() {
                tvImageProgress.setText("图片加载失败");
                btnGetImage.setEnabled(true);
                Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(Integer progress) {
                tvImageProgress.setText("图片加载"+progress+"%");
            }
        });
    }


    /**
     * 初始化获取文字网络,使用get方法
     */
    private void initTextMethodGet() {
        //使用普通文本的AsyNet
        HashMap<String, String> keyValuePair = new HashMap<>();
        keyValuePair.put("id","41030319950221001X");
        netText = new NormalAsyNet(PATH,keyValuePair,AsyNet.NetMethod.GET);
        //增加http报文头
        netText.addHeader("apikey", API_KEY);
        //设置网络的监听器
        netText.setOnNetStateChangedListener(new AsyNet.OnNetStateChangedListener<String>() {
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
                btnGetText.setEnabled(true);
            }

            /**
             * 当网络连接出现问题的时候调用,这里不区分问题的类型
             */
            @Override
            public void whenException() {
                btnGetText.setEnabled(true);
                Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();

            }

            /**
             * 这个方法在获取文字中没有效果
             *
             * @param progress
             */
            @Override
            public void onProgress(Integer progress) {

            }
        });

    }



}

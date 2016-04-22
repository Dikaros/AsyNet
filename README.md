# AsyNet v0.3
####简介
> 一个异步的Android Http工具，基于OkHttp和Asynctask

####功能
* Http的基本Get和Post请求
* Android异步的请求
* 异步的网络图片加载，可以对图片进行压缩，压缩后的图片不大于100kb
* 异步的文件下载以及进度查询

####基本用法

	public static final String API_KEY = "db642b2fac4fafe26849179ad8883592";
    public static final String PATH = "http://apis.baidu.com/apistore/idservice/id";
---

	//创建键值对
	HashMap<String, String> keyValuePair = new HashMap<>();
	//在键值对里放入需要的值
	keyValuePair.put("id","41030319950221001X");
	//创建AsyNet
	AsyNet net = new NormalAsyNet(PATH,keyValuePair,AsyNet.NetMethod.GET);
        //增加http报文头
    net.addHeader("apikey", API_KEY);  
    //设置网络状态改变监听器    
    net.setOnNetStateChangedListener(new AsyNet.OnNetStateChangedListener<String>() {
            @Override
            public void beforeAccessNet() {
                //网络访问前
            }

            @Override
            public void afterAccessNet(String result) {
                //获取到结果时的回调
            }

            @Override
            public void whenException() {
                //网络访问失败时
            }

            @Override
            public void onProgress(Integer progress) {
                //访问进度
            }
        });
    //执行(这个只能执行一次)
    net.execute();

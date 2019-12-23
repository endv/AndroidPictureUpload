//package cn.endv.mytestapp.mycloud1;
//
//
//    import java.io.FileOutputStream;
//    import java.io.IOException;
//
//    import android.Manifest;
//    import android.app.Activity;
//    import android.content.Intent;
//    import android.content.pm.PackageManager;
//    import android.graphics.Bitmap;
//    import android.net.Uri;
//    import android.os.Build;
//    import android.os.Bundle;
//    import android.view.View;
//    import android.widget.Button;
//    import android.widget.ProgressBar;
//    import android.widget.TextView;
//    import android.widget.Toast;
//
//    import androidx.appcompat.app.AppCompatActivity;
//
//    import java.io.File;
//    import java.io.IOException;
//    import java.io.InputStream;
//    import java.util.concurrent.TimeUnit;
//
//    import cn.endv.mytestapp.R;
//    import okhttp3.Call;
//    import okhttp3.Callback;
//    import okhttp3.Interceptor;
//    import okhttp3.MediaType;
//    import okhttp3.MultipartBody;
//    import okhttp3.OkHttpClient;
//    import okhttp3.Request;
//    import okhttp3.RequestBody;
//    import okhttp3.Response;
//
//    public class MainActivity extends AppCompatActivity {
//
//        private OkHttpClient okHttpClient;
//        private TextView textView;
//        private ProgressBar progressBar;
//        private File tempFile;
//        private File targetFile;
//        private String path;
//        private Button button1;
//        private Button button2;
//        private Button button3;
//        private Button button4;
//        //private long time;
//
//        private static final int REQUEST_EXTERNAL_STORAGE = 1;
//        private static final int FILE_SELECT_CODE = 1;
//        private static String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            setTitle("上传文件并显示进度");
//            //verifyStoragePermissions(MainActivity.this);
//
//            textView = (TextView) findViewById(R.id.tv1);
//            progressBar = (ProgressBar) findViewById(R.id.pb);
//            button1 = (Button) findViewById(R.id.btn_choose);
//            button2 = (Button) findViewById(R.id.btn_upload);
//            button3 = (Button) findViewById(R.id.btn_download);
//            button4 = (Button) findViewById(R.id.btn_display);
//            progressBar.setMax(100);
//            path = "";
//
//            okHttpClient = new OkHttpClient.Builder()
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .writeTimeout(120, TimeUnit.SECONDS)
//                    .build();
//
//            button1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showFileChooser();
//                }
//            });
//
//            button2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startUploadClick();
//                }
//            });
//
//            button3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startDownloadClick();
//                }
//            });
//
//            button4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//
//                //打开文件选择器
//        private void showFileChooser() {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//            try {
//                startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
//            switch (requestCode) {
//                case FILE_SELECT_CODE:
//                    if (resultCode == RESULT_OK) {
//                        // Get the Uri of the selected file
//                        Uri uri = data.getData();
//                        path = FileUtils.getPath(this, uri);
//                    }
//                    break;
//            }
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//        public String getFileName(String pathandname){
//
//            int start=pathandname.lastIndexOf("/");
//            //int end=pathandname.lastIndexOf("");
//            if(start!=-1 ){
//                return pathandname.substring(start+1);
//            }else{
//                return null;
//            }
//
//        }
//
//        //点击按钮开始上传文件
//        public void startUploadClick() {
//            //tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test1.txt");
//            //showFileChooser();
//            String fileName = "";
//            if (path  == null){
//                Toast.makeText(MainActivity.this, "path 找不到该文件 path !", Toast.LENGTH_SHORT).show();
//            }
////            if(path==null)return;
//            tempFile = new File(path);
//            //tempFile = new File("/data/data/upload/man.jpg");
//
//            if (tempFile.getName() == null){
//                Toast.makeText(MainActivity.this, "找不到该文件!", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                fileName = getFileName(path);
//            }
//
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/jpg"), tempFile))
//                    .build();
//            //ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, progressListener);
//            Request request = new Request.Builder()
//                    .url("http://www.endv.cn/php_upload.php")
//                    .post(requestBody)
//                    .build();
//            okHttpClient.newCall(request).enqueue(callback_upload);
//
//
//        }
//
//        //点击按钮开始下载文件
//        public void startDownloadClick() {
//            //time = System.currentTimeMillis();
////            targetFile = new File("/data/data/cn.endv.mytestapp.mycloud1/cache/"  + "test1.jpg");
//            //targetFile = new File( "/data/data/downloads/" + System.currentTimeMillis() + ".jpg");
//            //targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/1.jpg");
//            Request request = new Request.Builder()
//                    .url("http://www.endv.cn/upload/timg.gif")
//                    .build();
//            okHttpClient.newCall(request).enqueue(callback_download);
//        }
//
////        //通过实现进度回调接口中的方法，来显示进度
////        private ProgressListener progressListener = new ProgressListener() {
////            @Override
////            public void update(long bytesRead, long contentLength, boolean done) {
////                int progress = (int) (100.0 * bytesRead / contentLength);
////                progressBar.setProgress(progress);
////            }
////        };
//
//        //上传请求后的回调方法
//        private Callback callback_upload = new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                setResult(e.getMessage(), false);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                setResult(response.body().string(), true);
//            }
//        };
//
//        private  Callback callback_download = new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                setResult(e.getMessage(),false);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response != null) {
//                    //下载完成，保存数据到文件
//                    //verifyStoragePermissions(MainActivity.this);
//                    InputStream is = response.body().byteStream();
//                    FileOutputStream fos = new FileOutputStream(targetFile);
//                    byte[] buf = new byte[1024];
//                    int hasRead = 0;
//                    while((hasRead = is.read(buf)) > 0) {
//                        fos.write(buf, 0, hasRead);
//                    }
//                    fos.close();
//                    is.close();
//                    setResult("下载成功", true);
//                }
//            }
//        };
//
//        //显示请求返回的结果
//        private void setResult(final String msg, final boolean success) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (success) {
//                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//                    }
//                    textView.setText(msg);
//                }
//            });
//        }
//
////    //自定义的RequestBody，能够显示进度
////    public class ProgressRequestBody extends RequestBody {
////        //实际的待包装请求体
////        private final RequestBody requestBody;
////        //进度回调接口
////        private final ProgressListener progressListener;
////        //包装完成的BufferedSink
////        private BufferedSink bufferedSink;
////
////        /**
////         * 构造函数，赋值
////         *
////         * @param requestBody      待包装的请求体
////         * @param progressListener 回调接口
////         */
////        public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
////            this.requestBody = requestBody;
////            this.progressListener = progressListener;
////        }
////
////        /**
////         * 重写调用实际的响应体的contentType
////         *
////         * @return MediaType
////         */
////        @Override
////        public MediaType contentType() {
////            return requestBody.contentType();
////        }
////
////        /**
////         * 重写调用实际的响应体的contentLength
////         *
////         * @return contentLength
////         * @throws IOException 异常
////         */
////        @Override
////        public long contentLength() throws IOException {
////            return requestBody.contentLength();
////        }
////
////        /**
////         * 重写进行写入
////         *
////         * @param sink BufferedSink
////         * @throws IOException 异常
////         */
////        @Override
////        public void writeTo(BufferedSink sink) throws IOException {
////            if (bufferedSink == null) {
////                //包装
////                bufferedSink = Okio.buffer(sink(sink));
////            }
////            //写入
////            requestBody.writeTo(bufferedSink);
////            //必须调用flush，否则最后一部分数据可能不会被写入
////            bufferedSink.flush();
////
////        }
////
////        /**
////         * 写入，回调进度接口
////         *
////         * @param sink Sink
////         * @return Sink
////         */
////        private Sink sink(Sink sink) {
////            return new ForwardingSink(sink) {
////                //当前写入字节数
////                long bytesWritten = 0L;
////                //总字节长度，避免多次调用contentLength()方法
////                long contentLength = 0L;
////
////                @Override
////                public void write(Buffer source, long byteCount) throws IOException {
////                    super.write(source, byteCount);
////                    if (contentLength == 0) {
////                        //获得contentLength的值，后续不再调用
////                        contentLength = contentLength();
////                    }
////                    //增加当前写入的字节数
////                    bytesWritten += byteCount;
////                    //回调
////                    progressListener.update(bytesWritten, contentLength, bytesWritten == contentLength);
////                }
////            };
////        }
////    }
////
////    //进度回调接口
////    interface ProgressListener {
////        void update(long bytesRead, long contentLength, boolean done);
////    }
////
////
////    public static void verifyStoragePermissions(Activity activity) {
////        // Check if we have write permission
////        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
////
////        if (permission != PackageManager.PERMISSION_GRANTED) {
////            // We don't have permission so prompt the user
////            ActivityCompat.requestPermissions(
////                    activity,
////                    PERMISSIONS_STORAGE,
////                    REQUEST_EXTERNAL_STORAGE
////            );
////        }
////    }
//
//}
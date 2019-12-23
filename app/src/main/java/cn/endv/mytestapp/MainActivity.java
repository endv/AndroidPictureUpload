package cn.endv.mytestapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.endv.mytestapp.cpr.ProgressHelper;
import cn.endv.mytestapp.cpr.ProgressUIListener;
import cn.endv.mytestapp.sample.ProgressMainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;
    public static int PICK_IMAGE_REQUEST = 21;
    String service_url = "";
    boolean isImageChanged = false;
    boolean isImageRemoved = false;
    Button updateItemCategory;
    //    String service_url2 =  "http://192.168.1.100:8081";
    String service_url2 = "";
    ImageView resultView;
    String url = "https://www.endv.cn/upload_file.php";
    private OkHttpClient okHttpClient;
    private NavigationView navigationView;
    //    private AppBarConfiguration mAppBarConfiguration;
    private TextView mTvName;   // NavigationView上的名字
    private ImageView mImgNav;  // NavigationView上的头像
    //    ImageService itemService;
    private DrawerLayout mDrawer;
    private Button upload, download;
    private TextView uploadInfo, downloadInfo;
    private ProgressBar uploadProgress, downloadProgeress;
    //上传请求后的回调方法
    private okhttp3.Callback callback_upload = new okhttp3.Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {
            Log.e("TAG", "request headers:" + response.request().headers());
            Log.e("TAG", "response headers:" + response.headers());

            if (response.code() == 201) {
                showToastMessage("没有更新项目 !");
            } else if (response.code() == 417) {
                showToastMessage("图片大于 2 MB.");
//                    item.setItemImageURL(null);
            } else if (response.code() == 404) {
                showToastMessage("服务器接口错误.");
//                    item.setItemImageURL(null);
            } else if (response.code() == 500) {
                showToastMessage("服务器目录无写入权限.");
//                    item.setItemImageURL(null);
            } else {
                showToastMessage("图片代码." + response.code());
                showToastMessage("图片更新成功 ");
//                    item.setItemImageURL(null);
            }
            File file = new File(service_url2);
            if (file != null) {
                file.delete();
                showToastMessage("文件已删除 !");
                service_url2 = "";
                return;
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            showToastMessage("图片更新失败 !");
//            setResult(e.getCause().hashCode(), new Intent().putExtra("noFaceDetection", false));
        }


    };
    // 下载请求回调
    private Callback callback_download = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("TAG", "=============onFailure===============");
            e.printStackTrace();
//            setResult(e.getMessage(),false);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {


            String filename = "v7.3.7z";
            String filepath = "/data/data/cn.endv.mytestapp/cache/";
            File outFile = new File(filepath + filename);
            if (outFile != null) {
                outFile.delete();
                System.out.println("delete");
            }
            outFile.getParentFile().mkdirs();
            outFile.createNewFile();//报错read-only file system的原因是你所在的分区只有读权限， 没有写权限
            FileOutputStream fos = new FileOutputStream(outFile);
//            File outFile = new File("v7.3.7z");
            Log.e("TAG", "=============onResponse===============");
            Log.e("TAG", "request headers:" + response.request().headers());
            Log.e("TAG", "response headers:" + response.headers());
            ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {

                //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                @Override
                public void onUIProgressStart(long totalBytes) {
                    super.onUIProgressStart(totalBytes);
                    Log.e("TAG", "onUIProgressStart:" + totalBytes);
                    Toast.makeText(getApplicationContext(), "开始下载：" + totalBytes, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                    Log.e("TAG", "=============start===============");
                    Log.e("TAG", "numBytes:" + numBytes);
                    Log.e("TAG", "totalBytes:" + totalBytes);
                    Log.e("TAG", "percent:" + percent);
                    Log.e("TAG", "speed:" + speed);
                    Log.e("TAG", "============= end ===============");
                    downloadProgeress.setProgress((int) (100 * percent));
                    downloadInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + " MB/秒");
                }

                //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                @Override
                public void onUIProgressFinish() {
                    super.onUIProgressFinish();
                    Log.e("TAG", "onUIProgressFinish:");
                    Toast.makeText(getApplicationContext(), "结束下载", Toast.LENGTH_SHORT).show();
                }
            });

//            BufferedSource source = responseBody.source();
//            InputStream is = source.inputStream();
            InputStream is = responseBody.source().inputStream();
            byte[] buf = new byte[1024];
            int hasRead = 0;
//            while((hasRead = is.read(buf)) > 0) {
//                fos.write(buf, 0, hasRead);
//            }

            while ((hasRead = is.read(buf)) != -1) {
                //将字节转换成char,连续输出print不换行
//                System.out.print((char)hasRead);
                fos.write(buf, 0, hasRead);
//                fos.flush();

            }

            fos.close();
            is.close();
//                setResult("下载成功", true);

//            outFile.delete();
//            outFile.getParentFile().mkdirs();
//            outFile.createNewFile();

//            BufferedSink sink = Okio.buffer(Okio.sink(outFile));
//            source.readAll(sink);
//            sink.flush();
//            source.close();
            Log.e("TAG", "下载成功:关闭流");


//            if(response != null) {
//                //下载完成，保存数据到文件
//                //verifyStoragePermissions(MainActivity.this);
//                InputStream is = response.body().byteStream();
//                FileOutputStream fos = new FileOutputStream(outFile);
//                byte[] buf = new byte[1024];
//                int hasRead = 0;
//                while((hasRead = is.read(buf)) > 0) {
//                    fos.write(buf, 0, hasRead);
//                }
//                fos.close();
//                is.close();
//                setResult("下载成功", true);
//            }
        }
    };

    //三、图片按比例大小压缩方法（根据Bitmap图片压缩）
    public static Bitmap compressByProportion(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImageByQuality(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //一、质量压缩法
    public static Bitmap compressImageByQuality(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 字节数组到文件的过程
    public static void ByteArrayToFile(byte[] data, String newFileNmae) {
        //选择源
        File file = new File(newFileNmae);
        //选择流
        FileOutputStream fos = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            fos = new FileOutputStream(file);
            int temp;
            byte[] bt = new byte[1024 * 10];
            while ((temp = bais.read(bt)) != -1) {
                fos.write(bt, 0, temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关流
            try {
                if (null != fos)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearCache(Context context) {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_one);
        initView();
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)

//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        updateItemCategory = findViewById(R.id.updateItemCategory);
        resultView = findViewById(R.id.uploadImage);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        ((App) getApplication()).setNavigationView(mDrawer);
    }

    private void initView() {
        uploadProgress = findViewById(R.id.upload_progress);
        downloadProgeress = findViewById(R.id.download_progress);
        uploadInfo = findViewById(R.id.upload_info);
        downloadInfo = findViewById(R.id.download_info);
        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    //  更新图片
    public void updateButtonClick(View v) {
        File file = new File(service_url2);
        if (file == null) {
            showToastMessage("文件不存在 !");
            return;
        }
        //获取图片的宽和高，并不把他加载到内存当中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(service_url2, options);
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
        //重点
        Bitmap bitma2 = compressByProportion(bitmap);
        ByteArrayToFile(bitma2.getNinePatchChunk(), service_url2);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", service_url2, RequestBody.create(file, MediaType.parse("image/jpg")))
                .addFormDataPart("userid", "18800608355")
                .build();
//
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        okHttpClient.newCall(request).enqueue(callback_upload);

//        file.getParentFile().mkdirs();
//        file.createNewFile();
    }

    public File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 20) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩 友盟缩略图要求不大于18kb
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        Log.d("=-=-=-=-=-", "compressImage: " + file);
        // recycleBitmap(bitmap);
        return file;
    }

    public Bitmap getNewBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    //  更新文件
    private void upload() {
        uploadInfo.setText("start upload");
        if (service_url2 == "") {
            showToastMessage("路径为空 !");
            return;
        }
        File apkFile = new File(service_url2);

        if (apkFile == null) {
            showToastMessage("文件不存在 !");
            return;
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart("file", service_url2, RequestBody.create(apkFile, null))
                .addFormDataPart("userid", "18800608355");
        MultipartBody build = bodyBuilder.build();

        RequestBody requestBody = ProgressHelper.withProgress(build, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Log.e("TAG", "onUIProgressStart:" + totalBytes);
                Toast.makeText(getApplicationContext(), "开始上传：" + totalBytes, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                Log.e("TAG", "=============start===============");
                Log.e("TAG", "numBytes:" + numBytes);
                Log.e("TAG", "totalBytes:" + totalBytes);
                Log.e("TAG", "percent:" + percent);
                Log.e("TAG", "speed:" + speed);
                Log.e("TAG", "============= end ===============");
                uploadProgress.setProgress((int) (100 * percent));
                uploadInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + "  MB/秒");

            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                Log.e("TAG", "onUIProgressFinish:");
                Toast.makeText(getApplicationContext(), "结束上传", Toast.LENGTH_SHORT).show();
            }
        });

        builder.post(requestBody);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback_upload);

    }

    // 下载文件
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void download() {
        uploadInfo.setText("start download");

        requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

        String url3 = "http://www.endv.cn/download/1234567.zip";
        Request request = new Request.Builder()
                .url(url3)
                .build();
        okHttpClient.newCall(request).enqueue(callback_download);

    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int id2 = item.getItemId();
        System.out.println("单击了菜单" + id2);
        mDrawer.closeDrawer(GravityCompat.START);
        mDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                int id = item.getItemId();


                if (id == R.id.nav_home) { // 消息
//                    Toast.makeText(this, "stop push", Toast.LENGTH_SHORT).show();
                    System.out.println("nav_home");

                } else if (id == R.id.nav_gallery) {
                    System.out.println("nav_gallery");
                } else if (id == R.id.nav_slideshow) {
                    System.out.println("nav_slideshow");
                } else if (id == R.id.nav_tools) {
                    System.out.println("nav_tools");
                } else if (id == R.id.nav_share) {
                    System.out.println("nav_share");
                    intent.setClass(MainActivity.this, ProgressMainActivity.class);
                } else if (id == R.id.nav_send) {
                    System.out.println("nav_send");

//                    intent.setClass(MainActivity.this, cn.endv.mytestapp.mycloud1.MainActivity.class);
                }
                startActivity(intent);

            }
        }, 300);
//        finish();
        return true;
    }

    void loadImage(String imagePath) {
        String iamgepath = service_url + "/api/v1/Staff/Image/" + imagePath;
        Picasso.get()
                .load(iamgepath)
                .into(resultView);
    }

    //    选择图片
    public void pickShopImage(View view) {
        System.out.println("选择图片");
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
            return;
        }
        clearCache(this);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == ImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {
            Uri filePath = result.getData();
            if (filePath != null) {
                try {
                    service_url2 = PathUtils.getPath(this, filePath);
                    System.out.println("打印路径" + service_url2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ImageCropUtility.startCropActivity(result.getData(), this);
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));
            isImageChanged = true;
            isImageRemoved = false;
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(result);
        }
    }

    void showToastMessage(String message) {
        System.out.println("消息：" + message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//               String message2="";

//                Toast.makeText(this, message2, Toast.LENGTH_SHORT).show();
                //然后将Bitmap设置到 ImageView 中
//            imageView.setImageBitmap(bitmap);
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        isDestroyed = false;
    }

}

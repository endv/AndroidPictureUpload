//package cn.endv.mytestapp;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by sumeet on 12/5/16.
// */
//public class ImageCalls {
//    String service_url =  "http://192.168.1.100:8081";;
//    private static ImageCalls instance;
//
//    Retrofit retrofit;
//    ImageService imageService;
//
//    private ImageCalls(Context context) {
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(service_url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        imageService = retrofit.create(ImageService.class);
//    }
//
//    public static ImageCalls getInstance() {
//        if (instance == null) {
//            instance = new ImageCalls(App.getContext());
//        }
//
//        return instance;
//    }
//
//    public void deleteImage(String imageID, Callback<ResponseBody> deleteImageCallback) {
//
//        if (imageID != null) {
//            if (imageID.length() > 2) {
//
//                imageID = imageID.substring(1);
//            }
//        }
//
//        Call<ResponseBody> response = imageService.deleteImage(imageID);
//
//        response.enqueue(deleteImageCallback);
//
//    }
//
//    // upload image stored in the cache directory
//
//    public void uploadPickedImage(
//            AppCompatActivity activityContext,
//            int REQUEST_CODE_READ_EXTERNAL_STORAGE,
//            Callback<Image> imageCallback
//    ) {
//
//        Log.d("applog", "onClickUploadImage");
//
//// code for checking the Read External Storage Permission and granting it.
//        if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
///// / TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            ActivityCompat.requestPermissions(activityContext,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
//
//            return;
//
//        }
//
//        File file = new File(activityContext.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
//
//// Marker
//
//        RequestBody requestBodyBinary = null;
//
//        InputStream in = null;
//
//        try {
//            in = new FileInputStream(file);
//
//            byte[] buf;
//            buf = new byte[in.available()];
//            while (in.read(buf) != -1) ;
//
//            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final Call<Image> imageCall = imageService.uploadImage(requestBodyBinary);
//
//        imageCall.enqueue(imageCallback);
//    }
//
//}

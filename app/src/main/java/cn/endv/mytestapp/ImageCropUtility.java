package cn.endv.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

/**
 * Created by sumeet on 5/5/16.
 */
public class ImageCropUtility {

    public static int PICK_IMAGE_REQUEST = 21;

    public static void showFileChooser(AppCompatActivity context) {
        clearCache(context);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);

    }

    // 取回后上传图片
    public static void startCropActivity(Uri sourceUri, AppCompatActivity activityContext) {

        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(activityContext.getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionQuality(100);

        options.setToolbarColor(activityContext.getResources().getColor(R.color.cyan900));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);

// 此函数从源URI获取文件并保存到目标URI位置。
        UCrop.of(sourceUri, destinationUri).withOptions(options)
                .withMaxResultSize(800, 600)
                .start(activityContext);


        //.withMaxResultSize(400,300)
        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }

    public static void clearCache(Context context) {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }

}

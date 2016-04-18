package com.iocm.business.utils.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.iocm.business.ui.activity.AlbumActivity;


public class AlbumIntentUtil {

    public static final int REQUEST_CODE_ALBUM = 0x0011;
    public static final int REQUEST_CODE_CAMERA = 0x0022;
    public static final int REQUEST_CODE_CROP = 0x0033;
    public static final int REQUEST_CODE_PREVIEW = 0x0044;

    public static final int RESULT_OK = 0x0055;
    public static final int RESULT_CANCEL = 0x0066;

    private static final String CROP_TYPE = "image/*";
    private static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();

    private static final int DEFAULT_ASPECT = 1;//设置裁剪宽高比
    private static final int DEFAULT_OUTPUT = 640;//设置输出图片分辨率

    private static String defaultTitle = "选择图片";


    /**
     * 跳转到单选相册页
     *
     * @param fragment
     * @param title
     * @param enableCamera
     * @param enablePreview
     * @param enableCrop
     */
    public static void startAlbumActivityForSingleResult(Fragment fragment, String title, boolean enableCamera, boolean enablePreview, boolean enableCrop) {
        Intent intent = new Intent(fragment.getActivity(), AlbumActivity.class);
        intent.putExtra("title", TextUtils.isEmpty(title) ? defaultTitle : title);
        intent.putExtra("enableCamera", enableCamera);
        intent.putExtra("enablePreview", enablePreview);
        intent.putExtra("enableCrop", enableCrop);
        intent.putExtra("maxLimit", 1);
        fragment.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * 跳转到单选相册页
     *
     * @param activity
     * @param title
     * @param enableCamera
     * @param enablePreview
     * @param enableCrop
     */
    public static void startAlbumActivityForSingleResult(Activity activity, String title, boolean enableCamera, boolean enablePreview, boolean enableCrop) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra("title", TextUtils.isEmpty(title) ? defaultTitle : title);
        intent.putExtra("enableCamera", enableCamera);
        intent.putExtra("enablePreview", enablePreview);
        intent.putExtra("enableCrop", enableCrop);
        intent.putExtra("maxLimit", 1);
        activity.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }


    /**
     * 跳转到多选相册页
     *
     * @param fragment
     * @param title
     * @param enableCamera
     * @param enablePreview
     * @param maxLimit
     */
    public static void startAlbumActivityForMultiResult(Fragment fragment, String title, boolean enableCamera, boolean enablePreview, int maxLimit) {
        Intent intent = new Intent(fragment.getActivity(), AlbumActivity.class);
        intent.putExtra("title", TextUtils.isEmpty(title) ? defaultTitle : title);
        intent.putExtra("enableCamera", enableCamera);
        intent.putExtra("enablePreview", enablePreview);
        intent.putExtra("enableCrop", false);
        intent.putExtra("maxLimit", maxLimit);
        fragment.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * 跳转到多选相册页
     *
     * @param activity
     * @param title
     * @param enableCamera
     * @param enablePreview
     * @param maxLimit
     */
    public static void startAlbumActivityForMultiResult(Activity activity, String title, boolean enableCamera, boolean enablePreview, int maxLimit) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra("title", TextUtils.isEmpty(title) ? defaultTitle : title);
        intent.putExtra("enableCamera", enableCamera);
        intent.putExtra("enablePreview", enablePreview);
        intent.putExtra("enableCrop", false);
        intent.putExtra("maxLimit", maxLimit);
        activity.startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }


    /**
     * 跳转到系统相机页面
     *
     * @param fragment
     * @param cameraOutPutUri
     */
    public static void startCameraActivityForResult(Fragment fragment, Uri cameraOutPutUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutPutUri);
        fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 跳转到系统相机页面
     *
     * @param activity
     * @param cameraOutPutUri
     */
    public static void startCameraActivityForResult(Activity activity, Uri cameraOutPutUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutPutUri);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 跳转到系统裁剪界面
     *
     * @param fragment
     * @param cropInPutUri
     * @param cropOutPutUri
     */
    public static void startCropActivityForResult(Fragment fragment, Uri cropInPutUri, Uri cropOutPutUri) {
        Intent intent = new Intent("com.android.camera.action.CROP", null)
                .setDataAndType(cropInPutUri, CROP_TYPE)
                .putExtra("crop", true)
                .putExtra("scale", true)
                .putExtra("aspectX", DEFAULT_ASPECT)
                .putExtra("aspectY", DEFAULT_ASPECT)
                .putExtra("outputX", DEFAULT_OUTPUT)
                .putExtra("outputY", DEFAULT_OUTPUT)
                .putExtra("return-data", false)
                .putExtra("outputFormat", OUTPUT_FORMAT)
                .putExtra("noFaceDetection", true)
                .putExtra("scaleUpIfNeeded", true)
                .putExtra(MediaStore.EXTRA_OUTPUT, cropOutPutUri);
        fragment.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 跳转到系统裁剪界面
     *
     * @param activity
     * @param cropInPutUri
     * @param cropOutPutUri
     */
    public static void startCropActivityForResult(Activity activity, Uri cropInPutUri, Uri cropOutPutUri) {
        Intent intent = new Intent("com.android.camera.action.CROP", null)
                .setDataAndType(cropInPutUri, CROP_TYPE)
                .putExtra("crop", true)
                .putExtra("scale", true)
                .putExtra("aspectX", DEFAULT_ASPECT)
                .putExtra("aspectY", DEFAULT_ASPECT)
                .putExtra("outputX", DEFAULT_OUTPUT)
                .putExtra("outputY", DEFAULT_OUTPUT)
                .putExtra("return-data", false)
                .putExtra("outputFormat", OUTPUT_FORMAT)
                .putExtra("noFaceDetection", true)
                .putExtra("scaleUpIfNeeded", true)
                .putExtra(MediaStore.EXTRA_OUTPUT, cropOutPutUri);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }


//    /**
//     * 跳转到预览界面
//     *
//     * @param fragment
//     * @param uri
//     */
//    public static void startPreviewActivityForResult(Fragment fragment, Uri uri) {
//        Intent intent = new Intent(fragment.getActivity(), AlbumPreviewActivity.class);
//        intent.putExtra("path", uri.toString());
//        fragment.startActivityForResult(intent, REQUEST_CODE_PREVIEW);
//    }
//
//
//    /**
//     * 跳转到预览界面
//     *
//     * @param activity
//     * @param uri
//     */
//    public static void startPreviewActivityForResult(Activity activity, Uri uri) {
//        Intent intent = new Intent(activity, AlbumPreviewActivity.class);
//        intent.putExtra("path", uri.toString());
//        activity.startActivityForResult(intent, REQUEST_CODE_PREVIEW);
//    }
}

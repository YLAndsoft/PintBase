package z.pint.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import f.base.utils.PerUtils;
import z.pint.activity.EditInfoActtivity;
import z.pint.constant.Constant;

import static android.R.attr.data;

/**
 * Created by DN on 2018/6/22.
 */

public class PhotoUtils {
    private PhotoUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取Uri
     * @param mContext
     * @param filePath
     * @return
     */
    public static Uri getFileUri(Context mContext,String filePath){
        Uri mUri = null;
        File mFile = new File(filePath);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //mUri = FileProvider.getUriForFile(context,"com.zxl.test_picture_camera",mFile);
            //通过FileProvider创建一个content类型的Uri
            String packageName = mContext.getPackageName();
            mUri = FileProvider.getUriForFile(mContext,packageName +".fileprovider", mFile);
        }else{
            mUri = Uri.fromFile(mFile);
        }
        return mUri;
    }

    /**
     * 打开相机
     * @param activity
     * @param requestCode
     * @param filePath
     */
    public static void startCamera(Activity activity,int requestCode,String filePath){
        if(hasSdcard()){
            Intent mOpenCameraIntent = new Intent();
            mOpenCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri desUri = getFileUri(activity,filePath);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                //已申请camera权限
                //mOpenCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            mOpenCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,desUri);
            activity.startActivityForResult(mOpenCameraIntent,requestCode);
        }
    }

    /**
     * 打开相册
     * @param activity
     * @param requestCode
     */
    public static void startGallery(Activity activity,int requestCode){
        if(hasSdcard()){
            Intent mOpenGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            mOpenGalleryIntent.setType("image/*");
            activity.startActivityForResult(mOpenGalleryIntent,requestCode);
        }
    }
    /**
     * 裁剪图片
     */
    public static void startCropImage(Activity activity, String originPath, String desPath, int aspectX, int aspectY, int outputX, int outputY, int requestCode){
        startCropImage(activity,getFileUri(activity,originPath),getFileUri(activity,desPath),aspectX,aspectY,outputX,outputY,requestCode);
    }
    /**
     * 裁剪图片
     * @param activity
     * @param originUri
     * @param desUri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public static void startCropImage(Activity activity, Uri originUri, Uri desUri, int aspectX, int aspectY, int outputX, int outputY, int requestCode){
        Intent mIntent = new Intent();
        mIntent.setAction("com.android.camera.action.CROP");
        mIntent.setDataAndType(originUri,"image/*");
        List resInfoList = queryActivityByIntent(activity,mIntent);
        if (resInfoList.size() == 0) {
            //showMsg(activity, "没有合适的应用程序");
            return;
        }
        Iterator resInfoIterator = resInfoList.iterator();
        while (resInfoIterator.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) resInfoIterator.next();
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, desUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mIntent.putExtra("crop","true");
        mIntent.putExtra("aspectX",aspectX);
        mIntent.putExtra("aspectY",aspectY);
        mIntent.putExtra("outputX",outputX);
        mIntent.putExtra("outputY",outputY);
        mIntent.putExtra("scale",true);

        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        mIntent.putExtra("return-data",false);
        mIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mIntent.putExtra("noFaceDetection",true);

        activity.startActivityForResult(mIntent,requestCode);
    }

    private static List<ResolveInfo> queryActivityByIntent(Activity activity, Intent intent){
        List resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resInfoList;
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static void showMsg(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static int getScreenWidth(Context context){
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point mPoint = new Point();
        mWindowManager.getDefaultDisplay().getSize(mPoint);
        return mPoint.x;
    }

    public static int getScreenHeight(Context context){
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point mPoint = new Point();
        mWindowManager.getDefaultDisplay().getSize(mPoint);
        return mPoint.y;
    }

    //以下是关键，原本uri返回的是file:///...，
    //android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



}

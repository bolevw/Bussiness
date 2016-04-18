package com.iocm.business.utils.album;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.iocm.business.model.AlbumModel;

import java.util.ArrayList;


public class AlbumMediaScanner {

    public static final String ALBUM_ID_ALL_PHOTOS = "all_photos";


    /**
     * 通知媒体库扫描刷新当前文件路径
     *
     * @param context
     * @param scanUri
     * @param albumCallback
     */
    public static void scanFile(Context context, Uri scanUri, final AlbumCallback<String> albumCallback) {
        MediaScannerConnection.scanFile(context, new String[]{scanUri.getPath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(final String path, Uri uri) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                albumCallback.onCompleted(path);
                            }
                        });
                    }
                });
    }


    /**
     * 获取相册列表
     *
     * @param context
     * @return
     */
    public static ArrayList<AlbumModel> getAllAlbums(Context context) {
        ArrayList<AlbumModel> albums = new ArrayList<>();
        Cursor cursor = null;
        try {
            int totalCount = getTotalPhotoCount(context);
            if (totalCount > 0) {
                AlbumModel allPhotos = new AlbumModel("全部图片", ALBUM_ID_ALL_PHOTOS, getCoverByAlbumId(context, ALBUM_ID_ALL_PHOTOS), totalCount);
                albums.add(allPhotos);
                final String[] columns = {MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_ID,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME};
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                        String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));
                        if (!isAlbumAdded(albums, id)) {
                            int count = getPhotoCountByAlbumId(context, id);
                            Uri coverUri = getCoverByAlbumId(context, id);
                            albums.add(new AlbumModel(name, id, coverUri, count));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return albums;
    }

    /**
     * 获取sd卡上所有图片
     *
     * @param context
     * @return
     */
    public static ArrayList<Uri> getAllPhotos(Context context) {
        ArrayList<Uri> uris = new ArrayList<>();
        Cursor cursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    orderBy);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Uri uri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                    uris.add(uri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return uris;
    }

    /**
     * 根据id获取当前相册下所有图片
     *
     * @param context
     * @param albumId
     * @return
     */
    public static ArrayList<Uri> getPhotosByAlbumId(Context context, String albumId) {
        ArrayList<Uri> uris = new ArrayList<>();
        Cursor cursor = null;
        try {
            final String[] columns = {MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATA};
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, "bucket_id = ?", new String[]{albumId}, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    Uri uri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    uris.add(uri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return uris;
    }

    /**
     * 根据id获取当前相册下的图片数量
     *
     * @param context
     * @param albumId
     * @return
     */
    public static Integer getPhotoCountByAlbumId(Context context, String albumId) {
        Cursor cursor = null;
        int count = 0;
        try {
            final String[] columns = {MediaStore.Images.ImageColumns.DATA};
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, "bucket_id = ?", new String[]{albumId},
                    MediaStore.Images.ImageColumns.DATE_MODIFIED);
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 获取图片总数
     *
     * @param context
     * @return
     */
    public static Integer getTotalPhotoCount(Context context) {
        Cursor cursor = null;
        int count = 0;
        try {
            final String[] columns = {MediaStore.Images.ImageColumns.DATA};
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 根据id获取当前相册的第一张图片的URI
     *
     * @param context
     * @param albumId
     * @return
     */
    public static Uri getCoverByAlbumId(Context context, String albumId) {
        Uri coverUri = null;
        Cursor cursor = null;
        try {
            if (albumId.equals(ALBUM_ID_ALL_PHOTOS)) {
                final String[] columns = {MediaStore.Images.Media.DATA,
                        MediaStore.Images.ImageColumns.ORIENTATION};
                final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, null, null, orderBy);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        coverUri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    }
                }
            } else {
                final String[] columns = {MediaStore.Images.ImageColumns.DATA};
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns,
                        "bucket_id = ?",
                        new String[]{albumId},
                        MediaStore.Images.ImageColumns.DATE_MODIFIED);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        coverUri = Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return coverUri;
    }

    private static boolean isAlbumAdded(ArrayList<AlbumModel> albums, String albumId) {
        boolean isAlbumAdded = false;
        for (AlbumModel albumModel : albums) {
            if (albumModel.getAlbumId().equals(albumId)) {
                isAlbumAdded = true;
                break;
            }
        }
        return isAlbumAdded;
    }
}

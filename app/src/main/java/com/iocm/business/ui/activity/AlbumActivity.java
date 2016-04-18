package com.iocm.business.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.model.AlbumModel;
import com.iocm.business.utils.album.AlbumCallback;
import com.iocm.business.utils.album.AlbumGridAdapter;
import com.iocm.business.utils.album.AlbumIntentUtil;
import com.iocm.business.utils.album.AlbumListAdapter;
import com.iocm.business.utils.album.AlbumMediaScanner;
import com.iocm.business.utils.album.AlbumSpacesItemDecoration;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlbumActivity extends BaseActivity {


    private static final int REQUEST_CODE_PERMISSION_CAMERA = 0x0099;

    private static final int SPAN_COUNT = 3;
    private static final String DIRECTORY_TYPE = Environment.DIRECTORY_PICTURES;
    private static final String CAMERA_FILE_NAME = "camera_pic.jpg";
    private static final String CROP_FILE_NAME = "crop_pic.jpg";

    private RecyclerView albumGridRecyclerView;
    private RecyclerView albumListRecyclerView;
    private Button albumListButton;


    private boolean enableCamera = false;
    private boolean enableCrop = false;
    private boolean enablePreview = false;
    private int maxChoice = 1;

    private String currentAlbumId = AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS;

    private Uri cameraOutPutUri = null;
    private Uri cropOutPutUri = null;
    private Uri currentPreviewUri = null;
    private ArrayList<AlbumModel> albums = new ArrayList<>();
    private ArrayList<Uri> photos = new ArrayList<>();
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private AlbumGridAdapter albumGridAdapter;
    private AlbumListAdapter albumListAdapter;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
        }
        enableCamera = intent.getBooleanExtra("enableCamera", false);
        enablePreview = intent.getBooleanExtra("enablePreview", false);
        enableCrop = intent.getBooleanExtra("enableCrop", false);
        maxChoice = intent.getIntExtra("maxLimit", 1);
        albumGridRecyclerView = (RecyclerView) findViewById(R.id.albumGridRecyclerView);
        albumListRecyclerView = (RecyclerView) findViewById(R.id.albumListRecyclerView);
        albumListButton = (Button) findViewById(R.id.albumListButton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        albumListRecyclerView.setLayoutManager(linearLayoutManager);
        albumGridRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        albumGridRecyclerView.addItemDecoration(new AlbumSpacesItemDecoration(2));
    }

    @Override
    protected void setListener() {
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (albumListRecyclerView.getVisibility() == View.VISIBLE) {
                    albumListRecyclerView.setVisibility(View.GONE);
                } else {
                    setResult(AlbumIntentUtil.RESULT_CANCEL);
                    AlbumActivity.this.finish();
                }
            }
        });
        albumListButton.setOnClickListener(listener);
    }

    @Override
    protected void bind() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        int itemWith = mScreenWidth / 3;
        albumGridAdapter = new AlbumGridAdapter(AlbumActivity.this, photos, gridClickListener, itemWith, enableCamera);
        albumListAdapter = new AlbumListAdapter(AlbumActivity.this, albums, listClickListener);
        albumGridRecyclerView.setAdapter(albumGridAdapter);
        albumListRecyclerView.setAdapter(albumListAdapter);
        loadAlbumPhotos(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS);
        loadAlbumList();
    }

    @Override
    protected void unBind() {

    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.albumListButton) {
                if (albums.size() > 0) {
                    if (albumListRecyclerView.getVisibility() == View.VISIBLE) {
                        albumListRecyclerView.setVisibility(View.GONE);
                    } else {
                        albumListRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };


    private void loadAlbumPhotos(final String albumId) {
        final ProgressDialog progressDialog = new ProgressDialog(AlbumActivity.this);
        progressDialog.setMessage("加载中。。");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                photos.clear();
                if (albumId.equals(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS)) {
                    photos.addAll(AlbumMediaScanner.getAllPhotos(AlbumActivity.this));
                } else {
                    photos.addAll(AlbumMediaScanner.getPhotosByAlbumId(AlbumActivity.this, albumId));
                }
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(Void v) {
                        albumGridAdapter.notifyDataSetChanged();
                    }
                });
    }


    private void loadAlbumList() {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                albums.clear();
                albums.addAll(AlbumMediaScanner.getAllAlbums(AlbumActivity.this));
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void v) {
                        albumListAdapter.notifyDataSetChanged();
                    }
                });
    }


/*    @TargetApi(Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (PermissionUtil.checkPermission(AlbumActivity.this, Manifest.permission.CAMERA)) {
            cameraOutPutUri = Uri
                    .fromFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_TYPE))
                    .buildUpon()
                    .appendPath(System.currentTimeMillis() + "_" + CAMERA_FILE_NAME)
                    .build();
            AlbumIntentUtil.startCameraActivityForResult(AlbumActivity.this, cameraOutPutUri);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    PermissionUtil.showPermissionsDescDialog(this, "请在设置-应用-小泰乐活-权限中开启相机权限，以正常使用拍照，二维码扫描等功能");
                } else {
                    // Permission GRANTED
                    cameraOutPutUri = Uri
                            .fromFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_TYPE))
                            .buildUpon()
                            .appendPath(System.currentTimeMillis() + "_" + CAMERA_FILE_NAME)
                            .build();
                    AlbumIntentUtil.startCameraActivityForResult(AlbumActivity.this, cameraOutPutUri);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AlbumIntentUtil.REQUEST_CODE_CAMERA) {
                scanFile(cameraOutPutUri);
                if (enableCrop) {
                    cropOutPutUri = Uri
                            .fromFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_TYPE))
                            .buildUpon()
                            .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                            .build();
                    AlbumIntentUtil.startCropActivityForResult(AlbumActivity.this, cameraOutPutUri, cropOutPutUri);
                }
            }
            if (requestCode == AlbumIntentUtil.REQUEST_CODE_PREVIEW) {
                if (currentPreviewUri != null) {
                    if (enableCrop) {
                        cropOutPutUri = Uri
                                .fromFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_TYPE))
                                .buildUpon()
                                .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                                .build();
                        AlbumIntentUtil.startCropActivityForResult(AlbumActivity.this, currentPreviewUri, cropOutPutUri);
                    }
                }
            }
            if (requestCode == AlbumIntentUtil.REQUEST_CODE_CROP) {
                if (maxChoice == 1) {
                    selectedPhotos.clear();
                    selectedPhotos.add(cropOutPutUri.getPath());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                    setResult(AlbumIntentUtil.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }


    private void scanFile(Uri scanUri) {
        AlbumMediaScanner.scanFile(AlbumActivity.this, scanUri, new AlbumCallback<String>() {
            @Override
            public void onCompleted(String object) {
                loadAlbumPhotos(AlbumMediaScanner.ALBUM_ID_ALL_PHOTOS);
            }

            @Override
            public void onFailed(String str) {

            }
        });
    }


    private AlbumGridAdapter.OnItemClickListener gridClickListener = new AlbumGridAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Uri uri, int position) {
            if (enableCamera && position == 0) {
            } else {
                if (maxChoice == 1) {
                    if (enableCrop) {
                        cropOutPutUri = Uri
                                .fromFile(Environment.getExternalStoragePublicDirectory(DIRECTORY_TYPE))
                                .buildUpon()
                                .appendPath(System.currentTimeMillis() + "_" + CROP_FILE_NAME)
                                .build();
                        AlbumIntentUtil.startCropActivityForResult(AlbumActivity.this, uri, cropOutPutUri);
                    } else {
                        selectedPhotos.clear();
                        selectedPhotos.add(uri.getPath());
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("selectedPhotos", selectedPhotos);
                        setResult(AlbumIntentUtil.RESULT_OK, intent);
                        finish();
                    }

                }
            }
        }
    };


    private AlbumListAdapter.OnItemClickListener listClickListener = new AlbumListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String albumId, String albumName, int position) {
            if (!albumId.equals(currentAlbumId)) {
                currentAlbumId = albumId;
                albumListButton.setText(albumName);
                loadAlbumPhotos(albumId);
            }
            albumListRecyclerView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onBackPressed() {
        if (albumListRecyclerView.getVisibility() == View.VISIBLE) {
            albumListRecyclerView.setVisibility(View.GONE);
        } else {
            setResult(AlbumIntentUtil.RESULT_CANCEL);
            super.onBackPressed();
        }
    }
}

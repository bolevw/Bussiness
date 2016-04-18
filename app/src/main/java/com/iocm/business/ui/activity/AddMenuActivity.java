package com.iocm.business.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;
import com.iocm.business.base.BaseApplication;
import com.iocm.business.model.MenuModel;
import com.iocm.business.utils.BitmapCompressUtil;
import com.iocm.business.utils.PicassoUtils;
import com.iocm.business.utils.ToastUtils;
import com.iocm.business.utils.album.AlbumIntentUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddMenuActivity extends BaseActivity {

    private AppCompatEditText nameEditText;
    private AppCompatEditText detailEditText;
    private AppCompatEditText funcEditText;

    private ImageButton addPicButton;

    private AppCompatImageView menuPicImageView;

    private AppCompatButton submitButton;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_menu);


        nameEditText = (AppCompatEditText) findViewById(R.id.menuNameEditText);
        detailEditText = (AppCompatEditText) findViewById(R.id.menuDetailEditTextView);
        funcEditText = (AppCompatEditText) findViewById(R.id.menuFunctionEditText);

        addPicButton = (ImageButton) findViewById(R.id.menuAddPicButton);

        menuPicImageView = (AppCompatImageView) findViewById(R.id.menuPicImageView);

        submitButton = (AppCompatButton) findViewById(R.id.menuSubmitButton);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != intent && null != bundle) {
            MenuModel model = (MenuModel) bundle.getSerializable("menu");
            updateView(model);
        }

    }

    private void updateView(MenuModel m) {
        nameEditText.setText(m.getName());
        detailEditText.setText(m.getDetail());
        funcEditText.setText(m.getFunction());
        PicassoUtils.normalShowImage(this, m.getImageSrc(), menuPicImageView);
    }

    @Override
    protected void setListener() {
        addPicButton.setOnClickListener(listener);
        submitButton.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == addPicButton.getId()) {
                AlbumIntentUtil.startAlbumActivityForSingleResult(AddMenuActivity.this, "选择照片", true, false, true);

            }

            if (id == submitButton.getId()) {
                uploadMenu();

            }

        }
    };

    private void uploadMenu() {
        String name = nameEditText.getText().toString();
        String detail = detailEditText.getText().toString();
        String function = funcEditText.getText().toString();
        String pic = picPath;
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showNormalToast("请输入菜名");
        } else if (TextUtils.isEmpty(detail)) {
            ToastUtils.showNormalToast("请输入配料");
        } else if (TextUtils.isEmpty(function)) {
            ToastUtils.showNormalToast("请输入菜的营养价值");
        } else if (!uploadPic) {
            ToastUtils.showNormalToast("请选择成品照片");
        } else {
            upload(name, detail, function, pic);
        }
    }

    private void upload(String name, String detail, String function, String pic) {
        AVObject avObject = new AVObject("MenuTable");
        avObject.put("name", name);
        avObject.put("detail", detail);
        avObject.put("function", function);
        avObject.put("picSrc", picFile);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.showNormalToast("上传菜品成功");
                    finish();
                } else {
                    ToastUtils.showNormalToast("上传失败，请检查您的网络!");
                }
            }
        });
    }

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }

    private String picPath = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumIntentUtil.REQUEST_CODE_ALBUM) {
            if (resultCode == AlbumIntentUtil.RESULT_OK) {
                ArrayList<String> photos = data.getStringArrayListExtra("selectedPhotos");
                if (photos.size() > 0) {
                    final String path = photos.get(0);
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.showNormalToast("选择图片");
                    } else {

                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                String targetPath = BaseApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/"
                                        + System.currentTimeMillis() + "_compress_avatar.jpg";
                                String resultPath = BitmapCompressUtil.compressToPath(path, targetPath);
                                subscriber.onNext(resultPath);
                                subscriber.onCompleted();
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String compressPath) {
                                        uploadAvatar(compressPath);
                                        menuPicImageView.setImageBitmap(BitmapFactory.decodeFile(compressPath));

                                    }
                                });


                    }
                } else {
                    ToastUtils.showNormalToast("选择图片");

                }
            }
        }
    }


    private AVFile picFile;
    private boolean uploadPic = false;

    private void uploadAvatar(String path) {
        try {
            picFile = AVFile.withAbsoluteLocalPath("pic.jpg", path);
            picFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        uploadPic = true;
                    } else {
                        Log.e("error", e.toString());
                        uploadPic = false;
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}

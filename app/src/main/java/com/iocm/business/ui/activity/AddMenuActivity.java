package com.iocm.business.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
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
    private AppCompatEditText moneyEditText, typeEditTextView, tasteEditTextView, methodEditTextView;

    private CheckBox hasGood;

    private TextView addPicTextView;

    private ImageButton addPicButton;

    private AppCompatImageView menuPicImageView;

    private AppCompatButton submitButton;
    private AppCompatButton delButton;
    private boolean edit = false;
    private MenuModel model;

    private AVObject post = new AVObject("MenuTable");

    private boolean has = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_menu);


        hasGood = (CheckBox) findViewById(R.id.hasGood);
        nameEditText = (AppCompatEditText) findViewById(R.id.menuNameEditText);
        detailEditText = (AppCompatEditText) findViewById(R.id.menuDetailEditTextView);
        funcEditText = (AppCompatEditText) findViewById(R.id.menuFunctionEditText);

        addPicButton = (ImageButton) findViewById(R.id.menuAddPicButton);

        menuPicImageView = (AppCompatImageView) findViewById(R.id.menuPicImageView);

        submitButton = (AppCompatButton) findViewById(R.id.menuSubmitButton);
        typeEditTextView = (AppCompatEditText) findViewById(R.id.typeEditTextView);
        tasteEditTextView = (AppCompatEditText) findViewById(R.id.tasteEditTextView);
        methodEditTextView = (AppCompatEditText) findViewById(R.id.methodEditTextView);
        addPicTextView = (TextView) findViewById(R.id.addPicTextView);

        moneyEditText = (AppCompatEditText) findViewById(R.id.menuMoneyEditText);
        delButton = (AppCompatButton) findViewById(R.id.delButton);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != intent && null != bundle) {
            edit = intent.getBooleanExtra("edit", false);
            model = (MenuModel) bundle.getSerializable("menu");
            updateView(model);
        }

    }

    boolean getS = false;

    private void updateView(MenuModel m) {
        nameEditText.setText(m.getName());
        detailEditText.setText(m.getDetail());
        funcEditText.setText(m.getFunction());
        moneyEditText.setText(m.getMoney());
        typeEditTextView.setText(m.getType());
        tasteEditTextView.setText(m.getTaste());
        methodEditTextView.setText(m.getMethod());

        hasGood.setChecked(m.isHas());

        PicassoUtils.normalShowImage(this, m.getImageSrc(), menuPicImageView);

        delButton.setVisibility(View.VISIBLE);


        AVQuery<AVObject> query = new AVQuery<>("MenuTable");


        query.getInBackground(model.getId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    getS = true;
                    post = avObject;
                    picFile = post.getAVFile("picSrc");
                } else {
                    ToastUtils.showNormalToast("参数错误");
                    finish();
                }
            }
        });

    }

    @Override
    protected void setListener() {
        addPicButton.setOnClickListener(listener);
        submitButton.setOnClickListener(listener);
        delButton.setOnClickListener(listener);
        hasGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                has = isChecked;
            }
        });

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == addPicButton.getId()) {
                AlbumIntentUtil.startAlbumActivityForSingleResult(AddMenuActivity.this, "选择照片", true, false, true);

            }

            if (id == submitButton.getId()) {
                if (edit) {
                    updateMenu();
                } else {
                    uploadMenu();
                }
            }

            if (id == delButton.getId()) {
                delMenu();
            }

        }
    };

    private void delMenu() {
        post.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtils.showNormalToast("删除成功！");
                    finish();
                } else {
                    Log.e("error", e.getCode() + " " + e.toString());
                }
            }
        });
    }

    private void updateMenu() {


        String name = nameEditText.getText().toString();
        String detail = detailEditText.getText().toString();
        String function = funcEditText.getText().toString();
        String money = moneyEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showNormalToast("请输入菜名");
        } else if (TextUtils.isEmpty(moneyEditText.getText().toString())) {
            ToastUtils.showNormalToast("请输入菜价");
        } else if (TextUtils.isEmpty(detail)) {
            ToastUtils.showNormalToast("请输入配料");
        } else if (TextUtils.isEmpty(function)) {
            ToastUtils.showNormalToast("请输入菜的营养价值");
        } else {
            post.put("name", name);
            post.put("detail", detail);
            post.put("function", function);
            post.put("picSrc", picFile);
            post.put("money", money);
            post.saveInBackground(new SaveCallback() {
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


    }

    private void uploadMenu() {
        String name = nameEditText.getText().toString();
        String detail = detailEditText.getText().toString();
        String function = funcEditText.getText().toString();
        String type = typeEditTextView.getText().toString();
        String taste = tasteEditTextView.getText().toString();
        String method = methodEditTextView.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showNormalToast("请输入菜名");
        } else if (TextUtils.isEmpty(moneyEditText.getText().toString())) {
            ToastUtils.showNormalToast("请输入菜价");
        } else if (TextUtils.isEmpty(detail)) {
            ToastUtils.showNormalToast("请输入配料");
        } else if (TextUtils.isEmpty(function)) {
            ToastUtils.showNormalToast("请输入菜的营养价值");
        } else if (!uploadPic) {
            ToastUtils.showNormalToast("照片上传中。。请稍等。");
        } else {
            upload(name, detail, function, moneyEditText.getText().toString(), type, taste, method);
        }
    }

    private void upload(String name, String detail, String function, String money, String type, String taste, String method) {
        AVObject avObject = new AVObject("MenuTable");
        avObject.put("name", name);
        avObject.put("detail", detail);
        avObject.put("function", function);
        if (getS) {
            avObject.put("picSrc", picFile);
        }
        avObject.put("money", money);
        avObject.put("type", type);
        avObject.put("taste", taste);
        avObject.put("method", method);
        avObject.put("has", has);
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
        addPicTextView.setText("正在上传照片。。请稍等。");
        addPicTextView.setTextColor(Color.parseColor("#ff0000"));
        try {
            picFile = AVFile.withAbsoluteLocalPath("pic.jpg", path);
            picFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        getS = true;
                        addPicTextView.setText("上传照片成功!");
                        uploadPic = true;

                    } else {
                        Log.e("error", e.toString());
                        addPicTextView.setText("上传照片失败,请重新选择照片!");
                        uploadPic = false;
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}

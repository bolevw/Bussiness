package com.iocm.business.ui.activity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageButton;

import com.iocm.business.R;
import com.iocm.business.base.BaseActivity;

public class AddMenuActivity extends BaseActivity {

    private AppCompatEditText nameEditText;
    private AppCompatEditText detailEditText;
    private AppCompatEditText funcEditText;

    private ImageButton addPicButton;

    private AppCompatButton submitButton;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_menu);

        nameEditText = (AppCompatEditText) findViewById(R.id.menuNameEditText);
        detailEditText = (AppCompatEditText) findViewById(R.id.menuDetailEditTextView);
        funcEditText = (AppCompatEditText) findViewById(R.id.menuFunctionEditText);

        addPicButton = (ImageButton) findViewById(R.id.menuAddPicButton);

        submitButton = (AppCompatButton) findViewById(R.id.menuSubmitButton);

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

            }

            if (id == submitButton.getId()) {

            }

        }
    };

    @Override
    protected void bind() {

    }

    @Override
    protected void unBind() {

    }
}

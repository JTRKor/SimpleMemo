package com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.data.Memo;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.db.DBManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteActivity extends AppCompatActivity {
    DBManager dbManager;

    @BindView(R.id.write_tv_complete) TextView write_tv_complete;
    @BindView(R.id.write_iv_cancle) ImageView write_iv_cancle;
    @BindView(R.id.write_et_content) EditText write_et_content;
    Intent intent;
    Long id = 0l;
    Memo item = new Memo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        ButterKnife.bind(this);

        dbManager = new DBManager(WriteActivity.this, "memo.db", null, 1);
        intent = getIntent();
        id = intent.getLongExtra("id", 0);
        if (id != 0) {
            item = dbManager.getMemo(id);
            write_et_content.setText(item.getContent());
        }

    }

    @OnClick(R.id.write_iv_cancle)
    void write_iv_cancle() {
        finish();
    }

    @OnClick(R.id.write_tv_complete)
    void write_tv_complete() {
        String content = write_et_content.getText().toString();
        if (id == 0) {
            if (content == null) {
                content = "";
            }
            dbManager.insertMemo(content);
        }
        else {
            dbManager.updateMemo(id, content);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}

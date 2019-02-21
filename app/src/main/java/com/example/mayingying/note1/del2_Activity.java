package com.example.mayingying.note1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class del2_Activity extends Activity {

    private TextView tv_date;
    private TextView t_content;
    private Button btn_cancel;
    private Button btn_delete;
    private Button btn_recover;
    private DatabaseHelper DB;
    private SQLiteDatabase dbread;
    private int bg;
    private int del;
    public static String last_time;
    public static String last_content;
    public static int id;
    public static int last_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_del2_);

        if (last_bg == 0) {
            findViewById(R.id.activity_del2).setBackgroundResource(0);

        } else {
            if (last_bg == 1) {
                findViewById(R.id.activity_del2).setBackgroundResource(0);
                bg = 1;

            }
            if (last_bg == 2) {
                findViewById(R.id.activity_del2).setBackgroundResource(R.drawable.bg2);
                bg = 2;

            }
            if (last_bg == 3) {
                findViewById(R.id.activity_del2).setBackgroundResource(R.drawable.bg5);
                bg = 3;

            }
            if (last_bg == 4) {
                findViewById(R.id.activity_del2).setBackgroundResource(R.drawable.bg1);
                bg = 3;

            }
        }
        DB = new DatabaseHelper(this);
        dbread = DB.getReadableDatabase();
        tv_date = (TextView) findViewById(R.id.tv_date2);

        tv_date.setText(last_time);

        t_content = (TextView) findViewById(R.id.text_view2);
        // 设置软键盘自动弹出
        //getWindow().setSoftInputMode(
        //WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //DB = new DatabaseHelper(this);
        //dbread = DB.getReadableDatabase();
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        Log.d("LAST_CONTENT", last_content);
        t_content.setText(last_content);

        btn_cancel = (Button) findViewById(R.id.btn_cancel2);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        btn_recover=(Button)findViewById(R.id.recover);
        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(del2_Activity.this);
                builder.setTitle("恢复该便签");
                builder.setMessage("确认恢复吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        del=0;
                        String updatesql = "update note set del='"
                                + del + "' where _id=" + id;
                        dbread.execSQL(updatesql);
                        //String sql_del = "update note set content='' where _id="
                        //+ id;
                        //dbread.execSQL(sql_del);
                        Intent data = new Intent();
                        setResult(2, data);
                        finish();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
            }
        });

        btn_delete = (Button) findViewById(R.id.delete2);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(del2_Activity.this);
                builder.setTitle("删除该便签");
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        del = 1;
                        //String updatesql = "update note set del='"
                        //+ del + "' where _id=" + id;
                        //dbread.execSQL(updatesql);
                        String sql_del = "update note set content='' where _id="
                                + id;
                        dbread.execSQL(sql_del);
                        Intent data = new Intent();
                        setResult(2, data);
                        data.setClass(del2_Activity.this, del_Activity.class);
                        startActivityForResult(data, 2);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
            }
        });



    }

}



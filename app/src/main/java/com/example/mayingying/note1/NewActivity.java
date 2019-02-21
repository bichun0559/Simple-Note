package com.example.mayingying.note1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewActivity extends Activity {
    private TextView tv_date;
    private String last_date;
    private EditText et_content;
    private Button btn_share;
    private Button set_bg;
    private Button btn_ok;
    private Button btn_cancel;
    private Button btn_delete;
    private DatabaseHelper DB;
    private SQLiteDatabase dbread;
    private int bg;
    private int del;
    public static int ENTER_STATE = 0;
    public static String last_content;
    public static String last_time;
    public static int id;
    public static int last_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new);
        bg=0;del=0;
        if(last_bg==0||NewActivity.ENTER_STATE==0){
            findViewById(R.id.activity_new).setBackgroundResource(0);
        }
        else {
            if(last_bg==1) {
                findViewById(R.id.activity_new).setBackgroundResource(0);
                bg = 1;
            }
            if(last_bg==2) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg2);
                bg = 2;
            }
            if(last_bg==3) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg5);
                bg = 3;
            }
            if(last_bg==4) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg1);
                bg = 3;
            }
        }

        //Toast.makeText(getApplicationContext(), last_bg,Toast.LENGTH_SHORT).show();
        DB = new DatabaseHelper(this);
        dbread = DB.getReadableDatabase();
        /*Cursor c = dbread.query("note"="+",null,"date'"+last_date+"'",null,null,null,null);
        while(c.moveToNext())
        {
            int name = c.getColumnIndex("id");
            last_date = c.getString(name);
        }*/
        //last_date=getData();
        tv_date=(TextView)findViewById(R.id.tv_date);


        if(ENTER_STATE==0){
            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String dateString = sdf.format(date);
        tv_date.setText(dateString);}
        else{
            tv_date.setText(last_time);
        }

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        set_bg=(Button)findViewById(R.id.bg);
        set_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(NewActivity.this,R.layout.bglayout);
            }
        });

        et_content = (EditText)findViewById(R.id.et_content);
        // 设置软键盘自动弹出
        //getWindow().setSoftInputMode(
        //WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //DB = new DatabaseHelper(this);
        //dbread = DB.getReadableDatabase();
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        Log.d("LAST_CONTENT", last_content);
        et_content.setText(last_content);
        // 确认按钮的点击事件

        btn_share=(Button)findViewById(R.id.share_button) ;
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et_content.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "便签内容："+content);
                startActivity(intent);
            }
        });

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 获取日志内容
                String content = et_content.getText().toString();
                Log.d("LOG1", content);
                // 获取写日志时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String dateNum = sdf.format(date);
                String sql;
                String sql_count = "SELECT COUNT(*) FROM note";
                SQLiteStatement statement = dbread.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();
                Log.d("COUNT", count + "");
                Log.d("ENTER_STATE", ENTER_STATE + "");
                // 添加一个新的日志
                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        sql = "insert into " + DatabaseHelper.TABLE_NAME_NOTES
                                + " values(" + count + "," + "'" + content
                                + "'" + "," + "'" + dateNum +
                                "'" + "," + "'" +bg+"'"+","+"'"+del+
                                "')";
                        Log.d("LOG", sql);
                        dbread.execSQL(sql);
                    }
                }
                // 查看并修改一个已有的日志
                else {
                    if(!content.equals(last_content)){
                        Date date2 = new Date();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                        String dateNum2 = sdf.format(date);
                    Log.d("执行命令", "执行了该函数");
                    String updatesql = "update note set content='"
                            + content + "' where _id=" + id;
                    String updatesq2 = "update note set date='"
                            + dateNum2 + "' where _id=" + id;

                        dbread.execSQL(updatesql);
                        dbread.execSQL(updatesq2);
                    }
                    if(bg!=last_bg){
                        String updatesq2 = "update note set bg='"
                                + bg + "' where _id=" + id;
                        dbread.execSQL(updatesq2);
                    }
                    // et_content.setText(last_content);
                }

                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });
        btn_delete=(Button)findViewById(R.id.delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewActivity.this);
                builder.setTitle("删除该便签");
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ENTER_STATE==1){
                        del=1;
                        String updatesql = "update note set del='"
                                + del + "' where _id=" + id;
                        dbread.execSQL(updatesql);
                            //String sql_del = "update note set content='' where _id="
                                    //+ id;
                            //dbread.execSQL(sql_del);
                        Intent data = new Intent();
                        setResult(2, data);
                            finish();}
                        else{finish();}
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // if (requestCode == 3 && resultCode == 4) {
        // last_content=data.getStringExtra("data");
        // Log.d("LAST_STRAING", last_content+"gvg");
        // }
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bg1:

                    break;

                case R.id.bg2:

                    break;
                // 取消
                case R.id.bg3:

                    break;
            }
        }
    };
    /*public void dialog_show()  //背景会变暗
    {
        Dialog mCameraDialog = new Dialog(NewActivity.this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(NewActivity.this).inflate(
                R.layout.bglayout, null);
        root.findViewById(R.id.bg1).setOnClickListener(btnlistener);
        root.findViewById(R.id.bg2).setOnClickListener(btnlistener);
        root.findViewById(R.id.bg3).setOnClickListener(btnlistener);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }*/

    private void showPopupWindow(final Context context, @LayoutRes int resource) {
        //设置要显示的view
        View view = View.inflate(context,resource,null);
        //此处可按需求为各控件设置属性
        view.findViewById(R.id.bg1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.activity_new).setBackgroundResource(0);
                bg=1;
            }
        });
        view.findViewById(R.id.bg2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg2);
                bg=2;
            }
        });
        view.findViewById(R.id.bg3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg5);
                bg=3;
            }
        });
        view.findViewById(R.id.bg4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.activity_new).setBackgroundResource(R.drawable.bg1);
                bg=4;
            }
        });
        PopupWindow popupWindow = new PopupWindow(view);
        //设置弹出窗口大小
        popupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //必须设置以下两项，否则弹出窗口无法取消
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置动画效果
        popupWindow.setAnimationStyle(R.style.dialogstyle);
        //设置显示位置,findViewById获取的是包含当前整个页面的view
        popupWindow.showAtLocation(findViewById(R.id.activity_new), Gravity.BOTTOM,0,0);
    }

}


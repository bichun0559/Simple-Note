package com.example.mayingying.note1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class del_Activity extends Activity implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView listview;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;
    private TextView tv_content;
    private DatabaseHelper DB;
    private SQLiteDatabase dbread;
    private Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_);

        tv_content = (TextView) findViewById(R.id.tv_content);
        listview = (ListView) findViewById(R.id.list_view2);
        dataList = new ArrayList<Map<String, Object>>();
        mContext = this;
        DB = new DatabaseHelper(this);
        dbread = DB.getReadableDatabase();
        // 清空数据库中表的内容
        //dbread.execSQL("delete from note");
        RefreshNotesList();
        listview.setOnItemClickListener(this);
        btn_return=(Button) findViewById(R.id.return2);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });

    }


    public void RefreshNotesList() {
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(this, getData(), R.layout.item_layout,
                new String[]{"tv_content", "tv_date", "bg"}, new int[]{
                R.id.tv_content, R.id.tv_date, R.id.image});
        listview.setAdapter(simp_adapter);
    }

    private List<Map<String, Object>> getData() {

        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String del = cursor.getString(cursor.getColumnIndex("del"));
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String bg = cursor.getString(cursor.getColumnIndex("bg"));
            Map<String, Object> map = new HashMap<String, Object>();
            if (Integer.parseInt(del) == 1) {
                map.put("tv_content", name);
                map.put("tv_date", date);
                if (Integer.parseInt(bg) == 0 || Integer.parseInt(bg) == 1) {
                    map.put("bg", R.drawable.blank);
                } else {
                    if (Integer.parseInt(bg) == 2)
                        map.put("bg", R.drawable.b22);
                    if (Integer.parseInt(bg) == 3)
                        map.put("bg", R.drawable.b5);
                    if (Integer.parseInt(bg) == 4)
                        map.put("bg", R.drawable.b1);
                }
                dataList.add(map);
            }
        }
        cursor.close();
        return dataList;
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Log.d("arg2", arg2 + "");
        // TextView
        // content=(TextView)listview.getChildAt(arg2).findViewById(R.id.tv_content);
        // String content1=content.toString();
        String content = listview.getItemAtPosition(arg2) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            String b1 = c.getString(c.getColumnIndex("bg"));
            String time = c.getString(c.getColumnIndex("date"));
            del2_Activity.last_time=time;
            del2_Activity.last_bg = Integer.parseInt(b1);
            Log.d("TEXT", No);
            // Intent intent = new Intent(mContext, noteEdit.class);
            // intent.putExtra("data", text);
            // setResult(4, intent);
            // // intent.putExtra("data",text);
            // startActivityForResult(intent, 3);
            Intent myIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            del2_Activity.id = Integer.parseInt(No);
            //NewActivity.last_bg = Integer.parseInt(background);
            myIntent.putExtras(bundle);
            myIntent.setClass(del_Activity.this, del2_Activity.class);
            startActivityForResult(myIntent, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshNotesList();
        }
    }
}

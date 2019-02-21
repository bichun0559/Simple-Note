package com.example.mayingying.note1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener,AbsListView.OnScrollListener,AdapterView.OnItemLongClickListener{

    private Context mContext;
    private ListView listview;
    private SimpleAdapter simp_adapter;
    private SimpleAdapter simp_adapter2;
    private List<Map<String, Object>> dataList;
    private List<Map<String, Object>> dataList1;
    private Button addNote;
    private FloatingActionButton addnote;
    private TextView tv_content;
    private DatabaseHelper DB;
    private SQLiteDatabase dbread;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchView = (SearchView)findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(true);
        //mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });

        tv_content=(TextView)findViewById(R.id.tv_content);
        listview=(ListView)findViewById(R.id.list_view);
        dataList=new ArrayList<Map<String, Object>>();
        addnote=(FloatingActionButton)findViewById(R.id.edit_note);
        mContext=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewActivity.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        /*addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*NewActivity.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });*/
        DB = new DatabaseHelper(this);
        dbread = DB.getReadableDatabase();
        // 清空数据库中表的内容
        //dbread.execSQL("delete from note");
        RefreshNotesList();

        listview.setTextFilterEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //ListAdapter adapter = listview.getAdapter();
                //Filter filter = ((Filterable)adapter).getFilter();
                if(!TextUtils.isEmpty(s)){
                    search_action(s);

                }
                else{
                    RefreshNotesList();
                }
                return true;
            }
        });

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //dbread.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent myIntent = new Intent();
            myIntent.setClass(this, del_Activity.class);
            startActivityForResult(myIntent, 1);

        } else if (id == R.id.nav_gallery) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于");
            builder.setMessage("版本1.0");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create();
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void RefreshNotesList(){
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(this, getData(), R.layout.item_layout,
                new String[] { "tv_content", "tv_date" ,"bg"}, new int[] {
                R.id.tv_content, R.id.tv_date,R.id.image});
        listview.setAdapter(simp_adapter);
    }//,"bg" ,R.id.image
    private List<Map<String, Object>> getData() {

        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String del = cursor.getString(cursor.getColumnIndex("del"));
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String bg = cursor.getString(cursor.getColumnIndex("bg"));
            Map<String, Object> map = new HashMap<String, Object>();
            if(Integer.parseInt(del)==0) {
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

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i) {
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }
    }

    public void onItemClick(AdapterView<?>arg0, View arg1, int arg2, long arg3) {
        NewActivity.ENTER_STATE = 1;
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
            String b1 =c.getString(c.getColumnIndex("bg"));
            String time = c.getString(c.getColumnIndex("date"));
            NewActivity.last_time=time;
            NewActivity.last_bg=Integer.parseInt(b1);
            Log.d("TEXT", No);
            // Intent intent = new Intent(mContext, noteEdit.class);
            // intent.putExtra("data", text);
            // setResult(4, intent);
            // // intent.putExtra("data",text);
            // startActivityForResult(intent, 3);
            Intent myIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            NewActivity.id = Integer.parseInt(No);
            //NewActivity.last_bg = Integer.parseInt(background);
            myIntent.putExtras(bundle);
            myIntent.setClass(MainActivity.this, NewActivity.class);
            startActivityForResult(myIntent, 1);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        final int n=arg2;
        final int del = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该便签");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview.getItemAtPosition(n) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set del='"+del+"' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    RefreshNotesList();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshNotesList();
        }
    }
    public void search_action(String s){
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(this, getData2(s), R.layout.item_layout,
                new String[] { "tv_content", "tv_date" ,"bg"}, new int[] {
                R.id.tv_content, R.id.tv_date,R.id.image});
        listview.setAdapter(simp_adapter);
    }
    private List<Map<String, Object>> getData2(String s) {

        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String del = cursor.getString(cursor.getColumnIndex("del"));
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String bg = cursor.getString(cursor.getColumnIndex("bg"));
            Map<String, Object> map = new HashMap<String, Object>();
            if(Integer.parseInt(del)==0) {
                if(name.contains(s)){
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
                dataList.add(map);}
            }
        }
        cursor.close();
        return dataList;
    }
}

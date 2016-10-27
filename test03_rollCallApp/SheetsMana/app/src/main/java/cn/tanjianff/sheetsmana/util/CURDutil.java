package cn.tanjianff.sheetsmana.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tanjianff.sheetsmana.entity.stuSheet;

/**
 * Created by tanjian on 16/10/27.
 * 操作SQLite数据库类
 */

public class CURDutil {
    /*
     *   使用此方法需要从Activity中传入应用上下文context*
     */
    private Context context;
    private dbOpenHelper dbHelper;
    private SQLiteDatabase db;

    public CURDutil(Context context) {
        this.context = context;
        this.dbHelper = new dbOpenHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public void add(List<stuSheet> sheets) {
        db.beginTransaction();//开始事务
        try {
            for (stuSheet item : sheets) {
                db.execSQL("INSERT INTO stuSheet VALUES (null,null,?,?,?)"
                        , new Object[]{item.getStd_id(), item.getStd_name(), item.getStd_className()});
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);//通知显示异常
        } finally {
            db.endTransaction();//结束事务
            Toast.makeText(context,"添加成功",Toast.LENGTH_LONG);
        }
    }

    public void updateIcon(stuSheet student) {
        ContentValues cv = new ContentValues();
        cv.put("icon", student.getIcon());
        db.update("student", cv, "icon=?", new String[]{student.getIcon()});
    }

    public List<stuSheet> query() {
        ArrayList<stuSheet> stuSheets = new ArrayList<stuSheet>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            stuSheet student = new stuSheet();
            student.setIcon(c.getString(c.getColumnIndex("icon")));
            student.setStd_id(c.getString(c.getColumnIndex("std_id")));
            student.setStd_name(c.getString(c.getColumnIndex("std_name")));
            student.setStd_className(c.getString(c.getColumnIndex("std_className")));
            stuSheets.add(student);
        }
        c.close();
        return stuSheets;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM stuSheet", null);
        return c;
    }

    //释放数据库资源
    public void closeDB() {
        db.close();
    }
}
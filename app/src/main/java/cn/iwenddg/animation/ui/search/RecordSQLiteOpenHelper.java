package cn.iwenddg.animation.ui.search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建、管理数据库 & 版本控制
 * 继承自SQLiteOpenHelper数据库类的子类
 *
 * @author iwen大大怪
 * @create 2021/11/08 20:31
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 保存历史信息的数据库
     */
    private static String name = "searchdata.db";

    /**
     * 数据库版本
     */
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 打开数据库 & 建立了一个叫 records 的表，里面只有一列name来存储历史记录：
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


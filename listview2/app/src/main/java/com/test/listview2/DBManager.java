package com.test.listview2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {
    //자식 클래스의 생성자에 매개변수가 너무 많아요!!
    //적당히 지워버립시다.!
    //public DBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    public DBManager(@Nullable Context context) {
        //super(context, name, factory, version);
        super(context, "Direct.db", null, 1);
        //1. contenxt => 화면 정보(Activity)
        //2. name => database 파일이름
        //3. factory => JDBC의 ResultSet 과 같은 일을 하는 객체
        //4. version => database의 버젼!(내가 만든 데이터 베이스 파일의 버젼) 최초 만들때는 1을 주면 된다.
    }
    //부모클래스에 매개변수가 있는 생성자가 있다면
    //자식클래스에서 반드시 부모클래스의 생성자를 호출해 주어야 합니다.
    //super => 자식클래스의 생성자에서만 호출가능!

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터베이스 파일이 최초로 생성될 때 호출~~
        //1. 테이블 생성하는 소스코드 입력 하기
        String sql = "create table direct(title text, address text)";
        //text(문자열)타입의 컬럼 2개 생성~

        //2. sql 문장 전송하기(매개변수로 주어진 SQLiteDataBase 객체 사용!)
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //데이터베이스 파일의 버젼이 바뀌었을 때~~
        //버젼이 바뀌면 Table 지우고 다시 만드는 소스코드 입력!!
        String sql = "drop table direct";
        db.execSQL(sql); //테이블 지우고

        onCreate(db); //다시 생성하고~~
    }

    public void insert(String name, String link) {
        //리펙토링 => 에러를 해결해나가는 방식으로 개발

        //1. 쓸 수 있는 데이터베이스 객체 꺼내기~
        SQLiteDatabase db = this.getWritableDatabase();

        //2. 데이터를 insert => key, value 형태로 insert!
        ContentValues cv = new ContentValues();
        cv.put("title", name); //컬럼명과 데이터를 매개변수로 넣는다.
        cv.put("address", link);

        db.insert("direct", null, cv);
    }

    public void insert(ContentListVO vo) {
        //리펙토링 => 에러를 해결해나가는 방식으로 개발

        //1. 쓸 수 있는 데이터베이스 객체 꺼내기~
        SQLiteDatabase db = this.getWritableDatabase();

        //2. 데이터를 insert => key, value 형태로 insert!
        ContentValues cv = new ContentValues();
        cv.put("title", vo.getName()); //컬럼명과 데이터를 매개변수로 넣는다.
        cv.put("address", vo.getLink());

        db.insert("direct", null, cv);
    }

    public Cursor getAllData() {
        //1. Database 객체 꺼내기~
        SQLiteDatabase db = this.getReadableDatabase();

        //2. select쿼리
        String sql = "select * from direct";

        //selectionArgs :null >> prepare 사용할 때 사용하는 배열 ( ?,  ?)형태로 사용하던 부분
        Cursor cs = db.rawQuery(sql, null);

        return cs;
    }

    public void delete(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("direct", "title='"+text+"'", null);
    }
}

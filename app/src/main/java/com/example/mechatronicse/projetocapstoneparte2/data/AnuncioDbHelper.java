package com.example.mechatronicse.projetocapstoneparte2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AnuncioDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "capstone_database";
    private static final int VERSION = 1;

    AnuncioDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + AnuncioContract.AnuncioEntry.TABLE_NAME + " (" +
                AnuncioContract.AnuncioEntry.COLUMN_ANUNCIO_ID + " TEXT NOT NULL PRIMARY KEY, " +
                AnuncioContract.AnuncioEntry.COLUMN_TELEFONE + " TEXT NOT NULL, " +
                AnuncioContract.AnuncioEntry.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AnuncioContract.AnuncioEntry.TABLE_NAME);
        onCreate(db);
    }
}

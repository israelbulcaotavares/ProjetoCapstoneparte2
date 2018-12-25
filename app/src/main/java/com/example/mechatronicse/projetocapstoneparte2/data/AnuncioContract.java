package com.example.mechatronicse.projetocapstoneparte2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class AnuncioContract {
    public static final String AUTHORITY = "com.example.mechatronicse.projetocapstoneparte2";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_ANUNCIOS = "capstone";

    public static final class AnuncioEntry implements BaseColumns {
        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_ANUNCIOS).build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_ANUNCIO_ID = "anuncioId";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}

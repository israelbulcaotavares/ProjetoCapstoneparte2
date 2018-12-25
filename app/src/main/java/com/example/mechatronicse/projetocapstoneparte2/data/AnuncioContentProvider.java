package com.example.mechatronicse.projetocapstoneparte2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

@SuppressWarnings("ALL")
public class AnuncioContentProvider extends ContentProvider {
    private static final String LOG_TAG = AnuncioContentProvider.class.getSimpleName();
    private static final int ANUNCIOS = 100;
    private static final int ANUNCIOS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AnuncioContract.AUTHORITY, AnuncioContract.PATH_ANUNCIOS, ANUNCIOS);
        uriMatcher.addURI(AnuncioContract.AUTHORITY, AnuncioContract.PATH_ANUNCIOS + "/*", ANUNCIOS_WITH_ID);
        return  uriMatcher;
    }

    private AnuncioDbHelper mAnuncioDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mAnuncioDbHelper = new AnuncioDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mAnuncioDbHelper.getReadableDatabase();
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)){
            case ANUNCIOS:
                returnCursor = db.query(AnuncioContract.AnuncioEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("getType method is not supported now");
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mAnuncioDbHelper.getWritableDatabase();
        Uri returnUri;
        Log.v(LOG_TAG, "uri : " + uri);
        switch (sUriMatcher.match(uri)){
            case ANUNCIOS:
                long id = db.insertWithOnConflict(AnuncioContract.AnuncioEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(AnuncioContract.AnuncioEntry.CONTENT_URI, id);
                    Log.v(LOG_TAG, "returnUri : " + returnUri);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mAnuncioDbHelper.getWritableDatabase();
        int favoriteDeleted;

        switch (sUriMatcher.match(uri)){
            case ANUNCIOS_WITH_ID:
                String id = uri.getLastPathSegment();
                favoriteDeleted = db.delete(AnuncioContract.AnuncioEntry.TABLE_NAME,
                        AnuncioContract.AnuncioEntry.COLUMN_ANUNCIO_ID + "=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);

        }

        if(favoriteDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favoriteDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Updated method is not supported now");
    }
}

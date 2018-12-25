package com.example.mechatronicse.projetocapstoneparte2.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.data.AnuncioContract;

public class AnuncioRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewWidgetRemoteViewsFactory(this.getApplicationContext());
    }

    class GridViewWidgetRemoteViewsFactory implements RemoteViewsFactory {

        private Cursor mCursor;
        private final Context mContext;

        GridViewWidgetRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();

            mCursor = getContentResolver().query(
                    AnuncioContract.AnuncioEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    AnuncioContract.AnuncioEntry.COLUMN_TIMESTAMP);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            if (mCursor == null) {
                return 0;
            } else {
                return mCursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor == null || mCursor.getCount() == 0) {
                return null;
            }

            mCursor.moveToPosition(i);

            int telefoneIndex = mCursor.getColumnIndex(AnuncioContract.AnuncioEntry.COLUMN_TELEFONE);
            String telefone = mCursor.getString(telefoneIndex);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_adapter_item);
            remoteViews.setTextViewText(R.id.widget_receita_name, telefone);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

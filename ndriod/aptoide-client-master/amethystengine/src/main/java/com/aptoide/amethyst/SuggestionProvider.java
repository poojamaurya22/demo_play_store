package com.aptoide.amethyst;


import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;

import com.aptoide.amethyst.BuildConfig;
import com.aptoide.amethyst.utils.Logger;
import com.aptoide.amethyst.websockets.WebSocketSingleton;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: brutus
 * Date: 26-09-2013
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    private static final String URI = "";


    public String getSearchProvider() {
        return BuildConfig.APPLICATION_ID + ".SuggestionProvider";
    }

    @Override
    public boolean onCreate() {

        setupSuggestions(getSearchProvider(), DATABASE_MODE_QUERIES);
        return super.onCreate();
    }

    @Override
    public Cursor query(final Uri uri, String[] projection, String selection, final String[] selectionArgs, String sortOrder) {
        Logger.d("TAG", "query: " + selectionArgs[0]);

        Cursor c = super.query(uri, projection, selection, selectionArgs, sortOrder);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1 && c != null) {
            BlockingQueue<MatrixCursor> arrayBlockingQueue = new ArrayBlockingQueue<MatrixCursor>(1);
            WebSocketSingleton.getInstance().setNotificationUri(uri).setContext(getContext()).setBlockingQueue(arrayBlockingQueue);

            MatrixCursor matrix_cursor = null;
            WebSocketSingleton.getInstance().send(selectionArgs[0]);
            try {
                matrix_cursor = arrayBlockingQueue.poll(5, TimeUnit.SECONDS);

                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    matrix_cursor.newRow().add(c.getString(c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_ICON_1))).add(c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))).add(c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_QUERY))).add("1");
                }

            } catch (InterruptedException e) {
                Logger.printException(e);
            } finally {
                c.close();
            }
            return matrix_cursor;
        } else {
            return null;
        }

    }

}
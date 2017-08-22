package com.mobilinker.mobiplace.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilinker.mobiplace.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pooja on 01-03-2017.
 */

public class DownloadManager extends ListActivity {

    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    public static String search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_manager);

        packageManager = getPackageManager();

        new LoadApplications().execute();

        ImageView imageView = (ImageView) findViewById(R.id.search_manager);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById(R.id.search_edit);
                search = editText.getText().toString();
                // ApplicationAdapterManager.s(search);

               /* Intent intent = new Intent(DownloadManager.this, ApplicationAdapterManager.class);
                intent.putExtra("search", search);
                startActivity(intent); */


                List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
                //ArrayList<ApplicationInfo> res = new ArrayList<ApplicationInfo>();

                for (int i = 0; i < apps.size(); i++) {

                    PackageInfo p = apps.get(i);
                    //ApplicationInfo info = new ApplicationInfo();
                    final String n = p.applicationInfo.loadLabel(getPackageManager()).toString();

                    if (search == n) {
                        Intent in = new Intent(DownloadManager.this, AppSearch.class);

                        in.putExtra("name", n);
                        in.putExtra("pkgname", p.packageName);
                        in.putExtra("desc", p.applicationInfo.loadDescription(getPackageManager()));
                        // For icon
                        Bitmap bitmap = null; // your bitmap
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                        in.putExtra("icon", bs.toByteArray());

                        startActivity(in);
                        break;
                    }
                }
              }
            }
        );

    }

    private ApplicationAdapterManager listadaptor = null;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_list, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch (item.getItemId()) {
            case R.id.menu_about: {
                displayAboutDialog();

                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);

                break;
            }
        }

        return result;
    }

    private void displayAboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.about_title));
        builder.setMessage(getString(R.string.about_desc));


        builder.setPositiveButton("Know More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javatechig.com"));
                startActivity(browserIntent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = applist.get(position);
        try {
            Intent intent = packageManager
                    .getLaunchIntentForPackage(app.packageName);

            if (null != intent) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DownloadManager.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(DownloadManager.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) throws ExecutionException, InterruptedException {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String packagename = pInfo.packageName;
        String current = pInfo.versionName;
        int current_version = Integer.parseInt(current.replaceAll("[\\D]", ""));

        VersionChecker versionChecker = new VersionChecker(packagename);
        String latest = versionChecker.execute().get();
        int latest_version = Integer.parseInt(latest.replaceAll("[\\D]", ""));

        for (ApplicationInfo info : list) {
            try {
                if ((current_version < latest_version))
                    if ((null != packageManager.getLaunchIntentForPackage(info.packageName))) {
                        applist.add(info);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listadaptor = new ApplicationAdapterManager(DownloadManager.this, R.layout.list_row_manager, applist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DownloadManager.this, null, "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}


package com.mobilinker.mobiplace.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilinker.mobiplace.R;

import java.util.List;

/**
 * Created by Pooja on 05-03-2017.
 */

public class ApplicationAdapterManager extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appsList = null;
    private static Context context;
    private PackageManager packageManager;
    private LayoutInflater layoutInflater;
    ImageView imageView;


    public ApplicationAdapterManager(Context context, int textViewResourceId,
                                     List<ApplicationInfo> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        this.layoutInflater = LayoutInflater.from(context);
        // View view = LayoutInflater.from(context).inflate(R.layout.list_row_manager, null);
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_row_manager, null);
        }

        final ApplicationInfo data = appsList.get(position);
        if (null != data) {
            TextView appName = (TextView) view.findViewById(R.id.app_name_manager);
            TextView packageName = (TextView) view.findViewById(R.id.app_package_manager);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon_manager);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            iconview.setImageDrawable(data.loadIcon(packageManager));
        }

         final String pname = data.packageName;

        Button b = (Button) view.findViewById(R.id.update);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id = " + pname));
                context.startActivity(intent);
            }
        });

       // imageView = (ImageView) view.findViewById(R.id.search_manager);
       // imageView.setClickable(true);

                // Intent intent = new Intent(Intent.ACTION_VIEW);
                // intent.setData(Uri.parse("market://search?q = " + search));
                // context.startActivity(intent);

                //Intent intent = ((Activity) context).getIntent();
                //final String search = intent.getStringExtra("search");

        return view;
    }

   /* public static void s(String search){
        List<PackageInfo> apps = context.getPackageManager().getInstalledPackages(0);
        //ArrayList<ApplicationInfo> res = new ArrayList<ApplicationInfo>();

        for (int i = 0; i < apps.size(); i++) {
            PackageInfo p = apps.get(i);
            //ApplicationInfo info = new ApplicationInfo();
            final String n = p.applicationInfo.loadLabel(context.getPackageManager()).toString();

            if (search == n) {
                Intent in = new Intent(context, AppSearch.class);

                in.putExtra("name", n);
                in.putExtra("pkgname", p.packageName);
                in.putExtra("desc", p.applicationInfo.loadDescription(context.getPackageManager()));
                // For icon
                Bitmap bitmap = null; // your bitmap
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                in.putExtra("icon", bs.toByteArray());

                context.startActivity(in);
                break;
            }
        }
    } */
}

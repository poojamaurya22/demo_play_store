package com.mobilinker.mobiplace.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilinker.mobiplace.R;

/**
 * Created by Pooja on 05-03-2017.
 */

public class AppSearch extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_search);

        if(getIntent().hasExtra("icon")) {
            ImageView imageView = (ImageView) findViewById(R.id.search_icon);
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("icon"),0,getIntent().getByteArrayExtra("icon").length);
            imageView.setImageBitmap(bitmap);
        }
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String pkg = i.getStringExtra("pkgname");
        String desc = i.getStringExtra("desc");

        TextView textView1 = (TextView) findViewById(R.id.search_app_name);
        TextView textView2 = (TextView) findViewById(R.id.search_package_name);
        TextView textView3 = (TextView) findViewById(R.id.app_desc);

        textView1.setText(name);
        textView2.setText(pkg);
        textView3.setText(desc);

    }

}

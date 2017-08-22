package com.mobilinker.mobiplace.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilinker.mobiplace.R;
import com.mobilinker.mobiplace.extra.AllConstants;
import com.mobilinker.mobiplace.extra.PrintLog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

/**
 * Created by Pooja on 25-02-2017.
 */

public class user extends Activity implements LocationListener{

    private static final Object REQUEST_READ_PHONE_STATE = "1";
    private static int IMAGE = 1;
    DatabaseHelper helper;

    LinearLayout home, deals, top, trends, categories;

    // private static final String SELECT_SQL = "SELECT * FROM contacts";
    // Cursor cursor;
    // SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        helper = new DatabaseHelper(this);

        //Set user image
        ImageView image = (ImageView) findViewById(R.id.userImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE);
            }
        });

        home = (LinearLayout) findViewById(R.id.home);
        home.setClickable(true);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, MainActivity.class);
                startActivity(i);
            }
        });

        deals = (LinearLayout) findViewById(R.id.deals);
        deals.setClickable(true);
        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, Deals.class);
                startActivity(i);
            }
        });

        top = (LinearLayout) findViewById(R.id.top);
        top.setClickable(true);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, Top.class);
                startActivity(i);
            }
        });

        trends = (LinearLayout) findViewById(R.id.trends);
        trends.setClickable(true);
        trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, Trends.class);
                startActivity(i);
            }
        });

        categories = (LinearLayout) findViewById(R.id.categories);
        categories.setClickable(true);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, Categories.class);
                startActivity(i);
            }
        });

        //Auto detected info
        TextView model = (TextView) findViewById(R.id.model);
        TextView brand = (TextView) findViewById(R.id.brand);
        TextView imei = (TextView) findViewById(R.id.imei);
        TextView region = (TextView) findViewById(R.id.region);
        TextView date = (TextView) findViewById(R.id.date);

        String service = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(service);
        String IMEI;
        IMEI = telephonyManager.getDeviceId();
        imei.setText(IMEI);

        String Model = Build.MODEL;
        model.setText(Model);

        String Device = Build.MANUFACTURER;
        brand.setText(Device);

        String place = Locale.getDefault().toString();
        region.setText(place);

        long d = Build.TIME;
        String dt = Long.toString(d);
        date.setText(dt);

        EditText name = (EditText) findViewById(R.id.userName);
        EditText email = (EditText) findViewById(R.id.userEmail);
        EditText phone = (EditText) findViewById(R.id.userPhone);


        String namestr = name.getText().toString();
        String emailstr = email.getText().toString();
        String phonestr = phone.getText().toString();

        Contact c = new Contact();
        c.setUname(namestr);
        c.setEmail(emailstr);
        c.setPhone(phonestr);
        helper.insertContact(c);

        //To save data
        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", namestr);
        editor.putString("email", emailstr);
        editor.putString("phone", phonestr);
        editor.apply();

        String n = sharedPreferences.getString("name", "");
        String e = sharedPreferences.getString("email", "");
        String p = sharedPreferences.getString("phone", "");

        name.setText(n);
        phone.setText(p);
        email.setText(e);

        //Retrieve data from SQLite database
        /*
        db = helper.getReadableDatabase();
        openDatabase();
        cursor = db.rawQuery(SELECT_SQL, null);
        cursor.moveToPosition(cursor.getCount()-1);
        showRecords(); */

    }

    /*protected void openDatabase() {
        db = openOrCreateDatabase("contacts", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String Name = cursor.getString(0);
        String Email = cursor.getString(1);
        String Phone = cursor.getString(2);
        name.setText(Name);
        email.setText(Email);
        phone.setText(Phone);
    } */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.userImage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.testmenu, menu);
       /* MenuItem item = menu.findItem(R.id.menu_app_list);
        myShareActionProvider = (ShareActionProvider) item.getActionProvider();
        myShareActionProvider.setShareHistoryFileName(
                ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        myShareActionProvider.setShareIntent(createShareIntent()); */
        return super.onCreateOptionsMenu(menu);

    }

    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private static final String TAG = user.class.getSimpleName();

    double latitude;
    double longitude;

    private Location mLastLocation;

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

//            btnStartLocationUpdates.setTitle(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
//            btnStartLocationUpdates
//                    .setTitle(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {


            case R.id.idloc_update:
                // refresh
                togglePeriodicLocationUpdates();

                return true;

            case R.id.idshow_me:
                // help action

                ShowonMap();
                return true;
            case R.id.idfind_us:
                // help action

                FindUsActivity();
                return true;
            case R.id.idabout_us:
                // check for updates action
                AboutUs();
                return true;
            case R.id.menu_app_list:
                Intent i = new Intent(user.this, AllAppsActivity.class);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************
     * Launching new activity
     * */

    private void ShowonMap() {
        Intent i = new Intent(user.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(user.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(user.this, FindUsActivity.class);
        startActivity(i);
    }

    private void ListActivity() {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    public void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude= mLastLocation.getLongitude();
//            lblLocation.setText(latitude + ", " + longitude);

            AllConstants.UPlat=Double.toString(latitude);
            AllConstants.UPlng=Double.toString(longitude);

//            lblLocation.setText(latitude + ", " + longitude);

            PrintLog.myLog("LatLong Found: LATT", +latitude + ", " + longitude);

        } else {

//            lblLocation
//                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

}
package com.mobilinker.mobiplace.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobilinker.mobiplace.R;
import com.mobilinker.mobiplace.extra.AllConstants;
import com.mobilinker.mobiplace.extra.PrintLog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Pooja on 28-02-2017.
 */

public class Trends extends Activity implements LocationListener{

    LinearLayout home, top, deals, categories;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trends);

        iUI();

        ImageView img = (ImageView) findViewById(R.id.userClick);
        img.bringToFront();
        img.setClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trends.this, user.class);
                startActivity(i);
            }
        });


        ImageView imageView = (ImageView)findViewById(R.id.add_trends);
        imageView.setClickable(true);
        imageView.setBackgroundResource(R.drawable.ads);
        AnimationDrawable adsAnimation = (AnimationDrawable) imageView.getBackground();
        adsAnimation.start();

        home = (LinearLayout) findViewById(R.id.home);
        home.setClickable(true);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trends.this, MainActivity.class);
                startActivity(i);
            }
        });

        deals = (LinearLayout) findViewById(R.id.deals);
        deals.setClickable(true);
        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trends.this, Deals.class);
                startActivity(i);
            }
        });

        top = (LinearLayout) findViewById(R.id.top);
        top.setClickable(true);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trends.this, Top.class);
                startActivity(i);
            }
        });

        categories = (LinearLayout) findViewById(R.id.categories);
        categories.setClickable(true);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trends.this, Categories.class);
                startActivity(i);
            }
        });
    }

    private LinearLayout atms, cafes, gym, hospitals, theater, park, police, school, mall;
    /////////////////Main Menu
    private void iUI() {

        atms = (LinearLayout) findViewById(R.id.trends_atms);
        atms.setClickable(true);
        atms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "ATMS LIST";
                AllConstants.query = "atm";
                final Intent atm = new Intent(Trends.this, Atms.class);
                atm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm);
            }
        });

        cafes = (LinearLayout) findViewById(R.id.trends_cafes);
        cafes.setClickable(true);
        cafes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "CAFES LIST";
                AllConstants.query = "cafe";
                final Intent cafe = new Intent(Trends.this, Cafes.class);
                cafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe);
            }
        });

        gym = (LinearLayout) findViewById(R.id.trends_gym);
        gym.setClickable(true);
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "GYM LIST";
                AllConstants.query = "gym";
                final Intent gym = new Intent(Trends.this, Gym.class);
                gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym);
            }
        });

        hospitals = (LinearLayout) findViewById(R.id.trends_hospitals);
        hospitals.setClickable(true);
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(Trends.this, Hospital.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);
            }
        });

        theater = (LinearLayout) findViewById(R.id.trends_theater);
        theater.setClickable(true);
        theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(Trends.this, Theater.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);
            }
        });

        park = (LinearLayout) findViewById(R.id.trends_park);
        park.setClickable(true);
        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(Trends.this, Park.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);
            }
        });

        police = (LinearLayout) findViewById(R.id.trends_police);
        police.setClickable(true);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "POLICE LIST";
                AllConstants.query = "police";
                final Intent police = new Intent(Trends.this, Police.class);
                police.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(police);
            }
        });

        school = (LinearLayout) findViewById(R.id.trends_school);
        school.setClickable(true);
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "SCHOOL LIST";
                AllConstants.query = "school";
                final Intent school = new Intent(Trends.this, Schools.class);
                school.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(school);
            }
        });

        mall = (LinearLayout) findViewById(R.id.trends_mall);
        mall.setClickable(true);
        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "SHOPPING MALL LIST";
                AllConstants.query = "shopping_mall";
                final Intent shopping_mall = new Intent(Trends.this, Malls.class);
                shopping_mall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(shopping_mall);
            }
        });

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

    private static final String TAG = Trends.class.getSimpleName();

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
                Intent i = new Intent(Trends.this, AllAppsActivity.class);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************
     * Launching new activity
     * */

    private void ShowonMap() {
        Intent i = new Intent(Trends.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(Trends.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(Trends.this, FindUsActivity.class);
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

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
import android.widget.Toast;

import com.mobilinker.mobiplace.R;
import com.mobilinker.mobiplace.extra.AllConstants;
import com.mobilinker.mobiplace.extra.PrintLog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Pooja on 02-03-2017.
 */

public class Theater extends Activity implements LocationListener {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theaters);

        ImageView imageView = (ImageView)findViewById(R.id.add_theater);
        imageView.setClickable(true);
        imageView.setBackgroundResource(R.drawable.ads);
        AnimationDrawable adsAnimation = (AnimationDrawable) imageView.getBackground();
        adsAnimation.start();
    }

    public void install_theater(View v){
        Intent i = new Intent(Theater.this, install.class);
        startActivity(i);
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

    private static final String TAG = Theater.class.getSimpleName();

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
                Intent i = new Intent(Theater.this, AllAppsActivity.class);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************
     * Launching new activity
     * */

    private void ShowonMap() {
        Intent i = new Intent(Theater.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(Theater.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(Theater.this, FindUsActivity.class);
        startActivity(i);
    }

    private void ListActivity() {
        Intent i = new Intent(this, Theater.class);
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

package com.mobilinker.mobiplace.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mobilinker.mobiplace.R;
import com.mobilinker.mobiplace.extra.AllConstants;
import com.mobilinker.mobiplace.extra.PrintLog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener, Observer {

    private Context con;
    private GoogleMap googleMap;

    // For ViewFlipper
    private ViewFlipper viewFlipper2;
    // private float initialX2;
    private ViewFlipper viewFlipper3;
    // private float initialX3;
    private ViewFlipper viewFlipper4;
    // private float initialX4;
    private ViewFlipper viewFlipper5;
    // private float initialX5;

    LinearLayout deals;
    LinearLayout top;
    LinearLayout trends;
    LinearLayout categories;


    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private TextView lblLocation;
    double latitude;
    double longitude;

    AdView adView;
    AdRequest bannerRequest, fullScreenAdRequest;
    InterstitialAd fullScreenAdd;
    private LinearLayout atms, banks, bookstores, busstations, cafes, carwash,
            dentist, doctor, food, gasstation, grocery, gym, hospitals,
            mosques, theater, park, pharmacy, police, restaurant, school, mall,
            spa, store, university;

    private ShareActionProvider myShareActionProvider;
    LocationFound updateLoc;

    // declare updater class member here (or in the Application)
    @SuppressWarnings("unused")
    private AutoUpdateApk aua;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        con = this;
        locUI();
        iUI();

        aua = new AutoUpdateApk(getApplicationContext(), AutoUpdateApk.PUBLIC_API_URL);	// <-- don't forget to instantiate
        aua.addObserver(this);

        ImageView imageView = (ImageView) findViewById(R.id.add_main);
        imageView.setClickable(true);
        imageView.setBackgroundResource(R.drawable.ads);
        AnimationDrawable adsAnimation = (AnimationDrawable) imageView.getBackground();
        adsAnimation.start();

        enableAd();
        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        } */

        ImageView img = (ImageView) findViewById(R.id.userClick);
        img.bringToFront();
        img.setClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, user.class);
                startActivity(i);
            }
        });

        viewFlipper2 = (ViewFlipper) findViewById(R.id.view_flipper2);
        viewFlipper2.setAutoStart(true);
        viewFlipper2.setFlipInterval(3000);
        viewFlipper2.startFlipping();

        viewFlipper3 = (ViewFlipper) findViewById(R.id.view_flipper3);
        viewFlipper3.setAutoStart(true);
        viewFlipper3.setFlipInterval(3000);
        viewFlipper3.startFlipping();

        viewFlipper4 = (ViewFlipper) findViewById(R.id.view_flipper4);
        viewFlipper4.setAutoStart(true);
        viewFlipper4.setFlipInterval(3000);
        viewFlipper4.startFlipping();

        viewFlipper5 = (ViewFlipper) findViewById(R.id.view_flipper5);
        viewFlipper5.setAutoStart(true);
        viewFlipper5.setFlipInterval(3000);
        viewFlipper5.startFlipping();

        deals = (LinearLayout) findViewById(R.id.deals);
        deals.setClickable(true);
        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Deals.class);
                startActivity(i);
            }
        });

        top = (LinearLayout) findViewById(R.id.top);
        top.setClickable(true);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Top.class);
                startActivity(i);
            }
        });

        trends = (LinearLayout) findViewById(R.id.trends);
        trends.setClickable(true);
        trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Trends.class);
                startActivity(i);
            }
        });

        categories = (LinearLayout) findViewById(R.id.categories);
        categories.setClickable(true);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Categories.class);
                startActivity(i);
            }
        });


    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(R.layout.main_land);
    } */


    private void enableAd() {
        // TODO Auto-generated method stub

        // adding banner add
//        adView = (AdView) findViewById(R.id.adView);
//        bannerRequest = new AdRequest.Builder().build();
//        adView.loadAd(bannerRequest);

        // adding full screen add
        fullScreenAdd = new InterstitialAd(this);
        fullScreenAdd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        fullScreenAdRequest = new AdRequest.Builder().build();
        fullScreenAdd.loadAd(fullScreenAdRequest);

        fullScreenAdd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {

                Log.i("FullScreenAdd", "Loaded successfully");
                fullScreenAdd.show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.i("FullScreenAdd", "failed to Load");
            }
        });

        // TODO Auto-generated method stub

    }




  /*  private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Turn ON your GPS to get better RESULTS.")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    } */

    /**
     * function to load map. If map is not created it will create it for you
     * */
//    private void initilizeMap() {
//
//        if (googleMap == null) {
//            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.ggmap)).getMap();
//
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
    public void locUI() {
//        lblLocation = (TextView) findViewById(R.id.textViewM);
//        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
//        btnStartLocationUpdates = (Button)findViewById(R.id.btnLocationUpdates);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

//        displayLocation();


        // Show location button click listener
//        btnShowLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                displayLocation();
//            }
//        });

        // Toggling the periodic location updates
//        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                togglePeriodicLocationUpdates();
//            }
//        });
        // Toggling the periodic location updates
    }


    //////// Location //////
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        initilizeMap();
        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    /**
     * Method to display the location on UI
     * */
    public void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
//            lblLocation.setText(latitude + ", " + longitude);

            AllConstants.UPlat = Double.toString(latitude);
            AllConstants.UPlng = Double.toString(longitude);

//            lblLocation.setText(latitude + ", " + longitude);

            PrintLog.myLog("LatLong Found: LATT", +latitude + ", " + longitude);

        } else {

//            lblLocation
//                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }


    }

//    private Menu menu;
//    MenuItem btnStartLocationUpdates = menu.findItem(R.id.action_check_updates);

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

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
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



    /////////
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

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        myShareActionProvider = (ShareActionProvider)item.getActionProvider();
//        myShareActionProvider.setShareHistoryFileName(
//                ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
//        myShareActionProvider.setShareIntent(createShareIntent());
//        return true;
//    }
//


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
                Intent i = new Intent(MainActivity.this, AllAppsActivity.class);
                startActivity(i);


            case R.id.download_manager:
                Intent intent = new Intent(MainActivity.this, DownloadManager.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************
     * Launching new activity
     * */

    private void ShowonMap() {
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(MainActivity.this, FindUsActivity.class);
        startActivity(i);
    }

    private void ListActivity() {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }


    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=com.codeandcoder.finalguide");
        return shareIntent;
    }



    /////////////////Main Menu
    private void iUI() {

        atms = (LinearLayout) findViewById(R.id.atms);
        atms.setClickable(true);
        atms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "ATMS LIST";
                AllConstants.query = "atm";
                final Intent atm = new Intent(MainActivity.this, Atms.class);
                atm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm);
            }
        });

        banks = (LinearLayout) findViewById(R.id.banks);
        banks.setClickable(true);
        banks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "BOOK STORES LIST";
                AllConstants.query = "book_store";
                final Intent book_store = new Intent(MainActivity.this, Banks.class);
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);
            }
        });

        bookstores = (LinearLayout) findViewById(R.id.bookstores);
        bookstores.setClickable(true);
        bookstores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "BOOK STORES LIST";
                AllConstants.query = "book_store";
                final Intent book_store = new Intent(MainActivity.this, BookStore.class);
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);
            }
        });

        busstations = (LinearLayout) findViewById(R.id.busstations);
        busstations.setClickable(true);
        busstations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "BUS STATION LIST";
                AllConstants.query = "bus_station";
                final Intent bus_station = new Intent(MainActivity.this, BusStation.class);
                bus_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bus_station);
            }
        });

        cafes = (LinearLayout) findViewById(R.id.cafes);
        cafes.setClickable(true);
        cafes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "CAFES LIST";
                AllConstants.query = "cafe";
                final Intent cafe = new Intent(MainActivity.this, Cafes.class);
                cafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe);
            }
        });

        carwash = (LinearLayout) findViewById(R.id.carwash);
        carwash.setClickable(true);
        carwash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "CAR WASH LIST";
                AllConstants.query = "car_wash";
                final Intent car_wash = new Intent(MainActivity.this, Carwash.class);
                car_wash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(car_wash);
            }
        });

        dentist = (LinearLayout) findViewById(R.id.dentist);
        dentist.setClickable(true);
        dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "DENTIST LIST";
                AllConstants.query = "dentist";
                final Intent dentist = new Intent(MainActivity.this, Dentist.class);
                dentist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dentist);
            }
        });

        doctor = (LinearLayout) findViewById(R.id.doctor);
        doctor.setClickable(true);
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "DOCTOR LIST";
                AllConstants.query = "doctor";
                final Intent doctor = new Intent(MainActivity.this, Doctor.class);
                doctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(doctor);
            }
        });

        food = (LinearLayout) findViewById(R.id.food);
        food.setClickable(true);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "FOOD LIST";
                AllConstants.query = "food";
                final Intent food = new Intent(MainActivity.this, Food.class);
                food.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(food);
            }
        });

        gasstation = (LinearLayout) findViewById(R.id.gasstation);
        gasstation.setClickable(true);
        gasstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "GAS STATION LIST";
                AllConstants.query = "gas_station";
                final Intent gas_station = new Intent(MainActivity.this, GasStation.class);
                gas_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gas_station);
            }
        });

        grocery = (LinearLayout) findViewById(R.id.grocery);
        grocery.setClickable(true);
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "GROCERY LIST";
                AllConstants.query = "grocery_or_supermarket";
                final Intent grocery_or_supermarket = new Intent(MainActivity.this, Grocery.class);
                grocery_or_supermarket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(grocery_or_supermarket);
            }
        });

        gym = (LinearLayout) findViewById(R.id.gym);
        gym.setClickable(true);
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "GYM LIST";
                AllConstants.query = "gym";
                final Intent gym = new Intent(MainActivity.this, Gym.class);
                gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym);
            }
        });

        hospitals = (LinearLayout) findViewById(R.id.hospitals);
        hospitals.setClickable(true);
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(MainActivity.this, Hospital.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);
            }
        });

        mosques = (LinearLayout) findViewById(R.id.mosques);
        mosques.setClickable(true);
        mosques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(MainActivity.this, Mosques.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);
            }
        });

        theater = (LinearLayout) findViewById(R.id.theater);
        theater.setClickable(true);
        theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(MainActivity.this, Theater.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);
            }
        });

        park = (LinearLayout) findViewById(R.id.park);
        park.setClickable(true);
        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(MainActivity.this, Park.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);
            }
        });

        pharmacy = (LinearLayout) findViewById(R.id.pharmacy);
        pharmacy.setClickable(true);
        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "PHARMACY LIST";
                AllConstants.query = "pharmacy";
                final Intent pharmacy = new Intent(MainActivity.this, Pharmacy.class);
                pharmacy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pharmacy);
            }
        });

        police = (LinearLayout) findViewById(R.id.police);
        police.setClickable(true);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "POLICE LIST";
                AllConstants.query = "police";
                final Intent police = new Intent(MainActivity.this, Police.class);
                police.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(police);
            }
        });

        restaurant = (LinearLayout) findViewById(R.id.restaurant);
        restaurant.setClickable(true);
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "RESTAURANT LIST";
                AllConstants.query = "restaurant";
                final Intent restaurant = new Intent(MainActivity.this, Restaurants.class);
                restaurant.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restaurant);
            }
        });

        school = (LinearLayout) findViewById(R.id.school);
        school.setClickable(true);
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "SCHOOL LIST";
                AllConstants.query = "school";
                final Intent school = new Intent(MainActivity.this, Schools.class);
                school.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(school);
            }
        });

        mall = (LinearLayout) findViewById(R.id.mall);
        mall.setClickable(true);
        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "SHOPPING MALL LIST";
                AllConstants.query = "shopping_mall";
                final Intent shopping_mall = new Intent(MainActivity.this, Malls.class);
                shopping_mall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(shopping_mall);
            }
        });

        spa = (LinearLayout) findViewById(R.id.spa);
        spa.setClickable(true);
        spa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "SPA LIST";
                AllConstants.query = "spa";
                final Intent spa = new Intent(MainActivity.this, Spas.class);
                spa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(spa);
            }
        });

        store = (LinearLayout) findViewById(R.id.store);
        store.setClickable(true);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "STORE LIST";
                AllConstants.query = "store";
                final Intent store = new Intent(MainActivity.this, Stores.class);
                store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(store);
            }
        });

        university = (LinearLayout) findViewById(R.id.university);
        university.setClickable(true);
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.topTitle = "UNIVERSITY LIST";
                AllConstants.query = "universities";
                final Intent university = new Intent(MainActivity.this, Universities.class);
                university.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(university);
            }
        });

    }

    @Override
    public void update(Observable observable, Object data) {

        if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_GOT_UPDATE) ) {
            android.util.Log.i("AutoUpdateApkActivity", "Have just received update!");
        }
        if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_HAVE_UPDATE) ) {
            android.util.Log.i("AutoUpdateApkActivity", "There's an update available!");
        }

    }

    /*
    @Override
    public void onClick(View v) {

        togglePeriodicLocationUpdates();

        switch (v.getId()) {

            case R.id.atms:
                AllConstants.topTitle = "ATMS LIST";
                AllConstants.query = "atm";
                final Intent atm = new Intent(this, ListActivity.class);
                atm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm);

                break;

            case R.id.banks:
               AllConstants.topTitle = "BOOK STORES LIST";
                AllConstants.query = "book_store";
                final Intent book_store = new Intent(this, ListActivity.class);
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);

                break;

            case R.id.bookstores:
                AllConstants.topTitle = "BOOK STORES LIST";
                AllConstants.query = "book_store";
                final Intent book_store = new Intent(this, ListActivity.class);
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);

                break;
            case R.id.busstations:
                AllConstants.topTitle = "BUS STATION LIST";
                AllConstants.query = "bus_station";
                final Intent bus_station = new Intent(this, ListActivity.class);
                bus_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bus_station);

                break;
            case R.id.cafes:
                AllConstants.topTitle = "CAFES LIST";
                AllConstants.query = "cafe";
                final Intent cafe = new Intent(this, ListActivity.class);
                cafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe);

                break;

            case R.id.carwash:
                AllConstants.topTitle = "CAR WASH LIST";
                AllConstants.query = "car_wash";
                final Intent car_wash = new Intent(this, ListActivity.class);
                car_wash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(car_wash);

                break;

            case R.id.dentist:
                AllConstants.topTitle = "DENTIST LIST";
                AllConstants.query = "dentist";
                final Intent dentist = new Intent(this, ListActivity.class);
                dentist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dentist);

                break;
            case R.id.doctor:
                AllConstants.topTitle = "DOCTOR LIST";
                AllConstants.query = "doctor";
                final Intent doctor = new Intent(this, ListActivity.class);
                doctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(doctor);

                break;
            case R.id.food:
                AllConstants.topTitle = "FOOD LIST";
                AllConstants.query = "food";
                final Intent food = new Intent(this, ListActivity.class);
                food.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(food);

                break;

            case R.id.gasstation:
                AllConstants.topTitle = "GAS STATION LIST";
                AllConstants.query = "gas_station";
                final Intent gas_station = new Intent(this, ListActivity.class);
                gas_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gas_station);

                break;

            case R.id.grocery:
                AllConstants.topTitle = "GROCERY LIST";
                AllConstants.query = "grocery_or_supermarket";
                final Intent grocery_or_supermarket = new Intent(this,
                        ListActivity.class);
                grocery_or_supermarket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(grocery_or_supermarket);

                break;
            case R.id.gym:
                AllConstants.topTitle = "GYM LIST";
                AllConstants.query = "gym";
                final Intent gym = new Intent(this, ListActivity.class);
                gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym);

                break;
            case R.id.hospitals:
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(this, ListActivity.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);

                break;

            case R.id.mosques:
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(this, ListActivity.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);

                break;

            case R.id.theater:
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(this, ListActivity.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);

                break;
            case R.id.park:
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(this, ListActivity.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);

                break;
            case R.id.pharmacy:
                AllConstants.topTitle = "PHARMACY LIST";
                AllConstants.query = "pharmacy";
                final Intent pharmacy = new Intent(this, ListActivity.class);
                pharmacy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pharmacy);

                break;

            case R.id.police:
                AllConstants.topTitle = "POLICE LIST";
                AllConstants.query = "police";
                final Intent police = new Intent(this, ListActivity.class);
                police.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(police);

                break;

            case R.id.restaurant:
                AllConstants.topTitle = "RESTAURANT LIST";
                AllConstants.query = "restaurant";
                final Intent restaurant = new Intent(this, ListActivity.class);
                restaurant.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restaurant);

                break;
            case R.id.school:
                AllConstants.topTitle = "SCHOOL LIST";
                AllConstants.query = "school";
                final Intent school = new Intent(this, ListActivity.class);
                school.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(school);

                break;
            case R.id.mall:
                AllConstants.topTitle = "SHOPPING MALL LIST";
                AllConstants.query = "shopping_mall";
                final Intent shopping_mall = new Intent(this, ListActivity.class);
                shopping_mall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(shopping_mall);

                break;

            case R.id.spa:
                AllConstants.topTitle = "SPA LIST";
                AllConstants.query = "spa";
                final Intent spa = new Intent(this, ListActivity.class);
                spa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(spa);

                break;

            case R.id.store:
                AllConstants.topTitle = "STORE LIST";
                AllConstants.query = "store";
                final Intent store = new Intent(this, ListActivity.class);
                store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(store);

                break;

            case R.id.universities:
                AllConstants.topTitle = "UNIVERSITY LIST";
                AllConstants.query = "universities";
                final Intent universities = new Intent(this, ListActivity.class);
                universities.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(universities);

        }
    } */

}
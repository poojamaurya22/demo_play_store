package com.mobilinker.mobiplace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobilinker.mobiplace.R;
import com.mobilinker.mobiplace.extra.AllConstants;
import com.mobilinker.mobiplace.extra.AllURL;
import com.mobilinker.mobiplace.holder.DrivingDetails;
import com.mobilinker.mobiplace.model.DrivingTime;
import com.mobilinker.mobiplace.parser.DrivingDetailsParser;

public class DrivingDetailsActivity extends Activity {


    private Context con;
    private String pos = "";
    private TextView dName;

    private ProgressDialog pDialog;
    private DrivingTime DT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_details);

        dName = (TextView) findViewById(R.id.driveView);

        updateUI();
    }

    private void updateUI() {


        pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

        final Thread d = new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub

                try {
                    if (DrivingDetailsParser.connect(con, AllURL
                            .drivingURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
                                    AllConstants.apiKey))) {
                    }

                } catch (final Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        if (pDialog != null) {
                            pDialog.cancel();
                        }
//						Typeface title = Typeface.createFromAsset(getAssets(),
//								"fonts/ROBOTO-REGULAR.TTF");
//						Typeface add = Typeface.createFromAsset(getAssets(),
//								"fonts/ROBOTO-LIGHT.TTF");
                        try {

                            DT = DrivingDetails.getAlldrivingdetails()
                                    .elementAt(0);

                            dName.setText(DT.getTime().trim());


                            // ------Rating ---


                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                    }
                });

            }
        });
        d.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.testmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

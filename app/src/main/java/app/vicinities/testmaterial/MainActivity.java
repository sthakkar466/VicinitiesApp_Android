package app.vicinities.testmaterial;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.microsoft.windowsazure.mobileservices.*;
//import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
//import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
//import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ViewPager viewPager;

    private Toolbar toolbar;

    private ImageButton map_tab_button, feed_tab_button, people_tab_button;

    //private MobileServiceClient mClient;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

//    private HOME1_MapPageFragment mapPage;
//    private HOME2_FeedPageFragment feedPage;
//    private HOME3_PeoplePageFragment peoplePage;

    public VicinitiesApp application;

    public UpdateDatabaseService mService;
    public boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SETUP
        super.onCreate(savedInstanceState);
        application = (VicinitiesApp)getApplicationContext();
        setContentView(R.layout.home_container);

        //SETUP LOCATION
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //SETUP VIEWPAGER
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new HomePagesAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new HomePageChangeListener());

        //SETUP TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //SETUP NAV DRAWER
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //SETUP TABS
        map_tab_button = (ImageButton) findViewById(R.id.map_tab_button);
        feed_tab_button = (ImageButton) findViewById(R.id.feed_tab_button);
        people_tab_button = (ImageButton) findViewById(R.id.people_tab_button);
        map_tab_button.setOnClickListener(this);
        feed_tab_button.setOnClickListener(this);
        people_tab_button.setOnClickListener(this);

        //SETUP PAGES
//        mapPage = new HOME1_MapPageFragment();
//        feedPage = new HOME2_FeedPageFragment();
//        peoplePage = new HOME3_PeoplePageFragment();

//        mapPage = application.mapPage;
//        feedPage = application.feedPage;
//        peoplePage = application.peoplePage;

        //SETUP TABLE
//        try {
//            mClient = new MobileServiceClient(
//                    "https://vicinities-test.azure-mobile.net/",
//                    "hAgSPHsgZxBFzuawIBQjPGFSwUwKvH34",
//                    this
//            );
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            System.out.println("A\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nERRRRROROROR");
//        }
//        final MobileServiceTable<login> loginTable = mClient.getTable(login.class);
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    final MobileServiceList<login> result =
//                            loginTable.execute().get();
//                    for (login item : result) {
//                        peoplePage.peopleList.add(item);
//                    }
//                } catch (Exception exception) {
//
//                }
//                return null;
//            }
//        }.execute();
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                login item = new login();
//                item.id = "dingle";
//                item.FirstName = "Raheem";
//                item.LastName = "Potato";
//                item.Email = "dingle.dingle@dingle.com";
//                item.Password = "dingle";
//                System.out.println('A');
//                try {
//                    loginTable.insert(item).get();
//                } catch (Exception exception) {
//                    System.out.println('B');
//                }
//                return null;
//            }
//        }.execute();

    }

////////////////////////////OPTIONS MENU METHODS//////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, TestPageActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_tab_button:
                viewPager.setCurrentItem(0);
                break;
            case R.id.feed_tab_button:
                viewPager.setCurrentItem(1);
                break;
            case R.id.people_tab_button:
                viewPager.setCurrentItem(2);
                login dingle = new login();
                dingle.FirstName = "hello," +application.time+ mService.dingle + (mService.mBound?"hi":"ho");
                dingle.LastName = "dingle";
                application.peoplePage.add(dingle);
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            application.mapPage.mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Me"));
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, "no Location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

////////////////////NEW PAGERADAPTER FOR VIEWPAGER/////////////////////////
    class HomePagesAdapter extends FragmentPagerAdapter {

        public HomePagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        Fragment page = null;
            switch (position) {
                case 0:
                    if (application.mapPage == null) {
                        application.mapPage = new HOME1_MapPageFragment();
                    }
                    page = application.mapPage;
                            //new HOME1_MapPageFragment();
                    break;
                case 1:
                    if (application.feedPage == null) {
                        application.feedPage = new HOME2_FeedPageFragment();
                    }
                    page = application.feedPage;
                            //new HOME2_FeedPageFragment();
                    break;
                case 2:
                    if (application.peoplePage == null) {
                        application.peoplePage = new HOME3_PeoplePageFragment();
                    }
                    page = application.peoplePage;
                            //new HOME3_PeoplePageFragment();
                    break;
            }
            return page;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
////////////////////END PAGERADAPTER FOR VIEWPAGER/////////////////////////
///////////////NEW PAGECHANGELISTENER FOR VIEWPAGER////////////////////
    class HomePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
///////////////END PAGECHANGELISTENER FOR VIEWPAGER/////////////////////
/////////////////////////////////CALLBACKS/////////////////////////////
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        application.peoplePage.onSaveInstanceState(outState);
        application.feedPage.onSaveInstanceState(outState);
        application.mapPage.onSaveInstanceState(outState);
        application.feedPage.saveScrollState();
        application.peoplePage.saveScrollState();
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("SAVEDINDEX", viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            application.peoplePage.onCreate(savedInstanceState);
            application.feedPage.onCreate(savedInstanceState);
            application.mapPage.onCreate(savedInstanceState);
            application.feedPage.restoreScrollState();
            application.peoplePage.restoreScrollState();
            viewPager.setCurrentItem(savedInstanceState.getInt("SAVEDINDEX"), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Intent intent = new Intent(this, UpdateDatabaseService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
//        application.peoplePage.onPause();
//        application.feedPage.onPause();
//        application.mapPage.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
//        application.peoplePage.onStop();
//        application.feedPage.onStop();
//        application.mapPage.onStop();
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
/////////////////////////////////END CALLBACKS/////////////////////////////
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                   IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            UpdateDatabaseService.UpdateDatabaseServiceBinder binder = (UpdateDatabaseService.UpdateDatabaseServiceBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService.mBound = false;
        }
    };
}
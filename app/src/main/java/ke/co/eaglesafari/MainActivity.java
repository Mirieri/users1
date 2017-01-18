package ke.co.eaglesafari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ke.co.eaglesafari.auth.LoginDb;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.db.PaginationDb;
import ke.co.eaglesafari.fragments.Application;
import ke.co.eaglesafari.fragments.History;
import ke.co.eaglesafari.fragments.Request_taxi;
import ke.co.eaglesafari.fragments.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Application.OnFragmentInteractionListener,
        History.OnFragmentInteractionListener, Settings.OnFragmentInteractionListener {
    public static FragmentManager fragmentManager;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        Log.e("MainActivity", "Main Activity Startedted")
        ;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {

            displayView(getIntent().getIntExtra(Constant.KEY_ACTIVE_FRAGMENT, 0));

        }
        //set initial fragment
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            k += 1;

            if (k == 1) {
                Toast.makeText(getApplicationContext(),
                        "Please click BACK again to exit.", Toast.LENGTH_SHORT)
                        .show();

            } else if (k == 2) {
                finish();
            }

            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              k = 0;
                                          }

                                      }

                    , 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            LoginDb loginDb = LoginDb.getInstance(MainActivity.this);
            loginDb.getWritableDatabase();
            loginDb.resetTables();

            PaginationDb paginationDb = PaginationDb.getInstance(MainActivity.this);
            paginationDb.getWritableDatabase();
            paginationDb.resetTables();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_request_taxi) {
            displayView(0);
        } else if (id == R.id.nav_history) {
            displayView(1);

        } else if (id == R.id.nav_help) {
            displayView(2);

        } else if (id == R.id.nav_share) {


            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=ke.co.eaglesafari");
            startActivity(Intent.createChooser(sharingIntent, "Share link to download this app"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        // check what we have

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Request_taxi();
                break;

            case 1:
                fragment = new History();
                break;
            case 2:
                fragment = new Application();
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

           /* mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);

            mDrawerLayout.closeDrawer(mDrawerList);*/
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.example.jacob.finalproject;


import android.app.FragmentManager;
import android.content.Context;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    fragmentPrice fragmentPrice;
    fragmentBalance fragmentBalance;
    FragmentManager fragmentManager;
    chartFragment chartFragment;
    fragmentBlock fragmentBlock;
    Logger log = Logger.getAnonymousLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        navigationView = (NavigationView) findViewById(R.id.nagView);

        if (findViewById(R.id.menuOption) == null) {
            System.out.println("potrait");
            drawerLayout = (DrawerLayout) findViewById(R.id.mainLayout);
            toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            fragmentPrice = new fragmentPrice();
            fragmentManager.beginTransaction().replace(R.id.detailLayout, fragmentPrice).commit();
            fragmentManager.executePendingTransactions();

        } else {

        }
     //   fragmentManager = getFragmentManager();
       // navigationView = (NavigationView) findViewById(R.id.nagView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override

            public boolean onNavigationItemSelected(MenuItem item) {
                Context c = getApplicationContext();
                switch (item.getItemId()) {
                    case R.id.get_price:
                        Toast.makeText(c, "Price", Toast.LENGTH_LONG).show();
                        fragmentPrice = new fragmentPrice();
                        fragmentManager.beginTransaction().replace(R.id.detailLayout, fragmentPrice).commit();
                        fragmentManager.executePendingTransactions();
                        log.info("On click called ");
                        break;

                    case R.id.get_chart:
                        Toast.makeText(c, "Chart", Toast.LENGTH_LONG).show();
                        chartFragment = new chartFragment();
                        fragmentManager.beginTransaction().replace(R.id.detailLayout, chartFragment).commit();
                        fragmentManager.executePendingTransactions();
                        break;

                    case R.id.get_blockInfo:
                        Toast.makeText(c, "block Info", Toast.LENGTH_LONG).show();
                        fragmentBlock = new fragmentBlock();
                        fragmentManager.beginTransaction().replace(R.id.detailLayout, fragmentBlock).commit();
                        fragmentManager.executePendingTransactions();
                        break;

                    case R.id.get_balance:
                        Toast.makeText(c, "Balance", Toast.LENGTH_LONG).show();
                        fragmentBalance = new fragmentBalance();
                        fragmentManager.beginTransaction().replace(R.id.detailLayout, fragmentBalance).commit();
                        fragmentManager.executePendingTransactions();
                        break;

                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    /*    getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchitem = menu.findItem(R.id.action_search_button);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
  //      System.out.println("-----------------------------"+searchView +         searchView.toString());
*/
        return super.onCreateOptionsMenu(menu);

    }


}
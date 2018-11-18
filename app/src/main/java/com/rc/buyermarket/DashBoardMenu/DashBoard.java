package com.rc.buyermarket.DashBoardMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rc.buyermarket.NavigationDrawerMenu.Home_Navigation;
import com.rc.buyermarket.NavigationDrawerMenu.Support_Navigation;
import com.rc.buyermarket.NavigationDrawerMenu.UpdateSettingsNavigation;
import com.rc.buyermarket.R;
import com.rc.buyermarket.view.CanaroTextView;

public class DashBoard extends AppCompatActivity implements View.OnClickListener{
    private CardView bankCard,ideasCard,addCard,linksCard,wifiCard;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    CanaroTextView tvTitle;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private FrameLayout fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        //initialize Toolbar
        setUpToolBar();
        //initialize UI
        initUI();
        //initialize Listener
        initListenerComponent();

      //  initFragment();
        }

//    private void initFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragment = new LandingFragment();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//    }

    private void initListenerComponent() {
        //add click listener to the cards
        bankCard.setOnClickListener(this);
        ideasCard.setOnClickListener(this);
        addCard.setOnClickListener(this);
        linksCard.setOnClickListener(this);
        wifiCard.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent i;
                switch (item.getItemId()){
//                    case R.id.nav_profile:
//                        i=new Intent( DashBoard.this,UpdateSettingsNavigation.class);
//                        startActivity(i);
//                        break;
                    case R.id.nav_home:
                        i=new Intent( DashBoard.this,Home_Navigation.class);
                        startActivity(i);
                        break;

                    default:break;
                }

                return true;
            }
        });
    }

    private void initUI() {
        navigationView=findViewById(R.id.navigation_menu);
        fm = (FrameLayout) findViewById(R.id.fragment_container);

        //defining cards
        bankCard=findViewById(R.id.bank_cardId);
        ideasCard=findViewById(R.id.search_cardId);
        addCard=findViewById(R.id.add_cardId);
        linksCard=findViewById(R.id.links_cardId);
        wifiCard=findViewById(R.id.wifi_cardId);
    }


    private void setUpToolBar(){
        drawerLayout=findViewById(R.id.drawerLayoutId);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        tvTitle.setText("Home");
//        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.bank_cardId: i=new Intent(this,AddHouse.class);startActivity(i);break;
            case R.id.search_cardId: i=new Intent(this,Singup.class);startActivity(i);break;
            case R.id.add_cardId: i=new Intent(this,Login.class);startActivity(i);break;
            case R.id.links_cardId: i=new Intent(this,Links.class);startActivity(i);break;
            case R.id.wifi_cardId: i=new Intent(this,Wifi.class);startActivity(i);break;
            default:break;
        }

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}


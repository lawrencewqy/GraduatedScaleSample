package com.lawrencewqy.graduatedscalesample;

import com.lawrencewqy.graduatedscale.ScaleView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.et_bottomlinestroke)
    EditText et_bottomlinestroke;
    @Bind(R.id.et_linespace)
    EditText et_linespace;
    @Bind(R.id.et_linestroke)
    EditText et_linestroke;
    @Bind(R.id.et_textsize)
    EditText et_textsize;

    @Bind(R.id.scaleview)
    ScaleView mScaleview;
    @OnClick(R.id.btn_setcontent)
    void setContent(View view){
        mScaleview.setContentList(getScaleList());
    }

    @OnClick(R.id.btn_linestroke_ok)
    void setLineStroke(View view){
        String linestroke = et_linestroke.getText().toString();
        if(!TextUtils.isEmpty(linestroke)){
            mScaleview.setLineStrokeWidth(Integer.valueOf(linestroke));
        }
    }

    @OnClick(R.id.btn_bottomlinestroke_ok)
    void setbottomLineStroke(View view){
        String bottomlinestroke = et_bottomlinestroke.getText().toString();
        if(!TextUtils.isEmpty(bottomlinestroke)){
            mScaleview.setBottomStroke(Integer.valueOf(bottomlinestroke));
        }
    }

    @OnClick(R.id.btn_linespace_ok)
    void setlinespace(View view){
        String linespace = et_linespace.getText().toString();
        if(!TextUtils.isEmpty(linespace)){
            mScaleview.setLineSpace(Integer.valueOf(linespace));
        }
    }


    @OnClick(R.id.btn_settextsize_ok)
    void setTextSize(View view){
        String textSize = et_textsize.getText().toString();
        if(!TextUtils.isEmpty(textSize)){
            mScaleview.setTextSize(Integer.valueOf(textSize));
        }
    }

    @OnClick(R.id.btn_setcursor)
    void setCursor(View view){
        mScaleview.setCursorImageRes(R.drawable.ic_pin);
    }

    @OnClick(R.id.btn_sethighlightcolor)
    void sethighlightcolor(View v){
        mScaleview.setHighLightColor(Color.GREEN);
    }

    private ArrayList<String> getScaleList(){
        ArrayList<String> scaleList = new ArrayList<>();
        for(int i = 0;i<20;i++){
            scaleList.add(String.valueOf(0.5*(i+1)));
        }
        return scaleList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

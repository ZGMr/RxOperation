package com.li.pro.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.li.fragmentutils.Fragmentation;
import com.li.fragmentutils.SupportFragment;
import com.li.fragmentutils.base.BaseActivity;
import com.li.fragmentutils.base.BaseLazyFragment;
import com.li.fragmentutils.base.BaseLazySwipFragment;
import com.li.pro.adapter.NavRecycleViewAdapter;
import com.li.pro.view.fragment.home.HomeFragment;
import com.li.pro.view.fragment.home.HomeFragment2;
import com.li.pro.view.fragment.home.HomeFragment3;
import com.li.pro.view.fragment.home.HomeFragment4;
import com.li.pro.view.fragment.rxjava.FragmentRxJava;
import com.li.skipAnimation.main.TransitionsHeleper;
import com.li.utils.AdbUtilS;
import com.li.utils.ui.mdbottom.BottomNavigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rxop.li.com.rxoperation.R;

public class ActivityMain extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigation.OnMenuItemSelectionListener, BaseLazySwipFragment.OnBackToFirstListener,BaseLazyFragment.OnBackToFirstListener {

    private CoordinatorLayout cdl_root;
    private RecyclerView rv_nav;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    //普通导航栏
//    private BottomBar bb_root;
    //MD导航栏
    private BottomNavigation bn_home_bottombar;

    private TabLayout tl_main;
    private DrawerLayout drawer;

    @Override
    public int bindLayout() {
        return R.layout.activity_item_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void doBusiness(Bundle savedInstanceState) {

        drawer = (DrawerLayout) findViewById(R.id.dl_nva);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        rv_nav = (RecyclerView) navigationView.
//                getHeaderView(0).
        findViewById(R.id.rv_nav);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_nav.setLayoutManager(mLayoutManager);
        NavRecycleViewAdapter.getInstance().init(this).clearAll();
        rv_nav.setAdapter(NavRecycleViewAdapter.getInstance()
                .init(this).addItem("RXJava", "开启wifi调试(需要Root)"));
        NavRecycleViewAdapter.getInstance().setOnNavRecycleViewItemClickListener(new NavRecycleViewAdapter.OnNavRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String str) {
                switch (position) {
                    case 0:
                        FragmentRxJava fragmentRxJava=new FragmentRxJava();
                        fragmentRxJava.startInitAnimation(ActivityMain.this, view, R.id.fl_mainroot);
                        Fragmentation.getInstance(ActivityMain.this).loadRootTransaction(getSupportFragmentManager(), R.id.fl_mainroot, new FragmentRxJava());
                        drawer.closeDrawers();
                        break;
                    case 1:
                        //开启ADB wifi调试
                        try {
                            AdbUtilS.getInstance().set(5555);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        NavRecycleViewAdapter.getInstance().refresh();

        navigationView.setNavigationItemSelectedListener(this);
        //普通导航栏
//        bb_root = (BottomBar) findViewById(R.id.bb_root);
//        bb_root.addItem(new BottomBarTab(this, R.drawable.ic_home_white_24dp))
//                .addItem(new BottomBarTab(this, R.drawable.ic_discover_white_24dp))
//                .addItem(new BottomBarTab(this, R.drawable.ic_message_white_24dp))
//                .addItem(new BottomBarTab(this, R.drawable.ic_account_circle_white_24dp));
//        bb_root.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
//            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
//            @Override
//            public void onTabSelected(int position, int prePosition) {
//                bb_root.setCurrentItem(position);
//                HomeFragment homeFragment = Fragmentation.getInstance(ActivityMain.this).findStackFragment(HomeFragment.class, getSupportFragmentManager(), true);
//                Fragmentation.getInstance(ActivityMain.this)
//                        .showHideFragment(getSupportFragmentManager(), homeFragment, homeFragment);
//                ((Button) homeFragment.getView().findViewById(R.id.tv_test)).setText("position：" + position + "\n prePosition" + prePosition);
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_nva);
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(int position) {
//
//            }
//
//            @Override
//            public void onTabReselected(int position) {
//            }
//        });

        //MD风格导航栏
        bn_home_bottombar = (BottomNavigation) findViewById(R.id.bn_home_bottombar);
        bn_home_bottombar.setDefaultSelectedIndex(0);
        bn_home_bottombar.setOnMenuItemClickListener(this);
//        bn_home_bottombar.getBadgeProvider().show();

        //加载默认Fregment
        if (savedInstanceState == null) {
            HomeFragment homeFragment=new HomeFragment();
            HomeFragment2 homeFragment2=new HomeFragment2();
            HomeFragment3 homeFragment3=new HomeFragment3();
            HomeFragment4 homeFragment4=new HomeFragment4();
            loadRootFragment( R.id.fl_home_root,homeFragment4);
            loadRootFragment( R.id.fl_home_root,homeFragment3);
            loadRootFragment( R.id.fl_home_root,homeFragment2);
            loadRootFragment( R.id.fl_home_root,homeFragment);
//            loadMultipleRootFragment( R.id.fl_home_root, 0, homeFragment, homeFragment2);
            fragments.add(homeFragment);
            fragments.add(homeFragment2);
            fragments.add(homeFragment3);
            fragments.add(homeFragment4);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到可滑动关闭的Activity
                Intent intent = new Intent(ActivityMain.this, ItemDetailActivity.class);
                TransitionsHeleper.startActivity(ActivityMain.this, intent, fab);
                //跳转到主页Fragment
                HomeFragment homeFragment = Fragmentation.getInstance(ActivityMain.this).findStackFragment(HomeFragment.class, getSupportFragmentManager(), true);
                Fragmentation.getInstance(ActivityMain.this)
                        .showHideFragment(getSupportFragmentManager(), homeFragment, homeFragment);
            }
        });

    }

    @Override
    public String setToolBarTitle() {
        return "BaseProject";
    }

    @Override
    public boolean isSetTransparentBar() {
        return true;
    }

    @Override
    public boolean isShowBackArrow() {
        return false;
    }

    @Override
    public int setLeftCornerLogo() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public String setActionBarCenterTitle() {
        return "RxJava2";
    }

    @Override
    public boolean isHideActionBar() {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    List<SupportFragment> fragments = new ArrayList<>();

    int prePosition = 0;

    //被选中时触发
    @Override
    public void onMenuItemSelect(@IdRes int itemId, int position) {
        showHideFragment(fragments.get(position), fragments.get(prePosition));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_nva);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        bn_home_bottombar.getBadgeProvider().remove(itemId);
        prePosition = position;
    }

    //被选中时再次点击触发
    @Override
    public void onMenuItemReselect(@IdRes int itemId, int position) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackToFirstFragment() {

    }
}

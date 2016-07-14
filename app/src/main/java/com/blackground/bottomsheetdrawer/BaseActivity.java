package com.blackground.bottomsheetdrawer;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseActivity extends AppCompatActivity {

    // the subclass should provide us with the menu resource that will be used for
    // ... navigation

    BottomSheetBehavior bottomSheetBehavior;
    // onCreate is irrelevant since this is an abstract class

    // We will override setcontentView so it adds the bottom sheets behaviour
    // ... which is required to manage the ups and downs of the navigation
    NavigationView navigationView;

    protected abstract int getMenuRes();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // we call super's setContentView with the base's layout file that we created earlier
        super.setContentView(R.layout.activity_base);

        // we can set the content with the sub-class' supplied layout file @layoutResID

        // first get the container view
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        // inflate the supplied layout  into the container
        // Note: the parameter attachToRoot has been set to true, so that ...
        // the base class can call findViewById() without problems
        LayoutInflater.from(this).inflate(layoutResID, container, true);

        // get our navigation view
        navigationView = (NavigationView) findViewById(R.id.navigation);

        // lets assign it to be the bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(navigationView);

        // we adjust some behaviours for the bottom sheet
        setUpBottomSheet();

        // we handle menu items in the navigation view here
        setUpNavigationView();

        // we handle the toolbar (if any) to display the menu when the hamburger is clicked
        // Note: you can set arbitrary views' onclick to display the navigation
        setUpToolbar();
    }

    private void setUpBottomSheet() {
        // what we're doing here is,
        // if the sheet is expanded the cancel drawable should replace the hamburger
        // else if it is collapsed the hamburger should come to place
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // if there's no toolbar, we are afraid of NullPointerException
                if (getToolbar() == null) return;
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // remember to provide your own ic_cancel in res/drawable
                        getToolbar().setNavigationIcon(R.drawable.ic_cancel);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // remember to provide your own ic_menu_white in res/drawable
                        getToolbar().setNavigationIcon(R.drawable.ic_menu_white);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setUpNavigationView() {
        // we have to give it the menu to display for navigation
        // this menu is obtained from the subclass
        navigationView.inflateMenu(getMenuRes());

        // here we handle normal onItemClicks
        // implement based on your navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // todo: so you won't forget, implement this on your own (no spoon-feeding)
                return true;
            }
        });
    }


    // Note: sub-classes should include a toolbar in their layout
    protected void setUpToolbar() {
        // here we set up the navigation icon, and let is display or dismiss ...
        // ... the bottom sheet appropriately
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first get the state and act accordingly
                int navigationState = bottomSheetBehavior.getState();
                if (navigationState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }

    // we should override this to hide the menu if it is expanded
    // if not, default action should take place.
    @Override
    public void onBackPressed() {
        // just be afraid of NullPointerException, okay?
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
}

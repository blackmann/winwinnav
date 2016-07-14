package com.blackground.bottomsheetdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class MainActivity extends BaseActivity {
    @Override
    protected int getMenuRes() {
        return R.menu.menu_main;
    }

    // just go on and setContentView, the magic happens here
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the BaseActivity.setContentView will be called instead
        setContentView(R.layout.activity_main);
    }
}

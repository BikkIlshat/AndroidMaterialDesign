package com.hfad.androidmaterialdesign.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfad.androidmaterialdesign.R
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayFragment
import com.hfad.androidmaterialdesign.ui.settings.SETTINGS_SHARED_PREFERENCES
import com.hfad.androidmaterialdesign.ui.settings.THEME_RES_ID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(
            getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
                .getInt(THEME_RES_ID, R.style.DefaultTheme)
        )
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNowAllowingStateLoss()
        }
    }
}
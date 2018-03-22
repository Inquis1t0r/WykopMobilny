package io.github.feelfreelinux.wykopmobilny.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import dagger.android.support.DaggerAppCompatActivity
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.app.SwipeBackActivityHelper
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi


// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : DaggerAppCompatActivity() {
    lateinit var swipeBackHandler : SwipeBackActivityHelper
    open val enableSwipeBackLayout : Boolean = false
    open val isActivityTransfluent : Boolean = false
    public var isRunning = false
    fun showErrorDialog(e : Throwable) {
        if (isRunning) {
            showExceptionDialog(e)
        }
    }

    private val themeSettingsPreferences by lazy { SettingsPreferences(this) as SettingsPreferencesApi }

    override fun onResume() {
        isRunning = true
        super.onResume()
    }

    override fun onPause() {
        isRunning = false
        super.onPause()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        if (enableSwipeBackLayout) {
            Slidr.attach(this,
                    SlidrConfig.Builder().edge(true).build())
        }

    }


    private fun initTheme() {
        if (themeSettingsPreferences.useDarkTheme) {
            if (themeSettingsPreferences.useAmoledTheme) {
                setTheme(R.style.WykopAppTheme_Amoled)
            } else {
                setTheme(R.style.WykopAppTheme_Dark)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark_Dark))
            }
        } else {
            setTheme(R.style.WykopAppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }
        if (isActivityTransfluent || enableSwipeBackLayout) {
            theme.applyStyle(R.style.TransparentActivityTheme, true)
            getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
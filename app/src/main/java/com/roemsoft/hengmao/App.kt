package com.roemsoft.hengmao

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.jeremyliao.liveeventbus.LiveEventBus
import com.roemsoft.hengmao.api.HttpConfig
import com.roemsoft.hengmao.preference.Preference
import timber.log.Timber
import kotlin.properties.Delegates

open class App : Application() {
    companion object {
        const val TAG = "HengMao"

        const val KEY_SCAN_LEFT = 622 // KeyEvent.KEYCODE_BUTTON_2
        const val KEY_SCAN_RIGHT = 623 //KeyEvent.KEYCODE_BUTTON_3

        var userCode: String? = null    // 登录工号

        var baseUrl: String by Preference(Preference.URL_KEY, HttpConfig.BASE_URL)

        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        Timber.plant(Timber.DebugTree())

        LiveEventBus.config().lifecycleObserverAlwaysActive(true)

        CrashHandler.init()

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Timber.tag(TAG).d("onCreated: %s", activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Timber.tag(TAG).d("onStart: %s", activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Timber.tag(TAG).d("onDestroy: %s", activity.componentName.className)
        }
    }
}
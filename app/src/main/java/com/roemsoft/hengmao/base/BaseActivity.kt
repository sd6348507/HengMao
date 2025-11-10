package com.roemsoft.hengmao.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.roemsoft.hengmao.App
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.api.HttpConfig
import com.roemsoft.hengmao.compactScreen
import com.roemsoft.hengmao.isNetworkConnected
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mTipView: View
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    private var mNetworkService: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = if (compactScreen())
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER

        val container: ViewGroup = window.decorView as ViewGroup

        // Add a utility view to the container to hook into
        // View.onConfigurationChanged. This is required for all
        // activities, even those that don't handle configuration
        // changes. You can't use Activity.onConfigurationChanged,
        // since there are situations where that won't be called when
        // the configuration changes. View.onConfigurationChanged is
        // called in those scenarios.
        val configurationView: View = object : View(this) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                Timber.tag(App.TAG).i("view on configuration changed")
                requestedOrientation = if (compactScreen())
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else
                    ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            }
        }
        configurationView.visibility = View.GONE
        container.addView(configurationView)

        bindingView()

        initToolbar()

        initTipView()

   //     initColor()

        setupEvent()

        initView()

        initData()

        /*onBackPressed(true) {
            val count = supportFragmentManager.backStackEntryCount
            if (count == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }*/
    }

    override fun onResume() {
        if (mNetworkService == null) {
            mNetworkService = getSystemService(ConnectivityManager::class.java)
        }
        mNetworkService?.registerDefaultNetworkCallback(mNetworkCallback)
        super.onResume()
    }

    override fun onPause() {
        mNetworkService?.unregisterNetworkCallback(mNetworkCallback)
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        if (mTipView.parent != null) {
            mWindowManager.removeView(mTipView)
        }
    }

    abstract fun bindingView()

    abstract fun initView()

    open fun initToolbar() {
        val toolbar = getToolbar()

        toolbar?.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setToolTitle()
    }

    open fun getToolbar(): Toolbar? = null

    open fun setToolTitle() {  }

    open fun setupEvent() {
        /*eventObserve(NetworkEvent::class.java) {
            onNetworkChanged(it.isConnected)
        }*/
    }

    open fun doReconnected() {
     //   fetchInfo()
    }

    open fun initData() {}

    open fun enableNetworkTip(): Boolean = true

    private fun onNetworkChanged(isActNw: Boolean) {
        HttpConfig.hasNetwork = isActNw
        if (isActNw) {
            doReconnected()
            if (mTipView.parent != null) {
                mWindowManager.removeViewImmediate(mTipView)
            }
        } else {
            if (mTipView.parent == null) {
                mWindowManager.addView(mTipView, mLayoutParams)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun initTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip, null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.windowAnimations = R.style.anim_float_view
    }

    private val mNetworkCallback: NetworkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkChanged(true)
            Timber.e("==网络连接成功，通知可以使用的时候调用====onAvailable===")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkChanged(false)

            Timber.e("==当网络已断开连接时调用===onLost===");
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (isNetworkConnected()) {
                Timber.d("onCapabilitiesChanged ---> ====网络可正常上网")
            }

            //表明此网络连接验证成功
            if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Timber.d("===当前在使用Mobile流量上网===")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Timber.d("====当前在使用WiFi上网===")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                    Timber.d("=====当前使用蓝牙上网=====")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Timber.d("=====当前使用以太网上网=====")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    Timber.d("===当前使用VPN上网====")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    Timber.d("===表示此网络使用Wi-Fi感知传输====")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                    Timber.d("=====表示此网络使用LoWPAN传输=====")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                    Timber.d("=====表示此网络使用USB传输=====")
                }
            }
        }
    }
}
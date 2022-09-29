package com.depromeet.housekeeper.ui

import android.content.*
import android.content.Intent.ACTION_SEND
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ActivityHousekeeperBinding
import com.depromeet.housekeeper.service.InternetService
import com.depromeet.housekeeper.util.FILTER_NETWORK_CONNECTED
import com.depromeet.housekeeper.util.IS_INTERNET_CONNECTED
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HouseKeeperActivity : AppCompatActivity() {

    private val viewModel: HouseKeeperViewModel by viewModels()
    lateinit var binding: ActivityHousekeeperBinding
    private val internetServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            LocalBroadcastManager.getInstance(applicationContext)
                .registerReceiver(internetReceiver, IntentFilter(FILTER_NETWORK_CONNECTED)
            )
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    private val internetReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, mIntent: Intent?) {
            if (mIntent != null && mIntent.action == ACTION_SEND) {
                val isInternetConnected = mIntent.getBooleanExtra(IS_INTERNET_CONNECTED, false)
                Timber.d(">>> isInternetConnected = $isInternetConnected")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        super.onCreate(savedInstanceState)
        applicationContext.startService(Intent(this, InternetService::class.java))
//        applicationContext.bindService(
//            Intent(this, InternetService::class.java),
//            internetServiceConnection,
//            Context.BIND_AUTO_CREATE
//        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_housekeeper)
        binding.lifecycleOwner = this
        getDynamicLink()
    }

    private fun getDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Timber.d("deepLink = $deepLink")
                    if (deepLink != null) {
                        navController.navigate(deepLink)
                    }
                }
            }
            .addOnFailureListener(this) { e -> Timber.w("getDynamicLink:onFailure $e") }
    }
}
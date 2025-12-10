package com.example.tagfinderapp.Util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler

class NetworkMonitor(private val context: Context, private val onInternetRestored: (() -> Unit)? = null ) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Internet is available, dismiss the dialog
            Handler(context.mainLooper).post {
                DialogUtils.dismissNoInternetDialog()
                onInternetRestored?.invoke() // Notify activity/fragment
            }
        }

        override fun onLost(network: Network) {
            // Internet is lost, show the dialog
            Handler(context.mainLooper).post {
                DialogUtils.showNoInternetDialog(context)
            }
        }
    }

    fun startMonitoring() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        // Initial check for network state
        if (isOnline(context)) {
            Handler(context.mainLooper).post {
                DialogUtils.dismissNoInternetDialog()
            }
        } else {
            Handler(context.mainLooper).post {
                DialogUtils.showNoInternetDialog(context)
            }
        }
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
    private fun isOnline(context: Context): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}

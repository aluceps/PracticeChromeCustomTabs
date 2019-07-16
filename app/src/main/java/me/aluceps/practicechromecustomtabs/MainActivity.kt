package me.aluceps.practicechromecustomtabs

import android.content.ComponentName
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsCallback
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import me.aluceps.practicechromecustomtabs.databinding.ActivityMainBinding
import org.chromium.customtabsclient.shared.CustomTabsHelper

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val packageNameToBind by lazy {
        CustomTabsHelper.getPackageNameToUse(this)
    }

    private var customTabsClient: CustomTabsClient? = null
    private var customTabsSession: CustomTabsSession? = null
    private var customTabsServiceConnection: CustomTabsServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webview.setOnClickListener {
            launchCustomTabs(Uri.parse("https://www.google.com"))
        }
    }

    override fun onStart() {
        super.onStart()
        bindCustomTabsService()
    }

    override fun onDestroy() {
        unbindCustomTabsService()
        super.onDestroy()
    }

    private fun launchCustomTabs(uri: Uri, referrer: Uri? = null) {
        val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .enableUrlBarHiding()
            .build()
        customTabsIntent.also { tabs ->
            tabs.intent.`package` = packageNameToBind
            referrer?.let { tabs.intent.putExtra(Intent.EXTRA_REFERRER, it) }
            tabs.launchUrl(this, uri)
        }
    }

    private fun bindCustomTabsService() {
        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {
                customTabsClient = client
                customTabsSession = customTabsClient?.newSession(object : CustomTabsCallback() {
                    override fun onRelationshipValidationResult(
                        relation: Int, requestedOrigin: Uri?, result: Boolean, extras: Bundle?
                    ) {
                    }

                    override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {}
                    override fun extraCallback(callbackName: String?, args: Bundle?) {}
                    override fun onPostMessage(message: String?, extras: Bundle?) {}
                    override fun onMessageChannelReady(extras: Bundle?) {}
                })
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                customTabsClient = null
            }
        }
        CustomTabsClient.bindCustomTabsService(this, packageNameToBind, customTabsServiceConnection)
    }

    private fun unbindCustomTabsService() {
        unbindService(customTabsServiceConnection)
        customTabsClient = null
        customTabsSession = null
    }
}

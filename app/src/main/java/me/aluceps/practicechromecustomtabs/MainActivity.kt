package me.aluceps.practicechromecustomtabs

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import me.aluceps.practicechromecustomtabs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webview.setOnClickListener {
            launchCustomTabs(Uri.parse("https://www.google.com"))
        }
    }

    private fun launchCustomTabs(uri: Uri) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .enableUrlBarHiding()
            .build()
        customTabsIntent.launchUrl(this, uri)
    }
}

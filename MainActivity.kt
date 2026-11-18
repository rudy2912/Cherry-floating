package com.pinkbeat.cherry

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.rgb(255, 241, 247))
        }
        val title = TextView(this).apply {
            text = "♡  CHERRY  ♡"
            textSize = 32f
            setTextColor(Color.rgb(205, 91, 147))
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Your tiny PINK//BEAT companion\n\nAllow display-over-other-apps permission, then tap Start Cherry. You can drag Cherry around your screen. Tap Cherry to make her speak; use the notification to dismiss her."
            textSize = 16f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(8, 28, 8, 28)
        }
        val permission = Button(this).apply {
            text = "Allow floating permission"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        }
        val start = Button(this).apply {
            text = "Start Cherry ♡"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                } else {
                    val i = Intent(this@MainActivity, CherryOverlayService::class.java)
                    if (Build.VERSION.SDK_INT >= 26) startForegroundService(i) else startService(i)
                }
            }
        }
        layout.addView(title)
        layout.addView(info)
        layout.addView(permission)
        layout.addView(start)
        setContentView(layout)
    }
}

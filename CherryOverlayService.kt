package com.pinkbeat.cherry

import android.app.*
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Toast
import kotlin.math.min

class CherryOverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var petView: CherryView? = null
    private val channelId = "cherry_floating"

    override fun onCreate() {
        super.onCreate()
        createNotification()
        startForeground(7, notification())
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        petView = CherryView(this).apply {
            contentDescription = "Cherry, your floating kitten. Drag to move and tap to interact."
            setOnTouchListener(object : View.OnTouchListener {
                var downX = 0f
                var downY = 0f
                var startX = 0
                var startY = 0
                var dragged = false
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    val params = v.layoutParams as WindowManager.LayoutParams
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = event.rawX; downY = event.rawY
                            startX = params.x; startY = params.y; dragged = false
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val dx = (event.rawX - downX).toInt()
                            val dy = (event.rawY - downY).toInt()
                            if (kotlin.math.abs(dx) + kotlin.math.abs(dy) > 8) dragged = true
                            params.x = startX + dx
                            params.y = startY + dy
                            windowManager?.updateViewLayout(v, params)
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!dragged) {
                                (v as CherryView).nextMood()
                                Toast.makeText(this@CherryOverlayService, "mew! ♡", Toast.LENGTH_SHORT).show()
                            }
                            return true
                        }
                    }
                    return false
                }
            })
        }
        val size = (96 * resources.displayMetrics.density).toInt()
        val params = WindowManager.LayoutParams(
            size, size,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 30; y = 180
        }
        try { windowManager?.addView(petView, params) } catch (_: Exception) {
            stopSelf()
        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "Cherry floating pet", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
    private fun notification(): Notification {
        val stop = PendingIntent.getService(this, 1, Intent(this, CherryOverlayService::class.java).setAction("STOP"), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, channelId).setContentTitle("Cherry is floating ♡").setContentText("Tap Stop to put Cherry away.").setSmallIcon(android.R.drawable.star_on).addAction(Notification.Action.Builder(null, "Stop Cherry", stop).build()).build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this).setContentTitle("Cherry is floating ♡").setSmallIcon(android.R.drawable.star_on).build()
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") stopSelf()
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        try { petView?.let { windowManager?.removeView(it) } } catch (_: Exception) {}
        petView = null
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
}

private class CherryView(context: android.content.Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mood = 0
    fun nextMood() { mood = (mood + 1) % 3; invalidate() }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val s = min(width, height).toFloat()
        canvas.save()
        canvas.scale(s / 100f, s / 100f)
        // pink headphones
        paint.color = Color.rgb(244, 139, 190); paint.style = Paint.Style.STROKE; paint.strokeWidth = 7f
        canvas.drawArc(RectF(19f, 7f, 81f, 70f), 190f, 160f, false, paint)
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(13f, 35f, 25f, 57f, 5f, 5f, paint)
        canvas.drawRoundRect(75f, 35f, 87f, 57f, 5f, 5f, paint)
        // ears and fluffy head
        paint.color = Color.WHITE
        val path = Path().apply {
            moveTo(18f, 40f); lineTo(20f, 13f); lineTo(42f, 29f)
            lineTo(58f, 29f); lineTo(80f, 13f); lineTo(82f, 42f)
            cubicTo(91f, 72f, 72f, 91f, 50f, 91f)
            cubicTo(25f, 91f, 8f, 69f, 18f, 40f); close()
        }
        canvas.drawPath(path, paint)
        // bow
        paint.color = Color.rgb(255, 133, 185)
        canvas.drawOval(RectF(31f, 15f, 46f, 27f), paint)
        canvas.drawOval(RectF(46f, 15f, 61f, 27f), paint)
        canvas.drawCircle(46f, 21f, 4f, paint)
        // face
        paint.color = Color.rgb(65, 55, 65)
        if (mood == 1) {
            paint.style = Paint.Style.STROKE; paint.strokeWidth = 2.8f
            canvas.drawArc(RectF(30f, 49f, 43f, 59f), 190f, 160f, false, paint)
            canvas.drawArc(RectF(57f, 49f, 70f, 59f), 190f, 160f, false, paint)
            paint.style = Paint.Style.FILL
        } else {
            canvas.drawCircle(37f, 54f, 3f, paint); canvas.drawCircle(63f, 54f, 3f, paint)
        }
        paint.color = Color.rgb(255, 167, 192)
        canvas.drawCircle(27f, 64f, 6f, paint); canvas.drawCircle(73f, 64f, 6f, paint)
        paint.color = Color.rgb(222, 126, 157)
        canvas.drawCircle(50f, 64f, 2.5f, paint)
        paint.style = Paint.Style.STROKE; paint.strokeWidth = 1.8f
        canvas.drawArc(RectF(43f, 64f, 50f, 71f), 0f, 90f, false, paint)
        canvas.drawArc(RectF(50f, 64f, 57f, 71f), 90f, 90f, false, paint)
        paint.style = Paint.Style.FILL
        if (mood == 2) {
            paint.color = Color.rgb(255, 125, 177)
            paint.textSize = 13f
            canvas.drawText("♥", 73f, 23f, paint)
        }
        canvas.restore()
    }
}

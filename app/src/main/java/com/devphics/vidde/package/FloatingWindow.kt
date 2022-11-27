package com.devphics.vidde.`package`

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import com.devphics.vidde.R
import com.devphics.vidde.activities.MainActivity

class FloatingWindow : Service() {

    private var mContext: Context? = null
    private var mWindowManager: WindowManager? = null
    private var mView: View? = null
    var mWindowsParams: WindowManager.LayoutParams? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        allAboutLayout(intent!!)
        moveView()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        if (mView != null) {
            mWindowManager!!.removeView(mView)
        }
        super.onDestroy()
    }
    private fun moveView() {
        val metrics = mContext!!.resources.displayMetrics
        val width = (metrics.widthPixels * 0.2f).toInt()
        val height = (metrics.heightPixels * 0.10f).toInt()
        mWindowsParams = WindowManager.LayoutParams(
            width,  //WindowManager.LayoutParams.WRAP_CONTENT,
            height,  //WindowManager.LayoutParams.WRAP_CONTENT,
            //WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            if (Build.VERSION.SDK_INT <= 25) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  //WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,  // Not displaying keyboard on bg activity's EditText
            //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, //Not work with EditText on keyboard
            PixelFormat.TRANSLUCENT
        )
        mWindowsParams!!.gravity = Gravity.TOP or Gravity.LEFT
        //params.x = 0;
        mWindowsParams!!.y = 100
        mWindowManager!!.addView(mView, mWindowsParams)
        mView!!.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            var startTime = System.currentTimeMillis()
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (System.currentTimeMillis() - startTime <= 300) {
                    return false
                }
                if (isViewInBounds(mView!!, event.rawX.toInt(), event.rawY.toInt())) {
                    // editTextReceiveFocus();
                } else {
                    // editTextDontReceiveFocus();
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = mWindowsParams!!.x
                        initialY = mWindowsParams!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mWindowsParams!!.x = initialX + (event.rawX - initialTouchX).toInt()
                        mWindowsParams!!.y = initialY + (event.rawY - initialTouchY).toInt()
                        mWindowManager!!.updateViewLayout(mView, mWindowsParams)
                    }
                }
                return false
            }
        })
    }

    private fun isViewInBounds(view: View, x: Int, y: Int): Boolean {
        val outRect = Rect()
        val location = IntArray(2)
        view.getDrawingRect(outRect)
        view.getLocationOnScreen(location)
        outRect.offset(location[0], location[1])
        return outRect.contains(x, y)
    }


    private fun allAboutLayout(intent: Intent) {
        val layoutInflater = mContext!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.layout_floating_widget, null)

//
        val btnClose = mView!!.findViewById<View>(R.id.activity_main) as RelativeLayout

        btnClose.setOnClickListener {
            stopSelf()
            val intent1 = Intent(applicationContext, MainActivity::class.java)
            intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            startActivity(intent1)
        }
    }
}
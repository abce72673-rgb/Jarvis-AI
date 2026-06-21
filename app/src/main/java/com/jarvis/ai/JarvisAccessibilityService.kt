package com.jarvis.ai

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class JarvisAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "JarvisAccessibility"
        private const val SCROLL_ACTION = "com.jarvis.ai.PERFORM_SCROLL"
    }

    private lateinit var scrollReceiver: BroadcastReceiver

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Jarvis Accessibility Service Connected")
        
        // Register broadcast receiver for scroll commands
        registerScrollReceiver()
    }

    private fun registerScrollReceiver() {
        scrollReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    SCROLL_ACTION -> {
                        performScrollDown()
                    }
                }
            }
        }
        
        val intentFilter = IntentFilter(SCROLL_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(scrollReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(scrollReceiver, intentFilter)
        }
        
        Log.d(TAG, "Scroll receiver registered")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Log.d(TAG, "Window state changed: ${event.packageName}")
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                Log.d(TAG, "Notification state changed: ${event.text}")
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    /**
     * Performs a scroll down gesture using dispatchGesture
     * This simulates a finger swipe gesture on the screen
     */
    fun performScrollDown() {
        Log.d(TAG, "Performing scroll down gesture")
        
        // Get device dimensions
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        
        // Define scroll parameters
        val startX = screenWidth / 2f
        val startY = screenHeight / 3f
        val endY = (screenHeight * 2) / 3f
        
        // Create a path for the gesture
        val path = Path()
        path.moveTo(startX, startY)
        path.lineTo(startX, endY)
        
        // Create gesture description
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 300))
            .build()
        
        // Dispatch the gesture
        dispatchGesture(gesture) { success ->
            if (success) {
                Log.d(TAG, "Scroll gesture dispatched successfully")
            } else {
                Log.e(TAG, "Failed to dispatch scroll gesture")
            }
        }
    }

    /**
     * Alternative scroll method using swipe from bottom to top
     */
    fun performScrollUp() {
        Log.d(TAG, "Performing scroll up gesture")
        
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        
        val startX = screenWidth / 2f
        val startY = (screenHeight * 2) / 3f
        val endY = screenHeight / 3f
        
        val path = Path()
        path.moveTo(startX, startY)
        path.lineTo(startX, endY)
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 300))
            .build()
        
        dispatchGesture(gesture) { success ->
            if (success) {
                Log.d(TAG, "Scroll up gesture dispatched successfully")
            } else {
                Log.e(TAG, "Failed to dispatch scroll up gesture")
            }
        }
    }

    /**
     * Performs a tap gesture at specified coordinates
     */
    fun performTap(x: Float, y: Float) {
        Log.d(TAG, "Performing tap gesture at ($x, $y)")
        
        val path = Path()
        path.moveTo(x, y)
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 50))
            .build()
        
        dispatchGesture(gesture) { success ->
            if (success) {
                Log.d(TAG, "Tap gesture dispatched successfully")
            } else {
                Log.e(TAG, "Failed to dispatch tap gesture")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(scrollReceiver)
            Log.d(TAG, "Scroll receiver unregistered")
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering receiver: ${e.message}")
        }
        Log.d(TAG, "Jarvis Accessibility Service Destroyed")
    }
}

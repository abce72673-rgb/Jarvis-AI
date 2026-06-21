package com.jarvis.ai

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private var isTtsReady = false
    
    private lateinit var statusTextView: TextView
    private lateinit var btnInitializeTts: Button
    private lateinit var btnSpeak: Button
    private lateinit var btnScrollDown: Button
    private lateinit var btnEnableAccessibility: Button
    private lateinit var btnEnableNotification: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        initializeViews()

        // Initialize TextToSpeech
        initializeTextToSpeech()

        // Set up button click listeners
        setupButtonListeners()

        // Check and display current service status
        updateServiceStatus()
    }

    private fun initializeViews() {
        statusTextView = findViewById(R.id.status_text_view)
        btnInitializeTts = findViewById(R.id.btn_initialize_tts)
        btnSpeak = findViewById(R.id.btn_speak)
        btnScrollDown = findViewById(R.id.btn_scroll_down)
        btnEnableAccessibility = findViewById(R.id.btn_enable_accessibility)
        btnEnableNotification = findViewById(R.id.btn_enable_notification)
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                val result = textToSpeech.setLanguage(Locale("hi", "IN"))
                
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fall back to English if Hindi is not available
                    textToSpeech.setLanguage(Locale.ENGLISH)
                }
                
                updateServiceStatus()
                Toast.makeText(this, "TTS Initialized Successfully", Toast.LENGTH_SHORT).show()
            } else {
                isTtsReady = false
                Toast.makeText(this, "TTS Initialization Failed", Toast.LENGTH_SHORT).show()
                updateServiceStatus()
            }
        }
    }

    private fun setupButtonListeners() {
        btnInitializeTts.setOnClickListener {
            if (!isTtsReady) {
                initializeTextToSpeech()
            } else {
                Toast.makeText(this, "TTS is already initialized", Toast.LENGTH_SHORT).show()
            }
        }

        btnSpeak.setOnClickListener {
            if (isTtsReady) {
                speakText(getString(R.string.jarvis_greeting))
            } else {
                Toast.makeText(this, "Please initialize TTS first", Toast.LENGTH_SHORT).show()
            }
        }

        btnScrollDown.setOnClickListener {
            if (isAccessibilityServiceEnabled()) {
                performScrollDown()
            } else {
                Toast.makeText(this, "Enable Accessibility Service first", Toast.LENGTH_SHORT).show()
                openAccessibilitySettings()
            }
        }

        btnEnableAccessibility.setOnClickListener {
            openAccessibilitySettings()
        }

        btnEnableNotification.setOnClickListener {
            openNotificationListenerSettings()
        }
    }

    private fun speakText(text: String) {
        if (isTtsReady) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            } else {
                @Suppress("DEPRECATION")
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun performScrollDown() {
        // Send broadcast to AccessibilityService to perform scroll
        val intent = Intent("com.jarvis.ai.PERFORM_SCROLL")
        sendBroadcast(intent)
        Toast.makeText(this, "Scroll command sent", Toast.LENGTH_SHORT).show()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASKS)
        
        return enabledServices.any { 
            it.id.contains(packageName) && it.id.contains("JarvisAccessibilityService")
        }
    }

    private fun updateServiceStatus() {
        val accessibilityEnabled = isAccessibilityServiceEnabled()
        val ttsStatus = if (isTtsReady) "✓ Ready" else "✗ Not Ready"
        val accessibilityStatus = if (accessibilityEnabled) "✓ Enabled" else "✗ Disabled"
        
        val statusText = """
            TTS: $ttsStatus
            Accessibility: $accessibilityStatus
        """.trimIndent()
        
        statusTextView.text = statusText
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        Toast.makeText(this, "Please enable 'Jarvis AI' accessibility service", Toast.LENGTH_LONG).show()
    }

    private fun openNotificationListenerSettings() {
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        startActivity(intent)
        Toast.makeText(this, "Please enable 'Jarvis AI' notification listener", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}

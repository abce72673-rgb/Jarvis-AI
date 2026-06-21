package com.jarvis.ai

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class JarvisNotificationService : NotificationListenerService() {

    companion object {
        private const val TAG = "JarvisNotification"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        
        if (sbn == null) return
        
        val packageName = sbn.packageName
        val notification = sbn.notification
        
        Log.d(TAG, "Notification posted from: $packageName")
        
        // Check if notification is from WhatsApp
        if (packageName == WHATSAPP_PACKAGE || packageName == WHATSAPP_BUSINESS_PACKAGE) {
            handleWhatsAppNotification(sbn)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        
        if (sbn == null) return
        
        val packageName = sbn.packageName
        Log.d(TAG, "Notification removed from: $packageName")
    }

    private fun handleWhatsAppNotification(sbn: StatusBarNotification) {
        try {
            val notification = sbn.notification
            
            // Extract notification details
            val notificationText = extractNotificationText(notification)
            
            Log.d(TAG, "WhatsApp Notification: $notificationText")
            
            // Parse WhatsApp notification
            val (sender, message) = parseWhatsAppNotification(notificationText)
            
            if (sender.isNotEmpty() && message.isNotEmpty()) {
                Log.d(TAG, "From: $sender, Message: $message")
                
                // You can broadcast this information to MainActivity or other components
                broadcastWhatsAppMessage(sender, message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling WhatsApp notification: ${e.message}")
        }
    }

    /**
     * Extract notification text from notification object
     */
    private fun extractNotificationText(notification: Notification): String {
        return try {
            val extras = notification.extras
            
            // Try to get title and text
            val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
            val text = extras.getString(Notification.EXTRA_TEXT) ?: ""
            val bigText = extras.getString(Notification.EXTRA_BIG_TEXT) ?: ""
            
            "$title: $text $bigText".trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting notification text: ${e.message}")
            ""
        }
    }

    /**
     * Parse WhatsApp notification to extract sender and message
     */
    private fun parseWhatsAppNotification(notificationText: String): Pair<String, String> {
        return try {
            // WhatsApp notifications typically come in format:
            // "Contact Name: Message text"
            // or for group messages:
            // "Group Name: Contact Name: Message text"
            
            val parts = notificationText.split(":", limit = 2)
            
            if (parts.size >= 2) {
                val sender = parts[0].trim()
                val message = parts[1].trim()
                Pair(sender, message)
            } else {
                Pair("", notificationText)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing WhatsApp notification: ${e.message}")
            Pair("", notificationText)
        }
    }

    /**
     * Broadcast WhatsApp message to MainActivity or other components
     */
    private fun broadcastWhatsAppMessage(sender: String, message: String) {
        try {
            val intent = android.content.Intent("com.jarvis.ai.WHATSAPP_MESSAGE")
            intent.putExtra("sender", sender)
            intent.putExtra("message", message)
            
            sendBroadcast(intent)
            Log.d(TAG, "WhatsApp message broadcast sent")
        } catch (e: Exception) {
            Log.e(TAG, "Error broadcasting WhatsApp message: ${e.message}")
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Jarvis Notification Listener Service Connected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "Jarvis Notification Listener Service Disconnected")
    }
}

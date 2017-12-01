package com.umang.example.fcmclient

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.FCMClientHelper

/**
 * @author Umang Chamaria
 */
class PushReceiver: FCMClientHelper.PushReceivedListener {
  override fun onPushReceived(remoteMessage: RemoteMessage) {
    for ((key, value) in remoteMessage.data){
      Log.v("SampleApplication", "Key: $key, Value: $value")
    }
  }
}
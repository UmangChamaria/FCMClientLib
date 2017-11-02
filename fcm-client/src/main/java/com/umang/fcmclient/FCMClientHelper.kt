package com.umang.fcmclient

import android.app.Application
import android.content.Context
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author Umang Chamaria
 */
class FCMClientHelper internal constructor(private var context: Context) {

  private var tokenReceivedListener: ConcurrentLinkedQueue<TokenReceivedListener> = ConcurrentLinkedQueue()

  fun registerTokenRegistrationListener(tokenReceivedListener: TokenReceivedListener) {
    this.tokenReceivedListener.add(tokenReceivedListener)
  }

  fun onTokenRegistered(token: String) {
    tokenReceivedListener.forEach{
      it.onTokenReceived(token)
    }
  }

  fun initializeFCMClient(application: Application) {
    application.registerActivityLifecycleCallbacks(FCMClientLibActivityLifecycleCallbacks())
  }

  private fun registerForPushIfRequired(){
    val savedToken: String? = SharedPref.newInstance(context).pushToken
    if (savedToken == null){
      forceRegisterForPush()
    }
  }

  fun subscribeToTopics(topics: Array<String>) {
    FCMClientLibWorker.subscribeToTopic(context, topics)
  }

  private fun forceRegisterForPush(){
    FCMClientLibWorker.registerForPush(context)
  }

  internal fun onStart(){
    if (activityCounter == 0){
      forceRegisterForPush()
    }
    registerForPushIfRequired()
    activityCounter++
  }

  internal fun onStop(){
    activityCounter--
    if (activityCounter == 0){
      registerForPushIfRequired()
    }
  }

  internal fun refreshToken(){
    FCMClientLibWorker.registerForPush(context)
  }
  companion object {
    private var activityCounter = 0

    fun newInstance(context: Context): FCMClientHelper {
      return FCMClientHelper(context)
    }
  }

  interface TokenReceivedListener {
    fun onTokenReceived(token: String)
  }
}
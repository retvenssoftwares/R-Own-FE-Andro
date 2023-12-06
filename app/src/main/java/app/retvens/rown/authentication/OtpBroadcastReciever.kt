package app.retvens.rown.authentication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever

import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class OtpBroadcastReciever : BroadcastReceiver() {

    lateinit var smsBroadcastReceiverListener: OtpBroadcastReceiverListener

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                val extras: Bundle? = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status?
                when (smsRetrieverStatus?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val messageIntent: Intent? =
                            extras?.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT)
                        smsBroadcastReceiverListener.onSuccess(messageIntent)
                    }

                    CommonStatusCodes.TIMEOUT -> {
                        smsBroadcastReceiverListener.onFailure()
                    }
                }
            }
        }catch (e:Exception){
            Log.e("error",e.message.toString())
        }
    }

    interface OtpBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }
}
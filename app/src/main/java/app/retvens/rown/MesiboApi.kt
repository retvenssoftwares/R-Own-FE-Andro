package app.retvens.rown

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile


@SuppressLint("StaticFieldLeak")
object MesiboApi {

    private var mContext: Context? = null

    fun init(context: Context) {
        mContext = context
        val api = Mesibo.getInstance()
        api.init(context)

//        Mesibo.setSecureConnection(true);
        if (!TextUtils.isEmpty(SharedPreferenceManagerAdmin.getInstance(mContext!!).user.uid)) {
            startMesibo(false)

        }

    }

    fun startMesibo(b: Boolean): Boolean {


        // add lister
        Mesibo.addListener(this)


        // set access token
        if (0 != Mesibo.setAccessToken(SharedPreferenceManagerAdmin.getInstance(mContext!!).user.uid)) {
            return false
        }


        // set database after setting access token so that it's associated with user
        Mesibo.setDatabase("mesibo.db", 0)


        // Now start mesibo
        val test = Mesibo.start()
        if (0 != test) {
            return false
        }

        return true
    }


}
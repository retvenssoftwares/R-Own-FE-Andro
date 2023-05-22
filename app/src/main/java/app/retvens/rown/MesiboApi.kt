package app.retvens.rown

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboProfile
import com.mesibo.calls.api.MesiboCall

@SuppressLint("StaticFieldLeak")
object MesiboApi : MesiboCall.IncomingListener {

    private var mContext: Context? = null

    fun init(context: Context) {
        mContext = context
        val api = Mesibo.getInstance()
        api.init(context)

        //Mesibo.setSecureConnection(true);
        if (!TextUtils.isEmpty(SharedPreferenceManagerAdmin.getInstance(mContext!!).user.uid)) {
            startMesibo(false)

        }

    }

    fun startMesibo(b: Boolean): Boolean {


        // add lister
        Mesibo.addListener(this)
        MesiboCall.getInstance().setListener(this)



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

    override fun MesiboCall_OnIncoming(p0: MesiboProfile?, p1: Boolean): MesiboCall.CallProperties {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnShowUserInterface(
        p0: MesiboCall.Call?,
        p1: MesiboCall.CallProperties?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_OnError(p0: MesiboCall.CallProperties?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun MesiboCall_onNotify(p0: Int, p1: MesiboProfile?, p2: Boolean): Boolean {
        TODO("Not yet implemented")
    }

}
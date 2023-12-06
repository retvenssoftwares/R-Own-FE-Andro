package app.retvens.rown.authentication;

import android.util.Log;

import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboProfile;

public final class SetStatus {

    public static void setStatusToMesibo(String status,String number){
        MesiboProfile profiles = Mesibo.getProfile(number);
        profiles.setStatus(status);
        profiles.save();
        Log.e("account",profiles.getStatus());
    }

}

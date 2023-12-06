package app.retvens.rown.Dashboard.notificationScreen.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.retvens.rown.R

class CommunityNotificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
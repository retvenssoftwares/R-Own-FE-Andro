package app.retvens.rown.Dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.retvens.rown.ChatSection.UserChatList
import app.retvens.rown.NavigationFragments.HomeFragment
import com.mesibo.messaging.UserListFragment

class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        // Return the total number of fragments
        return 2
    }

    override fun getItem(position: Int): Fragment {
        // Return the fragment for the given position
        return when (position) {
            0 -> HomeFragment()
            1 -> UserChatList()
            else -> HomeFragment()
        }
    }
}
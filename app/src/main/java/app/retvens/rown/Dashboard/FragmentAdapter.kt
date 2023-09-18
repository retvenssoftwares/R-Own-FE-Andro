package app.retvens.rown.Dashboard

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import app.retvens.rown.ChatSection.UserChatList
import app.retvens.rown.NavigationFragments.ExploreFragment
import app.retvens.rown.NavigationFragments.HomeFragment
import app.retvens.rown.NavigationFragments.ProfileFragment
import app.retvens.rown.NavigationFragments.ProfileFragmentForHotelOwner
import app.retvens.rown.NavigationFragments.ProfileFragmentForVendors
import com.mesibo.messaging.MessagingFragment
import com.mesibo.messaging.UserListFragment

class FragmentAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    // Override getItemPosition to enable or disable swiping for specific fragments
    override fun getItemPosition(`object`: Any): Int {
        if (`object` is HomeFragment) {
            return POSITION_NONE // Disable swiping for NonSwipeableFragment
        }
        return super.getItemPosition(`object`)
    }
}
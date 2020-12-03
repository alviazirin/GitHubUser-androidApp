package alviazirin.dicoding.githubuseruiuxapi.ui.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import alviazirin.dicoding.githubuseruiuxapi.R
import alviazirin.dicoding.githubuseruiuxapi.ui.tabs.follower.FollowerFragment
import alviazirin.dicoding.githubuseruiuxapi.ui.tabs.following.FollowingFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_follower,
    R.string.tab_following
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = FollowerFragment().newInstance(username.toString())
            1 -> fragment = FollowingFragment().newInstance(username.toString())
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}
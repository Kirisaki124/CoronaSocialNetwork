package hava.coronasocialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import hava.coronasocialnetwork.fragment.MenuFragment
import hava.coronasocialnetwork.fragment.NewFeedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int) = when (position) {
                0 -> NewFeedFragment()
                3 -> MenuFragment()
                else -> Fragment()
            }

            override fun getCount() = 4
        }
        tabLayout.setupWithViewPager(viewPager)
        with(tabLayout) {
            getTabAt(0)?.setIcon(R.drawable.ic_home)
            getTabAt(1)?.setIcon(R.drawable.ic_chat)
            getTabAt(2)?.setIcon(R.drawable.ic_notification)
            getTabAt(3)?.setIcon(R.drawable.ic_menu)
        }

        setSupportActionBar(toolbar as Toolbar)
        GlobalScope.launch {
        }
    }
}

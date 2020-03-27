package hava.coronasocialnetwork

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import hava.coronasocialnetwork.activity.CreatePostActivity
import hava.coronasocialnetwork.activity.SearchActivity
import hava.coronasocialnetwork.fragment.HistoryChatFragment
import hava.coronasocialnetwork.fragment.MenuFragment
import hava.coronasocialnetwork.fragment.NewFeedFragment
import kotlinx.android.synthetic.main.activity_main.*

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
                1 -> HistoryChatFragment()
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchView: SearchView = menu?.findItem(R.id.btnSearch)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnCreatePost -> {
                val intent = Intent(this, CreatePostActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

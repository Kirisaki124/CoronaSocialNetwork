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
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.activity.CreatePostActivity
import hava.coronasocialnetwork.activity.SearchActivity
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.fragment.HistoryChatFragment
import hava.coronasocialnetwork.fragment.MenuFragment
import hava.coronasocialnetwork.fragment.NewFeedFragment
import hava.coronasocialnetwork.fragment.NotiFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        viewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int) = when (position) {
                0 -> NewFeedFragment()
                1 -> HistoryChatFragment()
                2 -> NotiFragment()
                3 -> MenuFragment()
                else -> Fragment()
            }

            override fun getCount() = 4
        }
        viewPager.offscreenPageLimit = 4
        tabLayout.setupWithViewPager(viewPager)
        with(tabLayout) {
            getTabAt(0)?.setIcon(R.drawable.ic_home)
            getTabAt(1)?.setIcon(R.drawable.ic_chat)
            getTabAt(2)?.setIcon(R.drawable.ic_notification)
            getTabAt(3)?.setIcon(R.drawable.ic_menu)
        }

        setSupportActionBar(toolbar as Toolbar)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNotification()
    }

    private fun setupNotification() {
        DaoNotiManagement.getAllNotiFromUid(DaoContext.authen.currentUser!!.uid)
            .orderByChild("seen").equalTo(false).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val count = p0.children.count()
                    val badge = tabLayout.getTabAt(2)!!.orCreateBadge
                    badge.isVisible = count > 0
                    badge.number = count
                }
            })
        DaoNotiManagement.getChatNotiFromUid(DaoContext.authen.currentUser!!.uid)
            .orderByChild("seen").equalTo(false).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val count = p0.children.count()
                    val badge = tabLayout.getTabAt(1)!!.orCreateBadge
                    badge.isVisible = count > 0
                    badge.number = count
                }
            })
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

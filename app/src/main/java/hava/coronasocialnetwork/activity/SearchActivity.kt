package hava.coronasocialnetwork.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.SearchAdapter
import hava.coronasocialnetwork.database.management.DaoUserManagement
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val query = intent.getStringExtra("query")
        GlobalScope.launch(Dispatchers.Main) {
            val uids: List<String> = DaoUserManagement.searchUserByName(query)
            val adapter = SearchAdapter(uids)
            searchRecylerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            searchRecylerView.adapter = adapter
        }
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Search: " + query
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

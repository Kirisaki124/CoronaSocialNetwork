package hava.coronasocialnetwork.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.PostAdapter
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoChatManagement
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.database.operator.DaoAuthen
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {
    private lateinit var postAdapter: PostAdapter
    private lateinit var uid: String
    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        postAdapter.stopListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        uid = intent!!.getStringExtra("uid")

        GlobalScope.launch(Dispatchers.Main) {
            Glide.with(this@UserProfileActivity).load(DaoUserManagement.getAvatarById(uid))
                .into(avatarImage)
        }

        val postQuery = DaoPostManagement.getUserPostsById(uid)
        val recyclerOptions = FirebaseRecyclerOptions.Builder<Post>().setQuery(postQuery) {
            it.getValue(Post::class.java)!!
        }.build()

        postAdapter = PostAdapter(recyclerOptions)
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.adapter = postAdapter

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        GlobalScope.launch(Dispatchers.Main) {
            userNameText.text = DaoUserManagement.getUserInfo(uid)?.username

            var isShow = true
            var scrollRange = -1
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = userNameText.text
                    collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.colorAccent))
                    avatarImage.visibility = View.INVISIBLE
                    avatarBound.visibility = View.INVISIBLE
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    avatarImage.visibility = View.VISIBLE
                    avatarBound.visibility = View.VISIBLE
                    isShow = false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            val isFriend = DaoUserManagement.isFriend(
                intent!!.getStringExtra("uid"),
                DaoAuthen.getCurrentUser()!!.uid
            )
            if (isFriend) {
                menu!!.findItem(R.id.addFriend).isVisible = false
            }
        }
        menuInflater.inflate(R.menu.add_friend_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.addFriend -> {
                GlobalScope.launch {
                    DaoUserManagement.addFriend(
                        DaoContext.authen.currentUser?.uid!!,
                        intent!!.getStringExtra("uid")
                    )
                }
                true
            }
            R.id.Chat -> {
                GlobalScope.launch {
                    val roomId = DaoChatManagement.getChatRoomWithId(uid)
                    var intent = Intent(this@UserProfileActivity, ChatActivity::class.java)
                    intent.putExtra("RoomID", roomId)
                    intent.putExtra("thierUid", uid)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

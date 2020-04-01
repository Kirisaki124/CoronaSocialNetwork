package hava.coronasocialnetwork.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.PostAdapter
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.*
import hava.coronasocialnetwork.database.operator.DaoAuthen
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

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
        if (uid == DaoAuthenManagement.getCurrentUser()!!.uid) {
            avatarImage.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    )
                    intent.type = "image/*"
                    startActivityForResult(intent, 100)
                }
            }
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
            if (uid == DaoAuthen.getCurrentUser()!!.uid) {
                menu!!.findItem(R.id.addFriend).isVisible = false
                menu.findItem(R.id.Chat).isVisible = false
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
                GlobalScope.launch(Dispatchers.Main) {
                    DaoUserManagement.addFriend(
                        DaoContext.authen.currentUser?.uid!!,
                        intent!!.getStringExtra("uid")
                    )
                    DaoNotiManagement.sendAddFriendNoti(uid)
                    item.isVisible = false
                }
                true
            }
            R.id.Chat -> {
                GlobalScope.launch {
                    val roomId = DaoChatManagement.getChatRoomWithId(uid)
                    var intent = Intent(this@UserProfileActivity, ChatActivity::class.java)
                    intent.putExtra("RoomID", roomId)
                    intent.putExtra("theirUid", uid)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            avatarImage.setImageURI(data?.data)
            val imagePath = Uri.fromFile(File(getRealPathFromURI(data?.data!!)!!))
            DaoUserManagement.setAvatar(imagePath)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor =
            contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}

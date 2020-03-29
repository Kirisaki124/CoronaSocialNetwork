package hava.coronasocialnetwork

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.activity.*
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.fragment.HistoryChatFragment
import hava.coronasocialnetwork.fragment.MenuFragment
import hava.coronasocialnetwork.fragment.NewFeedFragment
import hava.coronasocialnetwork.fragment.NotiFragment
import hava.coronasocialnetwork.model.Noti
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val CHANNEL_ID = 100
    }

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
                2 -> NotiFragment()
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

        val uid = DaoAuthenManagement.getCurrentUser()!!.uid
        DaoNotiManagement.getAllNotiFromUid(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value == null) return

                GlobalScope.launch {
                    var notification: Notification?

                    val child = p0.children.first()

                    val noti = Noti().apply {
                        createdDate =
                            child.child("createdDate").value.toString().toLongOrNull() ?: 0
                        type = child.child("type").value.toString()
                        postId = child.child("postId").value.toString()
                        senderId = child.child("senderId").value.toString()
                    }

                    val sender = DaoUserManagement.getUserInfo(noti.senderId)!!
                    val avatarBitmap =
                        Glide.with(this@MainActivity).asBitmap()
                            .load(DaoUserManagement.getAvatarById(noti.senderId))
                            .submit()
                            .get()

                    val intent =
                        if (noti.type == Noti.LIKE_NOTIFICATION || noti.type == Noti.COMMENT_NOTIFICATION)
                            Intent(applicationContext, ShowCommentActivity::class.java).apply {
                                putExtra("postId", noti.postId)
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        else
                            Intent(applicationContext, UserProfileActivity::class.java).apply {
                                putExtra("uid", noti.senderId)
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }

                    val pendingIntent =
                        PendingIntent.getActivity(applicationContext, 0, intent, 0)

                    when (noti.type) {
                        Noti.LIKE_NOTIFICATION ->
                            notification = createNotification(
                                "New like",
                                "Your post is liked by ${sender.username}",
                                avatarBitmap,
                                pendingIntent
                            )
                        Noti.COMMENT_NOTIFICATION ->
                            notification = createNotification(
                                "New comment",
                                "${sender.username} comments on your post",
                                avatarBitmap,
                                pendingIntent
                            )
                        else ->
                            notification = createNotification(
                                "New friend",
                                "${sender.username} added you as friend",
                                avatarBitmap,
                                pendingIntent
                            )
                    }

                    NotificationManagerCompat.from(applicationContext).notify(1, notification!!)
                }
            }
        })

        DaoNotiManagement.getChatNotiFromUid(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value == null) return

                    GlobalScope.launch {
                        val noti = Noti().apply {
                            chatRoomId = p0.child("chatRoomId").value.toString()
                            type = p0.child("type").value.toString()
                            senderId = p0.child("senderId").value.toString()
                        }

                        val sender = DaoUserManagement.getUserInfo(noti.senderId)!!
                        val avatarBitmap =
                            Glide.with(this@MainActivity).asBitmap()
                                .load(DaoUserManagement.getAvatarById(sender.id))
                                .submit()
                                .get()

                        val intent = Intent(applicationContext, ChatActivity::class.java).apply {
                            putExtra("RoomId", noti.chatRoomId)
                            putExtra("theirUid", noti.senderId)
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                        val pendingIntent =
                            PendingIntent.getActivity(applicationContext, 0, intent, 0)

                        val notification = createNotification(
                            "New message",
                            "${sender.username} sent you a message",
                            avatarBitmap,
                            pendingIntent
                        )

                        NotificationManagerCompat.from(applicationContext).notify(1, notification!!)
                    }
                }
            })
    }

    private fun createNotification(
        title: String,
        desc: String,
        avatarBitmap: Bitmap,
        pendingIntent: PendingIntent
    ): Notification? {
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(CHANNEL_ID.toString(), "Notification", importance).apply {
                    description = "Notification"
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(desc)
            .setSound(defaultSound)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(avatarBitmap)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder.build()
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

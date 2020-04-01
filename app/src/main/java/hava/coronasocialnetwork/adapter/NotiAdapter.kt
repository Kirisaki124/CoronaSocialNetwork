package hava.coronasocialnetwork.adapter

import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.activity.ShowCommentActivity
import hava.coronasocialnetwork.activity.UserProfileActivity
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Noti
import kotlinx.android.synthetic.main.noti_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotiAdapter(firebaseOptions: FirebaseRecyclerOptions<Noti>) :
    FirebaseRecyclerAdapter<Noti, NotiAdapter.NotiViewHolder>(firebaseOptions) {
    class NotiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(noti: Noti) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    if (!noti.seen) {
                        setBackgroundColor(resources.getColor(R.color.colorTextView))
                    }
                    val userName = DaoUserManagement.getUserInfo(noti.senderId)!!.username
                    txtUsernameNoti.text = userName
                    val avatar = DaoUserManagement.getAvatarById(noti.senderId)
                    if (avatar != Uri.EMPTY) {
                        Glide.with(this).load(avatar).into(notiAvatar)
                    }
                    val currentUid = DaoContext.authen.currentUser!!.uid
                    when (noti.type) {
                        Noti.ADD_FRIEND_NOTIFICATION -> {
                            notiContent.text = userName + " added you as friend"
                            setOnClickListener {
                                var intent = Intent(context, UserProfileActivity::class.java)
                                intent.putExtra("uid", noti.senderId)
                                DaoNotiManagement.markPostAsSeen(currentUid, noti.id)
                                context.startActivity(intent)
                            }
                        }
                        Noti.COMMENT_NOTIFICATION -> {
                            notiContent.text = userName + " comment on your post"
                            setOnClickListener {
                                var intent = Intent(context, ShowCommentActivity::class.java)
                                intent.putExtra("postId", noti.postId)
                                DaoNotiManagement.markPostAsSeen(currentUid, noti.id)
                                context.startActivity(intent)
                            }
                        }
                        Noti.LIKE_NOTIFICATION -> {
                            notiContent.text = userName + " like your post"
                            setOnClickListener {
                                var intent = Intent(context, ShowCommentActivity::class.java)
                                intent.putExtra("postId", noti.postId)
                                DaoNotiManagement.markPostAsSeen(currentUid, noti.id)
                                context.startActivity(intent)
                            }

                        }
                    }
                    notiTime.text = DateUtils.getRelativeTimeSpanString(noti.createdDate)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.noti_item_layout, parent, false)
        return NotiAdapter.NotiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int, model: Noti) {
        holder.bind(model)
    }
}

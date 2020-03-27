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
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.activity.UserProfileActivity
import hava.coronasocialnetwork.activity.ShowCommentActivity
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.newfeed_post_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class PostAdapter(firebaseOptions: FirebaseRecyclerOptions<Post>) :
    FirebaseRecyclerAdapter<Post, PostAdapter.PostViewHolder>(firebaseOptions) {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    val user = DaoUserManagement.getUserInfo(post.ownerUid)!!

                    userNameText.text = user.username
                    userNameText.setOnClickListener {
                        val intent = Intent(context, UserProfileActivity::class.java).apply {
                            putExtra("uid", user.id)
                        }
                        context.startActivity(intent)
                    }

                    if (DaoUserManagement.getAvatarById(user.id) != Uri.EMPTY) {
                        Glide.with(this).load(DaoUserManagement.getAvatarById(user.id))
                            .into(avatarImage)
                    }

                    if (DaoPostManagement.getPostImage(post.id) == Uri.EMPTY) {
                        postImage.visibility = View.GONE
                    } else {
                        Glide.with(context.applicationContext)
                            .load(DaoPostManagement.getPostImage(post.id)).into(postImage)
                    }

                    dateText.text = DateUtils.getRelativeTimeSpanString(Date(post.createdDate).time)
                    postCaptionText.text = post.caption

                    val currentUserId = DaoContext.authen.currentUser?.uid!!
                    val isLiked = DaoPostManagement.isPostLiked(currentUserId, post.id)

                    if (isLiked) {
                        loveButton.setTextColor(resources.getColor(R.color.colorPrimary))
                        (loveButton as MaterialButton).setIconTintResource(R.color.colorPrimary)
                    } else {
                        loveButton.setTextColor(resources.getColor(R.color.colorTextView))
                        (loveButton as MaterialButton).setIconTintResource(R.color.colorTextView)
                    }

                    loveButton.setOnClickListener {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (loveButton.currentTextColor == resources.getColor(R.color.colorPrimary)) {
                                DaoPostManagement.unlikePostById(currentUserId, post.id)
                                loveButton.setTextColor(resources.getColor(R.color.colorTextView))
                                (loveButton as MaterialButton).setIconTintResource(R.color.colorTextView)
                            } else {
                                DaoPostManagement.likePostById(currentUserId, post.id)
                                loveButton.setTextColor(resources.getColor(R.color.colorPrimary))
                                (loveButton as MaterialButton).setIconTintResource(R.color.colorPrimary)
                            }
                        }
                    }

                    commentButton.setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                ShowCommentActivity::class.java
                            ).apply {
                                putExtra("postId", post.id)
                            })
                    }

                    DaoPostManagement.getLikeByPostId(post.id, object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            GlobalScope.launch(Dispatchers.Main) {
                                loveButton.text =
                                    "${dataSnapshot.childrenCount} ${if (dataSnapshot.childrenCount > 1) "loves" else "love"}"
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int, post: Post) {
        viewHolder.bind(post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.newfeed_post_layout, parent, false)
        return PostViewHolder(view)
    }
}
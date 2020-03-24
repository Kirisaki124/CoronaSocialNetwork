package hava.coronasocialnetwork.adapter

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

                    if (user.avatar != Uri.EMPTY) {
                        Glide.with(this).load(user.avatar).into(avatarImage)
                    }

                    if (post.imageURI == Uri.EMPTY) {
                        postImage.visibility = View.GONE
                    } else {
                        Glide.with(this).load(post.imageURI).into(postImage)
                    }

                    dateText.text = DateUtils.getRelativeTimeSpanString(Date(post.createdDate).time)
                    postCaptionText.text = post.caption
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
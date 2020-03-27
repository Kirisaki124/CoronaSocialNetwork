package hava.coronasocialnetwork.adapter

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
import hava.coronasocialnetwork.model.Comment
import kotlinx.android.synthetic.main.comment_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommentAdapter(firebaseOptions: FirebaseRecyclerOptions<Comment>) :
    FirebaseRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder>(firebaseOptions) {
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(comment: Comment) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    val user = DaoUserManagement.getUserInfo(comment.uid)!!

                    userNameText.text = user.username

                    Glide.with(context.applicationContext)
                        .load(DaoUserManagement.getAvatarById(user.id)).into(avatarImage)

                    userCommentText.text = comment.comment
                    dateText.text =
                        DateUtils.getRelativeTimeSpanString(comment.createdDate.toLong())
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comment) {
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item_layout, parent, false)
        return CommentViewHolder(view)
    }
}
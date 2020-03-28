package hava.coronasocialnetwork.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.CommentAdapter
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.model.Comment
import hava.coronasocialnetwork.model.Noti
import kotlinx.android.synthetic.main.activity_show_comment.*

class ShowCommentActivity : AppCompatActivity() {
    private lateinit var commentAdapter: CommentAdapter

    override fun onStart() {
        super.onStart()
        commentAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        commentAdapter.stopListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comment)

        val postId = intent!!.getStringExtra("postId")

        val commentQuery = DaoPostManagement.getAllCommentByPostId(postId)
        val recyclerOptions = FirebaseRecyclerOptions.Builder<Comment>().setQuery(commentQuery) {
            it.getValue(Comment::class.java)!!
        }.build()

        commentAdapter = CommentAdapter(recyclerOptions)
        commentAdapter.setHasStableIds(true)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.adapter = commentAdapter

        sendButton.setOnClickListener {
            if (commentEdit.text.isNotBlank()) {
                DaoPostManagement.addCommentPostById(
                    DaoContext.authen.currentUser!!.uid,
                    postId,
                    commentEdit.text.toString()
                )
                DaoNotiManagement.sendPostNoti(
                    Noti.COMMENT_NOTIFICATION,
                    postId,
                    intent.getStringExtra("ownerId")
                )
                commentEdit.text.clear()
            }
        }
    }
}

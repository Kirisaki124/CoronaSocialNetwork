package hava.coronasocialnetwork.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.ChatMessage
import kotlinx.android.synthetic.main.their_message_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MessageAdapter(firebaseOptions: FirebaseRecyclerOptions<ChatMessage>) :
    FirebaseRecyclerAdapter<ChatMessage, MessageAdapter.MessageViewHolder>(firebaseOptions) {
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    var messageBody = findViewById<TextView>(R.id.message_body)
                    messageBody.text = message.message

                    if (message.uid != DaoAuthenManagement.getCurrentUser()?.uid) {
                        name.text = DaoUserManagement.getUserInfo(message.uid)?.username
                        val avatar = DaoUserManagement.getAvatarById(message.uid)
                        if (avatar != Uri.EMPTY) {
                            Glide.with(this).load(avatar).into(findViewById(R.id.avatar))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        if (message.uid == DaoAuthenManagement.getCurrentUser()?.uid) {
            return R.layout.my_message_layout
        } else {
            return R.layout.their_message_layout
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, message: ChatMessage) {
        holder.bind(message)
    }
}
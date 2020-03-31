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
import hava.coronasocialnetwork.activity.ChatActivity
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.ChatRoom
import kotlinx.android.synthetic.main.chat_history_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatRoomAdapter(firebaseOptions: FirebaseRecyclerOptions<ChatRoom>) :
    FirebaseRecyclerAdapter<ChatRoom, ChatRoomAdapter.ChatRoomViewHolder>(firebaseOptions) {
    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(room: ChatRoom) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    if (!room.seen) {
                        setBackgroundColor(resources.getColor(R.color.colorTextView))
                    }

                    var theirUid: String
                    if (room.uid1 == DaoAuthenManagement.getCurrentUser()!!.uid) {
                        theirUid = room.uid2
                    } else {
                        theirUid = room.uid1
                    }
                    if (room.lastMessage != "null" && room.lastMessage != "") {
                        latestChat.text = room.lastMessage
                    } else {
                        latestChat.text = ""
                    }
                    if (room.lastUpdate != "null" && room.lastUpdate != "") {
                        latestChatDate.text =
                            DateUtils.getRelativeTimeSpanString(room.lastUpdate.toLong())
                    } else {
                        latestChatDate.text = ""
                    }


                    txtUsernameHistoryChat.text = DaoUserManagement.getUserInfo(theirUid)!!.username
                    val avatar = DaoUserManagement.getAvatarById(theirUid)
                    if (avatar != Uri.EMPTY) {
                        Glide.with(this).load(avatar).into(historyChatAvatar)
                    }
                    setOnClickListener {
                        var intent = Intent(context, ChatActivity::class.java)
                        intent.putExtra("RoomID", room.chatRoomId)
                        intent.putExtra("theirUid", theirUid)
                        DaoNotiManagement.markChatAsRead(
                            DaoAuthenManagement.getCurrentUser()!!.uid,
                            room.chatRoomId
                        )
                        context.startActivity(intent)
                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_history_item_layout, parent, false)
        return ChatRoomViewHolder(view)
    }


    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int, model: ChatRoom) {
        holder.bind(model)
    }


}
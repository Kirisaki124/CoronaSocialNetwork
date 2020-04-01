package hava.coronasocialnetwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.ChatRoomAdapter
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoChatManagement
import hava.coronasocialnetwork.model.ChatRoom
import kotlinx.android.synthetic.main.history_chat_layout.view.*

class HistoryChatFragment : Fragment() {
    private lateinit var roomAdapter: ChatRoomAdapter
    override fun onStart() {
        super.onStart()
        roomAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        roomAdapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.history_chat_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            val roomQuery =
                DaoChatManagement.getChatRoomByUserId(DaoAuthenManagement.getCurrentUser()!!.uid)
            val recyclerOptions = FirebaseRecyclerOptions.Builder<ChatRoom>().setQuery(roomQuery) {
                val room = ChatRoom()
                room.chatRoomId = it.child("id").value.toString()
                room.uid1 = it.child("uid1").value.toString()
                room.uid2 = it.child("uid2").value.toString()
                room.lastMessage = it.child("lastMessage").value.toString()
                room.lastUpdate = it.child("lastUpdate").value.toString()
                room.seen = it.child("seen").value.toString().toBoolean()
                room
            }.build()

            roomAdapter = ChatRoomAdapter(recyclerOptions)
            chatRoomRecyclerView.layoutManager = LinearLayoutManager(view.context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            chatRoomRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            chatRoomRecyclerView.adapter = roomAdapter
        }
    }
}
package hava.coronasocialnetwork.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.MessageAdapter
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoChatManagement
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.ChatMessage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var messageAdapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val roomId = intent.getStringExtra("RoomID")
        val messageQuery = DaoChatManagement.getMessageFromChatRoom(
            DaoAuthenManagement.getCurrentUser()?.uid!!,
            roomId
        )
        val recyclerOptions =
            FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(messageQuery) {
                it.getValue(ChatMessage::class.java)!!
            }.build()

        messageAdapter = MessageAdapter(recyclerOptions)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter
        send.setOnClickListener {
            if (editText.text.toString() != "") {
                DaoChatManagement.addChatMessage(
                    DaoAuthenManagement.getCurrentUser()!!.uid,
                    roomId,
                    editText.text.toString()
                )
                DaoNotiManagement.sendChatNoti(DaoAuthenManagement.getCurrentUser()!!.uid, roomId)
            }
            editText.text.clear()

        }
        setSupportActionBar(chatToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        GlobalScope.launch(Dispatchers.Main) {
            supportActionBar?.title =
                DaoUserManagement.getUserInfo(intent.getStringExtra("theirUid"))!!.username
        }
    }

    override fun onStart() {
        super.onStart()
        messageAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        messageAdapter.stopListening()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

package hava.coronasocialnetwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.NotiAdapter
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoNotiManagement
import hava.coronasocialnetwork.model.Noti
import kotlinx.android.synthetic.main.notification_layout.view.*

class NotiFragment : Fragment() {
    private lateinit var notiAdapter: NotiAdapter
    override fun onStart() {
        super.onStart()
        notiAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        notiAdapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.notification_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            val notiquery =
                DaoNotiManagement.getAllNotiFromUid(DaoAuthenManagement.getCurrentUser()!!.uid)
            val recyclerOptions = FirebaseRecyclerOptions.Builder<Noti>().setQuery(notiquery) {
                val noti = Noti()
                noti.createdDate = it.child("createdDate").value.toString().toLong()
                noti.type = it.child("type").value.toString()
                noti.postId = it.child("postId").value.toString()
                noti.senderId = it.child("senderId").value.toString()
                noti
            }.build()
            notiAdapter = NotiAdapter(recyclerOptions)
            notiAdapter.setHasStableIds(true)
            notiRecycleView.layoutManager = LinearLayoutManager(view.context)
            notiRecycleView.adapter = notiAdapter
        }
    }

}
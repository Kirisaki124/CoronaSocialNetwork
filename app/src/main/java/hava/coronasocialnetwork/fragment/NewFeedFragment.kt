package hava.coronasocialnetwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.adapter.PostAdapter
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.newfeed_layout.view.*

class NewFeedFragment : Fragment() {
    private lateinit var postAdapter: PostAdapter

    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        postAdapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.newfeed_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            val postQuery = DaoPostManagement.getNewFeed()
            val recyclerOptions = FirebaseRecyclerOptions.Builder<Post>().setQuery(postQuery) {
                it.getValue(Post::class.java)!!
            }.build()

            postAdapter = PostAdapter(recyclerOptions)
            postAdapter.setHasStableIds(true)
            postRecyclerView.layoutManager = LinearLayoutManager(view.context)
            postRecyclerView.adapter = postAdapter
        }
    }
}
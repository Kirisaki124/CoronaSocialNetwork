package hava.coronasocialnetwork.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.activity.UserProfileActivity
import hava.coronasocialnetwork.database.management.DaoUserManagement
import kotlinx.android.synthetic.main.search_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchAdapter(val uids: List<String>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(uid: String) {
            GlobalScope.launch(Dispatchers.Main) {
                with(itemView) {
                    val user = DaoUserManagement.getUserInfo(uid)!!
                    txtUsernameSearch.text = user.username
                    val avatar = DaoUserManagement.getAvatarById(uid)
                    if (avatar != Uri.EMPTY) {
                        Glide.with(this).load(avatar).into(searchAvatar)
                    }
                    setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                UserProfileActivity::class.java
                            ).apply {
                                putExtra("uid", uid)
                            })
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(uids[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return uids.size
    }
}

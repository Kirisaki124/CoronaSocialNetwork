package hava.coronasocialnetwork.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.activity.LoginActivity
import hava.coronasocialnetwork.activity.UserProfileActivity
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.operator.DaoAuthen
import kotlinx.android.synthetic.main.menu_layout.view.*

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.menu_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.signOutButton.setOnClickListener {
            DaoAuthen.signout()
            startActivity(Intent(view.context, LoginActivity::class.java))
            activity?.finish()
        }
        view.userProfileButton.setOnClickListener {
            var intent = Intent(view.context, UserProfileActivity::class.java)
            intent.putExtra("uid", DaoAuthenManagement.getCurrentUser()!!.uid)
            startActivity(intent)
        }
    }
}
package hava.coronasocialnetwork

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.activities.CreatePostActivity
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCreate.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            DaoAuthenManagement.signOut()
            finish()
        }
    }
}

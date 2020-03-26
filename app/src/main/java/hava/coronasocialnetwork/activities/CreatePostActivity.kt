package hava.coronasocialnetwork.activities

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class CreatePostActivity : AppCompatActivity() {
    private var imagePath: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            val user = DaoUserManagement.getUserInfo(DaoAuthenManagement.getCurrentUser()?.uid)
            txtUsername.text = user?.username
        }

        setContentView(R.layout.activity_create_post)
        btnPhoto.setOnClickListener(View.OnClickListener {
            intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, 100)
        })
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cretae_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnPost -> {
                val caption = etCaption.text.toString()
                if (caption.trim() != "") {
                    val uid = DaoAuthenManagement.getCurrentUser()?.uid!!
                    val post = Post(caption, uid, imagePath, Date().toString())
                    DaoPostManagement.addPost(uid, post)
                }
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            imgImage.setImageURI(data?.data)
            imgImage.visibility = View.VISIBLE
            imagePath = data?.data?.path!!
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}


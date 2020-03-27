package hava.coronasocialnetwork.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class CreatePostActivity : AppCompatActivity() {
    private var imagePath: Uri = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            val user = DaoUserManagement.getUserInfo(DaoAuthenManagement.getCurrentUser()?.uid)
            txtUsername.text = user?.username
            val avatar =
                DaoUserManagement.getAvatarById(DaoAuthenManagement.getCurrentUser()?.uid!!)
            if (avatar != Uri.EMPTY) {
                Glide.with(this@CreatePostActivity).load(avatar).into(avatarImage)
            }
        }

        setContentView(R.layout.activity_create_post)
        btnPhoto.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        })
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.create_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnPost -> {
                val caption = etCaption.text.toString()
                if (caption.trim() != "" || imagePath.toString().trim() != "") {
                    val uid = DaoAuthenManagement.getCurrentUser()?.uid!!
                    val post = Post(caption, uid, Date().toString())
                    GlobalScope.launch(Dispatchers.Main) {
                        DaoPostManagement.addPost(post, imagePath)
                    }
                    finish()
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
            imagePath = Uri.fromFile(File(getRealPathFromURI(data?.data!!)!!))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor =
            contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}


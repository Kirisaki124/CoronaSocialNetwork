package hava.coronasocialnetwork

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.model.Post
import java.io.File
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    var filePath: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        GlobalScope.launch {
//            DaoAuthenManagement.login("KhanhChan@fpt.edu.vn", "123@123a")
//            val user = DaoUserManagement.getUserInfo(DaoAuthenManagement.getCurrentUser()?.uid)
//            Log.i("USER", user?.username)
//        }

//        GlobalScope.async(Dispatchers.IO) {
//            //            DaoAuthenManagement.register(
////                "KhanhChan@fpt.edu.vn",
////                "123@123a",
////                "0123123",
////                "Princess of Thanh Hoa",
////                "Tieu vuong quoc"
////            )
//
//            Log.i("uid", DaoAuthenManagement.getCurrentUser()?.uid)
//        }


        var post = Post("test", "hzRnaNIxqnbc6gSENvyWAZQXi472", File("/storage/emulated/0/DCIM/Camera/IMG_20200322_143548.jpg"), LocalDateTime.now())
        DaoPostManagement.addPost("hzRnaNIxqnbc6gSENvyWAZQXi472", post)

    }


}

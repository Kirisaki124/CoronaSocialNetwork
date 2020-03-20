package hava.coronasocialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.database.management.DaoUserManagement

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        DaoAuthen.register("KhanhChanCute@fpt.edu.vn", "123@123a", "0123123", "tung", "wuhan")
//        var userId: String? =
//            DaoManagement.login("KhanhChanCute@fpt.edu.vn", "123@123a")
//        Log.i("UserId", userId)
        DaoUserManagement.changeName("-M2rL0qKErGM_lu32Mru", "Crush chan")
    }

}

package hava.coronasocialnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.async(Dispatchers.IO) {
            //            DaoAuthenManagement.register(
//                "KhanhChan@fpt.edu.vn",
//                "123@123a",
//                "0123123",
//                "Princess of Thanh Hoa",
//                "Tieu vuong quoc"
//            )

            DaoAuthenManagement.login("KhanhChan@fpt.edu.vn", "123@123a")
            Log.i("uid", DaoAuthenManagement.getCurrentUser()?.uid)
        }
    }
}

package hava.coronasocialnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.operator.OnAuthen
import hava.coronasocialnetwork.database.operator.Status

class MainActivity : AppCompatActivity(), OnAuthen {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        DaoAuthenManagement.register(this, "KhanhChanSoCute@fpt.edu.vn", "123@123a", "0123123", "Princess of Thanh Hoa", "Tieu vuong quoc")

//        var userId: String? =
        DaoAuthenManagement.login(this, "KhanhChanCute@fpt.edu.vn", "123@123a")
//        Log.i("UserId", DaoAuthenManagement.getCurrentUser()?.uid)
//        DaoAuthenManagement.signOut()
//        DaoUserManagement.changeName("-M2rL0qKErGM_lu32Mru", "Crush chan")
//        DaoAuthen.register(this, "", "", "", "", "")
    }

    // state: login / register
    override fun onSuccess(status: Status) {
        Log.i("Status", status.toString())
    }

    override fun onFailed(message: String?) {
        Log.i("Error", message)
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

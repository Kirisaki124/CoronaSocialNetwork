package hava.coronasocialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.database.Dao
import hava.coronasocialnetwork.database.DaoManagement

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Dao.setContext(this)
//        DaoManagement().register("tu1ngd123sdfsdfqwedshsdw4130134@fpt.edu.vn", "123@123a", "0123123", "tung", "wuhan")
        var userId: String? =
            DaoManagement().login("tu1ngd123sdfsdfqwedshsdw4130134@fpt.edu.vn", "123@123a")

    }

}

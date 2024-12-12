package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.input.ImeAction
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager
import org.w3c.dom.Text

class Menu : AppCompatActivity() {
    private lateinit var auth : AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.ltb.orderfoodapp.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnfoodlist = findViewById<TextView>(R.id.myfoodlist)
        val logout = findViewById<TextView>(R.id.logout)
        btnfoodlist.setOnClickListener{
            val myfoodlist = Intent(this, MyFood::class.java)
            startActivity(myfoodlist)
        }
        logout.setOnClickListener{
            auth = AuthManager(this)
            auth.logout()
        }
        val statistics = findViewById<TextView>(R.id.statistics)
        statistics.setOnClickListener{
            val thongke = Intent(this, StatisticsFragment::class.java)
            startActivity(thongke)
        }
        val personalInforBtn = findViewById<TextView>(R.id.personalInfo)
        personalInforBtn.setOnClickListener{
            val personalInfor = Intent(this,PersonalInformation::class.java)
            startActivity(personalInfor)
        }
        val myCateList = findViewById<TextView>(R.id.myCateList)
        myCateList.setOnClickListener{
            val addCategory = Intent(this,AddNewCategory::class.java)
            startActivity(addCategory)
        }
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            val sellerDashboard = Intent(this,SellerDashboardHome::class.java)
            startActivity(sellerDashboard)
        }

    }
}
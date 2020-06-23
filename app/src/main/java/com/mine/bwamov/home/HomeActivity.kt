package com.mine.bwamov.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mine.bwamov.R
import com.mine.bwamov.home.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentTicket = TicketFragment()
        val fragmentSetting = SettingFragment()

        val fragmentHome = DashboardFragment()
        setFragment(fragmentHome)
        action_home.setOnClickListener {
            setFragment(fragmentHome)

            changeIcon(action_home, R.drawable.ic_home_active)
            changeIcon(action_ticket, R.drawable.ic_tiket)
            changeIcon(action_setting, R.drawable.ic_profile)
        }

        action_ticket.setOnClickListener {
            setFragment(fragmentTicket)

            changeIcon(action_home, R.drawable.ic_home)
            changeIcon(action_ticket, R.drawable.ic_tiket_active)
            changeIcon(action_setting, R.drawable.ic_profile)
        }
        action_setting.setOnClickListener {
            setFragment(fragmentSetting)

            changeIcon(action_home, R.drawable.ic_home)
            changeIcon(action_ticket, R.drawable.ic_tiket)
            changeIcon(action_setting, R.drawable.ic_profile_active)
        }

    }

    protected fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    protected fun changeIcon(imageView: ImageView, int: Int){
        imageView.setImageResource(int)
    }
}
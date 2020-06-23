package com.mine.bwamov.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mine.bwamov.R
import com.mine.bwamov.home.model.Film
import kotlinx.android.synthetic.main.activity_choose_seat.*

class ChooseSeatActivity : AppCompatActivity() {

    var statusA3:Boolean = false
    var statusA4:Boolean = false

    var total:Int = 0
    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)

        val data = intent.getParcelableExtra<Film>("data")

        tv_judul.text = data.judul

        imgSeat3.setOnClickListener {
            if (statusA3){
                imgSeat3.setImageResource(R.drawable.ic_rectangle_empty)
                statusA3 = false
                total -=1
                belitiket(total)
            } else{
                imgSeat3.setImageResource(R.drawable.ic_rectangle_choose)
                statusA3 = true
                total += 1
                belitiket(total)

                val data = Checkout("A3", "70000")
                dataList.add(data)
            }
        }

        imgSeat4.setOnClickListener {
            if (statusA4){
                imgSeat4.setImageResource(R.drawable.ic_rectangle_empty)
                statusA4 = false
                total -=1
                belitiket(total)
            } else{
                imgSeat4.setImageResource(R.drawable.ic_rectangle_choose)
                statusA4 = true
                total += 1
                belitiket(total)

                val data = Checkout("A4", "70000")
                dataList.add(data)
            }
        }

        actionPurchase.setOnClickListener {

            val intent = Intent(this@ChooseSeatActivity,
                CheckoutActivity::class.java
            ).putExtra("data", dataList)
            startActivity(intent)
        }

    }

    private fun belitiket(total: Int) {
        if (total == 0){
            actionPurchase.setText("Beli Tiket")
            actionPurchase.visibility = View.INVISIBLE
        } else {
            actionPurchase.setText("Beli Tiket (" + total + ")")
            actionPurchase.visibility = View.VISIBLE
        }
    }
}
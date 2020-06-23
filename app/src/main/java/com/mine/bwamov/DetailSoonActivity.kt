package com.mine.bwamov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.mine.bwamov.home.adapter.PlaysAdapter
import com.mine.bwamov.home.model.Plays
import com.mine.bwamov.home.model.Soon
import kotlinx.android.synthetic.main.activity_detail_soon.*

class DetailSoonActivity : AppCompatActivity() {

    lateinit var cDatabase: DatabaseReference
    private var soonList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_soon)

        val data = intent.getParcelableExtra<Soon>("data")

        cDatabase = FirebaseDatabase.getInstance().getReference("Soon")
            .child(data.judul.toString())
            .child("play")

        tvJudul.text = data.judul
        tvGenre.text = data.genre
        tvStoryDesc.text = data.desc
        tvRate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster)

        actionBack.setOnClickListener {
            finish()
        }

        action_choose_seat.visibility = View.INVISIBLE

        rv_plays.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()
    }

    private fun getData() {
        cDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailSoonActivity, ""+error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                soonList.clear()

                for (getdataSnapshot in dataSnapshot.getChildren()){

                    val film = getdataSnapshot.getValue(Plays::class.java)
                    soonList.add(film!!)
                }

                rv_plays.adapter =
                    PlaysAdapter(soonList) {

                    }
            }

        });
    }
}
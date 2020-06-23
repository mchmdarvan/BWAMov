package com.mine.bwamov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.mine.bwamov.home.adapter.PlaysAdapter
import com.mine.bwamov.checkout.ChooseSeatActivity
import com.mine.bwamov.home.model.Film
import com.mine.bwamov.home.model.Plays
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    private var filmList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val data = intent.getParcelableExtra<Film>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
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

        action_choose_seat.setOnClickListener {
            val intent = Intent(this@DetailMovieActivity,
                ChooseSeatActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }

        rv_plays.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailMovieActivity, ""+error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                filmList.clear()

                for (getdataSnapshot in dataSnapshot.getChildren()){

                    val film = getdataSnapshot.getValue(Plays::class.java)
                    filmList.add(film!!)
                }

                rv_plays.adapter =
                    PlaysAdapter(filmList) {

                    }
            }

        });
    }
}
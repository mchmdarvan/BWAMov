package com.mine.bwamov.home.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.mine.bwamov.DetailMovieActivity
import com.mine.bwamov.DetailSoonActivity
import com.mine.bwamov.R
import com.mine.bwamov.home.dashboard.adapter.ComingSoonAdapter
import com.mine.bwamov.home.dashboard.adapter.NowPlayingAdapter
import com.mine.bwamov.home.model.Film
import com.mine.bwamov.home.model.Soon
import com.mine.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences
    lateinit var mDatabase: DatabaseReference
    lateinit var cDatabase: DatabaseReference
    private var soonList = ArrayList<Soon>()
    private var dataList = ArrayList<Film>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
        cDatabase = FirebaseDatabase.getInstance().getReference("Soon")

        tvUsername.setText(preferences.getValues("nama"))
        if (!preferences.getValues("saldo").equals("")){
            currecy(preferences.getValues("saldo")!!.toDouble(), tvMoney)
        }

        if (preferences.getValues("url") != null){
            Glide.with(this)
                .load(preferences.getValues("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(imgPhoto)
        }


        rv_nowPlaying.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_comingsoon.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getFilmData()
        getSoonData()
    }

    private fun getSoonData() {
        cDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                soonList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()){

                    val soon = getdataSnapshot.getValue(Soon::class.java)
                    soonList.add(soon!!)
                }

                rv_comingsoon.adapter =
                    ComingSoonAdapter(
                        soonList
                    ) {
                        val intent = Intent(
                            context,
                            DetailSoonActivity::class.java
                        ).putExtra("data", it)
                        startActivity(intent)
                    }
            }


        })
    }

    private fun getFilmData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                rv_nowPlaying.adapter =
                    NowPlayingAdapter(
                        dataList
                    ) {
                        val intent = Intent(
                            context,
                            DetailMovieActivity::class.java
                        ).putExtra("data", it)
                        startActivity(intent)
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun currecy(harga:Double, textView: TextView){
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga as Double))
    }

}
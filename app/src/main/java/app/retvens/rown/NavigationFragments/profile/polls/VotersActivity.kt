package app.retvens.rown.NavigationFragments.profile.polls

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R

class VotersActivity : AppCompatActivity() {

    private lateinit var vote1RecyclerView: RecyclerView
    private lateinit var vote2RecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voters)


        vote1RecyclerView = findViewById(R.id.recyclerVote1)
        vote2RecyclerView = findViewById(R.id.vote2Recycler)




    }
}
package app.retvens.rown.NavigationFragments.profile.polls

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class VotesActivity : AppCompatActivity() {

    private lateinit var recycler1:RecyclerView
    private lateinit var recycler2:RecyclerView
    private lateinit var recycler3:RecyclerView
    private lateinit var recycler4:RecyclerView
    private lateinit var recycler5:RecyclerView

    private var vote1:ArrayList<Vote> = ArrayList()
    private var vote2:ArrayList<Vote> = ArrayList()
    private var vote3:ArrayList<Vote> = ArrayList()
    private var vote4:ArrayList<Vote> = ArrayList()
    private var vote5:ArrayList<Vote> = ArrayList()

    private lateinit var voteTitle1:TextView
    private lateinit var voteTitle2:TextView
    private lateinit var voteTitle3:TextView
    private lateinit var voteTitle4:TextView
    private lateinit var voteTitle5:TextView

    private lateinit var relative1:RelativeLayout
    private lateinit var relative2:RelativeLayout
    private lateinit var relative3:RelativeLayout
    private lateinit var relative4:RelativeLayout
    private lateinit var relative5:RelativeLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votes)

        findViewById<ImageView>(R.id.notifications_backBtn).setOnClickListener {
            onBackPressed()
        }

        relative1 =  findViewById(R.id.relative1)
        relative2 =  findViewById(R.id.relative2)
        relative3 =  findViewById(R.id.relative3)
        relative4 =  findViewById(R.id.relative4)
        relative5 =  findViewById(R.id.relative5)

        voteTitle1 = findViewById(R.id.votetitle1)
        voteTitle2 = findViewById(R.id.votetitle2)
        voteTitle3 = findViewById(R.id.votetitle3)
        voteTitle4 = findViewById(R.id.votetitle4)
        voteTitle5 = findViewById(R.id.votetitle5)

        recycler1 = findViewById(R.id.recycler_vote_1)
        recycler2 = findViewById(R.id.recycler_vote_2)
        recycler3 = findViewById(R.id.recycler_vote_3)
        recycler4 = findViewById(R.id.recycler_vote_4)
        recycler5 = findViewById(R.id.recycler_vote_5)


        recycler1.layoutManager = LinearLayoutManager(this)
        //recycler1. //recyclerView.setHasFixedSize(true)
        recycler2.layoutManager = LinearLayoutManager(this)
        //recycler2. //recyclerView.setHasFixedSize(true)
        recycler3.layoutManager = LinearLayoutManager(this)
        //recycler3. //recyclerView.setHasFixedSize(true)
        recycler4.layoutManager = LinearLayoutManager(this)
        //recycler4. //recyclerView.setHasFixedSize(true)
        recycler5.layoutManager = LinearLayoutManager(this)
        //recycler5. //recyclerView.setHasFixedSize(true)

        val postId = intent.getStringExtra("postId")

        getVotes(postId)

    }

    private fun getVotes(postId: String?) {

        val getVotes = RetrofitBuilder.feedsApi.getVotes(postId!!)

        getVotes.enqueue(object : Callback<List<VotesDataClass>?> {
            override fun onResponse(
                call: Call<List<VotesDataClass>?>,
                response: Response<List<VotesDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach {
                        try {
                            vote1.addAll(it.Options.get(0).votes)
                            voteTitle1.text = "${it.Options.get(0).Option} Votes"
                            val adapter1 = VotesAdapter(vote1,applicationContext)
                            recycler1.adapter = adapter1
                            adapter1.notifyDataSetChanged()
                        }catch (e:NullPointerException){
                            relative1.visibility = View.GONE
                        }

                        try {
                            vote2.addAll(it.Options.get(1).votes)
                            voteTitle2.text = "${it.Options.get(1).Option} Votes"
                            val adapter2 = VotesAdapter(vote2,applicationContext)
                            recycler2.adapter = adapter2
                            adapter2.notifyDataSetChanged()
                        }catch (e:NullPointerException){
                            relative2.visibility = View.GONE
                        }

                        try {
                            vote3.addAll(it.Options.get(2).votes)
                            voteTitle3.text = "${it.Options.get(2).Option} Votes"
                            val adapter3 = VotesAdapter(vote3,applicationContext)
                            recycler3.adapter = adapter3
                            adapter3.notifyDataSetChanged()
                        }catch (e:IndexOutOfBoundsException){
                            relative3.visibility = View.GONE
                        }

                        try {
                            vote4.addAll(it.Options.get(3).votes)
                            voteTitle4.text = "${it.Options.get(3).Option} Votes"
                            val adapter4 = VotesAdapter(vote4,applicationContext)
                            recycler4.adapter = adapter4
                            adapter4.notifyDataSetChanged()
                        }catch (e:IndexOutOfBoundsException){
                            relative4.visibility = View.GONE
                        }

                        try {
                            vote5.addAll(it.Options.get(4).votes)
                            voteTitle5.text = "${it.Options.get(4).Option} Votes"
                            val adapter5 = VotesAdapter(vote5,applicationContext)
                            recycler5.adapter = adapter5
                            adapter5.notifyDataSetChanged()
                        }catch (e:IndexOutOfBoundsException){
                            relative5.visibility = View.GONE
                        }

                    }
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<VotesDataClass>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }
}
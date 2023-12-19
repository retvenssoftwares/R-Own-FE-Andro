package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.GetRequestedJobDara
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllMatches : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var matchesJobAdapter: MatchesJobAdapter
    private lateinit var empty: TextView
    private lateinit var back_ic: ImageView
    private lateinit var jobs_search_matches: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_matches)

        recyclerView = findViewById(R.id.popular_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
         //recyclerView. //recyclerView.setHasFixedSize(true)

        empty = findViewById(R.id.empty)
        back_ic = findViewById(R.id.back_ic)
        jobs_search_matches = findViewById(R.id.jobs_search_matches)

        back_ic.setOnClickListener{
            onBackPressed()
        }

        getData()

    }

    private fun getData() {
        val getData = RetrofitBuilder.jobsApis.getRequestJob()

        getData.enqueue(object : Callback<List<GetRequestedJobDara>?> {
            override fun onResponse(
                call: Call<List<GetRequestedJobDara>?>,
                response: Response<List<GetRequestedJobDara>?>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.isNotEmpty()) {

                        matchesJobAdapter = MatchesJobAdapter(applicationContext,response.body()!!)
                        recyclerView.adapter = matchesJobAdapter
                        matchesJobAdapter.notifyDataSetChanged()

                        jobs_search_matches.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                val filterData = response.body()!!.filter { item ->
                                    item.jobTitle.contains(p0.toString(),ignoreCase = true)
                                }

                                matchesJobAdapter.updateData(filterData)

                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }

                        })

                    } else {
                        empty.visibility = View.VISIBLE
                    }
                } else {
                    empty.visibility = View.VISIBLE
                    empty.text = response.code().toString()
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<GetRequestedJobDara>?>, t: Throwable) {
                empty.text = "Try Again"
                empty.visibility = View.VISIBLE
            }
        })
    }
}
package app.retvens.rown.NavigationFragments.exploreForUsers.people

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePeopleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var explorePeopleAdapter: ExplorePeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_people, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.explore_peoples_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        getAllProfiles()

    }

    private fun getAllProfiles() {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getProfiles = RetrofitBuilder.exploreApis.getPeople(user_id,"1")

        getProfiles.enqueue(object : Callback<List<ExplorePeopleDataClass>?>,
            ExplorePeopleAdapter.ConnectClickListener {
            override fun onResponse(
                call: Call<List<ExplorePeopleDataClass>?>,
                response: Response<List<ExplorePeopleDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    response.forEach { explorePeopleDataClass ->
                        explorePeopleAdapter = ExplorePeopleAdapter(requireContext(),explorePeopleDataClass.posts)
                        recyclerView.adapter = explorePeopleAdapter
                        explorePeopleAdapter.notifyDataSetChanged()
                    }



                    explorePeopleAdapter.setJobSavedClickListener(this)


                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ExplorePeopleDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onJobSavedClick(connect: Post) {
                sendConnectionRequest(connect.User_id)
            }
        })

    }

    private fun sendConnectionRequest(userId: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val sendRequest = RetrofitBuilder.connectionApi.sendRequest(userId, ConnectionDataClass(user_id))

        sendRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }


}
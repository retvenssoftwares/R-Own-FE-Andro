package app.retvens.rown.NavigationFragments.profile.setting.discoverPeople

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleDataClass
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.UsersProfileAdapter
import app.retvens.rown.bottomsheet.bottomSheetPeople.BottomSheetAdapterPeople
import app.retvens.rown.bottomsheet.bottomSheetPeople.BottomSheetPeopleData
import app.retvens.rown.bottomsheet.bottomSheetPeople.MatchedContact
import app.retvens.rown.databinding.ActivityDiscoverPeopleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverPeopleActivity : AppCompatActivity() {
    lateinit var binding : ActivityDiscoverPeopleBinding

    private lateinit var discoverAdapter: DiscoverAdapter
    private lateinit var discoverAllAdapter: DiscoverAllAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBackBtn.setOnClickListener { onBackPressed() }
        binding.discoverRecycler.layoutManager = LinearLayoutManager(this)
        binding.discoverRecycler.setHasFixedSize(true)
        getContacts()
        binding.discoverRecycler2.layoutManager = LinearLayoutManager(this)
        binding.discoverRecycler2.setHasFixedSize(true)
        getAllProfiles()
    }
    fun getContacts(){
        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val pro = RetrofitBuilder.retrofitBuilder.getContactsProfile(user_id)
        pro.enqueue(object : Callback<BottomSheetPeopleData?>,
            UsersProfileAdapter.ConnectClickListener {
            override fun onResponse(
                call: Call<BottomSheetPeopleData?>,
                response: Response<BottomSheetPeopleData?>
            ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!

                        if (data.message == "Matches found") {

                            try {
                                val original = data.matchedContacts.toList()
                                discoverAdapter = DiscoverAdapter(data.matchedContacts as ArrayList<MatchedContact>, this@DiscoverPeopleActivity)
                                binding.discoverRecycler.adapter = discoverAdapter
                                discoverAdapter.removeUser(data.matchedContacts)
                                discoverAdapter.removeUsersFromList(data.matchedContacts)
                                discoverAdapter.notifyDataSetChanged()

                                binding.searchBar.addTextChangedListener(object : TextWatcher{
                                    override fun beforeTextChanged(
                                        p0: CharSequence?,
                                        p1: Int,
                                        p2: Int,
                                        p3: Int
                                    ) {

                                    }

                                    override fun onTextChanged(
                                        p0: CharSequence?,
                                        p1: Int,
                                        p2: Int,
                                        p3: Int
                                    ) {

                                    }

                                    override fun afterTextChanged(p0: Editable?) {
                                        val filterData = original.filter { item ->
                                            item.matchedNumber.Full_name.contains(p0.toString(),ignoreCase = true)
                                        }

                                        discoverAdapter.updateData(filterData as ArrayList<MatchedContact>)
                                    }
                                })

                            }catch (e:NullPointerException){
//                                getAllProfiles()
                                binding.empty.visibility = View.VISIBLE
                                Log.e("error",e.message.toString())
                            }

//                            usersProfileAdapter.setJobSavedClickListener(this)
//                            usersProfileAdapter.cancelConnRequest(this)

                        } else {
//                            empty.text = "You did'nt post yet"
                            binding.empty.visibility = View.VISIBLE
//                            getAllProfiles()
                        }
                    } else {
                        binding.empty.visibility = View.VISIBLE
                        binding.empty.text = response.code().toString()
                    }
            }

            override fun onFailure(call: Call<BottomSheetPeopleData?>, t: Throwable) {
                binding.empty.visibility = View.VISIBLE
            }

            override fun onJobSavedClick(connect: MatchedContact) {
                sendConnectionRequest(connect.matchedNumber.User_id)
            }
            override fun onCancelRequest(connect: MatchedContact) {
                removeConnRequest(connect.matchedNumber.User_id)
            }
        })
    }
    private fun getAllProfiles() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getProfiles = RetrofitBuilder.exploreApis.getPeople(user_id,"1")

        getProfiles.enqueue(object : Callback<List<ExplorePeopleDataClass>?>,
            BottomSheetAdapterPeople.ConnectClickListener {
            override fun onResponse(
                call: Call<List<ExplorePeopleDataClass>?>,
                response: Response<List<ExplorePeopleDataClass>?>
            ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
                            val response = response.body()!!
                            var original:List<Post> = emptyList()
                            response.forEach {
                                original = it.posts.toList()
                            }
                            response.forEach { explorePeopleDataClass ->
                                try {
                                    discoverAllAdapter = DiscoverAllAdapter(explorePeopleDataClass.posts as ArrayList<Post>, this@DiscoverPeopleActivity )
                                    binding.discoverRecycler2.adapter = discoverAllAdapter
                                    discoverAllAdapter.removeUser(explorePeopleDataClass.posts)
                                    discoverAllAdapter.removeUsersFromList(explorePeopleDataClass.posts)
                                    discoverAllAdapter.notifyDataSetChanged()

                                    binding.searchBar.addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {

                                        }

                                        override fun onTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {
                                            val filterData = original.filter { item ->
                                                item.Full_name.contains(p0.toString(),ignoreCase = true)
                                            }

                                            discoverAllAdapter.updateData(filterData as ArrayList<Post>)
                                        }

                                        override fun afterTextChanged(p0: Editable?) {

                                        }
                                    })

                                }catch (e:NullPointerException){
//                                getAllProfiles()
                                    Log.e("error",e.message.toString())
                                }
                            }

//                            explorePeopleAdapter.setJobSavedClickListener(this)
//                            explorePeopleAdapter.cancelConnRequest(this)


                        } else {
//                            empty.text = "You did'nt post yet"
                        }
                    } else {
//                        empty.text = response.code().toString()
                    }
            }

            override fun onFailure(call: Call<List<ExplorePeopleDataClass>?>, t: Throwable) {

            }


            override fun onJobSavedClick(connect: Post) {
                sendConnectionRequest(connect.User_id)
            }
            override fun onCancelRequest(connect: Post) {
                removeConnRequest(connect.User_id)
            }
        })
    }

    private fun removeConnRequest(userId: String) {
        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val removeRequest = RetrofitBuilder.connectionApi.removeRequest(userId,
            ConnectionDataClass(user_id)
        )

        removeRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
//                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
            }
        })

    }
    private fun sendConnectionRequest(userId: String) {

        val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val sendRequest = RetrofitBuilder.connectionApi.sendRequest(userId, ConnectionDataClass(user_id))

        sendRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
//                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

}
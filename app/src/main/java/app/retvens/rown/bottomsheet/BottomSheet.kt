package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.PopularUsersAdapter
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleDataClass
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.bottomSheetPeople.BottomSheetAdapterPeople
import app.retvens.rown.bottomsheet.bottomSheetPeople.BottomSheetPeopleData
import app.retvens.rown.bottomsheet.bottomSheetPeople.MatchedContact
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }

    private lateinit var popularUsersAdapter: PopularUsersAdapter
    private  var userList: List<MesiboUsersData> = emptyList()

    private lateinit var usersProfileAdapter: UsersProfileAdapter

    lateinit var recycler : RecyclerView
    lateinit var recyclerAll : RecyclerView
    private lateinit var explorePeopleAdapter: BottomSheetAdapterPeople

    lateinit var connectionsSerch: EditText
    lateinit var empty: TextView

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectionsSerch = view.findViewById(R.id.connections_search)
        empty = view.findViewById(R.id.empty)

        recycler = view.findViewById<RecyclerView>(R.id.popularUsers_recycler)
            recycler.layoutManager = GridLayoutManager(context,2)

        recyclerAll = view.findViewById<RecyclerView>(R.id.popularUsers_recycler2)
            recyclerAll.layoutManager = GridLayoutManager(context,2)

//            popularUsersAdapter = PopularUsersAdapter(requireContext(), emptyList())
//            recycler.adapter = popularUsersAdapter
//            popularUsersAdapter.notifyDataSetChanged()

//            getMesiboUsers()
//        getProfil()
        getContacts()
        getAllProfiles()
    }
    private fun getMesiboUsers() {
        val send = RetrofitBuilder.retrofitBuilder.getMesiboUsers()

        send.enqueue(object : Callback<UsersList?> {
            override fun onResponse(call: Call<UsersList?>, response: Response<UsersList?>) {
                if (response.isSuccessful && isAdded) {
                    val response = response.body()!!
                    if (response.result) {
                        userList = response.users
                        // Update the adapter with the new data
                        popularUsersAdapter.userList = userList ?: emptyList()
                        popularUsersAdapter.notifyDataSetChanged()

                        Toast.makeText(context,userList.size.toString(),Toast.LENGTH_SHORT).show()

                    }
                }else{
                    if (isAdded) {
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<UsersList?>, t: Throwable) {
                if (isAdded){
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
/*

    fun getProfil(){
        val pro = RetrofitBuilder.retrofitBuilder.getProfile()
        pro.enqueue(object : Callback<List<UserProfileRequestItem>?> {
            override fun onResponse(
                call: Call<List<UserProfileRequestItem>?>,
                response: Response<List<UserProfileRequestItem>?>
            ) {

                Log.d("Profile",response.toString())
                Log.d("Profile",response.body().toString())

                if(response.isSuccessful && isAdded){
                    val data = response.body()!!
                    usersProfileAdapter = UsersProfileAdapter(requireContext(),
                        data as ArrayList<UserProfileRequestItem>
                    )
                    usersProfileAdapter.removeUser(data)
                    usersProfileAdapter.removeUsersFromList(data)
                    usersProfileAdapter.notifyDataSetChanged()
                    recycler.adapter = usersProfileAdapter

                     connectionsSerch.addTextChangedListener(object : TextWatcher {
                         override fun beforeTextChanged(
                             s: CharSequence?,
                             start: Int,
                             count: Int,
                             after: Int
                         ) {

                         }

                         override fun onTextChanged(
                             s: CharSequence?,
                             start: Int,
                             before: Int,
                             count: Int
                         ) {
                             val original = data.toList()
                             val filter = original.filter { searchUser ->
                                 searchUser.Full_name!!.contains(s.toString(),ignoreCase = false)
                             }
                             usersProfileAdapter.searchConnection(filter)
                         }

                         override fun afterTextChanged(s: Editable?) {

                         }
                     })

                }

//                Log.d("Profile",response.body()?.)
            }
            override fun onFailure(call: Call<List<UserProfileRequestItem>?>, t: Throwable) {
                Toast.makeText(context,t.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
                Log.d("Profile",t.localizedMessage?.toString(),t)
            }
        })
    }
*/

    fun getContacts(){
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val pro = RetrofitBuilder.retrofitBuilder.getContactsProfile(user_id)
        pro.enqueue(object : Callback<BottomSheetPeopleData?>,
            UsersProfileAdapter.ConnectClickListener {
            override fun onResponse(
                call: Call<BottomSheetPeopleData?>,
                response: Response<BottomSheetPeopleData?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        if (response.message == "Matches found") {

                            response.matchedContacts

                                try {
                                    usersProfileAdapter = UsersProfileAdapter(requireContext(),
                                        response.matchedContacts as ArrayList<MatchedContact>
                                    )
                                    usersProfileAdapter.removeUser(response.matchedContacts)
                                    usersProfileAdapter.removeUsersFromList(response.matchedContacts)
                                    usersProfileAdapter.notifyDataSetChanged()
                                    recycler.adapter = usersProfileAdapter
                                }catch (e:NullPointerException){
                                    empty.visibility = View.VISIBLE
                                    Log.e("error",e.message.toString())
                                }

                            usersProfileAdapter.setJobSavedClickListener(this)
                            usersProfileAdapter.cancelConnRequest(this)


                        } else {
//                            empty.text = "You did'nt post yet"
                            empty.visibility = View.VISIBLE
                        }
                    } else {
//                        getAllProfiles()
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                    }
                }
            }

            override fun onFailure(call: Call<BottomSheetPeopleData?>, t: Throwable) {
                empty.visibility = View.VISIBLE
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

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getProfiles = RetrofitBuilder.exploreApis.getPeople(user_id,"1")

//        isLoading = true

        getProfiles.enqueue(object : Callback<List<ExplorePeopleDataClass>?>,
            BottomSheetAdapterPeople.ConnectClickListener {
            override fun onResponse(
                call: Call<List<ExplorePeopleDataClass>?>,
                response: Response<List<ExplorePeopleDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isNotEmpty()) {
                            val response = response.body()!!

                            response.forEach { explorePeopleDataClass ->

                                try {
                                    explorePeopleAdapter = BottomSheetAdapterPeople(requireContext(),
                                        explorePeopleDataClass.posts as ArrayList<Post>
                                    )
                                    explorePeopleAdapter.removeUser(explorePeopleDataClass.posts)
                                    explorePeopleAdapter.removeUsersFromList(explorePeopleDataClass.posts)
                                    explorePeopleAdapter.notifyDataSetChanged()
                                    recyclerAll.adapter = explorePeopleAdapter
                                }catch (e:NullPointerException){
                                    Log.e("error",e.message.toString())
                                }
                            }

                            explorePeopleAdapter.setJobSavedClickListener(this)
                            explorePeopleAdapter.cancelConnRequest(this)


                        } else {
//                            empty.text = "You did'nt post yet"
//                            empty.visibility = View.VISIBLE
                        }
                    } else {
//                        empty.visibility = View.VISIBLE
//                        empty.text = response.code().toString()
                    }
                }
            }

            override fun onFailure(call: Call<List<ExplorePeopleDataClass>?>, t: Throwable) {
//                empty.text = "Try Again"
//                empty.visibility = View.VISIBLE
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
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val removeRequest = RetrofitBuilder.connectionApi.removeRequest(userId,
            ConnectionDataClass(user_id)
        )

        removeRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
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
                if (response.isSuccessful && isAdded){
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
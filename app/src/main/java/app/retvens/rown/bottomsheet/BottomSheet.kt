package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.PopularUsersAdapter
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.R
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
    lateinit var connectionsSerch: EditText

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

        recycler = view.findViewById<RecyclerView>(R.id.popularUsers_recycler)
            recycler.layoutManager = GridLayoutManager(context,2)

//            popularUsersAdapter = PopularUsersAdapter(requireContext(), emptyList())
//            recycler.adapter = popularUsersAdapter
//            popularUsersAdapter.notifyDataSetChanged()

//            getMesiboUsers()
        getProfil()
    }
    private fun getMesiboUsers() {
        val send = RetrofitBuilder.retrofitBuilder.getMesiboUsers()

        send.enqueue(object : Callback<UsersList?> {
            override fun onResponse(call: Call<UsersList?>, response: Response<UsersList?>) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    if (response.result) {
                        userList = response.users
                        // Update the adapter with the new data
                        popularUsersAdapter.userList = userList ?: emptyList()
                        popularUsersAdapter.notifyDataSetChanged()

                        Toast.makeText(context,userList.size.toString(),Toast.LENGTH_SHORT).show()

                    }
                }else{
                    Toast.makeText(context,response.message().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UsersList?>, t: Throwable) {
//                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getProfil(){
        val pro = RetrofitBuilder.retrofitBuilder.getProfile()
        pro.enqueue(object : Callback<List<UserProfileRequestItem>?> {
            override fun onResponse(
                call: Call<List<UserProfileRequestItem>?>,
                response: Response<List<UserProfileRequestItem>?>
            ) {
                Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show()
                Log.d("Profile",response.toString())
                Log.d("Profile",response.body().toString())

                if(response.isSuccessful){
                    val data = response.body()!!
                    usersProfileAdapter = UsersProfileAdapter(requireContext(), data)
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
                                 searchUser.Full_name.contains(s.toString(),ignoreCase = false)
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
                Toast.makeText(context,t.localizedMessage!!.toString(),Toast.LENGTH_SHORT).show()
                Log.d("Profile",t.localizedMessage?.toString(),t)
            }
        })
    }

}
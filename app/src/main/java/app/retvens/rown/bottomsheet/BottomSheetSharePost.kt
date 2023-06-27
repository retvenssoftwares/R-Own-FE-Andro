package app.retvens.rown.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.LocationFragmentAdapter
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionListDataClass
import app.retvens.rown.DataCollections.ConnectionCollection.Connections
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.location.CompanyAdapter
import app.retvens.rown.MessagingModule.MesiboMessagingActivity
import app.retvens.rown.MessagingModule.MesiboUI
import app.retvens.rown.NavigationFragments.home.SharePostAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetSharePost(val data:PostItem) : BottomSheetDialogFragment() {

    var mListener: OnBottomCompanyClickListener ? = null
    fun setOnCompanyClickListener(listener: OnBottomCompanyClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetSharePost? {
        return BottomSheetSharePost(data)
    }
    interface OnBottomCompanyClickListener{
        fun bottomLocationClick(CompanyFrBo : String)
    }

    companion object {
        const val Company_TAG = "BottomSheetDailog"
    }


    lateinit var recyclerView : RecyclerView


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.share_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.company_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

       val getList = RetrofitBuilder.connectionApi.getConnectionList(user_id)

        getList.enqueue(object : Callback<List<ConnectionListDataClass>?>,
            SharePostAdapter.OnItemClickListener {
            override fun onResponse(
                call: Call<List<ConnectionListDataClass>?>,
                response: Response<List<ConnectionListDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach { it ->
                        val adapter = SharePostAdapter(requireContext(),it.conns)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()

                        adapter.setOnItemClickListener(this)
                    }

                }
            }

            override fun onFailure(call: Call<List<ConnectionListDataClass>?>, t: Throwable) {

            }

            override fun onItemClick(data: Connections) {

                val address = data.Mesibo_account[0].address

                val profile = Mesibo.getProfile(address)

                val message:MesiboMessage = MesiboMessage()

                message.message = "Hi"
                message.profile = profile
                message.send()

            }


        })



    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
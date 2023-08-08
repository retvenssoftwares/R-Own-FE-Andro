package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.location.UpcomingEventAdapter
import app.retvens.rown.DataCollections.location.UpcomingEventDataclass
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetUpcomingEvent(val location:String) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: UpcomingEventAdapter


    var mListener: OnBottomJobTitleClickListener ? = null
    fun setOnJobTitleClickListener(listener: OnBottomJobTitleClickListener?){
        mListener = listener
    }

    interface OnBottomJobTitleClickListener{
        fun bottomJobTitleClick(name: String, image:String,eventId:String,date:String,location: String)

    }

    companion object {
        const val Job_Title_TAG = "BottomSheetDailog"
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_upcoming_event, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.event_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         //recyclerView. //recyclerView.setHasFixedSize(true)

        getEvent(location)



    }

    private fun getEvent(location: String) {


        val getEvent = RetrofitBuilder.feedsApi.getEvent(location)

        getEvent.enqueue(object : Callback<List<UpcomingEventDataclass>?>,
            UpcomingEventAdapter.OnLocationClickListener {
            override fun onResponse(
                call: Call<List<UpcomingEventDataclass>?>,
                response: Response<List<UpcomingEventDataclass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    eventAdapter = UpcomingEventAdapter(requireContext(),response)
                    recyclerView.adapter = eventAdapter
                    eventAdapter.notifyDataSetChanged()
                    Log.e("location",location)
                    eventAdapter.setOnLocationClickListener(this)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UpcomingEventDataclass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onStateDataClick(
                name: String,
                image: String,
                eventId: String,
                date: String,
                location: String
            ) {

                mListener?.bottomJobTitleClick(name, image, eventId, date,location)


            }
        })


    }


}
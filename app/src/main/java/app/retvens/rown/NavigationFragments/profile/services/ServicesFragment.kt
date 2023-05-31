package app.retvens.rown.NavigationFragments.profile.services

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.VendorServicesAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.VendorServicesData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetEventCategory
import app.retvens.rown.bottomsheet.BottomSheetServiceName
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServicesFragment(val userId:String) : Fragment(), BottomSheetServiceName.OnBottomSNClickListener {

    lateinit var servicesRecycler : RecyclerView
    lateinit var profileServicesAdapter: ProfileServicesAdapter
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView
    lateinit var notPosted : ImageView

    lateinit var addService: CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesRecycler = view.findViewById(R.id.servicesRecycler)
        servicesRecycler.layoutManager = LinearLayoutManager(context)
        servicesRecycler.setHasFixedSize(true)

        empty = view.findViewById(R.id.empty)
        notPosted = view.findViewById(R.id.notPosted)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)

        getServices()

        addService = view.findViewById(R.id.addService)
        addService.setOnClickListener {
            val bottomSheet = BottomSheetServiceName()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetServiceName.SN_TAG)}
            bottomSheet.setOnSNclickListener(this)
        }
    }

    private fun getServices() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val serviceG = RetrofitBuilder.ProfileApis.getProfileService(user_id)
        serviceG.enqueue(object : Callback<List<ProfileServicesDataItem>?> {
            override fun onResponse(
                call: Call<List<ProfileServicesDataItem>?>,
                response: Response<List<ProfileServicesDataItem>?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        Log.d("res", response.body().toString())
                        val response = response.body()!!
                        if (response.isNotEmpty()) {
                            profileServicesAdapter =
                                ProfileServicesAdapter(response, requireContext())
                            servicesRecycler.adapter = profileServicesAdapter
                            profileServicesAdapter.notifyDataSetChanged()
                            Log.d("res", response.toString())
                        } else {
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
//                            empty.text = "You did'nt add any service yet"
                            notPosted.visibility = View.VISIBLE
                        }
                        } else {
                        addService.visibility = View.GONE
                        empty.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                    }
            }
            override fun onFailure(call: Call<List<ProfileServicesDataItem>?>, t: Throwable) {
                if (isAdded){
                    addService.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                    empty.text = "${t.localizedMessage}"
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                }
            }
        })
    }
    override fun bottomSNClick(serviceName: String, id: String) {

    }
}
package app.retvens.rown.NavigationFragments.profile.services

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetEventCategory
import app.retvens.rown.bottomsheet.BottomSheetServiceName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServicesFragment(val userId:String) : Fragment(), BottomSheetServiceName.OnBottomSNClickListener {

    lateinit var servicesRecycler : RecyclerView
    lateinit var profileServicesAdapter: ProfileServicesAdapter

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
                        Log.d("res", response.body().toString())
                        if (response.body()!!.isNotEmpty()) {
                            profileServicesAdapter =
                                ProfileServicesAdapter(response.body()!!, requireContext())
                            servicesRecycler.adapter = profileServicesAdapter
                            profileServicesAdapter.notifyDataSetChanged()
                            Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show()
                            Log.d("res", response.body().toString())
                        } else {

                        }
                    } else {
                        Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<ProfileServicesDataItem>?>, t: Throwable) {
                if (isAdded){
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun bottomSNClick(serviceName: String, id: String) {

    }
}
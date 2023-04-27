package app.retvens.rown.Dashboard.profileCompletion.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainData
import app.retvens.rown.R

class HotelOwnerChainFragment : Fragment(), BackHandler {

    lateinit var chainRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_owner_chain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chainRecyclerView = view.findViewById(R.id.chainHotelRecyclerView)
        chainRecyclerView.layoutManager = LinearLayoutManager(context)
        chainRecyclerView.setHasFixedSize(true)

        setUpChainAdapter()

    }

    private fun setUpChainAdapter() {
        val chainHotelList : MutableList<HotelChainData> = mutableListOf()

        var n : Int = 2

        if(arguments?.getString("hotels") != null){
            val no = arguments?.getString("hotels")
             n = no!!.toInt()
        }

        for (i in 1..n) {
            val hotelChainData = HotelChainData(
                R.drawable.png_post,
                "Hotel $i Name",
                "Hotel $i Star rating",
                "Hotel $i Location"
            )
            chainHotelList.add(hotelChainData)
        }

        val hotelChainAdapter = HotelChainAdapter(chainHotelList, requireContext())
        chainRecyclerView.adapter = hotelChainAdapter
        hotelChainAdapter.notifyDataSetChanged()
    }

    override fun handleBackPressed(): Boolean {

        val fragment = HotelOwnerFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true

    }

}
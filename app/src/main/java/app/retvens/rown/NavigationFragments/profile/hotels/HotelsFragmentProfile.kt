package app.retvens.rown.NavigationFragments.profile.hotels

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelsData
import app.retvens.rown.R


class HotelsFragmentProfile : Fragment() {

    lateinit var recycler : RecyclerView

    lateinit var profileHotelsAdapter: ProfileHotelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotels_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)

        view.findViewById<CardView>(R.id.addHotel).setOnClickListener {
            startActivity(Intent(context, AddHotelActivity::class.java))
        }


        val blogs = listOf<ExploreHotelsData>(
            ExploreHotelsData("Paradise Inn"),
            ExploreHotelsData("Paradise Inn 2"),
            ExploreHotelsData("Neck Deep Paradise Inn"),
            ExploreHotelsData("Paradise Inn 23"),
            ExploreHotelsData("Paradise Inn 1"),
        )

        profileHotelsAdapter = ProfileHotelsAdapter(blogs, requireContext())
        recycler.adapter = profileHotelsAdapter
        profileHotelsAdapter.notifyDataSetChanged()

    }
}
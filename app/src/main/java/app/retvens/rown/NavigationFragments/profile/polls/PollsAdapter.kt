package app.retvens.rown.NavigationFragments.profile.polls

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.Option
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.TimesStamp.convertTimeToText
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PollsDetails
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.utils.verificationStatus
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import java.lang.IndexOutOfBoundsException

class PollsAdapter(val pollList:ArrayList<PostItem>, val context: Context,val UserID:String) : RecyclerView.Adapter<PollsAdapter.PollsViewHolder>() {

    class PollsViewHolder(itemView: View) : ViewHolder(itemView){
        val checkVotes = itemView.findViewById<CardView>(R.id.checkVotes)
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
        val location = itemView.findViewById<TextView>(R.id.location)
        val time = itemView.findViewById<TextView>(R.id.post_time)
        val title = itemView.findViewById<TextView>(R.id.title_poll)
        val recycler = itemView.findViewById<RecyclerView>(R.id.votesOptionsrecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_poll_profile, parent, false)
        return PollsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pollList.size
    }

    override fun onBindViewHolder(holder: PollsViewHolder, position: Int) {

        var option:ArrayList<app.retvens.rown.DataCollections.FeedCollection.Option> = ArrayList()

        val poll = pollList[position]

        if (poll.verificationStatus != "false"){
            holder.verification.visibility = View.VISIBLE
        }

        val time = TimesStamp.convertTimeToText(poll.date_added)
        holder.time.text = time

        poll.pollQuestion.forEach {
            holder.title.text = it.Question
        }

        if (poll.User_name.isNotEmpty()){
            holder.name.text = poll.User_name
        } else{
            holder.name.text = poll.Full_name
        }

        if (poll.Profile_pic.isNotEmpty()) {
            Glide.with(context).load(poll.Profile_pic).into(holder.profile)
        } else {
            holder.profile.setImageResource(R.drawable.svg_user)
        }

        holder.location.text = poll.location

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        if (user_id != UserID){
            holder.checkVotes.visibility = View.GONE
        }

        holder.checkVotes.setOnClickListener {

            val intent = Intent(context,VotesActivity::class.java)
            intent.putExtra("postId",poll.post_id)
            context.startActivity(intent)
        }


        holder.recycler.layoutManager = LinearLayoutManager(context)
        holder.recycler.setHasFixedSize(true)

        poll.pollQuestion.forEach {
            option.addAll(it.Options)
        }
        var total:Int = 0
        poll.pollQuestion.forEach {
            total = calculateTotalVotes(it.Options.toTypedArray())
        }

        Log.e("total",total.toString())

        try {
            val adapter = app.retvens.rown.NavigationFragments.home.PollsAdapter(context,option,
                PollsDetails(poll.post_id,poll.voted),total
            )
            holder.recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }



    }
    fun removePostsFromList(data: List<PostItem>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    pollList.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }


    fun calculateTotalVotes(options: Array<Option>): Int {
        var totalVotes = 0

        for (option in options) {
            totalVotes += option.votes.size
        }

        return totalVotes
    }
}
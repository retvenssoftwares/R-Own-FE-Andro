package app.retvens.rown.NavigationFragments.profile.polls

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.TimesStamp.convertTimeToText
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import java.lang.IndexOutOfBoundsException

class PollsAdapter(val pollList:List<PostItem>, val context: Context) : RecyclerView.Adapter<PollsAdapter.PollsViewHolder>() {

    class PollsViewHolder(itemView: View) : ViewHolder(itemView){
        val title_poll = itemView.findViewById<TextView>(R.id.title_poll)
        val checkVotes = itemView.findViewById<CardView>(R.id.checkVotes)
        val option1 = itemView.findViewById<TextView>(R.id.option1)
        val option2 = itemView.findViewById<TextView>(R.id.option2)
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val location = itemView.findViewById<TextView>(R.id.location)
        val time = itemView.findViewById<TextView>(R.id.post_time)
        val progressBar1 = itemView.findViewById<RoundedProgressBar>(R.id.progressBar)
        val progressBar2 = itemView.findViewById<RoundedProgressBar>(R.id.progressBar2)
        val voteOption1 = itemView.findViewById<LinearLayout>(R.id.voteOption1)
        val voteOption2 = itemView.findViewById<LinearLayout>(R.id.voteOption2)
        val Option1Votes = itemView.findViewById<TextView>(R.id.Option1_votes)
        val Option2Votes = itemView.findViewById<TextView>(R.id.Option2_votes)
        val option1_percentage = itemView.findViewById<TextView>(R.id.option1_percentage)
        val option2_percentage = itemView.findViewById<TextView>(R.id.option2_percentage)
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

        val poll = pollList[position]

        try {
            val question = poll.pollQuestion[position]

            holder.title_poll.text = question.Question
            holder.option1.text = question.Options[0].Option
            holder.option2.text = question.Options[1].Option

            val progressBarOption1: RoundedProgressBar = holder.progressBar1
            val progressBarOption2: RoundedProgressBar = holder.progressBar2


            val vote1: List<String> = question.Options[0].votes.map { it.user_id }
            val vote2: List<String> = question.Options[1].votes.map { it.user_id }

            val timestamp = convertTimeToText(question.date_added)
            holder.time.text = timestamp




//            holder.voteOption1.setOnClickListener {
//
//                if (poll.voted == "no") {
//                    voteOption(banner.post_id, item.Options[0].option_id)
//                    val vote = vote1.size+1
//                    holder.Option1Votes.text = "${vote} votes"
//                    val totalVotes = vote + vote2.size
//                    val completedTasks = totalVotes
//                    val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
//                    if (!completedPercentage.isNaN()) {
//                        progressBarOption1.setProgressPercentage(completedPercentage)
//                        binding.option1Percentage.text = "${completedPercentage}%"
//                    }
//                }
//            }
//
//
//
//
//            binding.voteOption2.setOnClickListener {
//
//                if (banner.voted == "no"){
//                    voteOption(banner.post_id,item.Options[1].option_id)
//
//                    val vote = vote2.size + 1
//                    binding.Option2Votes.text = "${vote} votes"
//                    val totalVotes = vote1.size + vote
//                    val completedTasks2 = totalVotes
//                    val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
//                    if (!completedPercentage2.isNaN()) {
//                        progressBarOption2.setProgressPercentage(completedPercentage2)
//                        binding.option2Percentage.text = "${completedPercentage2}%"
//
//                    }
//                }
//
//            }
//
            holder.Option1Votes.text = "${vote1.size} votes"
            holder.Option2Votes.text = "${vote2.size} votes"

            val totalVotes = vote1.size + vote2.size
            val completedTasks = vote1.size
            val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
            if (!completedPercentage.isNaN()) {
                progressBarOption1.setProgressPercentage(completedPercentage)
                holder.option1_percentage.text = "${completedPercentage}%"
            }


            val completedTasks2 = vote2.size
            val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
            if (!completedPercentage2.isNaN()) {
                progressBarOption2.setProgressPercentage(completedPercentage2)
                holder.option2_percentage.text = "${completedPercentage2}%"
            }



        } catch (e : IndexOutOfBoundsException){}
        holder.name.text = poll.Full_name
        Glide.with(context).load(poll.Profile_pic).into(holder.profile)
        holder.location.text = poll.location



        holder.checkVotes.setOnClickListener {
            context.startActivity(Intent(context, VotersActivity::class.java))
        }
    }
}
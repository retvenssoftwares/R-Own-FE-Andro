package app.retvens.rown.NavigationFragments.profile.polls

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
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


            val timeStamp = TimesStamp.convertTimeToText(question.date_added)
            holder.time.text = timeStamp.toString()

        } catch (e : IndexOutOfBoundsException){}
        holder.name.text = poll.Full_name
        Glide.with(context).load(poll.Profile_pic).into(holder.profile)
        holder.location.text = poll.location



        holder.checkVotes.setOnClickListener {
            context.startActivity(Intent(context, VotersActivity::class.java))
        }
    }
}
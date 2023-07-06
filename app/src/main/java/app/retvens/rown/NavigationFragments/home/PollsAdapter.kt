package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.Comments
import app.retvens.rown.DataCollections.FeedCollection.Option
import app.retvens.rown.DataCollections.FeedCollection.PollQuestion
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.LocationDataClass
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.mackhartley.roundedprogressbar.RoundedProgressBar

class PollsAdapter(val context: Context, var pollList:List<Option>,var datas:PollsDetails): RecyclerView.Adapter<PollsAdapter.MyViewHolderClass>() {

    private var onItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(optionId: String,postId:String)


    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MyViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val options = itemview.findViewById<TextView>(R.id.option1)
        val count = itemview.findViewById<TextView>(R.id.Option1_votes)
        val percentage = itemview.findViewById<TextView>(R.id.option1_percentage)
        val progress = itemview.findViewById<RoundedProgressBar>(R.id.progressBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vote_polls,parent,false)
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        val data = pollList[position]

        Log.e("option",data.Option.toString())

        holder.options.text = data.Option

        holder.count.text = data.votes.size.toString()

        val vote = data.votes
        holder.count.text = "${vote.size} votes"
        val totalVotes = vote.size.toDouble()
        val completedTasks2 = totalVotes
        val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
        if (!completedPercentage2.isNaN()) {
            holder.progress.setProgressPercentage(completedPercentage2)
            holder.percentage.text = "${completedPercentage2}%"

        }





        holder.itemView.setOnClickListener {

            val vote = data.votes
            holder.count.text = "${vote.size+1} votes"
            val totalVotes = vote.size.toDouble()+1
            val completedTasks2 = totalVotes
            val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
            if (!completedPercentage2.isNaN()) {
                holder.progress.setProgressPercentage(completedPercentage2)
                holder.percentage.text = "${completedPercentage2}%"

            }

            onItemClickListener?.onItemClick(data.option_id,datas.post_id)

        }



    }


    override fun getItemCount(): Int {
        return pollList.size
    }

}
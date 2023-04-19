package app.retvens.rown.authentication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.GetInterests
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInterestAdapter(val context: Context, var interestList : List<GetInterests>) : RecyclerView.Adapter<UserInterestAdapter.InterestViewHolder>() {

    lateinit var user_id : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.interest_grid_item,parent,false)
        return InterestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return interestList.size
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val currentItem = interestList[position]
        holder.name.text = currentItem.Name

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id", "").toString()

        holder.name.setOnClickListener {
            uploadInterest(currentItem.id)
            holder.linearLayout.background = holder.itemView.context.getDrawable(R.drawable.interest_item_selected)
        }

    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.interest_name)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.interest_item_layout)
    }

    fun searchInterest(searchText : List<GetInterests>){
        interestList = searchText
        notifyDataSetChanged()
    }

    private fun uploadInterest(id: String) {
        val update = RetrofitBuilder.retrofitBuilder.updateInterest(id,
            UpdateInterestClass(user_id))
        Toast.makeText(context,"User_id : $user_id",Toast.LENGTH_SHORT).show()
        update.enqueue(object : Callback<ContactResponse?> {
            override fun onResponse(
                call: Call<ContactResponse?>,
                response: Response<ContactResponse?>
            ) {
                Toast.makeText(context,response.body()?.message.toString(),Toast.LENGTH_SHORT).show()
                Log.d("update_interest",response.body()?.message.toString())
                Log.d("update_interest",response.toString())
            }

            override fun onFailure(call: Call<ContactResponse?>, t: Throwable) {
                Toast.makeText(context,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                Log.d("update_interest",t.localizedMessage,t)
            }
        })

    }
}
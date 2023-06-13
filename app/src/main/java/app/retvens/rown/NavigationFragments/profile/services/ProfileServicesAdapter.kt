package app.retvens.rown.NavigationFragments.profile.services

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.viewAll.vendorsDetails.VendorDetailsActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileServicesAdapter(val listS : List<ProfileServicesDataItem>, val context: Context, val isOwner : Boolean) : RecyclerView.Adapter<ProfileServicesAdapter.PollsViewHolder>() {

    class PollsViewHolder(itemView: View) : ViewHolder(itemView){
        val vendor_name = itemView.findViewById<TextView>(R.id.vendor_name)
        val vendors_id = itemView.findViewById<TextView>(R.id.vendors_id)
        val serviceName = itemView.findViewById<TextView>(R.id.serviceName)
        val location = itemView.findViewById<TextView>(R.id.location)
        val servicePrice = itemView.findViewById<TextView>(R.id.servicePrice)
        val vendor_profile = itemView.findViewById<ImageView>(R.id.vendor_profile)

        val edit = itemView.findViewById<LinearLayout>(R.id.edit)
        val del = itemView.findViewById<ImageView>(R.id.del)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_services_profile, parent, false)
        return PollsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: PollsViewHolder, position: Int) {
        val data = listS[position]

        if (!isOwner){
            holder.del.visibility = View.GONE
            holder.edit.visibility = View.GONE
        }
        holder.vendor_name.text = data.vendorName
        holder.vendors_id.text = "@${data.User_name}"
        holder.serviceName.text = data.service_name
        holder.location.text = data.location
        holder.servicePrice.text = data.vendorServicePrice
        Glide.with(context).load(data.Profile_pic).into(holder.vendor_profile)

        holder.del.setOnClickListener {
            openBottomForDel(listS[position].vendorServiceId)
        }
        holder.edit.setOnClickListener {
            openBottomEdit(listS[position].vendorServiceId)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VendorDetailsActivity::class.java)
            intent.putExtra("user_id", data.user_id)
            intent.putExtra("vendorImage", data.vendorImage)
            intent.putExtra("vendorName", data.vendorName)
            context.startActivity(intent)
        }

    }
    private fun openBottomForDel(vendorServiceId: String) {
        val dialogLanguage = Dialog(context)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_delete_service)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        dialogLanguage.findViewById<TextView>(R.id.yes).setOnClickListener {
            deleteService(vendorServiceId)
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.not).setOnClickListener { dialogLanguage.dismiss() }
    }

    private fun deleteService(vendorServiceId: String) {
        val del = RetrofitBuilder.ProfileApis.deleteService(vendorServiceId)
        del.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {

            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openBottomEdit(vendorServiceId: String) {
        val dialogLanguage = Dialog(context)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_edit_service)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        val price = dialogLanguage.findViewById<TextInputEditText>(R.id.price)

        dialogLanguage.findViewById<CardView>(R.id.save).setOnClickListener {
            updateServicePrice(vendorServiceId, price.text.toString())
            dialogLanguage.dismiss()
        }
    }

    private fun updateServicePrice(vendorServiceId: String, price: String) {
        val up = RetrofitBuilder.ProfileApis.updatePrice(vendorServiceId, UpdatePrice("$price INR"))
        up.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {

            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}
package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.DeleteAccount
import app.retvens.rown.DataCollections.FeedCollection.DeletePost
import app.retvens.rown.DataCollections.FeedCollection.EditPostClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.MainActivity
import app.retvens.rown.R
import app.retvens.rown.authentication.LoginActivity
import app.retvens.rown.utils.clearConnectionNo
import app.retvens.rown.utils.clearFullName
import app.retvens.rown.utils.clearProfileImage
import app.retvens.rown.utils.clearUserId
import app.retvens.rown.utils.moveToClear
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetDeleteAccount() : BottomSheetDialogFragment(){

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog:Dialog

    var mListener: OnBottomSheetHotelierProfileSettingClickListener ? = null
    fun setOnBottomSheetProfileSettingClickListener(listener: OnBottomSheetHotelierProfileSettingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetDeleteAccount? {
        return BottomSheetDeleteAccount()
    }
    interface OnBottomSheetHotelierProfileSettingClickListener{
        fun bottomSheetHotelierProfileSettingClick(bottomSheetHotelierProfileSettingFrBo : String)
    }

    companion object {
        const val Hotelier_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_delete_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val deletePost = view.findViewById<CardView>(R.id.card_yes)
        val deleteNoPost = view.findViewById<CardView>(R.id.card_no)
        val text = view.findViewById<TextView>(R.id.deleteText)
        text.text = "Do you really want to      delete this Account?"

        deletePost.setOnClickListener {
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
            saveChanges()
        }

        deleteNoPost.setOnClickListener {
            dismiss()
            progressDialog.dismiss()
        }

    }

    private fun saveChanges() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
       val delete = RetrofitBuilder.ProfileApis.deleteAccount(DeleteAccount(user_id))

        delete.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Account Deleted",Toast.LENGTH_SHORT).show()
                    dismiss()
                    auth.signOut()
                    SharedPreferenceManagerAdmin.getInstance(requireContext()).clear()
                    moveToClear(requireContext())
                    clearUserId(requireContext())
                    clearFullName(requireContext())
                    clearProfileImage(requireContext())
                    clearConnectionNo(requireContext())
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{
                    Log.e("error",response.message().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })


    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
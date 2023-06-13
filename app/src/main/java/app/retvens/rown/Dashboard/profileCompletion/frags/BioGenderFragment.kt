package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BioGenderFragment : Fragment() {

    private lateinit var profile: ShapeableImageView
    private lateinit var name: TextView
    private lateinit var bioEt: TextInputEditText
    private var Gender = ""

    lateinit var male : LinearLayout
    lateinit var female : LinearLayout
    lateinit var nonBinary : LinearLayout
    lateinit var preferNotSay : LinearLayout

    lateinit var progressDialog : Dialog

    lateinit var card_complete_continue : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bio_gender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        male = view.findViewById(R.id.male)
        female = view.findViewById(R.id.female)
        nonBinary = view.findViewById(R.id.nonBinary)
        preferNotSay = view.findViewById(R.id.preferNotSay)

        card_complete_continue = view.findViewById(R.id.card_complete_continue)

        profile = view.findViewById(R.id.user_complete_profile)
        name = view.findViewById(R.id.user_complete_name)

        bioEt = view.findViewById(R.id.bioEt)

        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferences = context?.getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferences?.getString("profile_image", "").toString()

        if (profilePic.isNotEmpty()) {
            Glide.with(requireContext()).load(profilePic).into(profile)
        }
        name.setText("Hi $profileName")

        male.setOnClickListener {
            Gender = "Male"
            male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_own))
            female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            nonBinary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            preferNotSay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        female.setOnClickListener {
            Gender = "Female"
            male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_own))
            nonBinary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            preferNotSay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        nonBinary.setOnClickListener {
            Gender = "Non Binary"
            male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            nonBinary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_own))
            preferNotSay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        preferNotSay.setOnClickListener {
            Gender = "Prefer not to say"
            male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            nonBinary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            preferNotSay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_own))
        }

        card_complete_continue.setOnClickListener {
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()
            sendData()
        }

    }

    private fun sendData() {
        val sharedPreferencesU = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferencesU?.getString("user_id", "").toString()

        val send = RetrofitBuilder.profileCompletion.setBio(user_id,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),bioEt.text.toString()),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Gender)
        )
        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        profileComStatus(context!!, "65")
                        profileCompletionStatus = "65"
                        val fragment = LocationFragment()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragment_username, fragment)
                        transaction?.commit()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        t.localizedMessage?.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressDialog.dismiss()
            }
        })

    }
}
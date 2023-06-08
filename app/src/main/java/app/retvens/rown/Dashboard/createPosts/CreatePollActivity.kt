package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCreatePollBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePollActivity : AppCompatActivity() {
    lateinit var binding:ActivityCreatePollBinding


    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        binding.sharePoll.setOnClickListener {
            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.etAddStatement.text!!.isEmpty()){
                binding.addStatement.error = "Add Statement"
            }else if (binding.opinion1.text!!.isEmpty()){
                binding.opinion1Layout.error = "Add Opinion"
            }else if (binding.opinion2.text!!.isEmpty()){
                binding.opinion2Layout.error = "Add Opinion"
            }else{
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                createPoll(user_id)
            }


        }





    }

    private fun createPoll(userId: String) {

        val statement = binding.etAddStatement.text.toString()
        val opinion1 = binding.opinion1.text.toString()
        val opinion2 = binding.opinion2.text.toString()

        val sendPoll = RetrofitBuilder.feedsApi.createPoll(userId,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userId),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),"Polls"),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),statement),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),opinion1),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),opinion2)
            )

        sendPoll.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    val response = response.body()!!
                    val intent = Intent(applicationContext,DashBoardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else{
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })

    }
}
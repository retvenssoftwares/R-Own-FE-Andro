package app.retvens.rown.authentication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.GetInterests
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserInterestBinding
import app.retvens.rown.utils.moveTo
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInterest : AppCompatActivity(), UserInterestAdapter.onItemClickListener {
    lateinit var binding: ActivityUserInterestBinding

    lateinit var progressDialog : Dialog
    lateinit var addedIntersts : MutableList<GetInterests>

    lateinit var username: String
    lateinit var userInterestAdapter: UserInterestAdapter

    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id", "").toString()

        username = intent.getStringExtra("user").toString()
        binding.userName.text = "Hello, $username!"

        binding.interestGrid.layoutManager = GridLayoutManager(this,3)
        binding.interestGrid.setHasFixedSize(true)

        binding.cardContinueInterest.setOnClickListener {
//            progressDialog = Dialog(this)
//            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            progressDialog.setCancelable(true)
//            progressDialog.setContentView(R.layout.progress_dialoge)
//            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
//
//            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
//            progressDialog.show()


            addedIntersts.forEach {
                Log.d("Interest", it.id.toString())
                uploadInterest(it.id)
//                Toast.makeText(this, it.Name.toString(), Toast.LENGTH_SHORT).show()
            }

            moveTo(this,"MoveToUC")
            val intent = Intent(applicationContext, UserContacts::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("name",username)
            startActivity(intent)
        }
        getInterests()
    }

    private fun getInterests() {
        val retroInterest = RetrofitBuilder.retrofitBuilder.getInterests()
        retroInterest.enqueue(object : Callback<List<GetInterests>?> {
            override fun onResponse(call: Call<List<GetInterests>?>, response: Response<List<GetInterests>?>) {
                Log.d("Interest","body : ${response.body().toString()}")
                Log.d("Interest","message ${response.message().toString()}")
                Log.d("Interest",response.code().toString())
                Log.d("Interest",response.isSuccessful.toString())

                if (response.isSuccessful){
                    val data = response.body()!!
                    userInterestAdapter = UserInterestAdapter(applicationContext, data)
                    userInterestAdapter.notifyDataSetChanged()
                    binding.interestGrid.adapter = userInterestAdapter
                    userInterestAdapter.setOnItemClickListener(this@UserInterest)

                    binding.etSearchInterest.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val originalData = data.toList()
                            val filterData = originalData.filter {searchInData->
                                searchInData.Name.contains(s.toString(),ignoreCase = false)
                            }
                            userInterestAdapter.searchInterest(filterData)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                }
            }
            override fun onFailure(call: Call<List<GetInterests>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                Log.d("Interest",t.localizedMessage,t)
            }
        })
    }

    override fun onItemClickInterest(addedItems: MutableList<GetInterests>) {
        addedIntersts = addedItems
    }

    private fun uploadInterest(id: String) {
        val update = RetrofitBuilder.retrofitBuilder.updateInterest(id, UpdateInterestClass(user_id))
        Toast.makeText(applicationContext,"User_id : $user_id",Toast.LENGTH_SHORT).show()
        update.enqueue(object : Callback<ContactResponse?> {
            override fun onResponse(
                call: Call<ContactResponse?>,
                response: Response<ContactResponse?>
            ) {
                Toast.makeText(applicationContext,"$id - ${response.body()?.message.toString()}",Toast.LENGTH_SHORT).show()
                Log.d("update_interest",response.body()?.message.toString())
                Log.d("update_interest",response.toString())
            }

            override fun onFailure(call: Call<ContactResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage!!.toString(),Toast.LENGTH_SHORT).show()
                Log.d("update_interest",t.localizedMessage,t)
            }
        })

    }
}
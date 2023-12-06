package app.retvens.rown.NavigationFragments.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.BlockUserDataClass
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockUserActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BlockUserAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_user)

        val back = findViewById<ImageView>(R.id.re_backBtn)
        back.setOnClickListener {
            onBackPressed()
        }

        recyclerView = findViewById(R.id.connectionsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
       //  //recyclerView. //recyclerView.setHasFixedSize(true)

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        getBlockUser(user_id)
    }

    private fun getBlockUser(userId: String) {
        val getBlock = RetrofitBuilder.ProfileApis.getBlockList(userId)

       getBlock.enqueue(object : Callback<List<BlockUserDataClass>?> {
           override fun onResponse(
               call: Call<List<BlockUserDataClass>?>,
               response: Response<List<BlockUserDataClass>?>
           ) {
                   if (response.isSuccessful){
                       val response = response.body()!!
                       adapter = BlockUserAdapter(applicationContext,response)
                       recyclerView.adapter = adapter
                       adapter.notifyDataSetChanged()
                   }else{
                       Log.e("error",response.code().toString())
                   }

           }

           override fun onFailure(call: Call<List<BlockUserDataClass>?>, t: Throwable) {
               Log.e("error",t.message.toString())
           }
       })
    }
}
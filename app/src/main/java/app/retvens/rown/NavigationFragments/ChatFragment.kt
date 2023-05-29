//package app.retvens.rown.NavigationFragments
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.cardview.widget.CardView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import app.retvens.rown.ApiRequest.AppDatabase
//import app.retvens.rown.ChatSection.MesiboUsers
//import app.retvens.rown.CreateCommunity.UploadIconAdapter
//import app.retvens.rown.NavigationFragments.FragmntAdapters.ChatFragmentAdapter
//import app.retvens.rown.R
//import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
//
//class ChatFragment : Fragment() {
//
//    private lateinit var myRecyclerView: RecyclerView
//    private lateinit var adapter: ChatFragmentAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val openList = view.findViewById<CardView>(R.id.openList)
//
//        openList.setOnClickListener {
//            startActivity(Intent(requireContext(),MesiboUsers::class.java))
//        }
//
//        myRecyclerView = view.findViewById(R.id.friendsList)
//        myRecyclerView.layoutManager = LinearLayoutManager(context)
//
//        val address = SharedPreferenceManagerAdmin.getInstance(requireContext()).user.address
//
//        Thread {
//            val receiver = AppDatabase.getInstance(requireContext()).chatMessageDao().getRecentChats(address!!)
//
//            run {
//
//
//                val handler = Handler(Looper.getMainLooper())
//
//// Run the UI update code in the main thread using the Handler
//                handler.post {
//                    // Update UI code goes here
//                    adapter = ChatFragmentAdapter(requireContext(), receiver)
//                    myRecyclerView.adapter = adapter
//                }
//            }
//
//        }.start()
//
//
//
//    }
//
//}
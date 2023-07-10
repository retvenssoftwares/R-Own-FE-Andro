package app.retvens.rown.NavigationFragments.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetEditEducation
import app.retvens.rown.bottomsheet.BottomSheetEditExperience
import app.retvens.rown.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity(),
    BottomSheetEditExperience.OnBottomEditExClickListener,
    BottomSheetEditEducation.OnBottomEditEdClickListener {

    lateinit var binding : ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener{ onBackPressed() }

        binding.addExperience.setOnClickListener {
            val bottomSheet = BottomSheetEditExperience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditExperience.Edit_TAG)}
            bottomSheet.setOnEditExClickListener(this)
        }

        binding.addEducation.setOnClickListener {
            val bottomSheet = BottomSheetEditEducation()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditEducation.Edit_TAG)}
            bottomSheet.setOnEditEdClickListener(this)
        }
    }

    override fun bottomEditClick(CTCFrBo: String) {

    }

    override fun bottomEditEdClick(EdFrBo: String) {

    }
}
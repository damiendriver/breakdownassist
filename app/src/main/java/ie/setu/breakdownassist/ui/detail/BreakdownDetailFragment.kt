package ie.setu.breakdownassist.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.breakdownassist.ui.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.FragmentBreakdownDetailBinding
import ie.setu.breakdownassist.ui.list.ListViewModel
import timber.log.Timber

class BreakdownDetailFragment : Fragment() {

    private lateinit var detailViewModel: BreakdownDetailViewModel
    private val args by navArgs<BreakdownDetailFragmentArgs>()
    private var _fragBinding: FragmentBreakdownDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentBreakdownDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(BreakdownDetailViewModel::class.java)
        detailViewModel.observableBreakdown.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editBreakdownButton.setOnClickListener {
            detailViewModel.updateBreakdown(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.breakdownid, fragBinding.breakdown?.observableBreakdown!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteBreakdownButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableBreakdown.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
        fragBinding.editTitle.setText("Garage Title")
        fragBinding.editType.setText("Services Offered")
        fragBinding.editOpen.setText("Hours")
        fragBinding.editDescription.setText("About Garage")
        fragBinding.breakdown = detailViewModel
        Timber.i("Retrofit fragBinding.breakdown == $fragBinding.breakdown")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getBreakdown(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.breakdownid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
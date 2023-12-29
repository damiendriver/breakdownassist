package ie.setu.breakdownassist.breakdown

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.FragmentBreakdownBinding
import ie.setu.breakdownassist.list.ListViewModel
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel

class BreakdownFragment : Fragment() {

    lateinit var app: MainApp
    var totalBreakdown = 0
    private var _fragBinding: FragmentBreakdownBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var breakdownViewModel: BreakdownViewModel
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_breakdown, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentBreakdownBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_breakdown)

        fragBinding.breakdownTitle.setText("")
        fragBinding.description.setText("")
        fragBinding.phone.setText("")
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BreakdownFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
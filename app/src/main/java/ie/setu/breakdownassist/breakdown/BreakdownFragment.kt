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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.lifecycle.Observer
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.FragmentBreakdownBinding
import ie.setu.breakdownassist.list.ListViewModel
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.map.MapsViewModel
import ie.setu.breakdownassist.models.BreakdownModel

class BreakdownFragment : Fragment() {

    lateinit var app: MainApp
    var totalBreakdown = 0
    private var _fragBinding: FragmentBreakdownBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var breakdownViewModel: BreakdownViewModel
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()

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
        setupMenu()
        breakdownViewModel = ViewModelProvider(this).get(BreakdownViewModel::class.java)
        breakdownViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        fragBinding.breakdownTitle.setText("")
        fragBinding.description.setText("")
        fragBinding.phone.setText("")
        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.breakdownError),Toast.LENGTH_LONG).show()
        }
    }



    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_breakdown, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BreakdownFragment().apply {
                arguments = Bundle().apply {}
            }
    }}

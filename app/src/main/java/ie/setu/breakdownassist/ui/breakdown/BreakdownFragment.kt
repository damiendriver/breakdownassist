package ie.setu.breakdownassist.ui.breakdown

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
import com.google.android.material.snackbar.Snackbar
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.ui.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.FragmentBreakdownBinding
import ie.setu.breakdownassist.ui.list.ListViewModel
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.models.BreakdownModel
import timber.log.Timber.Forest.i

class BreakdownFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentBreakdownBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var breakdownViewModel: BreakdownViewModel
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_breakdown, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentBreakdownBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_breakdown)
        setupMenu()
        breakdownViewModel = ViewModelProvider(this).get(BreakdownViewModel::class.java)
        breakdownViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })
        setButtonListener(fragBinding)
        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                }
            }

            false -> Toast.makeText(context, getString(R.string.breakdownError), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setButtonListener(layout: FragmentBreakdownBinding) {
        layout.breakdownButton.setOnClickListener {
            val title = layout.breakdownTitle.text.toString()
            if (title.isNotEmpty()) {
                i("add Button Pressed: $title")
                Snackbar
                    .make(it, "Breakdown $title added", Snackbar.LENGTH_LONG)
                    .show()

                val type = when (layout.serviceType.checkedRadioButtonId) {
                    R.id.radioPetrol -> "Petrol"
                    R.id.radioTyre -> "Tyre"
                    else -> {
                        "Not Know"
                    }
                }
                val selectedOpen = layout.openingHours.checkedRadioButtonId
                val open = when (selectedOpen) {
                    R.id.radio24 -> "Open 24 Hour"
                    R.id.radioNot24 -> "Open 9-5"
                    else -> {
                        "Closed"
                    }
                }

                    val description = layout.description.text.toString()
                    if (description.isNotEmpty()) {
                        i("add Button Pressed: $description")
                        Snackbar
                            .make(it, "Breakdown $description added", Snackbar.LENGTH_LONG)
                            .show()
                }
                breakdownViewModel.addBreakdown(loggedInViewModel.liveFirebaseUser,
                    BreakdownModel(
                        title = title,
                        type = type,
                        open = open,
                        description = description,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!
                    )
                )
                i("Added: $title, $type, $open, $description")
            } else {
                Snackbar
                    .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
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
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

}

package ie.setu.breakdownassist.list

import android.os.Bundle
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.adapters.BreakdownAdapter
import ie.setu.breakdownassist.adapters.BreakdownListener
import ie.setu.breakdownassist.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.FragmentListBinding
import ie.setu.breakdownassist.models.BreakdownModel
import ie.setu.breakdownassist.utils.SwipeToDeleteCallback
import ie.setu.breakdownassist.utils.SwipeToEditCallback
import ie.setu.breakdownassist.utils.createLoader
import ie.setu.breakdownassist.utils.hideLoader
import ie.setu.breakdownassist.utils.showLoader

class ListFragment : Fragment(), BreakdownListener {

    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToBreakdownFragment()
            findNavController().navigate(action)
        }
        showLoader(loader,"Downloading Breakdowns")
        listViewModel.observableBreakdownsList.observe(viewLifecycleOwner, Observer {
                breakdowns ->
            breakdowns?.let {
                render(breakdowns as ArrayList<BreakdownModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Breakdown")
                val adapter = fragBinding.recyclerView.adapter as BreakdownAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                listViewModel.delete(listViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as BreakdownModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onBreakdownClick(viewHolder.itemView.tag as BreakdownModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_report, menu)

                val item = menu.findItem(R.id.toggleBreakdowns) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleBreakdowns: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleBreakdowns.isChecked = false

                toggleBreakdowns.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) listViewModel.loadAll()
                    else listViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(breakdownsList: ArrayList<BreakdownModel>) {
        fragBinding.recyclerView.adapter = BreakdownAdapter(breakdownsList,this, listViewModel.readOnly.value!!)
        if (breakdownsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.breakdownsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.breakdownsNotFound.visibility = View.GONE
        }
    }

    override fun onBreakdownClick(breakdown: BreakdownModel) {
        val action = ListFragmentDirections.actionListFragmentToBreakdownDetailFragment(breakdown.uid!!)
        if(!listViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }


    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Breakdowns")
            if(listViewModel.readOnly.value!!)
                listViewModel.loadAll()
            else
                listViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Breakdowns")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                listViewModel.liveFirebaseUser.value = firebaseUser
                listViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
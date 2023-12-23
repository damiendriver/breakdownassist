package ie.setu.breakdownassist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.setu.breakdownassist.databinding.FragmentBreakdownBinding
import ie.setu.breakdownassist.main.MainApp

class BreakdownFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentBreakdownBinding? = null
    private val fragBinding get() = _fragBinding!!

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
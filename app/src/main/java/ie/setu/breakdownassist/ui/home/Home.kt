package ie.setu.breakdownassist.ui.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import ie.setu.breakdownassist.R
import ie.setu.breakdownassist.databinding.HomeBinding
import ie.setu.breakdownassist.ui.auth.Login
import ie.setu.breakdownassist.ui.auth.LoggedInViewModel
import ie.setu.breakdownassist.databinding.NavHeaderBinding
import ie.setu.breakdownassist.firebase.FirebaseImageManager
import ie.setu.breakdownassist.main.MainApp
import ie.setu.breakdownassist.utils.readImageUri
import ie.setu.breakdownassist.utils.*
import timber.log.Timber


class Home : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var headerView : View
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        app = application as MainApp

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.breakdownFragment, R.id.listFragment, R.id.mapsFragment, R.id.aboutFragment), drawerLayout)


        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(firebaseUser)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })
        registerImagePickerCallback()
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.lightning_mcqueen,
                        navHeaderBinding.navHeaderImage
                    )
                }        } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false
                )
            }    }
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName
    }

    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderImage.setOnClickListener {
            showImagePicker(intentLauncher)
        }
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.navHeaderImage,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
package com.devphics.vidde.activities
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.devphics.vidde.BuildConfig
import com.devphics.vidde.R
import com.devphics.vidde.databinding.ActivityMainBinding
import com.devphics.vidde.fragments.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding:ActivityMainBinding
    private lateinit var come_frmvideactiy: String
    var toggle: ActionBarDrawerToggle? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        come_frmvideactiy = intent.getStringExtra("videoactivty").toString()
        if (come_frmvideactiy.equals("yes")){
            supportFragmentManager.beginTransaction()
            .add(android.R.id.content,
                MyDownloadsFragment()
            ).commit()
        startActivity(intent)


        }


        MobileAds.initialize(this){}

        binding.apply {

            setSupportActionBar(toolBar)

            toolBar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00000000"))
            navigationView.setNavigationItemSelectedListener(this@MainActivity)
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawer,
                toolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawer.addDrawerListener(toggle!!)
            toggle!!.syncState()
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.drawer_icon
            )

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    HomeFragment()
                ).commit()
                navigationView.setCheckedItem(R.id.nav_home)
            }
            navigationView.setNavigationItemSelectedListener(this@MainActivity)
        }

        val intent = intent
        val action = intent.action
        val type = intent.type
        if ("android.intent.action.SEND" == action && type != null && "text/plain" == type) {
            Log.println(
                Log.ASSERT,
                "shareablTextExtra",
                intent.getStringExtra("android.intent.extra.TEXT").toString()
            )
            MainActivity()

            shareApp(this)



        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home ->
                supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()
            R.id.nav_status->supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                SavedStatusFragment()
            ).commit()

            R.id.nav_user_downloads->supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                MyDownloadsFragment()
            ).commit()

            R.id.nav_user_share ->
                shareApp(this)
            R.id.nav_about_us->
                supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                fragment_about_us()
            ).commit()
            R.id.nav_logOut-> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                fragment_terms_conditions()
            ).commit()
            R.id.nav_TandC->supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                DStudio()
            ).commit()



        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            HomeFragment()
        ).commit()
        binding.navigationView.setCheckedItem(R.id.nav_home)
        binding.drawer.isDrawerOpen(GravityCompat.START)
        binding.drawer.closeDrawer(GravityCompat.START)



        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("Are you sure want to do exit?")
        builder.setCancelable(true)
        builder.setNegativeButton(
            "Yes"
        )
        { dialog, which ->finishAffinity() }
        builder.setPositiveButton(
            "No"
        ) { dialog, which -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show()

    }
    private fun shareApp(context: Context) {
        val appPackageName = BuildConfig.APPLICATION_ID
        val appName: String = context.getString(R.string.app_name)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareBodyText = "https://play.google.com/store/apps/details?id=" +
                appPackageName
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
        context.startActivity(Intent.createChooser(shareIntent,"Share With"))
    }

    fun aboutApp(){
        val aboutUsFragment = fragment_about_us()
        val termsFragment = fragment_terms_conditions()
        val dtNewFragment = DT_New()

    }
    fun showCustomAlert() {


        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_layout, null)

        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()

        val btDismiss = dialogView.findViewById<Button>(R.id.btDismissCustomDialog)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
    }


}
package com.nemislimus.tratometr.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.databinding.ActivityMainBinding
import com.nemislimus.tratometr.expenses.ui.fragment.model.AutoCompleteItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var items2: List<AutoCompleteItem>
    val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = fragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        items2 = mutableListOf(
            AutoCompleteItem("Апельсин", R.drawable.ic_custom_cat_01, false),
            AutoCompleteItem("Ананас", R.drawable.ic_custom_cat_02, false),
            AutoCompleteItem("Абрикос", R.drawable.ic_custom_cat_03, false),
            AutoCompleteItem("Брюква", R.drawable.ic_custom_cat_04, false),
            AutoCompleteItem("Картошка", R.drawable.ic_custom_cat_05, false),
            AutoCompleteItem("Морковка", R.drawable.ic_custom_cat_06, false),
            AutoCompleteItem("Свекла", R.drawable.ic_custom_cat_07,false),
            AutoCompleteItem("Шпинат", R.drawable.ic_custom_cat_08,false),
            AutoCompleteItem("Горошек совсем зеленый", R.drawable.ic_custom_cat_09,false),
            AutoCompleteItem("Арбуз", R.drawable.ic_custom_cat_10,false),
        )


    }
}

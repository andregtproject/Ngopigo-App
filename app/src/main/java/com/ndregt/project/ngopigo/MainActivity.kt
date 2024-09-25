package com.ndregt.project.ngopigo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {
    private lateinit var rvCoffee: RecyclerView
    private lateinit var catAll: TextView
    private lateinit var catHot: TextView
    private lateinit var catCold: TextView
    private lateinit var iconAll: ImageView
    private lateinit var iconHot: ImageView
    private lateinit var iconCold: ImageView
    private lateinit var allCategory: ConstraintLayout
    private lateinit var hotCategory: ConstraintLayout
    private lateinit var coldCategory: ConstraintLayout
    private lateinit var aboutPage: ImageView
    private lateinit var searchView: SearchView
    private val list = ArrayList<Coffee>()
    private var filteredList = ArrayList<Coffee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)

        rvCoffee = findViewById(R.id.rv_coffee)
        rvCoffee.setHasFixedSize(true)

        catAll = findViewById(R.id.cat_all)
        catHot = findViewById(R.id.cat_hot)
        catCold = findViewById(R.id.cat_cold)
        iconAll = findViewById(R.id.icon_all)
        iconHot = findViewById(R.id.icon_hot)
        iconCold = findViewById(R.id.icon_cold)
        allCategory = findViewById(R.id.all_category)
        hotCategory = findViewById(R.id.hot_category)
        coldCategory = findViewById(R.id.cold_category)

        aboutPage = findViewById(R.id.about_page)

        list.addAll(getListCoffee())
        filteredList.addAll(list)
        showRecyclerList()

        setupCategoryListeners()
        setupMenu()
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterCoffeeList(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterCoffeeList(newText)
                return true
            }
        })
    }

    private fun setupMenu(){
        aboutPage.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCategoryListeners() {
        allCategory.setOnClickListener {
            filterCategory("All")
            updateCategorySelection(allCategory)
        }
        hotCategory.setOnClickListener {
            filterCategory("Hot")
            updateCategorySelection(hotCategory)
        }
        coldCategory.setOnClickListener {
            filterCategory("Cold")
            updateCategorySelection(coldCategory)
        }
    }

    private fun updateCategorySelection(selectedCategory: ConstraintLayout) {
        val categories = listOf(
            Triple(allCategory, catAll, iconAll),
            Triple(hotCategory, catHot, iconHot),
            Triple(coldCategory, catCold, iconCold)
        )
        val activeColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        val inactiveColor = ContextCompat.getColor(this, R.color.light_gray)
        val roundedBackground = ContextCompat.getDrawable(this, R.drawable.rounded_background)

        categories.forEach { (categoryLayout, categoryText, categoryIcon) ->
            if (categoryLayout == selectedCategory) {
                categoryLayout.background = roundedBackground
                categoryText.setTextColor(activeColor)
                categoryIcon.setColorFilter(activeColor)
            } else {
                categoryLayout.background = null
                categoryText.setTextColor(inactiveColor)
                categoryIcon.setColorFilter(inactiveColor)
            }
        }
    }

    private fun filterCoffeeList(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(list)
        } else {
            filteredList.addAll(list.stream()
                .filter { (name): Coffee ->
                    name.contains(query)
                }
                .collect(Collectors.toList()))
        }
        showRecyclerList()
    }

    private fun filterCategory(category: String) {
        filteredList.clear()
        if (category == "All") {
            filteredList.addAll(list)
        } else {
            filteredList.addAll(list.filter { it.category == category })
        }
        showRecyclerList()
    }


    private fun getListCoffee(): ArrayList<Coffee> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataCategory = resources.getStringArray(R.array.data_category)
        val dataIngredients = resources.getStringArray(R.array.data_ingredients)
        val dataTools = resources.getStringArray(R.array.data_tools)
        val dataServingSteps = resources.getStringArray(R.array.data_serving_steps)
        val listCoffee = ArrayList<Coffee>()
        for (i in dataName.indices) {
            val coffee = Coffee(
                dataName[i],
                dataDescription[i],
                dataCategory[i],
                dataPhoto.getResourceId(i, -1),
                dataIngredients[i],
                dataTools[i],
                dataServingSteps[i]
            )
            listCoffee.add(coffee)
        }
        return listCoffee
    }

    private fun showRecyclerList() {
        rvCoffee.layoutManager = GridLayoutManager(this, 2)
        val listCoffeeAdapter = ListCoffeeAdapter(filteredList)
        rvCoffee.adapter = listCoffeeAdapter

        listCoffeeAdapter.setOnItemClickCallback(object : ListCoffeeAdapter.OnItemClickCallback {
            override fun onDetailClicked(data: Coffee) {
                showSelectedCoffee(data)
            }
        })
    }

    private fun showSelectedCoffee(coffee: Coffee) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_COFFEE, coffee)
        startActivity(intent)
    }
}
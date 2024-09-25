package com.ndregt.project.ngopigo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var listCoffeeAdapter: ListCoffeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        rvCoffee = findViewById(R.id.rv_coffee)
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

        setupRecyclerView()
        loadCoffeeData()
        setupCategoryListeners()
        setupMenu()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        rvCoffee.setHasFixedSize(true)
        rvCoffee.layoutManager = GridLayoutManager(this, 2)
        listCoffeeAdapter = ListCoffeeAdapter(filteredList)
        rvCoffee.adapter = listCoffeeAdapter

        listCoffeeAdapter.setOnItemClickCallback(object : ListCoffeeAdapter.OnItemClickCallback {
            override fun onDetailClicked(data: Coffee) {
                showSelectedCoffee(data)
            }
        })
    }

    private fun loadCoffeeData() {
        lifecycleScope.launch(Dispatchers.Default) {
            val coffeeList = getListCoffee()
            withContext(Dispatchers.Main) {
                list.addAll(coffeeList)
                filteredList.addAll(list)
                listCoffeeAdapter.notifyDataSetChanged()
            }
        }
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

    private fun setupMenu() {
        aboutPage.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
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
        lifecycleScope.launch(Dispatchers.Default) {
            val filtered = if (query.isEmpty()) {
                ArrayList(list)
            } else {
                list.filter { it.name.contains(query, ignoreCase = true) }
            }
            withContext(Dispatchers.Main) {
                filteredList.clear()
                filteredList.addAll(filtered)
                listCoffeeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun filterCategory(category: String) {
        lifecycleScope.launch(Dispatchers.Default) {
            val filtered = if (category == "All") {
                ArrayList(list)
            } else {
                list.filter { it.category == category }
            }
            withContext(Dispatchers.Main) {
                filteredList.clear()
                filteredList.addAll(filtered)
                listCoffeeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getListCoffee(): ArrayList<Coffee> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataCategory = resources.getStringArray(R.array.data_category)
        val dataIngredients = resources.getStringArray(R.array.data_ingredients)
        val dataTools = resources.getStringArray(R.array.data_tools)
        val dataServingSteps = resources.getStringArray(R.array.data_serving_steps)

        return ArrayList(dataName.indices.map { i ->
            Coffee(
                dataName[i],
                dataDescription[i],
                dataCategory[i],
                dataPhoto.getResourceId(i, -1),
                dataIngredients[i],
                dataTools[i],
                dataServingSteps[i]
            )
        }).also {
            dataPhoto.recycle()
        }
    }

    private fun showSelectedCoffee(coffee: Coffee) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_COFFEE, coffee)
        }
        startActivity(intent)
    }
}
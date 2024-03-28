package com.example.collegeapp.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.collegeapp.itemview.FacultyItemView
import com.example.collegeapp.navigation.Routes
import com.example.collegeapp.viewmodel.FacultyViewModel


@Composable

fun Faculty(navController: NavController) {

    val facultyViewModel: FacultyViewModel = viewModel ()
    val categoryList by facultyViewModel.categoryList.observeAsState(null)
    facultyViewModel.getCategory()


    LazyColumn {
        items(categoryList ?: emptyList()) { it->
            FacultyItemView(it, delete = { id->
                facultyViewModel.deleteCategory(id)
            }, onClick={categoryName->
                val routes = Routes.FacultyDetailsScreen.route
                    .replace("{category_name}", categoryName)
                navController.navigate(routes)
            })
        }
    }
}

@Preview
@Composable
private fun FacultyPreview() {
    Faculty(rememberNavController())
}
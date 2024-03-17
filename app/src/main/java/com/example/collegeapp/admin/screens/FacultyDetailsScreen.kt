package com.example.collegeapp.admin.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.collegeapp.itemview.TeacherItemView
import com.example.collegeapp.ui.theme.Purple40
import com.example.collegeapp.viewmodel.FacultyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyDetailsScreen(navController: NavController, catName: String) {

    val context = LocalContext.current

    val facultyViewModel: FacultyViewModel = viewModel ()

    val facultyList by facultyViewModel.facultyList.observeAsState(null)

    facultyViewModel.getFaculty(catName)

    // May be these 4 lines may contains error.
    val isDeleted by facultyViewModel.isDeleted.observeAsState(false)
    LaunchedEffect(isDeleted) {
        if(isDeleted) {
            Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {
                Text(
                    text = catName,
                    color = Color.White
                )
            },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Purple40),

                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                })
        }
    ) { padding ->
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier.padding(padding)) {
            items(facultyList?: emptyList()) {it->
                TeacherItemView(facultyModel = it, delete = {facultyModel->
                    facultyViewModel.deleteFaculty(facultyModel)
                })
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//private fun FacultyDetailsScreenPreview() {
//    FacultyDetailsScreen(rememberNavController(), catName = "Hello")
//}
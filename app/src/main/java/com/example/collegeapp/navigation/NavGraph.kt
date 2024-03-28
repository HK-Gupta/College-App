package com.example.collegeapp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collegeapp.admin.screens.AdminDashboard
import com.example.collegeapp.admin.screens.AdminSignup
import com.example.collegeapp.admin.screens.FacultyDetailsScreen
import com.example.collegeapp.admin.screens.ManageBanner
import com.example.collegeapp.admin.screens.ManageCollegeInfo
import com.example.collegeapp.admin.screens.ManageFaculty
import com.example.collegeapp.admin.screens.ManageGallery
import com.example.collegeapp.admin.screens.ManageNotice
import com.example.collegeapp.screens.AboutUs
import com.example.collegeapp.screens.BottomNav
import com.example.collegeapp.screens.Faculty
import com.example.collegeapp.screens.Gallery
import com.example.collegeapp.screens.Home
import com.example.collegeapp.utils.Constants.CATEGORY
import com.example.collegeapp.utils.Constants.IS_ADMIN


@Composable
fun NavGraph(navController: NavHostController) {



    NavHost(
        navController = navController,
        startDestination =
            if(IS_ADMIN)
                Routes.AdminDashboard.route
            else
                Routes.BottomNav.route
    ) {

        composable(Routes.BottomNav.route) {
            BottomNav(navController)
        }
        composable(Routes.AboutUs.route) {
            AboutUs()
        }
        composable(Routes.Faculty.route) {
            Faculty(navController)
        }
        composable(Routes.Gallery.route) {
            Gallery()
        }
        composable(Routes.Home.route) {
            Home()
        }
        composable(Routes.AdminDashboard.route) {
            AdminDashboard(navController)
        }
        composable(Routes.ManageBanner.route) {
            ManageBanner(navController)
        }
        composable(Routes.ManageCollegeInfo.route) {
            ManageCollegeInfo(navController)
        }
        composable(Routes.ManageFaculty.route) {
            ManageFaculty(navController)
        }
        composable(Routes.ManageGallery.route) {
            ManageGallery(navController)
        }
        composable(Routes.ManageNotice.route) {
            ManageNotice(navController)
        }
        composable(Routes.FacultyDetailsScreen.route) {it->
            val data = it.arguments!!.getString(CATEGORY)

            FacultyDetailsScreen(navController, data!!)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NavGraphPreview() {
    NavGraph(navController = rememberNavController())
}
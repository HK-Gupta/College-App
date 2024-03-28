package com.example.collegeapp.screens

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.collegeapp.R
import com.example.collegeapp.admin.screens.AdminDashboard
import com.example.collegeapp.models.BottomNavItem
import com.example.collegeapp.models.NavItem
import com.example.collegeapp.navigation.Routes
import kotlinx.coroutines.launch

//navHostController: NavHostController
//@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navHostController: NavHostController) {

    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val list = listOf(
        NavItem("Website", Icons.Rounded.Language),
        NavItem("Notice", Icons.Rounded.StickyNote2),
        NavItem("Notes", Icons.Rounded.EditNote),
        NavItem("Contact Us", Icons.Rounded.ContactMail)
    )

    ModalNavigationDrawer(
        drawerState=drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = null,
                    modifier = Modifier.height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.size(7.dp))
                Divider()
                Text(text = "")

                list.forEachIndexed { index, item->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = (index==selectedItemIndex),
                        onClick = {
                            Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        },
        content = {
            // Used to display app bar
            Scaffold (
                topBar = {
                    TopAppBar(title = { 
                        Text(text = "College App")
                    },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(imageVector = Icons.Rounded.Menu,
                                    contentDescription = null
                                )
                            }
                        })
                },
                bottomBar = {
                    com.example.collegeapp.screens.MyBottomNav(navController = navController)
                }
            ){padding->
                NavHost(
                    navController = navController,
                    startDestination = Routes.Home.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(route = Routes.Home.route) {
                        com.example.collegeapp.screens.Home()
                    }
                    composable(Routes.AboutUs.route) {
                        com.example.collegeapp.screens.AboutUs()
                    }
                    composable(Routes.Faculty.route) {
                        com.example.collegeapp.screens.Faculty(navHostController)
                    }
                    composable(Routes.Gallery.route) {
                        com.example.collegeapp.screens.Gallery()
                    }
                }

            }
        })
}


@Composable
fun MyBottomNav(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Home",
                Icons.Rounded.Home,
                Routes.Home.route
        ),
        BottomNavItem(
            "Faculty",
            Icons.Rounded.Person,
            Routes.Faculty.route
        ),
        BottomNavItem(
            "Gallery",
            Icons.Rounded.Photo,
            Routes.Gallery.route
        ),
        BottomNavItem(
            "About",
            Icons.Rounded.Info,
            Routes.AboutUs.route
        )
    )

    BottomAppBar {
        list.forEach {

            val curRoute = it.routes
            val otherRoute =
                try {
                    backStackEntry.value!!.destination.route
                } catch (e: Exception) {
                    Routes.Home.route
                }

            val selected = (curRoute==otherRoute)

            NavigationBarItem(
                selected = selected,
                onClick = {
                          navController.navigate(it.routes){
                              popUpTo(navController.graph.findStartDestination().id){
                                  saveState = true
                              }
                              launchSingleTop = true
                          }
                },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title,
                        modifier = Modifier.size(24.dp))
                })

        }
    }
}

@Preview (showSystemUi = true)
@Composable
fun Preview() {
    com.example.collegeapp.screens.BottomNav(navHostController = rememberNavController())
}
package com.example.collegeapp.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.collegeapp.R
import com.example.collegeapp.itemview.BannerItemView
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.ui.theme.Purple40
import com.example.collegeapp.utils.Constants.BANNER
import com.example.collegeapp.viewmodel.BannerViewModel
import com.example.collegeapp.widget.LoadingDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBanner(navController: NavController) {

    val context = LocalContext.current

    val bannerViewModel: BannerViewModel = viewModel ()

    val isUploaded by bannerViewModel.isPosted.observeAsState(false)
    val isDeleted by bannerViewModel.isDeleted.observeAsState(false)
    val bannerList by bannerViewModel.bannerList.observeAsState(null)

    bannerViewModel.getBanner()

    val showLoader = remember {
        mutableStateOf(false)
    }

    if(showLoader.value) {
        LoadingDialog (
            onDismissRequest = {
                showLoader.value = false
            }
        )
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) {it->
        imageUri = it
    }

    // To know whether the image is Uploaded or not.
    LaunchedEffect(isUploaded) {
        if(isUploaded) {
            showLoader.value = false
            Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
            imageUri = null
        }
    }
    LaunchedEffect(isDeleted) {
        if(isDeleted) {
            Toast.makeText(context, "Image Deleted", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold (
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Manage Banner",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),

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
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                launcher.launch("image/*")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ){padding->
        Column(modifier = Modifier.padding(padding)){

            if (imageUri != null) {
                ElevatedCard(
                    modifier = Modifier.padding(7.dp)
                ) {
                    Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = BANNER,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )

                    Row {
                        Button(
                            onClick = {
                                showLoader.value = true
                                bannerViewModel.saveImage(imageUri!!)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Add Image")
                        }
                        OutlinedButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
            LazyColumn {
                items(bannerList ?: emptyList()) { it: BannerModel ->
                    BannerItemView(
                        bannerModel = it,
                        delete = {docId->
                            bannerViewModel.deleteBanner(docId)
                        })
                }
            }
        }
    }
}


@Preview
@Composable
fun ManageBannerView() {
    ManageBanner(rememberNavController())
}
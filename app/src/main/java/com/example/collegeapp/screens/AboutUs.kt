package com.example.collegeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.collegeapp.ui.theme.SMALL_TEXT
import com.example.collegeapp.ui.theme.TITLE_SIZE
import com.example.collegeapp.viewmodel.CollegeInfoViewModel

@Composable
fun AboutUs() {

    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()
    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)
    collegeInfoViewModel.getCollegeInfo()

    Column (Modifier.padding(8.dp)){
        if(collegeInfo != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Image(painter = rememberAsyncImagePainter(model = collegeInfo!!.imageUrl!!),
                contentDescription = "College Logo",
                modifier = Modifier.height(200.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = collegeInfo!!.name!!,
                fontWeight = FontWeight.Bold,
                fontSize = TITLE_SIZE
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = collegeInfo!!.desc!!,
                fontWeight = FontWeight.SemiBold,
                fontSize = SMALL_TEXT
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = collegeInfo!!.address!!,
                fontWeight = FontWeight.SemiBold,
                fontSize = SMALL_TEXT
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = collegeInfo!!.websiteLink!!,
                color = Color.Blue,
                fontSize = SMALL_TEXT
            )
        }

    }
}
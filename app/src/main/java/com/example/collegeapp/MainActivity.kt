package com.example.collegeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.collegeapp.admin.screens.AdminLogin
import com.example.collegeapp.admin.screens.AdminSignup
import com.example.collegeapp.navigation.NavGraph
import com.example.collegeapp.navigation.Routes
import com.example.collegeapp.ui.theme.CollegeAppTheme
import com.example.collegeapp.ui.theme.SkyBlue
import com.example.collegeapp.utils.Constants.IS_ADMIN


var canNavigateUser by mutableStateOf(false)
var canNavigateAdmin by mutableStateOf(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollegeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(!canNavigateAdmin && !canNavigateUser)
                        MainActivityPreview()
                    if (canNavigateUser)
                        NavGraph(navController = rememberNavController())
                    if(canNavigateAdmin)
                        AdminLogin()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MainActivityPreview() {

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))


        Text (
            text = ("IIIT KALYANI"),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.image),
            contentDescription = null,
            modifier = Modifier.size(320.dp)
        )

        Text(
            text = "Vidya Dharma Sarva Dhanam Pradhanam",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            IS_ADMIN = true
            canNavigateAdmin = true
        },
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)


        ) {
            Text(text = "Admin", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            IS_ADMIN = false
            canNavigateUser = true
        },
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Text(text = "Student", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(130.dp))
        Text(
            text = "Developed by\n Harsh Kumar",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }


}


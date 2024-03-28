package com.example.collegeapp.admin.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.collegeapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.foundation.layout.Column as Column1
import androidx.navigation.compose.rememberNavController as rememberNavController1
import com.example.collegeapp.navigation.NavGraph as NavGraph1

private lateinit var auth: FirebaseAuth
private var login by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun AdminLogin() {

    auth = Firebase.auth
    val context = LocalContext.current


    var gotoSignup by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (login || auth.currentUser!=null) {
        NavGraph1(navController = rememberNavController1())
    }
    if(gotoSignup) {
        AdminSignup()
    }

    if (!gotoSignup && !login && auth.currentUser==null) {

        ConstraintLayout (
            Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            val (column1, column2) = createRefs()
            Column1(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .constrainAs(column1) {
                        top.linkTo(parent.top)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))


                Text(
                    text = ("IIIT KALYANI"),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    text = "Vidya Dharma Sarva Dhanam Pradhanam",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))


                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    placeholder = {
                        Text(text = "Enter Email Id")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = {
                        Text(text = "Enter the Password")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        loginUserAccount(email, password, context)
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)

                ) {
                    Text(text = "Login", fontSize = 20.sp)
                }

            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 25.dp)
                    .fillMaxWidth()
                    .constrainAs(column2) {
                        bottom.linkTo(parent.bottom)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "New User")
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    onClick = {
                        gotoSignup = true
                    }
                ) {
                    Text(
                        text = "Click to Sign Up !",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }

}

private fun loginUserAccount(email: String, password: String, context: Context) {
    if(email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context , "Kindly Fill all the fields", Toast.LENGTH_SHORT).show()
    } else {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Toast.makeText(context, "Account Created Successfully.", Toast.LENGTH_SHORT).show()
                login = true
            } else {
                Toast.makeText(context, "Something went wrong \nPlease Try Again", Toast.LENGTH_LONG).show()
            }
        }
    }

}

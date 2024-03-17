package com.example.collegeapp.itemview

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.collegeapp.R
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.models.FacultyModel
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.ui.theme.SMALL_TEXT
import com.example.collegeapp.ui.theme.SkyBlue
import com.example.collegeapp.ui.theme.TITLE_SIZE
import com.example.collegeapp.utils.Constants

@Composable
fun TeacherItemView(
    facultyModel: FacultyModel,
    delete: (facultyModel: FacultyModel)->Unit
) {

    OutlinedCard (
        modifier = Modifier.padding(4.dp)
    ){
        ConstraintLayout {
            val (image, delete) = createRefs()

            Column (horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = facultyModel.imageUrl),
                    contentDescription = Constants.NOTICE,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(CircleShape),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = facultyModel.name!!,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontWeight = FontWeight.Black,
                    fontSize = TITLE_SIZE
                )
                Text(
                    text = facultyModel.email!!,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontSize = SMALL_TEXT,
                    color = SkyBlue
                )
                Text(
                    text = facultyModel.position!!,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontSize = SMALL_TEXT,
                    color = SkyBlue
                )


            }

            Card (
                modifier = Modifier
                    .constrainAs(delete) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(7.dp)
                    .clickable {
                        delete(facultyModel)
                    }
            ){
                Image(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(7.dp),
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun BItemViewPreview() {
////    BannerItemView(bannerModel = BannerModel())
//}


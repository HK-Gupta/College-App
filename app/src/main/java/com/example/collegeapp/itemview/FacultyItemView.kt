package com.example.collegeapp.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
import com.example.collegeapp.ui.theme.TEXT_SIZE
import com.example.collegeapp.ui.theme.TITLE_SIZE
import com.example.collegeapp.utils.Constants.IS_ADMIN

@Composable
fun FacultyItemView(
    category_name: String,
    delete: (category_name: String)->Unit,
    onClick: (category_name: String)->Unit
) {
    OutlinedCard (
        modifier = Modifier.padding(4.dp).fillMaxWidth()
            .clickable {
                onClick(category_name)
            }
    ){
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (category, delete) = createRefs()

            Text(
                text = category_name,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .constrainAs(
                        category
                    ) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    },
                fontWeight = FontWeight.Black,
                fontSize = TEXT_SIZE
            )

            if (IS_ADMIN) {
                Card(
                    modifier = Modifier
                        .constrainAs(delete) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(7.dp)
                        .clickable {
                            delete(category_name)
                        }
                ) {
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
}




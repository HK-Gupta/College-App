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
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.ui.theme.SMALL_TEXT
import com.example.collegeapp.ui.theme.SkyBlue
import com.example.collegeapp.ui.theme.TITLE_SIZE
import com.example.collegeapp.utils.Constants.IS_ADMIN

@Composable
fun NoticeItemView(
    noticeModel: NoticeModel,
    delete: (noticeModel: NoticeModel)->Unit
) {
    OutlinedCard (
        modifier = Modifier.padding(4.dp)
    ){
        ConstraintLayout {
            val (image, delete) = createRefs()

            Column {
                Text(
                    text = noticeModel.title!!,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontWeight = FontWeight.Black,
                    fontSize = TITLE_SIZE
                )
                if(noticeModel.link != "") {
                    Text(
                        text = noticeModel.link!!,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        fontSize = SMALL_TEXT,
                        color = SkyBlue
                    )
                }
                Image(
                    painter = rememberAsyncImagePainter(model = noticeModel.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

            }

            if(IS_ADMIN) {
                Card(
                    modifier = Modifier
                        .constrainAs(delete) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(7.dp)
                        .clickable {
                            delete(noticeModel)
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

//@Preview(showSystemUi = true)
//@Composable
//fun BItemViewPreview() {
////    BannerItemView(bannerModel = BannerModel())
//}


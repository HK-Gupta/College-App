package com.example.collegeapp.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import com.example.collegeapp.models.GalleryModel
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.ui.theme.SMALL_TEXT
import com.example.collegeapp.ui.theme.SkyBlue
import com.example.collegeapp.ui.theme.TEXT_SIZE
import com.example.collegeapp.ui.theme.TITLE_SIZE

@Composable
fun GalleryItemView(
    galleryModel: GalleryModel,
    delete: (galleryModel: GalleryModel)->Unit,
    deleteImage: (category: String, image: String)->Unit
) {
    OutlinedCard (
        modifier = Modifier.padding(4.dp).fillMaxWidth()
    ){
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (category, delete) = createRefs()

            Text(
                text = galleryModel.category!!,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 7.dp)
                    .constrainAs(
                        category
                    ) {
                        start.linkTo(parent.start)
                        end.linkTo(delete.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    },
                fontWeight = FontWeight.Black,
                fontSize = TEXT_SIZE
            )

            Card (
                modifier = Modifier
                    .constrainAs(delete) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(7.dp)
                    .clickable {
                        delete(galleryModel)
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

        LazyRow() {
            items(galleryModel.images?: emptyList()) {it->
                ImageItemView(imageUrl=it, cat=galleryModel.category!!, delete = { category:String, imageUrl->
                    deleteImage( category, imageUrl)
                })
            }
        }
    }
}




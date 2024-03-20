package com.example.collegeapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.collegeapp.itemview.NoticeItemView
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.ui.theme.SMALL_TEXT
import com.example.collegeapp.ui.theme.TITLE_SIZE
import com.example.collegeapp.viewmodel.BannerViewModel
import com.example.collegeapp.viewmodel.CollegeInfoViewModel
import com.example.collegeapp.viewmodel.NoticeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalPagerApi::class)
@Preview(showSystemUi = true)
@Composable
fun Home() {

    val bannerViewModel: BannerViewModel = viewModel ()
    val bannerList by bannerViewModel.bannerList.observeAsState(null)
    bannerViewModel.getBanner()

    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()
    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)
    collegeInfoViewModel.getCollegeInfo()

    val noticeViewModel: NoticeViewModel = viewModel ()
    val noticeList by noticeViewModel.noticeList.observeAsState(null)
    noticeViewModel.getNotice()

    val pagerState = com.google.accompanist.pager.rememberPagerState (0)
    
    val imageSlider = ArrayList<AsyncImagePainter> ()

    if(bannerList != null) {
        bannerList!!.forEach {
            imageSlider.add(rememberAsyncImagePainter(model = it.url))
        }
    }

    LaunchedEffect(Unit ) {
        try {
            while (true) {
                yield()
                delay(2000)
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % pagerState.pageCount
                )
            }
        } catch (e: Exception) { }
    }

    LazyColumn (Modifier.padding(8.dp)){
        item {
            HorizontalPager(
                count = imageSlider.size,
                state = pagerState
            ) {pager->
                Card (Modifier.height(220.dp)){
                    Image(
                        painter = imageSlider[pager],
                        contentDescription = "Banner",
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillParentMaxWidth()
            ){
                HorizontalPagerIndicator(pagerState = pagerState, Modifier.padding(8.dp))
            }
        }

        item {
            if(collegeInfo != null) {
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

                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Notices",
                    fontWeight = FontWeight.Bold,
                    fontSize = TITLE_SIZE
                )
                Spacer(modifier = Modifier.height(7.dp))
            }


        }

        items(noticeList ?: emptyList()) { it: NoticeModel ->
            NoticeItemView(
                noticeModel = it,
                delete = { docId ->
                    noticeViewModel.deleteNotice(docId)
                })
        }
    }
}
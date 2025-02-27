package com.example.csx4109_542_assignment_3_6612054.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun GalleryScreen(imageList : List<String>, modifier: Modifier,
                  imageClicked : (image : String) -> Unit){

    LazyVerticalGrid(GridCells.Fixed(2), modifier = modifier.padding(16.dp)) {
        items(imageList){ image ->
            val imageHeight = (image
                .substringAfterLast("/")
                .substringBeforeLast(".jpg")
                .toInt() * 2.5).dp

            val adjustedHeight = if (imageHeight < 150.dp) 160.dp else imageHeight

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .height(adjustedHeight)
                    .clickable { imageClicked(image) },
                elevation = CardDefaults.cardElevation(6.dp),
                ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun ImageDetailScreen(image : String, modifier : Modifier){
    AsyncImage(model = image,
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black))
}
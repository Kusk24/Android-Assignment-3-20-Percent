package com.example.csx4109_542_assignment_3_6612054.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csx4109_542_assignment_3_6612054.model.Person
import com.example.csx4109_542_assignment_3_6612054.model.PersonEntity
import com.example.csx4109_542_assignment_3_6612054.viewModel.PersonViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonListScreen(modifier : Modifier, personEntityList: List<PersonEntity>,
                     loadPersonClicked : () -> Unit,
                     viewGalleryClicked : () -> Unit,
                     deleteClicked : (image : String) -> Unit,
                     saveClicked : (image : String) -> Unit ) {

    val groupedNames = personEntityList.groupBy{ it.first.first() }.toSortedMap()

    Column(modifier.padding(16.dp)) {
        LazyColumn(modifier = modifier.weight(1f)) {
            groupedNames.forEach { groupedName ->
                stickyHeader {
                    Surface(modifier = Modifier.padding(top = 16.dp)) {
                        Text("Alplabet: ${groupedName.key}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                items(groupedName.value) { item ->

                    var offsetX = remember(item) { mutableStateOf(0f) }
                    val leftThreshold = -50f  // Threshold to snap left
                    val rightThreshold = 50f  // Threshold to snap right
                    val maxLeftSwipe = -120f  // Maximum left swipe (reveal Delete)
                    val maxRightSwipe = 120f  // Maximum right swipe (reveal Save)

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(bottom = 16.dp)) {

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .background(Color.LightGray)){
                            Button(onClick = {
                                saveClicked(item.large)
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green,
                                contentColor = Color.Black),
                                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start)
                                    .weight(1f)){
                                Text("Save")
                            }

                            Button(onClick = {
                                deleteClicked(item.large)
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red,
                                contentColor = Color.White),
                                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
                                    .weight(1f)){
                                Text("Delete")
                            }
                        }

                        //using z-index or putting last put this over everything in box
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .offset(offsetX.value.dp)
                                .background(color = Color.White)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            offsetX.value = (offsetX.value + dragAmount).coerceIn(
                                                maxLeftSwipe,
                                                maxRightSwipe
                                            )
                                        },
                                        onDragEnd = {
                                            when {
                                                offsetX.value < leftThreshold -> {
                                                    offsetX.value = maxLeftSwipe
                                                }
                                                offsetX.value > rightThreshold -> {
                                                    offsetX.value = maxRightSwipe
                                                }
                                                else -> {
                                                    offsetX.value = 0f
                                                }
                                            }
                                        }
                                    )
                                }
                        ) {
                            Text(
                                "${item.title} ${item.first} ${item.last}",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxHeight()
                                    .wrapContentHeight(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                loadPersonClicked()
            }, modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Load Person", modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = {
                viewGalleryClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text("View Gallery", modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}
package com.example.csx4109_542_assignment_3_6612054.view

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.csx4109_542_assignment_3_6612054.model.Person
import com.example.csx4109_542_assignment_3_6612054.viewModel.PersonViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainAppScreen(modifier : Modifier){

    val personViewModel: PersonViewModel = viewModel()
    val personEntityList = personViewModel.personEntityList.observeAsState()
    val imageList = personViewModel.imageList.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "PersonList"
        ){


        composable("PersonList"){
            PersonListScreen(
                modifier, personEntityList.value.orEmpty(),
                loadPersonClicked = {personViewModel.loadPerson()},
                viewGalleryClicked = { navController.navigate("Gallery")},
                saveClicked = {personViewModel.saveImage(it)},
                deleteClicked = {personViewModel.deleteImage(it)}
            )}

        composable("Gallery") {
            GalleryScreen(
                imageList.value,
                modifier,
                imageClicked = { image ->
                    val encodedImage = Uri.encode(image) // Encode URL properly
                    navController.navigate("ImageDetail/$encodedImage")
                }
            )
        }

        composable(
            "ImageDetail/{image}",
            arguments = listOf(navArgument("image") { type = NavType.StringType })
        ) { backStackEntry ->
            val image = backStackEntry.arguments?.getString("image")?.let { Uri.decode(it) } ?: ""
            ImageDetailScreen(
                image = image,
                modifier = modifier
            )
        }
        
    }

}
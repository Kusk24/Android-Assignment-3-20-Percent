package com.example.csx4109_542_assignment_3_6612054

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.csx4109_542_assignment_3_6612054.dao.PersonEntityDao
import com.example.csx4109_542_assignment_3_6612054.database.PersonEntityDatabase
import com.example.csx4109_542_assignment_3_6612054.ui.theme.CSX4109_542_Assignment_3_6612054Theme
import com.example.csx4109_542_assignment_3_6612054.view.MainAppScreen
import com.example.csx4109_542_assignment_3_6612054.view.PersonListScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSX4109_542_Assignment_3_6612054Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

}

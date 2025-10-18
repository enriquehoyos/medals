package pe.com.medals.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.com.medals.R
import pe.com.medals.ui.theme.LightGray
import pe.com.medals.ui.theme.*
import pe.com.medals.viewmodel.ProfileViewModel

/**
 * Created by Quique on 10/17/2025.
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    var showMedals by remember { mutableStateOf(false) }
    val vm: ProfileViewModel = hiltViewModel()

    Scaffold(
        content = {
            if (showMedals) {
                MedalsScreen(
                    vm,
                    onBack = { showMedals = false }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.medals),
                                color = SkyBlue
                            ) { showMedals = true }
                        }
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.mission),
                                color = Red
                            ) { }
                        }
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.rachas),
                                color = Orange
                            ) { }
                        }
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.album),
                                color = LightGray
                            ) { }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ButtonScreen(section: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = section,
            color = Black,
            fontSize = 16.sp
        )
    }
}

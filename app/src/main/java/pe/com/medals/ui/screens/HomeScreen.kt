package pe.com.medals.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // Reset con 5 taps
    var taps by remember { mutableStateOf(0) }
    var lastTap by remember { mutableStateOf(0L) }
    val tapWindow = 1200L

    fun onHeaderTap() {
        val now = System.currentTimeMillis()
        taps = if (now - lastTap <= tapWindow) taps + 1 else 1
        lastTap = now
        if (taps >= 5) {
            scope.launch { vm.resetAll() }
            taps = 0
        }
    }
    LaunchedEffect(Unit) {
        vm.resetEvent.collect {
            Toast.makeText(context, "Se han reiniciado los niveles", Toast.LENGTH_SHORT).show()
        }
    }
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
                            Profile("Prueba Enrique Hoyos") {
                                onHeaderTap()
                            }
                            Spacer(Modifier.height(20.dp))
                        }
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
                            ) {
                                Toast.makeText(
                                    context,
                                    "Módulo en construcción",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.rachas),
                                color = Orange
                            ) {
                                Toast.makeText(
                                    context,
                                    "Módulo en construcción",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        item {
                            ButtonScreen(
                                section = stringResource(id = R.string.album),
                                color = LightGray
                            ) {
                                Toast.makeText(
                                    context,
                                    "Módulo en construcción",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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

@Composable
fun Profile(
    userName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_profile_circle),
            contentDescription = "Perfil de usuario",
            tint = Red,
            modifier = Modifier.size(70.dp)
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${userName}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}
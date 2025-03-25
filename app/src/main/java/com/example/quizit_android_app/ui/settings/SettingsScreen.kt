package com.example.quizit_android_app.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.network.NetworkMonitor
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    networkMonitor: NetworkMonitor = hiltViewModel(),
    onLogout: () -> Unit
) {
    val isConnected = networkMonitor.isConnected
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val user: User? = viewModel.user
    val isLoading: Boolean = viewModel.isLoading
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {

            DeleteUserBottomSheet(
                onCancel = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                onDelete = {
                    if(isConnected) {
                        viewModel.deleteUser()
                        onLogout()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = SnackbarDuration.Short)
                            sheetState.hide()
                        }
                    }
                }
            )

        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Settings", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(
                        Alignment.CenterHorizontally))

                }


            },
            content = { paddingValues->

                if(isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator( modifier = Modifier.align(Alignment.Center), trackColor = Color.Gray)
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(top = 8.dp, bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {

                        item {
                            Spacer(modifier = Modifier.size(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Color(0xFFEAF2FF), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Icon",
                                    tint = Color(0xFFB4DBFF),
                                    modifier = Modifier.size(70.dp)
                                )
                            }

                            Spacer(modifier = Modifier.size(8.dp))

                            Text(
                                user?.userFullname ?: "",
                                style = MaterialTheme.typography.titleLarge,

                                )
                            Spacer(modifier = Modifier.size(8.dp))

                            Text(user?.userMail ?: "", style = MaterialTheme.typography.titleMedium, color = Color(0xFF71727A))

                            Spacer(modifier = Modifier.size(32.dp))

                        }

                        item {
                            SelectYearItem(userYear = user?.userYear ?: 0, onYearSelected = {
                                if(isConnected) {
                                    viewModel.updateUserYear(it)
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Keine Internetverbindung", "OK", duration = SnackbarDuration.Short)
                                    }

                                }
                            })
                        }

                        item {

                            SettingsListItem(title = "Kontaktiere uns", imageVector = Icons.Default.Mail, onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:khoeher@tgm.ac.at"))
                                context.startActivity(intent)
                            })

                        }

                        item {
                            SettingsListItem(title = "Über uns", imageVector = Icons.Outlined.Info, onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://projekte.tgm.ac.at/quizit"))
                                context.startActivity(intent)

                            })
                        }

                        item {
                            SettingsListItem(
                                title = "Abmelden",
                                imageVector = Icons.Default.Logout,
                                onClick = {
                                    viewModel.logOut()
                                    onLogout()
                                }
                            )
                        }

                        item {

                            Spacer(modifier = Modifier.size(24.dp))
                            DeleteUserItem(
                                onClick = {
                                    coroutineScope.launch {
                                        sheetState.show()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        item {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Column(
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.logo_lightmode),
                                        contentDescription = "QuizIT Logo",
                                        modifier = Modifier
                                            .width(75.dp)
                                            .aspectRatio(975f / 337f)
                                            .align(Alignment.CenterHorizontally),
                                        contentScale = ContentScale.FillBounds
                                    )

                                    Spacer(modifier = Modifier.height(32.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.just_do_it),
                                        contentDescription = "IT Logo",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .aspectRatio(719f / 223f)
                                            .align(Alignment.CenterHorizontally),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }

                            }

                        }

                    }

                }
            }
        )
    }
}

@Composable
fun SelectYearItem(userYear: Int, onYearSelected: (Int) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(userYear) }
    var dropdownWidth by remember { mutableStateOf(0) } // Speichert die Breite der zweiten Row

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            .background(
                shape = if (expanded) RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 0.dp
                ) else RoundedCornerShape(16.dp),
                color = Color(0xFFF8F9FE)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    color = Color(0xFFEAF2FF)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Box als Anker für das DropdownMenu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    dropdownWidth = coordinates.size.width
                }
                .clickable { expanded = !expanded }
                .offset(x = -16.dp, y = 16.dp),

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .offset(y = -16.dp)
                ) {
                    Text("Jahrgang auswählen", style = MaterialTheme.typography.bodyMedium)
                    Text(userYear.toString() + "xHIT", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF71727A))
                }

                if(expanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Forward Icon",
                        tint = Color(0xFF8F9098),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .offset(y = -16.dp)
                        ,
                    )

                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Forward Icon",
                        tint = Color(0xFF8F9098),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .offset(y = -16.dp)
                    )
                }


            }

            val availableYears = listOf(1, 2, 3, 4, 5)

            // DropdownMenu mit gemessener Breite
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { dropdownWidth.toDp() + 16.dp }),
                containerColor = Color(0xFFF8F9FE),
                shape = RoundedCornerShape(bottomStart= 16.dp, bottomEnd = 16.dp),
                // Dropdown auf die Breite der Box setzen
            ) {
                availableYears.forEach { year ->
                    DropdownMenuItem(
                        onClick = {
                            selectedYear = year
                            onYearSelected(year)
                            expanded = false
                        },
                        text = {
                            Text(text = year.toString()+"xHIT", style = MaterialTheme.typography.bodyMedium)
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun SettingsListItem(title: String, imageVector: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            .clickable(onClick = { onClick() })
            .background(shape = RoundedCornerShape(16.dp), color = Color(0xFFF8F9FE)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    color = Color(0xFFEAF2FF)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterHorizontally),
            )


        }
        Spacer(modifier = Modifier.size(16.dp))

        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End

        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Forward Icon",
                tint = Color(0xFF8F9098)
            )
        }
    }
}

@Composable
fun DeleteUserItem(onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            .clickable(onClick = { onClick() })
            .background(shape = RoundedCornerShape(16.dp), color = Color(0xFFF8F9FE)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                    color = Color(0xFFEAF2FF)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = Icons.Default.NoAccounts,
                contentDescription = "Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterHorizontally),
            )


        }
        Spacer(modifier = Modifier.size(16.dp))

        Column {
            Text("Account löschen", style = MaterialTheme.typography.bodyMedium)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End

        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Forward Icon",
                tint = Color(0xFF8F9098)
            )
        }
    }

}

@Composable
fun DeleteUserBottomSheet(onCancel: () -> Unit, onDelete: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFF8F9FE), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Text("Account löschen", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.size(16.dp))
            Text("Bist du sicher, dass du deinen Account löschen möchtest? Alle mit dem Account verknüpften Daten werden gelöscht. ", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onCancel() }) {
                    Text("Abbrechen", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                }
                TextButton(onClick = { onDelete() }) {
                    Text("Löschen", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
                }
            }
        }
    }


}
package com.example.quizit_android_app.ui.settings

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerDefaults.shape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.R


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        content = { paddingValues->
            Column(
                modifier = Modifier
                    .padding(paddingValues).padding(top = 16.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text("Settings", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(
                    Alignment.CenterHorizontally))

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
                    "Timo Enzi",
                    style = MaterialTheme.typography.titleLarge,

                )
                Spacer(modifier = Modifier.size(8.dp))

                Text("tenzi@student.tgm.ac.at", style = MaterialTheme.typography.titleMedium, color = Color(0xFF71727A))

                Spacer(modifier = Modifier.size(32.dp))

                SelectYearItem(userYear = 5)

                SettingsListItem(title = "Kontaktiere uns", imageVector = Icons.Default.Mail, onClick = {})

                SettingsListItem(title = "Über uns", imageVector = Icons.Outlined.Info, onClick = {})
                
                SettingsListItem(
                    title = "Abmelden",
                    imageVector = Icons.Default.Logout,
                    onClick = { viewModel.logOut() }
                )



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
                                .aspectRatio(975f/337f)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.FillBounds
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        Image(
                            painter = painterResource(id = R.drawable.just_do_it),
                            contentDescription = "IT Logo",
                            modifier = Modifier
                                .width(100.dp)
                                .aspectRatio(719f/223f)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                }


           }
        }
    )
}

@Composable
fun SelectYearItem(userYear: Int) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            .background(shape = RoundedCornerShape(16.dp), color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .background(shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp), color = Color(0xFFEAF2FF))
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

        Column {
            Text("Jahrgang auswählen", style = MaterialTheme.typography.bodyMedium)
            Text(userYear.toString()+"xHIT", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF71727A))
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
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
fun SettingsListItem(title: String, imageVector: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            .clickable(onClick = {onClick()}),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp), color = Color(0xFFEAF2FF))
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
            modifier = Modifier.fillMaxWidth(),
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
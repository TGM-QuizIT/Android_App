package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizit_android_app.ui.theme.Typography
import kotlin.Result

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    onGoBack: () -> Unit
) {

    val userResults = viewModel.userResults.value
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(onClick = { onGoBack()}) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Social",
                                style = Typography.titleLarge
                            )
                        }
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(52.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        content = { paddingValues->

            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Text(
                        "Julian Stoll",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("4BHIT", style = MaterialTheme.typography.bodyMedium)



                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009DE0),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(170.dp),
                        shape = RoundedCornerShape(16.dp)

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "befreundet",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }

                    }



                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(170.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = "Add",
                                modifier = Modifier
                                    .size(20.dp)
                            )

                            Text(
                                "anfreunden",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(170.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEAF2FF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color.Black)

                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PendingActions,
                                contentDescription = "Wait",
                                modifier = Modifier
                                    .size(20.dp)
                            )


                            Text(
                                "ausstehend",
                                style = MaterialTheme.typography.titleMedium,
                            )

                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text("Julians Herausforderungen", style = MaterialTheme.typography.titleMedium)



                    Spacer(modifier = Modifier.size(16.dp))

                    LazyRow{
                        items(userResults) {
                            ResultCard(result = it)
                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    StatisticsCard()
                }
            }
        }
    )
}
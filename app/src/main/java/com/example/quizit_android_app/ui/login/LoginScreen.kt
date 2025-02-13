package com.example.quizit_android_app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.R
import com.example.quizit_android_app.navigation.AppNavGraph
import com.example.quizit_android_app.ui.MainScreen

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {

    var isLogInSuccess = viewModel.isLogInSuccess.value

    if(isLogInSuccess){
        onLoginSuccess()
    }

    else {
        var username = viewModel.username.value
        var password = viewModel.password.value
        var passwordVisible = viewModel.passwordVisibility.value

        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {},
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()

                ) {

                    Spacer(modifier = Modifier.height(150.dp))
                    Text(
                        text = "Willkommen zurück!",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )


                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "mit TGM-Username einloggen!",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(

                        value = username,
                        onValueChange = { viewModel.setUsername(it) },
                        placeholder = { Text("mmmustermann") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        colors = TextFieldDefaults.colors(

                            unfocusedContainerColor = Color(0xFFf2f2f2),
                            focusedContainerColor = Color(0xFFf2f2f2),

                            )

                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    TextField(
                        value = password,
                        onValueChange = { viewModel.setPassword(it) },
                        placeholder = { Text("Passwort") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { viewModel.setPasswordVisibility() }) {
                                Icon(image, contentDescription = "Passwort anzeigen/verstecken")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFf2f2f2), focusedContainerColor = Color(0xFFf2f2f2))
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE5E5E5))

                    Spacer(modifier = Modifier.height(32.dp))


                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .height(50.dp)
                            .width(175.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009DE0))
                    ) {
                        Text("Einloggen", color = Color.White, style= MaterialTheme.typography.titleMedium)
                    }

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
                                    .width(125.dp)
                                    .aspectRatio(975f / 337f)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.FillBounds
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Image(
                                painter = painterResource(id = R.drawable.just_do_it),
                                contentDescription = "IT Logo",
                                modifier = Modifier
                                    .width(150.dp)
                                    .aspectRatio(719f / 223f)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        Spacer(modifier = Modifier.height(64.dp))
                    }

                }
            }
        )

    }
}
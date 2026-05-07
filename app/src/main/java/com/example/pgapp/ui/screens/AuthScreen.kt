package com.example.pgapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pgapp.R
import com.example.pgapp.navigation.NavRoutes
import com.example.pgapp.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {

    var isLogin by remember { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val formattedName =

        name.trim()
            .split("\\s+".toRegex())
            .joinToString(" ") {

                it.replaceFirstChar { char ->
                    char.uppercase()
                }
            }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val isLoggedIn = viewModel.isLoggedIn.value

    // SUCCESS NAVIGATION
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(NavRoutes.EXPLORE) {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // HEADER
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            if (isLogin) "Welcome back" else "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            if (isLogin)
                "Find your perfect PG."
            else
                "Start your journey with us",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        // FORM CARD
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ) {
            if (!isLogin) {

                AuthTextField(
                    "Full Name",
                    Icons.Default.Android,
                    name
                ) { name = it }

                Spacer(Modifier.height(12.dp))
            }

            AuthTextField("Email Address", Icons.Default.Email, email) {
                email = it
            }

            Spacer(Modifier.height(12.dp))

            AuthTextField("Password", Icons.Default.Lock, password, true) {
                password = it
            }

            if (!isLogin) {
                Spacer(Modifier.height(12.dp))
                AuthTextField("Confirm Password", Icons.Default.Lock, confirmPassword, true) {
                    confirmPassword = it
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isLogin) {
                Text(
                    "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(Modifier.height(16.dp))

            // BUTTON
            Button(
                onClick = {
                    if (isLogin) {
                        viewModel.login(email, password)
                    } else {
                        if (password == confirmPassword) {
                            viewModel.signUp(formattedName, email, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isLogin) "Login" else "Sign Up")
                }
            }

            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text(" OR ")
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            Row {

                SocialButton(

                    "Google",

                    painterResource(R.drawable.google),

                    Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                SocialButton(

                    "Facebook",

                    painterResource(R.drawable.facebook),

                    Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            if (isLogin) "Don't have an account yet?" else "Already have an account?"
        )

        OutlinedButton(
            onClick = { isLogin = !isLogin },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (isLogin) "Sign Up" else "Login")
        }
    }
}

@Composable
fun AuthTextField(
    label: String,
    icon: ImageVector,
    value: String,
    isPassword: Boolean = false,
    onChange: (String) -> Unit,
) {

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(

        value = value,

        onValueChange = onChange,

        singleLine = true,

        textStyle = MaterialTheme.typography.bodyMedium,

        placeholder = {
            Text(label)
        },

        leadingIcon = {

            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },

        trailingIcon = {

            if (isPassword) {

                IconButton(
                    onClick = {

                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Icon(

                        if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,

                        contentDescription = null,

                        tint =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },

        visualTransformation =

            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,

        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),

        shape = RoundedCornerShape(18.dp),

        colors = OutlinedTextFieldDefaults.colors(

            focusedBorderColor =
                MaterialTheme.colorScheme.primary,

            unfocusedBorderColor =
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),

            focusedContainerColor =
                MaterialTheme.colorScheme.surface,

            unfocusedContainerColor =
                MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun SocialButton(
    text: String,
    icon: Painter,
    modifier: Modifier,
) {

    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            Toast.makeText(context, "$text login coming soon 🚀", Toast.LENGTH_SHORT).show()
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(text)
    }
}
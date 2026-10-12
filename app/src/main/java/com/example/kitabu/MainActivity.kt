package com.example.kitabu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookingEntity
import com.example.kitabu.ui.theme.KitabuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KitabuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var currentScreen by remember { mutableStateOf("login") }
                    var userBookings by remember { mutableStateOf<List<Pair<BookingEntity, BookEntity>>>(emptyList()) }

                    when (currentScreen) {
                        "login" -> {
                            LoginScreen(
                                onLoginClick = {
                                    currentScreen = "home"
                                },
                                onRegisterClick = {},
                            )
                        }
                        "home" -> {
                            HomePage(
                                onReserveClick = { book ->
                                    val newBooking = BookingEntity(
                                        id = "b_${System.currentTimeMillis()}",
                                        bookId = book.id,
                                        bookingDate = System.currentTimeMillis(),
                                        returnDeadline = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L),
                                    )
                                    userBookings += Pair(newBooking, book.copy(isAvailable = false))
                                    currentScreen = "reservations"
                                },
                            )
                        }
                        "reservations" -> {
                            ReservationsDashboard(
                                bookings = userBookings.ifEmpty { defaultSampleBookings },
                                onBackToCatalog = {
                                    currentScreen = "home"
                                },
                                onRenew = { booking ->
                                    userBookings = userBookings.map { (b, book) ->
                                        if (b.id == booking.id) {
                                            Pair(b.copy(returnDeadline = b.returnDeadline + (7 * 24 * 60 * 60 * 1000L)), book)
                                        } else {
                                            Pair(b, book)
                                        }
                                    }
                                },
                                onReturn = { booking ->
                                    userBookings = userBookings.filter { (b, _) -> b.id != booking.id }
                                },
                                onCancel = { booking ->
                                    userBookings = userBookings.filter { (b, _) -> b.id != booking.id }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private val defaultSampleBookings = listOf(
    Pair(
        BookingEntity(
            id = "b1",
            bookId = "1",
            bookingDate = System.currentTimeMillis(),
            returnDeadline = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L),
        ),
        BookEntity(
            id = "1",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            category = "Fiction",
            isAvailable = false,
        ),
    ),
)

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Kitabu Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(top = 32.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title below logo
        Text(
            text = "Kitabu",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username input
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot password link
        Text(
            text = "Forgot Password?",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {},
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login button (2 times the size of the Register button)
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register button (half the size of the Login button)
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(28.dp),
            contentPadding = PaddingValues(vertical = 0.dp),
        ) {
            Text("Register", fontSize = 12.sp)
        }
    }
}

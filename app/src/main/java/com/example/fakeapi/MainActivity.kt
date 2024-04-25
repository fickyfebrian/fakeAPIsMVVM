package com.example.fakeapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fakeapi.data.ApiInterface
import com.example.fakeapi.models.Posts
import com.example.fakeapi.ui.theme.FakeAPITheme
import com.example.fakeapi.utils.RetrofitInstance
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : ComponentActivity() {
    private val api : ApiInterface by lazy {
        RetrofitInstance.api
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeAPITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(api = api)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp( api: ApiInterface ) {
    var scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var post by remember{ mutableStateOf<List<Posts>>(emptyList()) }

    LaunchedEffect(Unit){
        coroutineScope.launch {
            fetchPosts(api)?.let { post = it }
        }
    }
    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                Text(
                    "Posts",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = {
            PostsList(posts = post)
        }

    )

}

@Composable
fun PostsList(posts: List<Posts>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ){
        items(posts){ posts ->
            PostItem(posts)
        }
    }

}

@Composable
fun PostItem( posts: Posts) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "ID: ${posts.id}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Title: ${posts.title}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Body: ${posts.body}", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

}
suspend fun fetchPosts(api: ApiInterface): List<Posts>? {
    return try {
        api.getPost()
    } catch (e: Exception) {
        // Tindakan tambahan yang mungkin dilakukan
        // Misalnya, mencatat pesan kesalahan atau menampilkan pesan kesalahan kepada pengguna
        Log.e("FetchPosts", "Error fetching posts", e)
        null // Kembalikan null atau nilai khusus lainnya untuk menunjukkan kesalahan
    }
}

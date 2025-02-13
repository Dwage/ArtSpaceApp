package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import android.view.View
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as ComponentActivity).window
                        WindowCompat.setDecorFitsSystemWindows(window, false)

                        window.decorView.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

data class Artwork(
    val imageResId: Int,
    val title: String,
    val artist: String,
    val year: String
)

@Composable
fun ArtSpaceApp() {
    var currentArtworkIndex by rememberSaveable { mutableIntStateOf(0) }
    println("ArtSpaceApp recomposed")

    val artworks = listOf(
        Artwork(R.drawable.image1, "Windsor Castle", "Wyatt Simpson", "2023"),
        Artwork(R.drawable.image2, "Coliseum", "Mauricio Artieda", "2017"),
        Artwork(R.drawable.image3, "Taj Mahal", "Annie Spratt", "2018"),
        Artwork(R.drawable.image4, "Norwich Cathedral", "Andy Bridge", "2024"),
        Artwork(R.drawable.image5, "Albert Hall Museum", "Martijn Vonk", "2024"),
        Artwork(R.drawable.image6, "Sydney Opera House", "Matt Hardy", "2018"),
        Artwork(R.drawable.image7, "Pantheon", "Chris Czermak", "2018"),
        Artwork(R.drawable.image8, "Wells Cathedral", "Michael D Beckwith", "2024"),
        Artwork(R.drawable.image9, "The Louvre Museum", "Kylie Paz", "2018"),
        Artwork(R.drawable.image10, "Munich Residenz", "Bernd Dittrich", "2024")
    )

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE


    if(isLandscape) {
        LandscapeLayout(artworks, currentArtworkIndex, {index -> currentArtworkIndex = index})
    } else {
        PortraitLayout(artworks, currentArtworkIndex, { index -> currentArtworkIndex = index })
    }


}
@Composable
fun PortraitLayout(artworks: List<Artwork>, currentArtworkIndex: Int, onArtworkChange: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArtworkDisplay(
            artwork = artworks[currentArtworkIndex],
            modifier = Modifier.weight(1f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtworkDescriptor(artwork = artworks[currentArtworkIndex])
            DisplayController(
                currentArtworkIndex = currentArtworkIndex,
                totalArtworks = artworks.size,
                onArtworkChange = onArtworkChange
            )
        }
    }
}

@Composable
fun LandscapeLayout(artworks: List<Artwork>, currentArtworkIndex: Int, onArtworkChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArtworkDisplay(
            artwork = artworks[currentArtworkIndex],
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            ArtworkDescriptor(artwork = artworks[currentArtworkIndex])
            Spacer(modifier = Modifier.height(16.dp))
            DisplayController(
                currentArtworkIndex = currentArtworkIndex,
                totalArtworks = artworks.size,
                onArtworkChange = onArtworkChange
            )
        }
    }
}

@Composable
fun ArtworkDisplay(artwork: Artwork, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(4.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(id = artwork.imageResId),
            contentDescription = artwork.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .border(2.dp, Color.Gray)
                .padding(16.dp)

        )
    }
}

@Composable
fun ArtworkDescriptor(artwork: Artwork) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = artwork.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "${artwork.artist} (${artwork.year})",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DisplayController(
    currentArtworkIndex: Int,
    totalArtworks: Int,
    onArtworkChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                val newIndex = if (currentArtworkIndex == 0) totalArtworks - 1 else currentArtworkIndex - 1
                onArtworkChange(newIndex)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Previous")
        }

        Button(
            onClick = {
                val newIndex = (currentArtworkIndex + 1) % totalArtworks
                onArtworkChange(newIndex)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}
package dev.rhyme.introduction.ui

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.introduction.R
import dev.rhyme.introduction.ui.destinations.MoreInfoPageDestination
import dev.rhyme.introduction.ui.theme.IntroductionTheme
import dev.rhyme.introduction.util.Constants
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val PAGE_COUNT = 5

@OptIn(ExperimentalPagerApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun MainContent(
    navigator: DestinationsNavigator
) {
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()

    val pagerState = rememberPagerState()

    val colors: List<Color> = rememberSaveable(
        PAGE_COUNT,
        saver = listSaver(
            save = { list ->
                list.map { it.value.toLong() }
            }, restore = { list ->
                list.map { Color(it.toULong()) }
            }
        )
    ) {
        buildList {
            repeat(PAGE_COUNT) {
                add(
                    Color(
                        red = Random.nextInt(256),
                        green = Random.nextInt(256),
                        blue = Random.nextInt(256)
                    )
                )
            }
        }
    }

    val bgColor by animateColorAsState(
        targetValue = colors[pagerState.currentPage]
    )
    val isDark = isDarkColor(bgColor)

    IntroductionTheme(darkTheme = !isDark) {
        val isLight = MaterialTheme.colors.isLight
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = isLight,
                isNavigationBarContrastEnforced = false
            )
        }

        Scaffold(
            backgroundColor = bgColor,
            contentColor = MaterialTheme.colors.onSurface
        ) {

            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    count = PAGE_COUNT,
                    state = pagerState
                ) { page ->
                    when (page) {
                        0 -> CoverPage(modifier = Modifier.fillMaxSize())
                        1 -> IntroductionPage(modifier = Modifier.fillMaxSize())
                        2 -> BackgroundPage(modifier = Modifier.fillMaxSize())
                        3 -> WhatsWithAppPage(modifier = Modifier.fillMaxSize())
                        4 -> LastPage(
                            modifier = Modifier.fillMaxSize(),
                            onMoreInfo = {
                                navigator.navigate(MoreInfoPageDestination)
                            }
                        )
                        else -> Box(Modifier.fillMaxSize())
                    }
                }
                BottomNav(pagerState = pagerState)
            }
        }
    }
}

fun isDarkColor(color: Color): Boolean {
    val whiteContrast = ColorUtils.calculateContrast(Color.White.toArgb(), color.toArgb())
    val blackContrast = ColorUtils.calculateContrast(Color.Black.toArgb(), color.toArgb())

    return whiteContrast < blackContrast
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content,
    )
}

@Composable
fun CoverPage(modifier: Modifier = Modifier) {
    Page(
        modifier = modifier,
    ) {
        Text(
            text = "Hi!", style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
        Text(
            text = "It's nice to meet you!", style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
        Text(
            text = "I am Oliver Rhyme G. Añasco",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.oliver),
            contentDescription = "Oliver",
        )
    }
}

@Composable
fun IntroductionPage(
    modifier: Modifier = Modifier
) {
    Page(modifier = modifier) {
        Text(
            "Let me introduce myself!",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.tagoloan_1),
            contentDescription = "Oliver",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Im 21 year old student, Android developer and a future computer engineer. " +
                "I am currently living in Tagoloan, Misamis Oriental which is famous for our tallest standing Christmas tree in the whole Mindanao.",
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun BackgroundPage(
    modifier: Modifier = Modifier,
) {
    Page(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Just a bit of a background", style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "I am a 3rd year BS in Computer Engineering student at " +
                "Mindanao State University - Iligan Institute of Technology."
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "I am also a Cagayan De Oro's Capitol University Senior High School alumnus of the strand STEM - Engineering."
        )
    }
}

@Composable
fun WhatsWithAppPage(
    modifier: Modifier = Modifier
) {
    Page(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "So what's with this app?", style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "I created this app to showcase my greatest passion with programming and creating apps that help people." +
                "I treat programming and technology in general as my go-to \"stress reliever\" " +
                "and I feel I am in my safe space by doing the things I love the most."
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "I am also an Android Developer currently working with Australia's " +
                "top app development company; Appetiser Apps."
        )
    }
}

@Composable
fun LastPage(
    modifier: Modifier = Modifier,
    onMoreInfo: () -> Unit
) {
    Page(modifier = modifier) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(16.dp)
                    .offset(y = (-4).dp),
                painter = painterResource(id = R.drawable.msu_iit),
                contentDescription = "MSU-IIT",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
//            modifier = Modifier.align(Alignment.Start),
                text = "Thank you very much for listening!",
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Submitted by: Añasco, Oliver Rhyme G.")
            Text(text = "Submitted to: Haim, Stephen H.")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "COE100 W1", fontStyle = FontStyle.Italic)

        }

        val context = LocalContext.current

//        Button(
//            onClick = onMoreInfo,
//        ) {
//            Text(text = "More Info")
//        }

        OutlinedButton(
            onClick = {
                val tabsIntent = CustomTabsIntent.Builder().build()
                tabsIntent.launchUrl(context, Uri.parse(Constants.SOURCE_CODE_URL))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.onSurface,
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_github),
                contentDescription = "GitHub"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Source Code")
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    pagerState: PagerState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val shouldShowBack by remember {
            derivedStateOf {
                pagerState.currentPage != 0
            }
        }

        val shouldShowForward by remember {
            derivedStateOf {
                pagerState.currentPage != pagerState.pageCount - 1
            }
        }

        val animationScope = rememberCoroutineScope()

        val backAlpha: Float by animateFloatAsState(if (shouldShowBack) 1f else 0f)
        val forwardAlpha: Float by animateFloatAsState(if (shouldShowForward) 1f else 0f)

        IconButton(
            modifier = Modifier.alpha(backAlpha),
            enabled = shouldShowBack,
            onClick = {
                animationScope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage - 1
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Backward"
            )
        }

        HorizontalPagerIndicator(
            modifier = Modifier.padding(16.dp),
            pagerState = pagerState
        )

        IconButton(
            modifier = Modifier.alpha(forwardAlpha),
            enabled = shouldShowForward,
            onClick = {
                animationScope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Forward"
            )
        }
    }
}
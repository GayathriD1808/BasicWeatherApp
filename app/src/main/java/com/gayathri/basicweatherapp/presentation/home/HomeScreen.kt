package com.gayathri.basicweatherapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gayathri.basicweatherapp.R
import com.gayathri.basicweatherapp.data.model.Main
import com.gayathri.basicweatherapp.data.model.Weather
import com.gayathri.basicweatherapp.ui.theme.BasicWeatherAppTheme
import com.gayathri.basicweatherapp.utils.Constants.GEO_BASE_URL
import com.gayathri.basicweatherapp.viewmodel.WeatherViewModel

@Composable
fun HomeScreen() {
    val viewModel: WeatherViewModel = hiltViewModel()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    HomeScreenBody(
        state = homeScreenState,
        onSearchClicked = { viewModel.searchCities(it) }
    )
}

@Composable
fun HomeScreenBody(
    state: HomeScreenState,
    onSearchClicked: (String) -> Unit
) {

    if (state != null) {
        WeatherInfo(
            weatherInfo = state,
            onSearchClicked = onSearchClicked
        )
    }
}

@Composable
fun WeatherInfo(
    weatherInfo: HomeScreenState,
    onSearchClicked: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = painterResource(id = R.color.teal_200),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                LocationSearch(onSearchClicked)
                Spacer(modifier = Modifier.padding(top = 32.dp))
                weatherInfo.main?.let {
                    weatherInfo.weatherInfo?.let { it1 ->
                        CurrentTemperature(
                            it.temp,
                            24,
                            it.temp_max,
                            it.temp_max,
                            it1.icon
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = 32.dp))
            }
        }
    }
}

@Composable
fun LocationSearch(
    onSearchClicked: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(id = R.string.lbl_cd_location)
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                onSearchClicked(searchText)
                keyboardController?.hide()
            }),
            singleLine = true,
            value = searchText,
            onValueChange = {
                searchText = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            label = { Text("Location(lat, long)") }
        )
        IconButton(
            onClick = {
                keyboardController?.hide()
                onSearchClicked(searchText)
            },
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.lbl_cd_search)
            )
        }
    }
}

@Composable
fun CurrentTemperature(
    currentTemp: Double,
    currentFeelsLike: Int,
    dailyHigh: Double,
    dailyLow: Double,
    weatherCode: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        val textColor = MaterialTheme.colorScheme.onPrimaryContainer


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {

            Text(
                text = stringResource(R.string.lbl_now),
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )

            ConstraintLayout {
                val (currentTempText, currentTempIcon) = createRefs()

                Text(
                    text = "$currentTemp°",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        fontSize = 64.sp
                    ),
                    color = textColor,
                    modifier = Modifier.constrainAs(currentTempText) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Box(
                    modifier = Modifier
                        .constrainAs(currentTempIcon) {
                            top.linkTo(parent.top)
                            start.linkTo(currentTempText.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(String.format(GEO_BASE_URL,weatherCode))
                            .build(),
                        contentDescription = weatherCode,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }

            }

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = String.format(
                    stringResource(R.string.lbl_current_temp_format),
                    dailyHigh,
                    dailyLow
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BasicWeatherAppTheme {
        Column(Modifier.fillMaxSize()) {
            WeatherInfo(
                weatherInfo = HomeScreenState(
                    weatherInfo = Weather(icon = "01d", description = "Clear Sky"),
                    main = Main( 198.2, 198.2, 198.2),
                    cityName = "New York"
                ),
                onSearchClicked = {}
            )
        }
    }
}
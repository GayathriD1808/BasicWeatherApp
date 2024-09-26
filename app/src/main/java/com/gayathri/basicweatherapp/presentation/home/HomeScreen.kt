package com.gayathri.basicweatherapp.presentation.home

import android.widget.Toast
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
import com.gayathri.basicweatherapp.data.model.WeatherData
import com.gayathri.basicweatherapp.ui.theme.BasicWeatherAppTheme
import com.gayathri.basicweatherapp.R
import com.gayathri.basicweatherapp.utils.Constants
import com.gayathri.basicweatherapp.presentation.ErrorView
import com.gayathri.basicweatherapp.presentation.LoadingView
import com.gayathri.basicweatherapp.viewmodel.HomeViewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    HomeScreenBody(
        state = homeScreenState,
        onSearchClicked = { viewModel.searchCityAndGetWeather(it) },
        onTextChanged = { viewModel.onTextChanged(it) },
        onRetry = { viewModel.onRetry() }
    )
}

@Composable
fun HomeScreenBody(
    state: HomeScreenState,
    onSearchClicked: (String) -> Unit,
    onTextChanged: (String) -> Unit,
    onRetry: () -> Unit
) {

    if (state.weatherInfo != null) {
        WeatherInfo(
            weatherInfo = state.weatherInfo,
            searchedText = state.searchedText,
            isValidSearch = state.isValidSearch,
            onTextChanged = onTextChanged,
            onSearchClicked = onSearchClicked
        )
    }

    if (state.error != null) {
        val context = LocalContext.current
        if (state.needRetryScreen) {
            ErrorView(errorMessage = state.error.asString(), onRetry = onRetry)
        } else {
            Toast.makeText(context, state.error.asString(), Toast.LENGTH_LONG).show()
        }
    }

    if (state.isLoading) {
        LoadingView()
    }
}

@Composable
fun WeatherInfo(
    weatherInfo: WeatherData,
    searchedText: String,
    isValidSearch: Boolean,
    onTextChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
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
                LocationSearch(isValidSearch, onTextChanged, onSearchClicked)
                Spacer(modifier = Modifier.padding(top = 32.dp))
                CurrentTemperature(
                    name = weatherInfo.name,
                    currentTemp = weatherInfo.temp.toInt(),
                    description = weatherInfo.description,
                    dailyHigh = weatherInfo.temp_max.toInt(),
                    dailyLow = weatherInfo.temp_min.toInt(),
                    weatherCode = weatherInfo.icon
                )
                Spacer(modifier = Modifier.padding(top = 32.dp))
            }
        }
    }
}

@Composable
fun LocationSearch(
    isValidSearch: Boolean,
    onTextChanged: (String) -> Unit,
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
                onTextChanged(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            label = { Text("Enter City") }
        )
        IconButton(
            onClick = {
                keyboardController?.hide()
                onSearchClicked(searchText)
            },
            enabled = isValidSearch,
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = if (isValidSearch) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.4f
                ),
                contentDescription = stringResource(id = R.string.lbl_cd_search)
            )
        }
    }
}

@Composable
fun CurrentTemperature(
    name: String,
    currentTemp: Int,
    description: String,
    dailyHigh: Int,
    dailyLow: Int,
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
                            .data(String.format(Constants.ICON_URL,weatherCode))
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

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = description,
                style = MaterialTheme.typography.bodySmall,
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
                weatherInfo = WeatherData(
                    name = "New York",
                    temp = 20.0f,
                    description = "Cloudy",
                    temp_max = 25.0f,
                    temp_min = 15.0f,
                    icon = "01d"
                ),
                searchedText = "",
                isValidSearch = false,
                onTextChanged = {},
                onSearchClicked = {}
            )
        }
    }
}
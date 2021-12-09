/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

/*
* PlantDetailDescription의 parameter로 plantDetailViewModel을 넣은 이유?
* ViewModel을 사용하여 데이터를 로드한 뒤 보여줄텐데
* 이때 ViewModel을 사용할 수 없거나, 해당 종속성을 컴포저블에 전달하고 싶지 않은 경우에는
* 컴포저블 내에서 viewModel 함수를 사용하여 ViewModel의 인스턴스를 가져올 수 있기 때문이다.
* 필주님의 영상에서도 나왔지만 뷰모델은 기존에 뷰모델을 반환하거나 지정된 스코프 내에서 새 뷰모델을 만들수도 있는 메서드이다.
* 예를들어 컴포저블이 액티비티에서 사용되는 경우에 뷰모델은 액티비티가 완료되거나 프로세스가 종료되기 전까지 동일한 인스턴스를 반환한다.
* 따라서 몇 개의 메서드에서는 같은 액티비티에서 사용된다면 뷰모델은 LifeCycleOwner 즉, 액티비티/프래그먼트가 살아있는 동안에 동일한 인스턴스를 반환하게 된다.
* */
@Composable
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    // observeAsState는 LiveData를 관찰하고 State를 통하여 값을 표현한다. (state == value == data)
    // 이때, LiveData에 새로운 값이 관찰되어 emit될 때 마다 반환된 State가 업데이트되고 모든 State.value사용이 recomposition 된다.
    val plant by plantDetailViewModel.plant.observeAsState()
    plant?.let {
        // null일 수 있어서 let으로 wrapping하는건 알겠는데, LiveData의 소비를 나누고 다른 컴포저블에서 수신하는 것이 좋은 패턴이라는게 무슨 의미일까?
        // 그러한 목적으로 아래에 PlantDetailContent 컴포저블을 만든다.
        PlantDetailContent(it)
    }
}

@Composable
fun PlantDetailContent(plant: Plant) {
    Surface {
        Column(Modifier.padding(dimensionResource(id = R.dimen.margin_normal))) {
            PlantName(plant.name)
            PlantWatering(wateringInterval = plant.wateringInterval)
            PlantDescription(description = plant.description)
        }
    }
}

@Composable
private fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth() // AndroidView에서의 match_parent
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally) // Text를 수평으로 정렬
    )
}

@Preview
@Composable
private fun PlantNamePreview() {
    val plant = Plant("id", "Apple", "description", 3, 30, "")
    MaterialTheme {
        PlantDetailContent(plant)
    }
}

@Composable
private fun PlantWatering(wateringInterval: Int) {
    Column(Modifier.fillMaxWidth()) {
        val centerWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)

        val normalPadding = dimensionResource(R.dimen.margin_normal)

        Text(
            text = stringResource(R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = centerWithPaddingModifier.padding(top = normalPadding)
        )

        // 수량화된 텍스트를 가져오기 위해 getQuantityString()사용
        // 그러나 현재 dimension에서 수량화된 문자열을 가지고 오지 않기 때문에
        // LocalContext.current.resources 를 사용하여 가져온다.
        val wateringIntervalText = LocalContext.current.resources.getQuantityString(
            R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
        )
        Text(
            text = wateringIntervalText,
            modifier = centerWithPaddingModifier.padding(bottom = normalPadding)
        )
    }
}

@Preview
@Composable
private fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(wateringInterval = 7)
    }
}

@Composable
private fun PlantDescription(description: String) {

    val htmlDescription = remember(description) { // 새로운 description이 들어오면 recomposition 된다.
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            // update되면 AndroidView를 recomposition하게 되고 텍스트를 업데이트 한다.
            it.text = htmlDescription
        }
    )
}


@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "HTML<br><br>description", 3, 30, "")
    MaterialTheme {
        PlantDetailContent(plant)
    }
}

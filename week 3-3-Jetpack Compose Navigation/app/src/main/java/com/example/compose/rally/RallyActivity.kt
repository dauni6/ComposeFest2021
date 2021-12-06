/*
 * Copyright 2021 The Android Open Source Project
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

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.accounts.SingleAccountBody
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState() // 백스택을 사용하기 위한 state
        val currentScreen = RallyScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name) // tab 클릭시 navigating
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost( // NavHost를 정의했으므로 네비게이션 그래프를 정의할 수 있다.
        navController = navController,
        startDestination = RallyScreen.Overview.name, // startDestination이 반드시 필요
        modifier = modifier
    ) {
        // 여기서 NavHost가 이동할 수 있는 목적지들은 다른 목적지들을 수용할 준비가 되었다.
        // 이를 NavGraphBuilder를 통해 수행한다.

        // OverviewBody로 가는 목적지
        composable(RallyScreen.Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                onAccountClick = { name ->
                    navigateToSingleAccount(navController, name)
                }
            )
        }
        // AccountsBody로 가는 목적지
        composable(RallyScreen.Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingleAccount(
                    navController = navController,
                    accountName = name
                )
            }
        }
        // BillsBody로 가는 목적지
        composable(RallyScreen.Bills.name) {
            BillsBody(bills = UserData.bills)
        }

        val accountsName = RallyScreen.Accounts.name
        // SingleAccountBody로 가는 새로운 목적지 추가
        composable(
            "$accountsName/{name}", // 명명된 인자는 {} 중괄호로 묶여서 내부로 제공된다. 또한 변수이름을 이스케이핑 하기위해 $ 기호를 사용한다.
            arguments = listOf(
                navArgument("name") {
                    // Make argument type safe
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://$accountsName/{name}"
            })
        ) { entry -> // NavBackStackEntry의 인자들중 "name"을 검색한다.
            val accountName = entry.arguments?.getString("name")
            // UserData에서 일치하는 첫번째 이름을 찾는다.
            val account = UserData.getAccount(accountName)
            // 계좌를 SingleAccountBody로 전달한다.
            SingleAccountBody(account = account)
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    accountName: String
) {
    navController.navigate("${RallyScreen.Accounts.name}/$accountName")
}


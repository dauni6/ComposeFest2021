package com.codelab.theming.ui.start.theme

import androidx.compose.ui.graphics.Color

/**
 * Compose의 색상은 Color클래스를 사용하여 정의된다.
 * 참고로 색상 지정을 위해 일반적인 ‘#dd0d3c’ 형식으로 부터 변환하려면 ‘#’을 ‘0xff’,
 * 즉 Color(0xffdd0d3c)로 바꿔야한다. 여기서 ‘ff’는 alpha 채널값이며, 완전히 불투명함을 의미한다.
 * */

/** for light theme */
val Red700 = Color(0xffdd0d3c) // Color의 변수명을 primaryRed와 같이 하지말고 Red700과 같이 짓는다.
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffc20029)

/** for dark theme */
val Red200 = Color(0xfff297a2)
val Red300 = Color(0xffea6d7e)
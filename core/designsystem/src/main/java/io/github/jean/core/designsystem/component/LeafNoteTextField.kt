package io.github.jean.core.designsystem.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafNoteTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    textStyle: TextStyle = LeafTheme.typography.note,
    color: Color = LeafTheme.colors.textPrimary,
    enabled: Boolean = true,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.MultiLine(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    BasicTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle.copy(color = color),
        cursorBrush = SolidColor(LeafTheme.colors.primary),
        lineLimits = lineLimits,
        keyboardOptions = keyboardOptions,
        decorator = { innerTextField ->
            Box {
                if (placeholder != null && state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = LeafTheme.colors.textMuted,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
fun LeafNoteTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    textStyle: TextStyle = LeafTheme.typography.note,
    color: Color = LeafTheme.colors.textPrimary,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle.copy(color = color),
        cursorBrush = SolidColor(LeafTheme.colors.primary),
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Box {
                if (placeholder != null && value.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = LeafTheme.colors.textMuted,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Preview(
    name = "1. Light",
    uiMode = UI_MODE_NIGHT_NO,
    heightDp = 2200,
    backgroundColor = 0xFFF5F4EF,
    showBackground = true,
)
@Preview(
    name = "2. Dark",
    uiMode = UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF15181F,
    showBackground = true,
)
@Composable
private fun LeafNoteTextFieldQuotePreview() {
    val mock =
        """
        터널을 지날 때.

        그리스 신화에서 저승까지 찾아가 아내 에우뤼디케를 구해내는데 성공한 오르페우스에겐 반드시 지켜야 할 금기가 주어집니다. 그건 저승을 다 빠져나갈 때까지 절대로 뒤를 돌아봐서는 안 된다는 조건이지요. 그러나 오비디우스의 ‘변신 이야기’ 속 설명에 따르면 “그녀를 보고 싶은 마음에, 그녀가 포기했을까 두려움에 사로잡혀” 그는 그만 뒤를 돌아보고 맙니다. 이로 인해 아내를 데려오는 일은 결국 마지막 순간에 수포로 돌아가고 말지요.

        구약 성서에서 롯의 아내도 그랬습니다. 죄악의 도시 소돔과 고모라가 불로 심판 받을 때 이를 간신히 피해 떠나가다가 신의 명령을 어기고 뒤를 돌아보는 바람에 소금 기둥이 되었으니까요. 금기를 깨고 뒤돌아보았다가 돌이나 소금 기둥이 되는 이야기는 전세계 도처에 널려 있지요. 우리의 경우도 탐욕스런 어느 부자의 집이 물로 심판 받을 때 뒤돌아본 그의 며느리가 바위가 되고 마는 충남 연기의 장자못 전설을 비롯해 조금씩 변형된 형태로 여러 지방에 전해져 내려오니까요. 

        국내 개봉된 일본 영화 중 최고 흥행 성적을 기록한 ‘센과 치히로의 행방불명’에서 치히로 역시 비슷한 상황에 놓입니다. 기상천외한 일들이 벌어지는 신들의 나라에서 돼지가 된 부모를 구출해 돌아가던 소녀 치히로는 바깥 세상으로 나가는 통로에 놓인 터널을 지나는 동안 결코 돌아봐서는 안 된다는 말을 듣는 거지요. 

        그런데 왜 허다한 이야기들에 이런 ‘돌아보지 말 것’에 대한 금기가 원형(原型)처럼 반복되는 걸까요. 그건 혹시 삶에서 지난(至難)했던 한 단계의 마무리는 결국 그 단계를 되짚어 생각하지 않을 때 비로소 완결된다는 것을, 사람들이 경험을 통해 체득하고 있기 때문이 아닐까요. 오르페우스처럼, 그리움 때문이든 두려움 때문이든, 지나온 단계를 되돌아볼 때 그 단계의 찌꺼기는 도돌이표처럼 지루하게 반복될 수 밖에 없는 게 아니겠습니까. 소금 기둥과 며느리 바위는 그 찌꺼기들이 퇴적해 남긴 과거의 퇴층(堆層) 같은 게 아닐까요. 

        류시화 시인은 ‘새는 날아가면서 뒤돌아보지 않는다’라는 시에서 “시를 쓴다는 것이/더구나 나를 뒤돌아본다는 것이/싫었다, 언제나 나를 힘들게 하는 것은/나였다/다시는 세월에 대해 말하지 말자”고 했지요. 정해종 시인도 ‘엑스트라’에서 “그냥 지나가야 한다/말 걸지 말고/뒤돌아보지 말고/모든 필연을/우연으로 가장해야 한다”고 했고요. 

        그런데 의미심장한 것은 치히로가 그 힘든 모험을 마치고 빠져 나오는 통로가 다리가 아닌 터널이었다는 사실입니다. 두 개의 공간을 연결하는 통로엔 다리와 터널이 있겠지요. 다리는 텅 빈 공간에 ‘놓는’ 것이라면, 터널은 (이미 흙이나 암반으로) 꽉 차 있는 공간을 ‘뚫는’ 것입니다. 그래서 다리가 ‘더하기의 통로’라면 터널은 ‘빼기의 통로’라고 할 수 있을 겁니다. 

        결국 삶의 단계들을 지날 때 중요한 것은 얻어낸 것들을 어떻게 한껏 지고 나가느냐가 아니라, 삭제해야 할 것들을 어떻게 훌훌 털어내느냐,인지도 모릅니다. 이제 막 어른이 되기 시작하는 초입을 터널로 지나면서 치히로는 할 수 있는 것과 할 수 없는 것들을 몸으로 익히면서 욕망과 집착을 조금 덜어내는 법을 배웠겠지요.

        박흥식 감독의 영화 ‘나도 아내가 있었으면 좋겠다’에서 사랑이 잘 풀리지 않을 무렵, 윤주는 봉수를 등지고 계단을 오르면서 “뒤돌아보지 마라. 뒤돌아보면 돌이 된다”고 되뇌지만 결국 뒤를 돌아 보지요. 그러나 그렇게 해서 쓸쓸히 확인한 것은 봉수의 부재(不在) 뿐이었습니다. 

        아무리 마음이 아파도 뒤돌아보지 마세요. 정말로 뒤돌아보고 싶다면 터널을 완전히 벗어난 뒤에야 돌아서서 보세요. 치히로가 마침내 부모와 함께 새로운 삶의 단계로 발을 디딜 수 있었던 것은 터널을 통과한 뒤에야 무표정한 얼굴로 그렇게 뒤돌아본 이후가 아니었던가요. _2007. 3. 12
        """.trimIndent()

    LeafTheme {
        LeafNoteTextField(
            state = rememberTextFieldState(initialText = mock),
            textStyle = LeafTheme.typography.quote,
            modifier = Modifier.padding(16.dp),
        )
    }
}

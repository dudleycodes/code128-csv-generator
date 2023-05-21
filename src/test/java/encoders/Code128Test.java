package encoders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

@DisplayName("Code128")
class Code128Test {
    @DisplayName("Encode")
    @Nested
    class EncodedTests {
        @DisplayName("Invalid inputs")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "      ", " \r\n\t     " })
        void invalidInputs(String s) {
            assertThrows(IllegalArgumentException.class, () -> Code128.Encode(s));
        }

        @DisplayName("Valid Inputs")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("validEncodingTargets")
        void validInputs(String input, String expected) {
            assertEquals(expected, Code128.Encode(input));
        }

        static Stream<Arguments> validEncodingTargets() {
            return Stream.of(
                arguments("A", "ÌABÎ"),
                arguments("a", "ÌabÎ"),
                arguments("aAaAaAaAaA", "ÌaAaAaAaAaAIÎ"),
                arguments("hello", "Ìhello%Î"),
                arguments("HELLO", "ÌHELLOHÎ"),
                arguments("QWERTyuiop", "ÌQWERTyuiopIÎ"),
                arguments("asdfgHJKL", "ÌasdfgHJKLoÎ"),
                arguments("ZxCvBnM", "ÌZxCvBnM|Î"),
                arguments("ABC123", "ÌABC123cÎ"),
                arguments("123456789", "Í,BXnÈ9oÎ"),
                arguments("0123456789", "Í!7McyiÎ"),
                arguments("05552020202034", "Í%W4444BÅÎ"),
                arguments("B3167", "ÌBÇ?coÎ"),
                arguments("6F164B2A16", "Ì6F164B2A16eÎ"),
                arguments("6aff57b776c11c0e70bd", "Ì6aff57b776c11c0e70bdLÎ"),
                arguments("3233a414fdde62395155", "Í@AÈa414fddeÇ^GSWiÎ"),
                arguments("LLFRA611039A", "ÌLLFRAÇ]*GÈA<Î"),
                arguments("LLFR185126C7", "ÌLLFRÇ2S:ÈC7<Î"),
                arguments("LLFR68505692", "ÌLLFRÇdRX||Î"),
                arguments("LLFR897B9129", "ÌLLFR897BÇ{=mÎ"),
                arguments("LLFRC06A6402", "ÌLLFRC06AÇ`\"+Î"),
                arguments("2020-01-01", "Í44È-01-01IÎ"),
                arguments(" Hello World", "ÌÂHelloÂWorld6Î"),
                arguments("Hello World ", "ÌHelloÂWorldÂKÎ"),
                arguments(" Hello World ", "ÌÂHelloÂWorldÂ6Î"),
                arguments("The quick brown fox", "ÌTheÂquickÂbrownÂfoxzÎ"),
                arguments("jumps over the lazy dog", "ÌjumpsÂoverÂtheÂlazyÂdogAÎ"),
                arguments(" 123456 ", "ÍÈÂÇ,BXÈÂ%Î"),
                arguments("123 456", "Í,È3Â456AÎ"),
                arguments(" 123 456 789 ", "ÍÈÂ1Ç7ÈÂ4ÇXÈÂ7ÇyÈÂNÎ"),
                arguments(" 12  AA  34  B5  6C  7R ", "ÍÈÂÇ,ÈÂÂAAÂÂÇBÈÂÂB5ÂÂ6CÂÂ7RÂTÎ"),
                arguments(" 1 (555) 555-5555 ext 5309 ", "ÌÂ1ÂÇÊWÈ5Â5ÇWÈ-ÇWWÈÂextÂÇU)ÈÂHÎ")
            );
        }
    }
}

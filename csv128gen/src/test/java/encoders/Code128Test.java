package encoders;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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
        @Test
        void validInputs() {
            assertAll(
                () -> assertEquals("ÌABÎ", Code128.Encode("A")),
                () -> assertEquals("ÌabÎ", Code128.Encode("a")),
                () -> assertEquals("ÌABC123cÎ", Code128.Encode("ABC123")),
                () -> assertEquals("ÌTheÂquickÂbrownÂfoxzÎ", Code128.Encode("The quick brown fox")),
                () -> assertEquals("ÌjumpsÂoverÂtheÂlazyÂdogAÎ", Code128.Encode("jumps over the lazy dog")),
                () -> assertEquals("ÌLLFRAÇ]*GÈA<Î", Code128.Encode("LLFRA611039A")),
                () -> assertEquals("ÌLLFRÇ2S:ÈC7<Î", Code128.Encode("LLFR185126C7")),
                () -> assertEquals("ÌLLFRÇdRX||Î", Code128.Encode("LLFR68505692")),
                () -> assertEquals("ÌLLFR897BÇ{=mÎ", Code128.Encode("LLFR897B9129")),
                () -> assertEquals("ÌLLFRC06AÇ`\"\"+Î", Code128.Encode("LLFRC06A6402"))
            );
        }
    }
}

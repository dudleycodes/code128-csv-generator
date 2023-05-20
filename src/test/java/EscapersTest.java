import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("String Escapers")
class EscapersTest {

    @DisplayName("csvQuotes()")
    @Nested
    class CsvQuotesTest {
        @DisplayName("No Encoding Needed")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "\r\n\t\t  ", "a", "aA123", "this is a sentence", "🖥️", "George's tea" })
        void noChanges(String expected) {
            String actual = Escapers.csvQuotes(expected);

            assertEquals(expected, actual);
        }

        @DisplayName("Encoding Needed")
        @Test
        void encoding() {
            assertAll(
                () -> { assertEquals("\"\"", Escapers.csvQuotes("\"")); },
                () -> { assertEquals("\"\"Laptop with 15 inch display.\"\"", Escapers.csvQuotes("\"Laptop with 15 inch display.\"")); },
                () -> { assertEquals("Laptop with \"\"15 inch\"\" display.", Escapers.csvQuotes("Laptop with \"15 inch\" display.")); },
                () -> { assertEquals("Laptop with 15 inch\"\" display.", Escapers.csvQuotes("Laptop with 15 inch\" display.")); },
                () -> { assertEquals("\"\"Laptop with \"\"15 inch\"\" display.\"\"", Escapers.csvQuotes("\"Laptop with \"15 inch\" display.\"")); },
                () -> { assertEquals("\"\"Laptop with 15 inch\"\" display.\"\"", Escapers.csvQuotes("\"Laptop with 15 inch\" display.\"")); }
            );
        }
    }
}

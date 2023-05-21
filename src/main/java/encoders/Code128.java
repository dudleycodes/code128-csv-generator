package encoders;

// Ported from C# code written by Joffrey VERDIER - https://grandzebu.net/informatique/codbar/code128_C%23.txt

public class Code128 {
    private static String encode(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank.");
        }

        int i = 1;
        int checksum = 0;
        int mini;
        int dummy;
        boolean tableB;
        String code128;
        int length;
        code128 = "";
        length = input.length();

        for (i = 0; i < length; i++) {
            if ((input.charAt(i) < 32) || (input.charAt(i) > 126)) {
                throw new IllegalArgumentException("Input contains invalid characters.");
            }
        }

        tableB = true;
        i = 0;
        while (i < length) {
            if (tableB) {
                mini = ((i == 0) || (i + 4 == length)) ? 3 : 5;

                if ((i + mini) <= length - 1) {
                    while (mini >= 0) {
                        if ((input.charAt(i + mini) < 48) || (input.charAt(i + mini) > 57)) {
                            break;
                        }
                        mini--;
                    }
                }

                if (mini < 0) {
                    code128 = (i == 0) ? Character.toString((char) 205) : code128 + (char) 199;
                    tableB = false;
                } else {
                    if (i == 0) {
                        code128 = Character.toString((char) 204);
                    }
                }
            }

            if (!tableB) {
                mini = 1;

                if (i + mini < length) {
                    while (mini >= 0) {
                        if (((input.charAt(i + mini)) < 48) || ((input.charAt(i)) > 57)) {
                            break;
                        }
                        mini = mini - 1;
                    }
                }

                if (mini < 0) {
                    dummy = Integer.parseInt(input.substring(i, i + 2));
                    dummy += (dummy < 95) ? 32 : 100;
                    code128 = code128 + (char) (dummy);
                    i += 2;
                } else {
                    code128 = code128 + (char) 200;
                    tableB = true;
                }
            }

            if (tableB) {
                code128 = code128 + input.charAt(i);
                i++;
            }
        }

        for (i = 0; i <= code128.length() - 1; i++) {
            dummy = code128.charAt(i);
            if (dummy < 127) {
                dummy = dummy - 32;
            } else {
                dummy = dummy - 100;
            }
            if (i == 0) {
                checksum = dummy;
            }
            checksum = (checksum + (i) * dummy) % 103;
        }

        if (checksum < 95) {
            checksum += 32;
        } else {
            checksum += 100;
        }

        code128 = code128 + (char) (checksum) + (char) (206);

        return code128.replaceAll(" ", "Â");
    }

    public static String Encode(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank.");
        }

        if (input.contains(" ")) {
            throw new UnsupportedOperationException("This encoder does not yes support any whitespace characters.");
        }

        return encode(input);
    }
}

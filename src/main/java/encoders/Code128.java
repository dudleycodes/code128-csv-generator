package encoders;

// Ported from C# code written by Joffrey VERDIER - https://grandzebu.net/informatique/codbar/code128_C%23.txt

public class Code128 {
    public static String Encode(String chaine) {
        if (chaine == null || chaine.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank.");
        }

        int ind = 1;
        int checksum = 0;
        int mini;
        int dummy;
        boolean tableB;
        String code128;
        int longueur;
        code128 = "";
        longueur = chaine.length();

        for (ind = 0; ind < longueur; ind++) {
            if ((chaine.charAt(ind) < 32) || (chaine.charAt(ind) > 126)) {
                throw new IllegalArgumentException("Input contains invalid characters.");
            }
        }

        tableB = true;
        ind = 0;
        while (ind < longueur) {
            if (tableB) {
                if ((ind == 0) || (ind + 3 == longueur - 1)) {
                    mini = 4;
                } else {
                    mini = 6;
                }

                mini = mini - 1;
                if ((ind + mini) <= longueur - 1) {
                    while (mini >= 0) {
                        if ((chaine.charAt(ind + mini) < 48) || (chaine.charAt(ind + mini) > 57)) {
                            break;
                        }
                        mini = mini - 1;
                    }
                }

                if (mini < 0) {
                    if (ind == 0) {
                        code128 = Character.toString((char) 205);
                    } else {
                        code128 = code128 + (char) 199;
                    }
                    tableB = false;
                } else {
                    if (ind == 0) {
                        code128 = Character.toString((char) 204);
                    }
                }
            }

            if (!tableB) {
                mini = 2;
                mini = mini - 1;

                if (ind + mini < longueur) {
                    while (mini >= 0) {
                        if (((chaine.charAt(ind + mini)) < 48) || ((chaine.charAt(ind)) > 57)) {
                            break;
                        }
                        mini = mini - 1;
                    }
                }

                if (mini < 0) {
                    dummy = Integer.parseInt(chaine.substring(ind, ind + 2));
                    if (dummy < 95) {
                        dummy = dummy + 32;
                    } else {
                        dummy = dummy + 100;
                    }
                    code128 = code128 + (char) (dummy);
                    ind = ind + 2;
                } else {
                    code128 = code128 + (char) 200;
                    tableB = true;
                }
            }

            if (tableB) {
                code128 = code128 + chaine.charAt(ind);
                ind = ind + 1;
            }
        }

        for (ind = 0; ind <= code128.length() - 1; ind++) {
            dummy = code128.charAt(ind);
            if (dummy < 127) {
                dummy = dummy - 32;
            } else {
                dummy = dummy - 100;
            }
            if (ind == 0) {
                checksum = dummy;
            }
            checksum = (checksum + (ind) * dummy) % 103;
        }

        if (checksum < 95) {
            checksum = checksum + 32;
        } else {
            checksum = checksum + 100;
        }

        code128 = code128 + (char) (checksum) + (char) (206);
        return code128;
    }
}

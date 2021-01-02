package com.vv.personal.prom.dbi.util;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.vv.personal.prom.dbi.constants.Constants.EMPTY_STR;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class DbiUtil {

    public static String extractContactNumbers(Collection<String> contactNumbers) {
        String contactNumbersString = EMPTY_STR;
        if (!contactNumbers.isEmpty())
            contactNumbersString = contactNumbers.stream()
                    .map(String::trim)
                    .filter(number -> !number.isEmpty())
                    .collect(Collectors.joining());
        return contactNumbersString;
    }
}

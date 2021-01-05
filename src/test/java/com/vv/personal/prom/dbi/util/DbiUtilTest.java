package com.vv.personal.prom.dbi.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.vv.personal.prom.dbi.util.DbiUtil.convertToContactList;
import static com.vv.personal.prom.dbi.util.DbiUtil.extractContactNumbers;
import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 05/01/21
 */
@RunWith(JUnit4.class)
public class DbiUtilTest {

    @Test
    public void testExtractContactNumbers() {
        List<String> contacts = new ArrayList<>();
        contacts.add("1234");
        contacts.add("3525");
        contacts.add("2098298492");
        String collatedContacts = extractContactNumbers(contacts);
        System.out.println(collatedContacts);
        assertEquals("1234,3525,2098298492", collatedContacts);
    }

    @Test
    public void testConvertToContactList() {
        Collection<String> contacts = convertToContactList("1234,3525,2098298492");
        System.out.println(contacts);
        assertEquals("[1234, 3525, 2098298492]", contacts.toString());
    }

}
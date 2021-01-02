
package com.vv.personal.prom.dbi.interactor;

import com.vv.personal.prom.dbi.interactor.ref.CachedRef;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Vivek
 * @since 02/01/21
 */
@RunWith(JUnit4.class)
public class CachedRefTest {
    private CachedRef cachedRef;

    @Before
    public void before() {
        cachedRef = new CachedRef();
    }

    @Test
    public void testAddNewCustomerId() {
        Boolean pushResult = cachedRef.addNewCustomerId(12);
        assertNull(pushResult);
        assertEquals(1, cachedRef.getActiveCustomerIds().size());
        System.out.println(cachedRef.getActiveCustomerIds());
        pushResult = cachedRef.addNewCustomerId(12);
        System.out.println(cachedRef.getActiveCustomerIds());
        System.out.println(pushResult);
    }

}
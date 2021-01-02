
package com.vv.personal.prom.dbi.interactor.ref;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.vv.personal.prom.dbi.constants.Constants.TABLE_REF_CUSTOMER;

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
    public void testAddNewIdToEntityCache() {
        String entity = TABLE_REF_CUSTOMER;
        System.out.println(cachedRef.getActiveRefEntityIds());

        Boolean res = cachedRef.addNewIdToEntityCache(entity, 12);
        System.out.println(res);
        System.out.println(cachedRef.getActiveRefEntityIds());

        res = cachedRef.addNewIdToEntityCache(entity, 12);
        System.out.println(res);
        System.out.println(cachedRef.getActiveRefEntityIds());

    }

}
package com.github.fengxxc;

import com.github.fengxxc.model.NobelPrize;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class DataWrapperTest {

    @Test
    public void getPropertyValue() throws InstantiationException, IllegalAccessException {
        DataWrapper dataWrapper = new DataWrapper(HashMap.class);
        dataWrapper.setPropertyValue("asdf", "1234");
        Assert.assertEquals("1234", dataWrapper.getPropertyValue("asdf"));

        DataWrapper dataWrapper1 = new DataWrapper(Map.class);
        dataWrapper1.setPropertyValue("qwer", 5678);
        Assert.assertEquals(5678, dataWrapper1.getPropertyValue("qwer"));

        DataWrapper dataWrapper2 = new DataWrapper(TreeMap.class);
        dataWrapper2.setPropertyValue("zxcv", '9');
        Assert.assertEquals('9', dataWrapper2.getPropertyValue("zxcv"));

        DataWrapper dataWrapper3 = new DataWrapper(ConcurrentMap.class);
        dataWrapper3.setPropertyValue("cccc", true);
        Assert.assertEquals(true, dataWrapper3.getPropertyValue("cccc"));

        DataWrapper dataWrapper4 = new DataWrapper(NobelPrize.class);
        dataWrapper4.setPropertyValue("total", 111);
        Assert.assertEquals(111, dataWrapper4.getPropertyValue("total"));

    }

    @Test
    public void getRootInstance() throws InstantiationException, IllegalAccessException {
        DataWrapper dataWrapper = new DataWrapper(HashMap.class);
        dataWrapper.setPropertyValue("asdf", "1234");
        Assert.assertEquals("1234", dataWrapper.getPropertyValue("asdf"));
        Assert.assertEquals(HashMap.class.getSimpleName(), dataWrapper.getRootInstance().getClass().getSimpleName());

        DataWrapper dataWrapper4 = new DataWrapper(NobelPrize.class);
        dataWrapper4.setPropertyValue("total", 111);
        Assert.assertEquals(111, dataWrapper4.getPropertyValue("total"));
        Assert.assertEquals(NobelPrize.class.getSimpleName(), dataWrapper4.getRootInstance().getClass().getSimpleName());
    }
}
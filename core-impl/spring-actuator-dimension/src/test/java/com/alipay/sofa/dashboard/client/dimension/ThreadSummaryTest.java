package com.alipay.sofa.dashboard.client.dimension;

import com.alipay.sofa.dashboard.client.context.DimensionTestContext;
import com.alipay.sofa.dashboard.client.model.thread.ThreadSummaryDescriptor;
import com.alipay.sofa.dashboard.client.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DimensionTestContext.class)
public class ThreadSummaryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadSummaryTest.class);

    @Autowired
    private ActuatorThreadSummaryDimension dimension;

    @Test
    public void basicInvokeTest() {
        Assert.assertEquals(dimension.getName(), DimensionName.THREAD_SUMMARY);
        Assert.assertEquals(dimension.getType(), ThreadSummaryDescriptor.class);

        ThreadSummaryDescriptor descriptor = dimension.currentValue();
        Assert.assertNotNull(dimension.currentValue());
        LOGGER.info("Fetch thread summary => {}", JsonUtils.toJsonString(descriptor));
    }

}

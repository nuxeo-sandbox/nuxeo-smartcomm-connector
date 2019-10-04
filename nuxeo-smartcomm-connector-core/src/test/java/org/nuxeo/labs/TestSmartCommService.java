package org.nuxeo.labs;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({PlatformFeature.class})
@Deploy("nuxeo-smartcomm-connector-core")
public class TestSmartCommService {

    @Inject
    protected SmartCommService smartcommservice;

    @Before
    public void init() {
        Properties properties = Framework.getProperties();
        properties.setProperty("smartcomm.hostname","na10-sb.smartcommunications.cloud");
        properties.setProperty("smartcomm.key","");
        properties.setProperty("smartcomm.username","");
        properties.setProperty("smartcomm.secret","");
    }

    @Test
    public void testService() {
        assertNotNull(smartcommservice);
    }

    @Test
    public void testGetTemplateList() {
        smartcommservice.getTemplateList();
    }

}

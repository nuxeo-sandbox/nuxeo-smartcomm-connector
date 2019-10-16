/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Thibaud Arguillere
 *     Michael Vachette
 */
package org.nuxeo.smartcomm.test;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.smartcomm.SmartCommService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy("nuxeo-smartcomm-connector-core")
/*
 * WARNING: Make sure you set the env. values in your dev tool or as system variables
 * before running the test.
 * WARNNG2 => some hard coded values relevant only for the Nuxeo-SmartComm demo.
 */
public class TestSmartCommService {

    @Inject
    protected SmartCommService smartCommService;

    @Before
    public void init() {

    }

    @Test
    public void testServiceIsLoaded() {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        assertNotNull(smartCommService);
    }

    @Test
    public void testCanGetToken() {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        String token = smartCommService.getToken();
        assertNotNull(token);
    }

    @Test
    public void testGetTemplateList() throws Exception {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        assertNotNull(smartCommService);

        JSONArray list = smartCommService.getTemplateList(null);
        assertNotNull(list);

        //System.out.print(list.toString(2));

    }

    /*
     * {
     * "fullPath": "/NUXEO Demo/Please Call",
     * "resourceId": 690006691,
     * "resourceName": "Please Call",
     * "effectiveDateTo": 0,
     * "folderId": 690002597,
     * "resourceType": "D",
     * "effectiveDateFrom": 0
     * },
     */
    @Test
    public void testGetTemplateDraft() throws Exception {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        JSONArray list = smartCommService.getTemplateList(null);
        assertNotNull(list);
        
        Assume.assumeTrue("No template found => No testGetTemplateDraft test", list.length() > 0);
        
        JSONObject template = (JSONObject) list.get(0);
        String templateId = template.getString("resourceId");
        
        HashMap<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("insuranceDemo_claimNumber", "CLM-1234");
        templateParams.put("insuranceDemo_policyNumber", "POL-1234");
        templateParams.put("insuranceDemo_claimantName", "John Moon");
        templateParams.put("insuranceDemo_lossDate", "2019-10-15");

        String xml = smartCommService.getTemplateDraft(templateId, templateParams, null, null);

        assertNotNull(xml);
        //System.out.print("\n" + xml);
        
        // Should parse the xml etc. Just check we have our values for now
        assertTrue(xml.indexOf("CLM-1234") > -1);
        assertTrue(xml.indexOf("John Moon") > -1);
        assertTrue(xml.indexOf("2019-10-15") > -1);
    }

}

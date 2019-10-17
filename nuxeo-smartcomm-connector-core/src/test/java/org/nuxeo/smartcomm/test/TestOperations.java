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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.smartcomm.SmartCommService;
import org.nuxeo.smartcomm.operations.FinalizeSmartCommTemplate;
import org.nuxeo.smartcomm.operations.GenerateSmartCommTemplateDraft;
import org.nuxeo.smartcomm.operations.GetSmartCommTemplateList;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * WARNING: Some test values are vaid only in the context of the Nuxeo-SmartComm demo environment
 */
@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("nuxeo-smartcomm-connector-core")
public class TestOperations {

    @Inject
    protected SmartCommService smartCommService;

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Test
    public void shouldGetTheTemplates() throws OperationException, Exception {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        // No parameters or empty => should use nuxeo.conf
        params.put("dataModelResId", "");
        String list = (String) automationService.run(ctx, GetSmartCommTemplateList.ID, params);
        assertNotNull(list);

        JSONArray listJson = new JSONArray(list);
        assertNotNull(listJson);
    }

    @Test
    public void shouldGetTheTemplateDraft() throws OperationException, Exception {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        String templateId = TestUtils.getFirstTemplateId(smartCommService);
        Assume.assumeTrue("No template found => No testGetTemplateDraft test", StringUtils.isNotBlank(templateId));

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        params.put("templateParamClaimNumber", "CLM-1234");
        params.put("templateParamClaimantName", "John Moon");
        params.put("templateParamLossDate", "2019-10-15");
        params.put("templateParamPolicyNumber", "POL-1234");
        String xml = (String) automationService.run(ctx, GenerateSmartCommTemplateDraft.ID, params);
        assertNotNull(xml);
        // System.out.print("\n" + xml);

        // Should parse the xml etc. Just check we have our values for now
        assertTrue(xml.indexOf("CLM-1234") > -1);
        assertTrue(xml.indexOf("John Moon") > -1);
        assertTrue(xml.indexOf("2019-10-15") > -1);

    }

    @Test
    public void shouldFinalizeTheTemplate() throws OperationException, Exception {

        Assume.assumeTrue("No configuration parameters found: No tests", TestUtils.hasConfigParameters());

        String templateId = TestUtils.getFirstTemplateId(smartCommService);
        Assume.assumeTrue("No template found => No testGetTemplateDraft test", StringUtils.isNotBlank(templateId));

        // Build a draft
        Map<String, String> templateParams = TestUtils.buildTemplateParameters();
        String xml = smartCommService.getTemplateDraft(templateId, templateParams, null, null);
        assertNotNull(xml);
        //System.out.print(xml);;

        // Now test the operation
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("templateXML", xml);
        String html = (String) automationService.run(ctx, FinalizeSmartCommTemplate.ID, params);
        assertNotNull(html);
        System.out.print("\n" + html);

        // Should parse the html etc. Just check we have our values for now
        // Check TestUtils.buildTemplateParameters for values to test
        assertTrue(html.indexOf("CLM-1234") > -1);
        assertTrue(html.indexOf("John Moon") > -1);
        assertTrue(html.indexOf("2019-10-15") > -1);

    }
}

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
 */
package org.nuxeo.smartcomm.test;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.smartcomm.SmartCommConstants;
import org.nuxeo.smartcomm.SmartCommService;

/**
 * Centralization of utilities
 * 
 * @since 10.10
 */
public class TestUtils {

    public static boolean hasConfigParameters() {

        String clientId = Framework.getProperty(SmartCommConstants.PARAM_NAME_CLIENT_ID);
        String clientSecret = Framework.getProperty(SmartCommConstants.PARAM_NAME_CLIENT_SECRET);
        String username = Framework.getProperty(SmartCommConstants.PARAM_NAME_USERNAME);
        String password = Framework.getProperty(SmartCommConstants.PARAM_NAME_PASSWORD);
        String tokenServerUrl = Framework.getProperty(SmartCommConstants.PARAM_NAME_TOKEN_SERVER_URL);

        String dataModelResId = Framework.getProperty(SmartCommConstants.PARAM_NAME_DATA_MODEL_RES_ID);

        if (isBlank(clientId) || isBlank(clientSecret) || isBlank(username) || isBlank(password)
                || isBlank(tokenServerUrl) || isBlank(dataModelResId)) {
            return false;
        }

        return true;

    }
    
    static public String getFirstTemplateId(SmartCommService smartCommService) throws Exception {
        
        JSONArray list = smartCommService.getTemplateList(null);
        assertNotNull(list);
        
        if(list.length() < 1) {
            return null;
        }
        
        JSONObject template = (JSONObject) list.get(0);
        String templateId = template.getString("resourceId");
        
        return templateId;
    }
    
    // IMPORTANT: This is very specific to Nuxeo-SmartComm demo environment
    static public Map<String, String> buildTemplateParameters() {
        HashMap<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("insuranceDemo_claimNumber", "CLM-1234");
        templateParams.put("insuranceDemo_policyNumber", "POL-1234");
        templateParams.put("insuranceDemo_claimantName", "John Moon");
        templateParams.put("insuranceDemo_lossDate", "2019-10-15");
        
        return templateParams;
    }

}

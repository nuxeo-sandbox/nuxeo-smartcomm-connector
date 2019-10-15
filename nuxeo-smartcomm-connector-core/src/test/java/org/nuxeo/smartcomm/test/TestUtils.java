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

import org.nuxeo.runtime.api.Framework;
import org.nuxeo.smartcomm.SmartCommConstants;

/**
 * @since TODO
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

}

package org.nuxeo.smartcomm;

import org.json.JSONArray;

public interface SmartCommService {

    String getToken();

    /**
     * If dataModelResId is blank => use the smartcom.dataModelResID configuraiton parameter
     * 
     * @param dataModelResId
     * @return
     * @since 10.10
     */
    JSONArray getTemplateList(String dataModelResId);

}

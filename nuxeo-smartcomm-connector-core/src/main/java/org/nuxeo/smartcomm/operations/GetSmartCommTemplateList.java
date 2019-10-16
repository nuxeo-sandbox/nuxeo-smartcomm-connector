package org.nuxeo.smartcomm.operations;

import org.json.JSONArray;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.smartcomm.SmartCommService;

@Operation(id = GetSmartCommTemplateList.ID, category = Constants.CAT_SERVICES, label = "Get Template List from SmartComm", description = ""
        + "Return the template list available for the dataModelResID."
        + " If dataModelResID is not passed, uses the smartcom.dataModelResID configuraiton parameter."
        + " Return a JSON string, array of templates")
public class GetSmartCommTemplateList {

    public static final String ID = "SmartComm.GetSmartCommTemplateList";

    @Context
    protected SmartCommService smartCommService;

    @Context
    protected CoreSession session;

    @Param(name = "dataModelResId", required = false)
    protected String dataModelResId;

    @OperationMethod
    public String run() {

        JSONArray list = smartCommService.getTemplateList(dataModelResId);
        return list.toString();
    }
}
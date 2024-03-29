package org.nuxeo.smartcomm.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.smartcomm.SmartCommService;

/**
 * 
 */
@Operation(id = FinalizeSmartCommTemplate.ID, category = Constants.CAT_SERVICES, label = "Finalize SmartComm Template", description = ""
        + "Finalize the template passed as XML in templateXML (likely generated via SmartComm.GenerateSmartCommTemplateDraft)"
        + " Return the HTML generated by SmartComm (raw HTML, not encoded)"
        + " If projectId is not passed it is read from configuration.")
public class FinalizeSmartCommTemplate {

    public static final String ID = "SmartComm.FinalizeSmartCommTemplate";

    @Context
    protected SmartCommService smartCommService;

    @Context
    protected CoreSession session;

    @Param(name = "templateXML", required = true)
    protected String templateXML;

    @Param(name = "projectId", required = false)
    protected String projectId;

    @OperationMethod
    public String run() {

        String html = smartCommService.finalizeDraft(templateXML, projectId);
        return html;
    }
}

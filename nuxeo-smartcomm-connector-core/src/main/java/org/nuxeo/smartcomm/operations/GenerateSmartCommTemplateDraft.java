package org.nuxeo.smartcomm.operations;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.smartcomm.SmartCommService;

/**
 * TODO: Change hard coded parameters to a list (property list, JSON, whatever)
 */
@Operation(id = GenerateSmartCommTemplateDraft.ID, category = Constants.CAT_SERVICES, label = "Generate SmartComm Template Draft", description = "Return the XML of the template id tmeplateId."
        + " Uses the projectId and batchConfigResId if passed (else, they are read from configuration)."
        + " Other parameters are optional")
public class GenerateSmartCommTemplateDraft {

    public static final String ID = "SmartComm.GenerateSmartCommTemplateDraft";

    @Context
    protected SmartCommService smartCommService;

    @Context
    protected CoreSession session;

    @Param(name = "templateId", required = true)
    protected String templateId;

    @Param(name = "projectId", required = false)
    protected String projectId;

    @Param(name = "batchConfigResId", required = false)
    protected String batchConfigResId;

    @Param(name = "templateParamClaimNumber", required = false)
    protected String templateParamClaimNumber;

    @Param(name = "templateParamClaimantName", required = false)
    protected String templateParamClaimantName;

    @Param(name = "templateParamLossDate", required = false)
    protected String templateParamLossDate;

    @Param(name = "templateParamPolicyNumber", required = false)
    protected String templateParamPolicyNumber;

    @OperationMethod
    public String run() {

        HashMap<String, String> templateParams = new HashMap<String, String>();
        if (StringUtils.isNotBlank(templateParamClaimNumber)) {
            templateParams.put("insuranceDemo_claimNumber", templateParamClaimNumber);
        }
        if (StringUtils.isNotBlank(templateParamClaimantName)) {
            templateParams.put("insuranceDemo_claimantName", templateParamClaimantName);
        }
        if (StringUtils.isNotBlank(templateParamLossDate)) {
            templateParams.put("insuranceDemo_lossDate", templateParamLossDate);
        }
        if (StringUtils.isNotBlank(templateParamPolicyNumber)) {
            templateParams.put("insuranceDemo_policyNumber", templateParamPolicyNumber);
        }

        String xml = smartCommService.getTemplateDraft(templateId, templateParams, projectId, batchConfigResId);
        return xml;
    }
}

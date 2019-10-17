package org.nuxeo.smartcomm;

import java.util.Map;

import org.json.JSONArray;

public interface SmartCommService {

    /**
     * Return a token from the service. If the last time a token was requested is > the token timeout, a new token is
     * requested.
     * 
     * @return a token from the service
     * @since 10.10
     */
    String getToken();

    /**
     * Return the list of templates available for this Data Model Resource Id.
     * If dataModelResId is blank => use the smartcom.dataModelResID configuration parameter
     * 
     * @param dataModelResId
     * @return an array of template(s)
     * @since 10.10
     */
    JSONArray getTemplateList(String dataModelResId);

    /**
     * Return the raw XML (not Base64 encoded) template, to be used in the SmartComm editor.
     * templateParams is a Map whose values are the name of the template parameters in the SmartComm template.
     * 
     * @param templateId
     * @param templateParams: A map of the parameters and their values
     * @param projectId. If null or "" it is read from configuration
     * @param batchConfigResId If null or "" it is read from configuration
     * @return the xml of the template
     * @since 10.10
     */
    String getTemplateDraft(String templateId, Map<String, String> templateParams, String projectId,
            String batchConfigResId);

    /**
     * Return the raw HTML (not Base64 encoded) of the template passed as xmlDraft.
     * 
     * @param xmlDraft
     * @param projectId
     * @return the HTML of the template
     * @since 10.10
     */
    String finalizeDraft(String xmlDraft, String projectId);

}

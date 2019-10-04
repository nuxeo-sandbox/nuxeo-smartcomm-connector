package org.nuxeo.labs;

import com.sun.jersey.api.client.WebResource;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.HMAC_SHA1;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

import javax.ws.rs.core.MediaType;

public class SmartCommServiceImpl extends DefaultComponent implements SmartCommService {

    // API URLs: CURRENTLY POINTING TO V1.0 APIs
    public static final String PROTOCOL = "https://";

    public static final int XML = 0;
    public static final int JSON = 1;

    protected Client client;
    protected OAuthClientFilter oauthFilter;

    protected void initClient() {
        client = Client.create();
        String key = Framework.getProperty("smartcomm.key");
        String username = Framework.getProperty("smartcomm.username");
        String secret = Framework.getProperty("smartcomm.secret");

        // build the OAuthFilter with correct key parameters using HMAC_SHA1 for encryption
        OAuthParameters params = new OAuthParameters().signatureMethod(HMAC_SHA1.NAME).consumerKey(
                key + "!" + username).timestamp().nonce().version();
        OAuthSecrets secrets = new OAuthSecrets().consumerSecret(secret);
        oauthFilter = new OAuthClientFilter(client.getProviders(), params, secrets);
    }

    /**
     * Helper method to create web resource
     *
     * @param path- API to call
     * @return
     */
    protected WebResource buildWebResource(String path) {
        String hostname =  Framework.getProperty("smartcomm.hostname");
        if (client==null) {
            initClient();
        }
        WebResource res = client.resource(PROTOCOL + hostname + path);
        res.addFilter(oauthFilter);
        return res;
    }

    @Override
    public String getTemplateList() {
        WebResource res = buildWebResource("/one/oauth1/api/v6/correspondence/queryTemplate");
        return res.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, "{\n" +
                "  \"dataModelResId\": 155400458\n" +
                "}");
    }

}

package org.nuxeo.smartcomm;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.oauth.client.OAuthClientFilter;

public class SmartCommServiceImpl extends DefaultComponent implements SmartCommService {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(SmartCommServiceImpl.class);

    protected static String token = null;

    protected static final int TOKEN_DURATION_SECONDS = 1800; // 30mn. SHOULD BE A CONFIG PARAMETER

    protected static long tokenStartTimeInSeconds = 0;

    public static final String LOCK = "Lock";

    public static String DEFAULT_DATA_MODEL_RES_ID = null;

    // API URLs: CURRENTLY POINTING TO V1.0 APIs
    public static final String PROTOCOL = "https://";

    public static final int XML = 0;

    public static final int JSON = 1;

    protected Client client;

    protected OAuthClientFilter oauthFilter;

    /*
     * Most of this code was provided by SmartComm. Just adapted to use configuration parameters.
     */
    @Override
    public String getToken() {

        synchronized (LOCK) {
            generateToken();
        }

        return token;
    }

    /*
     * Assume it is called form a synchronized code
     */
    protected void generateToken() {

        long nowInSeconds = System.currentTimeMillis() / 1000;

        if (isNotBlank(token) && (nowInSeconds - tokenStartTimeInSeconds) < TOKEN_DURATION_SECONDS) {
            return;
        }

        String grantType = "password";
        String clientId = Framework.getProperty(SmartCommConstants.PARAM_NAME_CLIENT_ID);
        String clientSecret = Framework.getProperty(SmartCommConstants.PARAM_NAME_CLIENT_SECRET);
        String username = Framework.getProperty(SmartCommConstants.PARAM_NAME_USERNAME);
        String password = Framework.getProperty(SmartCommConstants.PARAM_NAME_PASSWORD);
        String tokenServerUrl = Framework.getProperty(SmartCommConstants.PARAM_NAME_TOKEN_SERVER_URL);

        if (isBlank(clientId) || isBlank(clientSecret) || isBlank(username) || isBlank(password)
                || isBlank(tokenServerUrl)) {
            String msg = "SmartComm authentication parameters missing:\n";
            msg += "clientId: " + clientId + "\n";
            msg += "clientSecret: " + clientSecret + "\n";
            msg += "username: " + username + "\n";
            msg += "password: " + password + "\n";
            msg += "tokenServerUrl: " + tokenServerUrl + "\n";
            throw new NuxeoException(msg);
        }

        // TODO: should be a try-with-resource
        try {
            List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

            parametersBody.add(new BasicNameValuePair("grant_type", grantType));
            parametersBody.add(new BasicNameValuePair("client_id", clientId));
            parametersBody.add(new BasicNameValuePair("client_secret", clientSecret));
            parametersBody.add(new BasicNameValuePair("username", username));
            parametersBody.add(new BasicNameValuePair("password", password));

            // generate new token

            HttpClient client = HttpClients.custom()
                                           .setDefaultRequestConfig(
                                                   RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                                           .build();

            HttpPost post = new HttpPost(tokenServerUrl);

            post.addHeader("Authorization", "Basic " + encodeCredentials(clientId, clientSecret));
            post.setHeader("Content-type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(parametersBody, "UTF-8"));

            HttpResponse response = client.execute(post);

            int code = response.getStatusLine().getStatusCode();

            if (code == 200) {
                tokenStartTimeInSeconds = System.currentTimeMillis() / 1000;

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> jsonMap = mapper.readValue(response.getEntity().getContent(), Map.class);
                token = (String) jsonMap.get("access_token");
                // System.out.println("SmartComm new accessToken: " + token);
            } else {
                throw new NuxeoException(
                        "Getting token from SmartComm, Return Code: " + response.getStatusLine().getStatusCode());
            }

            post.releaseConnection();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public JSONArray getTemplateList(String dataModelResId) {

        // Most of this code comes from SmartComm
        JSONArray templateList = null;

        if (isBlank(dataModelResId)) {
            dataModelResId = Framework.getProperty(SmartCommConstants.PARAM_NAME_DATA_MODEL_RES_ID);
        }
        if (isBlank(dataModelResId)) {
            throw new NuxeoException("No dataModelResId: Cannot get the templates");
        }

        String token = getToken();

        HttpClient client = HttpClients.custom()
                                       .setDefaultRequestConfig(
                                               RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                                       .build();

        HttpPost post = new HttpPost(
                "https://na10-sb.smartcommunications.cloud/one/oauth2/api/v6/correspondence/queryTemplate");

        post.addHeader("Accept", "application/json"); // application/json or application/xml
        post.addHeader("Content-Type", "application/json"); // application/json or application/xml
        post.addHeader("Authorization", "Bearer " + token);
        post.addHeader("Cache-Control", "no-cache");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Connection", "keep-alive");

        JSONObject json = new JSONObject();

        try {
            json.put("dataModelResId", dataModelResId);

            StringEntity params = new StringEntity(json.toString());

            post.setEntity(params);

            HttpResponse response = client.execute(post);

            int code = response.getStatusLine().getStatusCode();

            if (code == 200) {

            } else {
                throw new NuxeoException("Getting templates from SmartComm for dataModelResId " + dataModelResId
                        + ", Return Code: " + response.getStatusLine().getStatusCode());
            }

            // System.out.println("HTTP Status Code: " + response.getStatusLine().getStatusCode());
            // System.out.println("\nBody : \n" + EntityUtils.toString(response.getEntity()));

            String jsonStr = EntityUtils.toString(response.getEntity());
            if (jsonStr.indexOf("while(1);") == 0) {
                // There certainly is a more elegant way of doing this than indexOf...
                jsonStr = jsonStr.substring("while(1);".length());
            }
            templateList = new JSONArray(jsonStr);

        } catch (IOException | JSONException e) {
            throw new NuxeoException("Error getitng the templates", e);
        }

        return templateList;

    }

    public String encodeCredentials(String username, String password) {
        String cred = username + ":" + password;
        byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
        return new String(encodedBytes);
    }

}

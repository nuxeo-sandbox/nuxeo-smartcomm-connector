# nuxeo-smartcomm-connector

This plugin is an example of integration with SmartComm and provides a couple of Operations to be used to handle templates.

_WARNING: For first implementation, unit tests rely a lot on the specific Nuxeo-SmartComm dev environment => Change the expected values in the misc. unit tests._

## Configuration
The plugin expects some configuration parameters:

##### Connection to SmartComm
You must provide the following parameters in your configuration file:

```
smartcom.auth.clientId=YOUR_CLIENT_ID
smartcom.auth.clientSecret=YOUR_CLIENT_SECRET
smartcom.auth.username=YOUR_USER_NAME
smartcom.auth.password=YOUR_PWD
smartcom.auth.tokenServerUrl=THE_TOKEN_SERVER_URL
```

Optional parameters:

* Default value is hard coded to 1800 (30 mn):

        smartcom.auth.tokenDUrationSeconds=THE_DURATION_IN_SECONDS

* The Data Model Res ID (used to fetch template list):

        smartcom.dataModelResID=THE_DATA_MODEL_RES_ID

* The project ID, to get the draft from:

        smartcom.projectId=THE_PROJECT_ID

* The Batch Config Res ID, to generate a draft form a template ID:

        smartcomm.batchConfigResId=THE_BATCH_CONF_RES_ID


##### Getting a List of Templates

The operation that fetches the list of templates accepts an optional parameter, the Data Model Resource Id. if it is not passed, the plug)in will read the default value in the configuration:
```
smartcom.dataModelResID=YOUR_DATA_MODEL_RES_ID
```

## Using the Plugin
Usage is done via the following operations:

##### `SmartComm.GetSmartCommTemplateList`
* Automation Category `Services`_
* Return a JSONArray as string, the list of templates for the given Data Model Resource Id.
* Parameter: `dataModelResourceId`
  * String, optional
  * If not passed or empty, the operation uses the `smartcom.dataModelResID` configuration parameter
* **Returns a String**: The string representation of a JSON Array, list of available templates. Use `JSON.parse` from JS Automation if you need to explore/walk the list. As of today, once converted to JSON, the values look like:

```
[
  {
    "fullPath": "/Your Project/Template for This",
    "resourceId": 123456789,
    "resourceName": "Template for This",
    "effectiveDateTo": 0,
    "folderId": 123456,
    "resourceType": "D",
    "effectiveDateFrom": 0
  },
  {
    "fullPath": "/Your Project/Template for That",
    "resourceId": 123456780,
    "resourceName": "Template for That",
    "effectiveDateTo": 0,
    "folderId": 123457,
    "resourceType": "D",
    "effectiveDateFrom": 0
  },
  ... etc ...
]
```


##### `SmartComm.GenerateSmartCommTemplateDraft`
* Automation Category `Services`_
* Return a XML string, the draft template containing the template parameters.
* Parameter: `templateId`
  * String, required
  * The ID of the template to use. After a call to `SmartComm.GetSmartCommTemplateList`you can use the `resourceId ` value.
* Parameters `projectId` and `batchConfigResId` are optional. if not passed, they are read from the configuration (see above)
* Template parameters:
  * **WARNING**: For first, quick implementation, these are specific to Nuxeo-SmartComm test environment. If we don't have time to do it ourselves, we recommend passing a map, or a property list, etc. instead
  * All these are string, optional, parameters
  * `templateParamClaimNumber`, `templateParamClaimantName`, `templateParamLossDate` and `templateParamPolicyNumber`.



##### `SmartComm.FinalizeSmartCommTemplate`
* Automation Category `Services`_
* Return a HTML string, the HTML returned by SmartComm after finalizing the draft
* Parameter: `templateXML`:
  * String, required.
  * The XML to pass to the API. It will likely about the same as the one received after calling `SmartComm.GenerateSmartCommTemplateDraft` (_about the same_: Maybe an end user will have change the message a little bit)
* Parameter: `projectId`, string, optional: The project Id in SmartComm. If not passed or empty, the plugin uses the configuration parameter `smartcom.projectId`.


## Build
Make sure to run the correct version of maven and Java, as expected by the target version of Nuxeo Server. As of October 2019, it is Java 8 (not greater) and Maven 3.3.n

```
cd /path/to/nuxeo-smartcomm-connector
mvn clean install
```

Tests will be skipped (so, not failing, just skipped) if the expected environnement variables are not set in the same shell. They must be the same as the one described above.


## Support

**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained in this repository.


## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

Contributors:
Thibaud Arguillere (https://github.com/ThibArg)

## About Nuxeo

Nuxeo, developer of the leading Content Services Platform, is reinventing enterprise content management (ECM) and digital asset management (DAM). Nuxeo is fundamentally changing how people work with data and content to realize new value from digital information. Its cloud-native platform has been deployed by large enterprises, mid-sized businesses and government agencies worldwide. Customers like Verizon, Electronic Arts, ABN Amro, and the Department of Defense have used Nuxeo's technology to transform the way they do business. Founded in 2008, the company is based in New York with offices across the United States, Europe, and Asia. Learn more at [www.nuxeo.com](http://www.nuxeo.com).

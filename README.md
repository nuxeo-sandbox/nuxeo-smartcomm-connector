# nuxeo-smartcomm-connector

This plugin is an example of integration with SmartComm and provides a couple of Operations to be sued to handle templates.

## Configuration
The plugin expects some configuration parameters:

##### Connection to SmartComm
You must provide the following parameters in your configuraiton file:

```
smartcom.auth.clientId=YOUR_CLIENT_ID
smartcom.auth.clientSecret=YOUR_CLIENT_SECRET
smartcom.auth.username=YOUR_USER_NAME
smartcom.auth.password=YOUR_PWD
smartcom.auth.tokenServerUrl=THE_TOKEN_SERVER_URL
```

##### Getting a List of Templates

The operation that fetches the list of templates accepts an optional parameter, the Data Model Resource Id. if it is not passed, the plug)in will read the default value in the configuration:
```
smartcom.dataModelResID=YOUR_DATA_MODEL_RES_ID
```

## Using the Plugin
Usage is done via the following operations:

##### `SmartComm.GetSmartCommTemplateList`
* Return a JSONArray as string, the list of templates for the given Data Model Resource Id.
* Parameter: `dataModelResourceId`
  * String, optional
  * If not passed or empty, the operation uses the `smartcom.dataModelResID` configuraiton parameter
* **Returns a String**: The string representaiton of a JSON Array, list of available templates. As of today, once converted to JSON, the values look like:

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

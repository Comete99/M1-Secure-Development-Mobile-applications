# M1-Secure-Development-Mobile-applications
  Using Android Studio (or any editor of your choice), we will have to create a mobile application.  The goal is to create a secure application to see our bank accounts.  This application must be available offline. A refresh button allows the user to update its accounts. Access to the application is restricted Exchanges with API must be secure ( with TLS)
  
  - Explain how you ensure user is the right one starting the app :

With digital print we ensure that the user who uses the application is also the owner of the phone.

- How do you securely save user's data on your phone ?

To save the user's data I chose to use the internal storage which ensures prompt storage of data and is simple in use. Each piece of data stored using the internal storage method is completely private to the app and if the app is uninstalled, the data is removed from the device. In addition other app (even the user) can't read this data.
The apk is totally obfuscaded to enhance security thanks to the tool obfuscapk (https://github.com/ClaudiuGeorgiu/Obfuscapk#-usage).

- How did you hide the API url ?

The exchanges with API are secured with networkSecurityConfig (see network_security_config.xml) and it uses the certificate of the system. In terms of the details for verifying certificates and hostnames, the Android framework takes care of it for you through APIs used in the RetrieveFeedTask class.
The API URL is hidden in the file "gradle.properties" and in the code we call the variable associated to the url to send the request.


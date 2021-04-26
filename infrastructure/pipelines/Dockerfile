FROM mcr.microsoft.com/azure-functions/java:3.0-java11-appservice
COPY / /home/site/wwwroot
ENV AzureWebJobsScriptRoot=/home/site/wwwroot \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true


# claim-vat-enrolment-frontend

This is a Scala/Play frontend to allow users to claim vat enrolment from HMRC.

### How to run the service

1. Make sure any dependent services are running using the following service-manager command
`sm --start CLAIM_VAT_ENROLMENT_ALL -r`

2. Stop the frontend in service manager using
 `sm --stop CLAIM_VAT_ENROLMENT_FRONTEND`
 
3. Run the frontend locally using
`./run.sh` or 
`sbt 'run 9936 -Dapplication.router=testOnlyDoNotUseInAppConf.Routes'`

4. Go to the homepage:
http://localhost:9936/claim-vat-enrolment

## End-Points
### GET /journey/:vrn 

---
This endpoint creates a journey, it also takes a query parameter called `continueUrl`
in which the calling service provides a url to send the user back to once they complete 
our journey.

####Request:

A valid VRN and continueUrl must be sent in the URL

#### Response:
            
| Expected Response                       | Reason  
|-----------------------------------------|------------------------------
|```SEE_OTHER(301)```                     |  ```Redirects to VatRegistrationDate page```       


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
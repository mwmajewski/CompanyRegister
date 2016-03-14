# Company Register

1.  Company Register is a sample application that keeps a list of Companies in the database. Each Company has several fields and may have a list of Beneficial Owners. 
    The application consists of two parts:
    *  A Rest web service which exposes several operations on Companies written in java using spring boot  
    *  A UI written in angular js which consumes the rest web service mentioned above
2.  Building and running
    *  to build the app simply type `mvn clean install` 
    *  to run the app simply type `mvn spring-boot:run` after a while you can type http://localhost:8080/ in the browser and you will see a simple angular js UI
3.  Using the UI
    *  The UI consists of two tables, one listing Companies(shown by default) and the second showing Beneficial Owners for a given company.
    *  To see Beneficial Owners list for a company click a *list* button on the Company row, a table listing Beneficial Owners will appear below
    *  When you click the *pencil* button you will see a modal window where Company data can be modified
    *  When you click the *plus* button you will see a modal window, type Beneficial Owner's name and click Save, a new Beneficial Owner has been added
    *  At the top of Company list table there is a link which similarly as above opens a modal window with a form to be filled with new Company data, clicking save stores the data from the form to the database
5.  Using Rest API with cURL tool
    Rest web service can be consumed using curl command 
    *  Obtaining Company list can be achieved by typing `curl -X GET http://localhost:8080/companies`
    Expected response looks like this:
    ```json
    [{"id":1,"name":"IBM","address":"#3 Brown St.","city":"London","country":"UK","email":"sales@ibm.com","phoneNumber":"+44 432 567 567","beneficialOwners":[{"id":1,"name":"Beneficial Owner 1"},{"id":2,"name":"Beneficial Owner 2"}]}
    ,{"id":2,"name":"GM","address":"#5 15th St.","city":"Detroit","country":"USA","email":"gm@gm.com","phoneNumber":"+1 876 876 876","beneficialOwners":[{"id":3,"name":"Beneficial Owner 3"},{"id":4,"name":"Beneficial Owner 4"}]}
    ,{"id":3,"name":"Apple","address":"#43 Creative St.","city":"San Diego","country":"USA","email":"apple@apple.com","phoneNumber":"+1 345 658 342","beneficialOwners":[{"id":6,"name":"Beneficial Owner 6"},{"id":5,"name":"Beneficial Owner 5"}]}
    ,{"id":4,"name":"HP","address":"#3 Packard St.","city":"Dallas","country":"USA","email":"sales@hm.com","phoneNumber":"+44 432 567 567","beneficialOwners":[]}]
    ```
    *  Obtaining a Company knowing it's id can be achieved by typing: `curl -X GET http://localhost:8080/companies/{companyId}`
        Expected response looks like this:
        ```json
        {"id":1,"name":"IBM","address":"#3 Brown St.","city":"London","country":"UK","email":"sales@ibm.com","phoneNumber":"+44 432 567 567","beneficialOwners":[{"id":1,"name":"Beneficial Owner 1"},{"id":2,"name":"Beneficial Owner 2"}]}
        ```
    *  Adding a Company can be achieved by typing: `curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Company Name\",\"address\":\"#1 Test Street\",\"city\":\"Test City\",\"country\":\"Poland\"}" http://localhost:8080/companies`
            Expected response which contains company data which has been added looks like this:
            ```json
            {"id":5,"name":"Company Name","address":"#1 Test Street","city":"Test City","country":"Poland","email":null,"phoneNumber":null,"beneficialOwners":null}
            ```
    *  Updating a Company can be achieved by typing: `curl -X PUT -H "Content-Type: application/json" -d "{\"id\":\"5\",\"name\":\"Company Name\",\"address\":\"#1 Test Street\",\"city\":\"Test City\",\"country\":\"Poland\",\"email\":\"example@example.pl\"}" http://localhost:8080/companies/5`
            Expected response which contains company data which has been added looks like this:
            ```json
            {"id":5,"name":"Company Name","address":"#1 Test Street","city":"Test City","country":"Poland","email":"example@example.pl","phoneNumber":null,"beneficialOwners":null}
            ```
    *  Obtaining Beneficial Owner list for a given Company knowing it's id can be achieved by typing: `curl -X GET http://localhost:8080/companies/{companyId}/beneficialOwners`
            Expected response looks like this:
            ```json
            [{"id":1,"name":"Beneficial Owner 1"},{"id":2,"name":"Beneficial Owner 2"}]
            ```
    *  Adding a Beneficial Owner to a given Company knowing it's id can be achieved by typing `curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"beneficiary #1\"}" http://localhost:8080/companies/{companyId}/beneficialOwners`
    *  Expected error codes:
        * 409 if {companyId} from URL does not match company id from POST request content when updating a company or adding a Beneficial Owner
        * 404 if company with id from URL({companyId}) does not exist when requesting a particular Company or it's Beneficial Owners
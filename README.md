**Pre Interview Exercise : David Hay**

**Running the Application**

To run this application you can either uncomment and edit the **data.url**
in **application.properties** or supply the **data.url**
via the command line.

This prevents the application from making calls on a remote web service without specific intent.

To run the application from the command line (for example)

    $ mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080,--data.url=https://localhost:8888/products

Where you have to choose appropriate values for **server.port** and **data.url**.

**Using the Application**

The following curl commands will get product information from this service.

    $ curl http://localhost:8080/products
    $ curl http://localhost:8080/products?labelType=ShowWasNow
    $ curl http://localhost:8080/products?labelType=ShowWasThenNow
    $ curl http://localhost:8080/products?labelType=ShowPercDiscount

The first and second commands are equivalent.

The formatted (using jq) json which results from these 4 commands is stored in the following files.

    ./JSON/withNoLabelType.json
    ./JSON/withLabelTypeShowWasNow.json
    ./JSON/withLabelTypeShowWasThenNow.json
    ./JSON/withNoLabelType.json

**Testing the Application**

I have written many unit tests to test the application.

Some are simple unit tests, others make use of Mockito and the 'Controller' is tested using MockMvc and Mockito.

There is one disabled integration test which attempts to connect to a local
server which was serving up test json.



    

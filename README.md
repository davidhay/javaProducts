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

**Overall Approach**

Using Jackson for mapping between JSON and java Objects.
I used three sets of objects
* The pojos in *com.ealanta.productapp.item* hold 'raw' String data coming from the remote web service.
* The pojos in *com.ealanta.productapp.label* hold the data used to create the responses from this web service.
* The pojos in *com.ealanta.productapp.product* hold data in a state to aid filtering, sorting and enriching.

When a request comes into the webservice the request's main flow is through these classes and back again.

    com.ealanta.productapp.web.LabelledProductController
    com.ealanta.productapp.label.LabelledProductSource
    com.ealanta.productapp.service.ProductSourceImpl
    com.ealanta.productapp.items.ProductItemsSourceImpl
    org.springframework.web.client.RestTemplate

I used the Java Money API and BigDecimal to hold money and percentages.
They are a bit clumsy to use from Java.

**Further Work**

This code makes use Spring MVC to provide the 'frontend' web service
and uses Spring's RestTemplate to talk to the 'backend' web service.

These are both blocking - a single thread will process an entire request
and it will block waiting on the response from the remote web service.
This will lead to problems under high load.

Using non-blocking technologies would allow for a greater number of concurrent client requests.

The RestTemplate should have its timeouts configured properly.

At the moment, the entire json response is read into memory, enriched, filtered and sorted before being 
send as a response from this webservice.

If the response did not have to be sorted, it may be possible to process the response from the remote web service
as a stream, process it in parts and send the response bit by bit to the user of this web service.
This would reduce the memory overhead.

At the moment, each request to this web service results in a seperate call to the remote web service.
Using an internal cache might reduce the load on this web service and the downstream web service.

The use of Spring Cloud Gateway or Camel might have made the processing pipeline nature of this web service
more explicit.

It would be nice to use SwaggerFox annotations to generate the swagger spec for this RESTful endpoint.

**Kotlin**

I had intended in writing this in Kotlin too but I didn't have the time.

The Kotlin solution would certainly benefit from :
* less boiler plate (data classes instead of Lombok)
* better Null Safety with Nullable and Non-Null types.
* more fluent code with Kotlin extension methods ( I think this would really have helped with MonetaryAmounts and BigDecimals)


    

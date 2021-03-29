# microservices-Project-Karmana-HW2

## Using an IDE

You can run the system in your IDE by running the three server classes in below order:
1. _RegistrationService_ -> [http://localhost:1111](http://localhost:1111)
2. _AccountsService_ -> [http://localhost:2222](http://localhost:2222)
3. _WebService_ -> [http://localhost:3333](http://localhost:3333)
4. _WebService_ -> [http://localhost:4444](http://localhost:4444)
5. _ForumService_ -> [http://localhost:5555](http://localhost:5555)

Each is a Spring Boot application using embedded Tomcat.  If using Spring Tools use `Run As ... Spring Boot App` otherwise just run each as a Java application - each has a static `main()` entry point.

As discussed in the Blog, open the Eureka dashboard [http://localhost:1111](http://localhost:1111) in your browser to see that the `ACCOUNTS-SERVICE` and `WEB-SERVICE` applications have registered.  Next open the Demo Home Page [http://localhost:3333](http://localhost:3333) in and click one of the demo links.

The `localhost:3333` web-site is being handled by a Spring MVC Controller in the _WebService_ application, but you should also see logging output from _AccountsService_ showing requests for Account data.

## Implementation Details

- Post service supports below
    - creates H2 database to store all posts/threads.
    - adding a new post within existing thread.
    - adding a new post by creating new thread.
    - retrieve all existing threads.
    - retrieve all posts withing a thread.

- Forum service supports below
    - Account Login - validation from account service database.
    - Access Forum Threads - retrieves threads from post service.
    - Create a new thread - invokes post service to create thread.
    - Add a post to an existing thread - invokes post service.
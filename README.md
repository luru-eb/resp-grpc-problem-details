# Error handling in gRPC with public RESTFul API using Problem Details

Dealing with errors in gRPC it could be hard, because the current version of gRPC only has limited built-in error handling based on simple status codes and metadata. You can read more about errors in gRPC in the next [link](https://www.grpc.io/docs/guides/error/)

Google has developed a [richer error model](https://cloud.google.com/apis/design/errors#error_model) which provides richer error handling capabilities, but it's nor part of the official gRPC error model and for thus not all languages has built-in support for this.

In this repo you will find a PoC using Standard error model and metadata to inform the consumers about those errors. The solution consists in one api gateway and a microservice that returns books by ISBN:

![img.png](img.png)

* The books' gateway is the client of the books microservice and expose the information through a REST API. In a more advance scenario it could be a BFF consuming more microservices but for the sake of simplicity I decided to consume only one.
* The books' microservice that expose a gRPC API to get books.

When you get a book by invalid ISBN, the books' microservice will throw and exception and the client application (Books' aPI Gateway) should handler the error showing a meaningful error response to the caller.

![img_1.png](img_1.png)

To provide this meaningful error messages to the caller, I decided to use [Problem Details](https://datatracker.ietf.org/doc/html/rfc7807), a standard way of specifying errors in HTTP API response. For this example I've used this [library](https://github.com/zalando/problem-spring-web) developed by Zalando

## Testing

Open a terminal in the root and execute the next command:

```shell
./gradlew --parallel bootRun
```

Open a new browser and type this:

```txt
http://localhost:8081/books/0-7645-2641-3
```

You'll see the details of the book

```json
{
  "isbn": "0-7645-2641-3",
  "title": "Clean Code",
  "author": "Uncle Bob",
  "page": 400
}
```

Try to an invalid ISBN

```txt
http://localhost:8081/books/123456
```

You'll see a problem details error:

```json
{
  "title": "Not Found",
  "status": 404,
  "detail": "NOT_FOUND: Book not found",
  "eb_isbn": "123456",
  "eb_message": "Book not found"
}
```

## Considertions

This a PoC and there are a lot of things to improve:

* Parsing eb_ prefix in the metadata to improve the error
* Improve the status mapping to avoid send sensitive message to the clients


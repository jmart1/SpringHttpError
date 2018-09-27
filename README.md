# SpringHttpError
A simple way to return any HTTP status-code from your Spring MVC code.  

Currently to return a specific HTTP status-code from your Spring MVC code you have to do one of the following.

1)  Throw an existing exception and map that exception to specific HTTP status via a custom ResponseEntityExceptionHandler.  This fails as a proper solution because you might not want to lock down a specific exception class to a stauts code.  The decision of what exception is thrown deep in your dependencies is up to someone else to decide, as such you do not want to assume those exceptions should be converted into the same HTTP status-code accross your application.

2)  Return `ResponseEntity<SomePojo>` from your controller then have all your business (aka service classes) return `ResponseEntity<SomePojo>` from their methods.  This is just clunky.  And if you want to return an error message you can't unless you are returning `ResponsEntity<String>`.

3)  In your controller classs accept the HttpServletResponse  as input then set the status-code directly on the HttpServletResponse.  This is not desirable due to either having force your controller methods to decide what the HTTP status-code should be or pass the HttpServletResponse to your service classes.

4)  Create custom exceptions and map each to a status-code using the @ResponseStatus(HttpStatus httpStatus) annotation.  This is not bad and is getting closer to an ideal solution.  What this project does is go one step further an created an eception class that can get mapped to *any* HTTP status-code.


Lets jump right in with some simple use cases.

#### some object is not found and need to return 404
```java
//method 1
if (account == null) throw new HttpErrorException(HttpStatus.NOT_FOUND, "The account requested for is not found.");

//method 2
HttpErrorException.throwIfNull(account, HttpStatus.NOT_FOUND, "The account requested for is not found.");
```

#### an exception is caught and need to return appropriate status-code
```java
try {
    //some complicated code that may throw various excpetions
} catch (NumberFormatException nfe){
    throw new HttpErrorException(HttpStatus.BAD_REQUEST, "The input provided is not in proper format.", nfe);
} catch (ResourceNotFoundException rnfe) {
    throw new HttpErrorException(HttpStatus.NOT_FOUND, "The the user requested for is not found.", nfe);
} catch (NotAuthorizedException e) {
    throw new HttpErrorException(HttpStatus.FORBIDDEN, "Operation requested is not allowed.", nfe);
}
```

#### some object being POST'd is already found and need to return 409
```java
//method 1
if (account != null) throw new HttpErrorException(HttpStatus.CONFLICT, "The account requested for is not found.");

//method 2
HttpErrorException.throwConflictIfNotNull(account, "The account already exists.");
```

#### return 400 if String is blank
```java
String username = ...

//method 1
if (username == null || username.isEmpty()) throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Username field missing from input request.");

//method 2
HttpErrorException.throwIfBlank(username, HttpStatus.BAD_REQUEST, "Username field missing from input request.");

//method 3
HttpErrorException.throwBadRequestIfBlank(username, "Username field missing from input request.");
```

#### other static helper methods that exist are:
1. throwIfTrue(boolean condition, HttpStatus httpStatus, String msg)
2. throwIfFalse(boolean condition, HttpStatus httpStatus, String msg)

#### the output error text will be a formatted JSON string.  
Here is an example:
```
{  
  "status": 400,  
  "message": "Unsupported currency.",  
  "cause": "some error occurred"  
}  
```

#### implementation
This project consists of just two classes.
1)  **HttpErrorException**:  The actual exception class that is thrown.
2)  **HttpErrorExceptionHandler**:  The implementation of Spring's *ResponseEntityExceptionHandler* that will catch any **HttpErrorException** exceptions, convert it into formatted JSON error, and will set the HTTP status-code that is returned.

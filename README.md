# Systematics


## Monads

A Monad is a concept in functional programming that describes computations as a series of steps.
It's a design pattern that allows you to structure your programs in a way that's easier to reason about.
Monads can be used to handle side effects, manage state, handle exceptions, and more.

In Java, there isn't a built-in Monad interface or class, but the concept can still be applied.
For example, the `Optional` class in Java can be thought of as a Monad. It wraps a value that may or may not be present, 
and provides methods to perform computations on that value in a safe way.

Here's a simple example of using `Optional` as a Monad:

```java
Optional<String> optional = Optional.of("Hello, world!");
optional = optional.map(s -> s.toUpperCase());
optional.ifPresent(System.out::println);
```

In this example, `Optional.of("Hello, world!")` creates an `Optional` that contains a string. The `map` method is then used 
to transform the value inside the `Optional` (if it is present) by applying a function to it. The `ifPresent` method is then used 
to perform an action with the value (if it is present), in this case printing it to the console.

This is a very basic example, but it demonstrates the core idea of a Monad: it's a way to perform a series of 
computations on a value in a controlled manner.

## Dyad

A Dyad is a term from the APL programming language, referring to a function that takes two arguments. In other words, it's a binary function. The term is used in functional
programming to describe functions that operate on two inputs.

In the context of Java, a similar concept would be a BiFunction interface, which represents a function that accepts two arguments and produces a result. This is a functional
interface whose functional method is `apply(Object, Object)`.

Here's a simple example of using `BiFunction`:

```java
BiFunction<Integer, Integer, Integer> addition = (a, b) -> a + b;
int result = addition.apply(5, 3);
```

In this snippet, `BiFunction<Integer, Integer, Integer> addition = (a, b) -> a + b;` defines a `BiFunction` that takes two integers as input and returns their sum. The `apply`
method is then used to apply this function to the numbers 5 and 3, storing the result in the `result` variable.

This is a basic example, but it illustrates the core idea of a Dyad: it's a function that operates on two inputs.

## Triad`

A Triad, similar to a Monad and Dyad, is a term from the APL programming language. It refers to a function that takes three arguments, making it a ternary function. This term is
used in functional programming to describe functions that operate on three inputs.

In the context of Java, a similar concept would be a `TriFunction` interface. However, unlike `BiFunction`, Java does not have a built-in `TriFunction` interface. But we can easily
create one. This interface would represent a function that accepts three arguments and produces a result.

Here's a simple example of how you might define and use a `TriFunction`:

```java
@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}

TriFunction<Integer, Integer, Integer, Integer> addition = (a, b, c) -> a + b + c;
int result = addition.apply(5, 3, 2);
```

In this snippet, `TriFunction<Integer, Integer, Integer, Integer> addition = (a, b, c) -> a + b + c;` defines a `TriFunction` that takes three integers as input and returns their
sum. The `apply` method is then used to apply this function to the numbers 5, 3, and 2, storing the result in the `result` variable.

This is a basic example, but it illustrates the core idea of a Triad: it's a function that operates on three inputs.

## Tetrad


## Try

The `Try` Monad is a concept in functional programming that encapsulates computations which may either result in a value or throw an exception. 
It's a design pattern that allows you to structure your programs in a way that's easier to reason about, especially when dealing with error handling.
Specifically 

In Java, there isn't a built-in `Try` Monad, but it can be implemented using standard Java features. The idea is to wrap a computation in a `Try` object, and then provide methods
to handle both the success and failure cases.

Here's a simple example of how you might define and use a `Try` Monad:

```java
public abstract class Try<T> {
    public abstract T get() throws Exception;
    public abstract boolean isSuccess();

    public static <T> Try<T> of(Callable<T> callable) {
        try {
            return new Success<>(callable.call());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    private static class Success<T> extends Try<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    private static class Failure<T> extends Try<T> {
        private final Exception exception;

        public Failure(Exception exception) {
            this.exception = exception;
        }

        @Override
        public T get() throws Exception {
            throw exception;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    }
}

Try<Integer> tryMonad = Try.of(() -> Integer.parseInt("123"));
if (tryMonad.isSuccess()) {
    System.out.println(tryMonad.get());
} else {
    System.out.println("Parsing failed");
}
```

In this example, `Try.of(() -> Integer.parseInt("123"))` creates a `Try` that contains the result of parsing a string to an integer. If the parsing is successful, `isSuccess()`
returns true and `get()` returns the parsed integer. If the parsing fails (for example, if the string cannot be parsed to an integer), `isSuccess()` returns false and `get()`
throws the exception that occurred during parsing.

This is a basic example, but it demonstrates the core idea of a `Try` Monad: it's a way to encapsulate computations that may either result in a value or throw an exception,
allowing for more robust error handling.


## Either 

The `either` concept in Erlang is not a built-in feature of the language, but it's a common pattern used in functional programming languages. It's often used to handle computations
that can result in two different types of values, typically representing success and failure cases.

In Erlang, this pattern can be implemented using tuples. A common convention is to use a tuple where the first element is an atom such as `ok` or `error`, and the second element is
the actual value or error information.

Here's a simple example of how you might use this pattern in Erlang:

```erlang
case some_function() of
    {ok, Value} ->
        %% Handle the success case
        io:format("Success: ~p~n", [Value]);
    {error, Reason} ->
        %% Handle the error case
        io:format("Error: ~p~n", [Reason])
end.
```

In this snippet, `some_function()` is expected to return either `{ok, Value}` or `{error, Reason}`. The `case` statement is then used to handle these two possible return values.
If `some_function()` returns `{ok, Value}`, the success case is handled and the value is printed. If `some_function()` returns `{error, Reason}`, the error case is handled and the
reason for the error is printed.

This is a basic example, but it illustrates the core idea of the `either` pattern: it's a way to handle computations that can result in two different types of values, allowing for
more robust error handling.

## Operational Result

The Operational Result pattern is a software design pattern often used in functional programming. It's a way to handle computations that can result in either a success or a
failure, and it's particularly useful for error handling. This pattern is similar to the `Either` type in some functional programming languages, or the `Try` Monad in others.

In Java, this pattern can be implemented using a class that encapsulates the result of an operation, which can be either a success or a failure. This class typically provides
methods to check if the operation was successful, retrieve the result in case of success, or retrieve the error in case of failure.

Here's a simple example of how you might define and use an `OperationalResult` class:

```java
public class OperationalResult<T> {
    private final T result;
    private final Exception error;

    private OperationalResult(T result, Exception error) {
        this.result = result;
        this.error = error;
    }

    public static <T> OperationalResult<T> success(T result) {
        return new OperationalResult<>(result, null);
    }

    public static <T> OperationalResult<T> failure(Exception error) {
        return new OperationalResult<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }
}

OperationalResult<Integer> result = OperationalResult.success(123);
if (result.isSuccess()) {
    System.out.println(result.getResult());
} else {
    System.out.println(result.getError().getMessage());
}
```

In this example, `OperationalResult.success(123)` creates an `OperationalResult` that represents a successful operation with a result of 123. The `isSuccess()` method is then used
to check if the operation was successful. If it was, `getResult()` is used to retrieve the result. If the operation was not successful, `getError().getMessage()` is used to
retrieve the error message.

This is a basic example, but it demonstrates the core idea of the Operational Result pattern: it's a way to encapsulate the result of an operation, which can be either a success or
a failure, allowing for more robust error handling.

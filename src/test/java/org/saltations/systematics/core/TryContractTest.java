package org.saltations.systematics.core;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.systematics.test.fixture.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.saltations.systematics.core.Try.attempt;

/**
 * Confirms the basic contract of the {@link Try} class.
 * <p>
 * The basic contract looks like this:
 * <dl>
 *     <dt>Success</dt>
 *     <dd>When an operation is successful, the {@link Try} instance should have the following behaviors</dd>
 *      <ol>
 *          <li>{@code isSuccess()} is true</li>
 *          <li>{@code isFailure()} is false</li>
 *          <li>{@code get()} returns a non-null value</li>
 *      </ol>
 *     </dd>
 *     <dt>Failure</dt>
 *     <dd>When an operation is a failure, the {@link Try} instance should have the following behaviors</dd>
 *      <ol>
 *          <li>{@code isSuccess()} is false</li>
 *          <li>{@code isFailure()} is true</li>
 *          <li>{@code get()} throws an IllegalStateException</li>
 *      </ol>
 *     </dd>
 * </dl>
 * </p>
 */

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TryContractTest
{
    @Test
    @Order(1)
    void givenSuccessfulAttempt_whenChecked_thenConformsToSuccessContract() {
        var attempt = attempt(() -> Integer.parseInt("123"));

        assertTrue(attempt instanceof Success, "Attempt should be a Success");
        assertTrue(attempt.isSuccess(), "Attempt should be successful");
        assertEquals(123, attempt.get(), "Attempt should return value");

        assertFalse(attempt.isFailure(), "Attempt should not be a failure");
    }

    @Test
    @Order(10)
    void givenFailedAttempt_whenChecked_thenConformsToFailureContract() {
        var attempt = attempt(() -> Integer.parseInt("abc"));

        assertTrue(attempt instanceof Failure, "Attempt should be a Failure");
        assertTrue(attempt.isFailure(), "Attempt should be a failure");

        assertFalse(attempt.isSuccess(), "Attempt should not be successful");
        assertThrows(IllegalStateException.class, attempt::get, "Attempt should throw exception");
    }

    @Test
    @Order(10)
    void givenSuccessfulOperation_whenMap_thenTransformsValue() {
        var attempt = attempt(() -> Integer.parseInt("123"));
        Try<String> transformed = attempt.map(Object::toString);
        assertTrue(transformed.isSuccess());
        assertEquals("123", transformed.get());
    }

    @Test
    @Order(12)
    void givenFailedAttempt_whenMapping_thenDoesNotTransformValue() {
        var attempt = attempt(() -> Integer.parseInt("abc"));
        var transformed = attempt.map(Object::toString);
        assertFalse(transformed.isSuccess());
        assertThrows(IllegalStateException.class, transformed::get);
    }
}

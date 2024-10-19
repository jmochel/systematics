package org.saltations.systematics.core;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.systematics.test.fixture.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saltations.systematics.core.Outcome.attempt;

/**
 * Confirms the basic contract of the {@link Outcome} class.
 * <p>
 * The basic contract looks like this:
 * <dl>
 *     <dt>Success</dt>
 *     <dd>When an operation is successful, the {@link Outcome} instance should have the following behaviors</dd>
 *      <ol>
 *          <li>{@code isSuccess()} is true</li>
 *          <li>{@code isFailure()} is false</li>
 *          <li>{@code get()} returns a non-null value</li>
 *      </ol>
 *     </dd>
 *     <dt>Failure</dt>
 *     <dd>When an operation is a failure, the {@link Outcome} instance should have the following behaviors</dd>
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
class OutcomeFunctionsTest
{
    @Test
    @Order(1)
    void givenSuccess_whenMapped_thenNewSuccessValue() {
        var success = attempt(() -> Integer.parseInt("123"));
        var mapped = success.map(Object::toString);

        assertTrue(mapped instanceof Success, "Should be a Success");
        assertTrue(mapped.isSuccess(), "Should be successful");
        assertEquals("123", mapped.get(), "Returns new value");
    }

    @Test
    @Order(2)
    void givenFailure_whenMapped_thenConformsToFailureContract() {
        var failure = attempt(() -> Integer.parseInt("abc"));
        var mapped = failure.map(Object::toString);

        assertTrue(mapped instanceof Failure, "Attempt should be a Failure");
        assertTrue(mapped.isFailure(), "Attempt should be a failure");
        assertEquals(failure, mapped, "Should be the same failure");
    }

    @Test
    @Order(10)
    void givenSuccess_whenOnSuccess_thenConsumesValue()
    {
        var success = attempt(() -> Integer.parseInt("123"));
        var consumer = new SuccessConsumer();

        success.onSuccess(consumer);
        assertEquals(123, consumer.getConsumedValue(), "Should have consumed the value");
    }

    @Test
    @Order(12)
    void givenFailure_whenOnSuccess_thenNothingGetsConsumed()
    {
        var failure = attempt(() -> Integer.parseInt("abc"));
        var consumer = new SuccessConsumer();

        failure.onSuccess(consumer);
        assertEquals(null, consumer.getConsumedValue(), "Should not have consumed the value");
    }

    @Test
    @Order(20)
    void givenSuccess_whenOnFailure_thenConsumerNotCalled()
    {
        var success = attempt(() -> Integer.parseInt("123"));
        var consumer = new FailureConsumer();

        success.onFailure(consumer);
        assertFalse(consumer.wasCalled(), "Should not have consumed the value");
    }

    @Test
    @Order(22)
    void givenFailure_whenOnFailure_thenConsumerIsCalled()
    {
        var failure = attempt(() -> Integer.parseInt("abc"));
        var consumer = new FailureConsumer();

        failure.onFailure(consumer);
        assertTrue(consumer.wasCalled(), "Should have called the consumer");
    }


    @Test
    @Order(30)
    void givenSuccess_whenOrElse_thenSupplierNotCalled()
    {
        var success = attempt(() -> Integer.parseInt("123"));
        var supplier = new IntegerSupplier();

        var alternativeOutcome = success.orElse(supplier);
        assertEquals(success, alternativeOutcome, "Should have returned the original outcome");
    }

    @Test
    @Order(32)
    void givenFailure_whenOrElse_thenAlternativeIsSupplied()
    {
        var failure = attempt(() -> Integer.parseInt("abc"));
        var supplier = new IntegerSupplier();

        var alternativeOutcome = failure.orElse(supplier);
        assertInstanceOf(Success.class, alternativeOutcome, "Should have returned a success");
        assertEquals(246, alternativeOutcome.get(), "Should have returned the alternative value");
    }

    static class FailureConsumer implements Consumer<Failure>
    {
        boolean called = false;

        public boolean wasCalled()
        {
            return called;
        }

        @Override
        public void accept(Failure failure)
        {
            this.called = true;
        }
    }

    static class SuccessConsumer implements Consumer<Integer>
    {
        Integer consumedValue = null;

        public Integer getConsumedValue()
        {
            return consumedValue;
        }

        @Override
        public void accept(Integer value)
        {
            this.consumedValue = value;
        }
    }

    static class IntegerSupplier implements Supplier<Outcome<Integer>>
    {
        @Override
        public Outcome<Integer> get()
        {
            return Outcomes.success(246);
        }
    }

    static class IntegerMurphysSuccessfulSupplier implements MurphysSupplier<Outcome<Integer>>
    {
        @Override
        public Outcome<Integer> supply() throws Exception
        {
            return Outcomes.success(246);
        }
    }

    static class IntegerMurphysKaboomSupplier implements MurphysSupplier<Outcome<Integer>>
    {
        @Override
        public Outcome<Integer> supply() throws Exception
        {
            throw new IllegalArgumentException("Kaboom!");
        }
    }
}

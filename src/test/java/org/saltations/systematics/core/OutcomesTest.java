package org.saltations.systematics.core;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.saltations.systematics.test.fixture.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
class OutcomesTest {

    @Test
    void givenNoValue_whenSuccess_thenReturnsDefaultSuccess() {
        var outcome = Outcomes.success();
        assertTrue(outcome instanceof Success);
        assertTrue(outcome.isSuccess());
        assertEquals(true, outcome.get());
    }

    @Test
    void givenValue_whenSuccess_thenReturnsSuccessWithValue() {
        var outcome = Outcomes.success("test");
        assertTrue(outcome instanceof Success);
        assertTrue(outcome.isSuccess());
        assertEquals("test", outcome.get());
    }

    @Test
    void whenGenericFailure_thenReturnsFailure() {
        var outcome = Outcomes.genericFailure();
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    @Test
    void givenTitleAndTemplate_whenGenericFailure_thenReturnsFailureWithTitleAndMessage() {
        var outcome = Outcomes.genericFailure("Test Title", "Test Message: {0}", "Detail");
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertEquals("Test Title", outcome.title());
        assertEquals("Test Message: Detail", outcome.detail());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    @Test
    void givenException_whenCausedFailure_thenReturnsFailureWithCause() {
        Exception cause = new Exception("Test Exception");
        var outcome = Outcomes.causedFailure(cause);
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertEquals(cause, outcome.cause());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    @Test
    void givenExceptionAndTitle_whenCausedFailure_thenReturnsFailureWithCauseAndTitle() {
        Exception cause = new Exception("Test Exception");
        var outcome = Outcomes.causedFailure(cause, "Test Title");
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertEquals("Test Title", outcome.title());
        assertEquals(cause, outcome.cause());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    @Test
    void givenExceptionTitleAndTemplate_whenCausedFailure_thenReturnsFailureWithCauseTitleAndMessage() {
        Exception cause = new Exception("Test Exception");
        var outcome = Outcomes.causedFailure(cause, "Test Title", "Test Message: {0}", "Detail");
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertEquals("Test Title", outcome.title());
        assertEquals("Test Message: Detail", outcome.detail());
        assertEquals(cause, outcome.cause());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    @Test
    void givenFailureType_whenTypedFailure_thenReturnsFailureWithTypeAndMessage() {
        var outcome = Outcomes.typedFailure(NewFailureType.NEW_FAILURE, "Detail");
        assertTrue(outcome instanceof Failure);
        assertTrue(outcome.isFailure());
        assertEquals(NewFailureType.NEW_FAILURE, outcome.type());
        assertEquals("New failure: Detail", outcome.detail());
        assertThrows(IllegalStateException.class, outcome::get);
    }

    enum NewFailureType implements FailureType {
        NEW_FAILURE("New failure", "New failure: {0}");

        private final String title;
        private final String template;

        NewFailureType(String title, String template) {
            this.title = title;
            this.template = template;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public String template() {
            return template;
        }
    }
}


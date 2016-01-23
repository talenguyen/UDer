package com.tale.uder.ui.presenters;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import rx.schedulers.Schedulers;

public class ItemsPresenterConfigurationTest {

  @Test public void hashCode_equals_shouldWorkCorrectly() {
    EqualsVerifier.forExamples(
        ItemsPresenterConfiguration.builder().ioScheduler(Schedulers.immediate()).build(),
        ItemsPresenterConfiguration.builder().ioScheduler(Schedulers.io()).build()).suppress(
        Warning.NULL_FIELDS) // AutoValue checks for nulls, but EqualsVerifier does not expect that by default.
        .verify();
  }
}
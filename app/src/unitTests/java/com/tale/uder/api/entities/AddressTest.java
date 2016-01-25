package com.tale.uder.api.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class AddressTest {

  @Test public void hashCode_equals_shouldWorkCorrectly() {
    EqualsVerifier.forExamples(Address.builder()
        .placeId("id1")
        .formattedAddress("Address1")
        .geometry(new Geometry())
        .build(), Address.builder()
        .placeId("id2")
        .formattedAddress("Address2")
        .geometry(new Geometry())
        .build()).suppress(Warning.NULL_FIELDS) // AutoValue checks nullability, EqualsVerifier does not expect that by default.
        .verify();
  }
}
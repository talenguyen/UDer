package com.tale.uder.integration_tests.api.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tale.uder.UderIntegrationRobolectricTestRunner;
import com.tale.uder.api.entities.Item;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(UderIntegrationRobolectricTestRunner.class) public class ItemTest {

  // Why test JSON serialization/deserialization?
  // 1. Update JSON libraries without worrying about breaking changes.
  // 2. Be sure that @JsonIgnore and similar annotations do not affect expected behavior (cc @karlicos).
  @Test public void fromJson() throws IOException {
    ObjectMapper objectMapper = UderIntegrationRobolectricTestRunner.qualityMattersApp()
        .applicationComponent()
        .objectMapper();

    Item item = objectMapper.readValue("{ " +
            "\"id\": \"test_id\", " +
            "\"title\": \"Test title\", " +
            "\"short_description\": \"Test short description\"" +
            "}", Item.class);

    assertThat(item.id()).isEqualTo("test_id");
    assertThat(item.title()).isEqualTo("Test title");
    assertThat(item.shortDescription()).isEqualTo("Test short description");
  }
}

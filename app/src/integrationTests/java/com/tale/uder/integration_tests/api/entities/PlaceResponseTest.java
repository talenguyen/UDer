/**
 * uder
 *
 * Created by Giang Nguyen on 1/25/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.integration_tests.api.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tale.uder.UderIntegrationRobolectricTestRunner;
import com.tale.uder.api.entities.Address;
import com.tale.uder.api.entities.PlaceResponse;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(UderIntegrationRobolectricTestRunner.class) public class PlaceResponseTest {
  private static final String SAMPLE_RESPONSE = "{\n"
      + "   \"results\" : [\n"
      + "      {\n"
      + "         \"address_components\" : [\n"
      + "            {\n"
      + "               \"long_name\" : \"277\",\n"
      + "               \"short_name\" : \"277\",\n"
      + "               \"types\" : [ \"street_number\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"Bedford Ave\",\n"
      + "               \"short_name\" : \"Bedford Ave\",\n"
      + "               \"types\" : [ \"route\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"Williamsburg\",\n"
      + "               \"short_name\" : \"Williamsburg\",\n"
      + "               \"types\" : [ \"neighborhood\", \"political\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"Brooklyn\",\n"
      + "               \"short_name\" : \"Brooklyn\",\n"
      + "               \"types\" : [ \"sublocality_level_1\", \"sublocality\", \"political\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"Kings County\",\n"
      + "               \"short_name\" : \"Kings County\",\n"
      + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"New York\",\n"
      + "               \"short_name\" : \"NY\",\n"
      + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"United States\",\n"
      + "               \"short_name\" : \"US\",\n"
      + "               \"types\" : [ \"country\", \"political\" ]\n"
      + "            },\n"
      + "            {\n"
      + "               \"long_name\" : \"11211\",\n"
      + "               \"short_name\" : \"11211\",\n"
      + "               \"types\" : [ \"postal_code\" ]\n"
      + "            }\n"
      + "         ],\n"
      + "         \"formatted_address\" : \"277 Bedford Ave, Brooklyn, NY 11211, USA\",\n"
      + "         \"geometry\" : {\n"
      + "            \"location\" : {\n"
      + "               \"lat\" : 40.714232,\n"
      + "               \"lng\" : -73.9612889\n"
      + "            },\n"
      + "            \"location_type\" : \"ROOFTOP\",\n"
      + "            \"viewport\" : {\n"
      + "               \"northeast\" : {\n"
      + "                  \"lat\" : 40.7155809802915,\n"
      + "                  \"lng\" : -73.9599399197085\n"
      + "               },\n"
      + "               \"southwest\" : {\n"
      + "                  \"lat\" : 40.7128830197085,\n"
      + "                  \"lng\" : -73.96263788029151\n"
      + "               }\n"
      + "            }\n"
      + "         },\n"
      + "         \"partial_match\" : true,\n"
      + "         \"place_id\" : \"ChIJd8BlQ2BZwokRAFUEcm_qrcA\",\n"
      + "         \"types\" : [ \"street_address\" ]\n"
      + "      }\n"
      + "   ],\n"
      + "   \"status\" : \"OK\"\n"
      + "}";

  // Why test JSON serialization/deserialization?
  // 1. Update JSON libraries without worrying about breaking changes.
  // 2. Be sure that @JsonIgnore and similar annotations do not affect expected behavior (cc @karlicos).
  @Test public void fromJson() throws IOException {
    ObjectMapper objectMapper =
        UderIntegrationRobolectricTestRunner.udersApp().applicationComponent().objectMapper();

    PlaceResponse item = objectMapper.readValue(SAMPLE_RESPONSE, PlaceResponse.class);

    assertThat(item.results).hasSize(1);
    final Address address = item.results.get(0);
    assertThat(address.formattedAddress()).isEqualTo("277 Bedford Ave, Brooklyn, NY 11211, USA");
    assertThat(address.placeId()).isEqualTo("ChIJd8BlQ2BZwokRAFUEcm_qrcA");
  }
}

package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.api.entities.DirectionResponse;
import com.tale.uder.api.entities.Distance;
import com.tale.uder.api.entities.Leg;
import com.tale.uder.api.entities.Location;
import com.tale.uder.api.entities.Route;
import com.tale.uder.api.entities.Step;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DirectionModelTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private UderRestApi uderRestApi;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private DirectionModel directionModel;

  @Before public void beforeEachTest() {
    uderRestApi = mock(UderRestApi.class);
    directionModel = new DirectionModel(uderRestApi);
  }

  @Test public void calculatePoints_shouldReturnResultSizeEqualStepAddingByOne() throws Exception {
    final int step = 3;
    assertThat(directionModel.calculatePoints(Collections.singletonList(newRoute(step)))
        .toBlocking()
        .value()).hasSize(step + 1);
  }

  @Test public void getDirection_shouldReturnErrorFromUderRestApi() throws Exception {
    Exception error = new RuntimeException();
    when(uderRestApi.getDirection(anyString(), anyString(), eq(Constants.API_KEY))).thenReturn(
        Single.error(error));

    try {
      directionModel.getDirection("origin", "destination").toBlocking().value();
      failBecauseExceptionWasNotThrown(RuntimeException.class);
    } catch (Exception expected) {
      assertThat(expected).isSameAs(error);
    }
  }

  @Test public void calculatePoints_shouldReturnALinearPointArray() throws Exception {

  }

  @Test public void selectShortestLeg_shouldReturnShortestDistanceLeg() throws Exception {
    final Leg shortest = newLeg(0, 1);
    Leg result =
        directionModel.selectShortestLeg(Arrays.asList(shortest, newLeg(1, 1), newLeg(2, 2)));
    assertThat(result).isEqualTo(shortest);
    result = directionModel.selectShortestLeg(Arrays.asList(newLeg(1, 1), shortest, newLeg(2, 2)));
    assertThat(result).isEqualTo(shortest);
    result = directionModel.selectShortestLeg(Arrays.asList(newLeg(1, 1), newLeg(2, 2), shortest));
    assertThat(result).isEqualTo(shortest);
  }

  @NonNull private DirectionResponse mockDirectionResponse(int step) {
    final DirectionResponse directionResponse = new DirectionResponse();
    directionResponse.routes = new ArrayList<>(1);
    directionResponse.routes.add(newRoute(step));
    return directionResponse;
  }

  @NonNull private Route newRoute(int step) {
    final Route route = new Route();
    route.legs = new ArrayList<>(1);
    route.legs.add(newLeg(0, step));
    return route;
  }

  @NonNull private Leg newLeg(long distanceValue, int step) {
    final Leg leg = new Leg();
    final Distance distance = new Distance();
    distance.value = distanceValue;
    leg.distance = distance;
    leg.steps = new ArrayList<>(step);
    for (int i = 0; i < step; i++) {
      leg.steps.add(newStep(i, i + 1));
    }
    return leg;
  }

  @NonNull private Step newStep(double start, double end) {
    final Step step = new Step();
    step.startLocation = newLocation(start);
    step.endLocation = newLocation(end);
    return step;
  }

  @NonNull private Location newLocation(double mockValue) {
    final Location location = new Location();
    location.lat = mockValue;
    location.lng = mockValue;
    return location;
  }
}
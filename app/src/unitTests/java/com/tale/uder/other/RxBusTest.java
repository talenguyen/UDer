/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.other;

import org.junit.Before;
import org.junit.Test;
import rx.Subscription;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class RxBusTest {

  private RxBus bus;

  @Before public void beforeEachTest() {
    bus = new RxBus();
  }

  @Test public void hasObservers_shouldReturnFalseByDefault() {
    assertThat(bus.hasObservers()).isFalse();
  }

  @Test public void hasObservers_shouldReturnTrueAfterSubscribe() {
    final Action1<Object> action1 = mock(Action1.class);
    bus.asObservable().subscribe(action1);
    assertThat(bus.hasObservers()).isTrue();
  }

  @Test public void hasObservers_shouldReturnFalseAfterUnsubscribe() {
    final Action1<Object> action1 = mock(Action1.class);
    final Subscription subscription = bus.asObservable().subscribe(action1);
    subscription.unsubscribe();
    assertThat(bus.hasObservers()).isFalse();
  }

  @Test public void send_subscriberShouldNotReceiveAnyEventBeforeSend() {
    final Action1<Object> action1 = mock(Action1.class);
    bus.asObservable().subscribe(action1);
    verifyZeroInteractions(action1);
  }

  @Test public void send_subscriberShouldReceiveEventAfterSend() {
    final Action1<Object> action1 = mock(Action1.class);
    bus.asObservable().subscribe(action1);
    final Object event = mock(Object.class);
    bus.send(event);
    verify(action1).call(event);
  }

  @Test public void send_allSubscribersShouldReceiveEventAfterSend() {
    final Action1<Object> action1 = mock(Action1.class);
    bus.asObservable().subscribe(action1);
    final Action1<Object> action2 = mock(Action1.class);
    bus.asObservable().subscribe(action2);
    final Object event = mock(Object.class);
    bus.send(event);
    verify(action1).call(event);
    verify(action2).call(event);
  }

  @Test public void send_subscriberShouldNotReceiveAnyEventAfterUnsubscribe() {
    final Action1<Object> action1 = mock(Action1.class);
    final Subscription subscription = bus.asObservable().subscribe(action1);
    subscription.unsubscribe();
    final Object event = mock(Object.class);
    bus.send(event);
    verifyZeroInteractions(action1);
  }


  @Test public void send_allSubscribersShouldNotReceiveAnyEventAfterUnsubscribe() {
    final Action1<Object> action1 = mock(Action1.class);
    final Subscription subscription1 = bus.asObservable().subscribe(action1);
    final Action1<Object> action2 = mock(Action1.class);
    final Subscription subscription2 = bus.asObservable().subscribe(action2);
    subscription1.unsubscribe();
    subscription2.unsubscribe();
    final Object event = mock(Object.class);
    bus.send(event);
    verifyZeroInteractions(action1);
    verifyZeroInteractions(action2);
  }

}

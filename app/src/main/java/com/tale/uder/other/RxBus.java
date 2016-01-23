/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.other;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

  private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

  public void send(Object event) {
    bus.onNext(event);
  }

  public Observable<Object> asObservable() {
    return bus;
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }
}

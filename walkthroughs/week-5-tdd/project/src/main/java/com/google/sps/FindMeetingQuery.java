// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/** Finds list of all potential calendar time slots to schedule a meeting with the given 
 * MeetingRequest constraints. Accounts for optional attendees when possible. */
public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ImmutableList<TimeRange> conflicting = getAttendeeTimeRanges(request.getAttendees(), events);
    ImmutableList<TimeRange> times = getMeetingTimes(request.getDuration(), conflicting);

    ImmutableList<String> allAttendees = 
        Stream.concat(request.getAttendees().stream(), request.getOptionalAttendees().stream())
            .collect(ImmutableList.toImmutableList());
    
    ImmutableList<TimeRange> optionalConflicting = getAttendeeTimeRanges(allAttendees, events);
    ImmutableList<TimeRange> timesWithOptional = getMeetingTimes(request.getDuration(), optionalConflicting);

    if (timesWithOptional.isEmpty() && !request.getAttendees().isEmpty()) {
      return times;
    }
    return timesWithOptional;
  }

  // Returns list of events that at least one of given attendees is going to.
  private ImmutableList<TimeRange> getAttendeeTimeRanges(Collection<String> attendees, Collection<Event> events) {
    return events.stream()
        .filter(evt -> evt.getAttendees().stream().anyMatch(attendees::contains))
        .map(Event::getWhen)
        .sorted(TimeRange.ORDER_BY_START)
        .collect(ImmutableList.toImmutableList());
  }

  // Returns list of time slots that fit the given list of potentially conflicting events.
  private ImmutableList<TimeRange> getMeetingTimes(long duration, List<TimeRange> conflicting) {
    List<TimeRange> times = new ArrayList<TimeRange>();
    int busyUntil = TimeRange.START_OF_DAY;
    for (TimeRange time : conflicting) {
      if (time.start() > busyUntil) {
        TimeRange meetingSlot = TimeRange.fromStartEnd(busyUntil, time.start(), false);
        if (meetingSlot.duration() >= duration) {
          times.add(meetingSlot);
        }
        busyUntil = time.end();
      } else if (time.end() > busyUntil) {
        busyUntil = time.end();
      }
    }
    if (busyUntil < TimeRange.END_OF_DAY && (TimeRange.END_OF_DAY - busyUntil >= duration)) {
      times.add(TimeRange.fromStartEnd(busyUntil, TimeRange.END_OF_DAY, true));
    }
    return ImmutableList.copyOf(times);
  }
}

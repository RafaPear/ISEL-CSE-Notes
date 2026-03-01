#include <stdio.h>

struct hmstime { unsigned short hours; unsigned char minutes; unsigned char seconds; };

void sumtimes(struct hmstime *res, const struct hmstime times[], size_t ntimes){
  if (ntimes == 0) return;
  unsigned short total_seconds = 0;
  unsigned short total_minutes = 0;
  unsigned short total_hours = 0;

  for (size_t i = 0; i < ntimes; i++) {
    total_seconds += times[i].seconds;
    total_minutes += times[i].minutes;
    total_hours += times[i].hours;
  }

  total_minutes += total_seconds / 60;
  total_seconds %= 60;
  total_hours += total_minutes / 60;
  total_minutes %= 60;

  res->hours = total_hours;
  res->minutes = total_minutes;
  res->seconds = total_seconds;
}
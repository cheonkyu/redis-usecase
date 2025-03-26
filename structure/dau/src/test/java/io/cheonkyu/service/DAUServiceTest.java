package io.cheonkyu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DAUServiceTest {
  @Autowired
  private DAUService dauService;

  @DisplayName("레디스, 이벤트 DAU 구하기")
  @Test
  void testRecordUserActivity() {
    final String DATE_YYYYMMDD = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    dauService.recordUserActivity("user1", DATE_YYYYMMDD);
    dauService.recordUserActivity("user2", DATE_YYYYMMDD);
    dauService.recordUserActivity("user3", DATE_YYYYMMDD);

    Long count = dauService.calculateDAU(DATE_YYYYMMDD);
    assertEquals(count, 3L);
  }

  @DisplayName("레디스, 이벤트 연속참여일 구하기 BITMAP AND 활용")
  @Test
  void testEventDate() {
    final String DATE_20250326 = LocalDate.of(2025, 3, 26).format(DateTimeFormatter.BASIC_ISO_DATE);
    dauService.recordUserActivity("user1", DATE_20250326);
    dauService.recordUserActivity("user2", DATE_20250326);
    dauService.recordUserActivity("user3", DATE_20250326);

    final String DATE_20250325 = LocalDate.of(2025, 3, 25).format(DateTimeFormatter.BASIC_ISO_DATE);
    dauService.recordUserActivity("user1", DATE_20250325);

    dauService.eventDateByBitAnd(DATE_20250326, DATE_20250325, "EVENT_202503");
  }
}

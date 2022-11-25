package me.whiteship.inflearnthejavatest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @Test
    void create(){
        Study study = new Study();

        assertAll(
                ()->assertNotNull(study),
                ()->assertThat(3).isEqualTo(4),
                ()->assertTrue(study.getLimit()>0,()->"limit 은 0을 넘어야한다."),
                ()->assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        ()->"스터디를 처음 만들면 상태값이 DRAFT 여야한다.")
        );

    }

    @Test
    void exception(){
        Study study = new Study();

        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new Study(-10));

        assertEquals("0보다 작으면 안됩니다.",exception.getMessage());

    }

    @Test
    void timeout(){
        assertTimeout(Duration.ofSeconds(10),()->new Study(10));
        assertTimeoutPreemptively(Duration.ofSeconds(10),()->new Study(10));
    }
}
package me.whiteship.inflearnthejavatest;

import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.study.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension=new FindSlowTestExtension(1000);

    @Test
    @Tag("local")
    @Order(1)
    @SlowTest
    void create_study() throws InterruptedException {
        Study study = new Study();
        Thread.sleep(1005L);
        assertAll(
                ()->assertNotNull(study),
//                ()->assertThat(3).isEqualTo(4),
//                ()->assertTrue(study.getLimit()>0,()->"limit 은 0을 넘어야한다."),
                ()->assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        ()->"스터디를 처음 만들면 상태값이 DRAFT 여야한다.")
        );

    }

    @Test
    @Order(3)
    void exception(){
        Study study = new Study();

        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> new Study(-10));

        assertEquals("limit은 0보다 커야 한다.",exception.getMessage());

    }

    @Test
    @Order(2)
    void timeout(){
        assertTimeout(Duration.ofSeconds(10),()->new Study(10));
        assertTimeoutPreemptively(Duration.ofSeconds(10),()->new Study(10));
    }

//    @Test
//    void assume(){
//        String test_env = System.getenv("TEST_ENV");
//        System.out.println("test_env = " + test_env);
//        Assumptions.assumeTrue("LOCAL".equalsIgnoreCase(test_env));
//    }

    @DisplayName("반복 테스트 하기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest(RepetitionInfo repetitionInfo){
        System.out.println("test"+repetitionInfo.getCurrentRepetition()+"/"
                +repetitionInfo.getTotalRepetitions());
    }


    @DisplayName("파라미터 전달")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"kim, user ,1234", "min, admin, 1234"})
    void parameterTest(@ConvertWith(StudyConverter.class) MemberInfo member){

        System.out.println("member.getName() = " + member.getName());
        System.out.println("member.getUsername() = " + member.getUsername());
        System.out.println("member.getPassword() = " + member.getPassword());

    }

    @DisplayName("파라미터 전달")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, 김민성","20, 김민수"})
    void parameterTest2(ArgumentsAccessor argumentsAccessor){

        Person person
                = new Person(argumentsAccessor.getString(1), argumentsAccessor.getInteger(0));
    }

    @DisplayName("파라미터 전달")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, 김민성","20, 김민수"})
    void parameterTest3 (@AggregateWith(StudyAggregator.class) Person person){
        System.out.println("person.getName() = " + person.getName());
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {

            return new Person(argumentsAccessor.getString(1), argumentsAccessor.getInteger(0));
        }
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object info, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(MemberInfo.class,aClass,"멤버타입이어야 합니다.");
            String[] split = info.toString().split(",");
             return new MemberInfo(split[0], split[1], split[2]);
        }
    }


}
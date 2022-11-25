package me.whiteship.inflearnthejavatest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Study {

    private int limit;

    private StudyStatus status=StudyStatus.DRAFT;

    public Study(int limit){
        if(limit<0){
            throw new IllegalArgumentException("0보다 작으면 안됩니다.");
        }
    }
}

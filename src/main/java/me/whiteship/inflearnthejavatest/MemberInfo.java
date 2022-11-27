package me.whiteship.inflearnthejavatest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfo {
    private String name;
    private String username;
    private String password;
}

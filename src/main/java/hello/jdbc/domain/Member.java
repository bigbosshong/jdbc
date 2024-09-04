package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    private String memberId;
    private int money;

    public Member() {
    }

    public Member(final String member, final int money) {
        this.memberId = member;
        this.money = money;
    }
}

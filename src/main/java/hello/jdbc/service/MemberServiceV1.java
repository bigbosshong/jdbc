package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        final Member fromMember = memberRepository.findById(fromId);
        final Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - amount);
        validateMember(toMember);
        memberRepository.update(toId, toMember.getMoney() + amount);
    }

    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex")) {
            throw new IllegalArgumentException("이체 중 예외 발생");
        }
    }
}

package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        final Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, amount);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void bizLogic(final Connection con, final String fromId, final String toId, final int amount) throws SQLException {
        final Member fromMember = memberRepository.findById(con, fromId);
        final Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - amount);
        validateMember(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + amount);
    }

    private void release(final Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex")) {
            throw new IllegalArgumentException("이체 중 예외 발생");
        }
    }
}

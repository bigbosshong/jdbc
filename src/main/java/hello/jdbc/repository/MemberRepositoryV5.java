package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;


/**
 * JDBC - jdbcTemplate 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV5 implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryV5(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id, money) values(?, ?)";
        jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return jdbcTemplate.queryForObject(sql, getMemberRowMapper(), memberId);
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> {
            final String id = rs.getString("member_id");
            final int money = rs.getInt("money");
            return new Member(id, money);
        };
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";
        jdbcTemplate.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";
        jdbcTemplate.update(sql, memberId);
    }
}

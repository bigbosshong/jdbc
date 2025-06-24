package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id, money) values(?, ?)";

        Connection conn = null;
        PreparedStatement psmt = null;

        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, member.getMemberId());
            psmt.setInt(2, member.getMoney());
            psmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw Objects.requireNonNull(exTranslator.translate("save", sql, e));
        } finally {
            close(conn, psmt, null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else
                throw new NoSuchElementException("member not found member id : " + memberId);
        } catch (SQLException e) {
            log.error("db error", e);
            throw Objects.requireNonNull(exTranslator.translate("findById", sql, e));
        } finally {
            close(conn, psmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money) {

        String sql = "update member set money = ? where member_id = ?";
        Connection conn = null;
        PreparedStatement psmt = null;

        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, money);
            psmt.setString(2, memberId);

            final int resultSize = psmt.executeUpdate();
            log.info("resultSize : " + resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw Objects.requireNonNull(exTranslator.translate("update", sql, e));
        } finally {
            close(conn, psmt, null);
        }

    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement psmt = null;

        try {
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);
            psmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw Objects.requireNonNull(exTranslator.translate("delete", sql, e));
        } finally {
            close(conn, psmt, null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //주의!
        DataSourceUtils.releaseConnection(conn, dataSource);
    }


    private Connection getConnection() throws SQLException {
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("connection : {}, class : {}", connection, connection.getClass());
        return connection;
    }
}

package my.edu.umk.smart.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import my.edu.umk.pams.connector.payload.StudentPayload;

/**
 */
@RestController
@Transactional
@RequestMapping("/api/smart")
public class SmartController {

    @Autowired
    private JdbcTemplate oraJdbcTemplate;

    @RequestMapping(value = "/student/smartCardPelajar", method = RequestMethod.POST)
    public ResponseEntity<String> smartCardPelajar(@RequestBody StudentPayload payload) {

        final String sql = "insert into smartcard_pelajar(matricNo,name) values(?,?)";

        oraJdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, payload.getMatricNo());
                ps.setString(2, payload.getName());
                return ps;
            }
        });
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/student/smartCardEncodePelajar", method = RequestMethod.POST)
    public ResponseEntity<String> smartCardEncodePelajar(@RequestBody StudentPayload payload) {

        final String sql = "insert into smartcard_encode_pelajar(matricNo,name) values(?,?)";

        oraJdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, payload.getMatricNo());
                ps.setString(2, payload.getName());
                return ps;
            }
        });
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
}

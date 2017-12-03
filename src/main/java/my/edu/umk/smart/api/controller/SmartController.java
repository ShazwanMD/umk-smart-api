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

import my.edu.umk.pams.connector.payload.CandidatePayload;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

/**
 */
@RestController
@Transactional
@RequestMapping("/api/smart")
public class SmartController {

	@Autowired
	private JdbcTemplate oraJdbcTemplate;

	@Autowired
	private JdbcTemplate mysqlJdbcTemplate;

	@RequestMapping(value = "/student/smartCardPelajar", method = RequestMethod.POST)
	public ResponseEntity<String> smartCardPelajar(@RequestBody CandidatePayload payload) {

		Date dNow = new java.util.Date();
		java.sql.Date sqldNow = new java.sql.Date(dNow.getTime());

		final String sql = "insert into smartcard_pelajar(NO_MATRIK,no_library,no_student,nama_pendek,nama,kod_kursus,ktrgn_kursus,kod_fakulti,"
				+ "ktrgn_fakulti,no_kp,tarikh_keluar) values(?,?,?,?,?,?,?,?,?,?,?)";

		oraJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, payload.getMatricNo());
				ps.setString(2, payload.getMatricNo());
				ps.setString(3, payload.getMatricNo());
				if (payload.getName().length() >= 29) {
					ps.setString(4, payload.getName().substring(0, 29));
				} else {
					ps.setString(4, payload.getName());
				}
				ps.setString(5, payload.getName());
				ps.setString(6, payload.getProgramCode().getCode());
				ps.setString(7, payload.getProgramCode().getDescriptionMs());
				ps.setString(8, payload.getFacultyCode().getCode());
				ps.setString(9, payload.getFacultyCode().getDescription());
				ps.setString(10, payload.getUserPayload().getNric());
				ps.setDate(11, sqldNow);
				return ps;
			}
		});
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/student/smartCardEncodePelajar", method = RequestMethod.POST)
	public ResponseEntity<String> smartCardEncodePelajar(@RequestBody CandidatePayload payload) {
		final String KATEGORI = payload.getProgramLevel();
		final String jantina = payload.getGender();
		final String sql = "insert into smartcard_encode_pelajar(KATEGORI,NO_MATRIK,nama,kod_kursus,FAK_KOD,no_kp,NOLIBRARY,WARGA,NEGERI,JANTINA) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";

		oraJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				if (KATEGORI.equals("MASTER")) {
					final String newKATEGORI = "PM";
					ps.setString(1, newKATEGORI);
				} else if (KATEGORI.equals("PHD")) {
					final String newKATEGORI = "PP";
					ps.setString(1, newKATEGORI);
				}
				ps.setString(2, payload.getMatricNo());
				ps.setString(3, payload.getName());
				ps.setString(4, payload.getProgramCode().getCode());
				ps.setString(5, payload.getFacultyCode().getCode());
				ps.setString(6, payload.getUserPayload().getNric());
				ps.setString(7, payload.getMatricNo());
				ps.setString(8, payload.getNationalityCode().getCode());
				ps.setString(9, payload.getPrimaryAddress().getStateCode());
				if (jantina.equals("1")) {
					ps.setString(10, "L");
				} else {
					ps.setString(10, "P");
				}
				return ps;
			}
		});
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/student/radiusManager/rmUser", method = RequestMethod.POST)
	public ResponseEntity<String> radiusUser(@RequestBody CandidatePayload payload) {

		String nric = payload.getUserPayload().getNric();

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(nric.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			nric = hash.toString(16);
			while (nric.length() < 32) { // 40 for SHA-1
				nric = "0" + nric;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // or "SHA-1"
		final String md5Nric = nric;

		Date dNow = new java.util.Date();

		Date date = new java.util.Date();

		java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		java.sql.Date sqldNow = new java.sql.Date(dNow.getTime());

		final String address = payload.getPrimaryAddress().getAddress1() + ","
				+ payload.getPrimaryAddress().getAddress2() + "," + payload.getPrimaryAddress().getAddress3();

		final String sql = "insert into rm_users(" + "username," // 1
				+ "password," // 2
				+ "groupid," // 3
				+ "enableuser," // 4
				+ "uplimit," // 5
				+ "downlimit," // 6
				+ "comblimit," // 7
				+ "firstname," // 8
				+ "lastname," // 9
				+ "company," // 10
				+ "phone," // 11
				+ "mobile," // 12
				+ "address," // 13
				+ "city," // 14
				+ "zip," // 15
				+ "country," // 16
				+ "state," // 17
				+ "comment," // 18
				+ "gpslat," // 19
				+ "gpslong," // 20
				+ "mac," // 21
				+ "usemacauth," // 22
				+ "expiration," // 23
				+ "uptimelimit,"// 24
				+ "srvid," // 25
				+ "staticipcm," // 26
				+ "ipmodecm," // 27
				+ "ipmodecpe," // 28
				+ "poolidcm," // 29
				+ "poolidcpe," // 30
				+ "createdon," // 31
				+ "acctype," // 32
				+ "credits," // 33
				+ "cardfails," // 34
				+ "createdby," // 35
				+ "owner," // 36
				+ "taxid," // 37
				+ "email," // 38
				+ "maccm," // 39
				+ "custattr," // 40
				+ "warningsent,"// 41
				+ "verifycode, "// 42
				+ "verified," // 43
				+ "selfreg," // 44
				+ "verifyfails,"// 45
				+ "verifysentnum," // 46
				+ "verifymobile," // 47
				+ "contractid," // 48
				+ "contractvalid," // 49
				+ "actcode," // 50
				+ "pswactsmsnum," // 51
				+ "alertemail," // 52
				+ "alertsms," // 53
				+ "lang," // 54
				+ "courseduration," // 55
				+ "programcode," // 56
				+ "studymode," // 57
				+ "campus," // 58
				+ "ic," // 59
				+ "usertype," // 60
				+ "staticipcpe" // 61
				+ ") values(?"// 1
				+ ",?"// 2
				+ ",?"// 3
				+ ",?"// 4
				+ ",?"// 5
				+ ",?"// 6
				+ ",?"// 7
				+ ",?"// 8
				+ ",?"// 9
				+ ",?"// 10
				+ ",?"// 11
				+ ",?"// 12
				+ ",?"// 13
				+ ",?"// 14
				+ ",?"// 15
				+ ",?"// 16
				+ ",?"// 17
				+ ",?"// 18
				+ ",?"// 19
				+ ",?"// 20
				+ ",?"// 21
				+ ",?"// 22
				+ ",?"// 23
				+ ",?"// 24
				+ ",?"// 25
				+ ",?"// 26
				+ ",?"// 27
				+ ",?"// 28
				+ ",?"// 29
				+ ",?"// 30
				+ ",?"// 31
				+ ",?"// 32
				+ ",?"// 33
				+ ",?"// 34
				+ ",?"// 35
				+ ",?"// 36
				+ ",?"// 37
				+ ",?"// 38
				+ ",?"// 39
				+ ",?"// 40
				+ ",?"// 41
				+ ",?"// 42
				+ ",?"// 43
				+ ",?"// 44
				+ ",?"// 45
				+ ",?"// 46
				+ ",?"// 47
				+ ",?"// 48
				+ ",?"// 49
				+ ",?"// 50
				+ ",?"// 51
				+ ",?"// 52
				+ ",?"// 53
				+ ",?"// 54
				+ ",?"// 55
				+ ",?"// 56
				+ ",?"// 57
				+ ",?"// 58
				+ ",?"// 59
				+ ",?"// 60
				+ ",?)";// 61

		mysqlJdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, payload.getMatricNo()); // username
				ps.setString(2, md5Nric); // password
				ps.setInt(3, 0); // groupid
				ps.setInt(4, 1); // enableuser
				ps.setInt(5, 0); // uplimit
				ps.setInt(6, 0); // downlimit
				ps.setInt(7, 100000000);// comblimit
				ps.setString(8, payload.getName());// firstname
				ps.setString(9, " ");// lastname
				ps.setString(10, "UMK");// company
				ps.setString(11, " ");// phone
				if (!payload.getMobile().isEmpty()) {
					ps.setString(12, payload.getMobile());// mobile
				} else {
					ps.setString(12, " ");// mobile
				}
				ps.setString(13, address);// address
				ps.setString(14, " ");// city
				ps.setString(15, payload.getPrimaryAddress().getPostcode());// zip
				ps.setString(16, payload.getPrimaryAddress().getCountryCode());// country
				ps.setString(17, payload.getPrimaryAddress().getStateCode());// state
				ps.setString(18, " ");// comment
				ps.setInt(19, 0);// gpslat
				ps.setInt(20, 0);// gpslong
				ps.setString(21, " ");// mac
				ps.setInt(22, 0);// usemacauth
				ps.setDate(23, sqldNow);// expiration
				ps.setInt(24, 0);// uptimelimit
				ps.setInt(25, 34);// srvid
				ps.setString(26, " ");// staticipcm
				ps.setInt(27, 0);// ipmodecm
				ps.setInt(28, 0);// ipmodecpe
				ps.setInt(29, 0);// poolidcm
				ps.setInt(30, 0);// poolidcpe
				ps.setDate(31, sqlDate);// createdon
				ps.setInt(32, 0);// acctype
				ps.setFloat(33, 0);// credits
				ps.setInt(34, 0);// cardfails
				ps.setString(35, " ");// createdby
				ps.setString(36, " ");// owner
				ps.setString(37, " ");// taxid
				ps.setString(38, payload.getEmail());// email
				ps.setString(39, " ");// maccm
				ps.setString(40, " ");// custattr
				ps.setInt(41, 0);// warningsent
				ps.setString(42, " ");// verifycode
				ps.setInt(43, 0);// verified
				ps.setInt(44, 0);// selfreg
				ps.setInt(45, 0);// verifyfails
				ps.setInt(46, 0);// verifysentnum
				ps.setString(47, payload.getMobile());// verifymobile
				ps.setString(48, " ");// contractid
				ps.setDate(49, sqlDate);// contractvalid
				ps.setString(50, " ");// actcode
				ps.setInt(51, 0);// pswactsmsnum
				ps.setInt(52, 0);// alertemail
				ps.setInt(53, 0);// alertsms
				ps.setString(54, " ");// lang
				ps.setString(55, "4");// courseduration
				ps.setString(56, payload.getProgramCode().getCode());// programcode
				ps.setString(57, payload.getStudyMode().getDescription());// studymode
				ps.setString(58, " ");// campus
				ps.setString(59, payload.getUserPayload().getNric());// ic
				ps.setString(60, "student");// usertype
				ps.setString(61, " ");// usertype
				return ps;
			}
		});
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	@RequestMapping(value = "/student/radiusManager/radCheck", method = RequestMethod.POST)

	public ResponseEntity<String> radCheckUser(@RequestBody CandidatePayload payload) {
		
		
		for (int i = 0; i < 2; i++) {
			
			final String sql = "insert into radcheck(username,attribute,op,value) values(?,?,?,?)";
			if (i == 0) {
			mysqlJdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setString(1, payload.getMatricNo());
					ps.setString(2, "Simultaneous-Use");
					ps.setString(3, ":=");
					ps.setString(4, payload.getUserPayload().getNric());
					return ps;
				}
			});
			}
			else
			{
				mysqlJdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql);
						ps.setString(1, payload.getMatricNo());
						ps.setString(2, "Cleartext-Password");
						ps.setString(3, ":=");
						ps.setString(4, payload.getUserPayload().getNric());
						return ps;
					}
				});
			}

		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
}

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

        final String sql = "insert into smartcard_pelajar(NO_MATRIK,no_library,no_student,nama_pendek,nama,kod_kursus,ktrgn_kursus,kod_fakulti"
        		+ "ktrgn_fakulti,no_kp,tarikh_keluar) values(?,?,?,?,?,?,?,?,?,?,?,?)";

        oraJdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, payload.getMatricNo());
                ps.setString(2, payload.getMatricNo());
                ps.setString(3, payload.getMatricNo());
                ps.setString(4, payload.getName().substring(0, 29));
                ps.setString(5, payload.getName());
                ps.setString(6, payload.getProgramCode());
                ps.setString(7, payload.getProgramCodeDescriptionMs());
                ps.setString(8, payload.getFacultyCode());
                ps.setString(9, payload.getFacultyCodeDescriptionMs());
                ps.setString(10, payload.getIcNo());
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
                if (KATEGORI.equals("MASTER")){
            		final String newKATEGORI = "PM";
				    ps.setString(1, newKATEGORI);
                }else if (KATEGORI.equals("PHD")){
            		final String newKATEGORI = "PP";
            		ps.setString(1, newKATEGORI);
            	}
                ps.setString(2, payload.getMatricNo());
                ps.setString(3, payload.getName());
                ps.setString(4, payload.getProgramCode());
                ps.setString(5, payload.getFacultyCode());
                ps.setString(6, payload.getIcNo());
                ps.setString(7, payload.getMatricNo());
                ps.setString(8, payload.getNationalityCode().getCode());
                ps.setString(9, payload.getPrimaryAddress().getStateCode());
                if (jantina.equals("1")){
                	ps.setString(10, "L");
                }
                else
                {
                	ps.setString(10, "P");
                }
                return ps;
            }
        });
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/student/radiusManager/rmUser", method = RequestMethod.POST)
    public ResponseEntity<String> radiusUser(@RequestBody CandidatePayload payload) {
    	Date dNow = new java.util.Date();
    	
    	Date date = new java.util.Date();

    	java.sql.Date sqlDate = new java.sql.Date( date.getTime() );
    	
    	java.sql.Date sqldNow = new java.sql.Date(dNow.getTime());
    	
        final String sql = "insert into rm_users("
        		+ "username,"	//1
        		+ "password,"  	//2
        		+ "groupid,"   	//3
        		+ "enableuser,"	//4
        		+ "uplimit,"	//5
        		+ "downlimit,"	//6
        		+ "comblimit,"	//7
        		+ "firstname,"	//8
        		+ "mobile,"		//9
        		+ "gpslat,"		//10
        		+ "gpslong,"	//11
        		+ "usemacauth,"	//12
        		+ "expiration,"	//13
        		+ "uptimelimit,"//14
        		+ "srvid,"		//15
        		+ "ipmodecm,"	//16
        		+ "ipmodecpe,"	//17
        		+ "poolidcm,"	//18
        		+ "poolidcpe,"	//19
        		+ "createdon,"	//20
        		+ "acctype,"	//21
        		+ "credits,"	//22
        		+ "cardfails,"	//23
        		+ "email,"		//24
        		+ "warningsent,"//25
        		+ "verified,"	//26
        		+ "selfreg,"	//27
        		+ "verifyfails,"//28
        		+ "verifysentnum,"	//29
        		+ "contractvalid,"	//30
        		+ "pswactsmsnum,"	//31
        		+ "alertemail,"		//32
        		+ "alertsms,"		//33
        		+ "courseduration,"	//34
        		+ "programcode,"	//35
        		+ "studymode,"		//36
        		+ "campus,"			//37
        		+ "ic,"				//38
        		+ "usertype"		//39
        		+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        mysqlJdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, payload.getMatricNo());
                ps.setString(2, payload.getUserPayload().getNric());
                ps.setInt(3, 0);
                ps.setInt(4, 1);
                ps.setInt(5, 0);
                ps.setInt(6, 0);
                ps.setInt(7, 100000000);
                ps.setString(8, payload.getName());
                ps.setString(9, payload.getPhone());
                ps.setInt(10, 0);
                ps.setInt(11, 0);
                ps.setInt(12, 0);
                ps.setDate(13, sqldNow);
                ps.setInt(14, 0);
                ps.setInt(15, 34);
                ps.setInt(16, 0);
                ps.setInt(17, 0);
                ps.setInt(18, 0);
                ps.setInt(19, 0);
                ps.setDate(20, sqlDate);
                ps.setInt(21, 0);
                ps.setInt(22, 0);
                ps.setInt(23, 0);
                ps.setString(24, payload.getEmail());
                ps.setInt(25, 0);
                ps.setInt(26, 0);
                ps.setInt(27, 0);
                ps.setInt(28, 0);
                ps.setInt(29, 0);
                ps.setInt(29, 0);
                ps.setDate(30, sqlDate);
                ps.setInt(31, 0);
                ps.setInt(32, 0);
                ps.setInt(33, 0);
                ps.setString(34, "4");
                ps.setString(35, payload.getProgramCode());
                ps.setString(36,payload.getStudyMode().getDescription());
                ps.setString(37, "todo:hanif");
                ps.setString(38, payload.getUserPayload().getNric());
                ps.setString(39, "student");
                return ps;
            }
        });
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/student/radiusManager/radCheck", method = RequestMethod.POST)
    public ResponseEntity<String> radCheckUser(@RequestBody CandidatePayload payload) {
    	
      String nric = payload.getUserPayload().getNric();
    	
    	MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			 md.update(nric.getBytes());
		        BigInteger hash = new BigInteger(1, md.digest());
		        nric = hash.toString(16);
		        while(nric.length() < 32) { //40 for SHA-1
		        	nric = "0" + nric;
		        }
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //or "SHA-1"
       final String md5Nric = nric;
       System.out.println("nric :"+nric);
       System.out.println("md5Nric :"+md5Nric);
        final String sql = "insert into radcheck(username,attribute,op,value) values(?,?,?,?)";

        mysqlJdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, payload.getMatricNo());
                ps.setString(2, md5Nric);
                ps.setString(3, ":=");
                ps.setString(4, payload.getUserPayload().getNric());
                return ps;
            }
        });
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
}

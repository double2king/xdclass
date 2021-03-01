import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class TokenTest {

    @Test
    public void getTokenInfo() {
        long expire = System.currentTimeMillis() + 1000 * 60;
        System.out.println(expire);
        String token = Jwts.builder().setSubject("xxx")
                .claim("id", "xxx")
                .setIssuedAt(new Date())
                .setExpiration(new Date(1614217944056L))
                .signWith(SignatureAlgorithm.HS256, "tencent_yun").compact();
        System.out.println(Jwts.parser().setSigningKey("tencent_yun").parse(token));
    }

}

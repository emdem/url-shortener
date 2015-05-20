package org.mvnsearch.shortener;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * url shortener application
 *
 * @author linux_china
 */
@SpringBootApplication
@Controller
public class UrlShortenerApplication {
    @Autowired
    private StringRedisTemplate redis;
    @Value("${info.app.domain}")
    private String host;

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void redirect(@PathVariable String id, HttpServletResponse response) throws Exception {
        final String url = redis.opsForValue().get(id);
        if (url != null) {
            redis.opsForValue().increment(id, 1L);
            response.sendRedirect(url);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> save(HttpServletRequest req) {
        final String queryParams = (req.getQueryString() != null) ? "?" + req.getQueryString() : "";
        final String url = (req.getRequestURI() + queryParams).substring(1);
        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(url)) {
            final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            redis.opsForValue().set(id, url);
            return new ResponseEntity<String>(host + id, HttpStatus.OK);
        } else
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
}

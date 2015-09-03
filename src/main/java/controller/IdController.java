package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by rene on 01.09.15.
 */
@RestController
@RequestMapping("/ids")
public class IdController {

  @Autowired
  private RedisConnectionFactory connectionFactory;

  @RequestMapping(value = "/**", method = RequestMethod.GET)
  public Long getNextId(HttpServletRequest request) {
    String url = (String) request.getAttribute(
        HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String key = url.substring(5).replace('/', ':');
    System.out.println(key);
    RedisAtomicLong atomicLong = new RedisAtomicLong(key,connectionFactory);
    return atomicLong.incrementAndGet();
  }

  @RequestMapping(value = "/**",method = RequestMethod.DELETE)
  public void deleteKey(HttpServletRequest request) {
    String url = (String) request.getAttribute(
        HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String key = url.substring(5).replace('/',':');
    String lua = "local arr = redis.call('keys', ARGV[1]); table.insert(arr,'"+key+"'); return redis.call('del',unpack(arr))";
    connectionFactory.getConnection().eval(lua.getBytes(), ReturnType.INTEGER,0,(key+":*").getBytes());
  }
}

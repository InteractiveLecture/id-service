package id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@Configuration
@ComponentScan("controller")
@EnableDiscoveryClient
public class IdServiceApplication {


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
    connectionFactory.setHostName("localhost");
    connectionFactory.setPort(6379);
    return connectionFactory;
  }

  @Bean
  public RedisOperations<String,Long> redisOperations() {
    RedisTemplate<String,Long> template = new RedisTemplate<String, Long>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;
  }

  public static void main(String[] args) {
        SpringApplication.run(IdServiceApplication.class, args);
    }
}

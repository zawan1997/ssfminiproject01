package vttp2022.ssfminiproject01.ssfproj.Repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class MainRepo {

    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();

    public void save(String location, String payload) {

        ValueOperations<String,String> valueOp = redisTemplate.opsForValue();
        valueOp.set(location.toLowerCase(), payload);
    }
     
    public Optional<String> get(String location) {
        ValueOperations<String,String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(location.toLowerCase());
        if(null == value) {
            return Optional.empty();
        }
        return Optional.of(value);
    }
}

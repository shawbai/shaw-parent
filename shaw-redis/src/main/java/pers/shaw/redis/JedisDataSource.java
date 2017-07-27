package pers.shaw.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository("jedisDataSource")
public class JedisDataSource{

	@Autowired
	private JedisPool jedisPool;

	public Jedis getRedis() {
		if (jedisPool != null) {
			return jedisPool.getResource();
		}
		return null;
	}

	public Jedis getRedis(int index) {
		if (jedisPool != null) {
			Jedis jedis = jedisPool.getResource();
			jedis.select(index);
			return jedis;
		}
		return null;
	}

	public void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
}
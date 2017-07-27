package pers.shaw.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Component
public class RedisTempalte {

	private static Logger logger = LoggerFactory.getLogger("RedisTempalte");

	@Autowired
	private JedisDataSource jedisDataSource;

	protected <T> T execute(RedisCallback<T> callback, Object... args) {
		Jedis jedis = null;
		try {
			Object index = ((Object[]) args)[0];
			if (null != index && Integer.parseInt(index.toString()) > 0
					&& Integer.parseInt(index.toString()) < 16) {
				jedis = jedisDataSource.getRedis(Integer.parseInt(index
						.toString()));
			} else {
				jedis = jedisDataSource.getRedis();
			}
			return callback.call(jedis, args);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedisDataSource.close(jedis);
			}
		}
		return null;
	}

	/**
	 * *************************************************************************
	 * *** Connection（连接）函数 Server（服务器）
	 */

	/**
	 * 选择DB实例
	 * 
	 * @param index
	 *            实例index
	 */
	public void select(int index) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				int index = Integer.parseInt(((Object[]) parms)[0].toString());
				return jedis.select(index);
			}
		}, index);
	}

	/**
	 * 是否连接
	 * 
	 * @param index
	 * @return
	 */
	public Boolean isConnected(int index) {
		return execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis, Object params) {
				return jedis.isConnected();
			}
		}, index);
	}

	/**
	 * 清空当前数据库中的所有 key。 此命令从不失败。 返回值： 总是返回 OK 。
	 * 
	 * @param index
	 * @return
	 */
	public String flushDB(int index) {
		return execute(new RedisCallback<String>() {
			@Override
			public String call(Jedis jedis, Object params) {
				return jedis.flushDB();
			}
		}, index);
	}

	/**
	 * *************************************************************************
	 * *** Key（键）
	 */

	/**
	 * 删除给定的一个 key 。 不存在的 key 会被忽略。
	 * 
	 * @param key
	 *            根据key删除
	 * @return 被删除 key 的数量。
	 */
	public Long del(int index, String key) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				return jedis.del(key);
			}
		}, index, key);
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public Boolean exists(int index, String key) {
		return execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				return jedis.exists(key);
			}
		}, index, key);
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key 。 KEYS * 匹配数据库中所有 key 。 KEYS h?llo 匹配 hello ，
	 * hallo 和 hxllo 等。 KEYS h*llo 匹配 hllo 和 heeeeello 等。 KEYS h[ae]llo 匹配 hello
	 * 和 hallo ，但不匹配 hillo 。
	 * 
	 * @param index
	 * @param pattern
	 * @return 符合给定模式的 key 列表。
	 */
	public Set<String> keys(int index, String pattern) {
		return execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis, Object params) {
				String pattern = ((Object[]) params)[1].toString();
				return jedis.keys(pattern);
			}
		}, index, pattern);
	}

	/**
	 * 删除所有符合给定模式 pattern 的 key
	 * 
	 * @param index
	 * @param pattern
	 * @return 被删除 key 的数量。
	 */
	public int batchDel(int index, String pattern) {
		Set<String> keys = keys(index, pattern);
		int deletedKeys = 0;
		for (String key : keys) {
			del(index, key);
			deletedKeys++;
		}
		return deletedKeys;
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。 在 Redis 中，带有生存时间的 key
	 * 被称为『易失的』(volatile)。 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET
	 * 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key
	 * 值来代替(replace)它的话，那么生存时间不会被改变。 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH
	 * 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。 另一方面，如果使用 RENAME 对一个 key
	 * 进行改名，那么改名后的 key 的生存时间和改名前一样。 RENAME 命令的另一种可能是，尝试将一个带生存时间的 key
	 * 改名成另一个带生存时间的 another_key ，这时旧的 another_key (以及它的生存时间)会被删除，然后旧的 key 会改名为
	 * another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。 使用 PERSIST 命令可以在不删除 key
	 * 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。 更新生存时间
	 * 可以对一个已经带有生存时间的 key 执行 EXPIRE 命令，新指定的生存时间会取代旧的生存时间。 过期时间的精确度
	 * 
	 * @param index
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(int index, String key, int seconds) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String seconds = ((Object[]) parms)[2].toString();
				return jedis.expire(key, Integer.parseInt(seconds));
			}
		}, index, key, seconds);
	}

	/**
	 * *************************************************************************
	 * *** String（字符串）
	 */

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。 EX second
	 * ：设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key second value
	 * 。 PX millisecond ：设置键的过期时间为 millisecond 毫秒。 SET key value PX millisecond
	 * 效果等同于 PSETEX key millisecond value 。 NX ：只在键不存在时，才对键进行设置操作。 SET key value
	 * NX 效果等同于 SETNX key value 。 XX ：只在键已经存在时，才对键进行设置操作。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public void set(int index, String key, String value) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String value = ((Object[]) parms)[2].toString();
				jedis.set(key, value);
				return null;
			}
		}, index, key, value);
	}

	public void set(int index, String key, byte[] value) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				byte[] value = (byte[]) ((Object[]) parms)[2];
				try {
					jedis.set(key.getBytes("UTF-8"), value);
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		}, index, key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @param seconds
	 *            seconds:过期时间（单位：秒）
	 * @author Wanglei Date: 2015-8-12 上午9:32:09
	 * @version [版本号1.0.0]
	 */
	public void set(int index, String key, String value, int seconds) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String value = ((Object[]) parms)[2].toString();
				String seconds = ((Object[]) parms)[3].toString();
				jedis.setex(key, Integer.parseInt(seconds), value);
				return null;
			}
		}, index, key, value, seconds);
	}

	public void set(int index, String key, byte[] value, int seconds) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				byte[] value = (byte[]) ((Object[]) parms)[2];
				String seconds = ((Object[]) parms)[3].toString();
				try {
					jedis.setex(key.getBytes("UTF-8"),
							Integer.parseInt(seconds), value);
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		}, index, key, value, seconds);
	}

	/**
	 * @param list
	 *            批量Set
	 */
	public void setPipeLine(int index, List<RedisKVPO> list) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				Pipeline p = jedis.pipelined();
				@SuppressWarnings("unchecked")
				List<RedisKVPO> values = (List<RedisKVPO>) ((Object[]) parms)[1];
				for (RedisKVPO po : values) {
					p.set(po.getK(), po.getV());
				}
				p.sync();
				return null;
			}
		}, index, list);
	}

	/**
	 * 返回 key 所关联的字符串值。 如果 key 不存在那么返回特殊值 nil 。 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET
	 * 只能用于处理字符串值。
	 * 
	 * @param key
	 * @return value
	 */
	public String get(int index, String key) {
		return execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				return jedis.get(key);
			}
		}, index, key);
	}

	/**
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public byte[] getByte(int index, String key) {
		return execute(new RedisCallback<byte[]>() {
			public byte[] call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				try {
					return jedis.get(key.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		}, index, key);
	}

	/**
	 * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 这是一个针对字符串的操作，因为 Redis 没有专用的整数类型，所以 key 内储存的字符串被解释为十进制 64 位有符号整数来执行 INCR
	 * 操作。
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public Long incr(int index, String key) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				return jedis.incr(key);
			}
		}, index, key);
	}

	/**
	 * *************************************************************************
	 * *** Hash（哈希表）
	 */

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。 在Redis2.4以下的版本里， HDEL
	 * 每次只能删除单个域，如果你需要在一个原子时间内删除多个域，请将命令包含在 MULTI / EXEC 块内。
	 * 
	 * @param index
	 * @param mapKey
	 * @param attributeKey
	 * @return
	 */
	public Long hdel(int index, String mapKey, String attributeKey) {
		return execute(new RedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				String field = ((Object[]) params)[2].toString();
				return jedis.hdel(key, field);
			}
		}, index, mapKey, attributeKey);
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 * 
	 * @param index
	 * @param mapKey
	 * @param attributeKey
	 * @return
	 */
	public Boolean hexists(int index, String mapKey, String attributeKey) {
		return execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				String field = ((Object[]) params)[2].toString();
				return jedis.hexists(key, field);
			}
		}, index, mapKey, attributeKey);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(int index, String key, String field) {
		return execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String field = ((Object[]) parms)[2].toString();
				return jedis.hget(key, field);
			}
		}, index, key, field);
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。 在返回值里，紧跟每个域名(field
	 * name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(int index, String key) {
		return execute(new RedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				return jedis.hgetAll(key);
			}
		}, index, key);
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。 如果域
	 * field 已经存在于哈希表中，旧值将被覆盖。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(int index, String key, String field, String value) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String field = ((Object[]) parms)[2].toString();
				String value = ((Object[]) parms)[3].toString();
				jedis.hset(key, field, value);
				return null;
			}
		}, index, key, field, value);
	}

	/**
	 * *************************************************************************
	 * *** List（列表）
	 */

	/**
	 * 返回列表 key 的长度。 如果 key 不存在，则 key 被解释为一个空列表，返回 0 . 如果 key 不是列表类型，返回一个错误。
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public String llen(int index, String key) {
		return execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				return jedis.llen(key) + "";
			}
		}, index, key);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头：
	 * 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH
	 * mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。 如果 key 不存在，一个空列表会被创建并执行
	 * LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 * 
	 * @param index
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lpush(int index, String key, String value) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String value = ((Object[]) parms)[2].toString();
				return jedis.lpush(key, value);
			}
		}, index, key, value);
	}

	public void lpushPipeLine(int index, String key, List<String> values) {
		execute(new RedisCallback<String>() {
			@SuppressWarnings("unchecked")
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				List<String> values = (List<String>) ((Object[]) parms)[2];
				Pipeline p = jedis.pipelined();
				for (String value : values) {
					p.lpush(key, value);
				}
				p.sync();
				return null;
			}
		}, index, key, values);
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。 下标(index)参数 start 和 stop 都以 0
	 * 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素，
	 * -2 表示列表的倒数第二个元素，以此类推。 注意LRANGE命令和编程语言区间函数的区别 假如你有一个包含一百个元素的列表，对该列表执行
	 * LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE
	 * 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的
	 * range() 函数。 超出范围的下标 超出范围的下标值不会引起错误。 如果 start 下标比列表的最大下标 end ( LLEN list
	 * 减去 1 )还要大，那么 LRANGE 返回一个空列表。 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end
	 * 。
	 * 
	 * @param index
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(int index, String key, long start, long end) {
		return execute(new RedisCallback<List<String>>() {
			public List<String> call(Jedis jedis, Object parms) {
				Object[] ps = ((Object[]) parms);
				String key = ps[1].toString();
				long start = Long.parseLong(ps[2].toString());
				long end = Long.parseLong(ps[3].toString());
				return jedis.lrange(key, start, end);
			}
		}, index, key, start, end);
	}

	public List<String> brpop(int index, String key) {
		return execute(new RedisCallback<List<String>>() {
			public List<String> call(Jedis jedis, Object parms) {
				Object[] ps = ((Object[]) parms);
				String key = ps[1].toString();
				return jedis.brpop(0, key);
			}
		}, index, key);
	}

	/**
	 * *************************************************************************
	 * *** Set（集合）
	 */

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 假如 key 不存在，则创建一个只包含
	 * member 元素作成员的集合。 当 key 不是集合类型时，返回一个错误。
	 * 
	 * @param index
	 * @param key
	 * @param value
	 * @return
	 */
	public Long sadd(int index, String key, String value) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String value = ((Object[]) parms)[2].toString();
				return jedis.sadd(key, value);
			}
		}, index, key, value);
	}

	/**
	 * SMEMBERS key 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。 时间复杂度: O(N)， N 为集合的基数。
	 * 返回值: 集合中的所有成员。
	 * 
	 * @param index
	 * @param key
	 * @return
	 */
	public Set<String> smembers(int index, String key) {
		return execute(new RedisCallback<Set<String>>() {
			public Set<String> call(Jedis jedis, Object parms) {
				Object[] ps = ((Object[]) parms);
				String key = ps[1].toString();
				return jedis.smembers(key);
			}
		}, index, key);
	}

	public Long scard(int index, String key) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				return jedis.scard(key);
			}
		}, index, key);
	}

	public Set<String> sinter(int index, String... key) {
		return execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis, Object parms) {
				return jedis.sinter((String[]) ((Object[]) parms)[1]);
			}
		}, index, key);
	}

	/**
	 * *************************************************************************
	 * *** SortedSet（有序集合）
	 */

	/**
	 * 将一个或多个成员元素及其分数值加入到有序集当中。
	 * 
	 * @param index
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Long zadd(int index, String key, double score, String member) {
		return execute(new RedisCallback<Long>() {
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				Double score = Double.parseDouble(((Object[]) parms)[2]
						.toString());
				String member = ((Object[]) parms)[3].toString();
				return jedis.zadd(key, score.doubleValue(), member);
			}
		}, index, key, score, member);
	}

	// zremrangebyscore myZset 1 2 ---删除分数>=1 and <=2的数据
	public Long zremrangebyscore(int index, String key, double start, double end) {
		return execute(new RedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				Double start = Double.parseDouble(((Object[]) parms)[2]
						.toString());
				Double end = Double.parseDouble(((Object[]) parms)[3]
						.toString());
				return jedis.zremrangeByScore(key, start, end);
			}
		}, index, key, start, end);
	}

	public Long zadd(int index, String key, Map<String, Double> scoreMembers) {
		return execute(new RedisCallback<Long>() {
			@SuppressWarnings("unchecked")
			public Long call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				Map<String, Double> scoreMembers = (Map<String, Double>) ((Object[]) parms)[2];

				return jedis.zadd(key, scoreMembers);
			}
		}, index, key, scoreMembers);
	}

	public Set<String> zrangeByScore(int index, String key, String min,
			String max, int offset, int count) {
		return execute(new RedisCallback<Set<String>>() {
			public Set<String> call(Jedis jedis, Object parms) {
				Object[] ps = ((Object[]) parms);
				String key = ps[1].toString();
				String min = ps[2].toString();
				String max = ps[3].toString();
				int offset = Integer.valueOf(ps[4].toString()).intValue();
				int count = Integer.valueOf(ps[5].toString()).intValue();
				return jedis.zrangeByScore(key, min, max, offset, count);
			}
		}, index, key, min, max, offset, count);
	}

	public Set<String> zrange(int index, String key, long start, long end) {
		return execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				long start = Long.valueOf(((Object[]) parms)[2].toString());
				long end = Long.valueOf(((Object[]) parms)[3].toString());
				return jedis.zrange(key, start, end);
			}
		}, index, key, start, end);
	}

}

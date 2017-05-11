package com.tqmall.data.monk.common.redis;

import com.tqmall.data.monk.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;

import java.util.*;

/**
 * redis封装后的类
 * Created by zxg on 15/7/20.
 */
@Component
@Slf4j
public class RedisClientTemplateImpl implements RedisClientTemplate {

    private static final Integer EXPIRE_TIME = 60 * 60 * 24 * 15; //15天有效期

    @Autowired
    private RedisDataSource redisDataSource;

    public void disconnect() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();
    }


    @Override
    public <T> T lazyGet(String key, Class<?> cls) {
        try {

            String jsonString = this.get(key);
            if (null == jsonString) {
                return null;
            }
            return (T) JsonUtil.jsonToObject(jsonString, cls);

        } catch (Exception e) {
            log.error("lazyGet error:", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> lazyGetList(String key, Class<?> cls) {

        try {

            String jsonString = this.get(key);
            if (null == jsonString) {
                return null;
            }
            return JsonUtil.jsonToList(jsonString, cls);

        } catch (Exception e) {
            log.error("lazyGetList error:", e);

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String lazySet(String key, Object value, Integer expire) {

        try {

            if (null == value) {
                return null;
            }
            String json = JsonUtil.objectToJson(value);

            if (expire == null) {
                return this.set(key, json);
            }
            return this.setStringWithTime(key, json, expire);
        } catch (Exception e) {
            log.error("lazySet error:", e);

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setNone(String key) {
        setStringWithTime(key, RedisKeyBean.NULL_OBJECT, RedisKeyBean.RREDIS_EXP_HOURS);
    }

    @Override
    public boolean isNone(String redisString) {
        return RedisKeyBean.NULL_OBJECT.equals(redisString);
    }

    /*=============普通String的操作============================*/

    /**
     * 设置单个值
     * <p/>
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     *
     * @param key   key
     * @param value string value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    @Override
    public String set(final String key, final String value) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。
     * 如果 key 已经存在， 将覆写旧值。
     * 类似于以下两个命令:
     * SET key value
     * EXPIRE key expire # 设置生存时间
     * 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。
     * 时间复杂度：O(1)
     *
     * @param key    key
     * @param value  string value
     * @param expire 生命周期,单位（秒）
     * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。
     */
    @Override
    public String setStringWithTime(final String key, final String value, Integer expire) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        if (null == expire) {
            expire = EXPIRE_TIME;
        }

        boolean broken = false;
        try {
            result = shardedJedis.setex(key, expire, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;

    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。
     * 时间复杂度：O(1)
     *
     * @param key   key
     * @param value string value
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    @Override
    public Long setStringIfNotExists(final String key, final String value) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * 当 key 存在但不是字符串类型时，返回一个错误。
     */
    @Override
    public String setReturnOld(final String key, final String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key   key
     * @param value
     * @return
     */
    @Override
    public Long setAppend(final String key, final String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    @Override
    public String get(final String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }


    //判断key值存在不存在
    @Override
    public Boolean exists(final String key) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    //判断key值的类型
    @Override
    public String getType(final String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.type(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     *
     * @param keys 匹配的key的集合
     * @return 删除成功的条数
     */
    @Override
    public Long delKeys(final String[] keys) {
        long result = 0;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            Collection<Jedis> jedisC = shardedJedis.getAllShards();
            Iterator<Jedis> iter = jedisC.iterator();
            long count = 0;
            while (iter.hasNext()) {
                Jedis _jedis = iter.next();
                count += _jedis.del(keys);
            }
            return count;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Long delKey(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * key在某个时间点失效的
     * <p/>
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
     *
     * @param key      key
     * @param unixTime 生命周期，单位为秒
     * @return 1: 设置成功 0: 已经超时或key不存在
     */
    @Override
    public Long expireAt(final String key, final long unixTime) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expireAt(key, unixTime);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }


    //获得该Key的生存时间
    @Override
    public Long getTtl(final String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /*============hashMap========================*/

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 时间复杂度: O(1)
     *
     * @param key   key 存在缓存中的key
     * @param field 域 －－－即hashmap的key 值
     * @param value string value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    @Override
    public Long setMapKey(final String key, final String field, final String value) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key    key
     * @param field  域
     * @param value  string value
     * @param expire 生命周期，单位为秒
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    @Override
    public Long setMapKeyWithTime(final String key, final String field, final String value, Integer expire) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        if (null == expire) {
            expire = EXPIRE_TIME;
        }
        boolean broken = false;
        try {
            Pipeline pipeline = shardedJedis.getShard(key).pipelined();
            Response<Long> response = pipeline.hset(key, field, value);
            pipeline.expire(key, expire);
            pipeline.sync();

            result = response.get();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;

    }


    /**
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * 若域 field 已经存在，该操作无效。
     *
     * @param key   key 存在缓存中的key
     * @param field 域 －－－即hashmap的key 值
     * @param value string value
     * @return 设置成功，返回 1 。
     * 如果给定域已经存在且没有操作被执行，返回 0 。
     */
    @Override
    public Long setMapKeyWithOutExit(final String key, final String field, final String value) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hsetnx(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key  key
     * @param hash field-value的map
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    @Override
    public String setMap(final String key, final Map<String, String> hash) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmset(key, hash);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 分布式管道异步存储
     * 批量的{link hashMultipleSet(String, Map)}，在管道中执行
     *
     * @param data Map<String, Map<String, String>>格式的数据
     * @return 操作状态的集合
     */
    @Override
    public List<Object> setBatchMap(final Map<String, Map<String, String>> data) {
        List<Object> resultList = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return resultList;
        }
        boolean broken = false;
        try {
            ShardedJedisPipeline pipeline = shardedJedis.pipelined();
            for (Map.Entry<String, Map<String, String>> iter : data.entrySet()) {
                pipeline.hmset(iter.getKey(), iter.getValue());
            }
            resultList = pipeline.syncAndReturnAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return resultList;

    }

    @Override
    public String getMapKey(final String key, final String field) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hget(key, field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。
     * 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key    key
     * @param fields field的数组
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    @Override
    public List<String> getHashMultiple(final String key, final String... fields) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmget(key, fields);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 时间复杂度: O(N)
     *
     * @param key key
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    @Override
    public Map<String, String> getAllHash(final String key) {
        Map<String, String> result = new HashMap<>();
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 异步获得 key数组对应的一个list的Map对象
     * 批量的{@link #getAllHash(String)}
     *
     * @param keys key的数组
     * @return 执行结果的集合
     */
    @Override
    public List<Map<String, String>> getAllBatchHashForList(final String... keys) {
        List<Map<String, String>> result = new ArrayList<>();
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            ShardedJedisPipeline pipeline = shardedJedis.pipelined();
            result = new ArrayList<Map<String, String>>(keys.length);
            List<Response<Map<String, String>>> responses = new ArrayList<Response<Map<String, String>>>(keys.length);
            for (String key : keys) {
                responses.add(pipeline.hgetAll(key));
            }
            pipeline.sync();
            for (Response<Map<String, String>> resp : responses) {
                result.add(resp.get());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }


    /**
     * 异步获得 key数组对应的一组Map中有Map对象
     * 批量的{@link #getHashMultiple(String, String...)}，与{@link #getAllBatchHashForList(String...)}不同的是，返回值为Map类型
     *
     * @param keys key的数组
     * @return 多个hash的所有filed和value
     */
    @Override
    public Map<String, Map<String, String>> getAllBatchHashForMap(final String... keys) {
        Map<String, Map<String, String>> result = new HashMap();
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            ShardedJedisPipeline pipeline = shardedJedis.pipelined();
            // 设置map容量防止rehash
            int capacity = 1;
            while ((int) (capacity * 0.75) <= keys.length) {
                capacity <<= 1;
            }
            result = new HashMap<String, Map<String, String>>(capacity);
            List<Response<Map<String, String>>> responses = new ArrayList<Response<Map<String, String>>>(keys.length);
            for (String key : keys) {
                responses.add(pipeline.hgetAll(key));
            }
            pipeline.sync();
            for (int i = 0; i < keys.length; ++i) {
                result.put(keys[i], responses.get(i).get());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Boolean existsHashMapKey(String key, String field) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Long setList(String key, List<String> list) {
        Long result = 0L;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
//            String str= StringUtils.join(list.toArray(),",");
            shardedJedis.del(key);
            result = shardedJedis.rpush(key, (String[]) list.toArray(new String[list.size()]));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public List<String> getList(String key) {

        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, 0, -1);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Set<String> result = new HashSet<>();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            Collection<Jedis> coll = shardedJedis.getAllShards();
            if(CollectionUtils.isEmpty(coll)){
                return result;
            }
            for(Jedis jedis : coll){
                result.addAll(jedis.keys(pattern));
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

}

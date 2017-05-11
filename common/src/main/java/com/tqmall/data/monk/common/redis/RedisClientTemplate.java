package com.tqmall.data.monk.common.redis;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zxg on 15/7/20.
 */
public interface RedisClientTemplate {
    /*=================普通String懒操作==============*/
    <T> T lazyGet(String key, Class<?> cls);

    <T> List<T> lazyGetList(String key, Class<?> cls);

    String lazySet(String key, Object value, Integer expire);

    /*
    *   缓存数据库不存在的数据，避免短时间重复查询数据库
    * */
    void setNone(String key);

    boolean isNone(String redisString);

    /*=============普通String的操作===========================*/

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
    public String set(String key, String value);

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
    public String setStringWithTime(final String key, final String value, Integer expire);

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。
     * 时间复杂度：O(1)
     *
     * @param key   key
     * @param value string value
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public Long setStringIfNotExists(final String key, final String value);

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * 当 key 存在但不是字符串类型时，返回一个错误。
     */
    public String setReturnOld(String key, String value);

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key   key
     * @param value
     * @return
     */

    public Long setAppend(String key, String value);

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public String get(String key);


    //判断key值存在不存在
    public Boolean exists(String key);

    //判断key值的类型
    public String getType(String key);

    /**
     *
     * @param keys 匹配的key的集合
     * @return 删除成功的条数
     */
    public Long delKeys(final String[] keys);

    public Long delKey(final String key);

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
    public Long expireAt(String key, long unixTime);


    //获得该Key的生存时间
    public Long getTtl(String key);

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
    public Long setMapKey(String key, String field, String value);

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
    public Long setMapKeyWithTime(final String key, final String field, final String value, Integer expire);


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
    public Long setMapKeyWithOutExit(String key, String field, String value);

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key  key
     * @param hash field-value的map
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    public String setMap(String key, Map<String, String> hash);

    /**
     * 分布式管道异步存储
     * 批量的{link hashMultipleSet(String, Map)}，在管道中执行
     *
     * @param data Map<String, Map<String, String>>格式的数据
     * @return 操作状态的集合
     */
    public List<Object> setBatchMap(final Map<String, Map<String, String>> data);

    public String getMapKey(String key, String field);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。
     * 时间复杂度: O(N) (N为fields的数量)
     *
     * @param key    key
     * @param fields field的数组
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public List<String> getHashMultiple(final String key, final String... fields);

    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 时间复杂度: O(N)
     *
     * @param key key
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public Map<String, String> getAllHash(final String key);

    /**
     * 异步获得 key数组对应的一个list的Map对象
     * 批量的{@link #getAllHash(String)}
     *
     * @param keys key的数组
     * @return 执行结果的集合
     */
    public List<Map<String, String>> getAllBatchHashForList(final String... keys);


    /**
     * 异步获得 key数组对应的一组Map中有Map对象
     * 批量的{@link #getHashMultiple(String, String...)}，与{@link #getAllBatchHashForList(String...)}不同的是，返回值为Map类型
     *
     * @param keys key的数组
     * @return 多个hash的所有filed和value
     */
    Map<String, Map<String, String>> getAllBatchHashForMap(final String... keys);

    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     * 如果哈希表含有给定域，返回true 。
     * 如果哈希表不含有给定域，或 key 不存在，返回 false。
     */
    Boolean existsHashMapKey(String key, String field);

 /*=================================List=============================*/

    Long setList(String key, List<String> list);

    List<String> getList(String key);

    /**
     * 根据表达式获得key集合
     *
     * @param pattern 例如：Athena_maintain_*
     * @return
     */
    Set<String> getKeys(final String pattern);
}

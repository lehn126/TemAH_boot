package com.temah.common.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取key
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 保存key
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 保存key(带超时时间)
     */
    public void set(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 保存key(带超时时间), duration不能为null
     */
    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 仅当key不存在时保存key(当key已经存在则返回false)
     */
    public Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 仅当key不存在时保存key(当key已经存在则返回false, 带超时时间), duration不能为null
     */
    public Boolean setIfAbsent(String key, Object value, Duration duration) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, duration);
    }

    /**
     * 仅当key已经存在时保存key(如果key不存在则返回false)
     */
    public Boolean setIfPresent(String key, Object value) {
        return redisTemplate.opsForValue().setIfPresent(key, value);
    }

    /**
     * 仅当key已经存在时保存key(如果key不存在则返回false, 带超时时间), duration不能为null
     */
    public Boolean setIfPresent(String key, Object value, Duration duration) {
        return redisTemplate.opsForValue().setIfPresent(key, value, duration);
    }

    /**
     * 删除key
     */
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     */
    public Long del(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 使用pattern进行匹配后批量删除最大数量为count的指定类型匹配key
     */
    public Long del(String pattern, Long count, DataType dataType) {
        List<String> keys = scan(pattern, count, dataType);
        if (!keys.isEmpty()) {
            return del(keys);
        }
        return 0L;
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeoutSeconds) {
        return redisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间, duration不能为null
     */
    public Boolean expire(String key, Duration duration) {
        return redisTemplate.expire(key, duration);
    }

    /**
     * 设置过期时间点, expireAt不能为null
     */
    public Boolean expireAt(String key, Instant expireAt) {
        return redisTemplate.expireAt(key, expireAt);
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断是否有该key
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 使用pattern进行匹配查询并返回最大数量为count的指定类型key
     */
    public List<String> scan(String pattern, Long count, DataType dataType) {
        ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions().match(pattern);
        if (count != null) {
            builder.count(count);
        }
        if (dataType != null) {
            builder.type(dataType);
        }
        List<String> keys = new ArrayList<>();
        try (Cursor<String> cursor = redisTemplate.scan(builder.build())) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                keys.add(key);
            }
        }
        return keys;
    }

    /**
     * 将key中储存的数字值增加delta并返回增加后的值（只能用在整型）
     */
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将key中储存的数字值减少delta并返回减少后的值（只能用在整型）
     */
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 获取指定key对应的Hash结构中hashKey对应的value
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 向指定key对应的Hash结构中放入一个hashKey及对应的value, 并为key设置过期时间
     */
    public Boolean hSet(String key, String hashKey, Object value, long timeoutSeconds) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, timeoutSeconds);
    }

    /**
     * 向指定key对应的Hash结构中放入一个hashKey及对应的value
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 直接获取整个指定key对应的Hash结构
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 直接设置整个Hash结构，并为key设置过期时间
     */
    public Boolean hSetAll(String key, Map<String, ?> map, long timeoutSeconds) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, timeoutSeconds);
    }

    /**
     * 直接设置整个Hash结构
     */
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 删除指定key对应的Hash结构中的hashKey
     */
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 判断指定key对应的Hash结构中是否有该hashKey
     */
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 将hashKey对应的数字值增加delta并返回增加后的值（只能用在整型）
     */
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 将hashKey对应的数字值减少delta并返回减少后的值（只能用在整型）
     */
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * 获取Set结构
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 向Set结构中添加元素
     */
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 向Set结构中添加元素，并为key设置过期时间
     */
    public Long sAdd(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    /**
     * 判断元素是否在Set中存在
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取Set结构的长度
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 删除Set结构中的元素
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取List结构中的下标从start开始到end为止的元素.
     * 0表示第一个元素，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取List结构的长度
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 根据索引获取List中的元素
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 向List结构中添加元素(right push)
     */
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向List结构中添加元素(right push)，并为key设置过期时间
     */
    public Long lPush(String key, Object value, long timeoutSeconds) {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, timeoutSeconds);
        return index;
    }

    /**
     * 向List结构中批量添加元素(right push)
     */
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向List结构中批量添加元素(right push)，并为key设置过期时间
     */
    public Long lPushAll(String key, Long timeoutSeconds, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, timeoutSeconds);
        return count;
    }

    /**
     * 从List结构中移除并返回count个元素(right pop), 如果List为空返回null
     */
    public List<Object> leftPop(String key, long count) {
        return redisTemplate.opsForList().rightPop(key, count);
    }

    /**
     * 从List结构中移除并返回最后一个元素(right pop), 如果List为空则阻塞直到List不为空或在指定等待时间duration后返回null
     */
    public Object leftPop(String key, Duration duration) {
        return redisTemplate.opsForList().rightPop(key, duration);
    }

    /**
     * 从List结构中移除并返回最后一个元素(right pop), 如果List为空则阻塞直到List不为空或在3秒后返回null
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().rightPop(key, 3, TimeUnit.SECONDS);
    }

    /**
     * 从List结构中移除数量为count并且值为value的元素
     */
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 向SortedSet中添加元素并设置score
     */
    public Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 从SortedSet中移除元素
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取SortedSet中元素的score
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 返回SortedSet中下标从start开始到end为止的元素.
     * 0表示第一个元素，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 返回SortedSet中下标从start开始到end为止的元素.
     * 0表示第一个元素，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 倒序返回SortedSet中下标从start开始到end为止的元素.
     * 0表示第一个元素，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 倒序返回SortedSet中下标从start开始到end为止的元素.
     * 0表示第一个元素，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, end);
    }

    /**
     * 返回SortedSet中score范围从min到max之间的元素(包含min和max).
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 返回SortedSet中score范围从min到max之间的元素(包含min和max).
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 倒序返回SortedSet中score范围从min到max之间的元素(包含min和max).
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 倒序返回SortedSet中score范围从min到max之间的元素(包含min和max).
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回SortedSet中按照元素属性ASCLL来排序后指定范围之间的元素.
     */
    public Set<Object> zRangeByScore(String key, RedisZSetCommands.Range range) {
        return redisTemplate.opsForZSet().rangeByLex(key, range);
    }

    /**
     * 倒序返回SortedSet中按照元素属性ASCLL来排序后指定范围之间的元素.
     */
    public Set<Object> zReverseRangeByScore(String key, RedisZSetCommands.Range range) {
        return redisTemplate.opsForZSet().reverseRangeByLex(key, range);
    }

    //--------------------------------------Redis锁--------------------------------------
    //获取线程前缀，同时也是线程表示。通过UUID唯一性
    private static final String ID_PREFIX = UUID.randomUUID().toString() + "-";
    //与线程id组合
    private String getThreadId() {
        return ID_PREFIX + Thread.currentThread().getId();
    }
    /**
     * 尝试获取Redis锁并设置过期时间，成功则返回true, duration不能为null
     */
    public boolean tryLock(String key, Duration duration) {
        //获取线程id
        String id = getThreadId();
        //获取锁
        return Boolean.TRUE.equals(setIfAbsent(key, id, duration));
    }
    /**
     * 移除Redis锁
     */
    public boolean unLock(String key) {
        //获取存储的线程标识
        String value = (String) get(key);
        //当前线程的线程标识
        String id = getThreadId();
        boolean ret = false;
        //线程标识相同则删除否,则不删除
        if (id.equals(value)){
            ret = Boolean.TRUE.equals(del(key));
        }
        return ret;
    }
}

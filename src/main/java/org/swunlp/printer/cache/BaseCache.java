package org.swunlp.printer.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.swunlp.printer.util.RedisUtil;

import java.util.List;

/**
 * 所有缓存类的父类，封装一些基本的增删查改
 * @author TangXi
 * @param <T>
 */
public abstract class BaseCache<T> {


    @Autowired
    protected RedisUtil<T> redisUtil;

    protected static String ROOT_KEY = "sharePrinter:";

    /**
     * 子类需要填写的该领域的key
     * 如填ShareFile
     * 最后效果为：sharePrinter:ShareFile:xxx
     * @return 该领域的key
     */
    protected abstract String domain();

    protected String getKey(){
        return ROOT_KEY + domain();
    }


    /**
     * 当传入的为null时代表就是领域本身，不涉及到下一级
     * @param key 下一级的key
     * @return 返回是否存在该key
     */
    public boolean exist(String key){
        String newKey = getKey() + (key == null ? "" : ":"+key);
        return redisUtil.hasKey(newKey);
    }
    /**
     * 当传入的为null时代表就是领域本身，不涉及到下一级
     * @param key 下一级的key
     * @return 缓存中对应Key的数据
     */
    public T get(String key){
        String newKey = getKey() + (key == null ? "" : ":"+key);
        return redisUtil.get(newKey);
    }

    /**
     * 当传入的为null时代表就是领域本身，不涉及到下一级
     * @param key 下一级的key
     * @param value 需要存的值
     * @return 是否存储成功
     */
    public boolean set(String key,T value){
        return set(key,value,-1);
    }

    /**
     * 当传入的为null时代表就是领域本身，不涉及到下一级
     * @param key 下一级的key
     * @param value 需要存的值
     * @param time – 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return 是否存储成功
     */
    public boolean set(String key,T value,long time){
        String newKey = getKey() + (key == null ? "" : ":"+key);
        return redisUtil.set(newKey,value,time);
    }

    /**
     * 当传入的为null时代表就是领域本身，不涉及到下一级
     *
     * @param key    下一级的key
     * @return 返回删除的条目数量
     */
    public boolean remove(String key){
        String newKey = getKey() + (key == null ? "" : ":"+key);
        return redisUtil.expire(newKey,0);
    }

    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 增加后的值
     */
    public long increase(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.incr(newKey, delta);
    }

    /**
     * 递减
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 减少后的值
     */
    public long decrease(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.decr(newKey, delta);
    }

    /**
     * HashSet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashSet(String key, String item,Object value) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.hset(newKey,item,value);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashGet(String key, String item) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.hget(newKey, item);
    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hashDel(String key, Object... item) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        redisUtil.hdel(newKey, item);
    }

    /**
     * 获取list缓存的全部内容
     *
     * @param key   键
     * @return List<T> 对应范围内的值
     */
    public List<T> lGetAll(String key) {
        return lGet(key,0,-1);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return List<T> 对应范围内的值
     */
    public List<T> lGet(String key, long start, long end) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lGet(newKey, start, end);
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lGetListSize(newKey);
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key   键
     * @param index 索引
     * @return 对应索引的值
     */
    public Object lGetIndex(String key, long index) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lGetIndex(newKey, index);
    }

    /**
     * 将value放入list缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, T value) {
        return lSet(key, value, -1);
    }

    /**
     * 将value放入list缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, T value, long time) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lSet(newKey, value, time);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, List<T> value) {
        return lSet(key, value, -1);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, List<T> value, long time) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lSet(newKey, value, time);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 是否成功
     */
    public boolean lUpdateIndex(String key, long index, T value) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lUpdateIndex(newKey, index, value);
    }

    /**
     * 移除N个值为value的元素
     *
     * @param key    键
     * @param count  移除个数
     * @param values 值
     * @return 移除的个数
     */
    @SafeVarargs
    public final long lRemove(String key, long count, T... values) {
        String newKey = getKey() + (key == null ? "" : ":" + key);
        return redisUtil.lRemove(newKey, count, values);
    }
}

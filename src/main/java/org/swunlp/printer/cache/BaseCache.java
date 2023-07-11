package org.swunlp.printer.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.swunlp.printer.util.RedisUtil;

/**
 * 所有缓存类的父类，封装一些基本的增删查改
 * @author TangXi
 * @param <T>
 */
public abstract class BaseCache<T> {


    @Autowired
    protected RedisUtil<T> redisUtil;

    protected static String ROOT_KEY = new String("sharePrinter:");

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
     * @param key 下一级的key
     * @param values 需要删除的值
     * @return 返回删除的条目数量
     */
    public long remove(String key,T ...values){
        String newKey = getKey() + (key == null ? "" : ":"+key);
        return redisUtil.setRemove(newKey,values);
    }
}

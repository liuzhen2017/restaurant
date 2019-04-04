package com.menu.manger.util;

/**
 * 緩存類
 * @author Administrator
 *
 * @param <T>
 * @param <b>
 */
public interface CacheBase<T,b> {
	/**
	 * 從緩存取數據
	 * @param id
	 * @return
	 */
    public T get(b id);
     
    /**
     * 從緩存刪除數據
     * @param id
     * @return
     */
    public T delete(b id);
    /**
     * 保存更新數據
     * @param id
     * @param value
     * @return
     */
    public T save(b id, T value);
    /**
     * 保持修改數據
     * 
     * 
     * @param id
     * @param value
     * @return
     */
    public T update(b id, T value);
}

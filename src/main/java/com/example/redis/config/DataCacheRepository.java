package com.example.redis.config;

import org.springframework.data.redis.core.SessionCallback;

import java.util.concurrent.TimeUnit;

public interface DataCacheRepository<T> {

  boolean addInSet(String key, Object... object);

  boolean deleteInSet(String key, Object... values);

  boolean containsInSet(String key, Object data);

  boolean addInStringWithExpiry(String key, Object object, Long time, TimeUnit timeUnit);

  boolean containsInString(String key);

 Object get(String key);

  <T> T executeMultiLineTransactions(SessionCallback<T> sessionCallBack);

  boolean delete(String key);
}

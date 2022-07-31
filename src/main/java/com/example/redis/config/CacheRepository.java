package com.example.redis.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class CacheRepository<T> implements DataCacheRepository<T> {

  @Autowired
  RedisTemplate<Object, Object> template;
  private static final Logger LOGGER = LogManager.getLogger(CacheRepository.class);

  @Override
  public boolean addInSet(String key, Object... object) {
    try {
      template.opsForValue().set(key, object);
      return true;
    } catch (Exception e) {
      LOGGER.error("Unable to add object of key {} to cache collection '{}'", key, e.getMessage());
      return false;
    }
  }

  @Override
  public boolean deleteInSet(String key, Object... values) {
    try {
      template.opsForSet().remove(key, values);
      return true;
    } catch (Exception e) {
      LOGGER.error("Unable to delete entry {} from cache collection '{}': {}", values, key,
          e.getMessage());
      return false;
    }
  }

  @Override
  public boolean containsInSet(String key, Object data) {
    try {
      return template.opsForSet().isMember(key, data);
    } catch (Exception e) {
      if (e.getMessage() == null) {
        LOGGER.error("Entry '{}' does not exist in cache", key);
      } else {
        LOGGER.error("Unable to find entry '{}' in cache collection '{}': {}", key, e.getMessage());
      }
      return false;
    }
  }

  @Override
  public boolean addInStringWithExpiry(String key, Object object, Long time, TimeUnit timeUnit) {
    try {
      template.opsForValue().set(key, object, time, timeUnit);
      return true;
    } catch (Exception e) {
      LOGGER.error("Unable to add object of key {} to cache collection '{}'", key, e.getMessage());
      return false;
    }
  }

  @Override
  public boolean delete(String key) {
    try {
      return template.delete(key);
    } catch (Exception e) {
      LOGGER.error("Unable to delete object of key {}", key, e.getMessage());
    }
    return false;
  }

  @Override
  public Object get(String key) {
    try {
      return template.opsForValue().get(key);
    } catch (Exception e) {
      LOGGER.error("Unable to get object of key {}", key, e.getMessage());
    }
    return null;
  }

  @Override
  public <T> T executeMultiLineTransactions(SessionCallback<T> sessionCallBack) {
    try {
      return template.execute(sessionCallBack);
    } catch (Exception e) {
      LOGGER.error("Unable to complete a multi line transaction", e, e.getMessage());
    }
    return null;
  }

  @Override
  public boolean containsInString(String key) {
    try {
      return Objects.nonNull(template.opsForValue().get(key));
    } catch (Exception e) {
      if (e.getMessage() == null) {
        LOGGER.error("Entry '{}' does not exist in cache", key);
      } else {
        LOGGER.error("Unable to find entry '{}' in cache collection '{}': {}", key, e.getMessage());
      }
      return false;
    }
  }

  public Boolean isAvailable() {
    try {
      return template.getConnectionFactory().getConnection().ping() != null;
    } catch (Exception e) {
      LOGGER.warn("Redis server is not available at the moment.");
    }
    return false;
  }
}

package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.LoggingEventType;
import org.springframework.data.repository.CrudRepository;

public interface LoggingEventTypeRepository extends CrudRepository<LoggingEventType, Long> {
    LoggingEventType findByEventType(String eventType);
}

package com.sharproute.repository;

import org.springframework.data.repository.CrudRepository;

import com.sharproute.common.object.FixServer;
import com.sharproute.common.object.FixSession;

public interface FixServerRepository extends CrudRepository<FixServer, Integer> {

}

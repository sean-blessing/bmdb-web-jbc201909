package com.bmdb.db;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.Actor;

public interface ActorRepository extends CrudRepository<Actor, Integer> {

}

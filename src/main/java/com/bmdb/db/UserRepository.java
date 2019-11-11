package com.bmdb.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.Movie;
import com.bmdb.business.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}

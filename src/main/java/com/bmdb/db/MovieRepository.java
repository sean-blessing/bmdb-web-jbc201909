package com.bmdb.db;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.Movie;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

}

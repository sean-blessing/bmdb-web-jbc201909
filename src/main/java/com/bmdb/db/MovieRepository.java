package com.bmdb.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.Movie;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
	List<Movie> findByRating(String rating);

}

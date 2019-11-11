package com.bmdb.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.MovieCollection;

public interface MovieCollectionRepository extends CrudRepository<MovieCollection, Integer> {
	List<MovieCollection> findAllByUserId(int id);
}

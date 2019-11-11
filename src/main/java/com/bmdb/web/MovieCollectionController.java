package com.bmdb.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.bmdb.business.MovieCollection;
import com.bmdb.business.User;
import com.bmdb.db.MovieCollectionRepository;
import com.bmdb.db.MovieRepository;
import com.bmdb.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/movie-collections")
public class MovieCollectionController {
	@Autowired
	private MovieCollectionRepository movieCollectionRepo;
	@Autowired
	private UserRepository userRepo;
	
	// list - return all movieCollections
	@GetMapping("/")
	public JsonResponse listMovieCollections() {
		JsonResponse jr = null;
		try  {
			jr = JsonResponse.getInstance(movieCollectionRepo.findAll());			
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// get - return 1 movieCollection for the given id
	@GetMapping("/{id}")
	public JsonResponse getMovie(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieCollectionRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// add - adds a new MovieCollection
	@PostMapping("/")
	public JsonResponse addMovie(@RequestBody MovieCollection mc) {
		// add a new movie
		JsonResponse jr = null;
		// for all maintenance methods recalculate the collection value
		try {
			// 1 do maintenance
			jr = JsonResponse.getInstance(movieCollectionRepo.save(mc));
			// 2 recalcCollectionValue
			recalculateCollectionTotal(mc.getUser());
		}
		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// update - update a MovieCollection
	@PutMapping("/")
	public JsonResponse updateMovieColletion(@RequestBody MovieCollection mc) {
		// update a movie
		JsonResponse jr = null;
		try {
			if (movieCollectionRepo.existsById(mc.getId())) {
				jr = JsonResponse.getInstance(movieCollectionRepo.save(mc));
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating MovieCollection.  id: "+
											mc.getId() + " doesn't exist!");
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// delete - delete a MovieCollection
	@DeleteMapping("/{id}")
	public JsonResponse deleteMovie(@PathVariable int id) {
		// delete a movie
		JsonResponse jr = null;
		
		try {
			if (movieCollectionRepo.existsById(id)) {
				movieCollectionRepo.deleteById(id);
				jr = JsonResponse.getInstance("Delete successful!");
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Movie.  id: "+
											id + " doesn't exist!");
			}
		}
		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// method will recalculate the collectionValue and save it in the User instance 
	private void recalculateCollectionTotal(User u) {
		// get a list of movieCollection
		// all movies for user
		List<MovieCollection> mcList = movieCollectionRepo.findAllByUserId(u.getId());
		// loop thru list to sum a total
		double total = 0.0;
		for (MovieCollection mc: mcList) {
			total += mc.getPurchasePrice();
		}
		// save that total in the User instance
		u.setCollectionValue(total);
		try {
			userRepo.save(u);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
}

package com.bmdb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.bmdb.business.User;
import com.bmdb.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepo;
	
	// list - return all users
	@GetMapping("/")
	public JsonResponse listUsers() {
		JsonResponse jr = null;
		try  {
			jr = JsonResponse.getInstance(userRepo.findAll());			
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// get - return 1 user for the given id
	@GetMapping("/{id}")
	public JsonResponse getUser(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// add - adds a new User
	@PostMapping("/")
	public JsonResponse addUser(@RequestBody User a) {
		// add a new user
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.save(a));
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
	
	// update - update a User
	@PutMapping("/")
	public JsonResponse updateUser(@RequestBody User a) {
		// update a user
		JsonResponse jr = null;
		try {
			if (userRepo.existsById(a.getId())) {
				jr = JsonResponse.getInstance(userRepo.save(a));
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating User.  id: "+
											a.getId() + " doesn't exist!");
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// delete - delete a User
	@DeleteMapping("/{id}")
	public JsonResponse deleteUser(@PathVariable int id) {
		// delete a user
		JsonResponse jr = null;
		
		try {
			if (userRepo.existsById(id)) {
				userRepo.deleteById(id);
				jr = JsonResponse.getInstance("Delete successful!");
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error deleting User.  id: "+
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

}

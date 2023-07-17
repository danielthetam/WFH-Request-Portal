package com.daniel.reactspringdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daniel.reactspringdemo.model.Request;
import com.daniel.reactspringdemo.repository.RequestRepository;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api")
public class RequestController {

	@Autowired // What
	RequestRepository requestRepository;

	@GetMapping("/requests")
	public ResponseEntity<List<Request>> getAllRequests(@RequestParam(required=false) String requester) {
		try {
			List<Request> requests = new ArrayList<Request>();

			if (requester == null)
				requestRepository.findAllByOrderByIdDesc().forEach(requests::add);
			else
				requestRepository.findByRequester(requester).forEach(requests::add);
			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/requests/{id}")
	public ResponseEntity<Request> getRequestById(@PathVariable("id") long id) {
		Optional<Request> requestData = requestRepository.findById(id);

		if (requestData.isPresent()) {
			return new ResponseEntity<>(requestData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/requests")
	public ResponseEntity<Request> createTutorial(@RequestBody Request request) {
		try {
			Request _request = requestRepository.save(new Request(request.getRequester(), false, request.getRequestDate(), request.getReason(), false, request.getDuration()));
			return new ResponseEntity<>(_request, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/requests/{id}")
	// Difference between post(create?) and put(update?)
	// @RequestBody vs @RequestParam vs @PathVariable
	public ResponseEntity<Request> updateRequest(@PathVariable("id") long id, @RequestBody Request request) {
		// what is optional bro
		Optional<Request> requestData = requestRepository.findById(id);

		if (requestData.isPresent()) {
			Request _request = requestData.get();
			_request.setRequester(request.getRequester());
			_request.setReason(request.getReason());
			_request.setApproved(request.getApproved());
			_request.setRequestDate(request.getRequestDate());
			_request.setRejected(request.getRejected());
			_request.setDuration(request.getDuration());
			return new ResponseEntity<>(requestRepository.save(_request), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/requests/{id}")
	public ResponseEntity<HttpStatus> deleteRequest(@PathVariable("id") long id) {
		try {
			requestRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/requests")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			requestRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/requests/approved")
	public ResponseEntity<List<Request>> findByApproved() {
		try {
			List<Request> requests = requestRepository.findByApproved(true);

			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/requests/ascdate")
	public ResponseEntity<List<Request>> findByDateAsc() {
		try {
			List<Request> requests = requestRepository.findByOrderByDateAsc();

			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/requests/descdate")
	public ResponseEntity<List<Request>> findByDateDesc() {
		try {
			List<Request> requests = requestRepository.findByOrderByDateDesc();

			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/requests/rejected")
	public ResponseEntity<List<Request>> findByRejected() {
		try {
			List<Request> requests = requestRepository.findByRejected(true);

			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/requests/unreviewed")
	public ResponseEntity<List<Request>> findByRejectedAndApproved() {
		try {
			List<Request> requests = requestRepository.findByRejectedAndApproved(false, false);

			if (requests.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(requests, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

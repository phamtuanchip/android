package com.thanhgiong.note8.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Note8RestController {

	@RequestMapping(value="/data", method = RequestMethod.GET)
	public List<Note> getNote() {
		List<Note> data = new ArrayList<Note>();
		return data;

	}
	
	@RequestMapping(value = "/data/{id}")
    public ResponseEntity<Note> getEmployeeById (@PathVariable("id") int id)
    {
        if (id <= 3) {
            Note note = new Note();
            return new ResponseEntity<Note>(note, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
	
}
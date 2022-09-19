package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO createNewStudent(@RequestBody StudentDTO student) {
		//Checking to see if their is a student with that email already.
		Student newStu = studentRepository.findByEmail(student.email);
		
		if(newStu == null ) {
			//If everything checks out, create the new student.
			Student students = new Student();
			students.setEmail(student.email);
			students.setName(student.name);

			//Storing the student.
			Student newStudent = studentRepository.save(students);
			StudentDTO result = createStudentDTO(newStudent);
			
			return result;
			
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email " + student.email + " is already taken.");
		}
	
	}

	private StudentDTO createStudentDTO(Student stu) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = stu.getStudent_id();
		studentDTO.email = stu.getEmail();
		studentDTO.name = stu.getName();
		studentDTO.statusCode = stu.getStatusCode();
		studentDTO.status = stu.getStatus();
		
		return studentDTO;
	}

	@PostMapping("/student/statusCode/{email}")
	@Transactional
	public void holdStudent(@RequestBody StudentDTO hold) {
		//Checking to see if their is a student with that email already.
		Student newStudent = studentRepository.findByEmail(hold.email);
		
		
		if(newStudent != null ) {
			//If there is then we will hold the student.
			newStudent.setStatusCode(1);
			newStudent.setStatus("Hold");
			
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student was not found.");
		}
	
	}
	
	
	@PostMapping("/student/release/{email}")
	@Transactional
	public void releaseStudent(@RequestBody StudentDTO release) {
		//Checking to see if their is a student with that email already.
		Student newStudent = studentRepository.findByEmail(release.email);
		
		
		if(newStudent != null ) {
			//If there is then we will release the student.
			newStudent.setStatusCode(0);
			newStudent.setStatus(null);
			
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student was not found.");
		}
	
	}




}

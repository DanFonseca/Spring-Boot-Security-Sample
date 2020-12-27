package com.github.daniel.demo.controller

import com.github.daniel.demo.model.Student
import com.github.daniel.demo.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/students")
class StudentController (
        val service: StudentService
) {

    @GetMapping ("/{id}")
    fun getStudents (@PathVariable id: Int) :Student?{
        return service.getStudentById(id)
    }
}
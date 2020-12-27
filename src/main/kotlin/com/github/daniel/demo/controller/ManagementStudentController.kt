package com.github.daniel.demo.controller

import com.github.daniel.demo.model.Student
import com.github.daniel.demo.service.StudentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("management/api/v1/students")
class ManagementStudentController (
        val studentService: StudentService
        ) {

    @GetMapping
    fun getAllStudents () = studentService.students

    @PostMapping
    fun registerNewStudent (@RequestBody student: Student) {
        studentService.registerNewStudent(student)
    }

    @DeleteMapping ("/{studentId}")
    fun deleteStudent (@PathVariable studentId: Int) {
        studentService.deleteById(studentId)
    }

    @PutMapping("/{studentId}")
    fun updateStudentById (@PathVariable studentId: Int, @RequestBody student: Student) {
        studentService.updateStudentById(studentId, student )
    }
}
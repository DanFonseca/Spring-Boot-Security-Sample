package com.github.daniel.demo.controller

import com.github.daniel.demo.model.Student
import com.github.daniel.demo.service.StudentService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("management/api/v1/students")
class ManagementStudentController (
        val studentService: StudentService
        ) {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMIN_TRAINEE')")
    fun getAllStudents () = studentService.students

    @PostMapping
    @PreAuthorize("hasAuthority('course:write')")
    fun registerNewStudent (@RequestBody student: Student) {
        studentService.registerNewStudent(student)
    }

    @DeleteMapping ("/{studentId}")
    @PreAuthorize("hasAuthority('course:write')")
    fun deleteStudent (@PathVariable studentId: Int) {
        studentService.deleteById(studentId)
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('course:write')")
    fun updateStudentById (@PathVariable studentId: Int, @RequestBody student: Student) {
        studentService.updateStudentById(studentId, student )
    }
}
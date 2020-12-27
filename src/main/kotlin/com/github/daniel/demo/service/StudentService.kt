package com.github.daniel.demo.service

import com.github.daniel.demo.model.Student
import org.springframework.stereotype.Service

@Service
class StudentService {
    val students = listOf(
            Student(1, "Jorgin da ZS"),
            Student(2, "Pedrin do Icarai"),
            Student(3, "Felomena do CC")
    )

    fun getStudentById(studentId: Int):Student? {
        return  students.find { x -> x.studentId == studentId }
    }

    fun registerNewStudent (student: Student) {
        students.toMutableList().add(student)
    }

    fun deleteById(studentId: Int) {
        students.toMutableList().removeAt(studentId)
    }

    fun updateStudentById(studentId: Int, student: Student) {
     students.toMutableList().apply {
         val currentStudent = getStudentById(studentId)
         val index = this.indexOf(currentStudent)
         this[index] = student
     }
    }
}
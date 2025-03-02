package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeacherDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/courseTeacher/list/{courseId}")
    public  List<CourseTeacher> getCourseTeacher(@PathVariable Integer courseId){

       List<CourseTeacher> teachers= teacherService.getCourseTeacherByCourseId(courseId);
       return teachers;

    }

    @PostMapping("/courseTeacher")
    public CourseTeacher addCourseTeacher(@RequestBody SaveTeacherDto saveTeacherDto){
        CourseTeacher save=teacherService.saveTeacher(saveTeacherDto);
        return save;
    }

    @PutMapping("/courseTeacher")
    public CourseTeacher updateCourseTeacher(@RequestBody SaveTeacherDto saveTeacherDto){
        CourseTeacher update = teacherService.updateTeacher(saveTeacherDto);
        return update;
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable Integer courseId,@PathVariable Integer teacherId){
        teacherService.deleteTeacher(courseId,teacherId);
    }
}

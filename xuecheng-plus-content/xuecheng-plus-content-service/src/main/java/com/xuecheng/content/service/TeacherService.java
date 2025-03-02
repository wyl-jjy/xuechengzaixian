package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface TeacherService {

    /**
     * 根据课程Id查询老师
     * @param courseId
     * @return
     */
    List<CourseTeacher> getCourseTeacherByCourseId(Integer courseId);

    /**
     * 保存老师
     * @param saveTeacherDto
     * @return
     */
    CourseTeacher saveTeacher(SaveTeacherDto saveTeacherDto);

    /**
     * 更新教师信息
     * @param saveTeacherDto
     * @return
     */
    CourseTeacher updateTeacher(SaveTeacherDto saveTeacherDto);

    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    void deleteTeacher(Integer courseId, Integer teacherId);
}

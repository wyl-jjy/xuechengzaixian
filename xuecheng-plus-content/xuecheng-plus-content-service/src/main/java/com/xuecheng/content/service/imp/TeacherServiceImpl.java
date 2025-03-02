package com.xuecheng.content.service.imp;

import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.SaveTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final CourseTeacherMapper courseTeacherMapper;

    /**
     * 根据课程ID查询老师
     * @param courseId
     * @return
     */
    @Override
    public List<CourseTeacher> getCourseTeacherByCourseId(Integer courseId) {

        List<CourseTeacher> teachers = courseTeacherMapper.selectByCourseId(courseId);
        return teachers;
    }

    /**
     * 保存教师信息
     * @param saveTeacherDto
     * @return
     */
    @Override
    public CourseTeacher saveTeacher(SaveTeacherDto saveTeacherDto) {
        CourseTeacher courseTeacher = new CourseTeacher();
        BeanUtils.copyProperties(saveTeacherDto, courseTeacher);

        //判断老师是否存在
        CourseTeacher selectTeacher = courseTeacherMapper.selectById(courseTeacher.getId());
        if(selectTeacher != null){
            //老师存在，更新老师信息
            courseTeacherMapper.updateById(courseTeacher);
        }else {
            //不存在就新增
            courseTeacherMapper.insert(courseTeacher);
        }

        //查询数据回显
        CourseTeacher result= courseTeacherMapper.selectById(courseTeacher.getId());
        return result;
    }

    /**
     * 更新教师信息
     * @param saveTeacherDto
     * @return
     */
    @Override
    public CourseTeacher updateTeacher(SaveTeacherDto saveTeacherDto) {

        CourseTeacher courseTeacher = new CourseTeacher();
        BeanUtils.copyProperties(saveTeacherDto, courseTeacher);
        System.out.println(saveTeacherDto);
        System.out.println(courseTeacher);
        courseTeacherMapper.updateById(courseTeacher);

        return courseTeacher;
    }

    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    @Override
    public void deleteTeacher(Integer courseId, Integer teacherId) {
        courseTeacherMapper.deleteByIds(courseId,teacherId);
    }
}

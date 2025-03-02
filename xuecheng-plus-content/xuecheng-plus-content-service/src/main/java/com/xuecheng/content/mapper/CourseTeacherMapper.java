package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {


    @Select("select * from course_teacher where course_id=#{courseId};")
    List<CourseTeacher> selectByCourseId(Integer courseId);

    @Delete("delete from course_teacher where course_id=#{courseId} and id=#{teacherId}")
    void deleteByIds(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId);

    @Delete("delete  from course_teacher where course_id=#{id}")
    void deleteByCourseId(Long id);
}

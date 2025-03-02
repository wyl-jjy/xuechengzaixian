package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * @description 查询课程计划树型结构
     * @param courseId  课程id
     * @return List<TeachplanDto>
     * @author Mr.M
     * @date 2022/9/9 11:13
     */
    public List<TeachplanDto> selectTreeNodes(long courseId);

    @Select("SELECT COUNT(*) FROM teachplan where parentid=#{teachplanId};")
    Integer selectCountById(Long teachplanId);

    @Select("select * from teachplan where orderby=#{orderby} AND parentid=#{parentId} AND course_id=#{courseId}")
    Teachplan selectByOrderBy(@Param("orderby")Integer orderby, @Param("parentId") Long parentId, @Param("courseId") Long courseId);

    @Select("select MAX(orderby) FROM teachplan where parentid=#{parentid}")
    Integer selectMax(Long parentid);

    @Delete("delete from teachplan where course_id=#{id}")
    void deleteByCourseId(Long id);
}

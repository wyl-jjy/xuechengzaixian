package com.xuecheng.content.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description 课程计划service接口实现类
 * @author Mr.M
 * @date 2022/9/9 11:14
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    private final TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {

        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id!=null){
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else{
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);

        }

    }


    /**
     * @description 获取最新的排序号
     * @param courseId  课程id
     * @param parentId  父课程计划id
     * @return int 最新排序号
     * @author Mr.M
     * @date 2022/9/9 13:43
     */
    private int getTeachplanCount(long courseId,long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 删除课程计划信息
     *
     * @param teachplanId
     * @return
     */
    @Override
    public void deleteTeacherPlanById(Long teachplanId) {

        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        //判断是否是一级标题
        //grade=1 是一级标题
        if(teachplan.getGrade()==1){
            //一级标题，判断是否有二级标题
            Integer count=teachplanMapper.selectCountById(teachplanId);
            if(count==0){
                teachplanMapper.deleteById(teachplanId);
                return;
            }
            //说明有二级标题，删除失败
            throw new XueChengPlusException("120409","课程计划信息还有子级信息，无法操作");
        }else {
            //二级标题直接删除
            //删除与二级标题相关的media
            teachplanMediaMapper.deleteByParentId(teachplanId);
            teachplanMapper.deleteById(teachplanId);
        }
    }

    /**
     * 移动课程计划
     * @param move
     * @param id
     */
    @Override
    public void moveTeachPlan(String move, Long id) {
        //上移动
        if(move.equals("moveup")){
            moveUp(id);
        }else {
            movedown(id);
        }
    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            XueChengPlusException.cast("教学计划不存在");
        }
        Integer grade = teachplan.getGrade();
        if(grade!=2){
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }
        //课程id
        Long courseId = teachplan.getCourseId();

        //先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));

        //再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

    private void movedown(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer max=teachplanMapper.selectMax(teachplan.getParentid());
        Integer orderby = teachplan.getOrderby();
        if(orderby!=max){
            orderby=orderby+1;
            //查询原有位置的信息
            Teachplan teachplanOri = teachplanMapper.selectByOrderBy(orderby,teachplan.getParentid(),teachplan.getCourseId());
            System.out.println(teachplanOri);
            //更新
            teachplan.setOrderby(orderby);
            teachplanMapper.updateById(teachplan);

            teachplanOri.setOrderby(teachplan.getOrderby()-1);
            teachplanMapper.updateById(teachplanOri);
        }
    }

    private void moveUp(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer orderby = teachplan.getOrderby();
        if(orderby!=1){
            orderby=orderby-1;
            //查询原有位置的信息
            Teachplan teachplanOri = teachplanMapper.selectByOrderBy(orderby,teachplan.getParentid(),teachplan.getCourseId());
            System.out.println(teachplanOri);
            //更新
            teachplan.setOrderby(orderby);
            teachplanMapper.updateById(teachplan);

            teachplanOri.setOrderby(teachplan.getOrderby()+1);
            teachplanMapper.updateById(teachplanOri);
        }
    }



}
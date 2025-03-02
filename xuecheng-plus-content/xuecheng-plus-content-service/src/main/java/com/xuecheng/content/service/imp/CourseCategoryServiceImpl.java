package com.xuecheng.content.service.imp;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCategoryServiceImpl implements CourseCategoryService {


    private final CourseCategoryMapper courseCategoryMapper;



    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {

        //调用递归mapper递归查询出分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        System.out.println(courseCategoryTreeDtos);
        //封装成List<CourseCategoryTreeDto>
        //将list转化为map，key就是节点id，value就是对象。
        Map<String, CourseCategoryTreeDto> map = courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(),
                value -> value, (key1, key2) -> key2));

        //定义一个结果list
        ArrayList<CourseCategoryTreeDto> resultList = new ArrayList<>();


        //遍历List<CourseCategoryTreeDto>,找子节点，放在父节点属性中。
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            //向list中写入元素
            if(item.getParentid().equals(id)){
                resultList.add(item);
            }
            //找到每个子节点，放在父节点的childrenTreeNodes属性中
            CourseCategoryTreeDto courseCategoryTreeDto = map.get(item.getParentid());
            //map已经过滤掉根节点，所以判断courseCategoryTreeDto != null，如果为null，则一定不为子节点
            if(courseCategoryTreeDto != null){
                if(courseCategoryTreeDto.getChildrenTreeNodes()==null){
                    //如果该父节点的childrenTreeNodes属性为空，new一个集合，放它的子节点
                    courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
            }
        });
        return resultList;
    }


}

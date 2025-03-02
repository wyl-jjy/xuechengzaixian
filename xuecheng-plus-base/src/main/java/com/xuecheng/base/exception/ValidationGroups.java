package com.xuecheng.base.exception;
/**
 * @description 校验分组(区分开当不同接口调用相同实体类时，错误信息交错)
 * @author Mr.M
 * @date 2022/9/8 15:05
 * @version 1.0
 */
public class ValidationGroups {

    public interface Insert {};
    public interface Update{};
    public interface Delete{};

}
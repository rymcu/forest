package com.rymcu.vertical.core.constant;

/**
 * 项目常量
 */
public final class ProjectConstant {
    public static final String BASE_PACKAGE = "com.rymcu.vertical";//项目基础包名称，根据自己公司的项目修改

    public static final String DTO_PACKAGE = BASE_PACKAGE + ".dto";//DTO所在包
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".entity";//Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";//Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";//Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.mapper.Mapper";//Mapper插件基础接口的完全限定名
}

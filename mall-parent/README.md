
# 概述

- 本项目为个人向，基于 SpringBoot 构建的 web 后端脚手架，经典 Web 三层架构
- 提供最基本的功能编写样例：认证、拦截、鉴权、通用 CRUD
- dao 层提供 jpa 和 mybatis-plus 两种配置样板（故存在部分代码冗余），统一基于 mall-api 编写 service 以提供一致的 service 服务
- mall-web 基于 mall-api 进行调用，通过依赖不同的实现包进行切换测试

# 相关依赖于技术

- 使用 lombok 和 mapstruct 简化 pojo 及其互相转换
- 登录方案：登录后，随机生成 uuid 作为 token 并存储于 Redis 中（TokenUtil），同时使用拦截器 LoginInterceptor 进行拦截并解析请求头的 token，识别 userId 并设置到 UserIdThreadLocal 供本次请求使用
- 缓存方案：采用 Redis（默认为 localhost:6379），对应工具类代码为 RedisUtil
- 鉴权：使用自定义 @Perm 注解配合 aop 拦截 Controller 层方法实现，拦截逻辑见 PermProcessor
- 异常：程序内部的可预知异常统一基于 BaseException 及其子类实现，并由 GlobalExceptionHandler 统一处理并转化为 json 提示，同时 GlobalExceptionHandler 还统一处理了由 @Validate 参数校验抛出的各类异常


# 实体与数据表

- 鉴权模块：User, Role, Perm, RolePerm 四个实体，采用经典用户-角色-权限模型，其中 User 直接添加 roleId 属性减少一张表
- 数据模块：Category 和 Product，测试数据查询
- 其中为了测试 sql 时间类型（timestamp, datetime, date）与 java 时间类型（Date, LocalDate, LocalDateTime）互相映射的情况，各个数据表的时间类型并不完全一致，只是为了方便测试
- PS. 个人还用过另一种方案，数据库直接采用 bigint 类型表示时间，存储时间戳 System.currentTimeMillis()，此处没有测试，因为其本质上是映射 long 类型
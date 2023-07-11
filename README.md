## 项目配置文件，信息脱敏版
```yml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://数据库地址:3306/shareprinter?characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: 数据库账号
    password: 数据库密码
    # 指定为HikariDataSource
    type: com.zaxxer.hikari.HikariDataSource
    # hikari连接池配置
    hikari:
      #连接池名
      pool-name: HikariCP
      #最小空闲连接数
      minimum-idle: 5
      # 空闲连接存活最大时间，默认10分钟
      idle-timeout: 600000
      # 连接池最大连接数，默认是10
      maximum-pool-size: 10
      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true
      auto-commit: true
      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
      max-lifetime: 1800000
      # 数据库连接超时时间,默认30秒
      connection-timeout: 30000
      # 连接测试query
      connection-test-query: SELECT 1
  redis:
    host: Redis地址
server:
  port: 12345
mybatis-plus:
  mapper-locations: classpath:mapper/*Mapper.xml
minio:
  endpoint: http://192.168.0.88:9000
  bucketName: printer
  accessKey: 访问的key
  secretKey: 访问的密钥
```
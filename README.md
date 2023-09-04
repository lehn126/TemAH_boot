# TemAH项目说明

TemAH是我在学习Spring各组件过程中产生的技术验证项目。分为仅使用Spring boot的[TemAH_boot](https://github.com/lehn126/TemAH_boot)和添加了Spring Cloud组件的[TemAH_cloud](https://github.com/lehn126/TemAH_cloud)两个版本。TemAH项目中包含的主要功能模块为：

* 模块TemAH_lam用于本地或远程主机的负载监控任务管理及相关告警的创建和分发。

* 模块TemAH_adapter用于从Kafka（已完成）或MQ（待添加）拉取已生成的告警并做告警格式标准化处理和分发。

* 模块TemAH_ahfm用于接收模块TemAH_lam或TemAH_adapter分发的告警，并进行数据库存储和告警信息管理。

* 模块TemAH_monitor是一个基于SpringBoot Admin Server的服务状态监控中心。

* 模块TemAH_auth是基于 spring-security-oauth2（2.3.8.RELEASE）实现的 OAuth2 授权服务器。由于其中使用的spring-security-oauth2 相关依赖库代码Spring官方已经停止维护。因此该模块仅用于学习和理解OAuth2相关概念。

* 模块TemAH_auth_server是基于Spring的Oauth2授权服务组件spring-security-oauth2-authorization-server实现的OAuth2授权服务器。用于替换模块TemAH_auth实现Oauth2作为新的授权服务器。

* 模块TemAH_auth_resource用于配合授权服务器模块TemAH_auth_server作为一个简单的测试用资源服务器。

* 模块TemAH_auth_client用于配合授权服务器模块TemAH_auth_server作为一个简单的测试用客户端。

* 模块TemAH_common，包含一些供各模块内使用的通用代码。

* 模块TemAH_common_redis，包含一些使用Redis可能会用到的通用代码。

* 模块TemAH_common_kafka，包含一些使用Kafka可能会用到的通用代码。

* 模块TemAH_common_alarm，包含告警处理会使用到的一些通用代码。

* 模块TemAH_demo_jwt，一个使用JWT token和自定义Aspect AOP注解进行token验证的demo。

项目中涉及告警处理的主要功能由模块TemAH_lam和TemAH_ahfm完成。因此在极简部署模式下仅需要部署模块TemAH_lam和TemAH_ahfm即可完成设备监控任务管理和告警相关处理。同时可以在不改动项目源代码仅修改Yaml配置的情况下加添加Kafka/MQ和模块TemAH_adapter来进行高并发情景下的异步消息处理。

下面是各主要功能模块的简介和使用到的技术说明。

## TemAH_lam

用于动态创建和管理本地（Windows或Linux）及远程（Linux）主机的负载监控任务。完成主机CPU及内存负载状态的监控，并在负载超过设定的阙值时创建和分发监控告警。

主要配置:

```
alarm:
    consumer:
      address: "127.0.0.1:8001" # 最终告警消费者AHFM的地址
      restful:
        enable: false
        uri: http://${alarm.consumer.address}/alarm/new
      kafka: # 发送告警到Kafka时才需要配
        enable: true
        topic: topic_alarm # 发送告警到Kafka使用的topic
```

在极简化部署时配置`alarm.consumer.kafka.enable`为`false`并设置`alarm.consumer.restful.enable`为`true`将产生的告警直接发送到模块TemAH_ahfm进行存储和管理。
在搭配模块TemAH_adapter和Kafka使用时配置`alarm.consumer.kafka.enable`为`true`并设置`alarm.consumer.restful.enable`为`false`将产生的告警分发至Kafka的指定topic供后续处理。

涉及技术:

| 名称                | 版本     |
| ----------------- | ------ |
| Springboot        | 2.7.14 |
| jsch              | 0.1.55 |
| mybatis           | 2.3.1  |
| spring-boot-admin | 2.7.10 |
| spring-kafka      | 2.9.10 |

可以无缝搭配Vue开发的前端项目[TemAH_web_vue](https://github.com/lehn126/TemAH_web_vue2)进行监控任务管理。

## TemAH_adapter

用于对不同来源格式的告警进行标准化转换及在高负载高并发情景下使用Kafka/MQ进行削峰和异步告警消息处理和分发。

主要配置：

```
alarm:
  adapter: # 只有从Kafka收告警的adapter端点才需要配
    kafka:
      topic: topic_alarm # 接收告警使用的topic
      group: group_alarm # 接收告警使用的group
  consumer:
    address: "127.0.0.1:8001" # 最终告警消费者AHFM的地址
    restful:
      enable: true
      uri: http://${alarm.consumer.address}/alarm/new
    kafka: # 发送adapter转换后的告警到Kafka时才需要配
        enable: false
        topic: topic_ahfm # 发送adapter转换后的告警到Kafka使用的topic
```

如果需要将adapter作标准化格式转换后的告警再次分发到Kafka指定的topic供其他资源进行消费，需要将配置`alarm.consumer.restful.enable`设置为`false`并将配置`alarm.consumer.kafka.enable`设置为`true`。

涉及技术:

| 名称                | 版本     |
| ----------------- | ------ |
| Springboot        | 2.7.14 |
| spring-boot-admin | 2.7.10 |
| spring-kafka      | 2.9.10 |

### TemAH_ahfm

用于接收从模块TemAH_lam（极简化部署）或TemAH_adapter（搭配Kafka使用）分发过来的设备告警并提供简单的告警管理服务（入库，查询，修改，状态变更）。

涉及技术:

| 名称                | 版本     |
| ----------------- | ------ |
| Springboot        | 2.7.14 |
| mybatis           | 2.3.1  |
| spring-boot-admin | 2.7.10 |

可以无缝搭配Vue开发的Web前端项目[TemAH_web_vue](https://github.com/lehn126/TemAH_web_vue2)进行告警信息管理。

## TemAH_auth

基于 spring-security-oauth2 （2.3.8.RELEASE）实现的 OAuth2 授权服务器。生成的 token 默认保存在内存中。服务器中已注册的clients信息为硬编码形式并默认保存在内存中。由于这个工程中使用的 spring-security-oauth2 依赖库中用于实现 OAuth2 授权服务器的相关代码spring已经停止维护，因此 TemAH_auth 工程中的代码只用于理解OAuth2相关概念。后续工程 TemAH_auth_server 使用spring提供的 spring-security-oauth2-authorization-server 进行了新版本的 OAuth2 授权服务器实现。

### **自定义配置项：**

security: 
  oauth2: 
    jwt: 
      enable: false  # 是否使用JWT token
      signingKey: 123456  # 使用JWT token时使用的签名密钥
    authServer: 
      supportRefreshToken: true  # 是否支持refresh token
      accessTokenValiditySeconds: 7200  # access token的有效时间（秒）
      refreshTokenValiditySeconds: 259200 # refresh token的有效时间（秒）

### **测试：**

#### 授权码模式

1. 请求授权码
   
   用浏览器发送http get请求：
   
   ```
   http://localhost:7001/oauth/authorize?response_type=code&client_id=client_1&secret=123&redirect_uri=https://www.baidu.com&scope=all
   ```
   
   浏览器会跳转到登陆界面。使用admin/admin模拟用户登陆后会自动跳转到授权界面。确认Approve后点击Authorize按钮会自动跳转到 redirect_uri 并附带上授权码，可以在浏览器的地址栏看到授权码信息：
   
   ```
   https://www.baidu.com/?code=SBsZJ6
   ```

2. 使用获得的授权码请求access token和refresh token
   
   复制上一步在浏览器地址栏看到的授权码**SBsZJ6**，在postman创建一个**POST**请求：
   
   地址：
   
   ```
   http://localhost:7001/oauth/token
   ```
   
   参数：
   
   ```
   grant_type=authorization_code
   code=SBsZJ6
   client_id=client_1
   client_secret=123
   redirect_uri=https://www.baidu.com
   ```
   
   code参数值为获得的授权码，redirect_uri 地址必须使用 `https://www.baidu.com`(在服务器内注册的 client_1 使用的redirect_uri固定就是这个!!! 可以在代码中进行修改)。
   
   发送请求后会收到如下token：
   
   ```
   {
       "access_token": "1ceb9b7a-60b1-44f5-98a4-ab7fda82b95f",
       "token_type": "bearer",
       "refresh_token": "bb97f68d-3d80-493c-bb5a-be6310bec98e",
       "expires_in": 6003,
       "scope": "all"
   }
   ```

3. 刷新access token
   
   使用获得的refresh_token在postman创建一个**POST**请求：
   
   地址：
   
   ```
   http://localhost:7001/oauth/token
   ```
   
   参数：
   
   ```
   grant_type=refresh_token
   refresh_token=bb97f68d-3d80-493c-bb5a-be6310bec98e
   client_id=client_1
   client_secret=123
   ```
   
   refresh_token参数值为上一步获得的refresh_token。
   
   发送请求后收到更新的access token信息：
   
   ```
   {
       "access_token": "1ceb9b7a-60b1-92a8-98a4-ag9fda82b63b",
       "token_type": "bearer",
       "refresh_token": "bb97f68d-3d80-493c-bb5a-be6310bec98e",
       "expires_in": 7199,
       "scope": "all"
   }
   ```

#### 密码模式

使用用户名和密码直接获取access token。在postman创建一个**POST**请求：

地址：

```
http://localhost:7001/oauth/token
```

参数：

```
grant_type=password
username=admin
password=admin
client_id=client_1
client_secret=123
```

发送请求后收到access token和refresh token：

```
{
    "access_token": "1ceb9b7a-60b1-92a8-98a4-ag9fda82b63b",
    "token_type": "bearer",
    "refresh_token": "bb97f68d-3d80-493c-bb5a-be6310bec98e",
    "expires_in": 4976,
    "scope": "all"
}
```

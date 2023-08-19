# TemAH_auth

基于 spring-security-oauth2 （2.3.8.RELEASE）实现的 OAuth2 授权服务器。生成的 token 默认保存在内存中。服务器中已注册的clients信息为硬编码形式并默认保存在内存中。由于这个工程中使用的 spring-security-oauth2 依赖库中用于实现 OAuth2 授权服务器的相关代码spring已经停止维护，因此 TemAH_auth 工程中的代码只用于理解OAuth2相关概念。后续工程 TemAH_auth_server 使用spring提供的 spring-security-oauth2-authorization-server 进行了新版本的 OAuth2 授权服务器实现。

## **自定义配置项：**

security: 
  oauth2: 
    jwt: 
      enable: false  # 是否使用JWT token
      signingKey: 123456  # 使用JWT token时使用的签名密钥
    authServer: 
      supportRefreshToken: true  # 是否支持refresh token
      accessTokenValiditySeconds: 7200  # access token的有效时间（秒）
      refreshTokenValiditySeconds: 259200 # refresh token的有效时间（秒）

## **测试：**

### 授权码模式

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

### 密码模式

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

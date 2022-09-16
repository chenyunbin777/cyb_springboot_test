# 索引创建
- 通过postman 的put请求去创建索引，put请求是幂等性的创建完成在请求会报错，
post请求是不被ES允许的
```
curl --location --request PUT 'http://localhost:9200/shopping' \
--header 'Cookie: JSESSIONID=0EF160F86EFF6BC4FA9ECBB4579ADE01'
```






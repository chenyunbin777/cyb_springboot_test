
#查看文档时，需要指明文档的唯一性标识，类似于 MySQL 中数据的主键查询
- 在 Postman 中，向 ES 服务器发 GET 请求 ： http://127.0.0.1:9200/shopping/_doc/1 。

```
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1",
    "_version": 1,
    "_seq_no": 1,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "title": "小米手机",
        "category": "小米",
        "images": "http://www.gulixueyuan.com/xm.jpg",
        "price": 3999
    }
}
```


# 查找不存在的内容向 
- ES 服务器发 GET 请求 ： http://127.0.0.1:9200/shopping/_doc/1001。
- 返回结果如下：

```
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1001",
    "found": false
}
```



#查看索引下所有数据
- 向 ES 服务器发 GET 请求 ： http://127.0.0.1:9200/shopping/_search。


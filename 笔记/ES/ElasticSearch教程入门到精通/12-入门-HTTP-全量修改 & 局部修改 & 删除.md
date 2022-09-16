# 全量修改
- 和新增文档一样，输入相同的 URL 地址请求，如果请求体变化，会将原有的数据内容覆盖
- 在 Postman 中，向 ES 服务器发 PUT 请求 ： http://127.0.0.1:9200/shopping/_doc/1

请求体JSON内容为:

`{
    "title":"华为手机",
    "category":"华为",
    "images":"http://www.gulixueyuan.com/hw.jpg",
    "price":1999.00
}`


# 局部修改
- 修改数据时，也可以只修改某一给条数据的局部信息
- 在 Postman 中，向 ES 服务器发 POST 请求 ： http://127.0.0.1:9200/shopping/_update/1。
- 请求体JSON内容为: 修改_id = 1 的title、category数据
`
{
	"doc": {
		"title":"华为手机",
		"category":"华为"
	}
}
`


# 删除
- 删除一个文档不会立即从磁盘上移除，它只是被标记成已删除（逻辑删除）。
- 在 Postman 中，向 ES 服务器发 DELETE 请求 ： http://127.0.0.1:9200/shopping/_doc/1





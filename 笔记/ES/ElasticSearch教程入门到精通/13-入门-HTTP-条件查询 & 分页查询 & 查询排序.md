# URL带参查询
- 查找category为小米的文档，在 Postman 中，向 ES服务器发GET请求 ： http://127.0.0.1:9200/shopping/_search?q=category:小米


- 上述为URL带参数形式查询，这很容易让不善者心怀恶意，或者参数值出现中文会出现乱码情况。为了避免这些情况，我们可用使用带JSON请求体请求进行查询。

# 请求体带参查询
接下带JSON请求体，还是查找category为小米的文档，在 Postman 中，向ES服务器发GET请求 ： http://127.0.0.1:9200/shopping/_search，附带JSON体如下：

```
{
	"query":{
		"match":{
			"category":"小米"
		}
	}
}


{
	"query":{
		"match_all":{}
	}
}
```


# 查询指定字段

如果你想查询指定字段，在 Postman 中，向 ES 服务器发 GET请求 ： http://127.0.0.1:9200/shopping/_search，附带JSON体如下：

`
{
	"query":{
		"match_all":{}
	},
	"_source":["title"]
}
`

# 分页查询

在 Postman 中，向 ES 服务器发 GET请求 ： http://127.0.0.1:9200/shopping/_search，附带JSON体如下：

```
{
	"query":{
		"match_all":{}
	},
	"from":0,
	"size":2
}

```

# 查询排序

```
{
	"query":{
		"match_all":{}
	},
	"sort":{
		"price":{
			"order":"desc"
		}
	}
}
```

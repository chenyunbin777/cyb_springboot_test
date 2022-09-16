# 多条件查询
## must《=》 and
- 假设想找出小米牌子，价格为3999元的。（must相当于数据库的&&）

在 Postman 中，向 ES 服务器发 GET请求 ： http://127.0.0.1:9200/shopping/_search，附带JSON体如下：
```
{
	"query":{
		"bool":{  //条件的意思
			"must":[  //需要多个条件成立才可以，所以使用[] 括号，与and类似
                {  
				"match":{
					"category":"小米"
				}
			},{
				"match":{
					"price":3999.00
				}
			}
            ]
		}
	}
}

```

## should《=》or

```
{
	"query":{
		"bool":{
			"should":[{ //小米 or 华为
				"match":{
					"category":"小米"
				}
			},{
				"match":{
					"category":"华为"
				}
			}]
		},
        "filter":{
            "range":{  //范围查询
                "price":{
                    "gt":2000
                }
            }
        }
	}
}

```

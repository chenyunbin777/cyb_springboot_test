# ES中的字段详解

## 1 _ignored field
- 字段索引并存储文档中每个字段的名称，这些_ignored字段在文档被索引时被忽略。
例如，当字段格式错误并ignore_malformed 打开时，或者当keyword字段值超过其可选 ignore_above设置时，可能会出现这种情况。
该字段可使用和查询进行搜索term，并作为搜索结果的一部分返回。termsexists

- 例如，以下查询匹配具有一个或多个被忽略的字段的所有文档
```
GET index/_search
{
  "query": {
    "exists": {
      "field": "_ignored"
    }
  }
}

```

- 类似地，以下查询查找field字段在索引时忽略其字段的所有文档
```
GET index/_search
{
  "query": {
    "term": {
      "_ignored": "field"  字段名称
    }
  }
}
```



```
{
	"_index": "my-index-dynamic-02",
	"_type": "_doc",
	"_id": "1",
	"_version": 2,
	"_seq_no": 1,
	"_primary_term": 1,
	"found": true,
	"_source": {
		"user": {
			"social_networks": {
				"network1": "haha",
				"network2": "lala"
			}
		}
	}
}}

```
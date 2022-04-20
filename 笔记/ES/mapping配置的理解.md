#

## 多fields情况

- 可以看到，我的es索引在定义的时候，city这个text类型里面还有一个fields的属性，这里涉及到多fileds的概念，名字叫raw，类型是keyword，keyword就是为了在做聚合 排序、精准匹配的时候使用的，keyword不会被分词，直接使用原始的文档去做匹配。
官方对多fileds的文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/multi-fields.html，
其实就是对一个字段做出多种分词处理，keyword即不分词，所以使用term的时候，请多注意一下，需要精准匹配原文档，请带上keyword类型的字段去查询，模糊查询就用match。
    - 原文链接：https://blog.csdn.net/cainiao1412/article/details/120798754

```
"itemName": {
	"type": "text",
	"fields": { //可以指定多种 分析器 来进行分词，有一些分词器都是在setting中配置好的自定义的分词器
		"ascii_ngram_2_2": {
			"type": "text",
			"analyzer": "name_ascii_ngram_2_2"
		},
		"ascii_word": {
			"type": "text",
			"analyzer": "name_ascii_word"
		},
		"ik_smart": {
			"type": "text",
			"analyzer": "name_ik_smart"
		},
		"keyword": {
			"type": "text",
			"analyzer": "lowercase_keyword"
		}
        "analyzer": "name_ik_max_word",
        "search_analyzer": "name_ik_smart"
	}
}
```

- 在查询的时候就可以通过 关键词.field   来指定使用按个分词器
如下：name：使用默认的search_analyzer：name_ik_smart分析器   name.ascii_word   name.ik_smart

- 测试：如果确定name 字段使用的  ik_smart 还是ik_max_word分词呢？
我们创建一个名为"系统"的文件。
    - "系统学" ik_smart 分词只能分出 "系统学" ；
    - 而ik_max_word可以分出 系统学 系统 学 三个分词。
    - 如果使用关键字 "系统学" 去搜索得到那么说明是使用的ik_max_word，搜索不到就是ik_smart。
    那么我们是搜索不到的，
    - 所以说："search_analyzer": "name_ik_smart" 的配置是生效的，使用的ik_smart去进行搜索关键字的分词。



"must": [{
	"multi_match": {
		"query": "实习陈云彬",
		"type": "cross_fields",
		"fields": ["name", "name.ascii_word", "name.ik_smart", "user_name.non_ascii_keyword", "creator_user_name.non_ascii_keyword", "description", "description.ik_smart", "tag", "text", "text.ik_smart"],
		"slop": 3
	}
}]

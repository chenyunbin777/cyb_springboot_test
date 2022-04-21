# mapping配置的理解
- 官方文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/multi-fields.html

## enabled
- 例一
- 该enabled设置只能应用于顶级映射定义和object字段，导致 Elasticsearch 完全跳过对字段内容的解析
- object类型数据保存时，存不到ES当中，但是其他类型的数据是可以保存的

```
PUT my-index-000001
{
  "mappings": {
    "properties": {
      "user_id": {
        "type":  "keyword"
      },
      "last_updated": {
        "type": "date"
      },
      "session_data": { 
        "type": "object", //只能由于object类型
        "enabled": false
      }
    }
  }
}

PUT my-index-000001/_doc/session_1
{
  "user_id": "kimchy",
  "session_data": {   //object类型的数据会被完全忽略，不会存储到ES中
    "arbitrary_object": {
      "some_array": [ "foo", "bar", { "baz": 2 } ]
    }
  },
  "last_updated": "2015-12-06T18:20:22"
}

PUT my-index-000001/_doc/session_2
{
  "user_id": "jpountz",
  "session_data": "你好",  //处理object类型的其他数据都可以保存在ES中
  "last_updated": "2015-12-06T18:22:13"
}

```
- 例二
- 整个映射也可能被禁用，在这种情况下，文档存储在_source字段中，这意味着可以检索它，但它的任何内容都不会以任何方式被索引
- 整个映射被禁用。
- 可以检索文档。
- 检查映射显示没有添加任何字段。

```
PUT my-index-000001
{
  "mappings": {
    "enabled": false //整个映射被禁用。
  }
}

//可以添加数据
PUT my-index-000001/_doc/session_1
{
  "user_id": "kimchy",
  "session_data": {
    "arbitrary_object": {
      "some_array": [ "foo", "bar", { "baz": 2 } ]
    }
  },
  "last_updated": "2015-12-06T18:20:22"
}

GET my-index-000001/_doc/session_1   //可以检索文档，查出来对应的数据，数据是保存在_source字段中

{
	"_index": "my-index-000002",
	"_type": "_doc",
	"_id": "session_1",
	"_version": 1,
	"_seq_no": 0,
	"_primary_term": 1,
	"found": true,
	"_source": {
		"user_id": "kimchy",
		"session_data": {
			"arbitrary_object": {
				"some_array": [
					"foo",
					"bar",
					{
						"baz": 2
					}
				]
			}
		},
		"last_updated": "2015-12-06T18:20:22"
	}
}


GET my-index-000001/_mapping   //检查映射显示没有添加任何字段。因为全部字段被禁用

```


JSON 仍然可以从该_source字段中检索，但不可搜索或以任何其他方式存储
- 请注意，由于 Elasticsearch 完全跳过解析字段内容，因此可以**将非对象数据添加到禁用的字段**

```
PUT my-index-000001
{
  "mappings": {
    "properties": {
      "session_data": {
        "type": "object", //object类型，json类型的数据
        "enabled": false  //定义为false
      }
    }
  }
}

PUT my-index-000001/_doc/session_1
{
  "session_data": "foo bar"   //就算是添加的数据不是object类型的也是可以添加成功的
}


```

## search_analyzer
- 默认情况下，查询将使用analyzer字段映射中定义的分析器，但这可以被search_analyzer设置覆盖

## analyzer
- 只有text字段支持analyzer映射参数
- 如果只是指定一个analyzer属性，那么 索引indexing和搜索 都使用analyzer来进行分词。

## search_quote_analyzer
- https://www.elastic.co/guide/en/elasticsearch/reference/current/analyzer.html#search-quote-analyzer
```
PUT my-index-000001
{
   "settings":{
      "analysis":{
         "analyzer":{
            "my_analyzer":{ //my_analyzer标记包括停用词在内的所有术语的分析器
               "type":"custom",
               "tokenizer":"standard",
               "filter":[
                  "lowercase"
               ]
            },
            "my_stop_analyzer":{ //删除停用词的分析器
               "type":"custom",
               "tokenizer":"standard",
               "filter":[
                  "lowercase",
                  "english_stop"
               ]
            }
         },
         "filter":{
            "english_stop":{
               "type":"stop",
               "stopwords":"_english_"
            }
         }
      }
   },
   "mappings":{
       "properties":{
          "title": {
             "type":"text",
             "analyzer":"my_analyzer", 
             "search_analyzer":"my_stop_analyzer",  //这个会覆盖analyzer使用去除停用词的分析器
             "search_quote_analyzer":"my_analyzer" //指向my_analyzer分析器并确保不从短语查询中删除停用词，特就是分词的时候不会删除停用词
         }
      }
   }
}

PUT my-index-000001/_doc/1
{
   "title":"The Quick Brown Fox"
}

PUT my-index-000001/_doc/2
{
   "title":"A Quick Brown Fox"
}

GET my-index-000001/_search
{
   "query":{
      "query_string":{
         "query":"\"the quick brown fox\"" 
      }
   }
}

```

- 由于查询包含在引号中，因此它被检测为短语查询，因此search_quote_analyzer启动并确保不从查询中删除停用词。


## coerce
- https://www.elastic.co/guide/en/elasticsearch/reference/current/coerce.html#coerce
- 强迫
- 数据并不总是干净的。根据生成方式，数字可能会在 JSON 正文中呈现为真正的 JSON 数字，例如5，但也可能呈现为字符串，例如"5"。
或者，应该是整数的数字可能会被呈现为浮点，例如5.0，甚至 "5.0"。强制尝试清理脏值以适应字段的数据类型。例如：
    - 字符串将被强制转换为数字。
    - 浮点数将被截断为整数值。
    
```
PUT my-index-000001
{
  "mappings": {
    "properties": {
      "number_one": {
        "type": "integer" //指定了字段的类型，那么就可以通过强制转换将字符串或者浮点数转换成integer
      },
      "number_two": {
        "type": "integer",
        "coerce": false  //被禁用强制转换
      }
    }
  }
}

PUT my-index-000001/_doc/1
{
  "number_one": "10" 
}

PUT my-index-000001/_doc/2
{
  "number_two": "10"   //会报错，因为已经禁用了强制转换功能
}
```  


```
{
  "settings": {
    "index.mapping.coerce": false //还可以设置全局禁用强制转换，这样只要不指定"coerce": true 那么就不支持，插入数据的时候会报错
  },
  "mappings": {
    "properties": {
      "number_one": {
        "type": "integer",
        "coerce": true
      },
      "number_two": {
        "type": "integer"
      }
    }
  }
}
```

  
    

## 多fields情况

- 可以看到，我的es索引在定义的时候，city这个text类型里面还有一个fields的属性，这里涉及到多fileds的概念，名字叫raw，类型是keyword，keyword就是为了在做聚合 排序、精准匹配的时候使用的，keyword不会被分词，直接使用原始的文档去做匹配。
官方对多fileds的文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/multi-fields.html，
其实就是对一个字段做出多种分词处理，keyword即不分词，所以使用term的时候，请多注意一下，需要精准匹配原文档，请带上keyword类型的字段去查询，模糊查询就用match。
    - 原文链接：https://blog.csdn.net/cainiao1412/article/details/120798754

```
{
  "properties": {
    中定义各个字段的类型，属性，分析器等

  }
}

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

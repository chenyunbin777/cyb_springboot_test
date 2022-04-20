# multi_match的使用方法
- 参考文档：
    - https://www.knowledgedict.com/tutorial/elasticsearch-query-multi-match.html
    - Full text queries 的 match_phrase query 和match_phrase_prefix query ：https://zhuanlan.zhihu.com/p/142641300
    - https://wenku.baidu.com/view/569eee07bfd126fff705cc1755270722192e5967.html
    - https://www.knowledgedict.com/tutorial/elasticsearch-query-multi-match.html
    
## match_phrase的使用方法
## multi_match 的type类型
- **例子：搜索关键字：实习生工资。通过ik_smart 分词器可以分为 1 实习生（key1），2 工资（key2）两个分词。** 

- **一般的ES的分词原则**： 
    - 1 建立索引使用ik_max_word来分词，这样索引建立的更全面
    - 2 搜索使用ik_smart分词，这个搜索的更精确。 通过ik_max_word 分词器可以分为 实习生（position 0），实习（position 1），生（position 2）工资（position 3）四个。

- 1 best_fields：默认，查询与任何字段匹配的文档，也就是说只要能匹配查询关键词的分词中的任何一个字段，就可以被查出。 匹配的分词越多
那么**评分_score 字段就越大，查询的顺序就越靠前**。
    - 如：只要是查询的es￿字段的分词中包含1 2中任何一个都可以被查询出来，也就是等同于sql语句。 select * from table where key1 in(es关键字分词) or 'key2' in(es关键字分词)

- 2 most_fields：如果我们希望越多字段匹配的文档评分越高，就使用most_fields。 如果匹配的一样多，那么就根据分词的多少定义_score值的大小，分词越多越靠后。
    例子：如下三个关键字，那么就是按照 1 2 3的顺序来排序
    1 ”实习生工资“分为：实习生（position 0），实习（position 1），生（position 2）工资（position 3）
    2 ”实习生工资套“分为：实习生（position 0），实习（position 1），生（position 2），工资（position 3），套（position 4）
    3 ”实习生工资标准“分为：实习生（position 0），实习（position 1），生（position 2），工资标准（position 3），工资（position 4），标准（position 5）


- 3 cross_fields:以词条为中心，如果我们希望搜索的关键词的所有分词Tokens List 是分布在不同的ES 字段fields的情况下，我们可以使用这个方式。
    - 如：实习陈云彬， 实习 陈 云彬 三个词条 可以分布在name，creator_user_name，description 等等fields字段中几个字段中。
    - cross_fields能解决什么问题？
    - 有一类实体存储，比如人名，地址等，不是存储在单一的field里边，人名可以分为first_name，last_name。
     地址有street, country, city等字段组成。这类实体查询的特殊性在于，每一个single query string都是跨越了多个字段（请仔细理解这句话）。

```
"multi_match": {
	"query": "实习陈云彬",  将其分词之后的所有分词字段Token，可以分布在fields指定的子弹中。  
	"type": "cross_fields",
	"fields": ["name", "user_name.non_ascii_keyword", "creator_user_name.non_ascii_keyword", "description", "description.ik_smart", "tag", "text", "text.ik_smart"],
}

```


- **4 phrase，也就是match_phrase搜索**
    - match_phrase query 首先会把 query 内容分词，分词器可以自定义，同时文档还要满足以下两个条件才会被搜索到：
        - 1 分词后所有词项都要出现在该字段中（相当于 and 操作）。
        - 2 字段中的词项顺序要一致。
        - 3 设置slop参数，slop参数允许查询结果中出现指定间隔单词个数的内容

    - 根据下面的搜索条件
    - 1 name.ik_smart：
      （1） ”实习生工资“分为：实习生（position 0），工资（position 1）。   这个按照实习生 工资的顺序可以匹配
      （2） ”实习生工资套“分为：实习生（position 0），工资（position 1），套（position 2）。   这个按照实习生 工资的顺序可以匹配
      （3）”实习生工资标准“分为：实习生（position 0），工资标准（position 1）。   这个按照实习生 工资的顺序不可以匹配，因为不存在 "工资" 的分词
      所有说使用match_phrase 搜索”实习生工资“只能搜索出1 2  ，3无法全部匹配关键词 ”实习生“和”工资“。
      
    - 2 name 默认使用ik_max_word 分词
     （1）”实习生工资“分为：实习生（position 0），实习（position 1），生（position 2）工资（position 3）
     （2）”实习生工资套“分为：实习生（position 0），实习（position 1），生（position 2），工资（position 3），套（position 4）
     （3）”实习生工资标准“分为：实习生（position 0），实习（position 1），生（position 2），工资标准（position 3），工资（position 4），标准（position 5）
      结论：
     （1）（2）按照分词的顺序，"工资"需要移动到 "实习"的位置才可以匹配的上，所以需要移动2次才可以，所以说可以设置 "slop":2 来使其进行匹配。
     （3）类似于需要移动3次才可以进行匹配，如果需要就设置 "slop":3 

- 5 phrase_prefix：与match_phrase查询类似，但是会对 **最后一个Token（一个Token就是一个分词）** 在倒排序索引列表中进行通配符搜索。Token的模糊匹配数控制：max_expansions 默认值为50。
    - 注意："max_expansions"的值最小为1，哪怕你设置为0，依然会 + 1个通配符匹配；所以，尽量不要用该语句，因为，最后一个Token始终要去扫描大量的索引，性能可能会很差。
    - 等价于sql：【where Token = 实习 or Token like “实习_”】；
```
"multi_match": {
	"query": "实习",
	"type": "phrase_prefix",
    "max_expansions": 1  默认是50，最小值是1，表示搜索关键词后边最多扩展多少个单词
```



```
GET /new_item/_search
{
	"query": {
		"bool": {
			"filter": {
				"bool": {
					"should": [{
						"terms": {
							"folder_id": [398000000094, 398000000109, 398000000112, 398000000115, 398000000116, 398000000117, 398000000124, 398000000125, 398000000126, 398000000169, 398000000170, 398000000208, 398000000236, 398000000250, 398000000251, 398000000252, 398000000253, 398000000255, 398000000269, 398000000270, 398000000271, 398000000272, 398000000274, 398000000288, 398000000289, 398000000290, 398000000291, 398000000293, 398000000307, 398000000308, 398000000309, 398000000310, 398000000312, 398000000326, 398000000327, 398000000328, 398000000329, 398000000370, 398000000384, 398000000385, 398000000386, 398000000387, 398000000389, 398000000403, 398000000404, 398000000405, 398000000406, 398000000408, 398000000411, 398000000425, 398000000426, 398000000427, 398000000428]
						}
					}, {
						"term": {
							"user_id": 1546596
						}
					}]
				}
			},
			"must": [{  //一个关键词匹配多个字段查询
				"multi_match": {
					"query": "实习生工资",
					"type": "phrase",
					"fields": ["name", "name.ik_smart", "user_name.non_ascii_keyword", "creator_user_name.non_ascii_keyword", "description", "description.ik_smart", "tag", "text", "text.ik_smart"],
                    "slap": 3
				}
			}]
		}
	}


```


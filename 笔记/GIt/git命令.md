# git pull的使用
其中git pull有这几项常见的选项搭配：

1 不带任何选项的git pull命令：先尝试快进合并，如果不行再进行正常合并生成一个新的提交。
这种情况的分支就会非常得混乱

2 git pull --ff-only命令：只尝试快进合并，如果不行则终止当前合并操作。 
快速合并还是比较不错的
3 git pull --no-ff命令：禁止快进合并，即不管能不能快进合并，最后都会进行正常合并生成一个新的提交。
一般不推荐这种
4 git pull --rebase命令：先尝试快进合并，如果不行再进行变基合并。
这种情况是比较推荐的

————————————————
原文链接：https://blog.csdn.net/wq6ylg08/article/details/114106272
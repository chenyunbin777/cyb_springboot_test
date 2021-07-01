1 解决git status中文乱码
- 原因
  在默认设置下，中文文件名在工作区状态输出，中文名不能正确显示，而是显示为八进制的字符编码。
- 解决办法
  将git 配置文件 core.quotepath项设置为false。
  quotepath表示引用路径
  加上--global表示全局配置
- git bash 终端输入命令：
    - git config --global core.quotepath false
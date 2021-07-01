# LBS--Location Based Service
- 基于位置的服务（Location Based Services，LBS），是利用各类型的定位技术来获取定位设备当前的所在位置，
通过移动互联网向定位设备提供信息资源和基础服务。

## 基于Redis实现的LBS
- Redis GEO 主要用于存储地理位置信息，并对存储的信息进行操作，该功能在 Redis 3.2 版本新增。
- Geo
    - Redis GEO 主要用于存储地理位置信息，并对存储的信息进行操作，**该功能在 Redis 3.2 版本新增。**
    - geoadd：添加地理位置的坐标。      
    - geopos：获取地理位置的坐标。
    - geodist：计算两个位置之间的距离。
    - georadius：根据用户给定的经纬度坐标来获取指定范围内的地理位置集合。
    - georadiusbymember：根据储存在位置集合里面的某个地点获取指定范围内的地理位置集合。
    - geohash：返回一个或多个位置对象的 geohash 值。
    - 命令：GEOADD key longitude latitude member [longitude latitude member ...]
       - 例子：
        - GEOADD position 116.20 39.56 beijing 120.2 30.3 hangzhou  添加北京，杭州经纬度
        - GEOPOS position beijing hangzhou  北京杭州经纬度
          1) 1) "116.19999736547470093"
             2) "39.56000019952067248"
          2) 1) "120.20000249147415161"
             2) "30.29999970751173777"
        - GEODIST position beijing hangzhou km   北京杭州之间的距离(单位：km，这个是可选的)
            "1092.3022"
        - GEORADIUS position 120 35 1000 km withcoord  **在某经纬度，半径在1000km范围内有哪些地点**
           1) 1) "beijing"
              2) 1) "116.19999736547470093"
                 2) "39.56000019952067248"
           2) 1) "hangzhou"
              2) 1) "120.20000249147415161"
                 2) "30.29999970751173777"
        - GEOHASH position beijing hangzhou  求某些地理位置的hash值
        - GEORADIUSBYMEMBER position beijing 1000 km / 2000 km  以北京为中心点 半径1000km/2000km范围内都有哪些城市
            "beijing"                                 1) "hangzhou"
            
            
            
# Mongodb也可以实现LBS服务

- https://blog.csdn.net/QcloudCommunity/article/details/87883630
- 上面我们讲到Mongodb使用平面四叉树的方式计算Geohash。事实上，平面四叉树仅存在于运算的过程中，在实际存储中并不会被使用到。
- 插入 对于一个经纬度坐标[x,y]，MongoDb计算出该坐标在2d平面内的grid编号，该编号为是一个52bit的int64类型，该类型被用作btree的key，因此实际数据是按照 {GeoHashId->RecordValue}的方式被插入到btree中的。
- 查询 对于geo2D索引的查询，常用的有geoNear和geoWithin两种。geoNear查找距离某个点最近的N个点的坐标并返回，该需求可以说是构成了LBS服务的基础（陌陌，滴滴，摩拜），geoWithin是查询一个多边形内的所有点并返回。我们着重介绍使用最广泛的geoNear查询。
- 1 geoNear的查询过程,查询语句如下
geoNear可以理解为一个从起始点开始的不断向外扩散的环形搜索过程。
如下图所示： 由于圆自身的性质，外环的任意点到圆心的距离一定大于内环任意点到圆心的距离，
所以以圆 环进行扩张迭代的好处是： 
    - 1）减少需要排序比较的点的个数 
    - 2）能够尽早发现满足条件的点从而返回，避免不必要的搜索 MongoDB在实现的细节中，
    如果内环搜索到的点数过少，圆环每次扩张的步长会倍增
db.runCommand(
{
geoNear: "places", //table Name
near: [ -73.9667, 40.78 ] , // central point 中心点的位置
spherical: true, // treat the index as a spherical index，是否采用球面几何计算
query: { category: "public" } // filters， 查询条件
maxDistance: 0.0001531 // distance in about one kilometer，最远距离
}

- MongoDB LBS服务遇到的问题
    - 
- 为何扫描集如此大？
    - 上面我们说过，MongoDB搜索距离最近的点的过程是一个环形扩张的过程，如果内环满足条件的点不够多，每次的扩张半径都会倍增。
    因此在遇到内环点稀少，外环有密集点的场景时，容易陷入BadCase。
# 为什么当桶中键值对数量大于8才转换成红黑树，数量小于6才转换成链表？

- 1 HashMap在JDK1.8及以后的版本中引入了红黑树结构，若桶中链表元素个数大于等于8时，链表转换成树结构；
若桶中链表元素个数小于等于6时，树结构还原成链表。因为红黑树的平均查找长度是log(n)，长度为8的时候，
平均查找长度为3，如果继续使用链表，平均查找长度为8/2=4，这才有转换为树的必要。链表长度如果是小于等于6，6/2=3，
虽然速度也很快的，但是转化为树结构和生成树的时间并不会太短。

- 2 还有选择6和8，中间有个差值7可以有效防止链表和树频繁转换。假设一下，如果设计成链表个数超过8则链表转换成树结构，
链表个数小于8则树结构转换成链表，如果一个HashMap不停的插入、删除元素，链表个数在8左右徘徊，就会频繁的发生树转链表、
链表转树，效率会很低。


- 3 线程不安全
    - 1.7 ：resize是使用的头插法可能会造成循环链表的情况
    - 1.8 ：resize是使用的尾插发不会造成循环链表，但是也不是线程安全的


`
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
            // 2 数组桶位置为null，新建一个节点
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {  //3 如果计算的key的位置(n - 1) & hash 不为空
            Node<K,V> e; K k;
            //  4 判断当前位置存在元素，（hash相同 && key的地址相同） ||  key.equals(k)， 在Node中重写了equals方法。
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
                // 5 如果是树形结构就去执行树形结构的put
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                // 6 这里是变量桶节点的链表，已经说明该桶节点是存在元素的。
                for (int binCount = 0; ; ++binCount) {
                    // 7 如果遍历到链表的结尾之后，将key value插入链表
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        // 8 如果链表的长度大于等于8 就将链表扩展为红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    
                    // 8 如果在链表中找到了相同的key值的话，就替换掉，此时的e就是要替换掉的node节点
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // 9 替换掉oldValue值
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                //存在key对应的value，返回oldValue
                return oldValue;
            }
        }
        // 10 记录HashMap的结构修改次数
        ++modCount;
        // 11  threshold == capacity * load factor, 如果到了容量的阈值，需要进行扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        // 12 不存在key对应的value，返回null
        return null;
    }
`

# ConcurrentHashMap

# 1.7
ConcurrentHashMap初始化时，计算出Segment数组的大小ssize和每个Segment中HashEntry数组的大小cap，并初始化Segment数组的第一个元素；其中ssize大小为2的幂次方，默认为16，cap大小也是2的幂次方，最小值为2，最终结果根据根据初始化容量initialCapacity进行计算，其中Segment在实现上继承了ReentrantLock，这样就自带了锁的功能。
当执行put方法插入数据时，根据key的hash值，在Segment数组中找到相应的位置，如果相应位置的Segment还未初始化，则通过CAS进行赋值，接着执行Segment对象的put方法通过加锁机制插入数据。


- size实现
先采用不加锁的方式，连续计算元素的个数，最多计算3次：
1、如果前后两次计算结果相同，则说明计算出来的元素个数是准确的；
2、如果前后两次计算结果都不同，则给每个 Segment 进行加锁，再计算一次元素的个数；

## 1.8
- 1 put
0. transient volatile Node<K,V>[] table 如果table是空的我们就初始化table
1、如果相应位置的Node还未初始化，则通过CAS插入相应的数据；
2、如果相应位置的Node不为空，且当前该节点不处于移动状态，则对该节点加synchronized锁，如果该节点的hash不小于0，则遍历链表更新节点或插入新节点；
3、如果该节点是TreeBin类型的节点，说明是红黑树结构，则通过putTreeVal方法往红黑树中插入节点；
4、如果binCount不为0，说明put操作对数据产生了影响，如果当前链表的个数达到8个，则通过treeifyBin方法转化为红黑树，
如果oldVal不为空，说明是一次更新操作，没有对元素个数产生影响，则直接返回旧值
5、如果插入的是一个新节点，则执行addCount()方法尝试更新元素个数baseCount；

- 2 size实现
1.8中使用一个volatile类型的变量baseCount记录元素的个数，当插入新数据或则删除数据时，会通过addCount()方法更新baseCount
1、初始化时counterCells为空，在并发量很高时，如果存在两个线程同时执行CAS修改baseCount值，则失败的线程会继续执行方法体中的逻辑，使用CounterCell记录元素个数的变化；
2、如果CounterCell数组counterCells为空，调用fullAddCount()方法进行初始化，并插入对应的记录数，通过CAS设置cellsBusy字段，只有设置成功的线程才能初始化CounterCell数组




   https://www.cnblogs.com/apprentice89/p/3234677.html
    select/poll把fd的监听列表放在用户空间，由用户空间管理，导致在用户空间和内核空间之间频繁重复拷贝大量fd；epoll在内核建立fd监听列表（实际是红黑树），每次通过epoll_ctl增删改即可。
    select/poll每当有fd内核事件时，都唤醒当前进程，然后遍历监听列表全部fd，检查所有就绪fd并返回；epoll在有fd内核事件时，通过回调把该fd放到就绪队列中，只需返回该就绪队列即可，不需要每次遍历全部监听fd。
    int epoll_create(int size);
    int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event);
    int epoll_wait(int epfd, struct epoll_event *events,int maxevents, int timeout);
    使用起来很清晰，首先要调用epoll_create建立一个epoll fd。参数size是内核保证能够正确处理的最大文件描述符数目（现在内核使用红黑树组织epoll相关数据结构，不再使用这个参数）。
    epoll_ctl可以操作上面建立的epoll fd，例如，将刚建立的socket fd加入到epoll中让其监控，或者把 epoll正在监控的某个socket fd移出epoll，不再监控它等等。
    epoll_wait在调用时，在给定的timeout时间内，当在监控的这些文件描述符中的某些文件描述符上有事件发生时，就返回用户态的进程。
    - 1.执行epoll_create时，创建了红黑树和就绪list链表。
    - 2.执行epoll_ctl时，如果增加fd（socket），则检查在红黑树中是否存在，存在立即返回，不存在则添加到红黑树上，
    然后向内核注册回调函数，用于当中断事件来临时向准备就绪list链表中插入数据。
    - 2. 第二种说发放：每个epoll对象都有一个独立的eventpoll结构体，通过eventpoll管理存放epoll_ctl添加的事件集合，
    这些事件以epitem为结点挂载到红黑树上。添加到epoll中的事件，都会与设备驱动建立回调关系，
    当相应事件发生时该回调将事件对应的epitem结点加入rdlist即可；
    
    - 3.当用户调用epoll_wait是指上内核只检查了rdlist是否为空，若非空将其拷贝到用户态并返回触发事件数量。
    - **可以说：红黑树+rdlist+回调铸就了epoll的高效。**
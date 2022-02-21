# Kubernetes核心技术-Controller
# 内容
# 一 什么是Controller
- Controller是在集群上管理和运行容器的对象，Controller是实际存在的，Pod是虚拟机的
# 二 Pod和Controller的关系
- Pod是通过Controller实现应用的运维，比如弹性伸缩，滚动升级等
- Pod 和 Controller之间是通过**label标签**来建立关系，同时Controller又被称为控制器工作负载

# 三 滚动升级
- 滚动升级的精髓在于升级过程中依然能够保持服务的连续性，使外界对于升级的过程是无感知的。
    - 整个过程中会有三个状态，全部旧实例，新旧实例皆有，全部新实例。
    - 旧实例个数逐渐减少，新实例个数逐渐增加，最终达到旧实例个数为0，新实例个数达到理想的目标值。
- 滚动升级是一种平滑过渡式的升级，在升级过程中，服务仍然可用。
这是kubernetes作为应用服务化管理的关键一步。服务无处不在，并且按需使用。
这是云计算的初衷，对于PaaS平台来说，应用抽象成服务，遍布整个集群，为应用提供随时随地可用的服务是PaaS的终极使命。

- 总结：可以理解滚动升级类似于负载均衡里的平滑升级，将升级之后的机器一点一点加入到集群当中进行负载工作。 但是在k8s中是使用
ReplicaSet来管理pod实例，上层在使用Deployment控制ReplicaSet来实现的对于pod的控制的

**？？？之后再慢慢学习pod的Deployment部署**
# Deployment控制器应用场景
# yaml文件字段说明
# Deployment控制器部署应用
# 升级回滚
# 弹性伸缩
- 升级： 假设从版本为1.14 升级到 1.15 ，这就叫应用的升级【升级可以保证服务不中断】
- 回滚：从版本1.15 变成 1.14，这就叫应用的回滚
- 弹性伸缩：我们根据不同的业务场景，来改变Pod的数量对外提供服务，这就是弹性伸缩


# Kubernetes控制器Controller详解

## 有状态应用Statefulset部署
- Statefulset主要是用来部署有状态应用
- 对于StatefulSet中的Pod，每个Pod挂载自己独立的存储，如果一个Pod出现故障，从其他节点启动一个同样名字的Pod，
要挂载上原来Pod的存储继续以它的状态提供服务。
- 重点：pod独立存储，自动其他node故障恢复，恢复之前的存储。

# 有状态因应用
- 让每个Pod独立的
- 让每个Pod独立的，保持Pod启动顺序和唯一性
- 唯一的网络标识符，持久存储
- 有序，比如mysql中的主从

- 适合StatefulSet的业务包括数据库服务MySQL 和 PostgreSQL，集群化管理服务Zookeeper、etcd等有状态服务
- StatefulSet的另一种典型应用场景是作为一种比普通容器更稳定可靠的模拟虚拟机的机制。
传统的虚拟机正是一种有状态的宠物，运维人员需要不断地维护它，容器刚开始流行时，我们用容器来模拟虚拟机使用，
所有状态都保存在容器里，而这已被证明是非常不安全、不可靠的。

- 使用StatefulSet，Pod仍然可以通过漂移到不同节点提供高可用，而存储也可以通过外挂的存储来提供高可靠性，
StatefulSet做的只是将确定的Pod与确定的存储关联起来保证状态的连续性。
    - 漂移到不同节点提供高可用
    - 确定pod与存储的关系

zk pod的配置文件
```
ownerReferences:
    - apiVersion: apps/v1
      kind: StatefulSet
      name: zk
      uid: 5030d38b-0152-11ea-876d-92f49ca8b482
      controller: true
      blockOwnerDeletion: true
```

## 无状态应用deployment部署
- 我们原来使用 deployment，部署的都是无状态的应用，那什么是无状态应用？
    - 认为Pod都是一样的
    - 没有顺序要求
    - 不考虑应用在哪个node上运行
    - 能够进行随意伸缩和扩展
    
# 部署有状态应用
- 无头service， ClusterIp：none
  
- 这里就需要使用 StatefulSet部署有状态应用  
- 这里有状态的约定，肯定不是简简单单通过名称来进行约定，而是更加复杂的操作
    deployment：是有身份的，有唯一标识
    statefulset：根据主机名 + 按照一定规则生成域名
- 每个pod有唯一的主机名，并且有唯一的域名
    格式：主机名称.service名称.名称空间.svc.cluster.local
    举例：nginx-statefulset-0.default.svc.cluster.local


# DaemonSet   
- DaemonSet 即后台支撑型服务，主要是用来部署守护进程.在每个节点上支撑K8S集群运行的服务。
                                 
- DaemonSet中的teleport-teleport作为守护进程,每个节点都会有一个这个服务，主要作用是作为服务之间的调用的校验。



# Job和CronJob
- 一次性任务Job：一次性执行完就结束
- 定时任务CronJob：周期性执行。使用cron表达式来执行定期任务。
    - spec.schedule标签来定义cron表达式

```
apiVersion: batch/v1
kind: Job
metadata:
  name: wpsedit-1634700000
  namespace: test
  selfLink: /apis/batch/v1/namespaces/test/jobs/wpsedit-1634700000
  uid: 94338857-a6a7-44f9-9a76-c39deb506d46
  resourceVersion: '4066722788'
  creationTimestamp: '2021-10-20T03:20:07Z'
  labels:
    controller-uid: 94338857-a6a7-44f9-9a76-c39deb506d46
    job-name: wpsedit-1634700000
  ownerReferences:
    - apiVersion: batch/v1beta1
      kind: CronJob
      name: wpsedit
      uid: 951792ed-e745-4dfa-a5b1-2f764fafb03a
      controller: true
      blockOwnerDeletion: true
status:
  conditions:
    - type: Complete
      status: 'True'
      lastProbeTime: '2021-10-20T03:20:09Z'
      lastTransitionTime: '2021-10-20T03:20:09Z'
  startTime: '2021-10-20T03:20:07Z'
  completionTime: '2021-10-20T03:20:09Z'
  succeeded: 1
spec:
  parallelism: 1
  completions: 1
  backoffLimit: 6
  selector:
    matchLabels:
      controller-uid: 94338857-a6a7-44f9-9a76-c39deb506d46
  template:
    metadata:
      creationTimestamp: null
      labels:
        controller-uid: 94338857-a6a7-44f9-9a76-c39deb506d46
        job-name: wpsedit-1634700000
    spec:
      containers:
        - name: wpsedit-cronjob
          image: 'registry.cn-hangzhou.aliyuncs.com/yifangyun-library/wpsedit:master'
          args:   这里应该是真的的一次性任务的命令
            - /bin/sh
            - '-c'
            - >-
              curl -X POST
              'http://wpsedit-svc/wpsedit/scheduler/release_unused_lock?biz_type=fang&time_elapsed=300'
          resources: {}
          terminationMessagePath: /dev/termination-log
          terminationMessagePolicy: File
          imagePullPolicy: Always
      restartPolicy: OnFailure
      terminationGracePeriodSeconds: 30
      dnsPolicy: ClusterFirst
      securityContext: {}
      imagePullSecrets:
        - name: registry-secret
      schedulerName: default-scheduler



```

```
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: wpsedit
  namespace: test
  selfLink: /apis/batch/v1beta1/namespaces/test/cronjobs/wpsedit
  uid: 951792ed-e745-4dfa-a5b1-2f764fafb03a
  resourceVersion: '4066776075'
  creationTimestamp: '2021-09-30T06:38:11Z'
  labels:
    app.kubernetes.io/managed-by: Helm
  annotations:
    meta.helm.sh/release-name: wpsedit
    meta.helm.sh/release-namespace: test
status:
  lastScheduleTime: '2021-10-20T03:40:00Z'
spec:
  schedule: '*/10 * * * *'    cron表达式
  concurrencyPolicy: Allow
  suspend: false
  jobTemplate:
    metadata:
      creationTimestamp: null
    spec:
      template:
        metadata:
          creationTimestamp: null
        spec:
          containers:
            - name: wpsedit-cronjob
              image: >-
                registry.cn-hangzhou.aliyuncs.com/yifangyun-library/wpsedit:master
              args:
                - /bin/sh
                - '-c'
                - >-
                  curl -X POST
                  'http://wpsedit-svc/wpsedit/scheduler/release_unused_lock?biz_type=fang&time_elapsed=300'
              resources: {}
              terminationMessagePath: /dev/termination-log
              terminationMessagePolicy: File
              imagePullPolicy: Always
          restartPolicy: OnFailure
          terminationGracePeriodSeconds: 30
          dnsPolicy: ClusterFirst
          securityContext: {}
          imagePullSecrets:
            - name: registry-secret
          schedulerName: default-scheduler
  successfulJobsHistoryLimit: 3
  failedJobsHistoryLimit: 1


```

# 删除svc 和 statefulset

- 使用下面命令，可以删除我们添加的svc 和 statefulset
    - kubectl delete svc web
    - kubectl delete statefulset --all
 
 
# Replication Controller
Replication Controller 简称 RC，是K8S中的复制控制器。RC是K8S集群中最早的保证Pod高可用的API对象。
通过监控运行中的Pod来保证集群中运行指定数目的Pod副本。指定的数目可以是多个也可以是1个；
少于指定数目，RC就会启动新的Pod副本；
多于指定数目，RC就会杀死多余的Pod副本。

即使在指定数目为1的情况下，通过RC运行Pod也比直接运行Pod更明智，因为RC也可以发挥它高可用的能力，保证永远有一个Pod在运行。
RC是K8S中较早期的技术概念，只适用于长期伺服型的业务类型，比如控制Pod提供高可用的Web服务。

# Replica Set
Replica Set 检查 RS，也就是副本集。RS是新一代的RC，提供同样**高可用能力**，区别主要在于RS后来居上，能够支持更多种类的匹配模式。
**副本集对象一般不单独使用，而是作为Deployment的理想状态参数来使用**
# 一 Pod基本概念
- 最小部署的单元
- Pod里面是由一个或多个容器组成【一组容器的集合】
- 一个pod中的容器是共享网络命名空间
- Pod是短暂的
- 每个Pod包含一个或多个紧密相关的用户业务容器

# 二 Pod存在的意义
- 创建容器使用docker，一个docker对应一个容器，一个容器运行一个应用进程
- Pod是**多进程设计**，运用多个应用程序，也就是一个Pod里面有多个容器，而一个容器里面运行一个应用程序
- Pod是在K8S集群中运行部署应用或服务的最小单元，它是可以支持多容器的。
# 以上总结 
##个人理解：
- 也就是说一个pod中就类似于一个真实的服务机，可以部署多个服务，也就是多个容器container（docker容器化服务）。
- 比如：transformer服务中就会部署两个container：1 transformer 和 2 cronjob服务。
    - 在Deployment下的yaml文件中的配置中的containers标签可以看到pod中的包含的containers都有什么，
    也可以看到对饮给的镜像到底是拉取的哪个。
```
这里包含2个containers可以一目了然和对应的镜像地址
containers:
        - name: transformer
          image: >-
            registry.cn-hangzhou.aliyuncs.com/yifangyun-library/transformer:bimface 

        - name: cronjob
          image: >-
            registry.cn-hangzhou.aliyuncs.com/yifangyun-library/transformer:P_2.0.1_beta1
    
```  
- 每一个pod有所属的节点node 



# 三 Pod实现机制
## 共享网络
- 容器本身之间相互隔离的，一般是通过 namespace 和 group 进行隔离，那么Pod里面的容器如何实现通信？
    - 首先需要满足前提条件，也就是容器都在同一个namespace之间，如test
    - 通过 Pause 容器，把其它业务容器加入到Pause容器里，让所有业务容器在同一个名称空间中，可以实现网络共享
        - 关于Pod实现原理，首先会在Pod会创建一个根容器： pause容器，然后我们在创建业务容器 【nginx，redis 等】，
        在我们创建业务容器的时候，会把它添加到 info容器 中
        - 而在 info容器 中会独立出 ip地址，mac地址，port 等信息，然后实现网络的共享
## 共享存储
- 使用 Volumn数据卷进行共享存储
- 如下就是数据卷的配置
```
spec:
      volumes:
        - name: transformer-application-config-volume
          configMap:
            name: transformer-helm-transformer-application-config
            defaultMode: 420
        - name: host-time
          hostPath:
            path: /etc/localtime
            type: ''
        - name: tmp
          emptyDir: {}
        - name: tmp-conversion
          emptyDir: {}
```
- 将pod的持久化数据挂载到mountPath下，如我们的transformer是挂载到/opt/transformer/properties下
```
volumeMounts:
            - name: transformer-application-config-volume
              mountPath: /opt/transformer/properties
            - name: tmp
              mountPath: /tmp
            - name: tmp-conversion
              mountPath: /var/tmp/conversion
```

# 四 Pod镜像拉取策略
- 我们以具体实例来说，拉取策略就是 imagePullPolicy，拉取策略主要分为了以下几种
    - IfNotPresent：默认值，镜像在宿主机上不存在才拉取
    - Always：每次创建Pod都会重新拉取一次镜像
    - Never：Pod永远不会主动拉取这个镜像


# 五 Pod资源限制
- 也就是我们Pod在进行调度的时候，可以对调度的资源进行限制，例如我们限制Pod调度是使用的资源是2CORE4G，
那么在调度对应的node节点时，只会占用对应的资源，对于不满足资源的节点，将不会进行调度。
如下：这里分了两个部分
    - request：表示调度所需的资源
    - limits：表示最大所占用的资源
```
          resources:
            limits:
              cpu: '1'
              memory: 8Gi
            requests:
              cpu: 200m
              memory: 512Mi
```

# 六 Pod重启机制
- 配置方式：restartPolicy: Always
- 重启策略主要分为以下三种

    - Always：当容器终止退出后，总是重启容器，默认策略 【nginx等，需要不断提供服务】
    - OnFailure：当容器异常退出（退出状态码非0）时，才重启容器。
    - Never：当容器终止退出，从不重启容器 【批量任务】

# 七 Pod健康检查
- kubectl get pod | grep pod name 使用命令来查看pod的生命周期状态
这个时候就可以使用应用层面的检查

- 存活检查，如果检查失败，将杀死容器，根据Pod的restartPolicy【重启策略】来操作
livenessProbe

- 就绪检查，如果检查失败，Kubernetes会把Pod从Service endpoints中剔除
readinessProbe

# 八 Pod调度策略
## Kubernetes K8S之固定节点nodeName和nodeSelector调度详解
- https://blog.csdn.net/woshizhangliang999/article/details/110354843
## nodeName调度
- nodeName是节点选择约束的最简单形式，但是由于其限制，通常很少使用它。nodeName是PodSpec的领域。
- pod.spec.nodeName将Pod直接调度到指定的Node节点上，会【跳过Scheduler的调度策略】，该匹配规则是【强制】匹配。
可以越过Taints污点进行调度。
```
要运行的yaml文件
apiVersion: apps/v1
kind: Deployment  
metadata:
  name: scheduler-nodename-deploy
  labels:
    app: nodename-deploy
spec:
  replicas: 5
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: myapp-pod
        image: registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1
        imagePullPolicy: IfNotPresent
        ports:
          - containerPort: 80
      # 指定节点运行
      nodeName: k8s-master

```
- 运行yaml文件并查看信息
```
[root@k8s-master scheduler]# kubectl apply -f scheduler_nodeName.yaml 
deployment.apps/scheduler-nodename-deploy created
[root@k8s-master scheduler]# 
[root@k8s-master scheduler]# kubectl get deploy -o wide
NAME                        READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS   IMAGES                                                      SELECTOR
scheduler-nodename-deploy   0/5     5            0           6s    myapp-pod    registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1   app=myapp
[root@k8s-master scheduler]# 
[root@k8s-master scheduler]# kubectl get rs -o wide
NAME                                  DESIRED   CURRENT   READY   AGE   CONTAINERS   IMAGES                                                      SELECTOR
scheduler-nodename-deploy-d5c9574bd   5         5         5       15s   myapp-pod    registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1   app=myapp,pod-template-hash=d5c9574bd
[root@k8s-master scheduler]# 
//这里看到所有 pod都被调度到了k8s-master节点,如果这里是nodeName: k8s-node02，那么就会直接调度到k8s-node02节点。
[root@k8s-master scheduler]# kubectl get pod -o wide
NAME                                        READY   STATUS    RESTARTS   AGE   IP             NODE         NOMINATED NODE   READINESS GATES
scheduler-nodename-deploy-d5c9574bd-6l9d8   1/1     Running   0          23s   10.244.0.123   k8s-master   <none>           <none>
scheduler-nodename-deploy-d5c9574bd-c82cc   1/1     Running   0          23s   10.244.0.119   k8s-master   <none>           <none>
scheduler-nodename-deploy-d5c9574bd-dkkjg   1/1     Running   0          23s   10.244.0.122   k8s-master   <none>           <none>
scheduler-nodename-deploy-d5c9574bd-hcn77   1/1     Running   0          23s   10.244.0.121   k8s-master   <none>           <none>
scheduler-nodename-deploy-d5c9574bd-zstjx   1/1     Running   0          23s   10.244.0.120   k8s-master   <none>           <none>

```
- 当nodeName指定节点不存在，则容器将不会运行，一直处于Pending 状态。

- 获取当前的节点信息
```
chenyunbin@chenyunbindeMacBook-Pro ~ % kubectl get node -o wide --show-labels
NAME                            STATUS   ROLES    AGE    VERSION            INTERNAL-IP    EXTERNAL-IP   OS-IMAGE                KERNEL-VERSION               CONTAINER-RUNTIME   LABELS
cn-hangzhou.172.16.0.157        Ready    <none>   713d   v1.16.9-aliyun.1   172.16.0.157   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,data=a,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.157,kubernetes.io/os=linux,node.kubernetes.io/service=teleport
cn-hangzhou.172.16.0.158        Ready    <none>   713d   v1.16.9-aliyun.1   172.16.0.158   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,data=b,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.158,kubernetes.io/os=linux
cn-hangzhou.172.16.0.159        Ready    <none>   712d   v1.16.9-aliyun.1   172.16.0.159   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.n2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.159,kubernetes.io/os=linux
cn-hangzhou.172.16.0.162        Ready    <none>   701d   v1.16.9-aliyun.1   172.16.0.162   47.99.47.98   CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.162,kubernetes.io/os=linux
cn-hangzhou.172.16.0.166        Ready    <none>   659d   v1.16.9-aliyun.1   172.16.0.166   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.166,kubernetes.io/os=linux
cn-hangzhou.172.16.0.173        Ready    <none>   440d   v1.16.9-aliyun.1   172.16.0.173   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.173,kubernetes.io/os=linux
cn-hangzhou.172.16.0.190        Ready    <none>   161d   v1.16.9-aliyun.1   172.16.0.190   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.190,kubernetes.io/os=linux,node.kubernetes.io/instance-type=ecs.sn2.large,topology.kubernetes.io/region=cn-hangzhou,topology.kubernetes.io/zone=cn-hangzhou-b
cn-hangzhou.172.16.0.218        Ready    <none>   40h    v1.16.9-aliyun.1   172.16.0.218   <none>        CentOS Linux 7 (Core)   3.10.0-957.21.3.el7.x86_64   docker://18.9.2     ack.aliyun.com=cb56a231ed46740ed917f1dbcf4789d97,alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,beta.kubernetes.io/arch=amd64,beta.kubernetes.io/instance-type=ecs.sn2.large,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=cn-hangzhou.172.16.0.218,kubernetes.io/os=linux,node.kubernetes.io/instance-type=ecs.sn2.large,topology.kubernetes.io/region=cn-hangzhou,topology.kubernetes.io/zone=cn-hangzhou-b
virtual-kubelet-cn-hangzhou-b   Ready    agent    159d   v1.16.9-aliyun.1   172.16.0.216   <none>        <unknown>               <unknown>                    <unknown>           alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,alpha.service-controller.kubernetes.io/exclude-balancer=true,beta.kubernetes.io/os=linux,failure-domain.beta.kubernetes.io/region=cn-hangzhou,failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,kubernetes.io/arch=amd64,kubernetes.io/hostname=virtual-kubelet-cn-hangzhou-b,kubernetes.io/os=linux,kubernetes.io/role=agent,service.beta.kubernetes.io/exclude-node=true,type=virtual-kubelet


labels信息：以"，"分割  /后的就是labels 如 arch=amd64
alibabacloud.com/nodepool-id=np421a7ecf1ccc469aa9b462fe0dd58f79,
beta.kubernetes.io/arch=amd64,
beta.kubernetes.io/instance-type=ecs.sn2.large,
beta.kubernetes.io/os=linux,data=a,
failure-domain.beta.kubernetes.io/region=cn-hangzhou,
failure-domain.beta.kubernetes.io/zone=cn-hangzhou-b,
kubernetes.io/arch=amd64,
kubernetes.io/hostname=cn-hangzhou.172.16.0.157,
kubernetes.io/os=linux,
node.kubernetes.io/service=teleport

```

## nodeSelector调度
- nodeSelector是节点选择约束的最简单推荐形式。nodeSelector是PodSpec的领域。它指定键值对的映射。
- Pod.spec.nodeSelector是通过Kubernetes的label-selector机制选择节点，由调度器调度策略匹配label，而后调度Pod到目标节点，该匹配规则属于【强制】约束。
由于是调度器调度，因此不能越过Taints污点进行调度。


- 我们给node：cn-hangzhou.172.16.0.157添加labels
```
chenyunbin@chenyunbindeMacBook-Pro ~ % kubectl label nodes cn-hangzhou.172.16.0.157 service-type=web
node/cn-hangzhou.172.16.0.157 labeled
```
- 删除标签命令
``` 
chenyunbin@chenyunbindeMacBook-Pro ~ % kubectl label nodes cn-hangzhou.172.16.0.157 service-type-
node/cn-hangzhou.172.16.0.157 labeled
```
- 运行如下yaml文件并查看信息
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: scheduler-nodeselector-deploy
  labels:
    app: nodeselector-deploy
spec:
  replicas: 5
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: myapp-pod
        image: registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1
        imagePullPolicy: IfNotPresent
        ports:
          - containerPort: 80
      # 指定节点标签选择，且标签存在
      nodeSelector:
        service-type: web

```

```
[root@k8s-master scheduler]# kubectl apply -f scheduler_nodeSelector.yaml    更新或者创建pod信息
deployment.apps/scheduler-nodeselector-deploy created
[root@k8s-master scheduler]# 
[root@k8s-master scheduler]# kubectl get deploy -o wide   查看控制器信息
NAME                            READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS   IMAGES                                                      SELECTOR
scheduler-nodeselector-deploy   5/5     5            5           10s   myapp-pod    registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1   app=myapp
[root@k8s-master scheduler]# 
[root@k8s-master scheduler]# kubectl get rs -o wide      查看ReplicaSets信息
NAME                                       DESIRED   CURRENT   READY   AGE   CONTAINERS   IMAGES                                                      SELECTOR
scheduler-nodeselector-deploy-79455db454   5         5         5       14s   myapp-pod    registry.cn-beijing.aliyuncs.com/google_registry/myapp:v1   app=myapp,pod-template-hash=79455db454
[root@k8s-master scheduler]# 
```

- 查看pod信息，这里显示出所有 pod都被调度到了k8s-node01中
```
[root@k8s-master scheduler]# kubectl get pod -o wide    
NAME                                             READY   STATUS    RESTARTS   AGE   IP             NODE         NOMINATED NODE   READINESS GATES
scheduler-nodeselector-deploy-79455db454-745ph   1/1     Running   0          19s   10.244.4.154   k8s-node01   <none>           <none>
scheduler-nodeselector-deploy-79455db454-bmjvd   1/1     Running   0          19s   10.244.4.151   k8s-node01   <none>           <none>
scheduler-nodeselector-deploy-79455db454-g5cg2   1/1     Running   0          19s   10.244.4.153   k8s-node01   <none>           <none>
scheduler-nodeselector-deploy-79455db454-hw8jv   1/1     Running   0          19s   10.244.4.152   k8s-node01   <none>           <none>
scheduler-nodeselector-deploy-79455db454-zrt8d   1/1     Running   0          19s   10.244.4.155   k8s-node01   <none>           <none>

```

- 如果nodeSelector匹配的标签不存在，则容器将不会运行，一直处于Pending 状态。


## 创建Pod流程
- 1 首先创建一个pod，然后创建一个API Server 和 Etcd【把创建出来的信息存储在etcd中】
- 2 然后创建 Scheduler，监控API Server是否有新的Pod，如果有的话，会通过调度算法，把pod调度某个node上
- 3 在node节点，会通过 kubelet -- apiserver 读取etcd 拿到分配在当前node节点上的pod，然后通过docker创建容器

API server
etcd
node
kubelet
docker

# 九 影响Pod调度的属性
- 就是可以指定pod的运行环境

Pod资源限制对Pod的调度会有影响

## 节点选择器标签影响Pod调度
- 关于节点选择器，其实就是有两个环境，然后环境之间所用的资源配置不同
    
```
 nodeSelector:
        node.kubernetes.io/service: tif-transformer
```

## 节点亲和性
- 节点亲和性 nodeAffinity 和 之前nodeSelector 基本一样的，根据节点上标签约束来决定Pod调度到哪些节点上
    - 硬亲和性：约束条件必须满足
    - 软亲和性：尝试满足，不保证
- 支持常用操作符：in、NotIn、Exists、Gt、Lt、DoesNotExists
- 反亲和性：就是和亲和性刚刚相反，如 NotIn、DoesNotExists等


## 污点和污点容忍
概述
- nodeSelector 和 NodeAffinity，都是Prod调度到某些节点上，属于Pod的属性，是在调度的时候实现的。
- Taint 污点：节点不做普通分配调度，是节点属性
- 场景
    - 专用节点【限制ip】
    - 配置特定硬件的节点【固态硬盘】
    - 基于Taint驱逐【在node1不放，在node2放】
- 污点值有三个
    - NoSchedule：一定不被调度
    - PreferNoSchedule：尽量不被调度【也有被调度的几率】
    - NoExecute：不会调度，并且还会驱逐Node已有Pod
- 查看污点情况
```
kubectl describe node k8smaster（node节点名字） | grep Taint

node：virtual-kubelet-cn-hangzhou-b yaml中配置
  taints:
    - key: virtual-kubelet.io/provider
      value: alibabacloud
      effect: NoSchedule

```

# 污点容忍
- 污点容忍就是某个节点可能被调度，也可能不被调度
```
  tolerations:
    - key: node.kubernetes.io/not-ready  ： Node未就绪状态
      operator: Exists
      effect: NoExecute
      tolerationSeconds: 300
    - key: node.kubernetes.io/unreachable
      operator: Exists
      effect: NoExecute
      tolerationSeconds: 300
```
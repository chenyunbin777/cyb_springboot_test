# kubernetes 核心技术-Label

## 概述
- Label 是 Kubernetes 系统中另一个核心概念。一个 Label 是一个 key=value 的键值对，其 中 key 与 value 由用户自己指定。
## **Label 可以附加到各种资源对象上，如 Node、Pod、 Service、RC，一个资源对象可以定义任意数量的 Label**， 同一个 Label 也可以被添加到 任意数量的资源对象上，
Label 通常在资源对象定义时确定，也可以在对象创建后动态 添 加或删除。

## 用法
- Label 的最常见的用法是使用 metadata.labels 字段，来为对象添加 Label，通过 spec.selector 来引用对象
    - Label用于给某个资源对象定义标识
    - Label Selector用于查询和筛选拥有某些标签的资源对象

- 创建标签命令：我们给node：cn-hangzhou.172.16.0.157添加labels
```
chenyunbin@chenyunbindeMacBook-Pro ~ % kubectl label nodes cn-hangzhou.172.16.0.157 service-type=web
node/cn-hangzhou.172.16.0.157 labeled
```
- 删除标签命令
``` 
chenyunbin@chenyunbindeMacBook-Pro ~ % kubectl label nodes cn-hangzhou.172.16.0.157 service-type-
node/cn-hangzhou.172.16.0.157 labeled


- 1 kubectl get node -o wide --show-labels 查看node中的label信息
- 2 kubectl get pod -o wide --show-labels  查看pod的label信息

```
NAME                                                 READY   STATUS      RESTARTS   AGE     IP             NODE                       NOMINATED NODE   READINESS GATES   LABELS
file-transformer-helm-transformer-754579889b-zqsk5   2/2     Running     0          3d2h    172.20.2.156   cn-hangzhou.172.16.0.173   <none>           <none>            app=transformer-helm-transformer,pod-template-hash=754579889b

```

- pod的资源配置文件中的labels配置信息

```

apiVersion: v1
kind: Pod
metadata:
  name: file-transformer-helm-transformer-754579889b-zqsk5
  generateName: file-transformer-helm-transformer-754579889b-
  namespace: test
  selfLink: >-
    /api/v1/namespaces/test/pods/file-transformer-helm-transformer-754579889b-zqsk5
  uid: 5e146d01-08d7-4b90-9a1d-3ca3ce04c69e
  resourceVersion: '4059779526'
  creationTimestamp: '2021-10-18T07:47:36Z'
  labels:
    app: transformer-helm-transformer   这就是labels的标志，定义了这是一个app pod
    pod-template-hash: 754579889b

```

- 3 kubectl get svc -o wide --show-labels  查看service的label信息

```
NAME                         TYPE           CLUSTER-IP      EXTERNAL-IP    PORT(S)                            AGE    SELECTOR                     LABELS
v2master-chenyunbin-svc      ClusterIP      172.21.1.164    <none>         80/TCP,443/TCP                     140d   app=v2master-chenyunbin      <none>
```


```
apiVersion: v1
kind: Service
metadata:
  name: v2master-chenyunbin-svc
  namespace: test
  selfLink: /api/v1/namespaces/test/services/v2master-chenyunbin-svc
  uid: 219b3a8a-25e8-434c-9aa8-10f4372b207c
  resourceVersion: '3370413623'
  creationTimestamp: '2021-06-03T07:39:38Z'
  annotations:
    kubectl.kubernetes.io/last-applied-configuration: >
      {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"name":"v2master-chenyunbin-svc","namespace":"test"},"spec":{"ports":[{"name":"http","port":80,"protocol":"TCP"},{"name":"https","port":443,"protocol":"TCP"}],"selector":{"app":"v2master-chenyunbin"}}}
status:
  loadBalancer: {}
spec:
  ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: 80
    - name: https
      protocol: TCP
      port: 443
      targetPort: 443
  selector:
    app: v2master-chenyunbin   ：通过 spec.selector 来引用对象，可以通过这个标识进行查询和筛选
  clusterIP: 172.21.1.164
  type: ClusterIP
  sessionAffinity: None
```


- Label 附加到 Kubernetes 集群中各种资源对象上，目的就是对这些资源对象进行分组管理， 
而分组管理的核心就 是 Label Selector。Label 与 Label Selector 都是不能单独定义， 
必须附加在一些资源对象的定义文件上，一般附加 在 RC 和 Service 的资源定义文件中
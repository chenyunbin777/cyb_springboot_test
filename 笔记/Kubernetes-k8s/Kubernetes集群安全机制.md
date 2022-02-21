# Kubernetes集群安全机制
#概述
- 当我们访问K8S集群时，需要经过三个步骤完成具体操作
    - 认证
    - 鉴权【授权】
    - 准入控制
- 进行访问的时候，都需要经过 apiserver， apiserver做统一协调，比如门卫
    - 访问过程中，需要证书、token、或者用户名和密码
    - 如果访问pod需要serviceAccount
    
    
# 认证
- 客户端身份认证常用方式
    - https证书认证，基于ca证书
    - http token认证，通过token来识别用户
    - http基本认证，用户名 + 密码认证
    
    
# 鉴权
- 基于RBAC进行鉴权操作

- 基于角色访问控制


# 准入控制
- 就是准入控制器的列表，如果列表有请求内容就通过，没有的话 就拒绝


# RBAC介绍(Role-Based Access Control)
- 基于角色的访问控制，为某个角色设置访问内容，然后用户分配该角色后，就拥有该角色的访问权限

k8s中有默认的几个角色

role：特定命名空间访问权限
ClusterRole：所有命名空间的访问权限
角色绑定

roleBinding：角色绑定到主体
ClusterRoleBinding：集群角色绑定到主体
主体

user：用户
group：用户组
serviceAccount：服务账号


# RBAC实现鉴权
## 命名空间
- kubectl get namespace 查看命名空间默认的是default
- kubectl create ns roledemo 创建命令空间
- 命名空间创建Pod
    - kubectl run nginx --image=nginx -n roledemo
# 实操一：User-role-rolebinding 用户与角色Role绑定
https://blog.csdn.net/wangmiaoyan/article/details/102551390

- 1、创建角色pod-reader，赋予角色对pods进行list,get,watch操作。
    - 创建：kubectl apply -f rbac-role.yaml
    - 查看：kubectl get role -n roledemo
    kubectl get role
    NAME                                    AGE
    milvus-minio-update-prometheus-secret   2d18h
    pod-reader                              4m5s
  
```
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: roledemo #定义namespace
  creationTimestamp: null
  name: pod-reader
rules:
- apiGroups:
  - ""
  resources:
  - pods
  verbs:
  - list
  - get
  - watch

```
- 2、将用户与角色进行绑定
创建角色绑定
- 我们还是通过 role-rolebinding.yaml 的方式，来创建我们的角色绑定，将某些用户的权限设置为某些Role的权限。
```
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  namespace: roledemo #定义namespace
  creationTimestamp: null
  name: sunny-read-pods
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: pod-reader
subjects:
- apiGroup: rbac.authorization.k8s.io
  kind: User
  name: sunny # 将用户sunny与pod-reader角色进行绑定，这样他就有我们pod-reader角色设置的权限了
```
- 创建角色绑定
kubectl apply -f rbac-rolebinding.yaml
- 查看角色绑定
kubectl -n roledemo  get role,rolebinding
NAME                                        AGE
role.rbac.authorization.k8s.io/pod-reader   19m
NAME                                                    AGE
rolebinding.rbac.authorization.k8s.io/sunny-read-pods   3m51s

- 3、测试 
    - 切换至sunny用户，请求查看pods资源与请求查看service资源，预期pods应该可以看到，而service资源应该看不到
    - kubectl config use-context sunny@kubernetes  #切换sunny用户
    - kubectl get pods  #请求pods资源，可看到
    - kubectl get svc   #请求svc资源，被拒绝

# 实操二 user-clusterrole-clusterrolebinding 集群角色与集群绑定

# 实操三 user-rolebinding-clusterrole 角色与集群绑定


# 结论
- k8s中有角色和角色绑定，因为K8S有两种资源，一种是集群资源，也就是cluster；一种是namespace资源；所以分别有role,rolebinding,clusterrole,clusterrolebinding.
他们的区别在于作用域不同，cluster是针对整个集群资源的，而role则是限制在namespace中的。
- 这里有个特例就是role可以绑定clusterrole，这是很便捷的一个操作，假设你有十个namespace，每个namespace要建立一个只读权限的角色，那么你需要在10个namespace中分别建立rolebinding为get；
但是如果role可以绑定clusterrolebinding，那么只需要建立一个clusterrolebinding为get，然后使用role去绑定这个clusterrolebinding即可，而不需要去建10次。
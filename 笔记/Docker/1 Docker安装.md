# 一 安装
macOS 我们可以使用 Homebrew 来安装 Docker。
Homebrew 的 Cask 已经支持 Docker for Mac，因此可以很方便的使用 Homebrew Cask 来进行安装：
```
- $ brew install --cask --appdir=/Applications docker
```


# 二 配置镜像加速
- 由于国内网络问题，后续拉取 Docker 镜像十分缓慢，我们可以需要配置加速器来解决，我使用的是网易的镜像地址：http://hub-mirror.c.163.com。
- 在设置-》Docker Engine中增加：
```
{
  "registry-mirrors": [
    "https://hub-mirror.c.163.com"
  ],
  "experimental": false,
  "features": {
    "buildkit": true
  }
}
```

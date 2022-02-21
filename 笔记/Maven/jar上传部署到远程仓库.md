
# 将本地的jar包部署到远程的maven仓库中

mvn deploy:deploy-file -DgroupId=com.aspose -DartifactId=aspose-imaging -Dversion=21.6 -Dpackaging=jar -Dfile=aspose-imaging-21.6-jdk16.jar -DrepositoryId=releases -Durl=http://s7.qbuild.corp.qihoo.net:9081/nexus/repository/maven-releases/ -X
# Gauss
Gauss是一个服务总线平台，可以用于发布Http服务和转发Http服务，架构上基于redkale进行开发，采用非关系型数据库进行存储，功能如下：
1. 快速建立HTTP发布服务，采用Restful风格的接口
2. 转发HTTP请求，建立多线程服务队列
3. 获取并统计相关HTTP请求的结果，存储在MongDB中
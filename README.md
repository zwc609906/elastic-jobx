##Elastic-Jobx - distributed scheduled job solution
[当当Elastic-Job](https://github.com/dangdangdotcom/elastic-job/tree/1.0.7) 

  Elastic-Jobx扩展至当当的Elastic-job1.0.7，增加如下功能:
  1. 支持jdk1.6，客户端支持Spring3.1+;
  2. 管控平台增加任务添加功能；
  3. 指定时间段任务跳过执行功能；
  4. 动态分片策略功能；
  5. 简化客户端任务配置项；
  6. 作业执行历史信息记录功能；
  7. 作业手动触发功能。
  


## Elastic-Jobx扩展功能

### 扩展功能

* **管控平台任务添加功能：** 可在管控平台预先添加任务配置，方便集中管理。
* ![任务添加功能](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/console_F01.png)

* **指定时间段任务跳过执行功能：** 在有大促或其它需求时，可提前配置全局或单个任务的暂停时间段。在指定时间段内任务将跳过执行。
* ![任务跳过执功能](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/console_F02_V1.PNG)

* **动态分片策略功能：** 可以在管控平台上传分片策略类，动态调整分片策略。
* ![动态分片策略功能](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/console_F03.png)

* **作业执行历史信息记录功能：** 通过在管控平台选主一台服务器监控ZK，记录作业执行记录(目前实现了PostgreSql的存储，可在全局配置中关闭，默认打开)。并实现了一个清理作业，默认清理时间为15天。
* ![历史信息记录功能](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/console_F04.PNG)

* **作业手动触发功能：** 可以在管控平台手动触发Job。
* ![手动触发功能](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/console_F05.PNG)

### ZK添加节点(1.0.2版本前)
 ![ZK添加节点](https://github.com/artoderk/elastic-jobx/blob/master/elastic-jobx-doc/content/img/zk_01.png)
 
## Quick Start

* **引入maven依赖**

1. 下载整个项目；
2. 使用maven打包；
3. 引入elastic-jobx-core.jar或elastic-jobx-spring.jar

```xml
<!-- 引入elastic-jobx核心模块 -->
<dependency>
    <groupId>com.dangdang</groupId>
    <artifactId>elastic-jobx-core</artifactId>
    <version>${lasted.release.version}</version>
</dependency>

<!-- 使用springframework自定义命名空间时引入 -->
<dependency>
    <groupId>com.dangdang</groupId>
    <artifactId>elastic-jobx-spring</artifactId>
    <version>${lasted.release.version}</version>
</dependency>
```
* **作业开发**

```java
public class MyElasticJob extends AbstractSimpleElasticJob {
    
    @Override
    public void process(JobExecutionMultipleShardingContext context) {
        // do something by sharding items
    }
}
```
```java
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.job.api.JobShortConfiguration;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.dangdang.example.elasticjob.core.job.SequenceDataFlowJobDemo;
import com.dangdang.example.elasticjob.core.job.SimpleJobDemo;
import com.dangdang.example.elasticjob.core.job.ThroughputDataFlowJobDemo;

public class JobDemo {
    
    // 定义Zookeeper注册中心配置对象
    private ZookeeperConfiguration zkConfig = new ZookeeperConfiguration("localhost:2181", "elasticjob-local", 1000, 3000, 3);
    
    // 定义Zookeeper注册中心
    private CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);
    
    // 定义作业1_1配置对象
    private JobConfiguration jobConfig1_1 = new JobConfiguration("simpleJob", (Class<? extends ElasticJob>) SimpleJobDemo.class, 1, "0/5 * * * * ?");
    
    // 定义作业1_2简化配置对象
    private JobConfiguration jobConfig1_2 = new JobShortConfiguration("myElasticJob", (Class<? extends ElasticJob>) MyElasticJob.class);
    
    // 定义作业2配置对象
    private JobConfiguration jobConfig2 = new JobConfiguration("throughputDataFlowJob", ThroughputDataFlowJobDemo.class, 1, "0/5 * * * * ?");
    
    // 定义作业3配置对象
    private JobConfiguration jobConfig3 = new JobConfiguration("sequenceDataFlowJob", SequenceDataFlowJobDemo.class, 10, "0/5 * * * * ?");
    
    public static void main(final String[] args) {
        new JobDemo().init();
    }
    
    private void init() {
        // 连接注册中心
        regCenter.init();
        // 启动作业1_1
        new JobScheduler(regCenter, jobConfig1_1).init();
        // 启动作业1_2
        new JobScheduler(regCenter, jobConfig1_2).init();
        // 启动作业2
        new JobScheduler(regCenter, jobConfig2).init();
        // 启动作业3
        new JobScheduler(regCenter, jobConfig3).init();
    }
}
```

* **作业配置**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:reg="http://www.dangdang.com/schema/ddframe/reg"
    xmlns:job="http://www.dangdang.com/schema/ddframe/job"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans.xsd
                        http://www.dangdang.com/schema/ddframe/reg
                        http://www.dangdang.com/schema/ddframe/reg/reg.xsd
                        http://www.dangdang.com/schema/ddframe/job
                        http://www.dangdang.com/schema/ddframe/job/job.xsd
                        ">
    <!--配置作业注册中心 -->
    <reg:zookeeper id="regCenter" serverLists=" yourhost:2181" namespace="dd-job" baseSleepTimeMilliseconds="1000" maxSleepTimeMilliseconds="3000" maxRetries="3" />

    <!-- 配置作业- 简化配置 -->
    <job:bean id="simpleElasticJob" class="com.dangdang.example.elasticjob.spring.job.SimpleJobDemo" regCenter="regCenter" />
    <!-- 配置作业-->
    <job:bean id="myElasticJob" class="xxx.MyElasticJob" regCenter="regCenter" cron="0/10 * * * * ?"   shardingTotalCount="3" shardingItemParameters="0=A,1=B,2=C" />
</beans>
```

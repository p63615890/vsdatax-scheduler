"# vsdatax-scheduler" 
    Datax是阿里开源的一个优秀的批量数据抽取框架/工具，提供了常用的Reader、Writer等组件的实现，可以实现在不同数据源之
间进行批量数据迁移。由于Datax本身并没有提供作业调度的功能，因此开发了VsDatax-Scheduler程序。
   VsDatax-Scheduler需要依赖vscommons、vsincr-framework两个项目（可以从我的github里下载）。所有项目采用mvn管理，
 mvn依赖的次序依次是VsDatax-Scheduler-->vsincr-framework-->vscommons.因此需要先mvn install vscommons,然后vsincr-framework,
 VsDatax-Scheduler.
 
   VsDatax-Scheduler说明详见《Datax增量任务调度程序说明》pdf文件


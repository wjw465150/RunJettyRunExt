    由于我们写WEB程序必须要引用"servlet和jsp的API库",而run-jetty-run只有在运行时才会导出此类的库路径,

    而我们在编写程序时要自己寻找"servlet和jsp的API库"库,多麻烦啊,俗话说:"懒人创造世界"!,

    既然run-jetty-run插件已经包含"servlet和jsp的API库"文件,何不让他自动建立一个User Library呢?
    于是RunJettyRunExt扩展插件横空出世!
扩展[run-jetty-run](https://code.google.com/p/run-jetty-run/)的Eclipse插件,自动把servlet,jsp的api库自动加入到Eclipse的user Libraries里,名称是:"JSP_API",包含jsp-2.1.jar,jsp-api-2.1.jar,servlet-api-2.5-20081211.jar

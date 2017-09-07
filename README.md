# frequest

[![Build Status](https://travis-ci.org/BrightLoong/frequest.svg?branch=master)](https://travis-ci.org/BrightLoong/frequest) [![Maven Central](https://img.shields.io/maven-central/v/io.github.brightloong/frequest.svg)](http://search.maven.org/#artifactdetails%7Cio.github.brightloong%7Cfrequest%7C1.0%7Cjar) [![Javadocs](http://www.javadoc.io/badge/io.github.brightloong/frequest.svg)](http://www.javadoc.io/doc/io.github.brightloong/frequest) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

## 一.简介
这是一个用JAVA编写的，可以通过文件进行方法调用请求传输的工具。

项目gitgub地址：<https://github.com/BrightLoong/frequest>

## 二.背景
之前在项目中遇到一下的需求，如图所示: 甲处要访问部署在乙处的服务serverB(因为数据库在乙处)，不过因为一些限制原因导致甲乙两地的网络不通。但是甲乙两地之间有一个文件传输的系统仅仅可以进行文件的传输交换。

基于以上的条件，考虑在甲地也搭建一个同样的服务serverA（A和B相同，并都加入对请求的处理），。但是过滤它对service层的调用，将方法调用放入文件中（也就是请求文件中），然后将文件发送到乙地对应目录（文件发送的功能并不由这两个系统负责）。serverB将解析文件的请求，调用对应方法，并将结果也存到文件中发送到甲的服务器serveA处，实现请求的响应。

这里把拦截本地方法调用，生请求文件，等待远端返回结果和远端响应文件请求并将结果生成问文件的功能抽取出来，并对这部分功能进行了提炼重构，修改了一些问题，形成了工具frquest(file-request)。

![项目背景](https://brightloong.github.io/images/frequest-背景.png)

## 三.具体思路

![实现思路](https://brightloong.github.io/images/frequest-思路.png)

**甲：本地服务，需要点去远端服务乙**

**乙：远端服务，可自己访问自己，亦可以接收甲的请求**

- 甲处发起请求，调用了方法，使用aspectj对方法调用进行拦截
- 对拦截到的方法进行解析，记录下调用方法名，参数，类，接口等信息，并将信息放入传输bean生成json字符串，写入到文件
- 把生成的请求文件发送到指定目录，并等待远端返回结果
- ......
- 乙远端扫描到请求文件，开始远端方法调用
- 对文件进行解析，反过来转为json字符串，再转为传输Bean，从中获取方法名，参数等信息，进行一定的处理。使用反射（invok）调用方法，把得到的结果放入传输Bean中，转为json字符串放入结果文件，发送到指定目录
- ......
- 甲检测到结果文件，转为json字符串，再转为传输Bean，获取结果信息。

## 四. 使用

可在java项目中使用。

### 1. 引用

- 直接下载jar包，下载地址<http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22frequest%22>


- 使用maven的方式引用

  ```xml
  <dependency>
  	<groupId>io.github.brightloong</groupId>
  	<artifactId>frequest</artifactId>
  	<version>1.0</version>
  </dependency>
  ```

> **注：**如果是java项目需要继承aspectj环境，如果是集成了spring的web项目需要开启aspectj支持

### 2. 配置发送接收文件的目录

请求端的发送目录是指拦截请求生成的请求文件，接收目录是指发送请求后等待接收的结果文件的目录。

远端的发送目录是值生成的结果文件的发送目录，接收目录是指扫描请求端的请求文件的目录。

项目中使用了xml配置的方式，格式如下。

> **注：本工具不负责文件的传输，所以如果想用该工具在本机做测试，可以把远端和请求端的send-path和receive-path交替配置**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configs>
    <config>
        <send-path>d:\send</send-path>
        <receive-path>d:\receive</receive-path>
    </config>
    <!--远端可以配置多个config，可能会有多个请求端需要同一个远端响应,请求端配置多个也默认只取第一个-->
    <config>
        <send-path>d:\send</send-path>
        <receive-path>d:\receive</receive-path>
    </config>
</configs>
```

### 3. 请求端（被拦截端）配置

- 同样首先需要配置收发文件目录（也就是上面说的xml）

- 如果是简单的java项目可使用下面的方式启动。

  - 继承`ServiceProxyInterceptor`并且实现`serviceAroundImpl()`方法
  - 在实现方法类调用父类的serviceAround()
  - 标注上`@Around`注解
  - 在Around里面加入切入点，`PointConstants.POINT_SERVICE`是在工具中定义的一个切入点，也就是对具有自定义注解 `@ServiceProxy`的方法进行拦截，也可以定义自己的切入点。
  - 启动frequest功能，如下mian()方法中前两行代码所示。
  - 如下，如果要对say()方法进行拦截，则加上 `@ServiceProxy`注解(前提是你使用了定义的切入点`PointConstants.POINT_SERVICE`，并且要保证请求端和远端拥有相同的方法，包括方法所在的类和包)

  ```java
  @Aspect
  public class Test extends ServiceProxyInterceptor{
      @Override
      @Around(PointConstants.POINT_SERVICE)
      public Object serviceAroundImpl(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
          return super.serviceAround(proceedingJoinPoint);
      }

      @ServiceProxy
      public String say(String msg) {
          System.out.println(msg);
          return msg;
      }

      public static void main(String[] args) {
         //setIsRemote(false),设置为请求端，并设置xml的目录。
          NormalConfig.getInstance().setXmlConfigPath("com/test/xml/config.xml").setIsRemote(false);
         //开启frequest功能
          StartEngine.start();
          Test test = new Test();
          System.out.println(test.say("hello"));
      }
  }
  ```



### 4. 远端（相应文件请求端）配置 

- 同样首先需要配置收发文件目录（也就是上面说的xml）

- 如果是简单的java项目可使用下面的方式启动。如果是web项目，同样也是调用mian()方法中的两行代码，保证其能在合适的地方进行调用，启动功能。

  ```java
  public class Start {
      public static void main(String[] args) {
        	//setIsRemote(true),设置为远端，并设置xml的目录。
          NormalConfig.getInstance().setIsRemote(true).setXmlConfigPath("com/test/xml/config.xml");
         //开启frequest功能
          StartEngine.start();
      }
  }
  ```

  ​

  ​

### 5. 其他配置

下面的配置具有默认值，所以不进行配置也是可以的。

- sleepTime：休眠时间，单位ms，扫描文件变动的间隔时间和等待远端返回结果的每次等待时间，默认是100ms。
- waitCount：请求端等待远端返回结果的次数，每次间隔时间为sleepTime，所以总共等待时间为waitCount*sleepTime(ms)。默认等待次数是300次

```java
//设置休眠时间sleepTime,设置等待结果次数300次
 NormalConfig.getInstance().setSleepTime(100).setWaitCount(300);
```

## 五.  LICENSE

遵循Apache License 2.0


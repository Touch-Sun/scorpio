# Scorpio(天蝎座)

✨ Scorpio(天蝎座) - 是一款基于Java语言的微型Web容器，名字的灵感来源于与女朋友的星座

<!-- PROJECT SHIELDS -->

![GitHub last commit](https://img.shields.io/github/last-commit/Touch-Sun/scorpio)
![GitHub issues](https://img.shields.io/github/issues/Touch-Sun/scorpio)
![GitHub Repo stars](https://img.shields.io/github/stars/Touch-Sun/scorpio)
![GitHub](https://img.shields.io/github/license/Touch-Sun/scorpio)
<!-- ![GitHub watchers](https://img.shields.io/github/watchers/Touch-Sun/scorpio) -->

<!-- PROJECT LOGO -->
<br />

<p align="center">
  <a href="https://github.com/Touch-Sun/scorpio">
    <!-- <img src="https://s1.328888.xyz/2022/06/19/0O7RC.png" alt="Logo" width="300" height="300"> -->
    <img style="box-shadow: 1px 1px 10px #6b1839; border-radius: 15px" src="https://s1.328888.xyz/2022/06/21/sArGM.png" alt="Logo" width="auto" height="auto">
  </a>

<h3 align="center">小巧、功能丰富、食用方便</h3>
  <p align="center">
    本项目支持类似Tomcat的所有核心功能，且都做了Micro设计！
    <br />
    <a href=""><strong>探索本Scorpio的使用手册 »</strong></a>
    <br />
    <br />
    <a href="">看看小Demo</a>
    ·
    <a href="">提个Bug</a>
    ·
    <a href="">有新的点子</a>
  </p>

</p>


Scorpio面向Java开发者，为他们巩固Web编程的基础,当然设计细想却不局限

## 目录

- [上手指南](#上手指南)
- [开发前的配置要求](#开发前的配置要求)
- [安装步骤](#安装步骤)
- [文件目录说明](#文件目录说明)
- [开发的架构](#开发的架构)
- [部署](#部署)
- [使用到的框架](#使用到的框架)
- [贡献者](#贡献者)
- [如何参与开源项目](#如何参与开源项目)
- [版本控制](#版本控制)
- [作者](#作者)
- [鸣谢](#鸣谢)

### 上手指南

直接Git clone; 干！

###### 开发前的配置要求

1. 准备好IDEA
2. 准备好浏览器

###### **安装步骤**

1. Clone the repo
2. 直接运行即可

```sh
git clone https://github.com/Touch-Sun/scorpio.git
```

### 文件目录说明

eg:

```
scorpio-----------------------------------------------[项目目录]
├── /config/------------------------------------------[应用配置文件目录]
│  ├── server.xml-------------------------------------[Web应用配置文件]
├── /example/-----------------------------------------[配置型Web应用案例目录]
│  ├── /score/----------------------------------------[配置型Web应用案例`score`]
│  ├── /student/--------------------------------------[配置型Web应用案例`student`]
│  ├── /.../------------------------------------------[配置型Web应用案例`其余案例`]
├── /lib/---------------------------------------------[应用依赖JAR目录]
│  │  └── hutool-all-4.3.1.jar------------------------[应用依赖JAR`hutool-all-4.3.1`]
│  │  └── jsoup-1.12.1.jar----------------------------[应用依赖JAR`jsoup-1.12.1`]
│  │  └── ...-----------------------------------------[应用依赖JAR`其余JAR`]
├── /src----------------------------------------------[应用源文件目录]
├── /webapps/-----------------------------------------[内置Web应用目录]
│  ├── /ROOT/-----------------------------------------[内置应用`ROOT`|`根`|`/`]
│  ├── /numbers/--------------------------------------[内置应用`numbers`]
│  ├── /.../------------------------------------------[内置应用`其余应用`]
├── .gitignore----------------------------------------[git忽略文件配置]
├── LICENSE-------------------------------------------[MIT协议文件]
├── pom.xml-------------------------------------------[项目结构文件]
└── README.md-----------------------------------------[README文件]

```

### 开发的架构

阅读Tomcat源码后,按照Tomcat的设计理念对Scorpio架构

### 部署

暂无

### 使用到的框架

- Java语言基础

### 贡献者

敬请期待您的贡献

#### 如何参与开源项目

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### 版本控制

该项目使用Git进行版本管理。您可以在repository参看当前可用版本。

### 作者

TouchSun

思否：哈迪斯

*您也可以在贡献者名单中参看所有参与该项目的开发者，当然目前还没有贡献者😂*

### 版权说明

该项目签署了MIT 授权许可

### 鸣谢

感谢IDEA提供工具支持

# 基于 Spring AI Alibaba 智能提框架的项目管理策略

## 概述

本文档提供了一套完整的项目管理策略，用于在保持与官方 Spring AI Alibaba 框架同步的同时，独立管理业务项目。该策略基于 Fork + 上游同步 + 业务项目独立管理的模式。

## 核心策略

### 1. Fork + 上游同步策略

#### 1.1 初始设置

**Step 1: Fork 官方仓库**
```bash
# 在 GitHub 上 fork https://github.com/alibaba/spring-ai-alibaba
# 然后克隆您的 fork
git clone https://github.com/YOUR_USERNAME/spring-ai-alibaba.git
cd spring-ai-alibaba
```

**Step 2: 添加上游远程仓库**
```bash
# 添加官方仓库作为上游
git remote add upstream https://github.com/alibaba/spring-ai-alibaba.git

# 验证远程仓库配置
git remote -v
# 应该显示：
# origin    https://github.com/YOUR_USERNAME/spring-ai-alibaba.git (fetch)
# origin    https://github.com/YOUR_USERNAME/spring-ai-alibaba.git (push)
# upstream  https://github.com/alibaba/spring-ai-alibaba.git (fetch)
# upstream  https://github.com/alibaba/spring-ai-alibaba.git (push)
```

#### 1.2 定期同步策略

**日常同步流程：**
```bash
# 1. 切换到主分支
git checkout master

# 2. 拉取官方最新更新
git fetch upstream
git merge upstream/master

# 3. 推送到您的 fork
git push origin master
```

**处理冲突：**
```bash
# 如果有冲突，手动解决后
git add .
git commit -m "Resolve merge conflicts with upstream"
git push origin master
```

### 2. 业务项目独立管理

#### 2.1 方案选择

我们提供两种业务项目管理方案：

**方案A：业务分支管理（推荐）**
- 在同一个仓库中使用独立分支
- 管理简单，同步方便
- 适合中小型业务项目

**方案B：完全独立仓库**
- 创建完全独立的业务项目仓库
- 通过 Maven 依赖引用框架
- 适合大型业务项目或多团队协作

#### 2.2 方案A：业务分支管理

**创建业务分支：**
```bash
# 基于最新的 master 创建业务分支
git checkout master
git pull origin master
git checkout -b business/your-project-name
```

**目录结构建议：**
```
spring-ai-alibaba/
├── 框架原有目录...
├── business-projects/           # 新增业务项目目录
│   └── your-project-name/
│       ├── pom.xml
│       ├── src/main/java/
│       │   └── com/yourcompany/
│       │       ├── Application.java
│       │       ├── controller/
│       │       ├── service/
│       │       └── config/
│       ├── src/main/resources/
│       │   ├── application.yml
│       │   └── static/
│       └── README.md
└── 其他框架目录...
```

**业务分支同步：**
```bash
# 定期将 master 的更新合并到业务分支
git checkout business/your-project-name
git rebase master

# 如果有冲突，解决后继续
git add .
git rebase --continue
```

#### 2.3 方案B：完全独立仓库

**创建独立业务项目：**
```bash
# 在不同目录创建业务项目
mkdir ../my-ai-business-project
cd ../my-ai-business-project
git init
```

**项目结构：**
```
my-ai-business-project/
├── pom.xml                    # 依赖 spring-ai-alibaba-starter
├── src/main/java/
│   └── com/yourcompany/
│       ├── Application.java
│       ├── controller/
│       ├── service/
│       └── config/
├── src/main/resources/
│   ├── application.yml
│   └── static/
├── src/test/java/
└── README.md
```

**依赖配置示例：**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.yourcompany</groupId>
    <artifactId>my-ai-business-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <dependencies>
        <!-- Spring AI Alibaba Starter -->
        <dependency>
            <groupId>com.alibaba.cloud.ai</groupId>
            <artifactId>spring-ai-alibaba-starter-dashscope</artifactId>
            <version>1.0.0-M2</version>
        </dependency>
        
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- 其他业务依赖 -->
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 工作流程

### 3.1 日常开发流程

**方案A（业务分支）：**
```bash
# 1. 同步最新框架代码
git checkout master
git pull upstream master
git push origin master

# 2. 更新业务分支
git checkout business/your-project-name
git rebase master

# 3. 开发业务功能
# 编辑代码...
git add .
git commit -m "Add new business feature"
git push origin business/your-project-name
```

**方案B（独立仓库）：**
```bash
# 1. 在框架仓库同步最新代码
cd spring-ai-alibaba
git checkout master
git pull upstream master
git push origin master

# 2. 在业务项目中更新依赖版本（如需要）
cd ../my-ai-business-project
# 更新 pom.xml 中的版本号
mvn clean install

# 3. 开发业务功能
# 编辑代码...
git add .
git commit -m "Add new business feature"
git push origin main
```

### 3.2 版本管理策略

**框架版本管理：**
- 跟踪官方发布的稳定版本
- 在业务项目中锁定特定版本
- 定期评估和升级框架版本

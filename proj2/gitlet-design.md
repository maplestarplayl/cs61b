## 3.30号对本文档进行一些增删，增加并修改了一些命令的注释
## 3.6号新增有关该项目本身的一些解释
   
### 其实本项目是跟着Berkeley CS61B的project 2 完成的，不过这门课程仅仅提供了设计文档，还有大致思路的讲解，真正的代码还是需要学生一点一点敲出来的。正因如此，这门project的难度也相对来说没那么高，我花了一整天的时间来阅读设计文档（据说近两万个词），以及其助教录制的git原理讲解视频，然后投入了大概两天的时间将大部分的需求实现了。之所以说这么多，是因为希望读者能对该项目的难度有一个客观，理性的认知。
## 如何阅读源代码
### 具体实现都在文件“gitlet"的里面，而那些提交时间为三月前的就是我的成果.其实还打算重构来着，毕竟写的时候突出一个能用就行，回头再看确实过于丑陋。可惜寒假打胶（bushi）咳咳..打游戏去了，就一直没来得及，估计再过10天把寒假大作战的搞完就开始重构。
### 具体来说，Blob,Commit,Repository,Main这几个文件都是我一行一行敲出来的，Utils那个则是课程老师提供的接口，让我们无需考虑i.o还有序列化的一些问题。所以只要看上面提到的四个文件就行了
## 具体实现了git的哪些功能
### 这个其实看Main函数就能明白，不过还是在这里列一下
#### 1.git init :初始化，将该文件变成gitlet 的一个库
#### 2.git add :将文件加入到缓冲区 （staging area)
#### 3.git commit: 完成一次提交
#### 4 git log : 查看当前分支提交历史还有commit的信息
#### 5.global log :不区分branch 和 master，根据提交的时间记录所有提交
#### 6.git checkout : 恢复到某次提交的状态或切换分支
#### 7.git rm : 删除文件或将指定文件从缓存区移除
#### 8.git find : 找到含指定message的提交并返回commitID
#### 9.git branch : 创建分支
#### 10.git rm branch :删除分支



# Gitlet Design Document(To be continued)

**Name**:

## Classes and Data Structures

### Class Blob

#### Fields

1. Field 1 :bytes 
通过Util里的方法将blob serilization 成字节串，用于存储
2. Field 2 :index 通过sha1算法得到该blob索引
#### Method
1.createBlob（File f) 传入一个文件f，调用Blob的构造函数。
2.returnBlobByIndex(String index)根据索引找到并返回该Blob对象



### Class 2

#### Fields

1. Field 1
2. Field 2


## Algorithms

## Persistence


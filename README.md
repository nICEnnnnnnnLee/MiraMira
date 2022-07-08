# MiraMira
目前支持网易云点歌、jrrp今日人品测试。  

以下为测试过的核心依赖版本：  
mirai-core: 2.12.0    
mirai-console: 2.12.0    
Mirai Console Loader version 2.1.0-71ec418

**目录**

- [网易云点歌](#网易云点歌)
- [今日人品](#今日人品)
- [闪照变正常](#闪照变正常)
- [动图添加文字](#动图添加文字)
- [支持命令](#支持命令)
  
### 网易云点歌  
支持黑白名单功能。点歌间隔为1分钟。    
一个可能的用例：  
```
某QQ用户:
网易云 Keep you mine
------------------
Bot: 
已为你找到以下歌曲：
0、Keep You Mine - NOTD / SHY Martin
1、Keep You Mine (Acoustic) - NOTD / SHY Martin
2、Keep You Mine - NOTD / SHY Martin
3、Keep You Mine - NOTD / SHY Martin
4、Keep You Mine - Willie West
5、Keep You Mine - NOTD / SHY Martin
6、keep you mine - Emoji
7、keep you mine - Jasper / Martin Arteta / 11:11 Music Group
8、Keep You Mine - NOTD / SHY Martin
9、Keep You Mine (银河) - BEAUZ / NOTD
发送序号即可点歌
------------------
某QQ用户:
0
------------------
Bot: 
[音乐卡片1]
------------------
某QQ用户:
1
------------------
Bot: 
[音乐卡片2]
```

### 今日人品  
返回一个[0, 100]的随机整数，当天都不会发生改变。  
一个可能的用例：  
```
某QQ用户:
.jrrp
------------------
Bot: 
@某QQ用户 你的jrrp为: 66
```

### 闪照变正常  
群消息发送一个闪照，将会回复一个正常照片  

### 动图添加文字  
+ 通过命令下载安装动图
```
/gif install
gif 正在进行安装，在没有结束之前请勿重复调用该命令
gif 安装完毕, 1 个成功, 1 个失败
安装成功：
 - 为所欲为.gif
安装失败：
 - 真香.gif

举例，如果始终`真香.gif`安装不上，那么，请自行下载
https://butterandbutterfly.github.io/Q-Gif/pics/真香/notext.gif
并放到
[配置文件夹]/gifs/真香.gif
```

+ 查询有哪些动图可用  
```
/gif list
 当前存在的动图为：
 - 为所欲为.gif
 - 你好骚哦.gif
 - 你那是馋她身子.gif
 - 元首骂人.gif
 - 充钱就能解决.gif
 - 吔屎啦梁非凡.gif
 - 外卖小哥我信你个鬼.gif
 - 张全蛋警告.gif
 - 张学友万恶之源.gif
 - 我卢本伟没有开挂.gif
 - 打人耳光.gif
 - 真香.gif
 - 谁赞成谁反对.gif
 - 金馆长斗图.gif
```

+ 发送命令生成动图
```
/gif 真香 台词1 台词2 台词3 台词4
机器人会回复相应动图
```

### 支持命令  
需要在`config/top.nicelee.mirai.miramira/config.yaml`中配置admin账号，否则以下命令只能在控制台输入，而不能通过QQ发消息改变。  
```
/gif [on|off]  开关动图加台词功能
/gif help  查询帮助
/gif list  查询当前存在的动图
/gif [fetch|install|download]  下载动图
 
/music [on|off] 开关点歌功能

/miramira help|h 查询帮助
/miramira status|st 查看状态
/miramira save|s 保存当前配置
/miramira jrrp [on|off] 开关今日人品功能
/miramira flashPicReply|fpr [on|off] 开关闪照变正常照片功能
/miramira whitelist|w [on|off] 开关白名单检测
/miramira whitelist|w add [g群号|fQQ号] 添加白名单
/miramira whitelist|w delete [g群号|fQQ号] 从白名单去掉
/miramira blacklist|b [on|off] 开关黑名单检测
/miramira blacklist|b add [g群号|fQQ号] 添加黑名单
/miramira blacklist|b delete [g群号|fQQ号] 从黑名单去掉
```

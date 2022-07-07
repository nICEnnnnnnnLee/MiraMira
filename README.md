# MiraMira
目前支持网易云点歌、jrrp今日人品测试。  

以下为测试过的核心依赖版本：  
mirai-core: 2.1.0
mirai-console: 2.12.0

# 网易云点歌  
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

# 今日人品  
返回一个[0, 100]的随机整数，当天都不会发生改变。  
一个可能的用例：  
```
某QQ用户:
.jrrp
------------------
Bot: 
@某QQ用户 你的jrrp为: 66
```

# 支持命令  
需要在`config/top.nicelee.mirai.miramira/config.yaml`中配置admin账号，否则以下命令只能在控制台输入，而不能通过QQ发消息改变。  
```
/music [on|off] 开关点歌功能

/miramira help|h 查询帮助
/miramira status|st 查看状态
/miramira save|s 保存当前配置
/miramira whitelist|w [on|off] 开关白名单检测
/miramira whitelist|w add [g群号|fQQ号] 添加白名单
/miramira whitelist|w delete [g群号|fQQ号] 从白名单去掉
/miramira blacklist|b [on|off] 开关黑名单检测
/miramira blacklist|b add [g群号|fQQ号] 添加黑名单
/miramira blacklist|b delete [g群号|fQQ号] 从黑名单去掉
```

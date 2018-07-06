# qlink
区块链项目

#p2p通信定义的逻辑
##请求WiFi密码
{"type":"wifipasswordReq","data":"{\"ssid\":\"YYM-1\",\"mac\":\"88:25:93:79:c1:31\"}"}
##发送WiFi密码
{"type":"wifipasswordRsp","data":"{\"address\":\"AM2tALNwmobiffTJQ2dpAuAwzC4mUSYFuU\",\"password\":\"youyoumob\",\"ssid\":\"YYM-1\",\"mac\":\"88:25:93:79:c1:31\"}"}
##请求用户分享的WiFi基本信息
{"type":"wifibasicinfoReq","data":"{\"ssid\":\"YYM-1\",\"mac\":\"88:25:93:79:c1:31\"}"}

##发送自己分享的WiFi基本信息
{"type":"wifibasicinfoRsp","data":"{\"deviceAllowed\":14,\"ssid\":\"YYM-1\",\"paymentType\":1,\"priceInQlc\":7.3,\"mac\":\"88:25:93:79:c1:31\",\"timeLimitPerDevice\":0,\"connectCount\":0,\"priceMode\":1,\"dailyTotalTimeLimit\":0}"}

##获取在线好友当前连接的WiFi的ssid，判断好友连接的WiFi是不是自己分享的WiFi

##发送自己当前连接的WiFi信息给好友

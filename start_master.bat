for /F "tokens=2 delims=:" %%i in ('"ipconfig | findstr IPv4 | findstr 192.168.1"') do SET LOCAL_IP=%%i
spark-class org.apache.spark.deploy.master.Master -h %LOCAL_IP%
echo

#快速编译打包apk脚本

echo "$$packge_begin$$"

#执行打包命令需要定位到项目更跟目录

cd..
gradle build
# 执行打包命令
gradle assembleRelease

echo -e "$$packge_begin$$"

#桌面右上角弹出通知
notify-send build.sh "pachge down!"


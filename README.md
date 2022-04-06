# SCRMRobot

SCRMRobot

项目发版流程：
1.关于测试/生产环境变量
  测试/生产环境中应对不同的变量，在AndroidManifest.xml及build.gradle文件中同时注册该变量，并在build.gradle中的
  buildTypes的打包环境类型中，在对应的release/debug环境中配置manifestPlaceholders字段中该变量名的value.

2.关于打包APK
  ·一般情况下直接运行/debug项目会产生app-debug.apk文件
  ·进入Android工程中，点开Bulid，选择Generate Signed Bundle/APK，从未打包过则选择创建新的密钥，注册选择好密钥之后
  则会在app/release下build新的app-release.apk文件.

  教程详见：https://zhuanlan.zhihu.com/p/51583507

3.关于发版分支流程
  develop分支（开发完成）——> test分支（测试完成）——> Master分支/打版本Tag
  其他feat/fix分支（完成） ——> develop ——> test分支（测试完成）——> 删除分支

app运行配置：
- 加入自启动
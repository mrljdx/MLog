## MLog
MLog是一个吸取了logger和timber的一个简单的Android Log工具库，用于打印日志的同时方便代码追踪和调试。

MLog提供如下功能：
* 打印Log线程信息
* 打印Log的类、方法以及所在行数
* 支持点击Log跳转到对应的类及行

## Dependence

    compile 'com.mrljdx.tools:mlog:0.1.4'
    

## MLog配置
MLog在不配置的情况下，默认TAG为"TAG"，在Application中可以配置MLog的全局打印TAG
    
    MLog.initLog("MLog") //设置Tag
                    .saveLog(BuildConfig.DEBUG) //是否保存Log日志到文件
                    .showLog(BuildConfig.DEBUG) //Log开关
                    .setLogDir("MyLogDir");  //Log存放的目录地址
                    

## MLog使用示例

打印普通的Log

    MLog.d("Hello MLog");
    
打印JSON

    MLog.json(JSON_STR); //JSON_STR 为 JSON字符串
    
打印XML

    MLog.xml(XML_STR); //XML_STR 为 XML 字符串
    
将需要的日志保存到日志文件

    MLog.file(FILE_DIR,LOG_FILE_NAME,LOG_MESSAGE); //FIEL_DIR 为文件夹路径，LOG_FILE_NAME为日志文件名，LOG_MESSAGE为日志内容
    
以上就是基本的MLog的使用示例

## License

<pre>
Copyright 2015 Mrljdx

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
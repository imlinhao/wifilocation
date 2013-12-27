To execute our codes properly, you need to install python 2.7+, matplotlib, numpy. The easiest way to install them is using pythonxy avialibe at http://code.google.com/p/pythonxy/wiki/Downloads.


The datasets is in https://github.com/imlinhao/wifilocation-datasets

You need download the datasets and place it under the scipts folder, to execute the code in scipts properly.


apks目录下是已编译的android程序, codes目录下为其相对应的android工程

--SensorRecord-debug.apk 所有信息都会记录在sdcard的sensorrecord目录底下。记录了手机型号的基本信息，以及声音、GPS、WiFi、加速度、陀螺仪、光照、湿度、温度、靠近传感器的数据，界面上的两个按钮，点击之后会记录点击按钮的时间。需要说明的是声音数据采用8kHz采样，PCM16位编码的方式进行记录，可以使用GoldWave等相关音频处理程序进行音频的回放。此外点击普通按钮记录下来的时间放在了movetime.txt里面，点击开关按钮记录下来的时间放在了stilltime.txt里面。

--SensorRecordSpecificChannels-debug.apk 基本功能和SensorRecord是一致的，只是该apk多了一个指定信道扫描的功能。这个功能的代价还是比较大的，首先要求手机必须是root过的，其次手机必须支持NL80211，然后在运行程序之前需要将libnl-3.so和libnl-genl-3.so拷贝到/system/lib下，然后把iw拷贝到/system/bin下，具体过程可以参考我们的一篇博文 http://blog.csdn.net/jksl007/article/details/16862435 。目前指定扫描信道，我们是放在wifilocation/codes/SensorRecordSpecificChannels/src/com/hao/sr/SensorRecordSpecificChannels.java这个源码中的，可以搜索iw dev wlan0 scan freq 2412 2437 2452 这句话。其次如果需要修改iw的功能的话，可以修改wifilocation/codes/SensorRecordSpecificChannels/jni/libnl-3-android/lib/iw里面的源码，大部分情况下需要修改的是iw.c，scan.c这两个文件。

--Client-debug.apk 这个就是通过socket和服务器通信的客户端程序，服务器的ip和端口号在源码wifilocation/codes/Client/src/com/hao/smploc/Connect.java中进行设置，目前的设置ip为192.168.1.100，端口号为9999。相应的服务器端的端口在wifilocation/scripts/server/server.py里面设置。


scripts目录下面是python脚本，包括可视化分析以及服务器端的程序。要试用scripts/analysis目录底下的脚本，推荐先安装pythonxy，然后从 https://github.com/imlinhao/wifilocation-datasets 下载我们的数据集，把数据集放到scripts/datasets里面，然后命令行运行相应python脚本即可。

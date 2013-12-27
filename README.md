To execute our codes properly, you need to install python 2.7+, matplotlib, numpy. The easiest way to install them is using pythonxy avialibe at http://code.google.com/p/pythonxy/wiki/Downloads.


The datasets is in https://github.com/imlinhao/wifilocation-datasets

You need download the datasets and place it under the scipts folder, to execute the code in scipts properly.

apks目录下是已编译的android程序, codes目录下为其相对应的android工程

--SensorRecord-debug.apk 所有信息都会记录在sdcard的sensorrecord目录底下。记录了手机型号的基本信息，以及声音、GPS、WiFi、加速度、陀螺仪、光照、湿度、温度、靠近传感器的数据，界面上的两个按钮，点击之后会记录点击按钮的时间。需要说明的是声音数据采用8kHz采样，PCM16位编码的方式进行记录，可以使用GoldWave等相关音频处理程序进行音频的回放。此外点击普通按钮记录下来的时间放在了movetime.txt里面，点击开关按钮记录下来的时间放在了stilltime.txt里面。

scripts目录下面是python脚本，包括可视化分析以及服务器端的程序。要试用scripts/analysis目录底下的脚本，推荐先安装pythonxy，然后从 https://github.com/imlinhao/wifilocation-datasets 下载我们的数据集，把数据集放到scripts/datasets里面，然后命令行运行相应python脚本即可。

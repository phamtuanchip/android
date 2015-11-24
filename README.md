# android
Mo mot doi tuong can xu ly se co 1 tham chieu.
Neu lam theo cach 1 thi no la ID (integer)
Neu lam theo cach 2 thi khong can ID

fix ver 7

    Go to your project in the navigator, right click on properties.

    Go to the Java Build Path tab on the left.

    Go to the libraries tab on top.

    Click add external jars.

    Go to your ADT Bundle folder, go to sdk/extras/android/support/v7/appcompat/libs.

    Select the file android-support-v7-appcompat.jar

    Go to order and export and check the box next to your new jar.

    Click ok.

	Ung dung nhom 8
	
	Tao ung dung ghi chu, su dung sqllite tren android 

	 adb connect {your ip address}

Example

	in android virtual machine Alt + f1 to go to console 
	in android virtual machine Alt + f7 to exit console 
	adb kill-server
    adb connect 192.168.1.51:5555 for virtual machine 

Note: if adb is not running or responding you can do following

adb kill-server

adb start-server 

you can check devices connected to adb

adb devices


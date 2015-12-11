ipk file downloader 
http://www.apkmirror.com
# Build with maven
maven 3.2.3
repository 



#Bai tap nhom 8
- Nghien cuu va viet 1 ung dung de lam viec voi du lieu tran android 
- Nhom chon viet ung dung quan ly ghi chu (note)
- Cac chuc nang
- Them, bot, sua xoa ghi chu
- Chup anh them vao ghi chu
- Them thong tin ban do vao ghi chu 
- Nhac lich khi den ngay 

- Co so du lieu su dung SQL Lite trong Android 
- Cong cu phat trien: eclipse hoac android studio 
- Nen tang cung cap tu android 4.0 tro len

#Huong dan cai dat 

- Nhan vao download source code o day: https://github.com/phamtuanchip/android/archive/master.zip
- download ve va unzip thu muc android-master 
- downloa eclopse o day https://drive.google.com/file/d/0Bw2eZ8CfkgNBZ3c2VzgxeWJZOGs/view?usp=sharing
- unzip eclipse-android ben trong
- download android SDK tai day: http://developer.android.com/sdk/index.html#Other
- download java 7 SDK o day: http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-windows-i586.exe
- cai dat java SDK
- tao ra bien JAVA_HOME tro vao thu muc da cai JDK (C:\Program file\java\jdk1.7)
- cai dat android SDK
+ Lan dau tien chay eclipse se hoi duong dan tro vao Android SDK 

+ Sau do import project android-master/com.thanhgiong.note8 vao work space de lam viec
+ Ung dung thiet ke tai file nay android-master/note8_mockup.pdf 





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


adb connect 10.225.3.151:5555

you can check devices connected to adb

adb devices


=============================

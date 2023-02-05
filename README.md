# Java-app-installer

### How to use (For mass distributing)
Download the zip for the latest release from the 'releases' tab, then extract it on your usb. Once you do that, it is recommended for you to add a shortcut to the program on your flash drive so that the client can locate the app easier. The final neccesary thing to do is The app will probably be blocked so you may have to transfer it to a directory on the client's computer that apps can run (see <a href="https://github.com/api0cradle/UltimateAppLockerByPassList">this</a> for bypassing applocker). This process will be autonomized in the future via an HTA, which can be run, even with AppLocker turned on. For the client app to be able to detect your usb, create a text file named the value of USB_DETECTOR_NAME in appConfig.java (default is "usbDetect.txt")

### How to use (For single client)
Download the zip for the latest release from the 'releases' tab, then extract it to a directory where you are allowed to run apps, (see <a href="https://github.com/api0cradle/UltimateAppLockerByPassList">this</a>). Run jApps.exe and install whatever.

<hr>

### Create/compile your own custom distribution

#### Create your own distribution
To add your own apps, modify the apps.json file to add your own apps. The apps.json file gives the parameters for the constructors in the Item class. There are already several examples in the file so look at that for help also. You can also modify other important things you may want to change is in the appConfig class such as the install directory and such.

#### Create an executable for your new distribution
Compile your program and create a jar file with your IDE, then use a program like <a href="https://github.com/libgdx/packr">packr</a> or <a href="https://sourceforge.net/projects/launch4j/">launch4j</a> to create the exe (<b>note</b>: launch4j is easier to use, see this for <a href="https://launch4j.sourceforge.net/docs.html">how to use launch4j</a>  and this for <a href="https://stackoverflow.com/questions/7071133/how-to-bundle-a-jre-with-launch4j"> including the jre in your exe for launch4j</a>) Other jvm packagers will be a similar process but in this example, I'll be using packr.

Create a JSON named config.json and put it whereever. Unless you've made massive changes, you shouldn't have to change much
```
{
    "platform": "windows64",
    "jdk": "C:/Program Files/Java/YOUR_JDK_VERSION!!!!",
    "executable": "jApps",
    "classpath": [
        "C:/PATH/TO/YOUR/JAR/jApps2.jar"
    ],
    "mainclass": "components.jApps",
    "vmargs": [
       "Xmx1G"
    ],
    "minimizejre": "hard",
    "output": "jApps"
}
```
<br>

Now run this cmd script in the same directory


```java -jar packr-all-4.0.0.jar config.json```

You now have your exe! You may want to create/download a miniature jre and replace the current one in the folder since your app is likely 200 MB+

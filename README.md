# CSCI_6221_SCALA
Git repo for Group Scala  
# 1.Environment      
    Windows 10 and above
    Scala 2.12.15  
    Spark 3.2.2  
    Hadoop 3.3.1
    sbt 1.8.0
To download the exact environment we use this link:  
https://drive.google.com/drive/folders/1bn8ApUKe88JQPCNB5JuZSP8xtqjZdI0m?usp=share_link  
Hadoop file is huge(about 1.2G), we did so modify for windows system. If you want to reproduce this project, pleas use windows instead of linux.
## 1.1 Development Environment Set Up
1. Download the zip file in the drive, unzip it in the directory without space in the directory name
2. Update the environment variables
    ```
    SCALA_HOME = your_environment_directory/Scala3  
    SPARK_HOME = your_environment_directory/Spark  
    JAVA_HOME = your_environment_directory/Java8  
    HADOOP_HOME = your_environment_directory/Hadoop
    ```
    **Append these to Path:**   
    ```
    %SPARK_HOME%\bin
    %HADOOP_HOME%\bin
    %JAVA_HOME%\bin
    %JAVA_HOME%\jre\bin
    %SCALA_HOME%\bin
    %SCALA_HOME%\jre\bin
    ```
1. Use these commands to check your environment in cmd  
    1. java -version  
    2. scala -version  
    3. spark-shell --version 
    4. hadoop -version
    
## 1.2 Project Library 
To write code using our ml API, you need to add library. We use sbt (Scala build tool) in this project.
You can import this project into IntelliJ IDEA and use that to set up the coding environment automatically.
Or you can add the %SPARK_HOME%\jar to your library and it should contain all the jars this project need.

# 2.Quick Test
Compile the project using these sbt command  
   ```
   sbt compile
   sbt test:compile
   ```
## 2.1 Using GUI
Step 1: Run GUI.Scala
Step 2: if you wish to create a master on Apache Spark just click create master if you wish to add yourself
as a worker of another master just simply type the master IP address of another user and click add worker.
if your master don't know what's his/her master IP just look at the first line of GUI which tells you what 
your IP address is.
Step 3: after all works are done just simply press exit to exit either master or worker state otherwise the 
program will always run at backstage and will never stop waste your computer's resources, therefore please 
press exit key instead of just close the window on the right up cornor.
Thank you for using Distributed Machine Learning Apache Spark easy connection system.
Use our GUI to start master and workers.Run startGUI in src/test/scala/com/wic/startGUI


## 2.2 Run simple test

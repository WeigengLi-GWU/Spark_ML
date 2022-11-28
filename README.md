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
Use our GUI to start master and workers.Run startGUI in src/test/scala/com/wic/startGUI


## 2.2 Run simple test

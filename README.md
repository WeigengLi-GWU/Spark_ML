# CSCI_6221_SCALA
Git repo for Group Scala  
# Environment      
    Windows 10 and above
    Scala 3.2.0  
    Spark 3.2.2  
    Hadoop 3.3.1  
To download the exact environment we use this link:  
https://drive.google.com/drive/folders/1bn8ApUKe88JQPCNB5JuZSP8xtqjZdI0m?usp=share_link  
Hadoop file is huge(about 1.2G), we did so modify for windows system. If you want to reproduce this project, pleas use windows instead of linux.
## How to build the environment 
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
    


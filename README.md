# CSCI_6221_SCALA
Git repo for Group Scala  
# Environment      
    Windows 10  
    Scala 3.2.0  
    Spark 3.2.2  
    Hadoop 3.3.1  
To download the exact environment we use this link:  
https://drive.google.com/drive/folders/1bn8ApUKe88JQPCNB5JuZSP8xtqjZdI0m?usp=share_link  
Hadoop file is huge(about 1.2G), we did so modify for windows system. If you want to reproduce this project, pleas use windows instead of linux.
## How to build the environment 
1. Download the zip file in the drive, unzip it in the directory without space in the directory name
2. Update the environment variables 
    SCALA_HOME = your_environment_directory/Scala3  
    SPARK_HOME = your_environment_directory/Spark  
    JAVA_HOME = your_environment_directory/Java8  
    HADOOP_HOME = your_environment_directory/Hadoop  
    **Append these to Path:**   
&ensp;&ensp;%SCALA_HOME%\bin;  
&ensp;&ensp;%SPARK_HOME%\bin;  
&ensp;&ensp;%HADOOP_HOME%\bin;  
&ensp;&ensp;%JAVA_HOME%\bin;  
&ensp;&ensp;%JAVA_HOME%\jre\bin;  
&ensp;&ensp;%SCALA_HOME%\bin;  
&ensp;&ensp;%SCALA_HOME%\jre\bin;  
1. Use these commands to check your environment in cmd  
&ensp;1. java -version  
&ensp;2. scala -version  
&ensp;3. spark-shell --version 
&ensp;4. hadoop -version
    


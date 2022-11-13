package com.project.conf
import com.project.conf.ip_tracker
class Auto_Hive_Conf{
    def write_master_nodes_bat():Unit={
        var ip = ip_tracker()
        print(ip)
    }
}

object Test {
   def main(args: Array[String]):Unit={
    var a= new Auto_Hive_Conf()
    a.write_master_nodes_bat()
   }
}
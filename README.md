# customize_configure
A tool for customize a special configure based on a template and cluster info. This tool was first designed for Cassandra, but now it can also be used for any other line by line customized configure files.

What we want is to edit the configure files of the whole cluster hosts at one place easily and conveniently. To do so, we need a template and the cluster-host-conf-info to be prepared in advance. The program generates configure files for each host according to cluster-host-conf-info.

Template format

Templates are processed line by line. Any parts of the template you want to change can be marked by "@{...json...}@". These marks will be replaced by mannual input or other values specified in host-specific-conf. There's a example as follows:

      original source:
         listen_address: 1.2.3.4
         native_transport_port: 9042

      template:
         listen_address: @{"name":"listen_address", "description":"listen address of Cassandra server", "default-value":"None"}@
         native_transport_port: 9042
   

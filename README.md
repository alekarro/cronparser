## Simple cron expression parser

Java command line application that outputs to console the expanded values for cron expression.

For example, the following input argument: 


*/15 0 1,15 * 1-5 /usr/bin/find 
 
Should yield the following output: 

minute        0 15 30 45
 
hour          0
 
day of month  1 15
 
month         1 2 3 4 5 6 7 8 9 10 11 12
 
day of week   1 2 3 4 5
 
command       /usr/bin/find
 
 

##### Running the example

This project uses the Maven to build the target jar.
 Requirements to build the jar:
   - JDK(minimum 1.8) must be installed and on the path.
   - Maven must be installed and on the path. 
 
To build the jar, in the root of this project, run:
  
  ###### $ mvn clean compile jar:jar** 

To run the jar, in the root of this project, run:
  
   ###### $ java -jar target/expandedcron-1.0.jar "*/15 17 1,3,15 7-12 * /usr/bin/find"
  
    where "*/15 17 1,3,15 7-12 * /usr/bin/find" is a parameter, cron expression 

##### Parameters
You should only consider the standard cron format with five time fields and command field (
**minute, 
hour, 
day of month, 
month, 
day of week, 
command**

and you do not need to pass the special time strings such as "@yearly". The input will be on a single line. 


For all questions feel free to contact me at alero@op.pl.



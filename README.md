# double_submit_cookie_pattern
This is a Simple project to demonstrate how Double Submit Cookie Pattern works.

Steps to run the project.

1.	Clone repository to your machine.
2.	Go to double_submit_cookie_pattern folder. open command prompt and type "mvn clean package" and wait untill build complete.
3.	Type "java -jar target\double_submit_cookie_pattern-0.0.1-SNAPSHOT -Dserver.port=8080" to run the project.(If "java.net.BindException: Address already in use: bind" Exception occured change server port.)
4.	Open browser and navigate to "http://localhost:8080/" .(Username – john | password - abc123)

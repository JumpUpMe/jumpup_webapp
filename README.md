JumpUp.me Application <a href="https://travis-ci.org/JumpUpMe/jumpup_webapp" title="Travis CI"><img src="https://travis-ci.org/JumpUpMe/jumpup_webapp.svg?branch=master" /></a>
=======================

Introduction
------------

JumpUp.me is the car-pooling community of the future. 
We want to change the mobility towards a request-when-you-need-it-model. Both drivers and passengers shall need less time in offering and finding rides.
If a passenger needs a ride from a place to another, JumpUp.me will find the best routes for him. 
If you are the driver, you just tell the system from which place you will drive to another. The system automatically calculates the correct prices, contains a meeting-point-finding system and finds trips that are near several routes of drivers and may contain via-waypoints.


Student project
------------

The idea of this project is to create a completely new backend for a better car pooling experience. Currently, an application prototype written in PHP Zend Framework 2 exists.
We will adapt this backend, but build a new and improved architecture based n J2EE technologies.

- Module: http://bkleinen.github.io/webapplications/index.html
- Studies: International Media and Computing (Master)
- University: HTW Berlin (http://htw-berlin.de)


Old prototype
------------

- Sources: https://github.com/sasfeld/JumpUp 
- Prototype: http://testing.jumpup.groupelite.de/ (Username: testing, Password: JustTesting


Technologies
------------

- Backend: J2EE
- Database Management: Hibernate as JPA implementation with JNDI on a JBoss 7.2
- Frontend: JQuery, Bootstrap, GoogleMap

Testing environment
------------

- http://groupelite.de:8080/jumpup

Installation
------------

1. Prepare the MySQL 5.6.X database
2. Add a database “jumpup_dev”.
3. Add a user “jumpup_dev” with password lzohs{FQvq(U6]?J
4. Create a folder “JumpUp” on the very uper level of your file system, e.g. C:\JumpUp .
5. Create a file “wildfly.log” in JumpUp.
6. Now, clone the WildFly server: 
  ```git clone git@groupelite.de:/home/git/jumpup/jumpup_jboss.git``` 
7. Afterwards, clone the environment configurations repository: 
  ```git clone git@groupelite.de:/home/git/jumpup/jumpup_configurations.git```
8. Now, you are ready to clone the JumpUp web application repository itself: 
	```git clone https://github.com/JumpUpMe/jumpup_webapp.git```
9. (Optionally, if you need to work on the android app): 
  ```git clone https://github.com/JumpUpMe/jumpup_android.git```
10. Make sure to have the following project structure:
  - JumpUp
    - jumpup_android (optionally)
    - jumpup_configurations
    - jumpup_jboss
    - jumpup_webapp
11. On UNIX systems, it might be neccessary to run
	```chmod -R 0744 jumpup_jboss/bin/```
	```chmod 0744 jumpup_configurations/deploy.sh```
12. You are ready to get the webapp running. Run the deployment script:
	```cd jumpup_configurations```
	```./deploy.sh -e development```
13. You can access the webapp on: http://localhost:8080/jumpup/

...

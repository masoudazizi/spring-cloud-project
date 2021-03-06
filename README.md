## Account management project 

I would like you to develop a simple Account Management Service which will be part of a bigger banking ecosystem. For this project, the variety of Spring Cloud components was used to the ability to scale up the services. The architecture of this project is based on microservice.


#### The relations between the modules in the project were shown at the below picture:

![](https://github.com/masoudazizi/spring-cloud-project/blob/master/Page-1-Image-1.jpg)

#### In the above picture I use three different colors for lines that show the relation between components:
- the red lines show the request flow for tracing via selught log key at the log file and tracing the requests among services.
- the green lines show the relation of modules which register themselves to discovery service
- he blue lines show the relation between Currency-exchange and Account-management service via the gateway, this flow is only used for transfer restful service that account-management send the request to currency-exchanges for calculating transfer amount for two accounts with two different funds.



#### The brief description of the modules :

##### account-management  available on the port 8000

 This is the simple account management system. I developed the currency-exchange service for simulating behaviour of  scaling the services, so i used the ribbon technology for client load balancer and feign client to call the exchange service for using in only transfer service, because we need currency exchange only in transfer service. 

Swagger panel : http://localhost:8000/swagger-ui.html


##### currency-exchange available on the port: 8100
I developed this module for providing a solution of scaling the container, i using the third-party service for getting currency-exchange-rate and unfortunately this third-party api sometimes has problems, please try two times for transfer if you get some problem. it’s related to third-party exchange api.

##### accountmanagement-mariadb available on the port: 3306
I use the mariadb for production database 

##### gateway available on the port: 8765
I needed to develop a simple gateway service for provide the scalability of services 

#####  config-server available on the port: 8888
I developed this service for future requirement and it does not specific work in my this architecture

##### naming-server available on the port: 8761
I developed this service for providing discovery service based on Eureka server

##### zipkin available on the port 9411
I used this service for tracing the request among the involved services

##### rabbitmq available on the port: 15672
The gateway, currency-exchange and account-management services are using rabbitmq for sending logs requests to zipkin for tracing.

### Dockerize:
I used the JIB maven plugin for creating docker images, because there are several modules in this project and I am using a simple below command to create all images.

In the root project you can run this command for creating all images

`mvn package -Pprod verify jib:dockerBuild
`

##### Docker-compose:
I created the docker-compose file in the docker directory of the root project for automating the running of containers.

###### In the docker container run command:
`docker-compose up -d
`

#### The below images show the diagram of account-management tables and their relations

![](https://github.com/masoudazizi/spring-cloud-project/blob/master/Page-4-Image-2.jpg)


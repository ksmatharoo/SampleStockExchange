This is Simple Maven Project using spring boot to add web interfaces

#Build with test 
mvn clean package

#Build without Test
mvn clean package -DskipTests

#Run application
java -jar SampleStockExchange-1.0.0-SNAPSHOT.jar

#Test Cases
org.ksm.exchange.service.StockServiceImplTest
org.ksm.exchange.service.TradeServiceImplTest

#exception
org.ksm.exchange.exception.ExchangeException
org.ksm.exchange.exception.ExchangeExceptionHandler

#API
ViewMe.png
or
all api can be seen in swagger 

#Notes
All web rest points can we views using swagger ui which is running at following URL
http://localhost:8080/swagger-ui.html#


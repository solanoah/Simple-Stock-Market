# Simple Stock Market

## Problem Statement

####Provide working source code that will:-

* For a given stock,
* * Given any price as input, calculate the dividend yield
* * Given any price as input, calculate the P/E Ratio
* * Record a trade, with timestamp, quantity of shares, buy or sell indicator and
* * Calculate Volume Weighted Stock Price based on trades in past 5 minutes
* Calculate the GBCE All Share Index using the geometric mean of prices for all stocks traded price

## Assumptions:

* Stocks are unique by Symbol and Type (Common and Preferred)
* P/E ratio is Price divided by Dividend yield
* The 5 minutes duration Weighted Stock price calculation is fixed but could be made configurable
* Stock trade creation seizes after the Weighted Stock price calculation
* Trade price is random between 1 and 100
* Quantity is also random between 100 and 1000
* Fixed Dividend is only applicable to Preferred Stock

## Patterns Used:

### Dependency Injection

Spring framework has been used achieve IoC needed for separation of concerns and a more robust testing of my code. Spring provides a container for dependent components needed by the calculation service i.e. StockRepository and TradeRepository.

### Repository

This is intended to create an abstraction layer between the in-memory data access layer (StockRepository and TradeRepository)
the business logic layer (CalculationService) of an application.

```
These patterns helped me to insulate the application from changes in the data store and can facilitated automated unit testing or test-driven development (TDD).
```

### Multithreading

This was introduce for performance and useful should we want to be accepting continuous input while processing GBCE and weighted stock price on another hand.
It was included in order to demonstrate my knowledge of concurrency that will be needed for this role.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

## Prerequisites

You need to install the following tools and why
* [Java 1.8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) - Lambda and Annotation were used in the code
* [Maven](https://maven.apache.org/) - Dependency Management
* [JaCoCo](http://www.eclemma.org/jacoco/trunk/doc/maven.html) - Code coverage

## Building the code:

* Get latest source from github
* From the terminal (MAC) navigate to the root location of the project, where the POM is.

```
mvn clean package ---> to clean and package the jar
```

## Running the App

```
mvn exec:java -Dexec.mainClass=com.jpmorgan.stockmarket.AppEntry
```

## Running the tests

```
mvn test
```

Test CalculateWeightedStockPrice_After_5mins_should_have_no_more_trade_Test takes 5 mins to run, please remove the @Ignore annotation in order to test.

## Code Coverage
JaCoCo has been used to demonstrate the importance of test coverage as a best practice in every software development.

```
Navigate to .../target/site/jacoco/index.html
```

